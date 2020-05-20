package com.david.user.sealseeksee;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.multidex.MultiDex;

import com.david.user.sealseeksee.kakaotalk.KakaoSDKAdapter;
import com.igaworks.v2.core.application.AbxActivityHelper;
import com.igaworks.v2.core.application.AbxActivityLifecycleCallbacks;
import com.kakao.auth.KakaoSDK;

public class GlobalApplication extends Application {




    private static volatile GlobalApplication instance = null;

    public static GlobalApplication getGlobalApplicationContext() {
        if(instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        KakaoSDK.init(new KakaoSDKAdapter());

        AbxActivityHelper.initializeSdk(getApplicationContext(),"q4OrsUzivkuuFfHd7O1wyw","LZlqiZ9wzkuaLIVpeJlWsg");
        registerActivityLifecycleCallbacks(new AbxActivityLifecycleCallbacks());
        JGController.getInstance().setHeight(getScreenHeight());
        JGController.getInstance().setWidth(getScreenWidth());



    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}