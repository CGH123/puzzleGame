package com.example.administrator.puzzleGame.game3DModel;


import java.util.List;

//物体基类
public abstract class WholeBody extends BaseBody implements Whole {

    List<PieceBody> pieces;

    public WholeBody(float x, float y, float z) {
        super.setBox(x, y, z);
    }

    public abstract void initPieces();

    @Override
    public Boolean isPickUpObject(float locationX, float locationY) {
        float time = this.getCurrBox().pickUpTime(locationX, locationY);
        return !Float.isInfinite(time);
    }

    @Override
    public Boolean isCompleted() {
        for (int i = 0; i < pieces.size(); i++) {
            if (!pieces.get(i).isEqualNum(i))
                return false;
        }
        return true;
    }

    @Override
    public int pickUpPiece(float x, float y) {
        float minTime = Float.POSITIVE_INFINITY;
        int resultNum = -1;
        for (int i = 0; i < pieces.size(); i++) {
            float time = pieces.get(i).pickUpTime(x, y);
            if (Float.isInfinite(time)) continue;
            if (time < minTime) {
                minTime = time;
                resultNum = i;
            }
        }
        return resultNum;
    }

    @Override
    public void swapPiece(int pieceNum1, int pieceNum2) {
        pieces.get(pieceNum1).swap(pieces.get(pieceNum2));
    }

    @Override
    public void hintPiece(int pieceNum) {
        pieces.get(pieceNum).hint();
    }

    @Override
    public int getPiecesSize() {
        return pieces.size();
    }


    @Override
    public void setDrawLine(Boolean isDrawLine){
        for (Piece piece : pieces)
            piece.setDrawLine(isDrawLine);
    }

    @Override
    public void drawSelf() {
        MatrixState.pushMatrix();
        for (Piece piece : pieces)
            piece.drawSelf();
        MatrixState.popMatrix();
    }
}
