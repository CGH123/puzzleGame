package com.example.administrator.puzzleGame.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.administrator.puzzleGame.R;

import com.example.administrator.puzzleGame.game2DModel.BorderModel;
import com.example.administrator.puzzleGame.game2DModel.ClassSetScreenWH;
import com.example.administrator.puzzleGame.sqlServer.GameDB;

/**
 * Created by Administrator on 2016-03-16.
 */
public class GamePlayActivity extends AppCompatActivity {
    Canvas canvas;
    private boolean inittable_sign;
    private BorderModel boardModel ;
    private ClassSetScreenWH classSetScreenWH;
    private Bitmap bit;
    private ImageView[] imageView;   //本身对应于正确的图片顺序
    private int[] reg_pic;
    private int num_row;
   // private Chronometer timer;
    /** 窗口的宽度*/
    private int screenWidth = 0;
    /**  窗口的高度*/
    private int screenHeight = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        bit=((BitmapDrawable) getResources().getDrawable(Integer.valueOf(GameDB.mImageIds.get(GameDB.pic_res_choice)))).getBitmap();
        classSetScreenWH=new ClassSetScreenWH(GamePlayActivity.this);
        this.screenHeight=classSetScreenWH.getScreenHeight();//设置屏幕的宽高
        this.screenWidth=classSetScreenWH.getScreenWidth();
        this.inittable_sign=true;

        this.boardModel=new BorderModel(bit);
        GameDB.game_state=GameDB.game_playing;
        this.boardModel.setScreenWidth(this.screenWidth);
        this.boardModel.setScreenHeight(this.screenHeight);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_playing);
        initImageview(GameDB.Diffcult_choice);
        //简单计时器
       // timer = (Chronometer)this.findViewById(R.id.chronometer);
        init_pic();//初始化图片
        init_num_row();//根据难度来初始化列数

    }
    public void init_num_row(){
        switch (GameDB.Diffcult_choice) {
            case GameDB.Diffcult_hard:
                num_row = 4;
                break;
            default:
                num_row = 3;
        }
    }
   public void init_pic(){
       reg_pic=new int[16];
       for(int i=0;i<16;i++)
        reg_pic[i]=-1;
    }
    /*
    根据难度选择，控件初始化内容
     */
   public void initImageview(int Diffcult_choice){
      imageView=new ImageView[16];
      imageView[0]=(ImageView)findViewById(R.id.ima_game_playing0);
      imageView[0].setOnTouchListener(movingEventListener);
      imageView[0].setImageBitmap(boardModel.all[0].bitmap);

      imageView[1]=(ImageView)findViewById(R.id.ima_game_playing1);
      imageView[1].setOnTouchListener(movingEventListener);
      imageView[1].setImageBitmap(boardModel.all[1].bitmap);

      imageView[2]=(ImageView)findViewById(R.id.ima_game_playing2);
     imageView[2].setOnTouchListener(movingEventListener);
      imageView[2].setImageBitmap(boardModel.all[2].bitmap);

      imageView[3]=(ImageView)findViewById(R.id.ima_game_playing3);
     imageView[3].setOnTouchListener(movingEventListener);
      imageView[3].setImageBitmap(boardModel.all[3].bitmap);

      imageView[4]=(ImageView)findViewById(R.id.ima_game_playing4);
      imageView[4].setOnTouchListener(movingEventListener);
      imageView[4].setImageBitmap(boardModel.all[4].bitmap);

      imageView[5]=(ImageView)findViewById(R.id.ima_game_playing5);
      imageView[5].setOnTouchListener(movingEventListener);
      imageView[5].setImageBitmap(boardModel.all[5].bitmap);

      imageView[6]=(ImageView)findViewById(R.id.ima_game_playing6);
     imageView[6].setOnTouchListener(movingEventListener);
      imageView[6].setImageBitmap(boardModel.all[6].bitmap);

      imageView[7]=(ImageView)findViewById(R.id.ima_game_playing7);
      imageView[7].setOnTouchListener(movingEventListener);
      imageView[7].setImageBitmap(boardModel.all[7].bitmap);

      imageView[8]=(ImageView)findViewById(R.id.ima_game_playing8);
    imageView[8].setOnTouchListener(movingEventListener);
      imageView[8].setImageBitmap(boardModel.all[8].bitmap);
    switch (Diffcult_choice){
    case GameDB.Diffcult_hard:
        imageView[12]=(ImageView)findViewById(R.id.ima_game_playing12);
        imageView[12].setOnTouchListener(movingEventListener);
        imageView[12].setImageBitmap(boardModel.all[12].bitmap);

        imageView[13]=(ImageView)findViewById(R.id.ima_game_playing13);
        imageView[13].setOnTouchListener(movingEventListener);
        imageView[13].setImageBitmap(boardModel.all[13].bitmap);

        imageView[14]=(ImageView)findViewById(R.id.ima_game_playing14);
        imageView[14].setOnTouchListener(movingEventListener);
        imageView[14].setImageBitmap(boardModel.all[14].bitmap);

        imageView[15]=(ImageView)findViewById(R.id.ima_game_playing15);
        imageView[15].setOnTouchListener(movingEventListener);
        imageView[15].setImageBitmap(boardModel.all[15].bitmap);

        imageView[9]=(ImageView)findViewById(R.id.ima_game_playing9);
        imageView[9].setOnTouchListener(movingEventListener);
        imageView[9].setImageBitmap(boardModel.all[9].bitmap);

        imageView[10]=(ImageView)findViewById(R.id.ima_game_playing10);
        imageView[10].setOnTouchListener(movingEventListener);
        imageView[10].setImageBitmap(boardModel.all[10].bitmap);

        imageView[11]=(ImageView)findViewById(R.id.ima_game_playing11);
        imageView[11].setOnTouchListener(movingEventListener);
        imageView[11].setImageBitmap(boardModel.all[11].bitmap);
break;
    case GameDB.Diffcult_normal:
        imageView[9]=(ImageView)findViewById(R.id.ima_game_playing9);
       imageView[9].setOnTouchListener(movingEventListener);
        imageView[9].setImageBitmap(boardModel.all[9].bitmap);

        imageView[10]=(ImageView)findViewById(R.id.ima_game_playing10);
        imageView[10].setOnTouchListener(movingEventListener);
        imageView[10].setImageBitmap(boardModel.all[10].bitmap);

        imageView[11]=(ImageView)findViewById(R.id.ima_game_playing11);
       imageView[11].setOnTouchListener(movingEventListener);
        imageView[11].setImageBitmap(boardModel.all[11].bitmap);
    default:

        break;
}


  }
    /*
    构建动画层并且实现移动
     */
    private RelativeLayout mAnimLayout;
    /*
    随机分布和动画
    */
    public void inittable(View ima,int Diffcult_choice){
        Boolean[] sign=new Boolean[16];
        int value;
        int num_row;

        switch (Diffcult_choice){
            case GameDB.Diffcult_normal:
                value=11;
                num_row=3;
                break;
            case GameDB.Diffcult_easy:
                value=8;
                num_row=3;
                break;
            default:
                value=15;
                num_row=4;
                break;
        }
        //timer.setBase(SystemClock.elapsedRealtime());
        ////开始计时
     //   timer.start();
        for(int i=0;i<=value;i++)
            sign[i]=false;
        //设置动画层
        final int Width=ima.getRight()-ima.getLeft();
        final int Height=ima.getBottom()-ima.getTop();
        int ix=Integer.valueOf((int) (Math.random()*value));

       setUpAnimlayout();
        for(int i=0;i<=value;i++) {
            while(true)
            if (sign[ix] == false) {
               final  int row = ix % num_row;
               final int column = ix / num_row;
               reg_pic[ix]=i;
                imageView[i].setVisibility(View.INVISIBLE);
            ImageView ima1 = new ImageView(this.getApplicationContext());
            ima1.setImageBitmap(boardModel.all[i].bitmap);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(Width, Height);

            lp.leftMargin = 50;
            lp.topMargin = 50;

            ima1.setLayoutParams(lp);
            mAnimLayout.addView(ima1);

            //设置动画
            ObjectAnimator anim = new ObjectAnimator().ofFloat(ima1, "TranslationX", 0, row * Width).setDuration(300);
            anim.ofFloat(ima1, "TranslationY", 0, column * Height).setDuration(300).start();
            // TranslateAnimation anim= new TranslateAnimation(50,2*Width, 50, 2 * Height);
            // anim.setDuration(300);
            // anim.setFillAfter(true);
            //  ima.startAnimation(anim);
            final int k = i;
            //监听动画
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }
                @Override
                public void onAnimationEnd(Animator animation) {

                    imageView[k].layout(row * Width + 50, column * Height + 50, row * Width + Width + 50, column * Height + 50 + Height);

                   mAnimLayout.removeAllViews();
                    // mAnimLayout.clearAnimation();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            anim.start();
            imageView[k].setVisibility(View.VISIBLE);
                sign[ix]=true;
                ix=Integer.valueOf((int) (Math.random()*value));
                break;
            }else{
                if(sign[ix]==true&&ix==value)  ix=Integer.valueOf((int) (Math.random()*value));
                else ix++;
            }
        }
    }
    /*
    创建动画层
     */
    private void setUpAnimlayout(){
    if(mAnimLayout==null){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT);

        mAnimLayout=new RelativeLayout(getApplicationContext());
        this.addContentView(mAnimLayout,params);
    }
    }
    /*
    图片移动监听事件
     */
    public boolean double_click=true;
    public boolean first_11=true;
    boolean move_up=false;
    boolean down_move=false;  //控制down和move的关系
    private View.OnTouchListener movingEventListener = new View.OnTouchListener() {
        int lastX, lastY;
        int r_lastX,r_lastY;
        private int num_click=0;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //动画层效果
            ImageView im= (ImageView)v;   //强转用来设置点击之后的颜色变化
            /**
             * 只执行一次的图片位置初始化
             */
           if(inittable_sign){ inittable(v,GameDB.Diffcult_choice);
               inittable_sign=false;
                return true;
            }
            /**
             * 判断游戏状态，如果已经获胜则停止操作
             */
          //  System.out.println("触屏出发事件总体");
            if(GameDB.game_state==GameDB.game_win)return true;
            int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        num_click++;
                        System.out.print("出发down事件");
                        System.out.print(num_click);
                        System.out.print(".............");
                       if(num_click>=2){
                           System.out.print("v被锁住");
                           v.setClickable(false);
                       }
                        down_move=true;
                        if(!double_click){  double_click=false;return true;}//
                        double_click=false;
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                            r_lastX=v.getLeft();
                            r_lastY=v.getTop();

                    im.setColorFilter(Color.parseColor("#55FF0000"));
                       break;
                    case MotionEvent.ACTION_MOVE:

                        if(first_11){
                            r_lastX=v.getLeft();
                            r_lastY=v.getTop();
                            first_11=false;
                            num_click++;
                        }
                        if(!down_move){down_move=false;return true;}
                        down_move=false;
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                     //   System.out.println("触屏--移动的dx");
                      //  System.out.println(dx);
                        int left = v.getLeft() + dx;
                        int top = v.getTop() + dy;
                        int right = v.getRight() + dx;
                        int bottom = v.getBottom() + dy;

                        // 设置不能出界
                        if (left < 0) {
                            left = 0;
                            right = left + v.getWidth();
                        }

                        if (right > screenWidth) {
                            right = screenWidth;
                            left = right - v.getWidth();
                        }

                        if (top < 0) {
                            top = 0;
                            bottom = top + v.getHeight();
                        }

                        if (bottom > screenHeight) {
                            bottom = screenHeight;
                            top = bottom - v.getHeight();
                        }
                      // pic_move(v,left,top,right,bottom);
                      //  v.setVisibility(View.VISIBLE);
                     //   System.out.print("能否被选择");
                     //   System.out.print(v.isClickable());
                      //  v.setClickable(false); 有效

                   if(num_click<2) {
                       System.out.print("....移动事件....");
                       v.layout(left, top, right, bottom);
                   }
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        down_move=true;
                        move_up=true;
                        break;

                    default:
                      /**
                       *放手时进入事件
                       */
                        //get_clickable();
                        down_move=false;
                        if(!move_up){move_up=false;return true;}
                      // double_click=false;
                        //实现图片交换
                        System.out.println("up事件");
                      swap(v,v.getLeft(),v.getTop(),v.getBottom(),v.getRight(),v.getWidth(),v.getHeight(),getsec(r_lastX,r_lastY,v.getWidth(),v.getHeight()));
                        if(num_click%2==0){v.setClickable(true);};
                        num_click-=2;
                        if(num_click<0)num_click=0;
                        im.setColorFilter(null);
                        intable(v, GameDB.Diffcult_choice);
                         back_to(v);
                        break;
                }
                return true;
            }

    };
    public void back_to(View v){   //虽然会出现各种复杂的问题，但是最后殊途同归啊。直接重新刷新图片即可
    int k=0;
     switch (GameDB.Diffcult_choice){
         case GameDB.Diffcult_easy:
            k=9;
             break;
         case GameDB.Diffcult_normal:
             k=12;
             break;
         case GameDB.Diffcult_hard:
             k=16;
             break;
         default:
             k=9;

     }
            System.out.println("返回原来的位置");
            int Width=v.getWidth();
            int Height=v.getHeight();
            for(int i=0;i<k;i++){
                int row=i%num_row;
                int column=i/num_row;
                imageView[reg_pic[i]].layout(row * Width + 50, column * Height + 50, row * Width + Width + 50, column * Height + Height + 50);
            }

    }
    /**
     * 想要通过动画层的方式实现图片一直在上面
     * @param v
     * @param left
     * @param top
     * @param Bottom
     * @param right
     * @param Width
     * @param Height
     * @param sec_first
     */
    /*@Override
    public boolean onTouchEvent(MotionEvent ev) {
        int x=(int)ev.getRawX();
        int y=(int)ev.getRawY();
     pic_move(imageView[0],x,y);
        return true;
    };
    public void pic_move(View v,int left,int top){
        setUpAnimlayout();

        ImageView ima1 = new ImageView(this.getApplicationContext());
        ima1.setImageBitmap(v.getDrawingCache());
        final int Width=ima1.getRight()-ima1.getLeft();
        final int Height=ima1.getBottom()-ima1.getTop();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(Width, Height);
        lp.leftMargin = v.getLeft();
              lp.topMargin = v.getTop();
        ima1.setLayoutParams(lp);
              mAnimLayout.addView(ima1);
        ima1.setOnTouchListener(movingEventListener);

        System.out.println("动画层李面");
       ima1.layout(left, top, left+Width,top+Height);
        v.setVisibility(View.VISIBLE);


    }*/
    public void swap(View v,int left,int top,int Bottom,int right,int Width,int Height,int sec_first){
        int left1=left;
        int top1=top;
        left-=50;
        top-=50;
        if(left<0)left=1;
        if(top<0)top=1;
        int row_sec=left/Width; //由于50
        int column=top/Height;
        if((row_sec+1)*Width+50-left1<right-50-(row_sec+1)*Width){
            if(right<screenWidth)
            row_sec++;
        }
      //  System.out.println(row_sec);
        if((column+1)*Height+50-top1<Bottom-(column+1)*Height-50){
            if(Bottom<screenHeight-v.getHeight()/4)//确保不出错，屏幕宽度好像是图片可一移动的范围
            column++;
        }
       // System.out.println(column);

        int sec=row_sec+column*num_row;//当前移动到的格子
        int row2=sec_first%num_row;  //求得本来格子的列数
        int column2=sec_first/num_row; //求得本来格子的行数
      //  System.out.println(sec);

        imageView[reg_pic[sec]].layout(row2*Width+50,column2*Height+50,row2*Width+Width+50,column2*Height+Height+50);
        int k=reg_pic[sec_first];
        reg_pic[sec_first]=reg_pic[sec];
      //  System.out.println(sec_first);
        imageView[k].layout(row_sec * Width + 50, column * Height + 50, row_sec * Width + Width + 50, column * Height + Height + 50);
        reg_pic[sec]=k;

        double_click=true;
    }
    public int getsec(int left,int top,int Width,int Height){

        left-=50;
        top-=50;
        if(left<0)left=0;
        if(top<0)top=0;
        int row=left/Width;
        int column=top/Height;
        int sec=row+column*num_row;
        System.out.println(sec);
        return sec;
    }
    /*
    图片自动进入表格事件
     */
    public void intable(View imageView,int COM_Diffcult){
        int row;
        int column;
        /**
         * 获取难度
         */
        switch (COM_Diffcult){
            case GameDB.Diffcult_easy:
                row=3;
                column=3;
                    break;
            case GameDB.Diffcult_normal:
                row=3;
                column=4;
                break;
            case GameDB.Diffcult_hard:
                row =4;
                column=4;
                break;
            default:
                row=3;
                column=3;
                break;
        }

        int left=imageView.getLeft();
        int top=imageView.getTop();
        int dx ;
        int dy;
        int Width=imageView.getRight()-imageView.getLeft();
        int Height=imageView.getBottom()-imageView.getTop();
        int right;
        int bottom;
        for(int i=0;i<column;i++){
            for(int j=0;j<row;j++){
                if(left<j*Width+this.screenWidth/2/row&& top<i*Height+this.screenHeight/column/2) {
                    if (left > j * Width && top > i * Height) {
                        dx = j*Width+50 - left;
                        dy = i*Height+50 - top;
                        left = imageView.getLeft() + dx;
                        top = imageView.getTop() + dy;
                        right = imageView.getRight() + dx;
                        bottom = imageView.getBottom() + dy;
                        imageView.layout(left, top, right, bottom);
                    }
                }
            }
        }
        /*
        检查游戏是否已经成功
         */
     if(checkState(imageView,row,column)){
         Toast.makeText(this,"成功过关",Toast.LENGTH_LONG).show();
         GameDB.game_state=GameDB.game_win;
      //   timer.stop();
     };

    }
    public boolean checkState(View v,int row,int column){
        int Width=v.getRight() -v.getLeft();
        int Height=v.getBottom()-v.getTop();
        boolean state=true;

        for(int i=0;i<column;i++){
            for(int j=0;j<row;j++){
                int x=imageView[i*row+j].getLeft();
                int y=imageView[i*row+j].getTop();
                if(x==50+j*Width&&y==50+i*Height){
                    state=true;
                }else {state=false;
                break;
                }
            }
            if(state==false){
                break;
            }
        }
        return state;
    }
}
