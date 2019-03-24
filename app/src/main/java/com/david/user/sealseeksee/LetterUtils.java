package com.david.user.sealseeksee;

import android.util.Log;

import java.math.BigDecimal;
import java.math.MathContext;

import static com.david.user.sealseeksee.LetterConstants.TAG;


public class LetterUtils
{
    public static boolean isVaildSmsFormat(String input)
    {
        if(input == null || input.length()==0) return false;
        String lines[] = input.split("\n");
        if(lines.length!=2) {
            Log.d(TAG, "line height "+lines.length);
            return false;
        }
        Log.d(TAG, "line 1 : "+lines[0]);
        Log.d(TAG, "line 2 : "+lines[1]);
        return true;
    }
    public static boolean isEmptyString(String str)
    {
        if(str == null || str.length()==0) return true;
        else return false;
    }
    public static boolean isValidPhoneNumber(String middle,String end)
    {
        if(middle.length()<3 || middle.length()>4) return false;
        if(end.length()<3 || end.length()>4) return false;
        return true;

    }

    public static String location_processing(double latitude, double longitude)
    {
        String result ="nothing";

        BigDecimal bd = new BigDecimal(latitude);
        bd = bd.round(new MathContext(6));
        double rounded_lati = bd.doubleValue();

        BigDecimal bd2 = new BigDecimal(longitude);
        bd = bd.round(new MathContext(6));
        double rounded_long = bd2.doubleValue();

//        my_lati=rounded_lati;
//        my_long=rounded_long;

        result = "";

        result += Double.toString(rounded_lati) + "," + Double.toString(rounded_long);

        Log.d("HONG2", "get_my_long_lat: "+result);

        return result;

    }

    public interface OnBackPressedListener {
        public void doBack();
    }

}
