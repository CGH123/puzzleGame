package com.example.administrator.puzzleGame.util;


import com.example.administrator.puzzleGame.gameModel.Vector2f;

public class BitmapUtil {

    /**
     * 将bitmap按任意四边形分割成col*row数量的不规则bitmaps，并返回纹理映射的坐标
     *
     * @param quadPositions 四边形坐标,按逆时针传入
     * @param colLength     行
     * @param rowLength     列
     * @return 不规则bitmaps得纹理映射坐标
     */
    public static Vector2f[] cutBitmapToQuads(Vector2f[] quadPositions, float rowLength, float colLength) {
        float xLength = Math.min(quadPositions[1].x - quadPositions[0].x, quadPositions[2].x - quadPositions[3].x);
        float yLength = Math.min(quadPositions[3].y - quadPositions[0].y, quadPositions[2].y - quadPositions[1].y);
        int row = (int) (colLength / yLength);
        int col = (int) (rowLength / xLength);/*
        row = (rowLength % 2 == 0) ? row : row + 1;
        col = (colLength % 2 == 0) ? col : col + 1;*/
        return buildQuads(quadPositions, row, col);
    }


    /**
     * 将bitmap按任意四边形分割成col*row数量的不规则bitmaps，并返回纹理映射的坐标
     *
     * @param quadPositions 四边形坐标,按逆时针传入
     * @param col           行
     * @param row           列
     * @return 不规则bitmaps得纹理映射坐标
     */
    public static Vector2f[] cutBitmapToCubes(Vector2f[] quadPositions, int row, int col) {
        return buildQuads(quadPositions, row, col);
    }

    private static Vector2f[] buildQuads(Vector2f[] quadPositions, int row, int col) {
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
