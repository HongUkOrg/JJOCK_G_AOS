package com.david.user.sealseeksee;

import android.app.Application;
import android.content.res.Resources;

public class MyApplication extends Application
{

    public static final String TAG ="HONG";


    @Override
    public void onCreate() {

        super.onCreate();
        JGController.getInstance().setHeight(getScreenHeight());
        JGController.getInstance().setWidth(getScreenWidth());

    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }




}
