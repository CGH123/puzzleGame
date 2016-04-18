package com.example.administrator.puzzleGame.game3DModel;


import com.example.administrator.puzzleGame.constant.GameConstant;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SpherePiece extends PieceBody {
    int num;
    int texId;
    int cutNum;
    float[] vertices;
    float[] normals;
    float[] textures;
    Float[] alVetex;
    Vector3f translateV;

    public SpherePiece(int num, int cutNum,List<Float> alVetex, float[] vertices, float[] normals, float[] textures, int texId) {
        this.num = num;
        this.texId = texId;
        this.vertices = vertices;
        this.normals = normals;
        this.textures = textures;
        this.cutNum = cutNum;
        this.alVetex = alVetex.toArray(new Float[alVetex.size()]);

        int pos1 = 0;
        int pos2 = cutNum * (cutNum + 1) / 2;
        int pos3 = cutNum * (cutNum + 1) / 2 + cutNum;
        Vector3f vector3f1 = new Vector3f(this.alVetex[pos1 * 3],this.alVetex[pos1 * 3 + 1],this.alVetex[pos1 * 3 + 2]);
        Vector3f vector3f2 = new Vector3f(this.alVetex[pos2 * 3],this.alVetex[pos2 * 3 + 1],this.alVetex[pos2 * 3+ 2]);
        Vector3f vector3f3 = new Vector3f(this.alVetex[pos3 * 3],this.alVetex[pos3 * 3 + 1],this.alVetex[pos2 * 3 + 2]);
        translateV = vector3f1.minus(vector3f2)
                .cross(vector3f2.minus(vector3f3))
                .normalize()
                .multiK(0.01f);

        pieceFill = new PieceFillBody(getPieceFillData(2.0f));
        pieceLine = new PieceLineBody(getPieceLineData(2.0f));
    }


    @Override
    public void PieceLineTransForm() {
        MatrixState.translate(translateV.x, translateV.y, translateV.z);
    }

    @Override
    public PieceLineData getPieceLineData(float scale) {
        float[] vertex = new float[9 * cutNum];
        int index = 0;
        for (int i = 0; i < cutNum; i++) {
            int pos = i * (i + 1) / 2;
            vertex[(index + i) * 3] = alVetex[pos * 3];
            vertex[(index + i) * 3 + 1] = alVetex[pos * 3 + 1];
            vertex[(index + i) * 3 + 2] = alVetex[pos * 3 + 2];
        }
        index += cutNum;
        for (int i = 0; i < cutNum; i++) {
            int pos = cutNum * (cutNum + 1) / 2 + i;
            vertex[(index + i) * 3] = alVetex[pos * 3];
            vertex[(index + i) * 3 + 1] = alVetex[pos * 3 + 1];
            vertex[(index + i) * 3 + 2] = alVetex[pos * 3 + 2];
        }
        index += cutNum;
        for (int i = 0; i < cutNum; i++) {
            int pos = i * (i - 3 - 2 * cutNum) / 2 + cutNum * (cutNum + 3) / 2;
            vertex[(index + i) * 3] = alVetex[pos * 3];
            vertex[(index + i) * 3 + 1] = alVetex[pos * 3 + 1];
            vertex[(index + i) * 3 + 2] = alVetex[pos * 3 + 2];

        }
        float[] lineTextures = new float[vertices.length / 3 * 4];
        for (int i = 0; i < vertices.length / 3; i++) {
            lineTextures[i * 4] = GameConstant.LINE_COLOR[0];
            lineTextures[i * 4 + 1] = GameConstant.LINE_COLOR[1];
            lineTextures[i * 4 + 2] = GameConstant.LINE_COLOR[2];
            lineTextures[i * 4 + 3] = GameConstant.LINE_COLOR[3];
        }
        return new PieceLineData(vertex, lineTextures);
    }

    @Override
    public PieceFillData getPieceFillData(float scale) {
        return new PieceFillData(num, texId, true, vertices, normals, textures);
    }
}
