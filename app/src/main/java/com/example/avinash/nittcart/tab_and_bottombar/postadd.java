package com.example.avinash.nittcart.tab_and_bottombar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.avinash.nittcart.R;
import com.example.avinash.nittcart.display_item_files.itemcondition_adapter;
import com.example.avinash.nittcart.display_item_files.itemtype_adapter;
import com.example.avinash.nittcart.login.TokenDatabase;
import com.example.avinash.nittcart.retrofit_api_calls.ApiObservable;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by AVINASH on 3/11/2017.
 */
public class postadd extends AppCompatActivity {
    Context context;
    private Uri outputFileUri, outputFileUri2;
    private int CHOOSER_REQUEST_CODE = 200 , CROP_REQUEST_CODE = 100;
    boolean move = false;
    Bitmap photo;
    int imageflag;
    public static String category = null;
    public static String condition = null;
    public boolean read_permission = true , write_permission = true;

    private static Uri IMAGE_URI;
    private String REAL_PATH;
    private String mCurrentPhotoPath;
    File imageF;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    private String USER_ID;
    private String AUTH_TOKEN;
    String NAME;
    String DESCRIPTION;
    MultipartBody.Part IMAGE;
    public static String ITEM_TYPE;
    String PRICE;
    public static String QUALITY;
    private TokenDatabase tokenDatabase;

    private ApiObservable apiObservable;
    Call<JsonObject> post_item;
    ProgressDialog progressDialog;
    private String BASE_IMAGE_URL="http://139.59.79.221:80/static/images/";

    String[] perms = {  "android.permission.WRITE_EXTERNAL_STORAGE" ,
            "android.permission.READ_EXTERNAL_STORAGE"};

    String[] tag = {
            "Chemistry","Physics","Maths","English","CSE","CIVIL","CHEM.","EEE","ECE","ICE","MECH","MME","PROD", "Others"
    };
    int[] imageId = {
            R.drawable.book, R.drawable.book, R.drawable.book, R.drawable.book, R.drawable.book,
            R.drawable.book,R.drawable.book,R.drawable.book,R.drawable.book,R.drawable.book,
            R.drawable.book,R.drawable.book,R.drawable.book, R.drawable.others
    };

    String[] tag2 = {
            "Good as new", "Fairly used", "Heavily used"
    };
    int[] imageId2 = {
            R.drawable.condnew, R.drawable.used, R.drawable.old
    };

    @Bind(R.id.gridview)
    GridView gridtype;
    @Bind(R.id.gridview2)
    GridView gridcondition;
    @Bind(R.id.item_image)
    ImageView item_image;
    @Bind(R.id.item_name)
    EditText item_name;
    @Bind(R.id.item_description)
    EditText item_description;
    @Bind(R.id.item_price)
    EditText item_price;
    @Bind(R.id.closePostadd)
    ImageView closePostadd;
    @Bind(R.id.donate)
    RadioButton donate;
    @Bind(R.id.postadd)
    RadioButton postadd;

    private Bitmap BIT_IMAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWindowAnimations();
        setContentView(R.layout.postad);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requesreadtpermission();
        }

        category = null;
        condition = null;

        tokenDatabase = new TokenDatabase(postadd.this);
        final Intent intent = getIntent();
        AUTH_TOKEN = intent.getStringExtra("auth_token");
        USER_ID = intent.getStringExtra("user_id");

        if(intent.getStringExtra("item_id")!=null){
            item_name.setText(intent.getStringExtra("item_name"));
            item_description.setText(intent.getStringExtra("item_description"));
            item_price.setText(""+intent.getIntExtra("item_price",0));
            QUALITY = intent.getStringExtra("item_quality");
            ITEM_TYPE = intent.getStringExtra("item_type");//not confirmed
            photo = BitmapFactory.decodeResource(postadd.this.getResources(), R.drawable.ic_user);
            Picasso.with(postadd.this).
                    load(BASE_IMAGE_URL+intent.getStringExtra("item_image"))
                    .placeholder(R.drawable.book).error(R.drawable.alert).into(item_image, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    BIT_IMAGE = ((BitmapDrawable)item_image.getDrawable()).getBitmap();
                }

                @Override
                public void onError() {
                    BIT_IMAGE = ((BitmapDrawable)item_image.getDrawable()).getBitmap();
                }
            });
        }

        mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        progressDialog = new ProgressDialog(postadd.this);
        progressDialog.setCancelable(false);
        //progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Uploading...");


        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.e("Clicked back", "Enter toh ho gaya!");
                postadd.setChecked(false);
                donate.setChecked(false);
                if(post_item!=null)post_item.cancel();
            }
        });


        donate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (postadd.isChecked()) postadd.setChecked(false);
                if (validate1() && donate.isChecked()) {
                    donate.setChecked(true);

                    NAME = item_name.getText().toString();
                    DESCRIPTION = item_description.getText().toString();
                    PRICE = "0";
                    IMAGE = get_upload_File();


                    AlertDialog.Builder alert = new AlertDialog.Builder(postadd.this);
                    AlertDialog dialog = alert.create();
                    alert.setTitle("DONATE!!");
                    alert.setIcon(R.drawable.alert);
                    dialog.setCanceledOnTouchOutside(false);
                    alert.setMessage("You are about to donate your item.Your price will not be considered" +
                            " .Free item add will be posted.Do you want to proceed?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO: Check here for user is authentication and postitem on server
                            if (intent.getStringExtra("item_id") != null) {
                                editItem(intent.getStringExtra("item_id"),
                                        USER_ID, AUTH_TOKEN, NAME, DESCRIPTION, ITEM_TYPE, PRICE, QUALITY, IMAGE);
                            } else
                                postItem(USER_ID, AUTH_TOKEN, NAME, DESCRIPTION, IMAGE, ITEM_TYPE, PRICE, QUALITY);
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            donate.setChecked(false);
                        }
                    });
                    alert.show();
                } else donate.setChecked(false);
            }
        });


        postadd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (donate.isChecked()) donate.setChecked(false);
                if (validate() && postadd.isChecked()) {
                    postadd.setChecked(true);

                    NAME = item_name.getText().toString();
                    DESCRIPTION = item_description.getText().toString();
                    PRICE = item_price.getText().toString();
                    IMAGE = get_upload_File();


                    AlertDialog.Builder alert = new AlertDialog.Builder(postadd.this);
                    AlertDialog dialog = alert.create();
                    alert.setTitle("POST_ADD!!");
                    dialog.setCanceledOnTouchOutside(false);
                    alert.setIcon(R.drawable.alert);
                    alert.setMessage("Do you want to post add for your item?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO: Check here for user is authentication and postitem on server

                            if(intent.getStringExtra("item_id")!=null){
                                editItem(intent.getStringExtra("item_id"),
                                        USER_ID, AUTH_TOKEN, NAME, DESCRIPTION, ITEM_TYPE, PRICE, QUALITY,IMAGE);
                            }
                            else
                                postItem(USER_ID, AUTH_TOKEN, NAME, DESCRIPTION, IMAGE, ITEM_TYPE, PRICE, QUALITY);
                        }
                    });

                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            postadd.setChecked(false);
                        }
                    });
                    alert.show();
                } else postadd.setChecked(false);
            }
        });

        closePostadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAfterTransition();
                //overridePendingTransition(R.anim.activity_slide_down, R.anim.activity_no_slide_tabitem);
            }
        });

        itemtype_adapter adapter = new itemtype_adapter(postadd.this, tag, imageId);
        gridtype.setAdapter(adapter);
        itemcondition_adapter adapter2 = new itemcondition_adapter(postadd.this, tag2, imageId2);
        gridcondition.setAdapter(adapter2);

        item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickimage();
            }
        });
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

    public void postItem(String user_id, String auth_token, String name,
                         String description, MultipartBody.Part image, String item_type,
                         String price, String quality) {


        //both wor
        HashMap<String,String> item_map = new HashMap<>();
        item_map.put("auth_token",auth_token);
        item_map.put("user_id",user_id);
        item_map.put("name",name);
        item_map.put("description",description);
        item_map.put("item_type",item_type);
        item_map.put("price",price);
        item_map.put("quality",quality);



        HashMap<String,RequestBody> map=new HashMap<>();
        map.put("auth_token",RequestBody.create(MediaType.parse("text/plain"), auth_token));
        map.put("user_id",RequestBody.create(MediaType.parse("text/plain"), user_id));
        map.put("name",RequestBody.create(MediaType.parse("text/plain"), name));
        map.put("description",RequestBody.create(MediaType.parse("text/plain"), description));

        map.put("price",RequestBody.create(MediaType.parse("text/plain"), price));
        map.put("quality",RequestBody.create(MediaType.parse("text/plain"), quality));
        //map.put("image",mFile);

        if(!item_type.matches("OTHER")){
            map.put("item_type",RequestBody.create(MediaType.parse("text/plain"), "BOOK"));
            map.put("subject", RequestBody.create(MediaType.parse("text/plain"), item_type));
        }
        else{
            map.put("item_type",RequestBody.create(MediaType.parse("text/plain"), item_type));
        }

//        Log.d("image",image.toString());
        apiObservable = new ApiObservable();
        post_item = apiObservable.postItem(map, image);
        progressDialog.show();

        post_item.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        JsonObject object = response.body().getAsJsonObject();
                        Log.d("Post Response", response.body().toString());
                        int respone_code = object.get("status_code").getAsInt();
                        if (respone_code == 200) {
                            Cursor cr = tokenDatabase.getToken(0);
                            cr.moveToFirst();
                            //tokenDatabase.update_dash_count(cr.getInt(cr.getColumnIndex("dashboard_count")+1));
                            Snackbar.make(postadd, "Successfully Uploaded", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            finishAfterTransition();
                            //overridePendingTransition(R.anim.activity_slide_down, R.anim.activity_no_slide_tabitem);
                        } else {
                            Snackbar.make(postadd, "Uploading failed!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            postadd.setChecked(false);
                            donate.setChecked(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Snackbar.make(postadd, "Error Occured!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                t.printStackTrace();
                postadd.setChecked(false);
                donate.setChecked(false);
                progressDialog.dismiss();
            }
        });
    }


    public void editItem(String item_id,String user_id, String auth_token, String name,
                         String description, String item_type,
                         String price, String quality,MultipartBody.Part image) {

        Log.d("inside","edit");
        //both work
        HashMap<String,String> item_map = new HashMap<>();
        item_map.put("auth_token",auth_token);
        item_map.put("user_id",user_id);
        item_map.put("name",name);
        item_map.put("description",description);
        //item_map.put("item_type",item_type);
        item_map.put("price",price);
        item_map.put("quality",quality);
        item_map.put("item_id",item_id);

        HashMap<String,RequestBody> map=new HashMap<>();
        map.put("item_id",RequestBody.create(MediaType.parse("text/plain"), item_id));
        map.put("auth_token",RequestBody.create(MediaType.parse("text/plain"), auth_token));
        map.put("user_id",RequestBody.create(MediaType.parse("text/plain"), user_id));
        map.put("name",RequestBody.create(MediaType.parse("text/plain"), name));
        map.put("description",RequestBody.create(MediaType.parse("text/plain"), description));
        //map.put("item_type",RequestBody.create(MediaType.parse("text/plain"), item_type));
        map.put("price",RequestBody.create(MediaType.parse("text/plain"), price));
        map.put("quality",RequestBody.create(MediaType.parse("text/plain"), quality));

        if(!item_type.matches("OTHER")){
            item_map.put("item_type", "BOOK");
            item_map.put("subject",item_type);
            map.put("item_type",RequestBody.create(MediaType.parse("text/plain"), "BOOK"));
            map.put("subject",RequestBody.create(MediaType.parse("text/plain"), item_type));
        }
        else{
            item_map.put("item_type", item_type);
            map.put("item_type",RequestBody.create(MediaType.parse("text/plain"), item_type));
        }

        apiObservable = new ApiObservable();
        if(image==null) {
            Log.d("image","nul");
            post_item = apiObservable.editItem(item_map, image);
        }
        else{Log.d("image","present");
            post_item = apiObservable.edit_image_Item(map, image);
        }

        progressDialog.setMessage("Saving Changes...");
        progressDialog.show();

        post_item.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        JsonObject object = response.body().getAsJsonObject();
                        Log.d("Post Response", response.body().toString());
                        int respone_code = object.get("status_code").getAsInt();
                        if (respone_code == 200) {
                            //tokenDatabase.update_dash_count(cr.getInt(cr.getColumnIndex("dashboard_count")+1));
                            Snackbar.make(postadd, "Successfully Edited", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            finishAfterTransition();
                            //overridePendingTransition(R.anim.activity_slide_down, R.anim.activity_no_slide_tabitem);
                        } else {
                            Snackbar.make(postadd, "Editing failed!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            postadd.setChecked(false);
                            donate.setChecked(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Snackbar.make(postadd, "Editing failed!!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    postadd.setChecked(false);
                    donate.setChecked(false);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Snackbar.make(postadd, "Error Occured!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                t.printStackTrace();
                postadd.setChecked(false);
                donate.setChecked(false);
                progressDialog.dismiss();
            }
        });
    }

    public boolean validate() {

        if (photo == null) {
            Snackbar.make(item_image, "Provide item Image!!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }
        if (category == null) {
            Snackbar.make(item_image, "Choose Ctegory!!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }
        if (item_name.getText().toString().isEmpty()) {
            item_name.requestFocus();
            item_name.setError("Provide a valid Item_name!!");
            return false;
        }
        if (item_description.getText().toString().isEmpty()) {
            item_description.requestFocus();
            item_description.setError("Item description needed!!");
            return false;
        }
        if (condition == null) {
            Snackbar.make(item_image, "Select your item condition!!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }
        if (item_price.getText().toString().isEmpty()) {
            item_description.requestFocus();
            item_price.setError("Set item_price");
            return false;
        }

        return true;
    }

    public boolean validate1() {

        if (photo == null) {
            Snackbar.make(item_image, "Provide item Image!!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }
        if (category == null) {
            Snackbar.make(item_image, "Choose Ctegory!!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }
        if (item_name.getText().toString().isEmpty()) {
            item_name.requestFocus();
            item_name.setError("Provide a valid Item_name!!");
            return false;
        }
        if (item_description.getText().toString().isEmpty()) {
            item_description.requestFocus();
            item_description.setError("Item description needed!!");
            return false;
        }
        if (condition == null) {
            Snackbar.make(item_image, "Select your item condition!!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        Log.e("Clicked back", "Enter toh ho gaya!");
        postadd.setChecked(false);
        donate.setChecked(false);
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
            post_item.cancel();
        } else {
            super.onBackPressed();
            finishAfterTransition();
            //overridePendingTransition(R.anim.activity_slide_down, R.anim.activity_no_slide_tabitem);
        }
    }

    public void pickimage(){
        if(read_permission&&write_permission)
            startActivityForResult(getPickImageChooserIntent(), CHOOSER_REQUEST_CODE);
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requesreadtpermission();
        }
    }

    public Intent getPickImageChooserIntent(){
        // Determine Uri of camera image to  save.
        outputFileUri = getImageUri();
        List<Intent> allIntents = new  ArrayList<>();
        PackageManager packageManager =  getPackageManager();

        // collect all camera intents
        Intent captureIntent = new  Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam =  packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new  Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery =  packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new  Intent(galleryIntent);
            intent.setComponent(new  ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the  list  so pickup the useless one
        Intent mainIntent =  allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if  (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);
        Intent chooserIntent =  Intent.createChooser(mainIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,  allIntents.toArray(new Parcelable[allIntents.size()]));
        return chooserIntent;
    }

    public Uri getImageUri() {
        File f = null;

        try{
            f = setUpPhotoFile();
            mCurrentPhotoPath = f.getAbsolutePath();
            Log.e("uri", Uri.fromFile(f).toString());
        } catch (IOException e) {
            e.printStackTrace();
            f = null;
            mCurrentPhotoPath = null;
        }
        return Uri.fromFile(f);
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File albumF = getAlbumDir();
        imageF = File.createTempFile(imageFileName, ".jpg", albumF);
        return imageF;
    }

    private String getAlbumName() {
        return "NittCart";
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    public Uri getPickImageResultUri(Intent  data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            Log.e("not null",data.getData().toString());
            String action = data.getAction();
            isCamera = action != null  && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ?  getCaptureImageOutputUri() : data.getData();
    }

    private Uri getCaptureImageOutputUri() {
        Log.d("uri cap image", Uri.fromFile(imageF).toString());
        return Uri.fromFile(imageF);
    }

    @Override
    protected void onActivityResult(int  requestCode, int resultCode, Intent data) {
        Uri imageUri = getPickImageResultUri(data);
        if (resultCode == Activity.RESULT_OK){
            if(requestCode == CROP_REQUEST_CODE){
                try {
                    photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    item_image.setImageBitmap(photo);
                    IMAGE_URI = imageUri;
                    get_upload_File();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(requestCode == CHOOSER_REQUEST_CODE){
                Log.d("crop","inside");
                if(data==null){
                    //camera
                    IMAGE_URI = Uri.fromFile(imageF);
                    Crop(Uri.fromFile(imageF));
                }
                else{
                    //gallary
                    IMAGE_URI = imageUri;
                    Crop(imageUri);
                }
            }
        }
    }

    public void Crop(Uri picuri){
        try{
            final File root = new File(android.os.Environment.
                    getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "NittCart_cropped" + File.separator);
            root.mkdirs();
            final String fname = "NittCart"+System.currentTimeMillis()+".jpeg";
            final File sdImageMainDirectory = new File(root, fname);
            outputFileUri2 = Uri.fromFile(sdImageMainDirectory);
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri2);
            cropIntent.setDataAndType(picuri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 500);
            cropIntent.putExtra("outputY", 500);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, CROP_REQUEST_CODE);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    void requesreadtpermission(){
        requestPermissions(perms,100);
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){
        switch(permsRequestCode){
            case 100:
                read_permission = grantResults[1]==PackageManager.PERMISSION_GRANTED;
                write_permission = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                break;
        }
    }

    public MultipartBody.Part get_upload_File(){
        if(IMAGE_URI == null)return null;
        String filePath = getRealPathFromURIPath(IMAGE_URI, postadd.this);
        File file = new File(filePath);
        Log.d("PartInside", "Filename " + file.getName());
        RequestBody mFile = RequestBody.create(MediaType.parse("file"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), mFile);
        //TypedFile photoTypedFile = new TypedFile("image/*", photoFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        return fileToUpload;
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            REAL_PATH = cursor.getString(idx);
            return cursor.getString(idx);
        }
    }
}