package com.david.user.sealseeksee;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class httpConnectionToPhwysl extends Thread
{
    private static HttpURLConnection http;


    private static httpConnectionToPhwysl hp;

    public static httpConnectionToPhwysl getHttpConnectionInstance()
    {
        if(hp == null)
        {
            return new httpConnectionToPhwysl();
        }
        else return hp;
    }

    synchronized public void HttpPostData(final JSONObject myObj, final String ApiAddress)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String endPoint = "http://phwysl.dothome.co.kr/";
                    String myUrl = endPoint += ApiAddress;

                    URL url = new URL(endPoint);
                    // URL 설정
                    http = (HttpURLConnection) url.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setDoOutput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("Accept-Charset", "UTF-8");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");

                    ContentValues contentValues = new ContentValues();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

                    String current = dateFormat.format(new Date());
                    myObj.put("created_date", current);
                    String myJson = myObj.toString();

                    StringBuffer buffer = new StringBuffer();
                    buffer.append("json=");
                    buffer.append(myJson);

                    OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                    PrintWriter writer = new PrintWriter(outStream);
                    writer.write(buffer.toString());
                    writer.flush();

                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String str;

                    if(http.getResponseCode()==200)
                    {
                        Log.d("HONG", "HttpPostData: Success! ");
                    }
                    while ((str = reader.readLine()) != null)
                    {
                        // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                        builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
                    }
                    Log.d(LetterConstants.TAG, "response : "+builder.toString());
                    if(ApiAddress.equals(LetterConstants.FIND_LETTER_API))
                    {
                        JGController.getInstance().myLetterListener.onReceiveLetter(builder.toString());
                    }

                } catch (MalformedURLException e) {
                } catch (IOException e) {
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                http.disconnect();

            }
        });
        thread.start();
    }

}
