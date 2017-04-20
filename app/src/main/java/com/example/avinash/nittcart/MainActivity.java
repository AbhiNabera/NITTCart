package com.example.avinash.nittcart;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.avinash.nittcart.display_item_files.FoldingCellActivity;
import com.example.avinash.nittcart.display_item_files.GridFoldingCellAdapter;
import com.example.avinash.nittcart.display_item_files.Item;
import com.example.avinash.nittcart.display_item_files.ViewPagerAdapter;
import com.example.avinash.nittcart.display_item_files.about_us;
import com.example.avinash.nittcart.display_item_files.user_profile;
import com.example.avinash.nittcart.firebase_notif.Firebase_notification;
import com.example.avinash.nittcart.login.LoginActivity;
import com.example.avinash.nittcart.login.TokenDatabase;
import com.example.avinash.nittcart.response_classes.Item_list_response;
import com.example.avinash.nittcart.response_classes.Item_structure;
import com.example.avinash.nittcart.response_classes.dashboard_Item_structure;
import com.example.avinash.nittcart.retrofit_api_calls.ApiCalls;
import com.example.avinash.nittcart.retrofit_api_calls.ApiObservable;
import com.example.avinash.nittcart.retrofit_api_calls.MainActivityCalls;
import com.example.avinash.nittcart.tab_and_bottombar.Cart;
import com.example.avinash.nittcart.tab_and_bottombar.dashboard_adpter;
import com.example.avinash.nittcart.tab_and_bottombar.filter;
import com.example.avinash.nittcart.tab_and_bottombar.my_order;
import com.example.avinash.nittcart.tab_and_bottombar.postadd;
import com.example.avinash.nittcart.tab_and_bottombar.dashboard;
import com.example.avinash.nittcart.waveview.WaveView;
import com.example.avinash.nittcart.waveview.Waveloading;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.github.ybq.android.spinkit.style.Pulse;
import com.github.ybq.android.spinkit.style.RotatingCircle;
import com.github.ybq.android.spinkit.style.RotatingPlane;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;
import com.like.LikeButton;
import com.like.OnLikeListener;
//import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.readystatesoftware.viewbadger.BadgeView;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import br.com.mauker.materialsearchview.MaterialSearchView;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import pl.droidsonroids.gif.GifTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConnectionListener , ApiCalls{

    public static List<Item_structure> ItemList = Collections.emptyList();
    public static List<Item_structure> MAIN_ITEM_LIST = Collections.emptyList();
    public ArrayList<String> arr;
    private static MaterialSearchView searchView;
    public static int DASH_LIST_SIZE=0;
    public static GridLayoutManager lLayout;
    public static RelativeLayout coordinatorLayout;
    public static TabLayout tabHost;
    private Resources res;
    public static RecyclerView recyclerView;
    public static Toolbar toolbar;
    public static String layoutmode = "GRID_LAYOUT";
    public static boolean isInternetConnected = false ;
    public ConnectivityChangeReceiver connectivityChangeReceiver;
    public static ConnectivityManager cm;
    private static Snackbar snackbar;
    public static TextView reload;
    public static ProgressBar reloadprogress;
    public static Boolean start = true;
    public static TextView loadingtext;
    public static HashMap<String,String> CURRENT_MAP;
    private static String NAME;
    private static String QUALITY;
    private static String ITEM_TYPE;
    private static String PRICE;
    private static Boolean search_flag = false;

    private Subscription searchResultsSubscription;
    private ApiObservable apiObservable;

    public RelativeLayout noitems;
    public RecyclerView dashboard_recycler;
    public static SwipeRefreshLayout swipeRefreshLayout;
    public BoomMenuButton bmb;
    public NestedScrollView myScrollingContent;
    public static ProgressBar loadmore;
    private static MainActivityCalls myCall;
    public WaveView waveView ;
    public RelativeLayout hide_layout;
    public TextView student_rollo;
    private ViewPager viewPager;
    private DrawerLayout drawer;
    private Button retry;
    private TextView roll_no_user;
    private static View cart,dash,notf;
    private TextView my_orders;

    private int i=0,delay=200;
    private Runnable runnable = null;
    private Handler handler=null;

    private static String AUTH_TOKEN = "";
    private static String USER_ID = "";
    private static String ROLL_NO = "";
    private static int DASH_COUNT = 0;
    private static int CART_COUNT = 0;
    private static int NOTIFICATION_COUNT = 0;
    TokenDatabase token;


    public static FoldingCellActivity folding_cell;
    Random r;

    @Override
    public void onDisconnected() {
        Log.d("disconnected","fff");
        createSnackbar();
        if(!snackbar.isShown())snackbar.show();
        isInternetConnected = false;
    }

    @Override
    public void onConnected() {
        Log.d("connected", "fff");
        if(snackbar!=null)snackbar.dismiss();
        isInternetConnected = true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setupWindowAnimations();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        r = new Random(System.nanoTime());
        bmb = (BoomMenuButton) findViewById(R.id.bmb);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_4_2);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_4_2);
        setBoomButton();

        new SimpleTooltip.Builder(this)
                .anchorView(bmb)
                .text("Click on image to view complete details!")
                .gravity(Gravity.END)
                .animated(true)
                .transparentOverlay(false)
                .build()
                .show();

        //TODO: send firebase_user_id to server
        bmb.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(final int index, final BoomButton boomButton) {
                Log.d("boom", "" + index);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onBoomSelected(index,boomButton);
                    }
                }, 00);
            }

            @Override
            public void onBackgroundClick() {

            }

            @Override
            public void onBoomWillHide() {

            }

            @Override
            public void onBoomDidHide() {

            }

            @Override
            public void onBoomWillShow() {

            }

            @Override
            public void onBoomDidShow() {

            }
        });

        layoutmode = "LIST_LAYOUT";


        recyclerView =(RecyclerView)findViewById(R.id.recycler_view);
        waveView = (WaveView) findViewById(R.id.waveview);

        apiObservable = new ApiObservable();
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        ///searchView.adjustTintAlpha(0.8f);
        arr = new ArrayList<>();
        res = this.getResources();


        viewPager = (ViewPager)findViewById(R.id.view_pager);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        viewPager.setVisibility(View.GONE);

        tabHost = (TabLayout) this.findViewById(R.id.tabHost);
        AppBarLayout.LayoutParams params2= new AppBarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        params2.height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
        tabHost.setLayoutParams(params2);
        coordinatorLayout = (RelativeLayout)findViewById(R.id.boom_main_layout);
        settab();
        cart = tabHost.getTabAt(2).getCustomView();
        notf = tabHost.getTabAt(3).getCustomView();
        dash = tabHost.getTabAt(4).getCustomView();

        if(getAuthToken()==null){
            Snackbar.make(coordinatorLayout, "Not logged_in. Login again!!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        //updateBadgeCount(tabHost.getTabAt(2).getCustomView(), CART_COUNT);
        //updateBadgeCount(tabHost.getTabAt(3).getCustomView(), NOTIFICATION_COUNT);
        //updateBadgeCount(tabHost.getTabAt(4).getCustomView(), DASH_COUNT);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                NAME = query;
                Log.d("Submit:", searchView.getCurrentQuery());
                CURRENT_MAP.put("name", searchView.getCurrentQuery());
                Log.d("Submit_map:", CURRENT_MAP.get("name"));
                //searchItems(CURRENT_MAP, searchView);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("query", searchView.getCurrentQuery());
                NAME = searchView.getCurrentQuery();
                CURRENT_MAP.put("name", searchView.getCurrentQuery());
                if(!newText.matches("")){
                    search_flag = true;
                    searchItems(CURRENT_MAP,searchView);}
                return false;
            }

        });

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String suggestion="";
                if(searchView.getSuggestionAtPosition(position).charAt(0)==' ') {
                    suggestion = searchView.getSuggestionAtPosition(position).substring(1);
                }else{
                    suggestion = searchView.getSuggestionAtPosition(position);
                }
                searchView.setQuery(suggestion,false);
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
            // TODO set the adapter by calling the server

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setUpWindow(MainActivity.this);
            }
        }, 500);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setEnabled(true);
                //TODO refreshed items here
                ItemList = Collections.emptyList();
                loadItems();
                //refreshItems();
            }
        });

        swipeRefreshLayout.setVisibility(View.VISIBLE);

        loadmore = (ProgressBar) findViewById(R.id.load_scrollend);
        loadmore.setVisibility(View.GONE);

        myScrollingContent = (NestedScrollView) findViewById(R.id.scrolltotop);
        myScrollingContent.setVisibility(View.VISIBLE);
        myScrollingContent.setSmoothScrollingEnabled(false);

        myCall = new MainActivityCalls(MainActivity.this,swipeRefreshLayout ,
                loadmore , waveView, hide_layout,reloadprogress);//for interface

        final Animation makeInAnimation = AnimationUtils.makeInAnimation(getBaseContext(), false);
        makeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
                bmb.setVisibility(View.VISIBLE);
            }
        });

        final Animation makeOutAnimation = AnimationUtils.makeOutAnimation(getBaseContext(), true);
        makeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                bmb.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });


        //remove it once iten list is not empty
        myScrollingContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        myScrollingContent.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                View view = (View) myScrollingContent.getChildAt(myScrollingContent.getChildCount() - 1);
                int diff = (view.getBottom() - (myScrollingContent.getHeight() + myScrollingContent.getScrollY()));
                // if diff is zero, then the bottom has been reached
                /*if (bmb.isShown()) {
                    bmb.startAnimation(makeOutAnimation);
                    if (diff < 0) {

                    }
                }
                if (diff > 0) {
                    if (!bmb.isShown()) {
                        bmb.startAnimation(makeInAnimation);
                    }
                }*/
                if (diff == 0) {
                    if (!loadmore.isShown()) {
                        //loadmore.setVisibility(View.VISIBLE);
                        //myScrollingContent.fullScroll(View.FOCUS_DOWN);
                        //loaditems();
                    }
                }
            }
        });



        delay=200;

        final Handler handler2 = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                first_loaditems();
            }
        }, 700);

        /*hide_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Firebase_notification n = new Firebase_notification();
                String[] data = {"dyV7k3EcKlY:APA91bFzrqeAJMT2HEAJ_m5JGqe5vw72zf-iRZ2L7tt_cJq7mcvfKvCw2qE" +
                        "qYxabIiAS6EvRaH9AIgA02bgpJmMfHu0yGKBFdHUBSVzNkZpAYhe75qG31cEJlNnHSTb3_P_LeEc5ZDmc"};
                JSONArray json = new JSONArray(Arrays.asList(data));
                n.sendMessage(json,"NITTCart_user","notificationtest","bjnj","notification_body");
                return false;
            }
        });*/
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("TOKEN_FIREBASE", "Refreshed token: " + refreshedToken);

    }

    ProgressBar chooseProgress(ProgressBar progressBar){
        int Low = 1;
        int High = 12;
        final int Result = r.nextInt(High-Low) + Low;
        Log.d("res", Result + "");
        switch (Result){
            case 1:
                Wave wave = new Wave();
                wave.setColor(Color.parseColor("#5e98ba"));
                progressBar.setIndeterminateDrawable(wave);
                break;
            case 2:
                CubeGrid c = new CubeGrid();
                c.setColor(Color.parseColor("#5e98ba"));
                progressBar.setIndeterminateDrawable(c);
                break;

            case 3:
                Wave wav = new Wave();
                wav.setColor(Color.parseColor("#5e98ba"));
                progressBar.setIndeterminateDrawable(wav);
                break;
            case 4:
                Pulse p = new Pulse();
                p.setColor(Color.parseColor("#5e98ba"));
                progressBar.setIndeterminateDrawable(p);
                break;
            case 5:
                ThreeBounce tb = new ThreeBounce();
                tb.setColor(Color.parseColor("#5e98ba"));
                progressBar.setIndeterminateDrawable(tb);
                break;

            case 6:
                ChasingDots cd = new ChasingDots();
                cd.setColor(Color.parseColor("#5e98ba"));
                progressBar.setIndeterminateDrawable(cd);
                break;

            case 7:
                Pulse p1 = new Pulse();
                p1.setColor(Color.parseColor("#5e98ba"));
                progressBar.setIndeterminateDrawable(p1);
                break;

            case 8:
                Wave wa = new Wave();
                wa.setColor(Color.parseColor("#5e98ba"));
                progressBar.setIndeterminateDrawable(wa);
                break;

            case 9:
                DoubleBounce db = new DoubleBounce();
                db.setColor(Color.parseColor("#5e98ba"));
                progressBar.setIndeterminateDrawable(db);
                break;

            case 10:
                FadingCircle fdc = new FadingCircle();
                fdc.setColor(Color.parseColor("#5e98ba"));
                progressBar.setIndeterminateDrawable(fdc);
                break;

            case 11:
                CubeGrid c1 = new CubeGrid();
                c1.setColor(Color.parseColor("#5e98ba"));
                progressBar.setIndeterminateDrawable(c1);
                break;

            case 12:RotatingCircle rc = new RotatingCircle();
                rc.setColor(Color.parseColor("#5e98ba"));
                progressBar.setIndeterminateDrawable(rc);
                break;
        }
        return progressBar;
    }

    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.END);
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);

        Slide slide2 = new Slide();
        slide.setDuration(1000);
        slide2.setSlideEdge(Gravity.END);
        //getWindow().setEnterTransition(slide2);
    }

    public boolean checkNetwork(){
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return isInternetConnected = activeNetwork != null;
    }

    public void first_loaditems(){
          {
              CURRENT_MAP.put("user_id",USER_ID);
              CURRENT_MAP.put("auth_token", AUTH_TOKEN);
              CURRENT_MAP.put("name","");
              CURRENT_MAP.put("quality","GOOD");
              CURRENT_MAP.put("item_type", "OTHER");
              folding_cell.first_loaditems(USER_ID, AUTH_TOKEN, "", "OTHER", "LH", "GOOD");
            Log.d("else", CURRENT_MAP.size()+"");
        }
    }

    public void loadItems(){
        folding_cell.loaditems(CURRENT_MAP);
        Log.d("else", "me");
    }

    void defaultmap(){
        CURRENT_MAP = new HashMap<>();
        NAME="";
        QUALITY="GOOD";
        ITEM_TYPE = "OTHER";
        PRICE = "LH";
        CURRENT_MAP.put("user_id",USER_ID);
        CURRENT_MAP.put("auth_token", AUTH_TOKEN);
        CURRENT_MAP.put("name","");
        CURRENT_MAP.put("price","LH");
        CURRENT_MAP.put("quality", "GOOD");
        CURRENT_MAP.put("item_type", "OTHER");

    }

    public void refreshItems(){
        myCall.RefreshItems();
    }

    public void searchItems(HashMap<String,String> map,MaterialSearchView searchView){
        folding_cell.Searchitems(map,searchView);
    }


    public void updateBadgeCount(View view,int count){
        BadgeView badge = new BadgeView(MainActivity.this, view);
        badge.setText(""+count);
        badge.setBadgeBackgroundColor(Color.parseColor("#dabb1f"));
        badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badge.setBadgeMargin((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                7, getResources().getDisplayMetrics()));
        badge.setTextSize(12);
        badge.show();
    }

    public void setUpWindow(Context context) {
        lLayout = new GridLayoutManager(context, 1);
         folding_cell =  new FoldingCellActivity(recyclerView
        ,new LinearLayoutManager(getApplicationContext()),context,lLayout,USER_ID,AUTH_TOKEN);
    }

    public void setBoomButton(){
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder();
            if(i == 0) {
                builder.normalImageRes(R.drawable.filter);
                builder.normalText("Filter");
            }
            else if(i == 1) {
                builder.normalImageRes(R.drawable.sort_asc);
                builder.normalText("Sort Ascending");
            }
            else if(i == 2) {
                builder.normalImageRes(R.drawable.sort_desc);
                builder.normalText("Sort Descending");
            }
            else if(i == 3) {
                builder.normalImageRes(R.drawable.ic_action_clear);
                builder.normalText("Clear Filters");
            }
            builder.typeface(Typeface.DEFAULT_BOLD)
                    .textGravity(Gravity.CENTER)
                    .rippleEffect(true);
            bmb.addBuilder(builder);
        }
    }

    public void onBoomSelected(int index,BoomButton boomButton) {
        if (index == 0) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), filter.class);
                    intent.putExtra("user_id", USER_ID);
                    intent.putExtra("auth_token", AUTH_TOKEN);
                    Fade slide = new Fade();
                    slide.setDuration(700);
                    getWindow().setExitTransition(slide);
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                    MainActivity.this.startActivityForResult(intent, 200, bundle);
                }
            }, 700);
        }
        if (index == 1) {
            PRICE = "LH";
            CURRENT_MAP.put("price", PRICE);
            loadItems();
           //TODO load item with map
        }
        if (index == 2) {
            PRICE = "HL";
            CURRENT_MAP.put("price",PRICE);
            loadItems();
           //
        }
        if (index == 3) {
            defaultmap();
           new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadItems();
                }
            }, 700);

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (searchView.isOpen()) searchView.closeSearch();

        else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        if(search_flag){
            search_flag = false;
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d("start", "inside");
        if(!checkNetwork()){
            createSnackbar();
            snackbar.show();
        }

        if(getAuthToken()==null){
            Snackbar.make(coordinatorLayout, "Not logged_in. Login again!!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        defaultmap();
        //setupDashboard();
    }

    @Override
    protected void onPause() {
        super.onPause();
        connectivityChangeReceiver.flag=false;
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(connectivityChangeReceiver);
    }

    @Override
    public void onStop(){
        super.onStop();

    }

    String getAuthToken(){
        token = new TokenDatabase(MainActivity.this);
        Cursor cr = token.getToken(0);
        cr.moveToFirst();
        Log.d("count", "" + cr.getCount());
        if(cr.getCount() !=0){
            AUTH_TOKEN = cr.getString(cr.getColumnIndex("auth_token"));
            USER_ID = cr.getString(cr.getColumnIndex("user_id"));
            CART_COUNT = cr.getInt(cr.getColumnIndex("cart_count"));
            DASH_COUNT = cr.getInt(cr.getColumnIndex("dashboard_count"));
            NOTIFICATION_COUNT = cr.getInt(cr.getColumnIndex("notification_count"));
            Log.d("auth_token",AUTH_TOKEN);
            Log.d("user_id", USER_ID);
            //updateBadgeCount(cart, CART_COUNT);
            //updateBadgeCount(notf, NOTIFICATION_COUNT);
            //updateBadgeCount(dash, DASH_COUNT);
            return AUTH_TOKEN;
        }
        return null;
    }

    public LinearLayout customtabView(int drawableid,String labeltext,int position){

        final LikeButton button = new LikeButton(this);
        button.setIconSizeDp(35);
        button.setLikeDrawableRes(drawableid);
        button.setUnlikeDrawableRes(drawableid);
        button.setLiked(true);
        button.setCircleStartColorRes(R.color.boom);
        button.setCircleEndColorRes(R.color.btnRequest);
        button.setExplodingDotColorsRes(R.color.btnRequest, R.color.boom);
        buttonclickevents(button, position);

        button.setAnimationScaleFactor(2);
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        l.addView(button);
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        l.setLayoutParams(params);
        return l;

    }

    public void buttonclickevents(final LikeButton button,int position){
        switch(position){
            case 0:
                final LikeButton button0 = button;
                button0.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    Log.d("click", "likebutton");
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    Log.d("click0","unlikebutton");
                    button0.setLiked(true);
                    NestedScrollView s= (NestedScrollView)findViewById(R.id.scrolltotop);
                    s.smoothScrollTo(0,0);
                    //recyclerView.smoothScrollToPosition(0);
                }
            });

                break;

            case 1:
                final LikeButton button1 = button;
                button1.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {
                        Log.d("click1","likebutton");
                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {
                        Log.d("click1", "unlikebutton");
                        button1.setLiked(true);
                        Intent intent = new Intent(getApplicationContext(),postadd.class);
                        intent.putExtra("user_id", USER_ID);
                        intent.putExtra("auth_token", AUTH_TOKEN);
                        Fade slide = new Fade();
                        slide.setDuration(700);
                        getWindow().setExitTransition(slide);
                        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                        MainActivity.this.startActivity(intent, bundle);
                        //startActivity(intent);
                        //overridePendingTransition(R.anim.up_from_bottom, R.anim.activity_no_slide_main);
                    }
                });

                break;

            case 2:
                final LikeButton button2 = button;
                button2.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {
                        Log.d("click","likebutton");
                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {
                        Log.d("click2","unlikebutton");
                        button2.setLiked(true);
                        Intent intent = new Intent(getApplicationContext(), Cart.class);
                        intent.putExtra("user_id", USER_ID);
                        intent.putExtra("auth_token", AUTH_TOKEN);
                        Fade slide = new Fade();
                        slide.setDuration(700);
                        getWindow().setExitTransition(slide);
                        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                        MainActivity.this.startActivity(intent, bundle);
                        //startActivity(intent);
                        //overridePendingTransition(R.anim.up_from_bottom, R.anim.activity_no_slide_main);

                    }
                });

                break;

            case 3:
                final LikeButton button3 = button;
                button3.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {
                        Log.d("click","likebutton");
                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {
                        Log.d("click3","unlikebutton");
                        button3.setLiked(true);
                        Intent intent = new Intent(getApplicationContext(), my_order.class);
                        intent.putExtra("user_id", USER_ID);
                        intent.putExtra("auth_token", AUTH_TOKEN);
                        Fade slide = new Fade();
                        slide.setDuration(700);
                        getWindow().setExitTransition(slide);
                        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                        MainActivity.this.startActivity(intent, bundle);
                    }
                });

                break;

            case 4:
                final LikeButton button4 = button;
                button4.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {
                        Log.d("click","likebutton");
                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {
                        Log.d("click4","unlikebutton");
                        button4.setLiked(true);
                        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        //drawer.openDrawer(GravityCompat.END);
                        Intent intent = new Intent(getApplicationContext(), dashboard.class);
                        intent.putExtra("user_id", USER_ID);
                        intent.putExtra("auth_token", AUTH_TOKEN);
                        Fade slide = new Fade();
                        slide.setDuration(700);
                        getWindow().setExitTransition(slide);
                        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                        MainActivity.this.startActivity(intent, bundle);
                    }
                });

                break;
        }
    }

    public void settab(){
        {
            Log.d("settab", "inside");
            tabHost.addTab(
                    tabHost.newTab()
                            .setCustomView(customtabView(R.drawable.house,"Home",0))
            );
            tabHost.addTab(
                    tabHost.newTab()
                            .setCustomView(customtabView(R.drawable.camera, "Postadd",1))
            );
            tabHost.addTab(
                    tabHost.newTab()
                            .setCustomView(customtabView(R.drawable.cart, "Cart",2))
            );
            tabHost.addTab(
                    tabHost.newTab()
                            .setCustomView(customtabView(R.drawable.notifications, "Notificati..",3))
            );
            tabHost.addTab(
                    tabHost.newTab()
                            .setCustomView(customtabView(R.drawable.dashboard, "Dashboard",4))
            );
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200 && resultCode == RESULT_OK && data != null) {

            //NAME = "";
            //QUALITY = "GOOD";
            //ITEM_TYPE = "OTHER";
            //PRICE = "LH";
            defaultmap();
            Log.d("result act", MAIN_ITEM_LIST.size() + "");
            folding_cell.set_new_List_Adapter(MAIN_ITEM_LIST);

            if (data.getStringExtra("quality") == null && data.getStringExtra("item_type") == null) {

            }
            else{
                if(data.getStringExtra("quality")!=null)
                {
                    Log.d("quality", data.getStringExtra("quality"));
                    QUALITY = data.getStringExtra("quality").toString();
                    CURRENT_MAP.put("quality", QUALITY);
                }

                if(data.getStringExtra("item_type")!=null) {
                    Log.d("type", data.getStringExtra("item_type"));
                    ITEM_TYPE = data.getStringExtra("item_type").toString();
                    CURRENT_MAP.put("item_type", ITEM_TYPE);
                }
                Log.d("load", "inside");
                loadItems();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        connectivityChangeReceiver.flag=true;
        LocalBroadcastManager.getInstance(this).registerReceiver(
                connectivityChangeReceiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (searchResultsSubscription != null && !searchResultsSubscription.isUnsubscribed())
            searchResultsSubscription.unsubscribe();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.item_search) {
            searchView.setVisibility(View.VISIBLE);
            searchView.openSearch();
            return true;
        }
        if (id == R.id.action_signIn) {
            token.drop();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            return true;
        }

        if(id == R.id.action_logout){
            //TODO logout
            apiObservable = new ApiObservable();
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Logging out...");
            //progressDialog.setCancelable(false);
            progressDialog.show();
            Call<JsonObject> logout = apiObservable.logout(AUTH_TOKEN,USER_ID);
            logout.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        Log.d("resp", response.body().toString());
                        JsonObject object = response.body().getAsJsonObject();
                        if (object.get("status_code").getAsInt() == 200) {
                            token.drop();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            //finishAfterTransition();
                            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                        }
                        else{progressDialog.dismiss();
                            Snackbar.make(coordinatorLayout, "Error Occured", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressDialog.dismiss();
                    Snackbar.make(coordinatorLayout, "Network error", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        if (id == R.id.action_list_to_grid) {

            if (!((Animatable) item.getIcon()).isRunning()) {
                if (lLayout.getSpanCount() == 1) {
                    layoutmode = "GRID_LAYOUT";
                    item.setIcon(AnimatedVectorDrawableCompat.create(MainActivity.this, R.drawable.avd_list_to_grid));
                    lLayout.setSpanCount(2);
                } else {
                    layoutmode = "LIST_LAYOUT";
                    item.setIcon(AnimatedVectorDrawableCompat.create(MainActivity.this, R.drawable.avd_grid_to_list));
                    lLayout.setSpanCount(1);
                }
                ((Animatable) item.getIcon()).start();
                Log.d("MAin item list",MAIN_ITEM_LIST.size()+"");
                //folding_cell.set_new_List_Adapter(MAIN_ITEM_LIST);
                FoldingCellActivity.mAdapter.notifyItemRangeChanged(0,
                        FoldingCellActivity.mAdapter.getItemCount());

            }
                return true;
            }


            return super.onOptionsItemSelected(item);
        }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_about_us) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), about_us.class);
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                    MainActivity.this.startActivity(intent, bundle);

                }
            }, 200);

        } else if (id == R.id.nav_contact_us) {final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), user_profile.class);
                    intent.putExtra("user_id", USER_ID);
                    intent.putExtra("auth_token", AUTH_TOKEN);
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                    MainActivity.this.startActivity(intent, bundle);

                }
            }, 200);

        } else if (id == R.id.nav_t_and_c) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), user_profile.class);
                    intent.putExtra("user_id", USER_ID);
                    intent.putExtra("auth_token", AUTH_TOKEN);
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                    MainActivity.this.startActivity(intent, bundle);

                }
            }, 200);

        } else if (id == R.id.nav_profile) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), user_profile.class);
                    intent.putExtra("user_id", USER_ID);
                    intent.putExtra("auth_token", AUTH_TOKEN);
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                    MainActivity.this.startActivity(intent, bundle);

                }
            }, 200);

        } else if (id == R.id.nav_notification) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), user_profile.class);
                    intent.putExtra("user_id", USER_ID);
                    intent.putExtra("auth_token", AUTH_TOKEN);
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                    //MainActivity.this.startActivity(intent, bundle);

                }
            }, 200);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }



    public void createSnackbar(){
        snackbar = Snackbar
                .make(coordinatorLayout, "No Internet Connection !!", Snackbar.LENGTH_INDEFINITE)
                .setAction("", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        {
                            if (checkNetwork()) {
                                isInternetConnected = true;
                            } else {
                                if(!isInternetConnected)
                                isInternetConnected = false;
                                createSnackbar();
                                snackbar.show();
                            }
                        }
                    }
                });

    }


    @Override
    public void loaditems(String user_id, String auth_token, String name, String subject, String price, String quality) {

    }

    @Override
    public void first_loaditems(String user_id, String auth_token, String name, String subject, String price, String quality) {

    }

    @Override
    public void RefreshItems() {

    }

    @Override
    public void Searchitems(String query) {

    }

    @Override
    public void onErrorLoading() {
        Log.d("error","vvhjbk");
        if(start){
            Log.d("start", "vvhjbk");
            swipeRefreshLayout.setRefreshing(false);
            loadmore.setVisibility(View.GONE);
            start = false;
            //loadingtext.setVisibility(View.GONE);
            myCall.hidewaveview();
        }
        else{
            reloadprogress.setVisibility(View.GONE);
            reload.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCompleteLoading(Item_list_response list_response, Context context) {
        Log.d("complete", "vvhjbk");

        swipeRefreshLayout.setVisibility(View.VISIBLE);
        folding_cell.set_new_List_Adapter(list_response);
    }

}
