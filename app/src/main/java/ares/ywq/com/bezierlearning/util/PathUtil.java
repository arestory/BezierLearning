package ares.ywq.com.bezierlearning.util;

import android.graphics.Path;

/**
 * Created by ares on 2017/2/10.
 */

public class PathUtil {

    /**
     * 绘制等边三角形的路径
     * @param x 等边三角形的中心点 x
     * @param y 等边三角形的中心点 y
     * @param length 等边三角形的边长
     * @return 等边三角形的路径
     */
    public static Path drawPerfectTriangle(float x,float y,float length){
        Path trianglePath = new Path();
        //根据等边三角形的特性，计算各顶点的坐标
        trianglePath.moveTo(x+(float)(length*Math.sqrt(3)/3),y);
        trianglePath.lineTo(x-(length-(float)(length*Math.sqrt(3)/3)),y-length/2);;
        trianglePath.lineTo(x-(length-(float)(length*Math.sqrt(3)/3)),y+length/2);
        trianglePath.close();

        return trianglePath;
    }

}
