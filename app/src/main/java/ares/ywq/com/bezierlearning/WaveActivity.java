package ares.ywq.com.bezierlearning;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileReader;

import ares.ywq.com.bezierlearning.view.WaveProgress;
import ares.ywq.com.bezierlearning.view.WaveView;

/**
 * Created by ares on 2017/2/7.
 */

public class WaveActivity extends Activity  {

    private WaveProgress waveProgress;
    private WaveView waveView;
    private SeekBar seekBar;
    private float progress=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave);
        CommonBar.init(this, "基于贝塞尔曲线绘制波浪", new CommonBar.Callback() {
            @Override
            public void onBackClick(View v) {
                finish();
            }
        });


        Log.v("memory ava",getAvailMemory());
        Log.v("memory total",getTotalMemory());


        seekBar =(SeekBar)findViewById(R.id.seekBar);
        waveView=(WaveView)findViewById(R.id.waveView);
        waveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                waveView.setVisibility(View.GONE);

            }
        });
        waveProgress=(WaveProgress)findViewById(R.id.waveProgress);
        waveProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(waveProgress.isPaused()){
                    waveProgress.recover();
                }else{
                    waveProgress.onPause();
                }
            }
        });
        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                Log.v("progress",progress+"%");

                waveProgress.setProgress((float)(progress/100.0));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //设置波浪的百分比为当前手机可用内存与总内存的比例
        waveProgress.setProgress(getAvaPercent());

    }

    private float  getAvaPercent(){

        float percent=0;
        String availMemory =getAvailMemory();
         int ava=0;
        float total=1;
        try{
            if(availMemory.contains("MB")){

                availMemory=availMemory.replace("MB","").replace(" ","");
                ava=Integer.parseInt(availMemory);
                Log.v("ava","ava="+ava);
            }
            String totalMemory = getTotalMemory();
            if(totalMemory.contains("GB")){
                totalMemory=totalMemory.replace("GB","").replace(" ","");
                total= Float.parseFloat(totalMemory);
                Log.v("total","total="+total);

            }

            percent=ava/(total*1000);
            Log.v("percent","percent="+percent);
        }catch (Exception e){
            e.printStackTrace();
        }




        return percent;


    }


    private String getAvailMemory() {// 获取android当前可用内存大小
          
            ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            am.getMemoryInfo(mi);
            //mi.availMem; 当前系统的可用内存 
          
            return Formatter.formatFileSize(getBaseContext(), mi.availMem);// 将获取的内存大小规格化
          }

    private String getTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }

            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();

        } catch (Exception e) {
        }
        return Formatter.formatFileSize(getBaseContext(), initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }



}
