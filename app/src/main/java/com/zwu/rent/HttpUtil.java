package com.zwu.rent;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    public static void sendHttpRequest( final String address, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url=new URL(address);
                    connection= (HttpURLConnection) url.openConnection();
                    //设置请求方式
                    connection.setRequestMethod("GET");
                    //设置连接超时的时间（优化）
                    connection.setConnectTimeout(5000);
                    final InputStream inputStream=connection.getInputStream();
                    if (listener != null){
                        //  回调onFinish()方法
                        listener.onFinish(inputStream);
                    }
                }catch (Exception e){
                    if (listener != null){
                        //  回调onError()方法
                        listener.onError(e);
                    }
                }finally {
                    if (connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
