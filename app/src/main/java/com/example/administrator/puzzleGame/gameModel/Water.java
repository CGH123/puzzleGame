package com.example.administrator.puzzleGame.gameModel;

import android.opengl.GLES20;

import com.example.administrator.puzzleGame.constant.GameConstant;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

//有波浪效果的纹理矩形
public class Water implements Draw {
    final float WIDTH_SPAN = 4 * 63;//2.8f;//横向长度总跨度
    int mPrograms;//自定义渲染管线着色器程序id
    int muMVPMatrixHandle;//总变换矩阵引用
    int muVPMatrixHandle;//摄像机观察及投影的总变换矩阵引用
    int maPositionHandle; //顶点位置属性引用
    int maTexCoorHandle; //顶点纹理坐标属性引用
    int maStartAngleHandle; //本帧起始角度属性引用
    int muWidthSpanHandle;//横向长度总跨度引用
    int muScreenWidth;//设备屏幕宽度引用
    int muScreenHeight;//设备屏幕高度引用
    int uDYTexHandle;//倒影纹理属性引用id
    int uWaterTexHandle;//水自身纹理属性引用id
    FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
    FloatBuffer mTexCoorBuffer;//顶点纹理坐标数据缓冲
    int vCount = 0;
    float currStartAngle = 0;//当前帧的起始角度0~2PI
    int texIdDY;
    int texIdWater;
    float width;
    float height;

    public Water(int texIdDY, int texIdWater, float width, float height) {
        this.texIdDY = texIdDY;
        this.texIdWater = texIdWater;
        this.width = width;
        this.height = height;
        //初始化顶点坐标与着色数据
        initVertexData();
        //初始化shader
        initShader(ShaderManager.getShaderProgram(2));
        //启动一个线程定时换帧
        new Thread() {
            public void run() {
                while (GameConstant.threadFlag) {
                    currStartAngle += (float) (Math.PI / 16);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


    public void setTexIdDY(int texIdDY) {
        this.texIdDY = texIdDY;
    }

    //初始化顶点坐标与着色数据的方法
    public void initVertexData() {
        final int cols = 32;//12;//列数
        final int rows = 32;//cols*3/4;//行数
        final float UNIT_SIZE = WIDTH_SPAN / cols;//每格的单位长度
        //顶点坐标数据的初始化================begin============================
        vCount = cols * rows * 6;//每个格子两个三角形，每个三角形3个顶点
        float vertices[] = new float[vCount * 3];//每个顶点xyz三个坐标
        int count = 0;//顶点计数器
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                //计算当前格子左上侧点坐标
                float zsx = -UNIT_SIZE * cols / 2 + i * UNIT_SIZE;
                float zsz = UNIT_SIZE * rows / 2 - j * UNIT_SIZE;
                float zsy = 0;

                vertices[count++] = zsx;
                vertices[count++] = zsy;
                vertices[count++] = zsz;

                vertices[count++] = zsx;
                vertices[count++] = zsy;
                vertices[count++] = zsz - UNIT_SIZE;

                vertices[count++] = zsx + UNIT_SIZE;
                vertices[count++] = zsy;
                vertices[count++] = zsz;

                vertices[count++] = zsx + UNIT_SIZE;
                vertices[count++] = zsy;
                vertices[count++] = zsz;

                vertices[count++] = zsx;
                vertices[count++] = zsy;
                vertices[count++] = zsz - UNIT_SIZE;

                vertices[count++] = zsx + UNIT_SIZE;
                vertices[count++] = zsy;
                vertices[count++] = zsz - UNIT_SIZE;
            }
        }
        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为Float型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置
        //顶点纹理坐标数据的初始化================begin============================
        float texCoor[] = generateTexCoor(cols, rows);
        //创建顶点纹理坐标数据缓冲
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length * 4);
        cbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mTexCoorBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
        mTexCoorBuffer.put(texCoor);//向缓冲区中放入顶点着色数据
        mTexCoorBuffer.position(0);//设置缓冲区起始位置
    }

    //初始化shader
    public void initShader(int mPrograms) {
        //基于顶点着色器与片元着色器创建程序
        this.mPrograms = mPrograms;
        //获取程序中顶点位置属性引用  
        maPositionHandle = GLES20.glGetAttribLocation(mPrograms, "aPosition");
        //获取程序中顶点纹理坐标属性引用  
        maTexCoorHandle = GLES20.glGetAttribLocation(mPrograms, "aTexCoor");
        //获取程序中总变换矩阵引用
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mPrograms, "uMVPMatrix");
        //获取程序中摄像机观察及投影总变换矩阵引用
        muVPMatrixHandle = GLES20.glGetUniformLocation(mPrograms, "uVPMatrix");
        //获取本帧起始角度属性引用
        maStartAngleHandle = GLES20.glGetUniformLocation(mPrograms, "uStartAngle");
        //获取横向长度总跨度引用
        muWidthSpanHandle = GLES20.glGetUniformLocation(mPrograms, "uWidthSpan");
        //获取设备屏幕宽度引用    
        muScreenWidth = GLES20.glGetUniformLocation(mPrograms, "swidth");
        //获取设备屏幕高度引用    
        muScreenHeight = GLES20.glGetUniformLocation(mPrograms, "sheight");
        //获取倒影、水自身两个纹理引用
        uDYTexHandle = GLES20.glGetUniformLocation(mPrograms, "sTextureDY");
        uWaterTexHandle = GLES20.glGetUniformLocation(mPrograms, "sTextureWater");
    }

    public void drawSelf() {
        //制定使用某套shader程序
        GLES20.glUseProgram(mPrograms);
        //将最终变换矩阵传入shader程序
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //将摄像机观察及投影变换矩阵传入shader程序
        GLES20.glUniformMatrix4fv(muVPMatrixHandle, 1, false, MatrixState.getFinalVPMatrix(), 0);
        //将本帧起始角度传入shader程序
        GLES20.glUniform1f(maStartAngleHandle, currStartAngle);
        //将横向长度总跨度传入shader程序
        GLES20.glUniform1f(muWidthSpanHandle, WIDTH_SPAN);
        //将设备屏幕高度传入shader程序
        GLES20.glUniform1f(muScreenHeight, height);
        //将设备屏幕宽度传入shader程序
        GLES20.glUniform1f(muScreenWidth, width);
        //将顶点位置数据传入渲染管线
        GLES20.glVertexAttribPointer(
                maPositionHandle,
                3,
                GLES20.GL_FLOAT,
                false,
                3 * 4,
                mVertexBuffer
        );
        //将顶点纹理坐标数据传入渲染管线
        GLES20.glVertexAttribPointer(
                maTexCoorHandle,
                2,
                GLES20.GL_FLOAT,
                false,
                2 * 4,
                mTexCoorBuffer
        );
        //启用顶点位置、纹理坐标数据
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maTexCoorHandle);
        //绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texIdDY);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texIdWater);
        GLES20.glUniform1i(uDYTexHandle, 0);//使用0号纹理
        GLES20.glUniform1i(uWaterTexHandle, 1);
        //执行绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }

    //自动切分纹理产生纹理数组的方法
    public float[] generateTexCoor(int bw, int bh) {
        float[] result = new float[bw * bh * 6 * 2];
        float sizew = 1.0f / bw;//列数
        float sizeh = 0.75f / bh;//行数
        int c = 0;
        for (int i = 0; i < bh; i++) {
            for (int j = 0; j < bw; j++) {
                //每行列一个矩形，由两个三角形构成，共六个点，12个纹理坐标
                float s = j * sizew;
                float t = i * sizeh;

                result[c++] = s;
                result[c++] = t;

                result[c++] = s;
                result[c++] = t + sizeh;

                result[c++] = s + sizew;
                result[c++] = t;


                result[c++] = s + sizew;
                result[c++] = t;

                result[c++] = s;
                result[c++] = t + sizeh;

                result[c++] = s + sizew;
                result[c++] = t + sizeh;
            }
        }
        return result;
    }
}
