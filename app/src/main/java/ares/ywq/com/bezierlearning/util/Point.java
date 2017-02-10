package ares.ywq.com.bezierlearning.util;

/**
 * 封装坐标点
 * Created by ares on 2017/2/10.
 */

public final class Point {

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
