package com.example.administrator.puzzleGame.view;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.administrator.puzzleGame.R;

import java.util.Random;

import com.example.administrator.puzzleGame.game3DModel.*;
import com.example.administrator.puzzleGame.game3DModel.Object;
import com.example.administrator.puzzleGame.constant.Game3DConstant;
import com.example.administrator.puzzleGame.util.BitmapUtil;
import com.example.administrator.puzzleGame.util.LogUtil;
import com.example.administrator.puzzleGame.util.TextureUtil;
import com.example.administrator.puzzleGame.util.VectorUtil;

public class Game3DView extends GLSurfaceView {
    public enum ObjectType{CUBE, SPHERE}

    private static final String TAG = "Game3DView";
    private Camera camera;
    private Context context;

    //单指时touch时间结束位置
    private float locationXEndP1, locationYEndP1;
    //三手down时两指向量
    private Vector2f Vector2fP01DownP3, Vector2fP02DownP3, Vector2fP12DownP3;
    //手指触控数
    private static int mode = 0;
    //单指操作模式:
    private int modeP1 = 0;

    private boolean hasLoad = false;//是否初始化完成
    private boolean isP1Lock = false;//判断单手指操作是否被锁定

    private Object object;
    private ObjectType objectType;
    private int cutNum;
    private int firstPickNum = -1;

    public Game3DView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.setEGLContextClientVersion(2); //设置使用OPENGL ES2.0
        SceneRenderer mRenderer = new SceneRenderer();    //创建场景渲染器
        setRenderer(mRenderer);                //设置渲染器
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);   //设置渲染模式为主动渲染
        //创建摄像机
        camera = new Camera();
        //初始化光源
        MatrixState.setLightLocation(0, 100, 0);
    }

    public void init(int cutNum, ObjectType objectType){
        this.objectType = objectType;
        this.cutNum = cutNum;
    }

    //触摸事件回调方法
    @Override
    public boolean onTouchEvent(MotionEvent e) {

        float locationXBeginP1 = e.getX();    //记录每次touch时间触发开始时的Y坐标
        float locationYBeginP1 = e.getY();    //记录每次touch时间触发开始时的X坐标

        switch (e.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = 1;//设置为单点模式

                //选择点为空，移动相机
                if (!object.isPickUpObject(e.getX(0), e.getY(0))) {
                    modeP1 = 1;
                }
                //拾取方块
                else {
                    modeP1 = 2;
                    int pieceNum = object.pickUpPiece(e.getX(0), e.getY(0));
                    LogUtil.d(TAG, "pick:" + pieceNum);
                    if (firstPickNum == -1) {
                        firstPickNum = pieceNum;
                        object.hintPiece(firstPickNum);
                    } else if (firstPickNum == pieceNum) {
                        object.hintPiece(firstPickNum);
                        firstPickNum = -1;
                    } else {
                        object.swapPiece(firstPickNum, pieceNum);
                        object.hintPiece(firstPickNum);
                        firstPickNum = -1;
                        if (object.isCompleted()) {
                            // TODO: 2016/4/15 获胜UI
                            LogUtil.d(TAG, "win!");
                        }
                    }
                }

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mode += 1; //2指或3指
                isP1Lock = true;
                if (mode == 3) {
                    Vector2fP01DownP3 = new Vector2f(e.getX(0) - e.getX(1), e.getY(0) - e.getY(1));//手指12初始向量
                    Vector2fP02DownP3 = new Vector2f(e.getX(2) - e.getX(0), e.getY(2) - e.getY(0));//手指13初始向量
                    Vector2fP12DownP3 = new Vector2f(e.getX(2) - e.getX(1), e.getY(2) - e.getY(1));//手指23初始向量
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == 1 && !isP1Lock) {
                    //摄像机移动与拾取
                    if (modeP1 == 1) {
                        float distanceXMoveP1 = locationXBeginP1 - locationXEndP1; //记录单指Move触发时X轴移动的距离
                        float distanceYMoveP1 = locationYBeginP1 - locationYEndP1;    //记录单指Move触发时Y轴移动的距离
                        //设置摄像机绕原点旋转
                        camera.rotateCamera(distanceXMoveP1, distanceYMoveP1);
                    }
                }
                if (mode == 3) {
                    //拉近拉远摄像头
                    float lengthP01MoveP3 = VectorUtil.Length(e.getX(0), e.getY(0), e.getX(1), e.getY(1));
                    float lengthP02MoveP3 = VectorUtil.Length(e.getX(0), e.getY(0), e.getX(2), e.getY(2));
                    float lengthP12MoveP3 = VectorUtil.Length(e.getX(1), e.getY(1), e.getX(2), e.getY(2));

                    if (lengthP01MoveP3 > 10f && lengthP02MoveP3 > 10f && lengthP12MoveP3 > 10f) {

                        float scale1 = lengthP01MoveP3 / (Vector2fP01DownP3.mod);
                        float scale2 = lengthP02MoveP3 / (Vector2fP02DownP3.mod);
                        float scale3 = lengthP12MoveP3 / (Vector2fP12DownP3.mod);

                        Vector2fP01DownP3 = new Vector2f(e.getX(0) - e.getX(1), e.getY(0) - e.getY(1));
                        Vector2fP02DownP3 = new Vector2f(e.getX(0) - e.getX(2), e.getY(0) - e.getY(2));
                        Vector2fP12DownP3 = new Vector2f(e.getX(1) - e.getX(2), e.getY(1) - e.getY(2));

                        //缩放相机
                        camera.scaleCamera(scale1, scale2, scale3);
                    }
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                mode -= 1;//设置为单点模式
                break;
            case MotionEvent.ACTION_UP:
                mode = 0;
                modeP1 = 1;
                isP1Lock = false;
                break;
        }

        locationXEndP1 = locationXBeginP1;    //记录每次touch时间触发结束时的X坐标
        locationYEndP1 = locationYBeginP1;    //记录每次touch时间触发结束时的Y坐标

        return true;
    }

    public class SceneRenderer implements Renderer {
        int [] texIds;
        private void initTaskReal() {
            Random rand = new Random();
            int randTime = 200;
            if (objectType == ObjectType.CUBE) {
                object = new Cube(context, cutNum, 1.0f, texIds);
                for (int i = 0; i < randTime; i++) {
                    int randNum1 = rand.nextInt(6 * cutNum * cutNum);
                    int randNum2 = rand.nextInt(6 * cutNum * cutNum);
                    object.swapPiece(randNum1, randNum2);
                }
            } else if (objectType == ObjectType.SPHERE) {
                //// TODO: 2016/4/15 球
            }
        }


        @Override
        public void onDrawFrame(GL10 gl) {
            //清除深度缓冲与颜色缓冲
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

            //调用此方法计算产生透视投影矩阵
            MatrixState.setProjectFrustum(-Game3DConstant.RATIO, Game3DConstant.RATIO, -1, 1, 10, 400);
            // 调用此方法产生摄像机9参数位置矩阵
            MatrixState.setCamera(0, 0, 70, 0, 0, 0, 0, 1, 0);

            //设置摄像头
            camera.initCamera();

            if (!hasLoad) {
                initTaskReal();
                hasLoad = true;
            } else {
                //绘制
                MatrixState.pushMatrix();    //进栈
                object.drawSelf();
                MatrixState.popMatrix();//出栈
            }
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //设置视窗大小及位置 
            GLES20.glViewport(0, 0, width, height);
            //计算GLSurfaceView的宽高比
            Game3DConstant.RATIO = (float) width / height;
            Game3DConstant.WIDTH = width;
            Game3DConstant.HEIGHT = height;

        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //设置屏幕背景色RGBA
            GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

            //打开深度检测
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            //打开背面剪裁   
            GLES20.glEnable(GLES20.GL_CULL_FACE);

            //初始化shader
            ShaderManager.loadCodeFromFile(context.getResources());
            ShaderManager.compileShader();

            //初始化变换矩阵
            MatrixState.setInitStack();

            int[] pictureIds = new int[]{
                    R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher
            };
            //初始化纹理
            texIds = new int[pictureIds.length * cutNum * cutNum];
            for (int i = 0; i < pictureIds.length; i++) {
                Bitmap src = TextureUtil.loadTexture(context, pictureIds[i]);//设置纹理图片
                Bitmap[] bitmaps = BitmapUtil.cutBitmap(src, cutNum, cutNum);
                src.recycle();
                for (int j = 0; j < bitmaps.length; j++) {
                    texIds[i * cutNum * cutNum + j] = TextureUtil.initTexture(bitmaps[j], true);//设置纹理ID
                }
            }
        }
    }
}
