package com.example.happyxo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.chaquo.python.PyObject;

import java.util.Arrays;

public class XO_user extends AppCompatActivity {
//this version very basic  , not use network between users
    LinearLayout main;
    ScrollView outerScrol;
    TextView lbl,userRolelbl;
    EditText[][] editTexts = new EditText[Settings.N_cells][Settings.N_cells];
    char[][] cells_values = new char[Settings.N_cells][Settings.N_cells];
    Long Time=Settings.time();

    boolean main_user_role=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xo_user);
        main = (LinearLayout) findViewById(R.id.mainLay);
        lbl=(TextView)findViewById(R.id.lbl);
        userRolelbl=(TextView)findViewById(R.id.lbl_userRole);
        outerScrol=(ScrollView)findViewById(R.id.mainscrol);
        userRolelbl.setText("Role :"+Settings.user_name);
        cells_values=intialize_Cells(cells_values);
        outerScrol=setAnimationFirst(outerScrol,XO_user.this);
        addTimeConstraint(cells_values,XO_user.this);
        setLabelText();
        editTexts=createGrid(main,editTexts,XO_user.this);
        setGridchangeListener();

    }

    private  void setLabelText() {
         Handler handler = new Handler();

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


    public static void addTimeConstraint(char[][] cells,AppCompatActivity actv) {
        if (Settings.time()==0L){

        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    XO_user.evelateBeforeFinishTime(cells,actv);
                    actv.finish();
                }
            }, Settings.time());
        }
    }

    public static void evelateBeforeFinishTime(char[][] cells,AppCompatActivity actv) {
        String ArrayToBracket = Utils.ArrayToBracket(cells);
        PyObject obj = Utils.startPython(actv, "XO_AI");
        int static_eval=obj.callAttr("static_eval", ArrayToBracket, !Settings.player_is_X).toInt();
        switch (static_eval){
            case 1:{
                Utils.goToActivity(actv, WON.class);
                break;
            } case -1:{
                Utils.goToActivity(actv, LOSE.class);
                break;
            } case 0:{
                Utils.showDialog(actv,"TIME OUT NO ONE WON!");
                Utils.goToActivity(actv,MainActivity2.class);
            }
        }
    }

    public static ScrollView setAnimationFirst(ScrollView layout,AppCompatActivity actv){
        Animation animation = AnimationUtils.loadAnimation(actv, R.anim.anim_activaty2_layout);
        //same as anim_activaty2_layout
        layout.startAnimation(animation);
        return layout;

    }
    private void setGridchangeListener() {//copied in the XO_AI class as difficult to be static
        for (int num_i = 0; num_i < Settings.N_cells; num_i++) {//not i ,j as they include TEXTwATCHER CLASS used i so
            //to prevent problem
            for (int num_j = 0; num_j < Settings.N_cells; num_j++) {
                TextWatcher_text_User t=new TextWatcher_text_User(num_i,num_j);
                editTexts[num_i][num_j].addTextChangedListener(t);
            }
        }
    }
    private class TextWatcher_text_User implements TextWatcher {
        Integer num_i, num_j;
        CharSequence charSequence;

        TextWatcher_text_User() {
        }

        TextWatcher_text_User(Integer i, Integer j) {
            this.num_i = i;
            this.num_j = j;
        }

        public CharSequence get_Char() {
            return this.charSequence;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            char roleChar=' ';//' ' to prevent not-assign problems
            if (main_user_role){
                roleChar=Settings.player_is_X ? 'x' : 'o';//if it's main_user role then roleChar is
                //same as user so same as settings
            }else{
                //the opposite of settings
                roleChar=(!Settings.player_is_X) ? 'x' : 'o';
            }
            if (charSequence.toString().length()==1) {//this if to prevent problem if backspace clicked for ex

                if (charSequence.toString().toLowerCase().charAt(0) == roleChar) {//to lower case: to work if user input captal or small
                    editTexts[num_i][num_j].setEnabled(false);
                    cells_values[num_i][num_j] = roleChar;

                    String ArrayToBracket = Utils.ArrayToBracket(cells_values);
                    //working methods between user/user use methods from xo_AI module
                    PyObject obj = Utils.startPython(XO_user.this, "XO_AI");

                    int static_eval = obj.callAttr("static_eval", ArrayToBracket, !Settings.player_is_X).toInt();
                    //static_eval take parameter computerIsX ,her no computer but opponent

                    switch (static_eval) {
                        case (0): {//do nothing
                            break;
                        }
                        case (-1): {//opponent lose so you won
                            Utils.goToActivity(XO_user.this, WON.class);
                            break;
                        }
                        case (1): {
                            Utils.goToActivity(XO_user.this, LOSE.class);
                            break;
                        }
                    }
                    String lbl_txt = lbl.getText().toString();
                    main_user_role = !main_user_role;

                    String user_name = main_user_role ? Settings.user_name : "Other User";
                    userRolelbl.setText("\nRole: " + user_name);
                }else{
                    Utils.showDialog(XO_user.this,"Please type only "+roleChar);

                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
    public static char[][] intialize_Cells(char[][] cells) {
        for (int i = 0; i < Settings.N_cells; i++) {
            for (int j = 0; j < Settings.N_cells; j++) {
                cells[i][j] = ' ';
            }
        }
        return cells;
    }
    public static EditText[][] createGrid(LinearLayout mainL,EditText[][] texts,AppCompatActivity actv) {//add editText to mainL given
        for (int i = 0; i < Settings.N_cells; i++) {
            LinearLayout row = new LinearLayout(actv);
            row.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < Settings.N_cells; j++) {
                CardView card_text = new CardView(actv);
                card_text=prepare_card(card_text);

                MaterialRippleLayout mat_text = new MaterialRippleLayout(actv);
                mat_text=prepare_materialRipple(mat_text);
                card_text.addView(mat_text);

                EditText text_i = new EditText(actv);
                text_i=prepare_EditText(text_i,actv);
                mat_text.addView(text_i);
                texts[i][j] = text_i;
                row.addView(card_text);
            }
            mainL.addView(row);
        }
        return texts;
    }
    private static MaterialRippleLayout prepare_materialRipple(MaterialRippleLayout mat) {
        mat.setRippleColor(Color.parseColor("#FF5722"));
        mat.setBackgroundColor(Color.WHITE);
        return mat;
    }

    private static CardView prepare_card(CardView card) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int marginPx = 100 / Settings.N_cells;
        layoutParams.setMargins(marginPx, marginPx, marginPx, marginPx);
        layoutParams.height = 800 / Settings.N_cells;//to get small when many cells
        layoutParams.width = 800 / Settings.N_cells;
        card.setLayoutParams(layoutParams);
        card.setRadius(35);
        card.setElevation(90);
        return card;
    }

    private static EditText prepare_EditText(EditText text,AppCompatActivity actv) {
        text.setText("");
        text.setBackgroundResource(R.drawable.texts);
        text.setWidth(800 / Settings.N_cells);//to get small when many cells
        text.setHeight(800 / Settings.N_cells);
        text.setGravity(Gravity.CENTER);
        text=addZoomAnimation(text,actv);
        return text;
    }
    private static EditText addZoomAnimation(EditText text,AppCompatActivity actv){
        text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Animation animation2 = AnimationUtils.loadAnimation(actv, R.anim.zoom_view);
                text.startAnimation(animation2);
                return false;
            }
        });
        return text;
    }
}