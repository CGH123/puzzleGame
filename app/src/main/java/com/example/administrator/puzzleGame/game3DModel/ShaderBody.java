package com.example.administrator.puzzleGame.game3DModel;

import java.nio.FloatBuffer;

public abstract class ShaderBody extends  BaseBody{
    int mProgram;//自定义渲染管线着色器程序id
    int maPositionHandle; //顶点位置属性引用
    int maTexCoorHandle; //顶点纹理坐标属性引用
    int maNormalHandle; //顶点法向量属性引用
    int muMMatrixHandle;
    int muMVPMatrixHandle;//总变换矩阵引用
    int muIsCheck;
    int muCameraHandle; //摄像机位置属性引用
    int muLightLocationHandle;//光源位置属性引用

    int maColorHandle; // 顶点颜色属性引用

    FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
    FloatBuffer mTexCoorBuffer;//顶点纹理坐标数据缓冲
    FloatBuffer mColorBuffer;// 顶点着色数据缓冲
    FloatBuffer mNormalBuffer;//顶点法向量数据缓冲

    float[] vertices;
    float[] texCoors;
    float[] normals;


    public abstract void initBuffer();
    public abstract void initShader(int Program);
    public abstract void initData();
}
