package ares.ywq.com.bezierlearning.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 波浪
 * Created by ares on 2017/2/7.
 */

public class WaveView extends View {


    //控件长宽
    private int mRealHeight;
    private int mRealWidth;
    //波浪高度
    private int waveHeight;

    private float move = 0;

    private Path wavePath;
    private BezierCur.Point p1;
    private BezierCur.Point pControl1;
    private BezierCur.Point p2;
    private BezierCur.Point pControl2;
    private BezierCur.Point p3;
    private BezierCur.Point pControl3;
    private BezierCur.Point p4;
    private BezierCur.Point pControl4;
    private BezierCur.Point p5;

    private Path wavePath2;

    private BezierCur.Point p1_2;
    private BezierCur.Point pControl1_2;
    private BezierCur.Point p2_2;
    private BezierCur.Point pControl2_2;
    private BezierCur.Point p3_2;
    private BezierCur.Point pControl3_2;
    private BezierCur.Point p4_2;
    private BezierCur.Point pControl4_2;
    private BezierCur.Point p5_2;


    public WaveView(Context context) {
        super(context);
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        wavePath = new Path();
        wavePath2 = new Path();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                startPathAnim(1000);
            }
        }, 20);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //  Path wavePath = new Path();
        //reset效果一样

        wavePath2.reset();
        float disY_move = moveDis / 20;

        wavePath2.moveTo(p1_2.x + move, p1_2.y + disY_move);
        wavePath2.quadTo(pControl1_2.x + move, pControl1_2.y + disY_move, p2_2.x + move, p2_2.y + disY_move);
        wavePath2.quadTo(pControl2_2.x + move, pControl2_2.y + disY_move, p3_2.x + move, p3_2.y + disY_move);
        wavePath2.quadTo(pControl3_2.x + move, pControl3_2.y + disY_move, p4_2.x + move, p4_2.y + disY_move);
        wavePath2.quadTo(pControl4_2.x + move, pControl4_2.y + disY_move, p5_2.x + move, p5_2.y + disY_move);
        wavePath2.lineTo(p5_2.x + move, mRealHeight);
        wavePath2.lineTo(p1_2.x + move, mRealHeight);
        wavePath2.lineTo(p1_2.x + move, p1_2.y);
        wavePath2.close();

        Paint paint2 = new Paint();
        paint2.setColor(Color.parseColor("#3F51B5"));
        paint2.setAntiAlias(true);
        paint2.setStrokeWidth(2);
        paint2.setStyle(Paint.Style.FILL);
        paint2.setAlpha(60);
        canvas.drawPath(wavePath2, paint2);


        wavePath.reset();
        wavePath.moveTo(p1.x + move, p1.y);
        wavePath.quadTo(pControl1.x + move, pControl1.y, p2.x + move, p2.y);
        wavePath.quadTo(pControl2.x + move, pControl2.y, p3.x + move, p3.y);
        wavePath.quadTo(pControl3.x + move, pControl3.y, p4.x + move, p4.y);
        wavePath.quadTo(pControl4.x + move, pControl4.y, p5.x + move, p5.y);
        wavePath.lineTo(p5.x + move, mRealHeight);
        wavePath.lineTo(p1.x + move, mRealHeight);
        wavePath.lineTo(p1.x + move, p1.y);

        wavePath.close();
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#3F51B5"));
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawPath(wavePath, paint);

    }


    private float moveDis = 0;
    private float lastY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float y = event.getY();
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
//                moveDis=moveDis-lastY+y;
//                Log.v("dis","moveDis="+moveDis);
//
//                if(p1_2.y+moveDis/20-10>=p1.y){
//                    moveDis=0;
//
//                }
                break;
            case MotionEvent.ACTION_UP:

                break;

        }


        return super.onTouchEvent(event);
    }

    /**
     * 循环动画
     *
     * @param duration
     */
    public void startPathAnim(final long duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mRealWidth);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                move = value;
                postInvalidate();
            }

        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

                move = 0;
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        //无限重复
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (p1 == null) {


            //测量控件的真实长宽
            mRealHeight = getMeasuredHeight() - getPaddingBottom() - getPaddingTop();
            mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();

            waveHeight = mRealHeight / 20;
            p1 = new BezierCur.Point(-mRealWidth, mRealHeight * 2 / 3);
            pControl1 = new BezierCur.Point(-mRealWidth * 3 / 4, mRealHeight - mRealHeight / 3 - waveHeight);
            p2 = new BezierCur.Point(-mRealWidth / 2, mRealHeight * 2 / 3);
            pControl2 = new BezierCur.Point(-mRealWidth / 4, mRealHeight * 2 / 3 + waveHeight);
            p3 = new BezierCur.Point(0, mRealHeight * 2 / 3);

            pControl3 = new BezierCur.Point(mRealWidth / 4, mRealHeight - mRealHeight / 3 - waveHeight);
            p4 = new BezierCur.Point(mRealWidth / 2, mRealHeight * 2 / 3);
            pControl4 = new BezierCur.Point(mRealWidth * 3 / 4, mRealHeight * 2 / 3 + waveHeight);
            p5 = new BezierCur.Point(mRealWidth, mRealHeight * 2 / 3);


            int dis = mRealWidth / 8;
            int disY = mRealHeight / 80;
            p1_2 = new BezierCur.Point(-mRealWidth + dis, mRealHeight * 2 / 3 - disY);
            pControl1_2 = new BezierCur.Point(-mRealWidth * 3 / 4 + dis, mRealHeight - mRealHeight / 3 - waveHeight - disY);
            p2_2 = new BezierCur.Point(-mRealWidth / 2 + dis, mRealHeight * 2 / 3 - disY);
            pControl2_2 = new BezierCur.Point(-mRealWidth / 4 + dis, mRealHeight * 2 / 3 + waveHeight - disY);
            p3_2 = new BezierCur.Point(0 + dis, mRealHeight * 2 / 3 - disY);

            pControl3_2 = new BezierCur.Point(mRealWidth / 4 + dis, mRealHeight - mRealHeight / 3 - waveHeight - disY);
            p4_2 = new BezierCur.Point(mRealWidth / 2 + dis, mRealHeight * 2 / 3 - disY);
            pControl4_2 = new BezierCur.Point(mRealWidth * 3 / 4 + dis, mRealHeight * 2 / 3 + waveHeight - disY);
            p5_2 = new BezierCur.Point(mRealWidth + dis, mRealHeight * 2 / 3 - disY);


        }
    }

}
