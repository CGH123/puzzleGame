package com.example.administrator.puzzleGame.gameModel;


import java.util.ArrayList;

public class Quad extends ObjectAbstract {
    int row, col;
    Vector2f[] points;
    int texId;
    float size;

    public Quad(float scale, float size, Vector2f[] points, int texId) {
        super(size * scale / 2, size * scale / 2, 0);
        row = (int) points[points.length - 1].x;
        col = (int) points[points.length - 1].y;
        this.size = size;
        this.points = points;
        this.texId = texId;
        this.scale = scale;
        initPieces();
    }

    @Override
    public void initPieces() {
        pieces = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int startPos = i * (col + 1) + j;
                Vector2f[] quadPoints = new Vector2f[]{
                        new Vector2f(points[startPos]),
                        new Vector2f(points[startPos + 1]),
                        new Vector2f(points[startPos + col + 1]),
                        new Vector2f(points[startPos + col + 2])};
                Boolean isOutPlane = false, isAllOutPlane = true;
                float range = size / 2;
                for (int k = 0; k < 4; k++) {
                    if (!(quadPoints[k].x >= range || quadPoints[k].y >= range || quadPoints[k].x <= -range || quadPoints[k].y <= -range)) {
                        isAllOutPlane = false;
                    }
                }
                for (int k = 0; k < 4; k++) {
                    if (quadPoints[k].x > range || quadPoints[k].y > range || quadPoints[k].x < -range || quadPoints[k].y < -range) {
                        isOutPlane = true;
                        if (quadPoints[k].x > range || quadPoints[k].y > range) {
                            quadPoints[k].x = Math.min(quadPoints[k].x, size / 2);
                            quadPoints[k].y = Math.min(quadPoints[k].y, size / 2);
                        }
                        if (quadPoints[k].x < -range || quadPoints[k].y < -range) {
                            quadPoints[k].x = Math.max(quadPoints[k].x, -range);
                            quadPoints[k].y = Math.max(quadPoints[k].y, -range);
                        }
                    } else
                        isAllOutPlane = false;
                }
                if (!isAllOutPlane) {
                    QuadPiece temp = new QuadPiece(scale, pieces.size(), quadPoints, texId, !isOutPlane);
                    pieces.add(temp);
                }
            }
        }
    }
}
