package com.example.administrator.puzzleGame.util;


import android.graphics.Bitmap;

public class BitmapUtil {

    public static Bitmap[] cutBitmap(Bitmap src, int columns, int rows) {
        Bitmap[] bitmaps = new Bitmap[columns * rows];
        int photoHeight = src.getHeight();
        int photoWidth = src.getWidth();
        int cellHeight = photoHeight / columns;
        int cellWidth = photoWidth / rows;
        //空隙像素大小
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                bitmaps[i * rows + j] = Bitmap.createBitmap(
                        src,
                        i * cellWidth, //坐标
                        j * cellHeight,
                        cellWidth,  //长度和宽度
                        cellHeight
                );
            }
        }
        return  bitmaps;
    }
}
