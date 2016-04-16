package com.example.administrator.puzzleGame.util;

import com.example.administrator.puzzleGame.game3DModel.Vector2f;

//计算三角形法向量的工具类
public class VectorUtil {


    //两点距离
    public static float Length(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    //两点距离
    public static float Length(Vector2f v1, Vector2f v2) {
        return (float) Math.sqrt((v1.x - v2.x) * (v1.x - v2.x) + (v1.y - v2.y) * (v1.y - v2.y));
    }

    //二维向量点乘
    public static float Product(Vector2f v1, Vector2f v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }

    //二维向量是否垂直
    public static boolean isVertical(Vector2f v1, Vector2f v2) {
        if (getDegree(v1, v2) > -Math.sqrt(2.0f) / 2 && getDegree(v1, v2) < Math.sqrt(2.0f) / 2)
            return true;
        else
            return false;
    }

    //二维向量夹角
    public static float getDegree(Vector2f v1, Vector2f v2) {
        if (v2.x == 0 && v2.y == 0)
            return 0;
        else {
            return Product(v1, v2) / (v1.mod * v2.mod);
        }
    }


    //射线与三角形相交的方法
    public static float IntersectTriangle(
            float[] g_nearxyz, float[] g_farxyz,
            float[] V0, float[] V1, float[] V2) {
        float[] edge1 = new float[3];
        float[] edge2 = new float[3];

        edge1[0] = V1[0] - V0[0];
        edge1[1] = V1[1] - V0[1];
        edge1[2] = V1[2] - V0[2];

        edge2[0] = V2[0] - V0[0];
        edge2[1] = V2[1] - V0[1];
        edge2[2] = V2[2] - V0[2];

        float[] dir = new float[3];
        dir[0] = g_farxyz[0] - g_nearxyz[0];
        dir[1] = g_farxyz[1] - g_nearxyz[1];
        dir[2] = g_farxyz[2] - g_nearxyz[2];

        float w = (float) Math.sqrt((double) Math.pow(dir[0], 2.0) + (double) Math.pow(dir[1], 2.0) + (double) Math.pow(dir[2], 2.0));
        dir[0] /= w;
        dir[1] /= w;
        dir[2] /= w;

        float[] pvec = new float[3];
        pvec[0] = dir[1] * edge2[2] - dir[2] * edge2[1];
        pvec[1] = dir[2] * edge2[0] - dir[0] * edge2[2];
        pvec[2] = dir[0] * edge2[1] - dir[1] * edge2[0];

        float det;
        det = edge1[0] * pvec[0] + edge1[1] * pvec[1] + edge1[2] * pvec[2];

        float[] tvec = new float[3];
        if (det > 0) {
            tvec[0] = g_nearxyz[0] - V0[0];
            tvec[1] = g_nearxyz[1] - V0[1];
            tvec[2] = g_nearxyz[2] - V0[2];
        } else {
            tvec[0] = V0[0] - g_nearxyz[0];
            tvec[1] = V0[1] - g_nearxyz[1];
            tvec[2] = V0[2] - g_nearxyz[2];
            det = -det;
        }

        if (det < 0.0001f) return -1;

        float u;
        u = tvec[0] * pvec[0] + tvec[1] * pvec[1] + tvec[2] * pvec[2];

        if (u < 0.0f || u > det)
            return -1;

        float[] qvec = new float[3];
        qvec[0] = tvec[1] * edge1[2] - tvec[2] * edge1[1];
        qvec[1] = tvec[2] * edge1[0] - tvec[0] * edge1[2];
        qvec[2] = tvec[0] * edge1[1] - tvec[1] * edge1[0];

        float v;
        v = dir[0] * qvec[0] + dir[1] * qvec[1] + dir[2] * qvec[2];
        if (v < 0.0f || u + v > det)
            return -1;

        float t = edge2[0] * qvec[0] + edge2[1] * qvec[1] + edge2[2] * qvec[2];
        float fInvDet = 1.0f / det;
        t *= fInvDet;
        u *= fInvDet;
        v *= fInvDet;
        return t;
    }
}
