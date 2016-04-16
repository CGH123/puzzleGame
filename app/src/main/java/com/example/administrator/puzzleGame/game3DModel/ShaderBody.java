package com.example.administrator.puzzleGame.game3DModel;


import android.opengl.GLES20;
import java.nio.FloatBuffer;

public class ShaderBody extends  BaseBody{

    public ShaderBody(int program){
        initShader(ShaderManager.getShaderProgram(program));
    }

    int mProgram;//自定义渲染管线着色器程序id
    int maPositionHandle; //顶点位置属性引用
    int maTexCoorHandle; //顶点纹理坐标属性引用
    int maNormalHandle; //顶点法向量属性引用
    int muMMatrixHandle;
    int muMVPMatrixHandle;//总变换矩阵引用
    int muIsCheck;
    int muCameraHandle; //摄像机位置属性引用
    int muLightLocationHandle;//光源位置属性引用

    FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
    FloatBuffer mTexCoorBuffer;//顶点纹理坐标数据缓冲
    FloatBuffer mNormalBuffer;//顶点法向量数据缓冲


    //自定义初始化着色器的initShader方法
    public void initShader(int Program) {
        //基于顶点着色器与片元着色器创建程序
        mProgram = Program;
        //获取程序中顶点位置属性引用id
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中顶点纹理坐标属性引用id
        maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //获取程序中顶点法向量属性引用id
        maNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        //获取位置、旋转变换矩阵引用id
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
        //获取程序中摄像机位置引用id
        muCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
        //获取程序中光源位置引用id
        muLightLocationHandle = GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        //获取程序中光源位置引用id
        muLightLocationHandle = GLES20.glGetUniformLocation(mProgram, "uIsCheck");
    }
}
