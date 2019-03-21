package com.david.user.sealseeksee;

import android.content.Context;
import android.util.Log;

import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.response.KakaoTalkProfile;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.network.ErrorResult;

public class HongController
{

    private static HongController instance;



    public int width, height;
    public static String resultFromFindLetterServer;
    public static Context myContext;
    public static String savedPhoneNumber;
    public static boolean writingNow = false;
    public
    WorkerThread workerThread = new WorkerThread("seal_letter");

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

    interface LetterListener
    {
        void onReceiveLetter(String resultJson);
    }


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
    public void requestKakatoTalkProfile(){
        requestProfile();
    }
    private abstract class KakaoTalkResponseCallback<T> extends TalkResponseCallback<T> {
        @Override
        public void onNotKakaoTalkUser() {
            Log.d("HONG", "not a KakaoTalk user");
        }

        @Override
        public void onFailure(ErrorResult errorResult) {
            Log.d("HONG", "onFailure: "+errorResult.toString());
        }

        @Override
        public void onSessionClosed(ErrorResult errorResult) {
//            redirectLoginActivity();
        }

        @Override
        public void onNotSignedUp() {
//            redirectSignupActivity();
        }
    }
    private void requestProfile(){
        KakaoTalkService.getInstance().requestProfile(new KakaoTalkResponseCallback<KakaoTalkProfile>() {
            @Override
            public void onSuccess(KakaoTalkProfile talkProfile) {
                final String nickName = talkProfile.getNickName();
                final String profileImageURL = talkProfile.getProfileImageUrl();
                final String thumbnailURL = talkProfile.getThumbnailUrl();
                final String countryISO = talkProfile.getCountryISO();
            }
        });
    }


}
