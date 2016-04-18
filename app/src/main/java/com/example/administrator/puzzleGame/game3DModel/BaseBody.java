package com.example.administrator.puzzleGame.game3DModel;


//物体基类
public abstract class BaseBody implements Draw{
    float[] matrix = new float[16];//仿射变换的矩阵
    Bound box;//仿射变换之前的包围盒

    //设置包围盒
    public void setBox(float x, float y, float z) {
        box = new Bound(x, y, z);
    }

    //更新AABB包围盒
    public Bound getCurrBox() {
        return box;
    }

    //复制变换矩阵
    public void setBody() {
        copyMatrix();
    }

    //复制变换矩阵
    private void copyMatrix() {
        System.arraycopy(MatrixState.getMMatrix(), 0, matrix, 0, 16);
    }

    //得到变换矩阵
    public float[] getMatrix() {
        return matrix;
    }

}
