package com.example.happyxo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity2 extends AppCompatActivity {
    RelativeLayout MainLayout;
    LinearLayout top,bottom,center;
    ImageButton withAI,userButton,Settings,about;
    ImageView img1,img3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        MainLayout=(RelativeLayout)findViewById(R.id.MainLayout);


        top=(LinearLayout)findViewById(R.id.TopLayout);
        top.setVisibility(View.INVISIBLE);//added to code not to xml to easy change xml
        bottom=(LinearLayout)findViewById(R.id.BottomLayout);
        bottom.setVisibility(View.INVISIBLE);
        img1=(ImageView)findViewById(R.id.img1);
        img3=(ImageView)findViewById(R.id.img3);

        withAI=(ImageButton) findViewById(R.id.AI_Button);
        userButton=(ImageButton) findViewById(R.id.Userutton);
        Settings=(ImageButton) findViewById(R.id.Settings);
        about=(ImageButton) findViewById(R.id.about_Button);

        setAnimationFirst();        //better to be oncreate not on start to work good


    }

    private void Button_WithAI() {
        withAI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.goToActivity(MainActivity2.this,XO_AI.class);
            }
        });
    }
    private void Button_User() {
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.goToActivity(MainActivity2.this,XO_user.class);
            }
        });
    }
    private void Button_Settings() {
        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.goToActivity(MainActivity2.this,configuration.class);
            }
        });
    }

    public void setAnimationFirst(){
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_activaty2_layout);
        MainLayout.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_top);
                top.setVisibility(View.VISIBLE);
                top.startAnimation(animation2);
                Animation animation3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom);
                bottom.setVisibility(View.VISIBLE);
                bottom.startAnimation(animation3);
            }

        });
    }
    protected void onStart() {
        super.onStart();
        addZoomAnimation(img1);
        addZoomAnimation(img3);
        Button_WithAI();
        Button_User();
        Button_Settings();
        Button_info();
    }

    private void Button_info() {
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.goToActivity(MainActivity2.this,AboutMe.class);
            }
        });
    }

    private void addZoomAnimation(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View vw, MotionEvent motionEvent) {
                Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_view);
                view.startAnimation(animation2);
                return false;
            }
        });
    }

}