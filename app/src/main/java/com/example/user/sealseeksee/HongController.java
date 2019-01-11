package com.example.user.sealseeksee;

import android.content.Context;

public class HongController
{

    private static HongController instance;
    public static String resultFromFindLetterServer;
    public static Context myContext;


    public static Context getMyContext() {
        return myContext;
    }

    public static void setMyContext(Context myContext) {
        HongController.myContext = myContext;
    }

    public static double getMy_lati() {
        return my_lati;
    }

    public static void setMy_lati(double my_lati) {
        HongController.my_lati = my_lati;
    }

    public static double my_lati;

    public static double getMy_long() {
        return my_long;
    }

    public static void setMy_long(double my_long) {
        HongController.my_long = my_long;
    }

    public static double my_long;



    public static LetterListener myLetterListener;

    public static synchronized HongController getInstance() {
        if (instance == null) {
            instance = new HongController();
        }
        return instance;
    }
    public static void setLetterListener(LetterListener letterListener)
    {
        myLetterListener =  letterListener;
    }


    interface LetterListener
    {
        void onReceiveLetter(String resultJson);
    }

}
