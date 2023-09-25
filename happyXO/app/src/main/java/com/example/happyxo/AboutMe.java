package com.example.happyxo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AboutMe extends AppCompatActivity {
    LinearLayout info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        info=(LinearLayout)findViewById(R.id.infol);
        addAnimtion();
    }
    private void addAnimtion() {
            Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_about);
            info.startAnimation(animation2);
    }
}