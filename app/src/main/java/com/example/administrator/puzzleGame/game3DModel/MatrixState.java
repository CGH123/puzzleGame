package com.example.administrator.puzzleGame.game3DModel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


import android.opengl.Matrix;
import android.opengl.GLU;

import com.example.administrator.puzzleGame.constant.GameConstant;

//存储系统矩阵状态的类
public class MatrixState {
    private static float[] currMatrix;//当前变换矩阵
    public static float[] lightLocation = new float[]{0, 0, 0};//定位光光源位置
    private static float[] mVMatrix = new float[16];//摄像机位置朝向9参数矩阵   
    private static float[] mProjMatrix = new float[16];//4x4矩阵 投影用
    public static float[] mMVPMatrix = new float[16];//摄像矩阵与变换矩阵相乘的模型矩阵
    public static FloatBuffer cameraFB;
    public static FloatBuffer lightPositionFB;

    //保护变换矩阵的栈
    static float[][] mStack = new float[10][16];
    static int stackTop = -1;

    //获取不变换初始矩阵
    public static void setInitStack() {
        currMatrix = new float[16];
        Matrix.setIdentityM(currMatrix, 0);
    }

    //保护变换矩阵
    public static void pushMatrix() {
        stackTop++;
        System.arraycopy(currMatrix, 0, mStack[stackTop], 0, 16);
    }

    //恢复变换矩阵
    public static void popMatrix() {
        System.arraycopy(mStack[stackTop], 0, currMatrix, 0, 16);
        stackTop--;
    }

    //设置沿xyz轴移动
    public static void translate(float x, float y, float z) {
        Matrix.translateM(currMatrix, 0, x, y, z);
    }

    //设置绕xyz轴旋转
    public static void rotate(float angle, float x, float y, float z) {
        Matrix.rotateM(currMatrix, 0, angle, x, y, z);
    }

    // 设置绕xyz缩放
    public static void scale(float x, float y, float z) {
        Matrix.scaleM(currMatrix, 0, x, y, z);
    }

    //设置摄像机
    static ByteBuffer llbb = ByteBuffer.allocateDirect(3 * 4);
    static float[] cameraLocation = new float[3];//摄像机位置

    public static void setCamera
            (
                    float cx,    //摄像机位置x
                    float cy,   //摄像机位置y
                    float cz,   //摄像机位置z
                    float tx,   //摄像机目标点x
                    float ty,   //摄像机目标点y
                    float tz,   //摄像机目标点z
                    float upx,  //摄像机UP向量X分量
                    float upy,  //摄像机UP向量Y分量
                    float upz   //摄像机UP向量Z分量
            ) {
        Matrix.setLookAtM
                (
                        mVMatrix,
                        0,
                        cx, cy, cz,
                        tx, ty, tz,
                        upx, upy, upz
                );
        cameraLocation[0] = cx;
        cameraLocation[1] = cy;
        cameraLocation[2] = cz;

        llbb.clear();
        llbb.order(ByteOrder.nativeOrder());//设置字节顺序
        cameraFB = llbb.asFloatBuffer();
        cameraFB.put(cameraLocation);
        cameraFB.position(0);
    }

    //设置透视投影参数
    public static void setProjectFrustum
    (
            float left,        //near面的left
            float right,    //near面的right
            float bottom,   //near面的bottom
            float top,      //near面的top
            float near,        //near面距离
            float far       //far面距离
    ) {
        Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    //设置正交投影参数
    public static void setProjectOrtho
    (
            float left,        //near面的left
            float right,    //near面的right
            float bottom,   //near面的bottom
            float top,      //near面的top
            float near,        //near面距离
            float far       //far面距离
    ) {
        Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    //获取具体物体的总变换矩阵
    public static float[] getFinalMatrix() {
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, currMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }

    //获取具体物体的变换矩阵
    public static float[] getMMatrix() {
        return currMatrix;
    }


    //设置灯光位置的方法
    static ByteBuffer llbbL = ByteBuffer.allocateDirect(3 * 4);

    public static void setLightLocation(float x, float y, float z) {
        llbbL.clear();

        lightLocation[0] = x;
        lightLocation[1] = y;
        lightLocation[2] = z;

        llbbL.order(ByteOrder.nativeOrder());//设置字节顺序
        lightPositionFB = llbbL.asFloatBuffer();
        lightPositionFB.put(lightLocation);
        lightPositionFB.position(0);
    }

    //三维坐标转二维坐标
    public static float[] getProject(float[] point) {
        float[] win = new float[3];
        int[] mView = {0, 0, GameConstant.WIDTH, GameConstant.HEIGHT};
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, currMatrix, 0);

        GLU.gluProject(point[0], point[1], point[2],
                mMVPMatrix, 0, mProjMatrix, 0, mView, 0, win, 0);

        return win;
    }

    //二维坐标转三维坐标 
    public static float[] getUnProject(float x, float y) {

        float w = GameConstant.WIDTH;// 屏幕宽度
        float h = GameConstant.HEIGHT;// 屏幕高度
        float left = GameConstant.RATIO;//视角left值
        float top = 1;//视角top值
        float near = 10;//视角near值
        float far = 400;//视角far值
        //求视口的坐标中心在原点时，触控点的坐标
        float x0 = x - w / 2;
        float y0 = h / 2 - y;
        //计算对应的near面上的x、y坐标
        float xNear = 2 * x0 * left / w;
        float yNear = 2 * y0 * top / h;
        //计算对应的far面上的x、y坐标
        float ratio = far / near;
        float xFar = ratio * xNear;
        float yFar = ratio * yNear;
        //通过摄像机坐标系中A、B两点的坐标，求世界坐标系中A、B两点的坐标
        float[] A = fromPtoPreP(new float[]{xNear, yNear, -near});
        float[] B = fromPtoPreP(new float[]{xFar, yFar, -far});
        return new float[]{//返回最终的AB两点坐标
                A[0], A[1], A[2],
                B[0], B[1], B[2]
        };
    }

    //获取摄像机矩阵的逆矩阵的方法
    public static float[] getInvertMvMatrix() {
        float[] invM = new float[16];
        Matrix.invertM(invM, 0, mVMatrix, 0);//求逆矩阵
        return invM;
    }

    //通过摄像机变换后的点求变换前的点的方法：乘以摄像机矩阵的逆矩阵
    public static float[] fromPtoPreP(float[] p) {
        //通过逆变换，得到变换之前的点
        float[] inverM = getInvertMvMatrix();//获取逆变换矩阵
        float[] preP = new float[4];
        Matrix.multiplyMV(preP, 0, inverM, 0, new float[]{p[0], p[1], p[2], 1}, 0);//求变换前的点
        return new float[]{preP[0], preP[1], preP[2]};//变换前的点就是变换之前的法向量
    }


}
