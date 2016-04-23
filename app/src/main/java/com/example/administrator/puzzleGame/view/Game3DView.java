package com.example.administrator.puzzleGame.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.administrator.puzzleGame.R;
import com.example.administrator.puzzleGame.constant.GameConstant;
import com.example.administrator.puzzleGame.game3DModel.Camera;
import com.example.administrator.puzzleGame.game3DModel.Cube;
import com.example.administrator.puzzleGame.game3DModel.MatrixState;
import com.example.administrator.puzzleGame.game3DModel.Quad;
import com.example.administrator.puzzleGame.game3DModel.ShaderManager;
import com.example.administrator.puzzleGame.game3DModel.SkyCloud;
import com.example.administrator.puzzleGame.game3DModel.SkyTree;
import com.example.administrator.puzzleGame.game3DModel.Sphere;
import com.example.administrator.puzzleGame.game3DModel.Vector2f;
import com.example.administrator.puzzleGame.game3DModel.Water;
import com.example.administrator.puzzleGame.game3DModel.Whole;
import com.example.administrator.puzzleGame.util.BitmapUtil;
import com.example.administrator.puzzleGame.util.LogUtil;
import com.example.administrator.puzzleGame.util.TextureUtil;
import com.example.administrator.puzzleGame.util.VectorUtil;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Game3DView extends GLSurfaceView {
    private static final String TAG = "Game3DView";
    //手指触控数
    private static int mode = 0;
    private Camera camera;
    private Context context;

    //单指时touch时间结束位置
    private float locationXEndP1, locationYEndP1;
    //三手down时两指向量
    private Vector2f Vector2fP01DownP3, Vector2fP02DownP3, Vector2fP12DownP3;
    //单指操作模式:
    private int modeP1 = 0;
    private boolean hasLoad = false;//是否初始化完成
    private boolean isP1Lock = false;//判断单手指操作是否被锁定
    private boolean isP1Move = false;//判断单手指操作是否被锁定
    private boolean canMoveCamera = false;
    private ObjectType objectType;
    private int cutNum;
    private int firstPickNum = -1;
    private Whole object;
    private Water water;
    private SkyTree tree;
    private SkyCloud cloud;
    public Game3DView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.setEGLContextClientVersion(2); //设置使用OPENGL ES2.0
        SceneRenderer mRenderer = new SceneRenderer();    //创建场景渲染器
        setRenderer(mRenderer);                //设置渲染器
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);   //设置渲染模式为主动渲染
        //创建摄像机
        float cameraDistance = 20f;
        camera = new Camera(cameraDistance);
        //初始化光源
        MatrixState.setLightLocation(0, 0, 0);
    }

    public void init(int cutNum, ObjectType objectType, Boolean canMoveCamera) {
        this.objectType = objectType;
        this.cutNum = cutNum;
        this.canMoveCamera = canMoveCamera;
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
                if (!object.isPickUpObject(e.getX(0), e.getY(0)) && canMoveCamera) {
                    modeP1 = 1;
                }
                //拾取方块
                else if (object.isPickUpObject(e.getX(0), e.getY(0))) {
                    modeP1 = 2;
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
                    if (modeP1 == 1 && canMoveCamera) {
                        float distanceXMoveP1 = locationXBeginP1 - locationXEndP1; //记录单指Move触发时X轴移动的距离
                        float distanceYMoveP1 = locationYBeginP1 - locationYEndP1;    //记录单指Move触发时Y轴移动的距离
                        //设置摄像机绕原点旋转
                        camera.rotateCamera(distanceXMoveP1, distanceYMoveP1);
                    } else if (modeP1 == 2) {
                        //// TODO: 2016/4/20 两手指交互
                        isP1Move = false;
                    }
                }
                if (mode == 3) {
                    //拉近拉远摄像头
                    float lengthP01MoveP3 = VectorUtil.Length(e.getX(0), e.getY(0), e.getX(1), e.getY(1));
                    float lengthP02MoveP3 = VectorUtil.Length(e.getX(0), e.getY(0), e.getX(2), e.getY(2));
                    float lengthP12MoveP3 = VectorUtil.Length(e.getX(1), e.getY(1), e.getX(2), e.getY(2));

                    if (lengthP01MoveP3 > 10f && lengthP02MoveP3 > 10f && lengthP12MoveP3 > 10f) {

                        float scale1 = lengthP01MoveP3 / (Vector2fP01DownP3.module());
                        float scale2 = lengthP02MoveP3 / (Vector2fP02DownP3.module());
                        float scale3 = lengthP12MoveP3 / (Vector2fP12DownP3.module());

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
                if (modeP1 == 2 && !isP1Move) {
                    int pieceNum = object.pickUpPiece(locationXBeginP1, locationYBeginP1);
                    LogUtil.d(TAG, "pick:" + pieceNum);
                    if (pieceNum != -1) {
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
                }
                mode = 0;
                modeP1 = 1;
                isP1Lock = false;
                isP1Move = false;
                break;
        }

        locationXEndP1 = locationXBeginP1;    //记录每次touch时间触发结束时的X坐标
        locationYEndP1 = locationYBeginP1;    //记录每次touch时间触发结束时的Y坐标

        return true;
    }

    public enum ObjectType {CUBE, SPHERE, QUAD_PLANE}

    public class SceneRenderer implements Renderer {
        Vector2f[] points;
        int[] texIds;
        //水的纹理
        int waterId;
        //天空球的纹理
        int treeId;
        int cloudId;
        int frameBufferId;
        int shadowId;// 动态产生的阴影纹理Id
        int renderDepthBufferId;// 动态产生的阴影纹理Id
        float skyAngle = 0;

        private boolean isBegin = true;

        private void initTaskReal() {

            water = new Water(shadowId, waterId, GameConstant.WIDTH, GameConstant.HEIGHT);
            tree = new SkyTree(treeId);
            cloud = new SkyCloud(cloudId);
            switch (objectType) {
                case CUBE:
                    object = new Cube(cutNum, points, texIds);
                    break;
                case SPHERE:
                    object = new Sphere(cutNum, 2, 1.6f, texIds[0]);
                    break;
                case QUAD_PLANE:
                    object = new Quad(cutNum, points, texIds[0]);
                    break;
            }
            //随机打乱初始开局
            Random rand = new Random();
            int randTime = 200;
            int range = object.getPiecesSize();
            for (int i = 0; i < randTime; i++) {
                int randNum1 = rand.nextInt(range);
                int randNum2 = rand.nextInt(range);
                object.swapPiece(randNum1, randNum2);
            }
        }


        @Override
        public void onDrawFrame(GL10 gl) {
            if (!hasLoad) {
                initTaskReal();
                hasLoad = true;
            } else {
                //设置摄像头
                camera.setCamera();

                //绘制倒影纹理
                MatrixState.pushMatrix();
                generateShadowImage();
                MatrixState.popMatrix();

                //使用到屏幕的帧缓冲
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
                //设置视窗大小及位置
                GLES20.glViewport(0, 0, GameConstant.WIDTH, GameConstant.HEIGHT);
                //清除深度缓冲与颜色缓冲
                GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

                //绘制物体
                MatrixState.pushMatrix();

                MatrixState.pushMatrix();    //进栈
                MatrixState.scale(2, 2, 2);
                object.setDrawLine(true);
                object.drawSelf();
                MatrixState.popMatrix();//出栈

                MatrixState.pushMatrix();
                MatrixState.translate(0, -35, 0);

                MatrixState.pushMatrix();
                MatrixState.rotate(skyAngle, 0, 1, 0);
                cloud.drawSelf();
                MatrixState.popMatrix();

                MatrixState.pushMatrix();
                //开启混合
                GLES20.glEnable(GLES20.GL_BLEND);
                //设置混合因子c
                GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
                MatrixState.translate(0, -1.0f, 0);
                tree.drawSelf();
                //关闭混合
                GLES20.glDisable(GLES20.GL_BLEND);
                MatrixState.popMatrix();

                //绘制水面
                MatrixState.pushMatrix();
                MatrixState.rotate(180, 1, 0, 0);
                water.setTexIdDY(shadowId);
                water.drawSelf();
                MatrixState.popMatrix();

                MatrixState.popMatrix();

                MatrixState.popMatrix();

                GLES20.glDeleteFramebuffers(1, new int[]{frameBufferId}, 0);
                GLES20.glDeleteTextures(1, new int[]{shadowId}, 0);
                skyAngle += 0.1f;
            }
        }

        //通过绘制产生倒影纹理
        private void generateShadowImage() {
            initFRBuffers();

            //清除深度缓冲与颜色缓冲
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

            //绘制倒影
            MatrixState.pushMatrix();
            MatrixState.scale(1, -1, 1);

            MatrixState.pushMatrix();    //进栈
            MatrixState.translate(0, 35f, 0);
            MatrixState.scale(2, 2, 2);
            object.setDrawLine(false);
            object.drawSelf();
            MatrixState.popMatrix();//出栈

            //绘制云天空
            MatrixState.pushMatrix();
            MatrixState.translate(0, -5.0f, 0);
            MatrixState.rotate(skyAngle, 0, 1, 0);
            cloud.drawSelf();
            MatrixState.popMatrix();

            //开启混合
            GLES20.glEnable(GLES20.GL_BLEND);
            //设置混合因子c
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            //绘制树木
            MatrixState.pushMatrix();
            tree.drawSelf();
            MatrixState.popMatrix();
            //关闭混合
            GLES20.glDisable(GLES20.GL_BLEND);


            MatrixState.popMatrix();
        }

        //初始化帧缓冲和渲染缓冲
        public void initFRBuffers() {
            int[] tia = new int[1];
            GLES20.glGenFramebuffers(1, tia, 0);
            frameBufferId = tia[0];

            if (isBegin) {
                GLES20.glGenRenderbuffers(1, tia, 0);
                renderDepthBufferId = tia[0];
                GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, renderDepthBufferId);
                GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, GameConstant.SHADOW_TEX_WIDTH, GameConstant.SHADOW_TEX_HEIGHT);
                isBegin = false;
            }

            int[] tempIds = new int[1];
            GLES20.glGenTextures(
                    1,          //产生的纹理id的数量
                    tempIds,   //纹理id的数组
                    0           //偏移量
            );

            shadowId = tempIds[0];

            GLES20.glViewport(0, 0, GameConstant.SHADOW_TEX_WIDTH, GameConstant.SHADOW_TEX_HEIGHT);
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferId);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, shadowId);//绑定纹理
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

            GLES20.glFramebufferTexture2D(
                    GLES20.GL_FRAMEBUFFER,
                    GLES20.GL_COLOR_ATTACHMENT0,
                    GLES20.GL_TEXTURE_2D,
                    shadowId,
                    0
            );

            GLES20.glTexImage2D(
                    GLES20.GL_TEXTURE_2D,
                    0,
                    GLES20.GL_RGB,
                    GameConstant.SHADOW_TEX_WIDTH,
                    GameConstant.SHADOW_TEX_HEIGHT,
                    0,
                    GLES20.GL_RGB,
                    GLES20.GL_UNSIGNED_SHORT_5_6_5,
                    null
            );

            GLES20.glFramebufferRenderbuffer(
                    GLES20.GL_FRAMEBUFFER,
                    GLES20.GL_DEPTH_ATTACHMENT,
                    GLES20.GL_RENDERBUFFER,
                    renderDepthBufferId
            );
        }


        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //设置视窗大小及位置 
            GLES20.glViewport(0, 0, width, height);
            //计算GLSurfaceView的宽高比
            GameConstant.RATIO = (float) width / height;
            GameConstant.WIDTH = width;
            GameConstant.HEIGHT = height;
            //关闭背面剪裁
            GLES20.glDisable(GLES20.GL_CULL_FACE);

        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //设置屏幕背景色RGBA
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

            //打开深度检测
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);

            //初始化shader
            ShaderManager.loadCodeFromFile(context.getResources());
            ShaderManager.compileShader();

            //初始化变换矩阵
            MatrixState.setInitStack();

            Bitmap src;
            Vector2f[] quadPositions;
            //初始化纹理
            waterId = TextureUtil.initTexture(context, R.drawable.water);
            treeId = TextureUtil.initTexture(context, R.drawable.sky_tree);
            cloudId = TextureUtil.initTexture(context, R.drawable.sky_cloud);


            switch (objectType) {
                case CUBE:
                    int[] pictureIds = new int[]{
                            R.mipmap.ic_launcher,
                            R.mipmap.ic_launcher,
                            R.mipmap.ic_launcher,
                            R.mipmap.ic_launcher,
                            R.mipmap.ic_launcher,
                            R.mipmap.ic_launcher
                    };
                    texIds = new int[pictureIds.length];
                    quadPositions = new Vector2f[]{
                            new Vector2f(0, 0),
                            new Vector2f(1, 0),
                            new Vector2f(1, 1),
                            new Vector2f(0, 1),
                    };
                    for (int i = 0; i < pictureIds.length; i++) {
                        src = TextureUtil.loadTexture(context, pictureIds[i]);//设置纹理图片
                        texIds[i] = TextureUtil.initTexture(src);//设置纹理ID
                    }
                    points = BitmapUtil.cutBitmapToCubes(quadPositions, cutNum, cutNum);

                    break;
                case SPHERE:
                    src = TextureUtil.loadTexture(context, R.mipmap.android_robot);//设置纹理图片
                    texIds = new int[1];
                    texIds[0] = TextureUtil.initTexture(src);//设置纹理ID
                    break;
                case QUAD_PLANE:
                    src = TextureUtil.loadTexture(context, R.mipmap.ic_launcher);//设置纹理图片
                    texIds = new int[1];
                    texIds[0] = TextureUtil.initTexture(src);//设置纹理ID
                    quadPositions = new Vector2f[]{
                            new Vector2f(0.0f, 0.0f),
                            new Vector2f(1.0f, 0.0f),
                            new Vector2f(1.0f, 1.0f),
                            new Vector2f(0.8f, 0.2f),
                    };
                    points = BitmapUtil.cutBitmapToQuads(quadPositions, cutNum, cutNum);
                    break;
            }
        }
    }
}
