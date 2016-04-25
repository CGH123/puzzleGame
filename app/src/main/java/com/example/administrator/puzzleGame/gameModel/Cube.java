package com.example.administrator.puzzleGame.gameModel;


import java.util.ArrayList;

public class Cube extends WholeBody {
    int cutNum;
    int squareNum;
    int faceNum;
    Vector2f[] points;
    int[] texIds;

    public Cube(int cutNum, Vector2f[] points, int[] texIds) {
        super(cutNum, cutNum, cutNum);
        this.cutNum = cutNum;
        this.squareNum = cutNum * cutNum;
        this.faceNum = 6;
        this.points = points;
        this.texIds = texIds;
        initPieces();
    }

    @Override
    public void initPieces() {
        pieces = new ArrayList<>(faceNum * squareNum);
        for (int i = 0; i < faceNum; i++) {
            for (int j = 0; j < cutNum; j++) {
                for (int k = 0; k < cutNum; k++) {
                    int startPos = j * (cutNum + 1) + k;
                    Vector2f[] quadPoints = new Vector2f[]{
                            new Vector2f(points[startPos]),
                            new Vector2f(points[startPos + 1]),
                            new Vector2f(points[startPos + cutNum + 1]),
                            new Vector2f(points[startPos + cutNum + 2])};

                    PieceBody piece = new CubePiece(i * squareNum + j * cutNum + k, quadPoints, texIds[i]);
                    pieces.add(piece);
                }
            }
        }
    }

    @Override
    public void drawSelf() {
        MatrixState.pushMatrix();

        //绘制前小面
        MatrixState.pushMatrix();
        MatrixState.translate(0, 0, cutNum);
        drawFace(0);
        MatrixState.popMatrix();

        //绘制后小面
        MatrixState.pushMatrix();
        MatrixState.translate(0, 0, -cutNum);
        MatrixState.rotate(180, 0, 1, 0);
        drawFace(1);
        MatrixState.popMatrix();

        //绘制上大面
        MatrixState.pushMatrix();
        MatrixState.translate(0, cutNum, 0);
        MatrixState.rotate(-90, 1, 0, 0);
        drawFace(2);
        MatrixState.popMatrix();

        //绘制下大面
        MatrixState.pushMatrix();
        MatrixState.translate(0, -cutNum, 0);
        MatrixState.rotate(90, 1, 0, 0);
        drawFace(3);
        MatrixState.popMatrix();

        //绘制左大面
        MatrixState.pushMatrix();
        MatrixState.translate(cutNum, 0, 0);
        MatrixState.rotate(-90, 1, 0, 0);
        MatrixState.rotate(90, 0, 1, 0);
        drawFace(4);
        MatrixState.popMatrix();

        //绘制右大面
        MatrixState.pushMatrix();
        MatrixState.translate(-cutNum, 0, 0);
        MatrixState.rotate(90, 1, 0, 0);
        MatrixState.rotate(-90, 0, 1, 0);
        drawFace(5);
        MatrixState.popMatrix();

        MatrixState.popMatrix();
    }

    private void drawFace(int i) {
        for (int j = 0; j < squareNum; j++) {
            pieces.get(i * squareNum + j).drawSelf();
        }
    }
}