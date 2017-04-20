package com.example.avinash.nittcart.retrofit_api_calls;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.avinash.nittcart.MainActivity;
import com.example.avinash.nittcart.R;
import com.example.avinash.nittcart.response_classes.Item_list_response;
import com.example.avinash.nittcart.waveview.WaveView;
import com.google.gson.JsonObject;
import com.ramotion.foldingcell.FoldingCell;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tyrantgit.explosionfield.ExplosionField;

/**
 * Created by AVINASH on 4/5/2017.
 */
public class MainActivityCalls extends AppCompatActivity implements ApiCalls {

    private Subscription searchResultsSubscription;
    private ApiObservable apiObservable;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar loadmore;
    private WaveView waveView;
    Handler handler=null;
    Runnable runnable=null;
    private int i=40;
    private static int LIST_SIZE = 0;
    RelativeLayout hide_layout;
    ProgressBar relod;
    MainActivity m;
    private ApiCalls myResponse;

    Context context;
    public MainActivityCalls(Context context,SwipeRefreshLayout swipeRefreshLayout, ProgressBar loadmore,
                             WaveView waveView, RelativeLayout hide_layout, ProgressBar reload){
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.loadmore = loadmore;
        this.waveView = waveView;
        this.context = context;
        apiObservable = new ApiObservable();
        //this.hide_layout = hide_layout;
        this.relod = reload;
        m = new MainActivity();
        i=40;
    }

    @Override
    public void loaditems(String user_id, String auth_token, String name,
                          String subject, String price, String quality) {
        Log.d("loaditems","inside");
        try {
            String encodedQuery = URLEncoder.encode("SRK", "UTF-8");
            if (searchResultsSubscription != null && !searchResultsSubscription.isUnsubscribed()) {
                searchResultsSubscription.unsubscribe();
            }
            HashMap<String,String> item_map = new HashMap<>();
            item_map.put("auth_token",auth_token);
            item_map.put("user_id",user_id);
            item_map.put("name",name);

            item_map.put("price",price);
            item_map.put("quality",quality);

            if(subject!="OTHER"){
                item_map.put("item_type","BOOK");
                item_map.put("subject",subject);
            }
            else{
                item_map.put("item_type",subject);
            }

            rx.Observable<Item_list_response> observable =
                    apiObservable.loadItems(item_map);
            searchResultsSubscription = observable
                    .debounce(250, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(load_items_Subscriber());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void first_loaditems(String user_id, String auth_token, String name, String subject, String price, String quality) {
        Log.d("loaditems","inside");
        try {
            String encodedQuery = URLEncoder.encode("SRK", "UTF-8");
            if (searchResultsSubscription != null && !searchResultsSubscription.isUnsubscribed()) {
                searchResultsSubscription.unsubscribe();
            }
            HashMap<String,String> item_map = new HashMap<>();
            item_map.put("auth_token",auth_token);
            item_map.put("user_id",user_id);
            item_map.put("name",name);

            item_map.put("price",price);
            item_map.put("quality",quality);

            if(subject!="OTHER"){
                item_map.put("item_type","BOOK");
                item_map.put("subject",subject);
            }
            else{
                item_map.put("item_type",subject);
            }

            rx.Observable<Item_list_response> observable =
                    apiObservable.loadItems(item_map);
            searchResultsSubscription = observable
                    .debounce(250, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(first_load_items_Subscriber());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void RefreshItems() {

    }

    @Override
    public void Searchitems(String query) {
        Log.d("loaditems","inside");

    }

    @Override
    public void onErrorLoading() {

    }

    @Override
    public void onCompleteLoading(Item_list_response list_response,Context context) {

    }

    private Subscriber<JsonObject> searchResultsSubscriber() {
        return new Subscriber<JsonObject>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {

                Log.d("error_call", e.toString());
            }
            @Override
            public void onNext(JsonObject jsonObject) {
                //TODO :initialize the list here for adapter view
                Log.d("respo",jsonObject.toString());
            }
        };
    }

    private Subscriber<Item_list_response> load_items_Subscriber() {
        return new Subscriber<Item_list_response>() {
            @Override
            public void onCompleted() {
                //set List size here

            }
            @Override
            public void onError(Throwable e) {

                swipeRefreshLayout.setRefreshing(false);
                Log.d("error_call", e.toString());
            }

            @Override
            public void onNext(Item_list_response item_list_response) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d("respo", item_list_response.getStatusCode() + "");
                if(item_list_response.getStatusCode()==200){
                    m.onCompleteLoading(item_list_response,context);
                }
            }
        };
    }

    private Subscriber<Item_list_response> first_load_items_Subscriber() {
        return new Subscriber<Item_list_response>() {
            @Override
            public void onCompleted() {
                //set List size here

            }
            @Override
            public void onError(Throwable e) {

                //m.onErrorLoading();
                swipeRefreshLayout.setRefreshing(false);
                //loadmore.setVisibility(View.GONE);
                Log.d("error_call", e.toString());
            }

            @Override
            public void onNext(Item_list_response item_list_response) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d("respo", ""+item_list_response.getStatusCode());
                if(item_list_response.getStatusCode()==200){
                    m.onCompleteLoading(item_list_response,context);
                }
            }
        };
    }

    public void hidewaveview(){
        handler = new Handler();
        final ExplosionField explosionField =  ExplosionField.attach2Window((Activity) context);;
        explosionField.expandExplosionBound(150,150);
        i= (int) waveView.getProgress();

        runnable = new Runnable() {
            @Override
            public void run() {
                i++;
                if (i <= 100) {
                    waveView.setProgress(i);
                }
                if (i == 100) {
                    m.loadingtext.setVisibility(View.GONE);
                    explosionField.explode(waveView);
                }
                if (i == 130) {
                    Log.d("inside", "" + i);
                    //explosionField.clear();
                    if (LIST_SIZE == 0) {
                            //hide_layout.setVisibility(View.VISIBLE);
                            Animation zoomEnterAnimation = AnimationUtils.loadAnimation(context, R.anim.zoom_enter);
                            //hide_layout.startAnimation(zoomEnterAnimation);
                    }
                    else if(LIST_SIZE!=0){
                        //hide_layout.setVisibility(View.GONE);
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        Animation zoomEnterAnimation = AnimationUtils.loadAnimation(context, R.anim.zoom_enter);
                        swipeRefreshLayout.startAnimation(zoomEnterAnimation);
                    }
                }

                if(i<=130)handler.postDelayed(runnable, 10);
            }
        };
        runnable.run();
    }
}
