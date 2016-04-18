package com.example.administrator.puzzleGame.game2DModel;


import android.graphics.Bitmap;

/**
 * 方格类
 */
public class Piece {
    boolean isblank = false;
    public Bitmap bitmap;
    boolean addString = false;//是否在每个方格上添加图片的原始位置
    public int index;
    /**
     * 方格的宽
     */
    float cellWidth;
    /**
     * 方格的高
     */
    float cellHeight;
    /**
     * 方格的水平偏移量
     */
    float cellOffset_x = 0;
    /**
     * 方格的垂直偏移量
     */
    float cellOffset_y = 0;
    float spaces = 1;

    int serial; // serial number for ordering
    //方格的初始化位置
    int ix; // initial location in grid coordinates
    int iy; // initial location in grid coordinates
    //方格的当前位置
    public int x; // current location in grid coordinates
    public int y; // current location in grid coordinates

    /**
     * @param bitmap 图片
     * @ser int    图片所在序列号
     * @nx int    方格的初始化位置
     * @ny int 方格的初始化位置
     * @cellWidth 方格的宽
     * @cellHeight 方格的高
     * @cellOffset_x 方格相对原点的水平偏移量
     * @cellOffset_y 方格相对原点的垂直偏移量
     */
    Piece(Bitmap bitmap_, int index, int ser, int nx, int ny, float cellWidth, float cellHeight, float cellOffset_x, float cellOffset_y, float spaces) {
        this.cellOffset_x = cellOffset_x;
        this.cellOffset_y = cellOffset_y;
        bitmap = bitmap_;
        serial = ser;
        x = ix = nx;
        y = iy = ny;
        this.cellHeight = cellHeight;
        this.cellWidth = cellWidth;
        this.spaces = spaces;
        this.index = index;
    }
}
