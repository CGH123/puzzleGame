package com.example.administrator.puzzleGame.util;


import android.graphics.Bitmap;

import com.example.administrator.puzzleGame.game3DModel.Vector2f;

public class BitmapUtil {

    /**
     * 将bitmap分割成col*row数量的规则bitmaps
     *
     * @param src 源bitmap
     * @param col 行
     * @param row 列
     * @return 规则bitmaps
     */
    public static Bitmap[] cutBitmap(Bitmap src, int col, int row) {
        Bitmap[] bitmaps = new Bitmap[col * row];
        int photoHeight = src.getHeight();
        int photoWidth = src.getWidth();
        int cellHeight = photoHeight / col;
        int cellWidth = photoWidth / row;
        //空隙像素大小
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                bitmaps[i * row + j] = Bitmap.createBitmap(
                        src,
                        i * cellWidth, //坐标
                        j * cellHeight,
                        cellWidth,  //长度和宽度
                        cellHeight
                );
            }
        }
        return bitmaps;
    }

    /**
     * 将bitmap按任意四边形分割成col*row数量的不规则bitmaps，并返回纹理映射的坐标
     *
     * @param src           源bitmap
     * @param quadPositions 四边形坐标,按逆时针传入
     * @param colLength     行
     * @param rowLength     列
     * @return 不规则bitmaps得纹理映射坐标
     */
    public static Vector2f[] cutBitmapToQuads(Bitmap src, Vector2f[] quadPositions, float rowLength, float colLength) {
        return buildQuadrangle(quadPositions, rowLength, colLength);
    }


    private static Vector2f[] buildQuadrangle(Vector2f[] quadPositions, float rowLength, float colLength) {
        float xLength = quadPositions[1].x - quadPositions[0].x + quadPositions[2].x - quadPositions[3].x;
        float yLength = quadPositions[3].y - quadPositions[0].y + quadPositions[2].y - quadPositions[1].y;
        int row = (int) (4 * colLength / yLength);
        int col = (int) (4 * rowLength / xLength);


        Vector2f[] v = new Vector2f[(row + 1) * (col + 1) + 1];
        Vector2f vDis01 = quadPositions[0].minus(quadPositions[1]);
        Vector2f vDis32 = quadPositions[3].minus(quadPositions[2]);
        Vector2f vDis03 = quadPositions[0].minus(quadPositions[3]);
        Vector2f vDis12 = quadPositions[1].minus(quadPositions[2]);

        Vector2f vExtend = (((vDis01.add(vDis32)).multiK(col))
                .add((vDis03.add(vDis12)).multiK(row)))
                .multiK(0.5f);

        Vector2f vCurrentRow = vExtend.multiK(-0.5f);
        Vector2f vCurrentCol;

        int index = 0;
        for (int i = 0; i <= row; i++) {
            vCurrentCol = vCurrentRow;
            for (int j = 0; j <= col; j++) {
                v[index++] = vCurrentCol;
                vCurrentCol = vCurrentCol.add(
                        ((i + j) & 1) == 1 ? vDis01 : vDis32
                );
            }
            vCurrentRow = vCurrentRow.add(
                    (i & 1) == 1 ? vDis03 : vDis12
            );
        }
        v[v.length - 1] = new Vector2f(row, col);
        return v;
    }

}
