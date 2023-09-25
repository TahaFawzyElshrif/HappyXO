package com.example.happyxo;

public class Settings {
    public static String user_name="USER";
    public static int AI_level=1;
    public static boolean player_is_X=true;
    public static int timed_level=0;//default no time
    public static int N_cells=3;//min 2 ,max 8
    public static int depth(){
        switch (AI_level){
            case 2:
                return 5;
            case 3:
                return 7;
            case 4:
                return 9;
        }
        return 3;//if not 2 or 3 or 4 so is 1 ..default so depth is 3

    }
    public static Long time(){
        switch (timed_level){
            case 2://1000(s) * 150 (3M)
                return 150000L;
            case 3://1000(s) * 60 (M)
                return 60000L;
            case 4://1000(s) * 20 (half M)
                return 20000L;
        }
        return 0L; //no time
    }


}
