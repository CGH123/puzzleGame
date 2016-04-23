package com.example.administrator.puzzleGame.game3DModel;

import android.opengl.Matrix;

import java.util.Vector;


public class Bound {
    public float xMax;
    public float xMin;
    public float yMax;
    public float yMin;
    public float zMax;
    public float zMin;

    public Bound(Vector3f p1, Vector3f p2, Vector3f p3) {
        xMax = xMin = p1.x;
        yMax = yMin = p1.y;
        zMax = zMin = p1.z;

        checkVertex(p2);
        checkVertex(p3);
    }

    public Bound(float x, float y, float z) {
        xMax = x;
        xMin = -x;
        yMax = y;
        yMin = -y;
        zMax = z;
        zMin = -z;
    }

    public Bound(Vector<Vector3f> vertices) {
        xMin = yMin = zMin = Float.POSITIVE_INFINITY;//将最小点设为最大值
        xMax = yMax = zMax = Float.NEGATIVE_INFINITY;//将最大点设为最小值

        for (int i = 1; i < vertices.size(); i++) {
            checkVertex(vertices.get(i));
        }
    }

    //参数为顶点数组的构造器
    public Bound(float[] vertices) {
        xMin = yMin = zMin = Float.POSITIVE_INFINITY;//将最小点设为最大值
        xMax = yMax = zMax = Float.NEGATIVE_INFINITY;//将最大点设为最小值
        //将所有的点加入包围盒
        for (int i = 0; i < vertices.length; i += 3) {
            this.add(vertices[i], vertices[i + 1], vertices[i + 2]);
        }
    }

    //将单个点加入到AABB中，并在必要的时候扩展AABB以包含每个点
    public void add(Vector3f p) {
        if (p.x < xMin) {
            xMin = p.x;
        }
        if (p.x > xMax) {
            xMax = p.x;
        }
        if (p.y < yMin) {
            yMin = p.y;
        }
        if (p.y > yMax) {
            yMax = p.y;
        }
        if (p.z < zMin) {
            zMin = p.z;
        }
        if (p.z > zMax) {
            zMax = p.z;
        }
    }

    public void add(float x, float y, float z) {
        if (x < xMin) {
            xMin = x;
        }
        if (x > xMax) {
            xMax = x;
        }
        if (y < yMin) {
            yMin = y;
        }
        if (y > yMax) {
            yMax = y;
        }
        if (z < zMin) {
            zMin = z;
        }
        if (z > zMax) {
            zMax = z;
        }
    }

    //获取AABB所有顶点坐标的方法
    public Vector3f[] getAllCorners() {
        Vector3f[] result = new Vector3f[8];
        for (int i = 0; i < 8; i++) {
            result[i] = getCorner(i);
        }
        return result;
    }

    //获取AABB第i个顶点坐标的方法
    public Vector3f getCorner(int i) {
        if (i < 0 || i > 7) {//检查i是否合法
            return null;
        }
        return new Vector3f(
                ((i & 1) == 0) ? xMax : xMin,
                ((i & 2) == 0) ? yMax : yMin,
                ((i & 4) == 0) ? zMax : zMin
        );
    }

    //通过当前仿射变换矩阵求得仿射变换后的AABB包围盒的方法
    public Bound setToTransformedBox(float[] m) {
        //获取所有顶点的坐标
        Vector3f[] va = this.getAllCorners();
        //用于存放仿射变换后的顶点数组
        float[] transformedCorners = new float[24];
        //将变换前的AABB包围盒的8个顶点与仿射变换矩阵m相乘，得到仿射变换后的OBB包围盒的所有顶点
        float[] tmpResult = new float[4];
        int count = 0;
        for (Vector3f v : va) {
            float[] point = new float[]{v.x, v.y, v.z, 1};//将顶点转换成齐次坐标
            Matrix.multiplyMV(tmpResult, 0, m, 0, point, 0);
            transformedCorners[count++] = tmpResult[0];
            transformedCorners[count++] = tmpResult[1];
            transformedCorners[count++] = tmpResult[2];
        }
        //通过构造器将OBB包围盒转换成AABB包围盒，并返回
        return new Bound(transformedCorners);
    }

    public boolean overlap(Bound bound) {
        return !((xMin > bound.xMax) || (xMax < bound.xMin) ||
                (yMin > bound.yMax) || (yMax < bound.yMin) ||
                (zMin > bound.zMax) || (zMax < bound.zMin));
    }

    void checkVertex(Vector3f vertex) {
        if (vertex.x > xMax) {
            xMax = vertex.x;
        } else if (vertex.x < xMin) {
            xMin = vertex.x;
        }

        if (vertex.y > yMax) {
            yMax = vertex.y;
        } else if (vertex.y < yMin) {
            yMin = vertex.y;
        }

        if (vertex.z > zMax) {
            zMax = vertex.z;
        } else if (vertex.z < zMin) {
            zMin = vertex.z;
        }
    }

    public float pickUpTime(float locationX, float locationY) {
        float[] AB = MatrixState.getUnProject(locationX, locationY);
        //射线AB
        Vector3f start = new Vector3f(AB[0], AB[1], AB[2]);//起点
        Vector3f end = new Vector3f(AB[3], AB[4], AB[5]);//终点
        Vector3f dir = end.minus(start);//长度和方向

        float[] Face = new float[4];
        return this.rayIntersect(start, dir, null, Face);//计算相交时间
    }

    /*
     * 先判断矩形边界框的哪个面会相交，再检测射线与包含这个面的平面的相交性。如果交点在盒子中，那么射线与矩形边界框相交，
     * 否则不存在相交
     */
    //和参数射线的相交性测试，如果不相交则返回值是一个非常大的数(大于1)
    //如果相交，返回相交时间t
    //t为0-1之间的值
    public float rayIntersect(
            Vector3f rayStart,//射线起点
            Vector3f rayDir,//射线长度和方向
            Vector3f returnNormal,//可选的，相交点处法向量
            float[] faceVector
    ) {
        //如果未相交则返回这个大数
        final float kNoIntersection = Float.POSITIVE_INFINITY;
        //检查点在矩形边界内的情况，并计算到每个面的距离
        boolean inside = true;
        float xt, xn = 0.0f;
        float yt, yn = 0.0f;
        float zt, zn = 0.0f;
        if (rayStart.x < xMin) {
            xt = xMin - rayStart.x;
            if (xt > rayDir.x) {
                return kNoIntersection;
            }
            xt /= rayDir.x;
            inside = false;
            xn = -1.0f;
        } else if (rayStart.x > xMax) {
            xt = xMax - rayStart.x;
            if (xt < rayDir.x) {
                return kNoIntersection;
            }
            xt /= rayDir.x;
            inside = false;
            xn = 1.0f;
        } else {
            xt = -1.0f;
        }

        if (rayStart.y < yMin) {
            yt = yMin - rayStart.y;
            if (yt > rayDir.y) {
                return kNoIntersection;
            }
            yt /= rayDir.y;
            inside = false;
            yn = -1.0f;
        } else if (rayStart.y > yMax) {
            yt = yMax - rayStart.y;
            if (yt < rayDir.y) {
                return kNoIntersection;
            }
            yt /= rayDir.y;
            inside = false;
            yn = 1.0f;
        } else {
            yt = -1.0f;
        }

        if (rayStart.z < zMin) {
            zt = zMin - rayStart.z;
            if (zt > rayDir.z) {
                return kNoIntersection;
            }
            zt /= rayDir.z;
            inside = false;
            zn = -1.0f;
        } else if (rayStart.z > zMax) {
            zt = zMax - rayStart.z;
            if (zt < rayDir.z) {
                return kNoIntersection;
            }
            zt /= rayDir.z;
            inside = false;
            zn = 1.0f;
        } else {
            zt = -1.0f;
        }
        //是否在矩形边界框内？
        if (inside) {
            if (returnNormal != null) {
                returnNormal = rayDir.multiK(-1);
                returnNormal.normalize();
            }
            return 0.0f;
        }
        //选择最远的平面————发生相交的地方
        int which = 0;
        float t = xt;
        if (yt > t) {
            which = 1;
            t = yt;
        }
        if (zt > t) {
            which = 2;
            t = zt;
        }

        switch (which) {
            case 0://和yz平面相交
            {
                faceVector[0] = 1;
                faceVector[1] = 0;
                faceVector[2] = 0;
                faceVector[3] = 1;
                float y = rayStart.y + rayDir.y * t;
                if (y < yMin || y > yMax) {
                    return kNoIntersection;
                }
                float z = rayStart.z + rayDir.z * t;
                if (z < zMin || z > zMax) {
                    return kNoIntersection;
                }
                if (returnNormal != null) {
                    returnNormal.x = xn;
                    returnNormal.y = 0.0f;
                    returnNormal.z = 0.0f;
                }
            }
            break;
            case 1://和xz平面相交
            {
                faceVector[0] = 0;
                faceVector[1] = 1;
                faceVector[2] = 0;
                faceVector[3] = 1;
                float x = rayStart.x + rayDir.x * t;
                if (x < xMin || x > xMax) {
                    return kNoIntersection;
                }
                float z = rayStart.z + rayDir.z * t;
                if (z < zMin || z > zMax) {
                    return kNoIntersection;
                }
                if (returnNormal != null) {
                    returnNormal.x = 0.0f;
                    returnNormal.y = yn;
                    returnNormal.z = 0.0f;
                }
            }
            break;
            case 2://和xy平面相交
            {
                faceVector[0] = 0;
                faceVector[1] = 0;
                faceVector[2] = 1;
                faceVector[3] = 1;
                float x = rayStart.x + rayDir.x * t;
                if (x < xMin || x > xMax) {
                    return kNoIntersection;
                }
                float y = rayStart.y + rayDir.y * t;
                if (y < yMin || y > yMax) {
                    return kNoIntersection;
                }
                if (returnNormal != null) {
                    returnNormal.x = 0.0f;
                    returnNormal.y = 0.0f;
                    returnNormal.z = zn;
                }
            }
            break;
        }
        return t;//返回相交点参数值
    }
}
