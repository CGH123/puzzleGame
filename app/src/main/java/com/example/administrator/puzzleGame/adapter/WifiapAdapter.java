package com.example.administrator.puzzleGame.adapter;

import java.util.List;
import java.util.Locale;

import com.example.administrator.puzzleGame.constant.WifiApConst;
import com.example.administrator.puzzleGame.util.WifiUtils;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.puzzleGame.R;

/**
 * Created by HUI on 2016-04-04.
 */
public class WifiapAdapter extends BaseAdapter {
    public static class ViewHolder {
        public TextView desc;
        public ImageView rssi;
        public TextView ssid;
    }
    private boolean isWifiConnected;
    private Context mContext;
    private List<ScanResult> mDatas;

    private LayoutInflater mInflater;

    public WifiapAdapter(Context context, List<ScanResult> list) {
        super();
        this.mDatas = list;
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.isWifiConnected = false;
    }

    @Override
    public int getCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }

    private String getDesc(ScanResult ap) {
        String desc = "";
        if (ap.SSID.startsWith(WifiApConst.WIFI_AP_HEADER)) {
            desc = "专用网络，可以直接连接";
        }
        else {
            String descOri = ap.capabilities;
            if (descOri.toUpperCase(Locale.getDefault()).contains("WPA-PSK")
                    || descOri.toUpperCase(Locale.getDefault()).contains("WPA2-PSK")) {
                desc = "受到密码保护";
            }
            else {
                desc = "未受保护的网络";
            }
        }

        // 是否连接此热点
        if (isWifiConnected) {
            desc = "已连接";
        }
        return desc;
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private int getRssiImgId(ScanResult ap) {
        int imgId;
        if (isWifiConnected) {
            imgId = R.drawable.ic_connected;
        }
        else {
            int rssi = Math.abs(ap.level);
            if (rssi > 100) {
                imgId = R.drawable.ic_small_wifi_rssi_0;
            }
            else if (rssi > 80) {
                imgId = R.drawable.ic_small_wifi_rssi_1;
            }
            else if (rssi > 70) {
                imgId = R.drawable.ic_small_wifi_rssi_2;
            }
            else if (rssi > 60) {
                imgId = R.drawable.ic_small_wifi_rssi_3;
            }
            else {
                imgId = R.drawable.ic_small_wifi_rssi_4;
            }
        }
        return imgId;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ScanResult ap = mDatas.get(position);
        ViewHolder viewHolder = null;
        isWifiConnected = false;
        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.listitem_wifiap, null, false);
            viewHolder.rssi = ((ImageView) convertView.findViewById(R.id.wifiap_item_iv_rssi));
            viewHolder.ssid = ((TextView) convertView.findViewById(R.id.wifiap_item_tv_ssid));
            viewHolder.desc = ((TextView) convertView.findViewById(R.id.wifiap_item_tv_desc));
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (ap.BSSID.equals(WifiUtils.getBSSID())) {
            isWifiConnected = true;
        }

        /*
        if (WifiUtils.isWifiConnect() && ap.BSSID.equals(WifiUtils.getBSSID())) {
            isWifiConnected = true;
        }
        */

        viewHolder.ssid.setText(ap.SSID);
        viewHolder.desc.setText(getDesc(ap));
        Picasso.with(mContext).load(getRssiImgId(ap)).into(viewHolder.rssi);
        return convertView;
    }

    // 新加的一个函数，用来更新数据
    public void setData(List<ScanResult> list) {
        this.mDatas = list;
    }
}

