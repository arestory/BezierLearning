package ares.ywq.com.bezierlearning;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 公共 bar
 * Created by ares on 2017/2/7.
 */

public class CommonBar {

    /**
     * 初始化 bar
     * @param context 上下文
     * @param title 标题
     * @param callback 返回按钮
     */
    public static void init(Context context, String title, final Callback callback){

        try{
            TextView textView = (TextView)((Activity)context).findViewById(R.id.title);
            textView.setText(title);
            Log.v(",","title="+title);
            Button backTtn =(Button)((Activity)context).findViewById(R.id.button);
            backTtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onBackClick(v);
                }
            });
        }catch (NullPointerException e){

            e.printStackTrace();
        }

    }

    public interface Callback{
        void onBackClick(View v);
    }


}

