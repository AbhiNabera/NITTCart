package com.example.avinash.nittcart.tab_and_bottombar;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.avinash.nittcart.MainActivity;
import com.example.avinash.nittcart.R;
import com.example.avinash.nittcart.display_item_files.ClickListener;
import com.example.avinash.nittcart.display_item_files.Dash_DetailActivity;
import com.example.avinash.nittcart.display_item_files.DetailActivity;
import com.example.avinash.nittcart.display_item_files.Item;
import com.example.avinash.nittcart.display_item_files.RecyclerTouchListener;
import com.example.avinash.nittcart.login.TokenDatabase;
import com.example.avinash.nittcart.response_classes.Item_structure;
import com.example.avinash.nittcart.response_classes.dashboard_Item_list_response;
import com.example.avinash.nittcart.response_classes.dashboard_Item_structure;
import com.example.avinash.nittcart.retrofit_api_calls.ApiObservable;
import com.google.gson.JsonObject;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by AVINASH on 4/1/2017.
 */
public class dashboard extends AppCompatActivity {

    @Bind(R.id.noitems)
    RelativeLayout noitems;
    @Bind(R.id.dash_recycler)
    RecyclerView dashboard_recycler;
    @Bind(R.id.retry)
    Button retry;
    @Bind(R.id.closecart)
    ImageButton closecart;
    CatLoadingView mView;

    Intent transitionIntent;
    private List<dashboard_Item_structure> DASHBOARD_ITEM_LIST;
    private String USER_ID, AUTH_TOKEN;

    private ApiObservable apiObservable;
    Call<JsonObject> result;
    ProgressDialog progressDialog;
    private static dashboard_adpter adapter;

    TokenDatabase tokenDatabase ;
    MainActivity m ;


    public MyAdapterListener onClickListener;
    public interface MyAdapterListener {
        void editButtonpressListner(List<dashboard_Item_structure> dash_item_list,int pos);
    }

    int i=0;

    public dashboard(){}

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWindowAnimations();
        setContentView(R.layout.user_dashboard);
        ButterKnife.bind(this);
        final Intent intent = getIntent();
        m = new MainActivity();
        USER_ID = intent.getStringExtra("user_id");
        AUTH_TOKEN = intent.getStringExtra("auth_token");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mView.show(getSupportFragmentManager(), "LOADING..");
                setDashboard_items();
            }
        }, 1000);

        tokenDatabase = new TokenDatabase(this);
        apiObservable = new ApiObservable();
        progressDialog = new ProgressDialog(this);
        DASHBOARD_ITEM_LIST = new ArrayList<>();


        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView.show(getSupportFragmentManager(), "LOADING..");
                loadDashboard(USER_ID, AUTH_TOKEN);
            }
        });

        closecart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAfterTransition();
                //overridePendingTransition(R.anim.activity_slide_down, R.anim.activity_no_slide_tabitem);
            }
        });
        mView = new CatLoadingView();
    }

    private void setupWindowAnimations() {
        Slide fade = new Slide();
        fade.setDuration(500);
        fade.setSlideEdge(Gravity.BOTTOM);
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.BOTTOM);

        slide.setDuration(700);
        getWindow().setEnterTransition(slide);
        getWindow().setReturnTransition(fade);
    }


    public void setDashboard_items(){

        adapter = new dashboard_adpter(DASHBOARD_ITEM_LIST,dashboard.this, new dashboard_adpter.MyAdapterListener() {
            @Override
            public void editButtonpressListner(View v, int position) {
                Log.d("positionedit",""+position);
                editItem(position,DASHBOARD_ITEM_LIST);
            }

            @Override
            public void deletebuttonpresslistener(View v, int position) {
                createAlertdialog(DASHBOARD_ITEM_LIST.get(position).getItemId(),position);
            }

            @Override
            public void onImageSelected(View v, int pos) {
                detailtransition(v, DASHBOARD_ITEM_LIST.get(pos));
            }

            @Override
            public Animator setupanimationutils(RelativeLayout rl, int cx, int cy, int start_rad, int end_rad) {
                Animator anim =
                        ViewAnimationUtils.createCircularReveal(rl, cx, cy, start_rad, end_rad);
                return anim;
            }
        });

        GridLayoutManager grid = new GridLayoutManager(this,1);
        dashboard_recycler.setLayoutManager(grid);
        dashboard_recycler.setItemAnimator(new DefaultItemAnimator());
        dashboard_recycler.setAdapter(adapter);
        //dashboard_recycler.setNestedScrollingEnabled(false);
        SlideInLeftAnimator animator = new SlideInLeftAnimator();
        animator.setInterpolator(new OvershootInterpolator());
        // or recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f));
        dashboard_recycler.setItemAnimator(animator);
        loadDashboard(USER_ID, AUTH_TOKEN);

        dashboard_recycler.addOnItemTouchListener(new RecyclerTouchListener(dashboard.this, dashboard_recycler, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                    Log.d("pos", position + "");
                    //detailtransition(view, DASHBOARD_ITEM_LIST.get(position));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    public void detailtransition(View v,dashboard_Item_structure item){

        transitionIntent = new Intent(dashboard.this, Dash_DetailActivity.class);
        transitionIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        transitionIntent.putExtra("item_id", item.getItemId());
        transitionIntent.putExtra("item_name", item.getName());
        transitionIntent.putExtra("item_price", item.getPrice());
        Log.e("price", item.getPrice().toString());
        transitionIntent.putExtra("item_description", item.getDescription());
        transitionIntent.putExtra("item_quality", item.getQuality());
        transitionIntent.putExtra("item_status",item.getStatus());
        transitionIntent.putExtra("item_image", item.getImage());
        transitionIntent.putExtra("user_id", USER_ID);
        transitionIntent.putExtra("auth_token", AUTH_TOKEN);
        transitionIntent.putExtra("seller_id",item.getSellerId().toString());
        Log.e("SELLERS_Id",item.getSellerId().toString());

        ImageView placeImage = (ImageView) v.findViewById(R.id.dash_item_image);
        TextView placeNameHolder = (TextView) v.findViewById(R.id.item_name);
        Pair<View, String> imagePair = Pair.create((View) placeImage, "tImage");
        //Pair<View, String> holderPair = Pair.create((View) placeNameHolder, "tNameHolder");
        Pair<View, String> toolbarPair = Pair.create((View)m.toolbar, "tActionBar");
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) dashboard.this,
                imagePair);
        Log.d("aaya", "debug");
        ActivityCompat.startActivity(dashboard.this, transitionIntent, options.toBundle());

    }

    public void loadDashboard(String user_id,String auth_token){
        apiObservable = new ApiObservable();
        Log.d("dash_user_id", user_id);
        Log.d("dash_auth", auth_token);
        Call<dashboard_Item_list_response> call= apiObservable.getdashboard(user_id,auth_token);
        call.enqueue(new Callback<dashboard_Item_list_response>() {
            @Override
            public void onResponse(Call<dashboard_Item_list_response> call, Response<dashboard_Item_list_response> response) {

                mView.dismiss();
                remove_network_error();

                if (response.isSuccessful()) {
                    if (response.body().getStatusCode() == 200) {

                        dashboard_recycler.setVisibility(View.VISIBLE);
                        adapter.removeAll();
                        Log.d("successful", "dash_resp");

                        DASHBOARD_ITEM_LIST.clear();
                        //dashboard_recycler.removeAllViews();

                        DASHBOARD_ITEM_LIST = response.body().getData();
                        m.DASH_LIST_SIZE = DASHBOARD_ITEM_LIST.size();
                        Log.d("size", m.DASH_LIST_SIZE + "");
                        if (m.DASH_LIST_SIZE == 0) {
                            set_noitems();
                        }
                        adapter.insertlist(DASHBOARD_ITEM_LIST);
                        adapter.notifyDataSetChanged();


                        Cursor cr = tokenDatabase.getToken(0);
                        cr.moveToFirst();
                        tokenDatabase.update_dash_count(DASHBOARD_ITEM_LIST.size());
                        //if(DASHBOARD_ITEM_LIST.size()!=0)m.updateBadgeCount(m.tabHost,DASHBOARD_ITEM_LIST.size());
                    }
                } else {
                    Snackbar.make(retry, "Bad Request!!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    set_networkerror();
                    remove_no_items();
                }

            }

            @Override
            public void onFailure(Call<dashboard_Item_list_response> call, Throwable t) {
                Snackbar.make(retry, "Network error!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                set_networkerror();
                remove_no_items();
                mView.dismiss();
            }
        });
          }

    public void createAlertdialog(final String item_id, final int position){

        AlertDialog.Builder alert = new AlertDialog.Builder((Activity)dashboard.this);
        AlertDialog dialog = alert.create();
        alert.setTitle("DELETE!!");
        alert.setIcon(R.drawable.alert);
        alert.setMessage("Are you sure you want to remove add for this item?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO network call to delete item
                deleteItem(USER_ID, AUTH_TOKEN, item_id, position);
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alert.show();
    }

    public void deleteItem(String user_id, String auth_token, String item_id, final int pos){
        result = apiObservable.delete_dashboard_item(user_id,auth_token,item_id);
        progressDialog.setMessage("Deleting..");
        progressDialog.show();
        result.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(response.body().toString());
                        Log.d("ACT_DETAIL Response", object.toString());
                        int respone_code = (int) object.get("status_code");
                        if (respone_code == 200) {
                            DASHBOARD_ITEM_LIST.remove(pos);

                            Cursor cr = tokenDatabase.getToken(0);
                            cr.moveToFirst();

                            dashboard_recycler.setItemAnimator(new SlideInLeftAnimator());
                            dashboard_recycler.getItemAnimator().setAddDuration(500);
                            dashboard_recycler.getItemAnimator().setRemoveDuration(500);

                            adapter.remove(pos);
                            adapter.notifyDataSetChanged();

                            if (DASHBOARD_ITEM_LIST.size() == 0) {
                                noitems.setVisibility(View.VISIBLE);
                            }
                            //tokenDatabase.update_dash_count(cr.getInt(cr.getColumnIndex("dashboard_count") -1));
                            Snackbar.make(retry, "Item deleted Successfully!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } else {
                            progressDialog.dismiss();
                            Snackbar.make(retry, "Error Occured!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Snackbar.make(retry, "Network error!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void set_networkerror(){
        retry.setVisibility(View.VISIBLE);
        dashboard_recycler.setAlpha((float) 0.5);
    }

    public void remove_network_error(){
        retry.setVisibility(View.GONE);
        dashboard_recycler.setAlpha((float) 1);
    }

    public void set_noitems(){
        noitems.setVisibility(View.VISIBLE);
        retry.setVisibility(View.GONE);
    }

    public void remove_no_items(){
        noitems.setVisibility(View.GONE);
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    public void editItem(final int pos, final List<dashboard_Item_structure> itemList){
        //onClickListener.editButtonpressListner(DASHBOARD_ITEM_LIST,pos);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), postadd.class);
                intent.putExtra("user_id",USER_ID);
                intent.putExtra("auth_token",AUTH_TOKEN);
                intent.putExtra("item_name",itemList.get(pos).getName());
                intent.putExtra("item_id",itemList.get(pos).getItemId());
                intent.putExtra("item_price",itemList.get(pos).getPrice());
                intent.putExtra("item_description",itemList.get(pos).getDescription());
                intent.putExtra("item_status",itemList.get(pos).getStatus());
                intent.putExtra("item_image",itemList.get(pos).getImage());
                intent.putExtra("item_quality",itemList.get(pos).getQuality());
                //TODO put intent extra
                Fade slide = new Fade();
                slide.setDuration(700);
                getWindow().setExitTransition(slide);
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(dashboard.this).toBundle();
                dashboard.this.startActivity(intent, bundle);
                //startActivity(intent);
                //overridePendingTransition(R.anim.up_from_bottom, R.anim.activity_no_slide_main);
            }
        }, 120);
    }
}
