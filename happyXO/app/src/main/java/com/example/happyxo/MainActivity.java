package com.example.happyxo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logo=(ImageView) findViewById(R.id.logo);
        loadSharedSettings();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_splach);
        logo.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Utils.goToActivity(MainActivity.this,MainActivity2.class);
                finish();
            }
        }, 3000);//3000 is total duration of animation
    }
    private void loadSharedSettings() {
        SharedPreferences preferences = getSharedPreferences("XOSettings", MODE_PRIVATE);
        Settings.user_name=preferences.getString("user_name","USER");
        Settings.player_is_X=preferences.getBoolean("player_is_X",true);
        Settings.N_cells=preferences.getInt("N_cells",3);
        Settings.AI_level=preferences.getInt("AI_level",1);
        Settings.timed_level=preferences.getInt("timed_level",0);

    }

}