package com.corporation8793.sewingmachine;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

        LinearLayout layout = findViewById(R.id.image_linear);
        final ImageView imageView = findViewById(R.id.logo_icon);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//
//        int deviceWidth = (int) ((displayMetrics.widthPixels /(double) 720) * 232);  // 핸드폰의 가로 해상도를 구함.
//        Log.e("deviceWidth",displayMetrics.widthPixels+"");
//        Log.e("deviceWidth devide",deviceWidth+"");
        int deviceHeight =(int) (displayMetrics.heightPixels /(double) 1280) *400 ;
////
//        LinearLayout.LayoutParams layoutParams =(LinearLayout.LayoutParams) layout.getLayoutParams();
//        layoutParams.topMargin = deviceHeight;
//        layout.setLayoutParams(layoutParams);

        translateScale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!isFirstAnimation) {
                    layout.clearAnimation();
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
                layout.clearAnimation();
                Log.e("???","뭐여");
                layout.startAnimation(translateScale);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        layout.startAnimation(hold);


    }
}
