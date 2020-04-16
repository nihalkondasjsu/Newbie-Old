package com.nihalkonda.newbie.utils.internet;

import android.app.Activity;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Server {

    private static Server instance;

    private OkHttpClient client ;

    private Server(){
        this.client = new OkHttpClient();
    }

    public static Server getInstance(){
        if(instance == null)
            instance = new Server();
        return instance;
    }

    public void loadProcedures(final Activity activity, final String path, final ResponseHandler successHandler, final ResponseHandler failureHandler){

        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpUrl.Builder httpBuilder = HttpUrl.parse("http://newbie.nihalkonda.com/loadProcedures.php").newBuilder();

                httpBuilder.addQueryParameter("path",path);

                Request request = new Request.Builder().url(httpBuilder.build()).build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    final String responseString = response.body().string();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            successHandler.onCallback(responseString);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            failureHandler.onCallback(e.toString());
                        }
                    });
                }
            }
        }).start();

    }

}
