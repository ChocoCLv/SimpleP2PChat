package com.dpcraft.simplep2pchat.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dpcraft.simplep2pchat.app.Config;
import com.dpcraft.simplep2pchat.data.ResponseFromServer;
import com.dpcraft.simplep2pchat.data.UserInfo;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by dpcraft on 18/11/2017.
 */

public class ServerUtils {
    public static void register(final UserInfo userInfo, final Handler handler){
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("login error", "error" );
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String responseBody = response.body().string();

                Log.i("response.body",responseBody);
                int code = ResponseFromServer.getCode(responseBody);
                Log.i("login code",code + "");
                Message message = handler.obtainMessage();//创建message的方式，可以更好地被回收
                message.what = code;
                message.obj = responseBody;
                Log.i("firstmessge.obj",message.obj.toString());
                handler.sendMessage(message);

            }
        };
        String url = Config.URL_REGISTER;
        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON,userInfo.toJSON());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        okHttpClient.newCall(request).enqueue(callback);
    }
}
