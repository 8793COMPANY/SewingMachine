package com.cube.sewingmachine;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private boolean isFirstAnimation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        /*Simple hold animation to hold ImageView in the centre of the screen at a slightly larger
        scale than the ImageView's original one.*/
        Animation hold = AnimationUtils.loadAnimation(this, R.anim.hold);

        /* Translates ImageView from current position to its original position, scales it from
        larger scale to its original scale.*/
        final Animation translateScale = AnimationUtils.loadAnimation(this, R.anim.translate_scale);

        final ImageView imageView = findViewById(R.id.logo_icon);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = (displayMetrics.widthPixels / 720) * 232;  // 핸드폰의 가로 해상도를 구함.
        int deviceHeight = (displayMetrics.heightPixels / 1280) ;

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(deviceWidth,deviceHeight*140);
        layoutParams.topMargin = deviceHeight *265;
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        imageView.setLayoutParams(layoutParams);

        translateScale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!isFirstAnimation) {
                    imageView.clearAnimation();
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

                isFirstAnimation = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        hold.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.clearAnimation();
                Log.e("???","뭐여");
                imageView.startAnimation(translateScale);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imageView.startAnimation(hold);


    }
}
