package com.example.administrator.puzzleGame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.administrator.puzzleGame.R;
import com.example.administrator.puzzleGame.msgbean.User;
import com.example.administrator.puzzleGame.util.DrawbalBuilderUtil;

import java.util.List;

/**
 * Created by HUI on 2016-04-26.
 */
public class RoomAdapter extends BaseAdapter {

    Context context;
    List<User> data;

    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder = DrawbalBuilderUtil.getDrawbalBuilder(DrawbalBuilderUtil.DRAWABLE_TYPE.SAMPLE_ROUND_RECT_BORDER);


    public void setData(List<User> mData) {
        this.data = mData;
    }

    private class ViewHolder {
        TextView mHtvDevice;
        ImageView mIvAvatar;
        ImageView mIvReady;
    }

    public RoomAdapter(Context context, List<User> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.listitem_players, null, false);
            holder.mHtvDevice = (TextView) convertView.findViewById(R.id.user_item_tv_device);
            holder.mIvAvatar = (ImageView) convertView.findViewById(R.id.user_item_iv_avatar);
            holder.mIvReady = (ImageView) convertView.findViewById(R.id.user_item_iv_isready);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        User people = (User) getItem(position);
        int color = mColorGenerator.getColor(people.name);
        TextDrawable drawable = mDrawableBuilder.build(people.name.substring(0, 1), color);
        holder.mIvAvatar.setImageDrawable(drawable);
        holder.mHtvDevice.setText(people.getName());
        holder.mIvReady.setImageResource(R.drawable.btn_ready);


        return convertView;
    }
}
