package com.example.happyxo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Set;

public class configuration extends AppCompatActivity {
            SeekBar AI,time,matrix;
            Button apply,cancel;
            EditText userName,userIsX;
            TextView AItxt,timetxt,matrixtxt;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_configuration);
                apply=(Button)findViewById(R.id.apply);
                cancel=(Button)findViewById(R.id.cancel);
                userName=(EditText) findViewById(R.id.userName);
                userIsX=(EditText) findViewById(R.id.userIsX);
                AI=(SeekBar)findViewById(R.id.seek_AI);
                time=(SeekBar)findViewById(R.id.seek_TIME);
                matrix=(SeekBar)findViewById(R.id.seekMatrix);
                AItxt=(TextView) findViewById(R.id.text_AI);
                timetxt=(TextView) findViewById(R.id.text_Time);
                matrixtxt=(TextView) findViewById(R.id.textMatrix);
                setToDefaultSettings();
                setCellsListener();
                setAIListener();
                setTimeListener();
                setApplyClick();
                setCancelClick();

            }

    private void setCancelClick() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utils.goToActivity(configuration.this,MainActivity2.class);

            }
        });
    }

    private void setTimeListener() {
        time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser){
                timetxt.setText("TIME LEVEL  :"+progress/40);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar){}
        });
    }

    private void setAIListener() {
        AI.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser){
                AItxt.setText("AI LEVEL :"+progress/40);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar){}
        });
    }

    private void setToDefaultSettings() {
        userName.setText(Settings.user_name);
        userIsX.setText(Settings.player_is_X?"x":"o");
        matrix.setProgress(Settings.N_cells*20);
        AI.setProgress(Settings.AI_level*40);
        time.setProgress(Settings.timed_level*40);
    }

    private void setApplyClick() {
                apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Settings.user_name = userName.getText().toString();
                            setXorO();
                            Settings.N_cells = matrix.getProgress() / 20;
                            Settings.AI_level = AI.getProgress() / 40;
                            Settings.timed_level = time.getProgress() / 40;
                            //next 2 lines must be last 2 lines
                            SaveSharedPrefenceSetting();
                            Utils.goToActivity(configuration.this, MainActivity2.class);
                        }catch (Exception ex){//to prevent to save wrong settings
                            Utils.showDialog(configuration.this,ex.toString());
                        }
                    }
                });
    }

    private void SaveSharedPrefenceSetting()throws Exception{
                try {
                    SharedPreferences preferences = getSharedPreferences("XOSettings", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("user_name", Settings.user_name);
                    editor.putBoolean("player_is_X", Settings.player_is_X);
                    editor.putInt("N_cells", Settings.N_cells);
                    editor.putInt("AI_level", Settings.AI_level);
                    editor.putInt("timed_level", Settings.timed_level);

                    editor.apply();
                }catch (Exception ex){
                    throw new Exception(ex.getMessage());
                }

    }


    private void setCellsListener() {
        matrix.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser){
                matrixtxt.setText("GAME MATRIX :"+progress/20);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar){}
        });
    }

    private void setXorO() throws Exception{
        String userIsX_Value=userIsX.getText().toString();
        if (userIsX_Value.toLowerCase().equals("x")||userIsX_Value.toLowerCase().equals("o")){
            char userIsX_Char=userIsX_Value.toLowerCase().charAt(0);
            if (userIsX_Char=='x'){
                Settings.player_is_X=true;
            }else{
                Settings.player_is_X=false;
            }
        }else if(userIsX_Value.toLowerCase().equals("")){
            throw new Exception("wrong input");
            //no thing to change
        }else{
            throw new Exception("wrong input");

        }
    }
}

