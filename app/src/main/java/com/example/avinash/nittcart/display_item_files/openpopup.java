package com.example.avinash.nittcart.display_item_files;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

/**
 * Created by AVINASH on 3/12/2017.
 */
public class openpopup extends Service {

    public static View popupView;
    public static PopupWindow pwindo;
    public static ImageView chatHead;
    Context context;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flag,int start){

        return Service.START_STICKY;
    }

}
