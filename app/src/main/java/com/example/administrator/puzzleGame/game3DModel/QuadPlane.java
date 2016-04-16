package com.example.administrator.puzzleGame.game3DModel;


public class QuadPlane extends BaseBody implements Object {
    int texId;
    Vector2f[] texturePoints;
    Quad[] quads;

    public QuadPlane(Vector2f[] points, int size, int row, int col, int texId) {
        super.setBox(col * size, row * size, 0);
        this.texturePoints = points;
        this.texId = texId;
        quads = new Quad[row * col];
        for (int i = 0; i < quads.length; i++) {
            quads[i] = new Quad(i, points[i * 4], points[i * 4 + 1], points[i * 4 + 2], points[i * 4 + 3], texId);
        }
    }

    @Override
    public Boolean isPickUpObject(float locationX, float locationY) {
        float time = this.getCurrBox().pickUpTime(locationX, locationY);
        return !Float.isInfinite(time);
    }

    @Override
    public int pickUpPiece(float x, float y) {
        float minTime = Float.POSITIVE_INFINITY;
        int resultNum = 0;
        for (int i = 0; i < quads.length; i++) {
            float time = quads[i].pickUpTime(x, y);
            if (Float.isInfinite(time)) continue;
            if (time < minTime) {
                minTime = time;
                resultNum = i;
            }
        }
        return resultNum;
    }

    @Override
    public void swapPiece(int baseNum1, int baseNum2) {
        int temp = quads[baseNum1].num;
        quads[baseNum1].num = quads[baseNum2].num;
        quads[baseNum2].num = temp;
    }

    @Override
    public Boolean isCompleted() {
        for (int i = 0; i < quads.length; i++) {
            if (quads[i].num != i)
                return false;
        }
        return true;
    }

    @Override
    public void hintPiece(int pieceNum) {
        quads[pieceNum].isCheck = 1 - quads[pieceNum].isCheck;
    }

    @Override
    public void drawSelf() {
        for (int i = 0; i < quads.length; i++) {
            MatrixState.pushMatrix();
            quads[i].drawSelf(texId);
            MatrixState.popMatrix();
        }
    }
}
