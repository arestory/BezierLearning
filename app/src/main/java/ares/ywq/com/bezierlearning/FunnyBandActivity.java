package ares.ywq.com.bezierlearning;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ares.ywq.com.bezierlearning.view.FunnyBandLoading;

/**
 * Created by ares on 2017/2/8.
 */

public class FunnyBandActivity extends Activity {


    FunnyBandLoading bandLoading;
    Button startBtn,stopBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band);
        bandLoading=(FunnyBandLoading)findViewById(R.id.band);
        startBtn=(Button)findViewById(R.id.start);
        stopBtn=(Button)findViewById(R.id.stop);
        CommonBar.init(this, "橡皮筋", new CommonBar.Callback() {
            @Override
            public void onBackClick(View v) {
                finish();
            }
        });
        startBtn.setVisibility(View.GONE);
        stopBtn.setVisibility(View.GONE);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bandLoading.startAnim(1000);
            }
        });



    }
}
