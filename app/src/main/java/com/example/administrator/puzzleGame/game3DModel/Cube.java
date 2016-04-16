package com.example.administrator.puzzleGame.game3DModel;


import com.example.administrator.puzzleGame.constant.GameConstant;

public class Cube extends BaseBody implements Object {
    CubeFace[] cubeFaces;
    float UNIT_SIZE;
    int cutNum;
    int squareNum;
    int faceNum;
    int[] texIds;

    public Cube(int cutNum, float size, int[] texIds) {
        super.setBox(cutNum * size, cutNum * size, cutNum * size);
        this.cutNum = cutNum;
        this.squareNum = cutNum * cutNum;
        this.faceNum = 6;
        this.UNIT_SIZE = size;
        this.texIds = texIds;

        cubeFaces = new CubeFace[faceNum];
        for (int i = 0; i < cubeFaces.length; i++)
            cubeFaces[i] = new CubeFace(cutNum, size, texIds, i);
    }


    /**
     * @param locationX 屏幕坐标X
     * @param locationY 屏幕坐标Y
     * @return 是否拾取
     */
    @Override
    public Boolean isPickUpObject(float locationX, float locationY) {
        float time = this.getCurrBox().pickUpTime(locationX, locationY);
        return !Float.isInfinite(time);
    }

    @Override
    public void hintPiece(int pieceNum) {
        cubeFaces[pieceNum / squareNum].squares[pieceNum % squareNum].isCheck = 1 - cubeFaces[pieceNum / squareNum].squares[pieceNum % squareNum].isCheck;
    }

    @Override
    public int pickUpPiece(float x, float y) {
        float minTime = Float.POSITIVE_INFINITY;
        int resultNum = 0;
        for (int i = 0; i < faceNum; i++) {
            for (int j = 0; j < squareNum; j++) {
                float time = cubeFaces[i].squares[j].pickUpTime(x, y);
                if (Float.isInfinite(time)) continue;
                if (time < minTime) {
                    minTime = time;
                    resultNum = i * squareNum + j;
                }
            }
        }
        return resultNum;
    }

    @Override
    public void swapPiece(int baseNum1, int baseNum2) {
        Square square1 = cubeFaces[baseNum1 / squareNum].squares[baseNum1 % squareNum];
        Square square2 = cubeFaces[baseNum2 / squareNum].squares[baseNum2 % squareNum];

        int tempFace = square1.faceNum, tempSquare = square1.squareNum;
        square1.faceNum = square2.faceNum;
        square2.faceNum = tempFace;
        square1.squareNum = square2.squareNum;
        square2.squareNum = tempSquare;
    }

    @Override
    public Boolean isCompleted() {
        for (int i = 0; i < faceNum; i++) {
            for (int j = 0; j < squareNum; j++) {
                if (cubeFaces[i].squares[j].squareNum != j)
                    return false;
            }
        }
        return true;
    }

    @Override
    public void drawSelf() {
        MatrixState.pushMatrix();

        //绘制前小面
        MatrixState.pushMatrix();
        MatrixState.translate(0, 0, cutNum * UNIT_SIZE * GameConstant.SPACE_SCALE);
        cubeFaces[0].drawSelf();
        MatrixState.popMatrix();

        //绘制后小面
        MatrixState.pushMatrix();
        MatrixState.translate(0, 0, -cutNum * UNIT_SIZE * GameConstant.SPACE_SCALE);
        MatrixState.rotate(180, 0, 1, 0);
        cubeFaces[1].drawSelf();
        MatrixState.popMatrix();

        //绘制上大面
        MatrixState.pushMatrix();
        MatrixState.translate(0, cutNum * UNIT_SIZE * GameConstant.SPACE_SCALE, 0);
        MatrixState.rotate(-90, 1, 0, 0);
        cubeFaces[2].drawSelf();
        MatrixState.popMatrix();

        //绘制下大面
        MatrixState.pushMatrix();
        MatrixState.translate(0, -cutNum * UNIT_SIZE * GameConstant.SPACE_SCALE, 0);
        MatrixState.rotate(90, 1, 0, 0);
        cubeFaces[3].drawSelf();
        MatrixState.popMatrix();

        //绘制左大面
        MatrixState.pushMatrix();
        MatrixState.translate(cutNum * UNIT_SIZE * GameConstant.SPACE_SCALE, 0, 0);
        MatrixState.rotate(-90, 1, 0, 0);
        MatrixState.rotate(90, 0, 1, 0);
        cubeFaces[4].drawSelf();
        MatrixState.popMatrix();

        //绘制右大面
        MatrixState.pushMatrix();
        MatrixState.translate(-cutNum * UNIT_SIZE * GameConstant.SPACE_SCALE, 0, 0);
        MatrixState.rotate(90, 1, 0, 0);
        MatrixState.rotate(-90, 0, 1, 0);
        cubeFaces[5].drawSelf();
        MatrixState.popMatrix();

        MatrixState.popMatrix();
    }
}
