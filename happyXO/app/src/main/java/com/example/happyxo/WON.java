package com.example.happyxo;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class WON extends AppCompatActivity {
    ImageView img1,img2,img3,img4,img5,img6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won);
        img1=(ImageView) findViewById(R.id.img1);
        img2=(ImageView) findViewById(R.id.img2);
        img3=(ImageView) findViewById(R.id.img3);
        img4=(ImageView) findViewById(R.id.img4);
        img5=(ImageView) findViewById(R.id.img5);
        img6=(ImageView) findViewById(R.id.img6);
        addAnimtion(img1,true);
        addAnimtion(img2,false);
        addAnimtion(img3,false);
        addAnimtion(img4,false);
        addAnimtion(img5,true);
        addAnimtion(img6,false);
        CloseActivity();
    }

    private void CloseActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Utils.goToActivity(WON.this,MainActivity2.class);
                finish();
            }
        }, 14000);
    }

    private void addAnimtion(ImageView img,boolean isTop) {
        if (isTop){
            Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_top_result_activity);
            img.startAnimation(animation2);
        }else{
            Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_bottom_finish_activity);
            img.startAnimation(animation2);
        }
    }
}