package com.example.administrator.puzzleGame.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.puzzleGame.R;
import com.example.administrator.puzzleGame.adapter.WifiapAdapter;
import com.example.administrator.puzzleGame.base.BaseDialog;
import com.example.administrator.puzzleGame.base.ConnWifiDialog;
import com.example.administrator.puzzleGame.base.WifiapBroadcast;
import com.example.administrator.puzzleGame.constant.GameConstant;
import com.example.administrator.puzzleGame.constant.WifiApConst;
import com.example.administrator.puzzleGame.util.WifiUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HUI on 2016-04-04.
 */
public class WifiapActivity extends Activity implements View.OnClickListener,
        OnScrollListener, AdapterView.OnItemClickListener, WifiapBroadcast.NetWorkChangeListener {

    private static final String TAG = "WifiapActivity";
    private static ApHandler mHandler;
    private boolean isRespond = true;
    private String localIPaddress; // 本地WifiIP
    private Button mBtnBack;
    private Button mBtnNext;
    private ConnWifiDialog mConnWifiDialog; // 连接热点窗口
    private BaseDialog mHintDialog; // 提示窗口
    private LinearLayout mLlApInfo;
    private ListView mLvWifiList;
    private SearchWifiThread mSearchWifiThread;
    private TextView mTvApSSID;
    private TextView mTvStatusInfo;
    private WifiapAdapter mWifiApAdapter;

    private WifiapBroadcast mWifiapBroadcast;

    private ArrayList<ScanResult> mWifiList; // 符合条件的热点列表

    private String serverIPaddres; // 热点IP


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifiap);
        initBroadcast();
        initViews();
        initEvent();
        initAction();

    }

    protected void initViews() {
        mLlApInfo = (LinearLayout) findViewById(R.id.wifiap_lv_create_ok);
        mTvStatusInfo = (TextView) findViewById(R.id.wifiap_tv_wifistatus);
        mTvApSSID = (TextView) findViewById(R.id.wifiap_tv_createap_ssid);
        mLvWifiList = (ListView) findViewById(R.id.wifiap_lv_wifi);
        mBtnBack = (Button) findViewById(R.id.wifiap_btn_back);
        mBtnNext = (Button) findViewById(R.id.wifiap_btn_next);
    }

    protected void initEvent() {
        mWifiList = new ArrayList<ScanResult>();
        WifiUtil temp = new WifiUtil(this);
        temp.startScan();
        List<ScanResult> scanResults = temp.getScanResults();
        mWifiList.addAll(scanResults);
        mWifiApAdapter = new WifiapAdapter(this, mWifiList);
        mLvWifiList.setAdapter(mWifiApAdapter);

        mHandler = new ApHandler();
        mConnWifiDialog = new ConnWifiDialog(this, mHandler);
        mSearchWifiThread = new SearchWifiThread(mHandler);
        mLvWifiList.setOnScrollListener(this);
        mLvWifiList.setOnItemClickListener(this);
        mBtnBack.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
    }

    /**
     * 初始化控件设置
     **/
    protected void initAction() {

        if (!WifiUtil.isWifiConnect() && !WifiUtil.isWifiApEnabled()) { // 无开启热点无连接WIFI
            WifiUtil.OpenWifi();
        }

        if (WifiUtil.isWifiConnect()) { // Wifi已连接
            mTvStatusInfo.setText(getString(R.string.wifiap_text_wifi_connected)
                    + WifiUtil.getSSID());

        }


        if (WifiUtil.isWifiEnabled() && !WifiUtil.isWifiConnect()) { // Wifi已开启，未连接
            mTvStatusInfo.setText(getString(R.string.wifiap_text_wifi_1_0));
        }

        mSearchWifiThread.start();
    }


    /**
     * 动态注册广播
     */
    public void initBroadcast() {
        mWifiapBroadcast = new WifiapBroadcast(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(mWifiapBroadcast, filter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ScanResult ap = mWifiList.get(position);
        if (ap.SSID.startsWith(WifiApConst.WIFI_AP_HEADER)) {
            mTvStatusInfo.setText(getString(R.string.wifiap_btn_connecting) + ap.SSID);
            // 连接网络
            boolean connFlag = WifiUtil.connectWifi(ap.SSID, WifiApConst.WIFI_AP_PASSWORD,
                    WifiUtil.WifiCipherType.WIFICIPHER_WPA);
            if (!connFlag) {
                mTvStatusInfo.setText(getString(R.string.wifiap_toast_connectap_error_1));
                mHandler.sendEmptyMessage(WifiApConst.WiFiConnectError);
            }
        } else if (!WifiUtil.isWifiConnect() || !ap.BSSID.equals(WifiUtil.getBSSID())) {
            mConnWifiDialog.setTitle(ap.SSID);
            mConnWifiDialog.setScanResult(ap);
            mConnWifiDialog.show();
        }
    }

    @Override
    public void WifiConnected() {
        mHandler.sendEmptyMessage(WifiApConst.WiFiConnectSuccess);
    }

    @Override
    public void wifiStatusChange() {
        mHandler.sendEmptyMessage(WifiApConst.NetworkChanged);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mWifiapBroadcast); // 撤销广播
        mSearchWifiThread.stop();
        mSearchWifiThread = null;
        super.onDestroy();
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case OnScrollListener.SCROLL_STATE_IDLE:
                setRespondFlag(true);
                break;
            case OnScrollListener.SCROLL_STATE_FLING:
                setRespondFlag(false); // 滚动时不刷新列表
                break;
            case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                setRespondFlag(false); // 滚动时不刷新列表
                break;
        }
    }

    public void setRespondFlag(boolean flag) {
        isRespond = flag;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    public void refreshAdapter(List<ScanResult> list) {
        mWifiApAdapter.setData(list);
        mWifiApAdapter.notifyDataSetChanged();
    }

    private void getWifiList() {
        mWifiList.clear();
        WifiUtil temp = new WifiUtil(this);
        temp.startScan();
        List<ScanResult> scanResults = temp.getScanResults();
        mWifiList.addAll(scanResults);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            // 返回按钮
            case R.id.wifiap_btn_back:
                finish();
                break;

            // 下一步按钮
            case R.id.wifiap_btn_next:
                GameConstant.IP = WifiUtil.getLocalIPAddress();
                Intent intent = new Intent();
                intent.setClass(WifiapActivity.this, ConnectModeActivity.class);
                startActivity(intent);
                break;

        }
    }

    /**
     * 短暂显示Toast提示(来自res)
     **/
    protected void showShortToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

    /**
     * 短暂显示Toast提示(来自String)
     **/
    protected void showShortToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 定时刷新Wifi列表信息
     */
    class SearchWifiThread implements Runnable {
        private Handler handler = null;
        private boolean running = false;
        private Thread thread = null;

        SearchWifiThread(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            while (!WifiUtil.isWifiApEnabled()) {
                if (!this.running)
                    return;
                try {
                    Thread.sleep(2000); // 扫描间隔
                } catch (InterruptedException e) {
                }
                handler.sendEmptyMessage(WifiApConst.ApScanResult);
            }
        }

        public void start() {
            try {
                this.thread = new Thread(this);
                this.running = true;
                this.thread.start();
            } finally {
            }
        }

        public void stop() {
            try {
                this.running = false;
                this.thread = null;
            } finally {
            }
        }
    }

    private class ApHandler extends Handler {
        public ApHandler() {
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case WifiApConst.ApScanResult: // 扫描Wifi列表
                    if (isRespond) {
                        getWifiList();
                        refreshAdapter(mWifiList);
                    }
                    break;

                case WifiApConst.WiFiConnectSuccess: // 连接热点成功
                    String str = getString(R.string.wifiap_text_wifi_connected)
                            + WifiUtil.getSSID();
                    mTvStatusInfo.setText(str);
                    showShortToast(str);
                    break;

                case WifiApConst.WiFiConnectError: // 连接热点错误
                    showShortToast(R.string.wifiap_toast_connectap_error);
                    break;

                case WifiApConst.NetworkChanged: // Wifi状态变化
                    if (WifiUtil.isWifiEnabled()) {
                        mTvStatusInfo.setText(getString(R.string.wifiap_text_wifi_1_0));
                    } else {
                        mTvStatusInfo.setText(getString(R.string.wifiap_text_wifi_0));
                        showShortToast(R.string.wifiap_text_wifi_disconnect);
                    }

                default:
                    break;
            }
        }
    }
}
