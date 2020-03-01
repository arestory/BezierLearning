package ares.ywq.com.bezierlearning.adapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import ares.ywq.com.bezierlearning.R;
import ares.ywq.com.bezierlearning.entity.ProgressItem;
import ares.ywq.com.bezierlearning.view.WaveProgressView;

public class ProgressAdapter extends BaseQuickAdapter<ProgressItem, BaseViewHolder> {
    public ProgressAdapter(List<ProgressItem> data) {
        super(R.layout.item_progress, data);
    }

    public ProgressAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ProgressItem progressItem) {

        WaveProgressView waveProgressView = baseViewHolder.getView(R.id.waveProgress);
        waveProgressView.setProgress(progressItem.getProgress()*1.0f/100);
        if(progressItem.isWaving()&&(!waveProgressView.isWaving())){
//            startWavingAnim(waveProgressView,1000);
        }
    }


    public void startWavingAnim(final WaveProgressView waveProgress,final long duration) {
        waveProgress.post(new Runnable() {
            @Override
            public void run() {

                ValueAnimator waveAnimator = ValueAnimator.ofFloat(0, waveProgress.getSideLength()*2);
                waveAnimator.setDuration(duration);
                waveAnimator.setInterpolator(new LinearInterpolator());
                waveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        //改变波浪的 x 值
                        float  waveMoveX = (Float) animation.getAnimatedValue();
                        waveProgress.changeMoveX(waveMoveX);
                    }

                });
                waveAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

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
                waveAnimator.setRepeatCount(ValueAnimator.INFINITE);
                waveAnimator.start();
            }
        });

    }

}

