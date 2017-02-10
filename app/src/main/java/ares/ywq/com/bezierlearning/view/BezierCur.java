package ares.ywq.com.bezierlearning.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 绘制贝塞尔曲线
 * Created by ares on 2017/2/2.
 */

public class BezierCur extends View {

    public BezierCur(Context context) {
        super(context);
    }

    public BezierCur(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //控件长宽
    private int mRealHeight;
    private int mRealWidth;

    //用于记录画布上的点
    private List<Point> points = new ArrayList<>();

    //默认为二阶
    private MODE mode = MODE.LEVEL2;

    public MODE getMode() {
        return mode;
    }

    public void setMode(MODE mode) {
        this.mode = mode;
    }

    //曲线是否闭合
    private boolean pathClose = false;

    public boolean isPathClose() {
        return pathClose;
    }

    public void setPathClose(boolean pathClose) {
        this.pathClose = pathClose;
        postInvalidate();
    }

    /**
     * 贝塞尔阶数
     */
    public   enum MODE {
        LEVEL2, LEVEL3
    }

    //用于测量 path，应用于路径动画
    private PathMeasure mPathMeasure;

    //用于保存动画上的圆的位置
    private float[] mCurrentPosition = new float[2];

    private boolean dismiss;


    //记录手指点击的点的位置
    private int currentPosition = -1;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //创建画笔
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#000000"));
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);


        for (int i = 0; i < points.size(); i++) {

            //绘制点与点之间的线条
            if (points.size() != 1) {
                if (i + 1 < points.size()) {
                    canvas.drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y, paint);
                }
            }

            //如果当前是第二个点或者是三阶模式下的第三个点，则属于控制点
            if (i == 1 || (mode == MODE.LEVEL3 && i == 2)) {

                Paint controlPaint = new Paint();
                controlPaint.setColor(Color.parseColor("#ffff17"));
                controlPaint.setAntiAlias(true);
                controlPaint.setStrokeWidth(2);
                controlPaint.setTextSize(30);
                canvas.drawCircle(points.get(i).x, points.get(i).y, 10, controlPaint);
                canvas.drawText("控制点"+(i)+("("+points.get(i).x+","+points.get(i).y)+")", points.get(i).x + 15, points.get(i).y, controlPaint);
            }
            //否则为起点或终点
            else {
                Paint beginOrEndPaint = new Paint();
                beginOrEndPaint.setColor(Color.parseColor("#000000"));
                beginOrEndPaint.setAntiAlias(true);
                beginOrEndPaint.setStrokeWidth(2);
                beginOrEndPaint.setTextSize(30);
                canvas.drawCircle(points.get(i).x, points.get(i).y, 10, beginOrEndPaint);
                if (i == 0) {
                    canvas.drawText("起点"+("("+points.get(i).x+","+points.get(i).y)+")", points.get(i).x + 15, points.get(i).y, beginOrEndPaint);
                }
                //三阶模式下的第4个点为终点
                if (mode == MODE.LEVEL3 && i == 3) {
                    canvas.drawText("终点"+("("+points.get(i).x+","+points.get(i).y)+")", points.get(i).x + 15, points.get(i).y, beginOrEndPaint);
                }
                //二阶模式下的第3个点为终点
                if (mode == MODE.LEVEL2 && i == 2) {
                    canvas.drawText("终点"+("("+points.get(i).x+","+points.get(i).y)+")", points.get(i).x + 15, points.get(i).y, beginOrEndPaint);
                }

            }
        }


        Paint paintLine = new Paint();
        paintLine.setColor(Color.parseColor("#ffffff"));
        paintLine.setAntiAlias(true);
        paintLine.setStrokeWidth(8);
        if(!isPathClose()){
            paintLine.setStyle(Paint.Style.STROKE);
        }

        if (mode == MODE.LEVEL2) {
            //当前为二阶模式，并且点数量为3
            if (points.size() == 3) {

                Point beginPoint = points.get(0);

                Path path = new Path();
                path.moveTo(beginPoint.x, beginPoint.y);
                path.quadTo(points.get(1).x, points.get(1).y, points.get(2).x, points.get(2).y);



                mPathMeasure = new PathMeasure(path, false);
                //绘制贝塞尔曲线
                canvas.drawPath(path, paintLine);

                //如果动画未消失
                if (!dismiss) {
                    Paint paintPoint = new Paint();
                    paintPoint.setColor(Color.parseColor("#3F51B5"));
                    paintPoint.setAntiAlias(true);
                    paintPoint.setStrokeWidth(10);
                    //动态更改圆的位置，产生动画效果
                    canvas.drawCircle(mCurrentPosition[0], mCurrentPosition[1], 10, paintPoint);
                }

            }


        } else if (mode == MODE.LEVEL3) {
            //当前为三阶模式，并且点数量为4
            if (points.size() == 4) {

                Point beginPoint = points.get(0);
                Path path = new Path();
                path.moveTo(beginPoint.x, beginPoint.y);
                path.cubicTo(points.get(1).x, points.get(1).y, points.get(2).x, points.get(2).y, points.get(3).x, points.get(3).y);

                mPathMeasure = new PathMeasure(path, false);
                //绘制贝塞尔曲线
                canvas.drawPath(path, paintLine);
                //如果动画未消失
                if (!dismiss) {

                    Paint paintPoint = new Paint();
                    paintPoint.setColor(Color.parseColor("#3F51B5"));
                    paintPoint.setAntiAlias(true);
                    paintPoint.setStrokeWidth(10);
                    //动态更改圆的位置，产生动画效果
                    canvas.drawCircle(mCurrentPosition[0], mCurrentPosition[1], 10, paintPoint);


                }

            }

        }


    }

      int i =0;
    /**
     * 路径动画
     * @param duration
     */
    public void startPathAnim(final long duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(duration);
        // 减速插值器
        valueAnimator.setInterpolator(new DecelerateInterpolator());

        final Point point0=points.get(0);
        final Point point1=points.get(1);
        final Point point2=points.get(2);


        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                // 获取当前点坐标封装到mCurrentPosition
                mPathMeasure.getPosTan(value, mCurrentPosition, null);


//                float t = (float)((++i)/(0.06*duration));
//                Log.v("value","value="+value+",t="+(t )+",TIME="+animation.getCurrentPlayTime());
//
//                mCurrentPosition[0]=getBezierPoint("X",point0,point1,point2,t);
//                mCurrentPosition[1]=getBezierPoint("Y",point0,point1,point2,t);

                postInvalidate();
            }

        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

                i=0;
                dismiss = false;

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                dismiss = true;
                i=0;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        valueAnimator.start();

    }

    public float getBezierPoint(String xOrY,final Point point0,final Point point1,final Point point2,float t){
        if("X".equals(xOrY)){
            return point0.x*(1-t)*(1-t)+2*t*(1-t)*point1.x+t*t*point2.x;

        }else{
            return point0.y*(1-t)*(1-t)+2*t*(1-t)*point1.y+t*t*point2.y;

        }

    }



    /**
     * 封装用于记录坐标点
     */
    public static class Point {

        public float x;
        public float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }


        @Override
        public String toString() {
            return "x="+x+",y="+y;
        }
    }


    /**
     * 清理画布内容
     */
    public void clearCanvas() {
        //清空画布中的点
        points.clear();
        //重置
        currentPosition = -1;
        postInvalidate();

    }


    /**
     * 重写touch 事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if(event.getPointerCount()==1){
        switch (event.getAction()) {


            //手指移动画布上的点
            case MotionEvent.ACTION_MOVE:

                if (currentPosition >=0) {

                    //手指移动的点
                    if (points.size() == 4 || points.size() == 3) {
                        if (x >= mRealWidth) {
                            x = mRealWidth;
                        } else if (x <= 0) {
                            x = 0;
                        }
                        if (y >= mRealHeight) {
                            y = mRealHeight;
                        } else if (y <= 0) {
                            y = 0;
                        }


                        Point newPoint = new Point(x, y);

                        //将图上的点更替为新的点
                        points.set(currentPosition, newPoint);

                    }

                }

                break;


            case MotionEvent.ACTION_DOWN:

                Point thisPoint = new Point(x, y);
                for (int i = 0; i < points.size(); i++) {
                    Point lastPoint = points.get(i);
                    //判断点击的范围
                    if (Math.abs(thisPoint.x - lastPoint.x) < 50 && Math.abs(thisPoint.y - lastPoint.y) < 50) {
                        //如在范围内，则保存被点击的点的位置
                        currentPosition = i;
                        break;
                    } else {
                        currentPosition = -1;
                    }
                }

                if (mode == MODE.LEVEL3) {

                    if (points.size() != 4) {
                        points.add(thisPoint);
                        currentPosition=-2;

                    }

                } else if (mode == MODE.LEVEL2) {

                    if (points.size() != 3) {
                        points.add(thisPoint);
                        currentPosition=-2;

                    }

                }

                break;

            case MotionEvent.ACTION_UP:
                if (mode == MODE.LEVEL3) {

                    if (points.size() == 4) {

                        if (currentPosition != -1)
                            postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startPathAnim(1200);
                                }
                            }, 200);

                    }
                }
                if (mode == MODE.LEVEL2) {

                    if (points.size() == 3) {
                        if (currentPosition != -1)
                            postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startPathAnim(1000);
                                }
                            }, 200);

                    }
                }


                break;
        }}

        postInvalidate();
        //需返回 true
        return true;
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量控件的真实长宽
        mRealHeight = getMeasuredHeight() - getPaddingBottom() - getPaddingTop();
        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();

    }


}
