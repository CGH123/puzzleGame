package com.example.administrator.puzzleGame.game3DModel;


import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.administrator.puzzleGame.util.VectorUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Quad extends ShaderBody implements Piece {
    Vector2f[] points;
    Vector2f[] texturePoints;
    int vCount;
    int isCheck;
    int num;
    int texId;

    public Quad(int num, Vector2f[] points, int texId) {
        super(0);
        vCount = 6;
        this.num = num;
        this.points = points;
        this.texId = texId;

        float scale = 1.0f / 3.0f;
        float translate = 0.5f;
        texturePoints = new Vector2f[4];
        for (int i = 0; i < 4; i++) {
            texturePoints[i] = points[i].multiK(scale).add(new Vector2f(translate, translate));
            texturePoints[i].y = 1 - texturePoints[i].y;
        }

        initData();
        initBuffer();
    }

    @Override
    public float pickUpTime(float x, float y) {
        float[] AB = MatrixState.getUnProject(x, y);
        float result = Float.POSITIVE_INFINITY;
        for (int i = 0; i < vertices.length; i += 9) {
            float[][] triangle = new float[3][3];
            for (int j = 0; j < 3; j++) {
                float[] coordinate = new float[4];
                //求变换前的点
                Matrix.multiplyMV(
                        coordinate, 0,
                        super.getMatrix(), 0,
                        new float[]{vertices[i + j * 3], vertices[i + j * 3 + 1], vertices[i + j * 3 + 2], 1}, 0
                );
                triangle[j][0] = coordinate[0];
                triangle[j][1] = coordinate[1];
                triangle[j][2] = coordinate[2];
            }

            float t = VectorUtil.IntersectTriangle(
                    new float[]{AB[0], AB[1], AB[2]},
                    new float[]{AB[3], AB[4], AB[5]},
                    triangle[0], triangle[1], triangle[2]
            );
            if (t != -1)
                result = Math.min(result, t);
        }
        return result;
    }

    @Override
    public void initData() {
        //顶点坐标数据的初始化================begin============================
        vertices = new float[]{
                points[0].x * 2, points[0].y * 2, 0, points[1].x * 2, points[1].y * 2, 0, points[2].x * 2, points[2].y * 2, 0,
                points[3].x * 2, points[3].y * 2, 0, points[2].x * 2, points[2].y * 2, 0, points[1].x * 2, points[1].y * 2, 0,
        };


        //法向量数据初始化
        normals = new float[vertices.length];
        for (int i = 0; i < normals.length; i += 3) {
            normals[i] = 0;
            normals[i + 1] = 0;
            normals[i + 2] = 1;
        }

        texCoors = new float[]{
                texturePoints[0].x, texturePoints[0].y, texturePoints[1].x, texturePoints[1].y, texturePoints[2].x, texturePoints[2].y,
                texturePoints[3].x, texturePoints[3].y, texturePoints[2].x, texturePoints[2].y, texturePoints[1].x, texturePoints[1].y
        };

    }


    @Override
    public void drawSelf(int texId) {
        setBody();

        //制定使用某套shader程序
        GLES20.glUseProgram(mProgram);
        //将最终变换矩阵传入shader程序
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //将位置、旋转变换矩阵传入shader程序
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
        //将摄像机位置传入shader程序
        GLES20.glUniform3fv(muCameraHandle, 1, MatrixState.cameraFB);
        //将光源位置传入shader程序
        GLES20.glUniform3fv(muLightLocationHandle, 1, MatrixState.lightPositionFB);
        //将是否选择传入shader程序
        GLES20.glUniform1i(muIsCheck, isCheck);

        //传送顶点位置数据
        GLES20.glVertexAttribPointer(
                maPositionHandle,
                3,
                GLES20.GL_FLOAT,
                false,
                3 * 4,
                mVertexBuffer
        );

        //传送顶点纹理坐标数据
        GLES20.glVertexAttribPointer(
                maTexCoorHandle,
                2,
                GLES20.GL_FLOAT,
                false,
                2 * 4,
                mTexCoorBuffer
        );

        //传送顶点法向量数据
        GLES20.glVertexAttribPointer(
                maNormalHandle,
                4,
                GLES20.GL_FLOAT,
                false,
                3 * 4,
                mNormalBuffer
        );


        //启用顶点位置数据
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        //启用顶点纹理数据
        GLES20.glEnableVertexAttribArray(maTexCoorHandle);
        //启用顶点法向量数据
        GLES20.glEnableVertexAttribArray(maNormalHandle);

        //绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);


        //绘制纹理矩形
        MatrixState.pushMatrix();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
        MatrixState.popMatrix();
    }
}
