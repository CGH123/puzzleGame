package com.example.administrator.puzzleGame.util;

import com.amulyakhare.textdrawable.TextDrawable;

/**
 *
 * Created by Guojun on 2015/12/9.
 */

public class DrawbalBuilderUtil {
    public enum DRAWABLE_TYPE{
        SAMPLE_RECT, SAMPLE_ROUND_RECT,SAMPLE_ROUND,
        SAMPLE_RECT_BORDER,SAMPLE_ROUND_RECT_BORDER, SAMPLE_ROUND_BORDER
    }

    public static TextDrawable.IBuilder getDrawbalBuilder(DRAWABLE_TYPE type) {// initialize the builder based on the "TYPE"
        TextDrawable.IBuilder mDrawableBuilder = TextDrawable.builder().rect();
        switch (type) {
            case SAMPLE_RECT:
                mDrawableBuilder = TextDrawable.builder()
                        .rect();
                break;
            case SAMPLE_ROUND_RECT:
                mDrawableBuilder = TextDrawable.builder()
                        .roundRect(10);
                break;
            case SAMPLE_ROUND:
                mDrawableBuilder = TextDrawable.builder()
                        .round();
                break;
            case SAMPLE_RECT_BORDER:
                mDrawableBuilder = TextDrawable.builder()
                        .beginConfig()
                        .withBorder(4)
                        .endConfig()
                        .rect();
                break;
            case SAMPLE_ROUND_RECT_BORDER:
                mDrawableBuilder = TextDrawable.builder()
                        .beginConfig()
                        .withBorder(4)
                        .endConfig()
                        .roundRect(10);
                break;
            case SAMPLE_ROUND_BORDER:
                mDrawableBuilder = TextDrawable.builder()
                        .beginConfig()
                        .withBorder(4)
                        .endConfig()
                        .round();
                break;
        }
        return mDrawableBuilder;
    }


}