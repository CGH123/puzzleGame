package com.example.administrator.puzzleGame.game3DModel;


public class CubeFace extends BaseBody {
    Square[] squares;
    float UNIT_SIZE;
    int cutNum;
    int faceNum;
    int[] texId;

    public CubeFace(int cutNum, float size, int[] texId, int faceNum) {
        this.cutNum = cutNum;
        this.UNIT_SIZE = size;
        this.faceNum = faceNum;
        this.texId = texId;
        squares = new Square[cutNum * cutNum];
        for (int i = 0; i < squares.length; i++)
            squares[i] = new Square(size, faceNum, i);
    }

    public void drawSelf() {
        int count = 0;
        float scale = 1.05f;
        MatrixState.pushMatrix();
        for (float i = UNIT_SIZE * (cutNum - 1); i >= -UNIT_SIZE * (cutNum - 1); i -= 2 * UNIT_SIZE)
            for (float j = UNIT_SIZE * (cutNum - 1); j >= -UNIT_SIZE * (cutNum - 1); j -= 2 * UNIT_SIZE) {
                MatrixState.pushMatrix();
                MatrixState.translate(i*scale, j*scale, 0);
                int squaresNum = cutNum * cutNum;
                squares[count].drawSelf(texId[squares[count].faceNum * squaresNum + squares[count].squareNum]);
                count++;
                MatrixState.popMatrix();
            }
        MatrixState.popMatrix();
    }
}
