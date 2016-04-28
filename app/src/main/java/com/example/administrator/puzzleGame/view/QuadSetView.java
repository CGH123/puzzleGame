package com.example.administrator.puzzleGame.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.administrator.puzzleGame.constant.GameConstant;
import com.example.administrator.puzzleGame.gameModel.Vector2f;

public class QuadSetView extends View {
    private int mHeight;
    private int mWidth;
    private int hX;
    private int hY;
    private Paint linePaint, dotPaint;
    private Vector2f[] quadPoints, points;
    private float endX, endY;
    private int pickNum;
    private OnPointsChanged onPointsChanged;

    public interface OnPointsChanged {
        void pointsChanged(Vector2f[] points);
    }

    public QuadSetView(Context context) {
        super(context);
        init();
    }


    public QuadSetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuadSetView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        this.mHeight = (int) (GameConstant.WIDTH * 0.4f);
        this.mWidth = (int) (GameConstant.WIDTH * 0.4f);

        hX = mWidth / 2;
        hY = mHeight / 2;
        setMinimumHeight(mHeight);
        setMinimumWidth(mWidth);

        quadPoints = new Vector2f[]{
                new Vector2f(0, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 0)
        };

        points = quadPoints.clone();

        for (int i = 0; i < points.length; i++) {
            points[i] = points[i].minus(new Vector2f(0.5f, 0.5f));
            points[i].x *= hX * 0.8f;
            points[i].y *= hY * 0.8f;

        }

        linePaint = new Paint();
        linePaint.setColor(Color.BLUE);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(10);
        dotPaint = new Paint();
        dotPaint = new Paint(Color.BLACK);
        linePaint.setStyle(Paint.Style.FILL);
    }

    public void setOnPointsChanged(OnPointsChanged onPointsChanged) {
        this.onPointsChanged = onPointsChanged;
    }

    public void setPoints(Vector2f[] quadPoints) {
        this.quadPoints = quadPoints;
        points = quadPoints.clone();
        for (int i = 0; i < points.length; i++) {
            points[i] = points[i].minus(new Vector2f(0.5f, 0.5f));
            points[i].x *= hX * 0.8f;
            points[i].y *= hY * 0.8f;

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.translate(hX, hY);

        canvas.drawLine(points[0].x, points[0].y, points[1].x, points[1].y, linePaint);
        canvas.drawLine(points[1].x, points[1].y, points[2].x, points[2].y, linePaint);
        canvas.drawLine(points[2].x, points[2].y, points[3].x, points[3].y, linePaint);
        canvas.drawLine(points[3].x, points[3].y, points[0].x, points[0].y, linePaint);


        canvas.drawCircle(points[0].x, points[0].y, 10, dotPaint);
        canvas.drawCircle(points[1].x, points[1].y, 10, dotPaint);
        canvas.drawCircle(points[2].x, points[2].y, 10, dotPaint);
        canvas.drawCircle(points[3].x, points[3].y, 10, dotPaint);

        super.onDraw(canvas);
    }

    //触摸事件回调方法
    @Override
    public boolean onTouchEvent(MotionEvent e) {

        float startX = e.getX();    //记录每次touch时间触发开始时的Y坐标
        float startY = e.getY();    //记录每次touch时间触发开始时的X坐标

        switch (e.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                pickNum = choosePickNum(endX - hX, endY - hY);
                if (pickNum != -1) {
                    points[pickNum].x = e.getX() - hX;
                    points[pickNum].y = e.getY() - hY;
                    quadPoints[pickNum].x = points[pickNum].x / (hX * 0.8f) + 0.5f;
                    quadPoints[pickNum].y = points[pickNum].y / (hY * 0.8f) + 0.5f;
                }
                onPointsChanged.pointsChanged(quadPoints);

                this.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        endX = startX;    //记录每次touch时间触发结束时的X坐标
        endY = startY;    //记录每次touch时间触发结束时的Y坐标

        return true;
    }

    private int choosePickNum(float x, float y) {
        for (int i = 0; i < 4; i++) {
            if ((x - points[i].x) * (x - points[i].x) + (y - points[i].y) * (x - points[i].y) < 500)
                return i;
        }
        return -1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(mWidth, mHeight);
    }

}
