package com.tgc.appledora.habitica.streakCounter.ui.detail_task;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.gelitenight.waveview.library.WaveView;

import java.util.ArrayList;
import java.util.List;

public class WaveHelper {
    private static final int DEFAULT_ANIMATION_DURATION = 1000;

    private WaveView mWaveView;

    private AnimatorSet mAnimatorSet;
    private ObjectAnimator waterLevelAnim;

    private TaskDetailViewModel viewModel;

    private Handler hanlder;
    private Runnable runnable;


    public WaveHelper(TaskDetailViewModel viewModel, WaveView waveView) {
        this.viewModel = viewModel;
        mWaveView = waveView;
        initAnimation();
        hanlder = new Handler();
        runnable = viewModel::taskCompleted;
    }

    private void initAnimation() {
        List<Animator> animators = new ArrayList<>();

        // horizontal animation.
        // wave waves infinitely.
        ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(
                mWaveView, "waveShiftRatio", 0f, 1f);
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setDuration(1000);
        waveShiftAnim.setInterpolator(new LinearInterpolator());
        animators.add(waveShiftAnim);

        // vertical animation.
        // water level increases from 0 to center of WaveView
        float waterLevel = (viewModel.getElapsedTime() * 2.0f) / viewModel.getMinutesInMilliSeconds();

        long timeDuration = viewModel.getMinutesInMilliSeconds() != 0 ? viewModel.getMinutesInMilliSeconds() - viewModel.getElapsedTime() : DEFAULT_ANIMATION_DURATION;
        waterLevelAnim = ObjectAnimator.ofFloat(
                mWaveView, "waterLevelRatio", waterLevel, 1f);
        waterLevelAnim.setDuration(timeDuration);
        waterLevelAnim.setInterpolator(new LinearInterpolator());
        animators.add(waterLevelAnim);

        // amplitude animation.
        // wave grows big then grows small, repeatedly
        ObjectAnimator amplitudeAnim = ObjectAnimator.ofFloat(
                mWaveView, "amplitudeRatio", 0.0001f, 0.05f);
        amplitudeAnim.setRepeatCount(ValueAnimator.INFINITE);
        amplitudeAnim.setRepeatMode(ValueAnimator.REVERSE);
        amplitudeAnim.setDuration(5000);
        amplitudeAnim.setInterpolator(new LinearInterpolator());
        animators.add(amplitudeAnim);

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animators);
    }

    public void startAnimation() {
        Log.d("STREAKY", "" + mAnimatorSet.getDuration());
        if (mAnimatorSet != null) {
            mAnimatorSet.start();
            startHandler();
        }
    }

    public void pauseAnimation() {
        stopHandler();
        Log.d("STREAKY", "" + mAnimatorSet.getDuration());
        if (mAnimatorSet != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mAnimatorSet.pause();
            } else {
                mAnimatorSet.cancel();
            }
        }
    }

    public void resumeAnimation() {
        Log.d("STREAKY", "" + mAnimatorSet.getDuration());
        if (mAnimatorSet != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mAnimatorSet.resume();
            } else {
                if (viewModel.getElapsedTime() != 0 && viewModel.getElapsedTime() <= viewModel.getMinutesInMilliSeconds()) {
                    setWaterLevelHeight((viewModel.getElapsedTime() * 1.0f) / viewModel.getMinutesInMilliSeconds());
                } else {
                    setWaterLevelHeight(0);
                }
                mAnimatorSet.start();
            }
            startHandler();
        }
    }

    public void endAnimation() {
        if (mAnimatorSet != null) {
            mAnimatorSet.end();
        }
    }

    private void startHandler() {
        hanlder.postDelayed(runnable, viewModel.getMinutesInMilliSeconds() - viewModel.getElapsedTime());
    }

    /**
     * task has been stopped
     */
    private void stopHandler() {
        hanlder.removeCallbacks(runnable);
    }


    private void setWaterLevelHeight(float waterLevelHeight) {
        waterLevelAnim.setFloatValues(waterLevelHeight, 1f);
    }
}

