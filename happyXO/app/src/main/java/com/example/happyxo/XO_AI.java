package com.example.happyxo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.chaquo.python.PyObject;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Arrays;

public class XO_AI extends AppCompatActivity {
    LinearLayout main;
    TextView lbl;
    ScrollView outerScrol;
    EditText[][] editTexts = new EditText[Settings.N_cells][Settings.N_cells];
    char[][] cells_values = new char[Settings.N_cells][Settings.N_cells];
    private Handler handler = new Handler();
    Long Time=Settings.time();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xo_ai);
        main = (LinearLayout) findViewById(R.id.main);
        lbl=(TextView)findViewById(R.id.lbl2);
        outerScrol=(ScrollView)findViewById(R.id.mainscrol);

        cells_values=XO_user.intialize_Cells(cells_values);

        outerScrol=XO_user.setAnimationFirst(outerScrol,XO_AI.this);
        XO_user.addTimeConstraint(cells_values,XO_AI.this);
        setLabelText();
        editTexts=XO_user.createGrid(main,editTexts,XO_AI.this);
        setGridchangeListener();
    }

    private void setLabelText() {
        String lbl_appended_Text="";
        lbl_appended_Text=lbl_appended_Text+Settings.user_name+
                " ("+(Settings.player_is_X?"x":"o")+")";
        String string_user_info=lbl.getText()+"\n"+lbl_appended_Text;
        lbl.setText(string_user_info);
        if (Settings.time()!=0L){
            Runnable updateVariableRunnable = new Runnable() {
                @Override
                public void run() {
                    Time=Time-1000;//checked that not 0 above
                    lbl.setText(string_user_info+"\nTime: " + Time/1000);
                    handler.postDelayed(this, 1000); // 1000 milliseconds = 1 second
                }
            };
            handler.post(updateVariableRunnable);
        }
    }


/*    private void addTimeConstraint() {
        if (Settings.time()==0L){

        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    evelateBeforeFinishTime();
                    finish();
                }
            }, Settings.time());
        }
    }

    private void evelateBeforeFinishTime() {
        String ArrayToBracket = Utils.ArrayToBracket(cells_values);
        PyObject obj = Utils.startPython(XO_AI.this, "XO_AI");
        int static_eval=obj.callAttr("static_eval", ArrayToBracket, !Settings.player_is_X).toInt();
        switch (static_eval){
            case 1:{
                Utils.goToActivity(XO_AI.this, WON.class);
                break;
            } case -1:{
                Utils.goToActivity(XO_AI.this, LOSE.class);
                break;
            } case 0:{
                Utils.showDialog(XO_AI.this,"TIME OUT NO ONE WON!");
                Utils.goToActivity(XO_AI.this,MainActivity2.class);
            }
        }
    }

    public void setAnimationFirst(){
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_activaty2_layout);
        main.startAnimation(animation);

    }
    private void intialize_Cells() {
        for (int i = 0; i < Settings.N_cells; i++) {
            for (int j = 0; j < Settings.N_cells; j++) {
                cells_values[i][j] = ' ';
            }
        }
    }
*/
    private void setGridchangeListener() {
        for (int num_i = 0; num_i < Settings.N_cells; num_i++) {//not i ,j as they include TEXTwATCHER CLASS used i so
            //to prevent problem
            for (int num_j = 0; num_j < Settings.N_cells; num_j++) {
                TextWatcher_text t=new TextWatcher_text(num_i,num_j);
                editTexts[num_i][num_j].addTextChangedListener(t);
            }
        }
    }

    private class TextWatcher_text implements TextWatcher {
        Integer num_i,num_j;
        CharSequence charSequence;
        TextWatcher_text(){}
        TextWatcher_text(Integer i,Integer j){
            this.num_i=i;this.num_j=j;
        }
        public CharSequence get_Char(){
            return this.charSequence;
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            char userChar=Settings.player_is_X?'x':'o';
            if (charSequence.toString().length()==1) {//this if to prevent problem if backspace clicked for ex
                if(charSequence.toString().toLowerCase().charAt(0)==userChar) {
                    //to lower case: to work if user input captal or small
                    //if AI role : this code not work as condition is false so all consistent

                    //make it not changable value
                    editTexts[num_i][num_j].setEnabled(false);

                    cells_values[num_i][num_j] = userChar;//always one char so no need to use charAt
                    //change it to way accessible by python
                    String ArrayToBracket = Utils.ArrayToBracket(cells_values);
                    PyObject obj = Utils.startPython(XO_AI.this, "XO_AI");
                    int static_eval = obj.callAttr("static_eval", ArrayToBracket, !Settings.player_is_X).toInt();//if player is x so computer is not x
                    switch (static_eval) {
                        case (0): {//not solved so call AI

                            String AI_result = obj.callAttr("get_best_step", ArrayToBracket, !Settings.player_is_X, Settings.depth()).toString();
                            //last string is in form (0,0) so use substring to get indices
                            int index_first = Integer.parseInt(AI_result.substring(1, AI_result.indexOf(",")));
                            int index_second = Integer.parseInt(AI_result.substring(AI_result.indexOf(",") + 2, AI_result.length() - 1));
                            //+2 in first argument as getting string from python add some spaces

                            cells_values[index_first][index_second] = (!Settings.player_is_X) ? 'x' : 'o';
                            editTexts[index_first][index_second].setText(!Settings.player_is_X ? "x" : "o");
                            editTexts[index_first][index_second].setEnabled(false);
                            break;


                        }
                        case (-1): {//computer lose so you won
                            Utils.goToActivity(XO_AI.this, WON.class);
                            break;
                        }
                        case (1): {
                            Utils.goToActivity(XO_AI.this, LOSE.class);
                            break;
                        }
                    }
                }else{
                //    Utils.showDialog(XO_AI.this,"Please type only "+userChar);

                }
            }
        }
        @Override
        public void afterTextChanged(Editable editable) {}
    }

    /*
    public void createGrid() {
        for (int i = 0; i < Settings.N_cells; i++) {
            LinearLayout row = new LinearLayout(XO_AI.this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < Settings.N_cells; j++) {
                CardView card_text = new CardView(XO_AI.this);
                prepare_card(card_text);

                MaterialRippleLayout mat_text = new MaterialRippleLayout(XO_AI.this);
                prepare_materialRipple(mat_text);
                card_text.addView(mat_text);

                EditText text_i = new EditText(XO_AI.this);
                prepare_EditText(text_i);
                mat_text.addView(text_i);
                editTexts[i][j] = text_i;
                row.addView(card_text);
            }
            main.addView(row);
        }
    }

    private void prepare_materialRipple(MaterialRippleLayout mat_text) {
        mat_text.setRippleColor(Color.parseColor("#FF5722"));
        mat_text.setBackgroundColor(Color.WHITE);
    }

    private void prepare_card(CardView card_text) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int marginPx = 100 / Settings.N_cells;
        layoutParams.setMargins(marginPx, marginPx, marginPx, marginPx);
        layoutParams.height = 800 / Settings.N_cells;//to get small when many cells
        layoutParams.width = 800 / Settings.N_cells;
        card_text.setLayoutParams(layoutParams);
        card_text.setRadius(35);
        card_text.setElevation(90);

    }

    private void prepare_EditText(EditText text_i) {
        text_i.setText("");
        text_i.setBackgroundResource(R.drawable.texts);
        text_i.setWidth(800 / Settings.N_cells);//to get small when many cells
        text_i.setHeight(800 / Settings.N_cells);
        text_i.setGravity(Gravity.CENTER);
        addZoomAnimation(text_i);
    }
    private void addZoomAnimation(EditText text_i){
        text_i.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_view);
                text_i.startAnimation(animation2);
                return false;
            }
        });

    }*/
}
