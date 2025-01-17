package com.david.user.sealseeksee;

import android.content.Context;

public class HongController
{

    private static HongController instance;


    public int width, height;
    public static String resultFromFindLetterServer;
    public static Context myContext;
    public static String savedPhoneNumber;
    public static boolean writingNow = false;
    public FinderChangeListener finderChangeListener;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    public int getHeight() {
        return height;
    }


    public static String getSavedPhoneNumber() {
        return savedPhoneNumber;
    }

    public static void setSavedPhoneNumber(String savedPhoneNumber) {
        HongController.savedPhoneNumber = savedPhoneNumber;
    }
    public void setFinderChangeListener(FinderChangeListener finderChangeListener){
        this.finderChangeListener = finderChangeListener;
    }

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
    public static double my_lati = -1;

    public static double getMy_long() {
        return my_long;
    }
    public static void setMy_long(double my_long) {
        HongController.my_long = my_long;
    }
    public static double my_long = -1;

    public static String getMy_w3w() {
        return my_w3w;
    }
    public static void setMy_w3w(String my_w3w) {
        HongController.my_w3w = my_w3w;
    }
    public static String my_w3w;




    public static LetterListener myLetterListener;

    public static synchronized HongController getInstance() {
        if (instance == null) {
            instance = new HongController();
        }
        return instance;
    }
    public void setLetterListener(LetterListener letterListener)
    {
        myLetterListener =  letterListener;
    }


    interface LetterListener
    {
        void onReceiveLetter(String resultJson);
    }
    public interface FinderChangeListener {
        void changeFinder(int i);
    }

}
