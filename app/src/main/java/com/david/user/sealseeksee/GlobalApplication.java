package com.david.user.sealseeksee;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.david.user.sealseeksee.kakaotalk.KakaoSDKAdapter;
import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

public class GlobalApplication extends Application {


    /*

    private static volatile GlobalApplication instance = null;

    public static GlobalApplication getGlobalApplicationContext() {
        if(instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }
     */


    @Override
    public void onCreate() {
        super.onCreate();
//        instance = this;
//        KakaoSDK.init(new KakaoSDKAdapter());

        HongController.getInstance().setHeight(getScreenHeight());
        HongController.getInstance().setWidth(getScreenWidth());



    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}