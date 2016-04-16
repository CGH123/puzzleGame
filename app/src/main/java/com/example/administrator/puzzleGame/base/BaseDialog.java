package com.example.administrator.puzzleGame.base;

/**
 * Created by HUI on 2016-04-05.
 */

import com.example.administrator.puzzleGame.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 对话框基类
 * @author GuoJun
 *
 */
public class BaseDialog extends Dialog implements View.OnClickListener {

    private static BaseDialog mBaseDialog;// 当前的对话框
    public static BaseDialog getDialog(Context context, int titleId, CharSequence message) {
        return getDialog(context, titleId, message, null, null, null, null, null, null);
    }
    public static BaseDialog getDialog(Context context, int titleId, CharSequence message,
                                       CharSequence button1, OnClickListener listener1) {
        return getDialog(context, titleId, message, button1, listener1, null, null, null, null);
    }
    public static BaseDialog getDialog(Context context, int titleId, CharSequence message,
                                       CharSequence button1, OnClickListener listener1, CharSequence button2,
                                       OnClickListener listener2) {
        return getDialog(context, titleId, message, button1, listener1, button2, listener2, null,
                null);
    }
    public static BaseDialog getDialog(Context context, int titleId, CharSequence message,
                                       CharSequence button1, OnClickListener listener1, CharSequence button2,
                                       OnClickListener listener2, CharSequence button3,
                                       OnClickListener listener3) {
        mBaseDialog = new BaseDialog(context);
        if (mBaseDialog.titleAndMessageIsExist(titleId, message)) {
            mBaseDialog.setTitle(titleId);
            mBaseDialog.setMessage(message);
        }
        if (mBaseDialog.buttonIsExist(button1, listener1, button2, listener2, button3, listener3)) {
            mBaseDialog.setButton1(button1, listener1);
            mBaseDialog.setButton2(button2, listener2);
            mBaseDialog.setButton3(button3, listener3);
        }
        mBaseDialog.setCancelable(true);
        mBaseDialog.setCanceledOnTouchOutside(true);
        return mBaseDialog;
    }
    private Button mBtnButton1;// 底部按钮1
    private Button mBtnButton2;// 底部按钮2
    private Button mBtnButton3;// 底部按钮3
    protected Context mContext;// 上下文
    private TextView mHtvMessage;// 内容
    private TextView mHtvTitle;// 标题
    private int mID; // 附加属性

    private LinearLayout mLayoutBottom;// 底部根布局
    private LinearLayout mLayoutContent;// 内容根布局
    private LinearLayout mLayoutRoot;// 总体根布局
    private LinearLayout mLayoutTitle;// 标题根布局

    private LinearLayout mLayoutTop;// 头部根布局

    private OnClickListener mOnClickListener1;// 按钮1的单击监听事件

    private OnClickListener mOnClickListener2;// 按钮2的单击监听事件

    private OnClickListener mOnClickListener3;// 按钮3的单击监听事件

    private View mViewTitleLine;// 标题分割线

    public BaseDialog(Context context) {
        super(context, R.style.Theme_Light_FullScreenDialogAct);
        mContext = context;
        setContentView(R.layout.dialog_generic);
        initViews();
        initEvents();
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    public boolean buttonIsExist(CharSequence button1, OnClickListener listener1,
                                 CharSequence button2, OnClickListener listener2, CharSequence button3,
                                 OnClickListener listener3) {
        if ((button1 != null && listener1 != null) || (button2 != null && listener2 != null)
                || (button3 != null && listener3 != null)) {
            mLayoutBottom.setVisibility(View.VISIBLE);
            return true;
        }
        else {
            mLayoutBottom.setVisibility(View.GONE);
            return false;
        }
    }

    public int getID() {
        return this.mID;
    }

    private void initEvents() {
        mBtnButton1.setOnClickListener(this);
        mBtnButton2.setOnClickListener(this);
        mBtnButton3.setOnClickListener(this);
    }

    private void initViews() {
        mLayoutRoot = (LinearLayout) findViewById(R.id.dialog_generic_layout_root);
        mLayoutTop = (LinearLayout) findViewById(R.id.dialog_generic_layout_top);
        mLayoutTitle = (LinearLayout) findViewById(R.id.dialog_generic_layout_title);
        mHtvTitle = (TextView) findViewById(R.id.dialog_generic_htv_title);
        mViewTitleLine = findViewById(R.id.dialog_generic_view_titleline);
        mLayoutContent = (LinearLayout) findViewById(R.id.dialog_generic_layout_content);
        mHtvMessage = (TextView) findViewById(R.id.dialog_generic_htv_message);
        mLayoutBottom = (LinearLayout) findViewById(R.id.dialog_generic_layout_bottom);
        mBtnButton1 = (Button) findViewById(R.id.dialog_generic_btn_button1);
        mBtnButton2 = (Button) findViewById(R.id.dialog_generic_btn_button2);
        mBtnButton3 = (Button) findViewById(R.id.dialog_generic_btn_button3);
        mLayoutRoot.setVisibility(View.VISIBLE);
        setTitleLineVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_generic_btn_button1:
                if (mOnClickListener1 != null) {
                    mOnClickListener1.onClick(mBaseDialog, 0);
                }
                break;

            case R.id.dialog_generic_btn_button2:
                if (mOnClickListener2 != null) {
                    mOnClickListener2.onClick(mBaseDialog, 1);
                }
                break;

            case R.id.dialog_generic_btn_button3:
                if (mOnClickListener3 != null) {
                    mOnClickListener3.onClick(mBaseDialog, 2);
                }
                break;
        }
    }

    public void setButton1(CharSequence text, OnClickListener listener) {
        if (text != null && listener != null) {
            mLayoutBottom.setVisibility(View.VISIBLE);
            mBtnButton1.setVisibility(View.VISIBLE);
            mBtnButton1.setText(text);
            mOnClickListener1 = listener;
        }
        else {
            mBtnButton1.setVisibility(View.GONE);
        }
    }

    public void setButton1Background(int id) {
        mBtnButton1.setBackgroundResource(id);
    }

    public void setButton1Clickable(boolean flag) {
        mBtnButton1.setClickable(flag);
    }

    public void setButton1Text(CharSequence chr) {
        mBtnButton1.setText(chr);
    }

    public void setButton2(CharSequence text, OnClickListener listener) {
        if (text != null && listener != null) {
            mLayoutBottom.setVisibility(View.VISIBLE);
            mBtnButton2.setVisibility(View.VISIBLE);
            mBtnButton2.setText(text);
            mOnClickListener2 = listener;
        }
        else {
            mBtnButton2.setVisibility(View.GONE);
        }
    }

    public void setButton2Background(int id) {
        mBtnButton2.setBackgroundResource(id);
    }

    public void setButton2Clickable(boolean flag) {
        mBtnButton2.setClickable(flag);
    }

    public void setButton2Text(CharSequence chr) {
        mBtnButton2.setText(chr);
    }

    public void setButton3(CharSequence text, OnClickListener listener) {
        if (text != null && listener != null) {
            mLayoutBottom.setVisibility(View.VISIBLE);
            mBtnButton3.setVisibility(View.VISIBLE);
            mBtnButton3.setText(text);
            mOnClickListener3 = listener;
        }
        else {
            mBtnButton3.setVisibility(View.GONE);
        }
    }

    public void setButton3Background(int id) {
        mBtnButton3.setBackgroundResource(id);
    }

    public void setButton3Clickable(boolean flag) {
        mBtnButton3.setClickable(flag);
    }

    public void setButton3Text(CharSequence chr) {
        mBtnButton3.setText(chr);
    }

    /**
     * 填充新布局到内容布局
     *
     * @param resource
     */
    public void setDialogContentView(int resource) {
        View v = LayoutInflater.from(mContext).inflate(resource, null);
        if (mLayoutContent.getChildCount() > 0) {
            mLayoutContent.removeAllViews();
        }
        mLayoutContent.addView(v);
    }

    /**
     * 填充新布局到内容布局
     *
     * @param resource
     * @param params
     */
    public void setDialogContentView(int resource, LinearLayout.LayoutParams params) {
        View v = LayoutInflater.from(mContext).inflate(resource, null);
        if (mLayoutContent.getChildCount() > 0) {
            mLayoutContent.removeAllViews();
        }
        mLayoutContent.addView(v, params);
    }

    public void setID(int id) {
        this.mID = id;
    }

    public void setMessage(CharSequence text) {
        if (text != null) {
            mLayoutContent.setVisibility(View.VISIBLE);
            mHtvMessage.setText(text);
        }
        else {

            mLayoutContent.setVisibility(View.GONE);
        }
    }

    @Override
    public void setTitle(CharSequence text) {
        if (text != null) {
            mLayoutTitle.setVisibility(View.VISIBLE);
            mHtvTitle.setText(text);
        }
        else {
            mLayoutTitle.setVisibility(View.GONE);
        }
    }

    public void setTitleLineVisibility(int visibility) {
        mViewTitleLine.setVisibility(visibility);
    }

    public boolean titleAndMessageIsExist(int titleId, CharSequence message) {
        if (mContext.getString(titleId) == null && message == null) {
            mLayoutTop.setVisibility(View.GONE);
            return false;
        }
        else {
            mLayoutTop.setVisibility(View.VISIBLE);
            return true;
        }
    }
}
