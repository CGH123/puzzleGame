package com.example.administrator.puzzleGame.game3DModel;


import android.content.res.Resources;

import com.example.administrator.puzzleGame.util.ShaderUtil;

public class ShaderManager {
    final static int shaderCount = 1;
    final static String[][] shaderName = {
            {"vertexTexture.sh", "fragTexture.sh"}
    };
    static String[] mVertexShader = new String[shaderCount];
    static String[] mFragmentShader = new String[shaderCount];
    static int[] program = new int[shaderCount];

    public static void loadCodeFromFile(Resources r) {
        for (int i = 0; i < shaderCount; i++) {
            //加载顶点着色器的脚本内容
            mVertexShader[i] = ShaderUtil.loadFromAssetsFile(shaderName[i][0], r);
            //加载片元着色器的脚本内容
            mFragmentShader[i] = ShaderUtil.loadFromAssetsFile(shaderName[i][1], r);
        }
    }

    //编译3D物体的shader
    public static void compileShader() {
        for (int i = 0; i < shaderCount; i++) {
            program[i] = ShaderUtil.createProgram(mVertexShader[i], mFragmentShader[i]);
        }
    }

    //这里返回的是普通物体的shader程序
    public static int getShaderProgram(int i) {
        if (i < shaderCount)
            return program[i];
        else
            return -1;
    }
}
