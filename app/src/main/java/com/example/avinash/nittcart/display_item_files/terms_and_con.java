package com.example.avinash.nittcart.display_item_files;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.avinash.nittcart.MainActivity;
import com.example.avinash.nittcart.R;

/**
 * Created by AVINASH on 4/14/2017.
 */
public class terms_and_con extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWindowAnimations();
        setContentView(R.layout.termsandcond);

    }


    private void setupWindowAnimations() {
        Slide fade = new Slide();
        fade.setDuration(700);
        fade.setSlideEdge(Gravity.END);
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.END);

        slide.setDuration(1000);
        getWindow().setEnterTransition(slide);
        getWindow().setReturnTransition(fade);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAfterTransition();
        //overridePendingTransition(R.anim.activity_slide_down, R.anim.activity_no_slide_tabitem);
    }
}
