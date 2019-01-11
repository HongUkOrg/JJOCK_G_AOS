package com.example.user.sealseeksee;

import android.util.Log;

import static com.example.user.sealseeksee.LetterConstants.TAG;


public class LetterUtils
{
    public static boolean isVaildSmsFormat(String input)
    {
        if(input == null || input.length()==0) return false;
        String lines[] = input.split("\n");
        if(lines.length!=2) {
            Log.d(TAG, "line length "+lines.length);
            return false;
        }
        Log.d(TAG, "line 1 : "+lines[0]);
        Log.d(TAG, "line 2 : "+lines[1]);
        return true;
    }
}
