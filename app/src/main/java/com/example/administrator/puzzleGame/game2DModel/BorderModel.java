package com.example.administrator.puzzleGame.game2DModel;

import android.app.Application;
import android.graphics.Bitmap;

import java.util.Random;

import com.example.administrator.puzzleGame.sqlServer.GameDB;

/**
 * Created by Administrator on 2016-03-16.
 */
public class BorderModel extends Application {
    public Bitmap orgImage;
    public int rows;  //行数
    public int columns;  //列数
    public Piece blankp; // 空方格类
    public Piece []all;// 所有方格类
    public Piece  [][]grid;// 二维方格类
    public Random rand;// 随机类
    // cell geometry in pixels
    public int cellWidth;// 方格的宽
    public int cellHeight;// 方格的高
    // 窗口的宽度
    private int screenWidth = 0;
    // 窗口的高度
    private int screenHeight = 0;
    // 背景图片的宽度
    private int photoWidth = 0;
    // 背景图片的高度
    private int photoHeight = 0;
    /**
     * 方格里面图片之间的空隙像素大小
     */
    public int spaces = 1;
    /**
     * 屏幕的水平偏移量
     */
    private int screenOffset_x = 0;
    /**
     * 屏幕的垂直偏移量
     */
    private int screenOffset_y = 0;
    /**
     * 图片的水平偏移量
     */
    private int photoOffset_x = 0;
    /**
     * 图片的垂直偏移量
     */
    private int photoOffset_y = 0;
    //Command[] cmd;// 命令数组
    public boolean cheated;

    // public ClassSQLite classSQLite;

    public void setScreenWidth(int screenWidth){
        this.screenWidth=screenWidth;
    }
    public void setScreenHeight(int screenHeight){
        this.screenHeight=screenHeight;
    }

    public  BorderModel(Bitmap bit) {
        this.orgImage = bit;
        System.out.println("BoardModel:初始化拼图信息");
       cheated = false;
        rand = new Random();
        switch (GameDB.Diffcult_choice){
            case GameDB.Diffcult_easy:
                this.rows=3;
                this.columns=3;
                break;
            case GameDB.Diffcult_normal:
                this.rows=3;
                this.columns=4;
                break;
            case GameDB.Diffcult_hard:
                this.rows =4;
                this.columns=4;
                break;
            default:
               this.rows=3;
                this.columns=3;
                break;
        }
        // create the grid arrays
        this.grid = new Piece[columns][rows];
       this.all = new Piece[rows * columns];
        initPhotoData();

    }

    public void initPhotoData() {

       // getPicture(); //初始化图片资源

        System.out.println("更新图片偏移结束。");
        photoHeight = orgImage.getHeight();
        photoWidth = orgImage.getWidth();
        cellWidth = photoWidth / rows;
        cellHeight = photoHeight / columns;

        Bitmap temp;
        int i, j;
        System.out.println("开始获取原图......");
        /**
         * @x 横坐标
         * @y 列坐标
         * */
     int x = 0, y = 0;
        for ( i = 0; i < columns ; ) {
            x = 0;
            for ( j = 0; j < rows; ) {
                //grid[i][j].img ;
                System.out.println(x+","+ y+","+ cellWidth+","+ cellHeight);    //创造图片，切割图片
                temp= Bitmap.createBitmap(
                        orgImage,
                        photoOffset_x+spaces+x, //坐标
                        photoOffset_y+spaces+y,
                        cellWidth-spaces,  //长度和宽度
                        cellHeight-spaces
                );
             grid[i][j]= all[i*rows+j]=new Piece(temp,i*rows+j, i*rows+j, i, j,cellWidth,cellHeight,screenOffset_x,screenOffset_y,spaces);
                x =(++j) * cellWidth;
            }
            y = (++i) * cellHeight;
        }
    }

}
