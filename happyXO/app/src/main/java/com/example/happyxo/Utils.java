package com.example.happyxo;

import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class Utils {
    public static void goToActivity(AppCompatActivity from, Class dir) {
        if (from == null || dir == null) {
            Log.e("error","null page");
            return;
        }else {
            Intent go = new Intent(from, dir);
            from.startActivity(go);
        }
    }
    public static void showDialog(AppCompatActivity currentActivity,String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
        builder.setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
    public static PyObject startPython(AppCompatActivity currentActivity, String module){
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(currentActivity));
        }
        Python py=Python.getInstance();
        PyObject obj= py.getModule(module);
        return obj;
    }
    //this  method for send and recieve data to python to easily recognized as python 2d list
    public static String ArrayToBracket(char[][] allArray) {

        String newSt="[";
        for (int i=0;i<allArray.length;i++){
            //algorithm use fencepost
                newSt=newSt+"[";

            for (int j=0;j<allArray.length;j++){
                //value
                if (allArray[i][j]==' '){
                    newSt=newSt+"\'\'";

                }else{
                    newSt=newSt+"\'"+allArray[i][j]+"\'";

                }
                //comma between elements
                if (j!=allArray.length-1) {//first element use fencepost
                    newSt=newSt+",";
                }else{
                    newSt=newSt+"]";
                }
            }
            //comma between arraies

            if (i!=allArray.length-1) {//first element use fencepost
                newSt=newSt+",";
            }else {
                newSt = newSt + "]";
            }
        }
    return newSt;
    }

}
