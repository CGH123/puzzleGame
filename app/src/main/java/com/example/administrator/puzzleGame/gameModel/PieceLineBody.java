package com.example.administrator.puzzleGame.gameModel;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class PieceLineBody extends BaseBody implements PieceLine {
    int mProgram;//自定义渲染管线着色器程序id
    int maPositionHandle; //顶点位置属性引用
    int muMVPMatrixHandle;//总变换矩阵引用

    int maColorHandle; // 顶点颜色属性引用

    FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
    FloatBuffer mColorBuffer;// 顶点着色数据缓冲

    float[] vertices;
    float[] textures;

    int vCount;


    public PieceLineBody(PieceBody.PieceLineData pieceLineData) {
        this.vertices = pieceLineData.vertices;
        this.textures = pieceLineData.textures;
        vCount = vertices.length / 3;
        initShader(ShaderManager.getShaderProgram(1));
        initBuffer();
    }


    public void initBuffer() {
        // vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());// 设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();// 转换为Float型缓冲
        mVertexBuffer.put(vertices);// 向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);// 设置缓冲区起始位置

        //创建顶点纹理坐标数据缓冲
        ByteBuffer cbb = ByteBuffer.allocateDirect(textures.length * 4);
        cbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mColorBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
        mColorBuffer.put(textures);//向缓冲区中放入顶点着色数据
        mColorBuffer.position(0);//设置缓冲区起始位置

    }

    public void initShader(int Program) {
        mProgram = Program;
        // 获取程序中顶点位置属性引用id
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        // 获取程序中顶点颜色属性引用id
        maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
        // 获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }


    @Override
    public void drawSelf() {
        setBody();
        // 制定使用某套shader程序
        GLES20.glUseProgram(mProgram);
        // 将最终变换矩阵传入shader程序
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        // 为画笔指定顶点位置数据
        GLES20.glVertexAttribPointer(maPositionHandle,
                3,
                GLES20.GL_FLOAT,
                false,
                3 * 4,
                mVertexBuffer
        );
        // 为画笔指定顶点着色数据
        GLES20.glVertexAttribPointer(maColorHandle,
                4,
                GLES20.GL_FLOAT,
                false,
                4 * 4,
                mColorBuffer
        );
        // 允许顶点位置数据数组
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maColorHandle);

        GLES20.glLineWidth(3);//设置线的宽度

        //绘制纹理矩形
        MatrixState.pushMatrix();
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, vCount);
        MatrixState.popMatrix();
    }


}
