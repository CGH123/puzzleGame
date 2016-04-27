package com.example.administrator.puzzleGame.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.administrator.puzzleGame.R;

import java.util.List;

/**
 * the adapter of drawable list
 * Created by guojun on 2015/12/13
 */
public class GameProgressAdapter extends RecyclerView.Adapter<GameProgressAdapter.ViewHolder> {
    protected Context context;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;
    private int itemId;
    private OnClickListener onClickListener;
    private List<ListData> dataList;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setGameProgress(int position, float progress){
        if(progress == 1.0f)
            dataList.get(position).progress = "完成";
        else
            dataList.get(position).progress = progress + "%";
    }

    public interface OnClickListener {
        void OnItemClick(int position);
    }

    public GameProgressAdapter(Context context, int itemId, List<ListData> dataList, TextDrawable.IBuilder mDrawableBuilder) {
        this.context = context;
        this.itemId = itemId;
        this.dataList = dataList;
        this.mDrawableBuilder = mDrawableBuilder;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(itemId, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListData item = dataList.get(position);
        int color;
        if (item.color == null)
            color = mColorGenerator.getColor(item.name);
        else
            color = item.color;
        TextDrawable drawable = mDrawableBuilder.build(item.IconText, color);
        holder.imageView.setImageDrawable(drawable);
        holder.view.setBackgroundColor(Color.TRANSPARENT);
        holder.position = position;
        holder.nameView.setText(item.name);
        holder.progressView.setText(item.progress);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View view;
        private ImageView imageView;
        private TextView nameView;
        private TextView progressView;
        public int position;

        private ViewHolder(View view) {
            super(view);
            this.view = view;
            imageView = (ImageView) view.findViewById(R.id.imageView);
            nameView = (TextView) view.findViewById(R.id.textView);
            progressView = (TextView) view.findViewById(R.id.progressView);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onClickListener != null) {
                onClickListener.OnItemClick(position);
            }
        }

    }

    public static class ListData {
        private String name;
        private String progress;
        private String IconText;
        private Integer color;

        public ListData(String data, String iconText) {
            this.name = data;
            this.IconText = iconText;
            this.progress = 0 + "%";
            this.color = null;
        }

        public ListData(String data, String iconText, Integer color) {
            this.name = data;
            this.IconText = iconText;
            this.progress = 0 + "%";
            this.color = color;
        }
    }
}

