package com.example.avinash.nittcart.display_item_files;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.avinash.nittcart.R;
import com.example.avinash.nittcart.retrofit_api_calls.ApiObservable;
import com.google.gson.JsonObject;
import com.roger.catloadinglibrary.CatLoadingView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscription;

/**
 * Created by AVINASH on 4/2/2017.
 */
public class Dash_DetailActivity extends AppCompatActivity {

    private String BASE_IMAGE_URL="http://139.59.79.221:80/static/images/";

    private Subscription searchResultsSubscription;
    private ApiObservable apiObservable;
    Call<JsonObject> result;
    retrofit2.Call<JsonObject> getinfo;
    ProgressDialog progressDialog;
    CatLoadingView mView;
    @Bind(R.id.add_to_cart)
    Button add_to_cart;
    @Bind(R.id.item_image)
    ImageView item_image;
    @Bind(R.id.item_name)
    TextView item_name;
    @Bind(R.id.item_price)
    TextView item_price;
    @Bind(R.id.product_id)
    TextView item_id;
    @Bind(R.id.item_description)
    TextView item_description;
    @Bind(R.id.item_verified)
    TextView item_verified;
    @Bind(R.id.item_quality)
    TextView item_quality;


    private String SELLERS_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.griddetail_activity);
        ButterKnife.bind(this);

        apiObservable = new ApiObservable();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding..");
        mView = new CatLoadingView();

        final Intent intent = getIntent();
        item_name.setText("Name: "+intent.getStringExtra("item_name"));
        item_price.setText("₹"+intent.getIntExtra("item_price",0));
        item_id.setText("Item id: "+intent.getStringExtra("item_id"));
        item_description.setText(intent.getStringExtra("item_description"));
        item_verified.setText(intent.getStringExtra("item_status"));
        item_quality.setText(intent.getStringExtra("item_quality"));
        Picasso.with(Dash_DetailActivity.this).
                load(BASE_IMAGE_URL+intent.getStringExtra("item_image"))
                .placeholder(R.drawable.book).error(R.drawable.alert).into(item_image);

        add_to_cart.setText("View Buyers' INFO");
        SELLERS_ID = intent.getStringExtra("seller_id");
        add_to_cart.setVisibility(View.VISIBLE);
        //TODO: check status


        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView.show(getSupportFragmentManager(), "LOADING..");
                loadprofile(getIntent().getStringExtra("user_id"),intent.getStringExtra("auth_token"));
            }
        });
    }

    public void loadprofile(String user_id,String auth_token){
        Log.d("user_id", user_id);
        Log.d("token",auth_token);
        //apiObservable=null;
        //getinfo=null;
        //TODO: GET BUYERS INFO HERE
        apiObservable = new ApiObservable();
        getinfo = apiObservable.get_buyer_info(user_id, auth_token,getIntent().getStringExtra("item_id"));
        getinfo.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
                mView.dismiss();
                if (response.isSuccessful()) {
                    try {
                        //user_layout.setVisibility(View.VISIBLE);
                        JSONObject object = new JSONObject(response.body().toString());
                        Log.d("profile Response", object.toString());
                        int respone_code = (int) object.get("status_code");
                        if (respone_code == 200) {
                            JsonObject data = response.body().get("data").getAsJsonObject();

                            View view = getLayoutInflater().inflate( R.layout.seller_buyer, null);
                            TextView tv = (TextView)view.findViewById(R.id.name);
                            TextView tv1 = (TextView)view.findViewById(R.id.roll_number);
                            TextView tv2 = (TextView)view.findViewById(R.id.email);
                            TextView tv3 = (TextView)view.findViewById(R.id.mobile);
                            TextView tv4 = (TextView)view.findViewById(R.id.address);
                            tv.setText(data.get("name").getAsString());
                            tv1.setText(data.get("roll_no").getAsString());
                            tv2.setText(data.get("email").getAsString());
                            tv3.setText(data.get("mobile_no").getAsString());
                            tv4.setText(data.get("address").getAsString());
                            AlertDialog.Builder dialog = new AlertDialog.Builder( Dash_DetailActivity.this);
                            dialog.setView(view);
                            dialog.show();

                        } else {

                            Snackbar.make(add_to_cart, "No buyer found for this item!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Snackbar.make(add_to_cart, "Error Occured!!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                mView.dismiss();
                Snackbar.make(add_to_cart, "Network error!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onBackPressed(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
            result.cancel();
        }else
            super.onBackPressed();
    }
}
