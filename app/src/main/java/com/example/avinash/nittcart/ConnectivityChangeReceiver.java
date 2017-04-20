package com.example.avinash.nittcart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by AVINASH on 3/29/2017.
 */
public class ConnectivityChangeReceiver
        extends BroadcastReceiver {

    public static Snackbar snackbar = null;
    Context context=null;
    static boolean flag = false;
    MainActivity m ;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        m = new MainActivity();
            debugIntent(intent, "ConnectionStatus");

    }

    private void debugIntent(Intent intent, String tag) {
        Log.v(tag, "action: " + intent.getAction());
        Log.v(tag, "component: " + intent.getComponent());
        Bundle extras = intent.getExtras();

        if (flag) {
            if (!checkNetwork()) {
                m.onDisconnected();
            } else
                m.onConnected();
        }
    }

    public boolean  checkNetwork(){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (MainActivity.isInternetConnected = activeNetwork != null);

    }
}
