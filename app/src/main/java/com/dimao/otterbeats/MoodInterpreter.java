package com.dimao.otterbeats;

import android.content.Context;

/**
 * Created by harrisonoglesby on 11/21/15.
 */
public class MoodInterpreter {

    public static MoodInterpreter instance = null;

    private Context context = null;

    private int mode = 0;
    //1 = keep mood      2 = elevate mood
    private int initMood = 0;                   //initial mood
    private int currentMood;                //recent mood
    private int lastMood;                   //last mood

    private String[] excitedStations = {"1R2iL5hYKBrKy32T58CUUI", "6Qf2sXTjlH3HH30Ijo6AUp"
                        , "1h90L3LP8kAJ7KGjCV2Xfd","1B9o7mER9kfxbmsRH9ko4z" ,"0kVaFpvoi0O4IbyJyEZckU"
                        , "1vfys0yYhZEyJ9yvnULyM2"};
    private int exCtr = 0;

    private String[] engagedStations = {"1R2iL5hYKBrKy32T58CUUI", "6Qf2sXTjlH3HH30Ijo6AUp"
            , "1h90L3LP8kAJ7KGjCV2Xfd" ,"0kVaFpvoi0O4IbyJyEZckU", "1vfys0yYhZEyJ9yvnULyM2"};
    private int engCtr = 0;

    private String[] boredStations = {"3fCn2nqmX6ZnYUe9uoty98", "1oXl0OHlE1mPDChMa8Y0Ax"
            , "63dDpdoVHvx5RkK87g4LKk"};
    private int borCtr = 0;

    private String[] frustratedStations = {"1R2iL5hYKBrKy32T58CUUI", "6Qf2sXTjlH3HH30Ijo6AUp"
            , "1h90L3LP8kAJ7KGjCV2Xfd" ,"6cdV0hVW2suJaMOxzwE46S", "1vfys0yYhZEyJ9yvnULyM2"
            , "5Z9xJvDtHpB6m5zHgJC5zR"};
    private int fruCtr = 0;

    private String[] calmStations = {"3fCn2nqmX6ZnYUe9uoty98", "1oXl0OHlE1mPDChMa8Y0Ax"
            , "63dDpdoVHvx5RkK87g4LKk" ,"3hpgM1U3bD6kvo7wJubQ8z", "1iHelgbMaB7G1bjMbABPRe"};
    private int medCtr = 0;

    public static MoodInterpreter getInstance(){
        return instance;
    }

    public static MoodInterpreter getInstance(Context ctx){
        if(instance == null){
            instance = new MoodInterpreter(ctx);
        }
        return instance;
    }

    public MoodInterpreter(Context ctx){
        this.context = ctx;
    }

    public void setMode(int newMode){
        mode = newMode;
    }

    public int getMode(){
        return mode;
    }

    public void setInitMood(int newMood){
        if(newMood > 0 && newMood < 5) {
            if (initMood == 0) {initMood = newMood;}
            lastMood = newMood;
        }
    }

    public void setCurrentMood(int newMood){
        if(newMood > 0 && newMood < 5){
            lastMood = currentMood;
            currentMood = newMood;
        }
    }

    public int getInitMood(){
        return initMood;
    }

    public int getCurrentMood(){
        return currentMood;
    }

    public String getUpdate(){
        String result = "";
        if(mode == 1){
            result = maintainMood();
        }
        else{
            result = elevateMood();
        }
        return result;
    }

    public String initialStation(){
        String choice = "";

        return choice;
    }

    public String maintainMood(){
        String choice = "";
        if(currentMood == lastMood){
            //play same station
        }
        else{
            if(lastMood == 2 || lastMood == 1){
                //play more engaging station
            }
            else if(lastMood == 5){
                //play more calm station
            }
            else if(lastMood == 3){
                //play more boring station
            }
            else if(lastMood == 4){
                //play more frustrating song
            }

        }
        return choice;
    }



    public String elevateMood(){
        String choice = "";
        if(lastMood == 1 || lastMood == 2 || lastMood == 5){
            //keep playing same station
        }
        else if(lastMood == 3 && currentMood == 3){
            //play more engaging station
        }
        else if(lastMood == 4 && currentMood == 4){
            //play more calming station
        }
        return choice;
    }
}
