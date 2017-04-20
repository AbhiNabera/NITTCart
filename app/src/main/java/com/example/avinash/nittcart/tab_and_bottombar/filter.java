package com.example.avinash.nittcart.tab_and_bottombar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.avinash.nittcart.MainActivity;
import com.example.avinash.nittcart.R;
import com.example.avinash.nittcart.response_classes.Item_list_response;
import com.example.avinash.nittcart.retrofit_api_calls.ApiObservable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.tankery.lib.circularseekbar.CircularSeekBar;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by AVINASH on 3/31/2017.
 */
public class filter extends AppCompatActivity {

    @Bind(R.id.closefilter)
    ImageButton closefilter;
    @Bind(R.id.category)
    LinearLayout category;
    @Bind(R.id.item_quality)
    LinearLayout quality;
    @Bind(R.id.item_price)
    RelativeLayout price;

    @Bind(R.id.cat_button)
    ImageView cat_button;
    @Bind(R.id.quality_button)
    ImageView quality_button;
    @Bind(R.id.price_button)
    ImageView price_button;
    @Bind(R.id.apply)
    Button apply;

    @Bind(R.id.donate)
    RadioButton donate;
    @Bind(R.id.chemistry)
    CheckBox check_chemistry;
    @Bind(R.id.physics)
    CheckBox check_physics;
    @Bind(R.id.maths)
    CheckBox check_maths;
    @Bind(R.id.english)
    CheckBox check_english;
    @Bind(R.id.cse)
    CheckBox check_cse;
    @Bind(R.id.civil)
    CheckBox check_civil;
    @Bind(R.id.chemical)
    CheckBox check_chemical;
    @Bind(R.id.eee)
    CheckBox check_eee;
    @Bind(R.id.ece)
    CheckBox check_ece;
    @Bind(R.id.ice)
    CheckBox check_ice;
    @Bind(R.id.mech)
    CheckBox check_mech;
    @Bind(R.id.mme)
    CheckBox check_mme;
    @Bind(R.id.prod)
    CheckBox check_prod;
    @Bind(R.id.others)
    CheckBox check_others;

    @Bind(R.id.good_as_new)
    CheckBox check_good_as_new;
    @Bind(R.id.used)
    CheckBox check_used;
    @Bind(R.id.old)
    CheckBox check_old;
    @Bind(R.id.seek_bar)
    CircularSeekBar price_seekbar;
    @Bind(R.id.price_seekbar)
    TextView pricetext;

    int checkstate=0;
    String item_type;
    String item_quality;
    String item_price;
    Boolean item_donated = false;
    private Subscription searchResultsSubscription;
    private ApiObservable apiObservable;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setupWindowAnimations();
        setContentView(R.layout.filter);
        ButterKnife.bind(this);

        apiObservable = new ApiObservable();
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO implement filter call to server here
                filter();
            }
        });


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Applying Filters...");
        donate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (checkstate == 0) {
                    donate.setChecked(true);
                    item_donated = true;
                    checkstate = 1;
                } else {
                    donate.setChecked(false);
                    item_donated = false;
                    checkstate = 0;
                }
            }
        });

        price_seekbar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                int price2 = (int) (1000 * progress / 100);
                pricetext.setText("" + price2);
                item_price = "" + price2;
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });

        closefilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAfterTransition();
                //overridePendingTransition(R.anim.zoom_out, R.anim.activity_no_slide_tabitem);
            }
        });

        cat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category.setVisibility(View.VISIBLE);
                quality.setVisibility(View.GONE);
                price.setVisibility(View.GONE);
            }
        });

        quality_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category.setVisibility(View.GONE);
                quality.setVisibility(View.VISIBLE);
                price.setVisibility(View.GONE);
            }
        });

        price_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category.setVisibility(View.GONE);
                quality.setVisibility(View.GONE);
                price.setVisibility(View.VISIBLE);
            }
        });

        check_chemistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_category_item_type(check_chemistry);
                item_type="CH";
            }
        });

        check_physics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_category_item_type(check_physics);
                item_type="PH";
            }
        });
        check_maths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_category_item_type(check_maths);
                item_type="MATH";
            }
        });
        check_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_category_item_type(check_english);
                item_type="ENG";
            }
        });
        check_cse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_category_item_type(check_cse);
                item_type="CSE";
            }
        });
        check_civil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_category_item_type(check_civil);
                item_type="CIVIL";
            }
        });
        check_chemical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_category_item_type(check_chemical);
                item_type="CHEM";
            }
        });
        check_eee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_category_item_type(check_eee);
                item_type="EEE";
            }
        });
        check_ece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_category_item_type(check_ece);
                item_type="ECE";
            }
        });
        check_ice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_category_item_type(check_ice);
                item_type="ICE";
            }
        });
        check_mech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_category_item_type(check_mech);
                item_type="MECH";
            }
        });

        check_mme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_category_item_type(check_mme);
                item_type="MME";
            }
        });
        check_prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_category_item_type(check_prod);
                item_type="PROD";
            }
        });
        check_others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_category_item_type(check_others);
                item_type="OTHER";
            }
        });

        check_good_as_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_used.setChecked(false);
                check_old.setChecked(false);
                item_quality = "GOOD";
            }
        });

        check_used.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_good_as_new.setChecked(false);
                check_old.setChecked(false);
                item_quality = "MEDIUM";
            }
        });

        check_old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_good_as_new.setChecked(false);
                check_used.setChecked(false);
                item_quality = "POOR";
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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        //overridePendingTransition(R.anim.zoom_out, R.anim.activity_no_slide_tabitem);
        finishAfterTransition();
    }

    public void filter(){
        HashMap<String,String> map = new HashMap<>();
        map.put("name","");
        map.put("price","LH");
        map.put("quality",item_quality);
        map.put("user_id", getIntent().getStringExtra("user_id"));
        map.put("auth_token", getIntent().getStringExtra("auth_token"));
        //map is ready
        if(item_quality!=null&&item_type!=null){
            if(!item_type.matches("OTHER")){
                map.put("item_type","BOOK");
                map.put("subject",item_type);
            }else{
                map.put("item_type",item_type);
            }
            Log.d("quality", item_quality);
            Log.d("item_type", item_type);

            progressDialog.show();
            loaditems(map);
            }
        else{
            Snackbar.make(apply, "Choose category and quality!!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        Intent output = new Intent();
        output.putExtra("item_type", item_type);
        output.putExtra("quality", item_quality);
        setResult(RESULT_OK, output);
        //finishAfterTransition();

    }

    void check_category_item_type(CheckBox checkbox){
         Log.d("true",checkbox.getId()+":"+check_chemistry.getId());
         check_chemistry.setChecked(checkbox.getId() - check_chemistry.getId() == 0);
         check_physics.setChecked(checkbox.getId() - check_physics.getId() == 0);
         check_maths.setChecked(checkbox.getId() - check_maths.getId() == 0);
         check_english.setChecked(checkbox.getId() - check_english.getId() == 0);
         check_cse.setChecked(checkbox.getId() - check_cse.getId() == 0);
         check_civil.setChecked(checkbox.getId() - check_civil.getId() == 0);
         check_chemical.setChecked(checkbox.getId() - check_chemical.getId() == 0);
         check_eee.setChecked(checkbox.getId() - check_eee.getId() == 0);
         check_ece.setChecked(checkbox.getId() - check_ece.getId() == 0);
         check_ice.setChecked(checkbox.getId() - check_ice.getId() == 0);
         check_mech.setChecked(checkbox.getId() - check_mech.getId() == 0);
         check_mme.setChecked(checkbox.getId() - check_mme.getId() == 0);
         check_prod.setChecked(checkbox.getId() - check_prod.getId() == 0);
         check_others.setChecked(checkbox.getId() - check_others.getId() == 0);

    }


    public void loaditems(HashMap<String,String> item_map) {

        rx.Observable<Item_list_response> observable =
                apiObservable.loadItems(item_map);
        searchResultsSubscription = observable
                .debounce(250, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(load_items_Subscriber());
    }

    private Subscriber<Item_list_response> load_items_Subscriber() {
        return new Subscriber<Item_list_response>() {
            @Override
            public void onCompleted() {
                //set List size here

            }
            @Override
            public void onError(Throwable e) {

                progressDialog.dismiss();
                //swipeRefreshLayout.setRefreshing(false);
                Log.d("error_call", e.toString());
            }

            @Override
            public void onNext(Item_list_response item_list_response) {
                //swipeRefreshLayout.setRefreshing(false);
                progressDialog.dismiss();
                Log.d("respo", item_list_response.getStatusCode() + "");
                if(item_list_response.getStatusCode()==200){
                    Log.d("resp_size:",item_list_response.getData().size()+"");
                    MainActivity.MAIN_ITEM_LIST.clear();
                    MainActivity.MAIN_ITEM_LIST = item_list_response.getData();
                    //set_new_List_Adapter(item_list_response);
                    Intent output = new Intent();
                    //output.putExtra("item_type", item_type);
                    //output.putExtra("quality", item_quality);
                    setResult(RESULT_OK, output);
                    finishAfterTransition();
                }
            }
        };
    }

}
