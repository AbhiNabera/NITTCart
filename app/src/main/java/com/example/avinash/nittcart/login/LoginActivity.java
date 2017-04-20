package com.example.avinash.nittcart.login;

/**
 * Created by AVINASH on 3/9/2017.
 */

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.avinash.nittcart.MainActivity;
import com.example.avinash.nittcart.R;
import com.example.avinash.nittcart.retrofit_api_calls.ApiObservable;
import com.example.avinash.nittcart.tab_and_bottombar.my_order;
import com.facebook.rebound.SimpleSpringListener;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.google.gson.JsonObject;
import com.gordonwong.materialsheetfab.AnimatedFab;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements AnimatedFab {

    SignupActivity register;
    ImageButton imageButton;
    ProgressDialog progressDialog;
    RelativeLayout loginview,  insidelv, Insiderv;
    Animation alphaAnimation ,alphaAnimation2,
            animShow, animHide , animShow2, animHide2 ;
    ScrollView registerview;
    float pixelDensity;
    boolean flag = true;
    int x,y;


    retrofit2.Call<JsonObject> getinfo;
    retrofit2.Call<JsonObject> postinfo;

    public static CatLoadingView mView;
    private static String AUTH_TOKEN = null;
    TokenDatabase token;

    private MatrixCursor matrixCursor;
    private ApiObservable apiObservable;
    Call<JsonObject> token_user;
    private static String USER_ID ;

    public static  ProgressDialog progressDialog2;
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;

    //-------------signup------------------//

    @Bind(R.id.user_name)
    EditText _nameText;
    @Bind(R.id.user_address)
    EditText _addressText;
    @Bind(R.id.user_email_id)
    EditText reg_emailText;
    @Bind(R.id.user_contact_no)
    EditText _contact_no;
    @Bind(R.id.btn_register)
    Button _signupButton;
    @Bind(R.id.link_login)
    TextView _loginLink;
    @Bind(R.id.skiplogin)
    TextView skiplogin;


////////////////////////////////////////

    //@TargetApi(Build.VERSION_CODES.LOLLIPOP)

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWindowAnimations();
        setContentView(R.layout.dialoglogin);
        ButterKnife.bind(this);
        //setupWindowAnimations();
        mView = new CatLoadingView();
        token = new TokenDatabase(LoginActivity.this);
        Cursor cr=checkToken(token);
        if(cr!=null){
            Log.d("user Authenticated", "");
            progressDialog2 = new ProgressDialog(LoginActivity.this);
            progressDialog2.setIndeterminate(true);
            progressDialog2.setMessage("Saving..");

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Authenticating....");
            progressDialog.show();
            //TODO put token and other info in intent extra
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    //TODO add other extra info with intent
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    finish();
                }
            }, 500);
        }

        progressDialog = new ProgressDialog(LoginActivity.this);

        register = new SignupActivity();
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("inside", "yo");
                if(validate2()){
                    updateprofile(USER_ID,AUTH_TOKEN,reg_emailText.getText().toString(),
                            _nameText.getText().toString(),_addressText.getText().toString(),_contact_no.getText().toString());
                }
            }
        });

        skiplogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                registerview.startAnimation(animHide2);
                registerview.setVisibility(View.GONE);
                loginview.setVisibility(View.VISIBLE);
                loginview.startAnimation(animShow2);
                //imageButton.setBackgroundResource(R.drawable.cart_and_plceorder_back);
                imageButton.setImageResource(R.drawable.ic_newuser);

                //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        pixelDensity = getResources().getDisplayMetrics().density;
        imageButton = (ImageButton)findViewById(R.id.user_profile_photo);
        loginview = (RelativeLayout)findViewById(R.id.relativeLayout);
        registerview = (ScrollView)findViewById(R.id.relativeLayout2);
        insidelv = (RelativeLayout)findViewById(R.id.inside_rl);
        Insiderv = (RelativeLayout)findViewById(R.id.registration_rllayout);

        alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_anim);
        alphaAnimation2= AnimationUtils.loadAnimation(this, R.anim.fadeout2);
        animShow = AnimationUtils.loadAnimation( this, R.anim.showlogin);
        animHide = AnimationUtils.loadAnimation( this, R.anim.transitionlogin);
        animShow2 = AnimationUtils.loadAnimation( this, R.anim.registershow);
        animHide2 = AnimationUtils.loadAnimation(this, R.anim.registerhide);



        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                flag=false;
                loginview.startAnimation( animHide );
                loginview.setVisibility(View.GONE);
                registerview.setVisibility(View.VISIBLE);
                registerview.startAnimation(animShow);
                //imageButton.setBackgroundResource(R.drawable.rotate_incart);
                imageButton.setImageResource(R.drawable.close_menu);

            }
        });
    }

    public boolean validate2() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = reg_emailText.getText().toString();
        String mobile = _contact_no.getText().toString();


        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            reg_emailText.setError("enter a valid email address");
            valid = false;
        } else {
            reg_emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            _contact_no.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _contact_no.setError(null);
        }

        return valid;
    }

    public void updateprofile(String user_id,String auth_token, final String email, final String name, final String address, final String contact){
        apiObservable = new ApiObservable();
        //mView.show(getSupportFragmentManager(), "LOADING..");
        progressDialog.setMessage("Saving..");
        progressDialog.show();
        postinfo = apiObservable.postStudentInfo(user_id, auth_token,email,name,address,contact);
        postinfo.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {

                //mView.dismiss();
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {//user_layout.setVisibility(View.VISIBLE);
                        JSONObject object = new JSONObject(response.body().toString());
                        Log.d("profile Response", object.toString());
                        int respone_code = (int) object.get("status_code");
                        if (respone_code == 200) {

                            Snackbar.make(_emailText, "Successfully posted!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            finish();
                        } else {
                            Snackbar.make(_emailText, "Error Occured!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            //network_error.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Snackbar.make(_emailText, "Error Occured!!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                //mView.dismiss();
                progressDialog.dismiss();                //network_error.setVisibility(View.VISIBLE);
                Snackbar.make(_emailText, "Network error!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(700);
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.START);

        slide.setDuration(1000);
        //getWindow().setEnterTransition(slide);
        //getWindow().setReturnTransition(fade);
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        authenticate(email, password);
    }


    public void authenticate(final String email,String password){

        apiObservable = new ApiObservable();
        token_user = apiObservable.loginApi(email,password);

        token_user.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {
                    try {
                        JsonObject object = response.body().getAsJsonObject();
                        Log.d("LOGIN Response", response.body().toString());

                        int respone_code = object.get("status_code").getAsInt();
                        if (respone_code == 200) {
                            JsonObject data = object.get("data").getAsJsonObject();
                            Log.e("database", "");
                            String auth_token = data.get("auth_token").getAsString();
                            String user_id = data.get("user_id").getAsString();
                            AUTH_TOKEN =auth_token;
                            USER_ID = user_id;
                            db_storeToken(auth_token, user_id, email, 0, 0, 0);
                            if (data.get("is_active").getAsString().matches("FALSE")) {
                                loginview.startAnimation( animHide );
                                loginview.setVisibility(View.GONE);
                                registerview.setVisibility(View.VISIBLE);
                                registerview.startAnimation(animShow);
                            } else {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                finish();
                            }

                        } else {
                            //Toast.makeText(LoginActivity.this,"Invalid Credentials!!",Toast.LENGTH_SHORT).show();
                            Snackbar.make(_loginButton, "Check Credentials!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Snackbar.make(loginview, "Error Occured!!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Log.e("error", response.errorBody().toString());
                    InputStream i = response.errorBody().byteStream();
                    BufferedReader r = new BufferedReader(new InputStreamReader(i));
                    StringBuilder errorResult = new StringBuilder();
                    String line;
                    try {
                        while ((line = r.readLine()) != null) {
                            errorResult.append(line).append('\n');
                        }
                        Log.d("errrrrr", errorResult.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                _loginButton.setEnabled(true);
                progressDialog.dismiss();

                //TODO: successfully logged in: check result code and store token in database and move to mainactivity
                //db_storeToken();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Log error here since request failed
                _loginButton.setEnabled(true);
                Snackbar.make(loginview, "Network error!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                progressDialog.dismiss();
                Log.e(TAG, t.toString());
            }
        });
    }


    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        if(progressDialog.isShowing()){
            _loginButton.setEnabled(true);
            progressDialog.dismiss();
            token_user.cancel();
        }
        else {
            //moveTaskToBack(true);
            super.onBackPressed();
        }

    }

    public void db_storeToken(String Token,String user_id,String roll_no, int cart_count, int notification_count, int dashboard_count){
        token.insertToken(Token,user_id,roll_no,cart_count,notification_count,dashboard_count);
    }

    public Cursor checkToken(TokenDatabase token){
        Cursor cr = token.getToken(0);
        cr.moveToFirst();
        Log.d("count",""+cr.getCount());
        if(cr.getCount() !=0)return cr;
        return null;
    }

    public void logout(){
        token.drop();
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty()) {
            _emailText.requestFocus();
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.requestFocus();
            _passwordText.setError("password required");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)

    public void launchAnimation() {
        int hypotenuse = (int) Math.hypot(registerview.getWidth(), registerview.getHeight());

        if (flag) {

            hypotenuse = (int) Math.hypot(loginview.getWidth(), loginview.getHeight());
            x = loginview.getRight();
            y = loginview.getTop();
            int dx=loginview.getWidth();
            int dy=loginview.getHeight();

            x -= ((28 * pixelDensity) + (16 * pixelDensity));

            loginview.setVisibility(View.GONE);
            //loginview.startAnimation(alphaAnimation2);

            registerview.setVisibility(View.VISIBLE);
            imageButton.setBackgroundResource(R.drawable.user_profile_image_background);
            imageButton.setImageResource(R.drawable.close_menu);

            Animator anim = ViewAnimationUtils.createCircularReveal(registerview,x/2 , y/2+150, 0, hypotenuse);

            anim.setDuration(800);

            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {

                    //loginview.setVisibility(View.GONE);
                    //loginview.startAnimation(alphaAnimation2);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            anim.start();

            flag = false;
        } else {

            x = loginview.getRight();
            y = loginview.getTop();

            imageButton.setBackgroundResource(R.drawable.user_profile_image_background);
            imageButton.setImageResource(R.drawable.ic_newuser);

            Animator anim = ViewAnimationUtils.createCircularReveal(registerview, x/2, y/2+150, hypotenuse, 0);
            anim.setDuration(500);

            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    registerview
                            .setVisibility(View.GONE);
                    loginview.setVisibility(View.VISIBLE);
                    loginview.startAnimation(alphaAnimation);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            anim.start();
            flag = true;

        }
    }

    @Override
    public void show() {
        show(0,0);
    }

    @Override
    public void show(float translationX, float translationY) {

    }

    @Override
    public void hide() {

    }
}
