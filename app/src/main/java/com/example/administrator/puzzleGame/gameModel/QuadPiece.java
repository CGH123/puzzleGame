package com.example.administrator.puzzleGame.gameModel;


import com.example.administrator.puzzleGame.constant.GameConstant;

public class QuadPiece extends PieceAbstract {
    Vector2f[] points;
    Vector2f[] texturePoints;
    int texId;
    int num;
    Boolean canChoose;

    public QuadPiece(int num, Vector2f[] points, int texId, Boolean canChoose) {
        this.points = points;
        this.texId = texId;
        this.num = num;
        this.canChoose = canChoose;
        pieceFill = new PieceFillBody(getPieceFillData(2.0f));
        pieceLine = new PieceLineBody(getPieceLineData(2.0f));
    }


    @Override
    public PieceFillData getPieceFillData(float scale) {
        float s = 1.0f / 3.0f;
        float translate = 0.5f;
        texturePoints = new Vector2f[4];
        for (int i = 0; i < 4; i++) {
            texturePoints[i] = points[i].multiK(s).add(new Vector2f(translate, translate));
            texturePoints[i].y = 1 - texturePoints[i].y;
        }


        //顶点坐标数据的初始化================begin============================
        float[] vertices = new float[]{
                points[0].x * 2, points[0].y * 2, 0, points[1].x * 2, points[1].y * 2, 0, points[2].x * 2, points[2].y * 2, 0,
                points[3].x * 2, points[3].y * 2, 0, points[2].x * 2, points[2].y * 2, 0, points[1].x * 2, points[1].y * 2, 0,
        };


        //法向量数据初始化
        float[] normals = new float[vertices.length];
        for (int i = 0; i < normals.length; i += 3) {
            normals[i] = 0;
            normals[i + 1] = 0;
            normals[i + 2] = 1;
        }

        float[] textures = new float[]{
                texturePoints[0].x, texturePoints[0].y, texturePoints[1].x, texturePoints[1].y, texturePoints[2].x, texturePoints[2].y,
                texturePoints[3].x, texturePoints[3].y, texturePoints[2].x, texturePoints[2].y, texturePoints[1].x, texturePoints[1].y
        };
        return new PieceFillData(num, texId, canChoose, vertices, normals, textures);
    }

    @Override
    public void PieceLineTransForm() {
        MatrixState.translate(0.0f, 0.0f, 0.01f);
    }

    @Override
    public PieceLineData getPieceLineData(float scale) {
        Vector3f[] vertex = new Vector3f[]{
                new Vector3f(points[0].x * scale, points[0].y * scale, 0),
                new Vector3f(points[1].x * scale, points[1].y * scale, 0),
                new Vector3f(points[3].x * scale, points[3].y * scale, 0),
                new Vector3f(points[2].x * scale, points[2].y * scale, 0)
        };
        int vCount = points.length;
        //顶点坐标数据的初始化================begin============================
        float[] vertices = new float[3 * vCount];
        float[] textures = new float[4 * vCount];
        for (int i = 0; i < vCount; i++) {
            vertices[i * 3] = vertex[i].x;
            vertices[i * 3 + 1] = vertex[i].y;
            vertices[i * 3 + 2] = vertex[i].z;

            textures[i * 4] = GameConstant.LINE_COLOR[0];
            textures[i * 4 + 1] = GameConstant.LINE_COLOR[1];
            textures[i * 4 + 2] = GameConstant.LINE_COLOR[2];
            textures[i * 4 + 3] = GameConstant.LINE_COLOR[3];
        }
        return new PieceLineData(vertices, textures);
    }

}
