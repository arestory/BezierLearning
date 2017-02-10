package ares.ywq.com.bezierlearning.view;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import ares.ywq.com.bezierlearning.R;

/**
 * 橡皮筋效果
 * Created by ares on 2017/2/8.
 */

public class FunnyBandLoading extends View {



    //控件长宽
    private int mRealHeight;
    private int mRealWidth;
    //橡皮筋峰值
    private int bandMax;

    private float position=0;

    //二阶贝塞尔曲线三个点
    private BezierCur.Point p1;
    private BezierCur.Point pControl;
    private BezierCur.Point p2;

    private BezierCur.Point ballPoint;//小球位置

    public FunnyBandLoading(Context context) {
        super(context);

    }

    private int ballColor;//当前球的颜色
    private int ballUpColor; //球在最高点的颜色
    private int ballDownColor;//球在最低点的颜色
    private int pointColor;//两边圆的颜色
    private int bandColor;//橡皮筋颜色

    public FunnyBandLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array =  context.obtainStyledAttributes(attrs, R.styleable.FunnyBandLoading);

        ballUpColor=array.getColor(R.styleable.FunnyBandLoading_ballUpColor,Color.parseColor("#3F51B5"));
        ballDownColor=array.getColor(R.styleable.FunnyBandLoading_ballDownColor,Color.parseColor("#3F51B5"));
        pointColor=array.getColor(R.styleable.FunnyBandLoading_pointColor,Color.parseColor("#000000"));
        bandColor=array.getColor(R.styleable.FunnyBandLoading_bandColor,Color.parseColor("#fa6c0e"));

        ballColor=ballDownColor;

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint ballPaint = new Paint();
        ballPaint.setColor(ballColor);
        ballPaint.setAntiAlias(true);
        ballPaint.setStrokeWidth(2);


        canvas.drawCircle(ballPoint.x,ballPoint.y+position,20,ballPaint);

        Paint pathPaint= new Paint();
        pathPaint.setColor(bandColor);
        pathPaint.setAntiAlias(true);
        pathPaint.setStrokeWidth(10);
        pathPaint.setStyle(Paint.Style.STROKE);


        Path path =new Path();
        path.moveTo(p1.x,p1.y);
        path.quadTo(pControl.x,pControl.y,p2.x,p2.y);
        canvas.drawPath(path,pathPaint);


        Paint pointPaint= new Paint();
        pointPaint.setColor(pointColor);
        pointPaint.setAntiAlias(true);
        pointPaint.setStrokeWidth(2);
        canvas.drawCircle(p1.x,p1.y,15,pointPaint);
        canvas.drawCircle(p2.x,p2.y,15,pointPaint);


    }


    //下落动画
    private ValueAnimator downValueAnimator;
    //上升动画
    private ValueAnimator upValueAnimator;
    //抖动动画
    private ValueAnimator shakeAnimator;



    /**
     * 开始动画
     * @param duration
     */
    public void startAnim(final long duration){
         startDown(duration);
    }

    /**
     * 下落动画
     * @param duration 下落时间
     */
    private  void startDown(final long duration) {
        //根据贝塞尔曲线求出最低点的y坐标,由于对称所以最低点 t=0.5
        float yMin = (float)((1-0.5)*(1-0.5)*p1.y+2*0.5*(1-0.5)*(pControl.y+bandMax)+0.5*0.5*p2.y);

        final float dis = yMin-ballPoint.y;
        downValueAnimator = ValueAnimator.ofFloat(0,dis);
        downValueAnimator.setDuration(duration);
        downValueAnimator.setInterpolator(new AccelerateInterpolator());


        changeBallColor(ballDownColor,ballUpColor);


        downValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float value = (Float) animation.getAnimatedValue();

                //根据贝塞尔二阶公式反推控制点的坐标
                float newY = (float)((ballPoint.y+position-0.5*0.5*p1.y-0.5*0.5*p2.y)/(2*0.5*0.5));

                position=value;
                if(ballPoint.y+position>=p1.y){
                    pControl=new BezierCur.Point(pControl.x,newY+40);
                }


                postInvalidate();


            }

        });
        downValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {



            }

            @Override
            public void onAnimationEnd(Animator animation) {

                startShake(2000);

                startUp(1000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        downValueAnimator.start();

    }



    /**
     * 改变球的颜色,颜色过渡动画
     * @param fromColor 起点颜色
     * @param toColor 终点颜色
     */
    public void changeBallColor(int fromColor,int toColor){
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(),fromColor,toColor);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {

                ballColor=(Integer)animator.getAnimatedValue();
            }
        });
        colorAnimation.setDuration(1000);
        colorAnimation.start();
    }

    /**
     * 上升动画
     * @param duration 上升时间
     */
    public void startUp(final long duration) {
        float dis = position;
        upValueAnimator = ValueAnimator.ofFloat(dis,0);
        upValueAnimator.setDuration(duration);
        upValueAnimator.setInterpolator(new DecelerateInterpolator());

        //changeBallColor(1);
        changeBallColor(ballUpColor,ballDownColor);
        upValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float value = (Float) animation.getAnimatedValue();

                position=value;

                //根据贝塞尔二阶公式反推控制点的坐标
                float newY = (float)((ballPoint.y+position-0.5*0.5*p1.y-0.5*0.5*p2.y)/(2*0.5*0.5));

                if(ballPoint.y+position+50<=p1.y){
                    //pControl=new BezierCur.Point(pControl.x,p1.y);

                }else{
                    pControl=new BezierCur.Point(pControl.x,newY+50);

                }

                postInvalidate();


            }

        });
        upValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {


            }

            @Override
            public void onAnimationEnd(Animator animation) {

                startDown(1000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        if(!upValueAnimator.isRunning()){
            upValueAnimator.start();

        }

    }


    /**
     * 橡皮筋的抖动
     * @param duration
     */
    public void startShake(final long duration){

        shakeAnimator = ValueAnimator.ofFloat(0,40,0,-40,0,20,0,-30,0,10,0);
        //shakeAnimator = ValueAnimator.ofFloat(0,40);
        shakeAnimator.setDuration(duration);
        shakeAnimator.setInterpolator(new DecelerateInterpolator());


        shakeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float value = (Float) animation.getAnimatedValue();


                //根据贝塞尔二阶公式反推控制点的坐标
                float newY = (float)((p1.y-value-0.5*0.5*p1.y-0.5*0.5*p2.y)/(2*0.5*0.5));

                pControl=new BezierCur.Point(pControl.x,newY);

                postInvalidate();


            }

        });
        shakeAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {



            }

            @Override
            public void onAnimationEnd(Animator animation) {

               // startDown(1000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        if(!shakeAnimator.isRunning()){
            shakeAnimator.start();

        }

    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量控件的真实长宽
        mRealHeight = getMeasuredHeight() - getPaddingBottom() - getPaddingTop();
        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();

        int min = mRealHeight>mRealWidth?mRealWidth:mRealHeight;
        //取两者最小值,并设置为正方形
        setMeasuredDimension(min,min);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int padding=w/5;

        bandMax =h/4;
        p1=new BezierCur.Point(padding,h*2/3);
        pControl=new BezierCur.Point(w/2,h*2/3);
        p2=new BezierCur.Point(w-padding,h*2/3);
        ballPoint = new BezierCur.Point(w/2,h/5);
        startAnim(1000);

        Log.v("p1",p1.toString());
        Log.v("p2",p2.toString());
        Log.v("pControl",pControl.toString());
    }
}
