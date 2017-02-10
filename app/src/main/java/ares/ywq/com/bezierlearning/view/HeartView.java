package ares.ywq.com.bezierlearning.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import ares.ywq.com.bezierlearning.R;

/**
 * Created by ares on 2017/2/2.
 */
public class HeartView extends View {

    private int mRealHeight;
    private int mRealWidth;

    private int sideLength;//边长

    private int heartColor;

    public HeartView(Context context) {
        super(context);
    }

    public HeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array =  context.obtainStyledAttributes(attrs, R.styleable.HeartView);

        heartColor=array.getColor(R.styleable.HeartView_heartColor,Color.parseColor("#ccffcc"));

    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(heartColor);
        paint.setStrokeWidth(6);
        paint.setAntiAlias(false);


        Paint linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#ffffff"));
        linePaint.setStrokeWidth(5);
        linePaint.setAntiAlias(false);



        // 绘制心形
        Path path = new Path();
        path.moveTo(sideLength/2,sideLength/4);
        path.cubicTo((sideLength*6)/7,sideLength/9,(sideLength*12)/13,(sideLength*2)/5, pointXBottom, pointYBottom);
        canvas.drawPath(path,paint);

        Path path2 = new Path();
        path2.moveTo(sideLength/2,sideLength/4);
        path2.cubicTo(sideLength / 7, sideLength / 9, sideLength / 13, (sideLength * 2) / 5, pointXBottom, pointYBottom);
        canvas.drawPath(path2,paint);



        canvas.drawLine(sideLength/2,sideLength/4,(sideLength*6)/7,sideLength/9,linePaint);
        canvas.drawLine((sideLength*6)/7,sideLength/9,(sideLength*12)/13,(sideLength*2)/5,linePaint);
        canvas.drawLine((sideLength*12)/13,(sideLength*2)/5, pointXBottom, pointYBottom,linePaint);

        canvas.drawLine(sideLength/2,sideLength/4,sideLength / 7, sideLength / 9,linePaint);
        canvas.drawLine(sideLength / 7, sideLength / 9,sideLength / 13, (sideLength * 2) / 5,linePaint);
        canvas.drawLine(sideLength / 13, (sideLength * 2) / 5, pointXBottom, pointYBottom,linePaint);

    }

    private float pointXBottom =0;
    private float pointYBottom =0;



    private float pointXTop =0;
    private float pointYTop =0;




    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.v("event",event.getX()+","+event.getY());


        float x = event.getX();
        float y = event.getY();



        pointXBottom =event.getX();
        pointYBottom =event.getY();



        postInvalidate();



        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mRealHeight=getMeasuredHeight()-getPaddingBottom()-getPaddingTop();
        mRealWidth=getMeasuredWidth()-getPaddingLeft()-getPaddingRight();

        int min = mRealWidth<mRealHeight?mRealWidth:mRealHeight;
        //取最小值并设置为正方形
        setMeasuredDimension(min,min);
    }






    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        sideLength=w;
        pointXBottom =sideLength/2;
        pointYBottom =(sideLength*8)/12;
    }


}
