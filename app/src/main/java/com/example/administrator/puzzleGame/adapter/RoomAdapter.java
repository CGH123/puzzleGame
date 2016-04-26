package com.example.administrator.puzzleGame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.puzzleGame.R;
import com.example.administrator.puzzleGame.msgbean.User;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by HUI on 2016-04-26.
 */
public class RoomAdapter extends BaseAdapter{

    Context mcontext;
    List<User> mData;

    public List<User> getmData() {
        return mData;
    }

    public void setmData(List<User> mData) {
        this.mData = mData;
    }

    private class ViewHolder {
        TextView mHtvDevice;
        ImageView mIvAvatar;
        ImageView mIvReady;
    }

    public RoomAdapter(Context context,List<User> data){
        mcontext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView== null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.listitem_players,null,false);
            holder.mHtvDevice = (TextView)convertView.findViewById(R.id.user_item_tv_device);
            holder.mIvAvatar = (ImageView)convertView.findViewById(R.id.user_item_iv_avatar);
            holder.mIvReady = (ImageView)convertView.findViewById(R.id.user_item_iv_isready);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        User people = (User) getItem(position);
        holder.mHtvDevice.setText(people.getName());
        holder.mIvAvatar.setImageResource(R.drawable.water);
        holder.mIvReady.setImageResource(R.drawable.btn_ready);


/*        User people = (User) getItem(position);
        int avatarId = ImageUtils.getImageID(User.AVATAR + people.getAvatar());
        Picasso.with(mContext).load(avatarId).into(holder.mIvAvatar);
        holder.mHtvName.setText(people.getNickname());
        holder.mLayoutGender.setBackgroundResource(people.getGenderBgId());
        holder.mIvGender.setImageResource(people.getGenderId());
        holder.mHtvTime.setText(DataUtils.getBetweentime(people.getLogintime()));
        holder.mHtvDevice.setText(people.getDevice());
        if(people.getOrder()>0){
            holder.mIvReady.setImageResource(R.drawable.btn_ready);
            holder.mHtvOrder.setText("顺序" + people.getOrder());
        }
        else{
            holder.mIvReady.setImageResource(R.drawable.btn_unready);
            holder.mHtvOrder.setText("");
        }*/


        return convertView;
    }
}
