package com.example.avinash.nittcart.display_item_files;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.avinash.nittcart.R;
import com.example.avinash.nittcart.retrofit_api_calls.ApiObservable;
import com.google.gson.JsonObject;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by AVINASH on 4/14/2017.
 */
public class user_profile extends SwipeDismissBaseActivity {

    private ApiObservable apiObservable;
    retrofit2.Call<JsonObject> getinfo;
    retrofit2.Call<JsonObject> postinfo;
    RelativeLayout user_layout;
    CatLoadingView mView;

    TextView profile_image;
    @Bind(R.id.user_profile_name)
    TextView user_name;
    @Bind(R.id.email)
    TextView user_email;
    @Bind(R.id.address)
    TextView user_address;
    @Bind(R.id.mobile)
    TextView user_contact;
    @Bind(R.id.ed_user_profile_name)
    EditText edit_user_name;
    @Bind(R.id.ed_email)
    EditText edit_user_email;
    @Bind(R.id.ed_mobile)
    EditText edit_user_contact;
    @Bind(R.id.ed_address)
    EditText edit_user_address;
    @Bind(R.id.user_profile_short_bio)
    TextView user_roll_no;

    @Bind(R.id.refresh)
    ImageView refresh;
    @Bind(R.id.edit)
    ImageView edit;
    @Bind(R.id.save)
    ImageView save;

    Boolean mode_edit = true;
    String AUTH_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWindowAnimations();
        setContentView(R.layout.user_profile);
        ButterKnife.bind(this);
        mView = new CatLoadingView();
        profile_image = (TextView) findViewById(R.id.user_profile_photo);
        final Intent intent = getIntent();
        //GestureDetectorCompat gestureDetector = new GestureDetector(new SwipeDetector());

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView.show(getSupportFragmentManager(), "LOADING..");
                loadprofile(intent.getStringExtra("user_id"), intent.getStringExtra("auth_token"));
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mView.show(getSupportFragmentManager(), "LOADING..");
                loadprofile(intent.getStringExtra("user_id"), intent.getStringExtra("auth_token"));
            }
        }, 600);

        Log.d("user_id", intent.getStringExtra("user_id"));
        Log.d("token", intent.getStringExtra("auth_token"));
        //user_layout = (RelativeLayout) findViewById(R.id.user_profile_layout);
        //user_layout.setVisibility(View.VISIBLE);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility();

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (validate()) {
                            mView.show(getSupportFragmentManager(), "LOADING..");
                            updateprofile(intent.getStringExtra("user_id"), intent.getStringExtra("auth_token"), edit_user_email.getText().toString(), edit_user_name.getText().toString(),
                                    edit_user_address.getText().toString(), edit_user_contact.getText().toString());
                        }
                        reverseVisibility();
                    }
                });

            }
        });

}
    public void setVisibility(){
        mode_edit = false;

        save.setVisibility(View.VISIBLE);
        edit.setVisibility(View.GONE);
        //user_name.setVisibility(View.GONE);
        user_email.setVisibility(View.GONE);
        user_contact.setVisibility(View.GONE);
        user_address.setVisibility(View.GONE);
        edit_user_name.setVisibility(View.VISIBLE);
        edit_user_name.setText(user_name.getText().toString());
        edit_user_email.setVisibility(View.VISIBLE);
        edit_user_email.setText(user_email.getText().toString());
        edit_user_contact.setVisibility(View.VISIBLE);
        edit_user_contact.setText(user_contact.getText().toString());
        edit_user_address.setVisibility(View.VISIBLE);
        edit_user_address.setText(user_address.getText().toString());
        edit_user_name.setFocusable(true);
    }

    public void reverseVisibility(){
        save.setVisibility(View.GONE);
        edit.setVisibility(View.VISIBLE);
        user_name.setVisibility(View.VISIBLE);
        user_email.setVisibility(View.VISIBLE);
        user_contact.setVisibility(View.VISIBLE);
        user_address.setVisibility(View.VISIBLE);
        user_roll_no.setVisibility(View.VISIBLE);
        edit_user_name.setVisibility(View.GONE);
        edit_user_email.setVisibility(View.GONE);
        edit_user_contact.setVisibility(View.GONE);
        edit_user_address.setVisibility(View.GONE);
    }

    public boolean validate() {
        boolean valid = true;

        if (edit_user_email.getText().toString().isEmpty()) {
            edit_user_email.requestFocus();
            edit_user_email.setError("enter a valid email address");
            valid = false;
        } else {
            edit_user_email.setError(null);
        }

        if (edit_user_name.getText().toString().isEmpty()) {
            edit_user_name.requestFocus();
            edit_user_name.setError("enter a valid email address");
            valid = false;
        } else {
            edit_user_name.setError(null);
        }
        if (edit_user_address.getText().toString().isEmpty()) {
            edit_user_address.requestFocus();
            edit_user_address.setError("enter a valid email address");
            valid = false;
        } else {
            edit_user_address.setError(null);
        }
        if (edit_user_contact.getText().toString().isEmpty()) {
            edit_user_contact.requestFocus();
            edit_user_contact.setError("enter a valid email address");
            valid = false;
        } else {
            edit_user_contact.setError(null);
        }

        return valid;
    }

    public void updateprofile(String user_id,String auth_token, final String email, final String name, final String address, final String contact){
        apiObservable = new ApiObservable();
        postinfo = apiObservable.postStudentInfo(user_id, auth_token, email, name, address,contact);
        postinfo.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {

                mView.dismiss();
                if (response.isSuccessful()) {
                    try {//user_layout.setVisibility(View.VISIBLE);
                        JSONObject object = new JSONObject(response.body().toString());
                        Log.d("profile Response", object.toString());
                        int respone_code = (int) object.get("status_code");
                        if (respone_code == 200) {
                            mode_edit = true;
                            //TODO
                            user_name.setText(name);
                            user_email.setText(email);
                            user_contact.setText(contact);
                            user_address.setText(address);
                            profile_image.setText(name.toUpperCase());

                            reverseVisibility();
                            Snackbar.make(profile_image, "Successfully posted!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } else {
                            Snackbar.make(profile_image, "Error Occured!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            //network_error.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                else{
                    Snackbar.make(profile_image, "Some Error Occured!!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                mView.dismiss();
                Snackbar.make(profile_image, "Network error!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void loadprofile(String user_id,String auth_token){
        Log.d("user_id", user_id);
        Log.d("token",auth_token);
        //apiObservable=null;
        //getinfo=null;
        apiObservable = new ApiObservable();
        getinfo = apiObservable.getStudentInfo(user_id, auth_token);
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
                            user_name.setText(data.get("name").getAsString());
                            user_email.setText(data.get("email").getAsString());
                            user_contact.setText(data.get("contact_no").getAsString());
                            user_address.setText(data.get("address").getAsString());
                            user_roll_no.setText("Welcome " + data.get("roll_no").getAsString());
                            String name = data.get("name").getAsString();
                            Log.d("name:",name+"");
                            profile_image.setText(name);

                            //TODO: Set cart Items here

                        } else {
                            Snackbar.make(profile_image, "Error Occured!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();}
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Snackbar.make(profile_image , "Some Error Occured!!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                mView.dismiss();
                Snackbar.make(profile_image, "Network error!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setupWindowAnimations() {
        Slide fade = new Slide();
        fade.setDuration(500);
        fade.setSlideEdge(Gravity.END);
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.END);

        slide.setDuration(500);
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
