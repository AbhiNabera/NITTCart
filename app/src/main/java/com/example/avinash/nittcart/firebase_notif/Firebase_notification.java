package com.example.avinash.nittcart.firebase_notif;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by AVINASH on 4/15/2017.
 */
public class Firebase_notification {

    private String SERVER_KEY = "AAAAE5dBYc8:APA91bHpBriCgfcfj2fFI8753Qli9h18fY9NCjou4wsWMXL_xCk57hTFXo7AV5CkQJ2Q6NYWTlgCzEkEkYFTr5AZ0qdNykk_" +
            "3vMzVUw7QFJuu5680-HZROvmhCridX0v9bGn1kuuF6xO";


    public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    OkHttpClient mClient = new OkHttpClient();
    public void sendMessage(final JSONArray recipients, final String title, final String body, final String icon, final String message) {

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    JSONObject root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    notification.put("body", body);
                    notification.put("title", title);
                    notification.put("sound","default");
                    //notification.put("icon", icon);

                    JSONObject data = new JSONObject();
                    data.put("message", message);
                    root.put("notification", notification);
                    root.put("data", data);
                    root.put("registration_ids", recipients);

                    String result = postToFCM(root.toString());
                    Log.d("notification", "Result: " + result);
                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if(result != null) {
                    try {
                        JSONObject resultJson = new JSONObject(result);
                        int success, failure;
                        success = resultJson.getInt("success");
                        failure = resultJson.getInt("failure");
                        Log.d("result", "" + success + ":" + failure);
                        //Toast.makeText(getCurrentActivity(), "Message Success: " + success + "Message Failed: " + failure, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Toast.makeText(getCurrentActivity(), "Message Failed, Unknown error occurred.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }.execute();
    }

    String postToFCM(String bodyString) throws IOException {
        RequestBody body = RequestBody.create(MediaType
                .parse("application/json"), bodyString);
        Request request = new Request.Builder()
                .url(FCM_MESSAGE_URL)
                .post(body)
                .addHeader("Authorization", "key=" + SERVER_KEY)
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }
}
