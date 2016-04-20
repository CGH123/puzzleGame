package com.example.administrator.puzzleGame.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;

public class TextureUtil {

    //通过IO加载图片
    public static Bitmap loadTexture(Context context, int drawableId) {
        InputStream is = context.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try {
            bitmapTmp = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmapTmp;
    }

    public static int initTexture(Bitmap bitmapTmp) {
        //生成纹理ID
        int[] textures = new int[1];
        GLES20.glGenTextures(
                1,          //产生的纹理id的数量
                textures,   //纹理id的数组
                0           //偏移量
        );
        int textureId = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLUtils.texImage2D(
                GLES20.GL_TEXTURE_2D, //纹理类型
                0,
                GLUtils.getInternalFormat(bitmapTmp),
                bitmapTmp, //纹理图像
                GLUtils.getType(bitmapTmp),
                0 //纹理边框尺寸
        );
        bitmapTmp.recycle(); //纹理加载成功后释放图片
        return textureId;
    }

    //生成纹理Id的方法
    public static int initTexture(Context context, int drawableId)
    {
        //生成纹理ID
        int[] textures = new int[1];
        GLES20.glGenTextures
                (
                        1,          //产生的纹理id的数量
                        textures,   //纹理id的数组
                        0           //偏移量
                );
        int textureId=textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
        //ST方向纹理拉伸方式
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);

        //通过输入流加载图片
        InputStream is = context.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try
        {
            bitmapTmp = BitmapFactory.decodeStream(is);
        }
        finally
        {
            try
            {
                is.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        //实际加载纹理
        GLUtils.texImage2D
                (
                        GLES20.GL_TEXTURE_2D,   //纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
                        0,                      //纹理的层次，0表示基本图像层，可以理解为直接贴图
                        bitmapTmp,              //纹理图像
                        0                      //纹理边框尺寸
                );
        //自动生成Mipmap纹理
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        //释放纹理图
        bitmapTmp.recycle();
        //返回纹理ID
        return textureId;
    }



    //生成纹理Id的方法
    public static int initTexture(Bitmap bitmapTmp, boolean isMipmap) {
        //生成纹理ID
        int[] textures = new int[1];
        GLES20.glGenTextures(
                1,          //产生的纹理id的数量
                textures,   //纹理id的数组
                0           //偏移量
        );
        int textureId = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        if (isMipmap) {
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_NEAREST);
        } else {
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        }
        //ST方向纹理拉伸方式
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        //实际加载纹理
        GLUtils.texImage2D(
                GLES20.GL_TEXTURE_2D,   //纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
                0,                      //纹理的层次，0表示基本图像层，可以理解为直接贴图
                bitmapTmp,              //纹理图像
                0                      //纹理边框尺寸
        );
        //自动生成Mipmap纹理
        if (isMipmap) {
            GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        }
        //释放纹理图
        bitmapTmp.recycle();
        //返回纹理ID
        return textureId;
    }
}
