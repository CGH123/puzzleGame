package com.example.administrator.puzzleGame.game3DModel;


import android.graphics.Color;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class QuadPieceLine extends ShaderBody{
    Vector2f[] points;
    int vCount;

    public QuadPieceLine(Vector2f[] points) {
        vCount = 4;
        this.points = points;
        initShader(ShaderManager.getShaderProgram(1));
        initData();
        initBuffer();
    }

    @Override
    public void initBuffer() {
        // vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());// 设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();// 转换为Float型缓冲
        mVertexBuffer.put(vertices);// 向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);// 设置缓冲区起始位置

        // 创建顶点着色数据缓冲
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoors.length * 4);
        cbb.order(ByteOrder.nativeOrder());// 设置字节顺序
        mColorBuffer = cbb.asFloatBuffer();// 转换为Float型缓冲
        mColorBuffer.put(texCoors);// 向缓冲区中放入顶点着色数据
        mColorBuffer.position(0);// 设置缓冲区起始位置

    }

    @Override
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
    public void initData() {
        //顶点坐标数据的初始化================begin============================
        vertices = new float[]{
                points[0].x * 2, points[0].y * 2, 0,
                points[1].x * 2, points[1].y * 2, 0,
                points[3].x * 2, points[3].y * 2, 0,
                points[2].x * 2, points[2].y * 2, 0
        };

        texCoors = new float[]{
                1.0f, 1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 1.0f, 0.0f
        };

    }

    public void drawSelf() {
        setBody();
        // 制定使用某套shader程序
        GLES20.glUseProgram(mProgram);
        // 将最终变换矩阵传入shader程序
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,MatrixState.getFinalMatrix(), 0);
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
