package com.example.administrator.puzzleGame.game3DModel;


import com.example.administrator.puzzleGame.constant.GameConstant;

public class Camera {
    float distance;
    float distanceTotal;//up向量和摄像机位置向量合成的模长
    float cx;    //摄像机位置x
    float cy;   //摄像机位置y
    float cz;   //摄像机位置z
    float tx;   //摄像机目标点x
    float ty;   //摄像机目标点y
    float tz;   //摄像机目标点z
    float upx;  //摄像机UP向量X分量
    float upy;//摄像机UP向量Y分量
    float upz; //摄像机UP向量Z分量
    float angelA;    //旋转角
    float angelB;    //旋转角
    float angelC;   //计算up向量和摄像机位置向量构成三角形中的夹角

    public Camera(float distance) {
        this.distance = distance;
        distanceTotal = (float) Math.sqrt(1 + distance * distance);
        cx = 0f;    //摄像机位置x
        cy = 0f;   //摄像机位置y
        cz = distance;   //摄像机位置z
        tx = 0f;   //摄像机目标点x
        ty = 0f;   //摄像机目标点y
        tz = 0f;   //摄像机目标点z
        upx = 0f;  //摄像机UP向量X分量
        upy = 1.0f;//摄像机UP向量Y分量
        upz = 0.0f; //摄像机UP向量Z分量
        angelA = 0;//(float) (Math.PI/4);	//旋转角
        angelB = 0;//(float) (Math.PI/4);	//旋转角
        angelC = (float) Math.atan(1 / distance);
    }

    public void setCamera() {
        cx = (float) (distance * Math.sin(angelA) * Math.cos(angelB));
        cy = (float) (distance * Math.sin(angelB));
        cz = (float) (distance * Math.cos(angelA) * Math.cos(angelB));
        upx = (float) (distanceTotal * Math.cos(angelB + angelC) * Math.sin(angelA) - distance * Math.cos(angelB) * Math.sin(angelA));
        upy = (float) (distanceTotal * Math.sin(angelB + angelC) - distance * Math.sin(angelB));
        upz = (float) (distanceTotal * Math.cos(angelB + angelC) * Math.cos(angelA) - distance * Math.cos(angelB) * Math.cos(angelA));

        //调用此方法计算产生透视投影矩阵
        MatrixState.setProjectFrustum(-GameConstant.RATIO, GameConstant.RATIO, -1, 1, 10, 400);
        // 调用此方法产生摄像机9参数位置矩阵
        MatrixState.setCamera(cx, cy, cz, tx, ty, tz, upx, upy, upz);
    }

    public void rotateCamera(float distanceXMoveP1, float distanceYMoveP1) {
        angelA -= distanceXMoveP1 * GameConstant.CAMERA_SPEED;
        angelB += distanceYMoveP1 * GameConstant.CAMERA_SPEED;
        angelB = Math.max(angelB, (float) (-Math.PI / 2));
        angelB = Math.min(angelB, (float) (Math.PI / 2));
    }

    public void scaleCamera(float scale1, float scale2, float scale3) {
        distance /= (scale1 + scale2 + scale3) / 3;
        distanceTotal = (float) Math.sqrt(1 + distance * distance);
        angelC = (float) Math.atan(1 / distance);
    }
}
