package ares.ywq.com.bezierlearning;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import ares.ywq.com.bezierlearning.view.BezierCur;

public class DrawBezierCurActivity extends Activity    {

    private BezierCur bezierCur;
    private Button clearBtn;
    private RadioGroup radioGroup;
    private RadioGroup levelGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        bezierCur = (BezierCur)findViewById(R.id.bezierCur);
        clearBtn=(Button)findViewById(R.id.clearBtn);
        radioGroup=(RadioGroup)findViewById(R.id.radio);
        levelGroup=(RadioGroup)findViewById(R.id.level);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.close){
                    bezierCur.setPathClose(true);
                }else
                if(checkedId==R.id.open){
                    bezierCur.setPathClose(false);
                }
            }
        });
        levelGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                if(bezierCur.getMode()==BezierCur.MODE.LEVEL2){
                    bezierCur.clearCanvas();
                    bezierCur.setMode(BezierCur.MODE.LEVEL3);
                     Toast.makeText(DrawBezierCurActivity.this,"请在下方任意点击4个点",Toast.LENGTH_LONG).show();

                }else{
                    bezierCur.clearCanvas();
                    Toast.makeText(DrawBezierCurActivity.this,"请在下方任意点击3个点",Toast.LENGTH_LONG).show();

                    bezierCur.setMode(BezierCur.MODE.LEVEL2);
                 }


            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bezierCur.clearCanvas();
            }
        });

        CommonBar.init(this, "动态绘制贝塞尔曲线", new CommonBar.Callback() {
            @Override
            public void onBackClick(View v) {
                finish();
            }
        });

    }


}
