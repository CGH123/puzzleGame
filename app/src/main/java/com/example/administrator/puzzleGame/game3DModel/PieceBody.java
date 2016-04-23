package com.example.administrator.puzzleGame.game3DModel;


public abstract class PieceBody extends BaseBody implements Piece {

    PieceFillBody pieceFill;
    PieceLineBody pieceLine;
    Boolean isDrawLine = true;

    public PieceBody() {
    }

    public abstract PieceLineData getPieceLineData(float scale);

    public abstract PieceFillData getPieceFillData(float scale);

    public abstract void PieceLineTransForm();

    @Override
    public void swap(PieceBody piece) {
        this.pieceFill.swap(piece.pieceFill);
    }

    @Override
    public float pickUpTime(float x, float y) {
        return pieceFill.pickUpTime(x, y);
    }

    @Override
    public Boolean isEqualNum(int pieceNum) {
        return pieceFill.isEqualNum(pieceNum);
    }

    @Override
    public void hint() {
        pieceFill.hint();
    }

    @Override
    public void setDrawLine(Boolean isDrawLine) {
        this.isDrawLine = isDrawLine;
    }

    @Override
    public void drawSelf() {
        //绘制纹理矩形
        MatrixState.pushMatrix();
        pieceFill.drawSelf();
        MatrixState.popMatrix();

        if (isDrawLine) {
            MatrixState.pushMatrix();
            PieceLineTransForm();
            pieceLine.drawSelf();
            MatrixState.popMatrix();
        }
    }

    public class PieceLineData {
        float[] vertices;
        float[] textures;

        public PieceLineData(float[] vertices, float[] textures) {
            this.vertices = vertices;
            this.textures = textures;
        }
    }

    public class PieceFillData {
        float[] vertices;
        float[] textures;
        float[] normals;
        int num;
        int texId;
        Boolean cantChoose;

        public PieceFillData(int num, int texId, Boolean cantChoose, float[] vertices, float[] normals, float[] textures) {
            this.num = num;
            this.texId = texId;
            this.cantChoose = cantChoose;
            this.vertices = vertices;
            this.normals = normals;
            this.textures = textures;
        }
    }
}
