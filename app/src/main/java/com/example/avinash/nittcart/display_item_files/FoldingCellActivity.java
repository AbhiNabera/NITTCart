package com.example.avinash.nittcart.display_item_files;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.avinash.nittcart.MainActivity;
import com.example.avinash.nittcart.R;
import com.example.avinash.nittcart.response_classes.Item_list_response;
import com.example.avinash.nittcart.response_classes.Item_structure;
import com.example.avinash.nittcart.retrofit_api_calls.ApiObservable;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.mauker.materialsearchview.MaterialSearchView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tyrantgit.explosionfield.ExplosionField;

/**
 * Created by AVINASH on 3/14/2017.
 */
public class FoldingCellActivity extends MainActivity {

    public static GridFoldingCellAdapter mAdapter;
    Context mContext;
    private static String AUTH_TOKEN = "";
    private static String USER_ID = "";
    Call<JsonObject> result;
    ProgressDialog progressDialog;
    static MaterialSearchView searchView;

    private Subscription searchResultsSubscription;
    private ApiObservable apiObservable;
    ArrayList<String> suggestion;
    //private SwipeRefreshLayout swipeRefreshLayout;

    public FoldingCellActivity(){}

    public FoldingCellActivity(RecyclerView recyclerView, RecyclerView.LayoutManager mLayoutManager,
                               final Context mContext , GridLayoutManager lLayout,String user_id,String auth_token) {

        this.mContext = mContext;
        AUTH_TOKEN = auth_token;
        USER_ID = user_id;

        apiObservable = new ApiObservable();
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Adding..");


        Animation animation = AnimationUtils.loadAnimation(mContext,
                R.anim.up_from_bottom);
        Animation animation2 = AnimationUtils.loadAnimation(mContext,
                R.anim.left_recycler);

        GridFoldingCellAdapter gadapter = new GridFoldingCellAdapter(ItemList, mContext,
                animation, animation2, new GridFoldingCellAdapter.MyAdapterListener() {
            @Override
            public void addtocart(View v, int position) {
                Log.d("yo", "add to cart");
                Item_structure item = ItemList.get(position);
                add_to_cart((TextView) v, item, position);
                //TODO:send request to server to add items to cart
            }
        });

        mAdapter = gadapter;

        {
             {
                recyclerView.setLayoutManager(lLayout);
                //recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(gadapter);
                recyclerView.setNestedScrollingEnabled(false);


                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Item_structure item = ItemList.get(position);
                        if (layoutmode.matches("GRID_LAYOUT")) {
                            Log.d("pos", position + "");
                            detailtransition(view, item);
                        }
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

            }
        }
    }

    public void set_new_List_Adapter(Item_list_response list){
        mAdapter.removeAll();
        mAdapter.notifyDataSetChanged();
        ItemList.clear();
        mAdapter = null;
        ItemList =  list.getData();
        MainActivity.MAIN_ITEM_LIST = ItemList;
        Log.d("size", ItemList.size() + "");
        Animation animation = AnimationUtils.loadAnimation(mContext,
                R.anim.up_from_bottom);
        Animation animation2 = AnimationUtils.loadAnimation(mContext,
                R.anim.left_recycler);
        mAdapter = new GridFoldingCellAdapter(ItemList, mContext,
                animation, animation2, new GridFoldingCellAdapter.MyAdapterListener() {
            @Override
            public void addtocart(View v, int position) {
                Log.d("yo", "add to cart");
                Item_structure item = ItemList.get(position);
                add_to_cart((TextView) v, item, position);
                //TODO:send request to server to add items to cart
            }
        });
        MainActivity.recyclerView.removeAllViews();
        recyclerView.setAdapter(mAdapter);
        //mAdapter.setnewList(list.getData());
        //ItemList =  list.getData();
        //mAdapter.notifyDataSetChanged();
    }

    public void set_new_List_Adapter(List<Item_structure> list){
        mAdapter.removeAll();
        mAdapter.notifyDataSetChanged();
        ItemList.clear();
        mAdapter = null;
        ItemList =  MAIN_ITEM_LIST;
        Log.d("size2",ItemList.size()+"");
        Animation animation = AnimationUtils.loadAnimation(mContext,
                R.anim.up_from_bottom);
        Animation animation2 = AnimationUtils.loadAnimation(mContext,
                R.anim.left_recycler);
        mAdapter = new GridFoldingCellAdapter(ItemList, mContext,
                animation, animation2, new GridFoldingCellAdapter.MyAdapterListener() {
            @Override
            public void addtocart(View v, int position) {
                Log.d("yo", "add to cart");
                Item_structure item = ItemList.get(position);
                add_to_cart((TextView) v, item, position);
                //TODO:send request to server to add items to cart
            }
        });
        MainActivity.recyclerView.removeAllViews();
        recyclerView.setAdapter(mAdapter);
        //mAdapter.setnewList(list.getData());
        //ItemList =  list.getData();
        //mAdapter.notifyDataSetChanged();
    }

    public void set_search_view_items(Item_list_response list){
        mAdapter.removeAll();
        mAdapter.notifyDataSetChanged();
        ItemList.clear();
        mAdapter = null;
        ItemList =  list.getData();
        Log.d("_size_", ItemList.size() + "");
        Animation animation = AnimationUtils.loadAnimation(mContext,
                R.anim.up_from_bottom);
        Animation animation2 = AnimationUtils.loadAnimation(mContext,
                R.anim.left_recycler);
        mAdapter = new GridFoldingCellAdapter(ItemList, mContext,
                animation, animation2, new GridFoldingCellAdapter.MyAdapterListener() {
            @Override
            public void addtocart(View v, int position) {
                Log.d("yo", "add to cart");
                Item_structure item = ItemList.get(position);
                add_to_cart((TextView) v, item, position);
                //TODO:send request to server to add items to cart
            }
        });
        MainActivity.recyclerView.removeAllViews();
        recyclerView.setAdapter(mAdapter);
        //mAdapter.setnewList(list.getData());
        //ItemList =  list.getData();
        //mAdapter.notifyDataSetChanged();
    }


    public void add_to_cart(final TextView view,Item_structure Item,int pos){
        //TODO implement server call
        if(Item.getStatus().matches("UNSOLD")){
            result = apiObservable.add_to_Cart(USER_ID,AUTH_TOKEN,Item.getItemId());
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
                                ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(mContext, R.animator.flipping);
                                anim.setTarget(view);
                                anim.setDuration(500);
                                anim.start();
                                view.setText("Item Addaded to cart");
                                Snackbar.make(coordinatorLayout, "Item Successfuly Addaded to Cart!!", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            } else {
                                progressDialog.dismiss();
                                Snackbar.make(coordinatorLayout, "Error Occured!!", Snackbar.LENGTH_LONG)
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
                    Snackbar.make(coordinatorLayout, "Network error!!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
        else {
            Snackbar.make(coordinatorLayout, "ITEM OUT OF STOCK!!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    public void detailtransition(View v,Item_structure item){
        Intent transitionIntent = new Intent(mContext, DetailActivity.class);
        transitionIntent.putExtra("item_id",item.getItemId());
        transitionIntent.putExtra("item_name",item.getName());
        transitionIntent.putExtra("item_price",item.getPrice());
        transitionIntent.putExtra("item_description",item.getDescription());
        transitionIntent.putExtra("item_quality",item.getQuality());
        transitionIntent.putExtra("item_status",item.getStatus());
        transitionIntent.putExtra("item_image",item.getImage());
        transitionIntent.putExtra("user_id",USER_ID);
        transitionIntent.putExtra("auth_token",AUTH_TOKEN);

        ImageView placeImage = (ImageView) v.findViewById(R.id.item_image);
        LinearLayout placeNameHolder = (LinearLayout) v.findViewById(R.id.grid_detail);
        Pair<View, String> imagePair = Pair.create((View) placeImage, "tImage");
        Pair<View, String> holderPair = Pair.create((View) placeNameHolder, "tNameHolder");
        Pair<View, String> toolbarPair = Pair.create((View)toolbar, "tActionBar");
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)mContext,
                imagePair, holderPair, toolbarPair);
        ActivityCompat.startActivity(mContext, transitionIntent, options.toBundle());

    }


    public void loaditems(HashMap<String,String> item_map) {
        Log.d("loaditems","inside");
        Log.d("loaditems",item_map.get("name"));
        try {
            String encodedQuery = URLEncoder.encode("SRK", "UTF-8");
            if (searchResultsSubscription != null && !searchResultsSubscription.isUnsubscribed()) {
                searchResultsSubscription.unsubscribe();
            }

            if(item_map.get("item_type")!="OTHER"){
                item_map.put("item_type","BOOK");
                item_map.put("subject",item_map.get("item_type"));
            }
            else{
                item_map.put("item_type", item_map.get("item_type"));
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


    public void first_loaditems(String user_id, String auth_token, String name, String subject, String price, String quality) {
        Log.d("first_loaditems","inside");
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
                item_map.put("item_type", subject);
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



    public void Searchitems(HashMap<String,String> item_map ,MaterialSearchView searchView) {
        Log.d("searchview", "inside");
        this.searchView = searchView;
        if (!item_map.get("name").matches("")) {
            Log.d("search_final", item_map.get("name") + ":map");

            try {
                if (item_map.get("item_type") != "OTHER") {
                    item_map.put("item_type", "BOOK");
                    item_map.put("subject", item_map.get("item_type"));
                } else {
                    item_map.put("item_type", item_map.get("item_type"));
                }
                String encodedQuery = URLEncoder.encode("SRK", "UTF-8");
                if (searchResultsSubscription != null && !searchResultsSubscription.isUnsubscribed()) {
                    searchResultsSubscription.unsubscribe();
                }

                rx.Observable<Item_list_response> observable =
                        apiObservable.getSearchResults(item_map);
                searchResultsSubscription = observable
                        .debounce(250, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(searchResultsSubscriber());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }


    private Subscriber<Item_list_response> searchResultsSubscriber() {
        return new Subscriber<Item_list_response>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
                Log.d("error_call", e.toString());
            }
            @Override
            public void onNext(Item_list_response item_list) {
                //m.onCompleteLoading();
                //TODO :initialize the list here for adapter view
                Log.d("respo",item_list.getStatusCode().toString());
                if(item_list.getStatusCode()==200) {
                    Log.d("size_search", "" + item_list.getData().size());
                    suggestion = new ArrayList<>();
                    Iterator<Item_structure> itrTemp = item_list.getData().iterator();
                    while (itrTemp.hasNext()) {
                        suggestion.add(" " + itrTemp.next().getName());
                    }
                    if (!suggestion.isEmpty()) {
                        searchView.clearSuggestions();
                        searchView.clearHistory();
                        searchView.addSuggestions(suggestion);
                        set_search_view_items(item_list);
                    }
                }
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

                Snackbar.make(coordinatorLayout, "Error Occured !!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                swipeRefreshLayout.setRefreshing(false);
                Log.d("error_call", e.toString());
            }

            @Override
            public void onNext(Item_list_response item_list_response) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d("respo", item_list_response.getData() + "");
                if(item_list_response.getStatusCode()==200){
                    set_new_List_Adapter(item_list_response);
                }
                else{
                    Snackbar.make(coordinatorLayout, "Failed to get response!!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
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
                    MAIN_ITEM_LIST = item_list_response.getData();
                    set_new_List_Adapter(item_list_response);
                }
            }
        };
    }


}