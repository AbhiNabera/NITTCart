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
public class Cart_DetailActivity extends AppCompatActivity {

    private String BASE_IMAGE_URL="http://139.59.79.221:80/static/images/";

    private Subscription searchResultsSubscription;
    private ApiObservable apiObservable;
    Call<JsonObject> result;
    ProgressDialog progressDialog;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.griddetail_activity);
        ButterKnife.bind(this);

        apiObservable = new ApiObservable();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding..");

        final Intent intent = getIntent();
        item_name.setText(intent.getStringExtra("item_name"));
        item_price.setText(intent.getStringExtra("item_price"));
        item_id.setText(intent.getStringExtra("item_id"));
        item_description.setText(intent.getStringExtra("item_description"));
        item_verified.setText(intent.getStringExtra("item_status"));
        item_quality.setText(intent.getStringExtra("item_quality"));
        Picasso.with(Cart_DetailActivity.this).
                load(BASE_IMAGE_URL+intent.getStringExtra("item_image"))
                .placeholder(R.drawable.book).error(R.drawable.alert).into(item_image);


        add_to_cart.setText("View Sellers' INFO");
        //add_to_cart.setVisibility(View.VISIBLE);
        //TODO: check status

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(intent.getStringExtra("item_status").matches("UNSOLD")){
                    result = apiObservable.get_seller_info(intent.getStringExtra("user_id"),
                            intent.getStringExtra("auth_token"),
                            intent.getStringExtra("item_id"));
                    progressDialog.show();
                    result.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            progressDialog.dismiss();
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject object = new JSONObject(response.body().toString());
                                    Log.d("Seller info", response.body().toString());
                                    int respone_code = (int) object.get("status_code");
                                    if (respone_code == 200) {
                                        View view = getLayoutInflater().inflate( R.layout.seller_buyer, null);
                                        TextView tv = (TextView)view.findViewById(R.id.name);
                                        TextView tv1 = (TextView)view.findViewById(R.id.roll_number);
                                        TextView tv2 = (TextView)view.findViewById(R.id.email);
                                        TextView tv3 = (TextView)view.findViewById(R.id.mobile);
                                        TextView tv4 = (TextView)view.findViewById(R.id.address);
                                        AlertDialog.Builder dialog = new AlertDialog.Builder( Cart_DetailActivity.this);
                                        dialog.setView(view);
                                        dialog.show();
                                        progressDialog.dismiss();

                                    } else {
                                        progressDialog.dismiss();
                                        Snackbar.make(item_image, "Error Occured!!", Snackbar.LENGTH_LONG)
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
                            Snackbar.make(item_image, "Network error!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });
                }
                else {
                    Snackbar.make(item_image, "ITEM OUT OF STOCK!!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
    }

    void setAlertdialog(){

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
