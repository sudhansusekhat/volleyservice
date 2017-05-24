package com.GoCarDev;

import android.Manifest;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.GoCarDev.Buycredit.BuyCreditFragment;
import com.GoCarDev.FAQ.faq;
import com.GoCarDev.GoCarInfo.GoInfo;
import com.GoCarDev.Notification.notification;
import com.GoCarDev.adapter.NavDrawerListAdapter;
import com.GoCarDev.adapter.SpinnerListAdapter;
import com.GoCarDev.addcredit.ADDCreditFragment;
import com.GoCarDev.booking.Booking_fragment;
import com.GoCarDev.changepin.ChangePin;
import com.GoCarDev.journey.CurrentJourney;
import com.GoCarDev.login.Joinus;
import com.GoCarDev.login.Login;
import com.GoCarDev.login.SignUpForBusinessActivity;
import com.GoCarDev.model.CommonUtilities;
import com.GoCarDev.model.Dialog;
import com.GoCarDev.model.DrawerItem;
import com.GoCarDev.model.Geo_code;
import com.GoCarDev.model.LoginRespose;
import com.GoCarDev.model.ResponseModel;
import com.GoCarDev.model.VerifyMail;
import com.GoCarDev.myprofile.ProfileFragment;
import com.GoCarDev.mytrips.TripFragment;
import com.GoCarDev.mytrips.UpcomingTrip;
import com.GoCarDev.paymentsetting.MyCreditCards;
import com.GoCarDev.paymentsetting.PromoCode;
import com.GoCarDev.serverAccess.ServerAccess;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Dashboard_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public ListView mDrawerList;
    public NavDrawerListAdapter adapter;
    public static Dashboard_Activity act;
    public int selectedPos;
    public ArrayList<DrawerItem> nav_drawer_items = new ArrayList<>();
    LinearLayout navigation_header;
    RelativeLayout  accountlayout, layout_profcomp, mainlayout, layout_professional;
    boolean isClick = false;
    Dialog dialog;
    SpinnerListAdapter accntadapter;
    public static Context context;
    ImageView notificationimg, notifyimage;
    LinearLayout profile_complete, logindrawer, drawerman, credit_layout;
    String notifcount;
    String custid;
    public static Geo_code geocode;
    TextView txtaccount, txtmembershipprof, txtliscenceprofess, txtaccntpaidfull,txtliscenceprofess2,txtaddress,txtaddress2;
    ListView accountlist;
    public ImageView goImage;
    Gson gson = new Gson();
    LoginRespose loginRespose;
    LinearLayout creditlayout;
    int account_selected_position;
    String account_type;
    public static String curr_account_id;
    getIP getip;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = Dashboard_Activity.this;
        setContentView(R.layout.activity_dashboard_);

       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);*/

        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        navigation_header = (LinearLayout) findViewById(R.id.navigation_header);
        credit_layout = (LinearLayout) findViewById(R.id.credit_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        accountlist = (ListView) findViewById(R.id.listaccount);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        context = this;
        getip=new getIP(this);
        // notifyimage = (ImageView) findViewById(R.id.dotnotify);
        goImage = (ImageView) findViewById(R.id.goImage);
        txtaccount = (TextView) findViewById(R.id.txtaccount);
        txtmembershipprof = (TextView) findViewById(R.id.txtmembershipprof);
        txtliscenceprofess = (TextView) findViewById(R.id.txtliscenceprofess);
        txtliscenceprofess2 = (TextView) findViewById(R.id.txtliscenceprofess2);
        txtaddress = (TextView) findViewById(R.id.txtaddress);
        txtaddress2 = (TextView) findViewById(R.id.txtaddress2);
        txtaccntpaidfull = (TextView) findViewById(R.id.txtaccntpaidfull);
        layout_professional = (RelativeLayout) findViewById(R.id.layout_professional);
        TextView tvlorem_ipsum = (TextView) findViewById(R.id.tvlorem_ipsum);
        TextView custname = (TextView) findViewById(R.id.custname);
        TextView txtcreditrnd = (TextView) findViewById(R.id.txtcreditrnd);
        accountlayout = (RelativeLayout) findViewById(R.id.account_layout);
        notificationimg = (ImageView) findViewById(R.id.notificationimg);
        creditlayout = (LinearLayout) findViewById(R.id.creditlayout);
        layout_profcomp = (RelativeLayout) findViewById(R.id.layout_profcomp);
        profile_complete = (LinearLayout) findViewById(R.id.profile_complete);
        layout_profcomp = (RelativeLayout) findViewById(R.id.layout_profcomp);
        final LinearLayout profile = (LinearLayout) findViewById(R.id.profile_complete);
        final ImageView signupbtn = (ImageView) findViewById(R.id.btn_signup);
        final ImageView login = (ImageView) findViewById(R.id.btn_signin);
        logindrawer = (LinearLayout) findViewById(R.id.logindrawer);
        drawerman = (LinearLayout) findViewById(R.id.drawerman);

        notificationimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(act, notification.class);
                startActivity(i);
            }
        });

        creditlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < nav_drawer_items.size(); i++) {
                    if (nav_drawer_items.get(i).itemNumber == 3 || nav_drawer_items.get(i).itemNumber == 6) {
                        selectItem(3);
                        mDrawerList.setSelection(i);
                        mDrawerList.setItemChecked(i, true);
                    } else {
                        mDrawerList.setItemChecked(i, false);
                    }
                }
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        setSupportActionBar(toolbar);
        dialog = new Dialog(act);
        getSupportActionBar().setTitle(null);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (!TextUtils.isEmpty(CommonUtilities.getPreference(act, CommonUtilities.pref_customer_id))) {
                    if (CommonUtilities.isConnectingToInternet(act)) {
                        CommonUtilities.hideSoftKeyboard(Dashboard_Activity.this);
                        new MenuDetails("0").execute();
                    } else
                        CommonUtilities.alertdialog(act,
                                getString(R.string.val_internet));

                    if (!CommonUtilities.getBooleanPreference(act, "isVerified")) {

                        if (CommonUtilities.isConnectingToInternet(act)) {
                            new Verifymail("0").execute();
                        } else
                            CommonUtilities.alertdialog(act,
                                    getString(R.string.val_internet));

                        mDrawerList.setEnabled(false);
                        notificationimg.setEnabled(false);
                        profile_complete.setEnabled(false);
                        creditlayout.setEnabled(false);
                    }
                    else{
                        mDrawerList.setEnabled(true);
                        notificationimg.setEnabled(true);
                        profile_complete.setEnabled(true);
                        creditlayout.setEnabled(true);
                    }
                }
                //act.invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
               // act.invalidateOptionsMenu();

                layout_professional.setVisibility(View.GONE);
                accountlayout.setVisibility(View.GONE);
                layout_profcomp.setVisibility(View.GONE);

            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            navigation_header.setPadding(30, getStatusBarHeight(), 0, 0);
            int height = (int) getResources().getDimension(R.dimen.height406);
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, height);
            navigation_header.setLayoutParams(parms);
        }

        dialog = new Dialog(act);

        Intent i = act.getIntent();
        i.getFlags();

        custid = CommonUtilities.getPreference(act, CommonUtilities.key_customer_id);

        if (custid.equals("")) {
            logindrawer.setVisibility(View.VISIBLE);
            drawerman.setVisibility(View.GONE);
        }

        String[] accntarray = {"Personal account", "Professional account"};
        accntadapter = new SpinnerListAdapter(act, accntarray);
        accountlist.setAdapter(accntadapter);

        loginRespose = (LoginRespose) gson.fromJson(CommonUtilities.getPreference(act, CommonUtilities.pref_customer_detail), LoginRespose.class);
        if (loginRespose != null) {
            String profiletype = loginRespose.response.user_info.profile_type;
            if (profiletype.equals("1")) {
                account_selected_position = 0;
                txtaccount.setText("personal");
                credit_layout.setVisibility(View.VISIBLE);
                CommonUtilities.setPreference(act, CommonUtilities.pref_job_type, "Personal");

            } else if (profiletype.equals("2")) {
                account_selected_position = 1;
                txtaccount.setText("professional");
                credit_layout.setVisibility(View.GONE);
                CommonUtilities.setPreference(act, CommonUtilities.pref_job_type, "Professional");

            }
            custname.setText(loginRespose.response.user_info.first_name + " " + loginRespose.response.user_info.last_name);

            String global_profile_type = CommonUtilities.getPreference(act, CommonUtilities.pref_global_profile_type);
            if (global_profile_type.equals("1")) {
                txtaccount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.dropdown_icon, 0);
            } else
                txtaccount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            setDrawerItems();
        }

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.custom_dialogprof, drawer, false);
                drawer.addView(view);
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupintent = new Intent(Dashboard_Activity.this, SignUpForBusinessActivity.class);
                signupintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                signupintent.putExtra("fromBooking","fromBooking");
                startActivity(signupintent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(Dashboard_Activity.this, Login.class);
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                login.putExtra("fromBooking","fromBooking");
                startActivity(login);
            }
        });

        adapter = new NavDrawerListAdapter(act, nav_drawer_items, loginRespose);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        if (!checkLocationPermission()) {
            requestCameraPermission();
        } else
            getLocation();

        //permissionToDrawOverlays();

        if(loginRespose!=null)
        {
            SetData();
        }

        profile_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (account_selected_position == 0) {
                    if (layout_profcomp.getVisibility() == View.VISIBLE) {
                        layout_profcomp.setVisibility(View.GONE);
                    } else {
                        layout_profcomp.setVisibility(View.VISIBLE);
                        accountlayout.setVisibility(View.GONE);
                    }
                } else {
                    if (layout_professional.getVisibility() == View.VISIBLE) {
                        layout_professional.setVisibility(View.GONE);
                    } else {
                        layout_professional.setVisibility(View.VISIBLE);
                        accountlayout.setVisibility(View.GONE);
                    }
                }
            }
        });

        accountlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (position == 0) {
                        account_selected_position = 0;
                        txtaccount.setText("personal");
                        accountlayout.setVisibility(View.GONE);
                        credit_layout.setVisibility(View.VISIBLE);

                        if (!TextUtils.isEmpty(CommonUtilities.getPreference(act, CommonUtilities.pref_customer_personal_id)))
                            CommonUtilities.setPreference(act, CommonUtilities.pref_customer_id, CommonUtilities.getPreference(act, CommonUtilities.pref_customer_personal_id));
                        CommonUtilities.setPreference(act, CommonUtilities.pref_job_type, "Personal");
                        if (!CommonUtilities.getBooleanPreference(act, "isPersonalVerified")) {
                            mDrawerList.setEnabled(false);
                            new Verifymail("1").execute();
                        }

                        curr_account_id=CommonUtilities.getPreference(act, CommonUtilities.pref_customer_professional).toString();
                        account_type="1";
                        new switch_cust_acc().execute();

                    } else if (position == 1) {
                        account_selected_position = 1;
                        txtaccount.setText("professional");
                        accountlayout.setVisibility(View.GONE);
                        credit_layout.setVisibility(View.GONE);

                        if (!TextUtils.isEmpty(CommonUtilities.getPreference(act, CommonUtilities.pref_customer_professional)))
                            CommonUtilities.setPreference(act, CommonUtilities.pref_customer_id, CommonUtilities.getPreference(act, CommonUtilities.pref_customer_professional));
                        CommonUtilities.setPreference(act, CommonUtilities.pref_job_type, "Professional");
                        curr_account_id=CommonUtilities.getPreference(act, CommonUtilities.pref_customer_personal_id).toString();
                        account_type="2";
                        new switch_cust_acc().execute();
                    }
            }
        });
        mainlayout = (RelativeLayout) findViewById(R.id.mainlayout);

        CommonUtilities.setFontFamily(act, tvlorem_ipsum, CommonUtilities.GillSansStd);
        CommonUtilities.setFontFamily(act, custname, CommonUtilities.GillSansStd);
        CommonUtilities.setFontFamily(act, txtcreditrnd, CommonUtilities.GillSansStd);
        CommonUtilities.setFontFamily(act, txtaccount, CommonUtilities.GillSansStd);

        txtaccount.setEnabled(true);
        txtaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginRespose = (LoginRespose) gson.fromJson(CommonUtilities.getPreference(act, CommonUtilities.pref_customer_detail), LoginRespose.class);

                if (isClick) {
                    isClick = false;
                    accountlayout.setVisibility(View.GONE);
                } else {
                    String global_profile_type = CommonUtilities.getPreference(act, CommonUtilities.pref_global_profile_type);
                    if (global_profile_type.equals("1")) {
                        isClick = true;
                        accountlayout.setVisibility(View.VISIBLE);
                        layout_profcomp.setVisibility(View.GONE);
                        txtaccount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.dropdown_icon, 0);
                    } else
                        txtaccount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);


                }
            }
        });
    }

    public void setDrawerItems() {
        nav_drawer_items.clear();
        String array[] = getResources().getStringArray(R.array.nav_drawer_items);

        for (int j = 0; j < array.length; j++) {
            DrawerItem drawerItem = new DrawerItem();
            drawerItem.itemname = array[j];
            drawerItem.itemNumber = j;
            nav_drawer_items.add(drawerItem);
        }
        loginRespose = (LoginRespose) gson.fromJson(CommonUtilities.getPreference(act, CommonUtilities.pref_customer_detail), LoginRespose.class);
        if (loginRespose != null) {

            if (loginRespose.response.getUser_info().getProfile_type()!=null && loginRespose.response.getUser_info().getProfile_type().equals("1")) {
                CommonUtilities.setPreference(act, CommonUtilities.pref_job_type, "Personal");

            } else if (loginRespose.response.getUser_info().getProfile_type().equals("2")) {
                CommonUtilities.setPreference(act, CommonUtilities.pref_job_type, "Professional");
            }

            if (loginRespose.response.getUser_info().getProfile_type().equals("1") && loginRespose.response.getUser_info().getRegistration_type().equals("2")
                    && loginRespose.response.getUser_info().getEmployee_role().equals("1")) {
                nav_drawer_items.remove(1);
            } else if (loginRespose.response.getUser_info().getProfile_type().equals("2") && loginRespose.response.getUser_info().getRegistration_type().equals("1")
                    && loginRespose.response.getUser_info().getEmployee_role().equals("2")) {
                nav_drawer_items.remove(1);
                for (int i = 0; i < nav_drawer_items.size(); i++) {
                    if (nav_drawer_items.get(i).itemNumber == 3) {
                        nav_drawer_items.remove(i);
                    }
                }
                for (int i = 0; i < nav_drawer_items.size(); i++) {
                    if (nav_drawer_items.get(i).itemNumber == 4) {
                        nav_drawer_items.remove(i);
                    }
                }
            } else if (loginRespose.response.getUser_info().getProfile_type().equals("2") && loginRespose.response.getUser_info().getRegistration_type().equals("2")
                    && loginRespose.response.getUser_info().getEmployee_role().equals("1")) {
                nav_drawer_items.remove(1);
                for (int i = 0; i < nav_drawer_items.size(); i++) {
                    if (nav_drawer_items.get(i).itemNumber == 3) {
                        nav_drawer_items.remove(i);
                    }
                }
                for (int i = 0; i < nav_drawer_items.size(); i++) {
                    if (nav_drawer_items.get(i).itemNumber == 4) {
                        nav_drawer_items.remove(i);
                    }
                }
            } else if (loginRespose.response.getUser_info().getProfile_type().equals("2") && loginRespose.response.getUser_info().getRegistration_type().equals("2")
                    && loginRespose.response.getUser_info().getEmployee_role().equals("2")) {
                nav_drawer_items.remove(1);
                for (int i = 0; i < nav_drawer_items.size(); i++) {
                    if (nav_drawer_items.get(i).itemNumber == 3) {
                        nav_drawer_items.remove(i);
                    }
                }
                for (int i = 0; i < nav_drawer_items.size(); i++) {
                    if (nav_drawer_items.get(i).itemNumber == 4) {
                        nav_drawer_items.remove(i);
                    }
                }
            }
            else if (loginRespose.response.getUser_info().getProfile_type().equals("2") && loginRespose.response.getUser_info().getRegistration_type().equals("1")
                    && loginRespose.response.getUser_info().getEmployee_role().equals("1")) {
                nav_drawer_items.remove(1);
                for (int i = 0; i < nav_drawer_items.size(); i++) {
                    if (nav_drawer_items.get(i).itemNumber == 3) {
                        nav_drawer_items.remove(i);
                    }
                }
                for (int i = 0; i < nav_drawer_items.size(); i++) {
                    if (nav_drawer_items.get(i).itemNumber == 4) {
                        nav_drawer_items.remove(i);
                    }
                }
            }
            else
            {
                if(loginRespose.response.getUser_info().global_profile_type.equals("1"))
                {
                    for (int i = 0; i < nav_drawer_items.size(); i++) {
                        if (nav_drawer_items.get(i).itemNumber == 10) {
                            nav_drawer_items.remove(i);
                        }
                    }
                }
            }
        }

        if(loginRespose.response.getUser_info().getCurrent_trip()!=null)
        {
            if (loginRespose.response.getUser_info().getCurrent_trip().equals("0")) {
                for (int i = 0; i < nav_drawer_items.size(); i++) {
                    if (nav_drawer_items.get(i).itemNumber == 5) {
                        nav_drawer_items.remove(i);
                    }
                }
            }
            else
            {
                if(!loginRespose.response.getUser_info().getCurrent_trip().equals("0"))
                    CommonUtilities.setPreference(this,CommonUtilities.pref_booking_id,loginRespose.response.getUser_info().getCurrent_trip());
            }
        }
        else
        {
            for (int i = 0; i < nav_drawer_items.size(); i++) {
                if (nav_drawer_items.get(i).itemNumber == 5) {
                    nav_drawer_items.remove(i);
                }
            }
        }
    }

    private void requestCameraPermission() {
        CommonUtilities.isPermissionGranted = true;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 21);
    }

    private boolean checkLocationPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 21:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                }
                else
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, new Booking_fragment(),"6");
                    fragmentTransaction.commitAllowingStateLoss();
                }
                break;

        }
    }

    private void getLocation() {
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSEnabled) {
            if (CommonUtilities.isConnectingToInternet(act)) {
                Verifymail();
            } else
                CommonUtilities.alertdialog(act, getString(R.string.val_internet));

        } else {
            showSettingsAlert();
        }
    }

    void Verifymail() {
        if (!CommonUtilities.getBooleanPreference(act, "isVerified")) {
            if (CommonUtilities.isConnectingToInternet(act)) {
                if (loginRespose != null)
                    new Verifymail("0").execute();
            } else
                CommonUtilities.alertdialog(act,
                        getString(R.string.val_internet));
        }
        Bundle bundle = new Bundle();
        if (getIntent().hasExtra("msg") && getIntent().hasExtra("type"))
        {
            bundle.putString("msg", getIntent().getExtras().getString("msg"));
            bundle.putString("type", getIntent().getExtras().getString("type"));
            bundle.putString("booking_id", getIntent().getExtras().getString("booking_id"));
            if(getIntent().hasExtra(CommonUtilities.REPORT_SUBMITTED))
            {
                bundle.putString(CommonUtilities.REPORT_SUBMITTED,getIntent().getExtras().getString(CommonUtilities.REPORT_SUBMITTED));
            }
            if(getIntent().hasExtra("currentJourney"))
            {
                bundle.putString("currentJourney","currentJourney");
            }
            Fragment fragment = new Fragment();
            String tag = "";
            if (getIntent().getExtras().getString("type").equals("1") || getIntent().getExtras().getString("type").equals("2")) {
                fragment = new Booking_fragment();
                tag = "6";
                for (int i = 0; i < nav_drawer_items.size(); i++) {
                    if (nav_drawer_items.get(i).itemNumber == 6) {
                        selectedPos = i;
                        break;
                    }
                }
            } else if(getIntent().getExtras().getString("type").equals("7"))
            {
                CommonUtilities.RemovePreference(this, CommonUtilities.pref_booking_id);
                fragment = new Booking_fragment();
                tag = "6";
                for (int i = 0; i < nav_drawer_items.size(); i++) {
                    if (nav_drawer_items.get(i).itemNumber == 6) {
                        selectedPos = i;
                        break;
                    }
                }
            }
            else {
                fragment = new CurrentJourney();
                tag = "5";
                for (int i = 0; i < nav_drawer_items.size(); i++) {
                    if (nav_drawer_items.get(i).itemNumber == 5) {
                        selectedPos = i;
                        mDrawerList.setItemChecked(i, true);
                        mDrawerList.smoothScrollToPosition(0);
                        break;
                    }
                }
            }
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment,tag);
            fragmentTransaction.commitAllowingStateLoss();

        } else if (getIntent().hasExtra("trackmycar")) {
            bundle.putString("trackmycar","trackmycar");
            bundle.putString("booking_id", getIntent().getExtras().getString("booking_id"));
            if(getIntent().hasExtra("currentJourney"))
                bundle.putString("currentJourney", getIntent().getExtras().getString("currentJourney"));
            changeFragment(bundle);
        } else if(getIntent().hasExtra("booking_id")) {
            for (int i = 0; i < nav_drawer_items.size(); i++) {
                if (nav_drawer_items.get(i).itemNumber == 6) {
                    selectItem(i);
                    mDrawerList.setSelection(i);
                    mDrawerList.setItemChecked(i, true);
                    mDrawerList.smoothScrollToPosition(0);
                    break;
                }
            }
        } else {
            if (loginRespose == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, new Booking_fragment());
                fragmentTransaction.commitAllowingStateLoss();
            }
            else {
                if(loginRespose.getResponse().getUser_info().current_trip!=null && !loginRespose.getResponse().getUser_info().current_trip.equals("0"))
                {
                    for (int i = 0; i < nav_drawer_items.size(); i++) {
                        if (nav_drawer_items.get(i).itemNumber == 5) {
                            selectItem(i);
                            mDrawerList.setSelection(i);
                            mDrawerList.setItemChecked(i, true);
                            mDrawerList.smoothScrollToPosition(0);
                            break;
                        }
                    }
                }
                else
                {
                    for (int i = 0; i < nav_drawer_items.size(); i++) {
                        if (nav_drawer_items.get(i).itemNumber == 6) {
                            selectItem(i);
                            mDrawerList.setSelection(i);
                            mDrawerList.setItemChecked(i, true);
                            mDrawerList.smoothScrollToPosition(0);
                            break;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < nav_drawer_items.size(); i++) {
            if (nav_drawer_items.get(i).itemNumber == 6) {
                mDrawerList.setSelection(i);
                mDrawerList.setItemChecked(i, true);
                mDrawerList.smoothScrollToPosition(0);
                break;
            }
        }
    }

    public void changeFragment(Bundle bundle) {
        CurrentJourney fragment = new CurrentJourney();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment,"5");
        fragmentTransaction.commitAllowingStateLoss();
        for (int i = 0; i < nav_drawer_items.size(); i++) {
            if (nav_drawer_items.get(i).itemNumber == 5) {
                selectedPos = i;
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                getLocation();
                break;
            case 1234:
                if (Build.VERSION.SDK_INT >= 23) {   //Android M Or Over
                    if (!Settings.canDrawOverlays(this)) {
                        Log.e("enter","onactivity result");
                        // ADD UI FOR USER TO KNOW THAT UI for SYSTEM_ALERT_WINDOW permission was not granted earlier...
                    }
                }

        }
    }

    boolean firstTime = true;

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result + 30;
    }


    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            accountlayout.setVisibility(View.GONE);
            layout_profcomp.setVisibility(View.GONE);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
                selectItem(position);
            }
            for (int i = 0; i < nav_drawer_items.size(); i++) {
                if (nav_drawer_items.get(i).itemNumber == 6 || i == position) {
                    mDrawerList.setSelection(position);
                    mDrawerList.setItemChecked(position, true);
                } else {
                    mDrawerList.setItemChecked(i, false);
                }
            }
        }
    }


    public void selectItem(int position) {
        Fragment fragment = null;
        selectedPos = position;
        switch (nav_drawer_items.get(position).itemNumber) {
            case 0:
                fragment = new ProfileFragment();
                break;
            case 1:
                fragment = new MyCreditCards();
                break;
            case 2:
                UpcomingTrip.shouldCall = false;
                fragment = new TripFragment();
                break;
            case 3:
                fragment = new ADDCreditFragment();
                break;
            case 4:
                fragment = new BuyCreditFragment();
                break;
            case 5:
                fragment = new CurrentJourney();
                break;
            case 6:
                fragment = new Booking_fragment();
                Foreground.instance.checkBackground = false;
                break;
            case 7:
                fragment = new PromoCode();
                break;
            case 8:
                fragment = new GoInfo();
                break;
            case 9:
                fragment = new faq();
                break;
            case 10:
                fragment = new ChangePin();
                break;
            case 11:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(act);
                builder1.setTitle(R.string.app_name);
                builder1.setIcon(R.mipmap.app_icon);
                builder1.setMessage("Are you sure you want to log out?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (CommonUtilities.isConnectingToInternet(act)) {
                            new ValidateLogout().execute();
                        } else
                            CommonUtilities.alertdialog(act, getString(R.string.val_internet));
                        dialog.cancel();
                    }
                });
                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert11 = builder1.create();
                if(!this.isFinishing() && !this.isDestroyed())
                    alert11.show();
                break;

            default:
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment,nav_drawer_items.get(position).itemNumber+"");
            if(!isFinishing())
                fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public class Verifymail extends AsyncTask<String, Void, String> {
        VerifyMail model;
        String flag;

        public Verifymail(String flag) {
            this.flag = flag;
        }

        @Override
        protected String doInBackground(String... params) {

            ServerAccess sa = new ServerAccess();
            custid = CommonUtilities.getPreference(act, CommonUtilities.pref_customer_id);
            model = sa.GetEmailVerify(Dashboard_Activity.this,custid);
            if (model != null) {
                if (model.response.status.equals(CommonUtilities.key_Success))
                    return "true";
                else
                    return "false";

            } else
                return "error";
        }

        @Override
        protected void onPostExecute(String result) {

            try
            {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            catch (IllegalArgumentException e)
            {
                e.printStackTrace();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            if (result.equals("true")) {
                if (flag.equals("0"))
                    CommonUtilities.setBooleanPreference(act, "isVerified", true);
                else
                    CommonUtilities.setBooleanPreference(act, "isPersonalVerified", true);
                mDrawerList.setEnabled(true);
            } else {
                if (model != null) {
                    if (!custid.equals("")) {
                        CommonUtilities.setBooleanPreference(act, "isVerified", false);

                        final AlertDialog.Builder builder = new AlertDialog.Builder(act);
                        builder.setMessage(model.response.msg)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.dismiss();

                                if (CommonUtilities.isConnectingToInternet(act)) {
                                    new ValidateLogout().execute();
                                } else
                                    CommonUtilities.alertdialog(act, getString(R.string.val_internet));

                            }
                        });
                        AlertDialog alert = builder.create();
                        if(!Dashboard_Activity.this.isFinishing())
                            alert.show();

                    }
                }
                else
                {
                    if(!Dashboard_Activity.this.isFinishing())
                        CommonUtilities.alertdialog(Dashboard_Activity.this,getString(R.string.val_something));
                }
            }
        }

        @Override
        protected void onPreExecute() {

        }
    }

    public class MenuDetails extends AsyncTask<String, Void, String> {
        LoginRespose model;
        String flag;

        public MenuDetails(String flag) {
            this.flag = flag;
        }

        @Override
        protected String doInBackground(String... params) {

            ServerAccess sa = new ServerAccess();
            custid = CommonUtilities.getPreference(act, CommonUtilities.pref_customer_id);
            model = sa.GetBurgerMenuDetails(Dashboard_Activity.this,custid,getip.NetwordDetect());
            if (model != null) {
                if (model.response.status.equals(CommonUtilities.key_Success))
                    return "true";
                else
                    return "false";

            } else
                return "false";
        }

        @Override
        protected void onPostExecute(String result) {
            try
            {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            catch (IllegalArgumentException e)
            {
                e.printStackTrace();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            if (result.equals("true")) {

                Gson gson = new Gson();
                String detail = gson.toJson(model);
                CommonUtilities.setPreference(Dashboard_Activity.this, CommonUtilities.pref_customer_detail, detail);
                if(model.response.getUser_info().force_download!=null && !model.response.getUser_info().force_download.equals("0"))
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Dashboard_Activity.this);
                    builder1.setTitle(R.string.app_name);
                    builder1.setIcon(R.mipmap.app_icon);
                    builder1.setMessage(model.response.getUser_info().message);
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                            Uri uri = Uri.parse("market://details?id=" + Dashboard_Activity.this.getPackageName());
                            Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                            try {
                                startActivity(myAppLinkToMarket);
                                System.exit(0);
                            } catch (ActivityNotFoundException e) {
                                CommonUtilities.ShowToast(Dashboard_Activity.this, "unable to find app");
                            }
                        }
                    });
                    if(!model.response.getUser_info().force_download.equals("1"))
                    {
                        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                new CancelNewUpdate().execute();
                                setUserInfo(flag);
                            }
                        });
                    }
                    AlertDialog alert11 = builder1.create();
                    if(!Dashboard_Activity.this.isFinishing())
                        alert11.show();
                }
                else if(Integer.valueOf(model.response.getUser_info().ResponseFlag) != null && model.response.getUser_info().ResponseFlag==0)
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Dashboard_Activity.this);
                    builder1.setTitle(R.string.app_name);
                    builder1.setIcon(R.mipmap.app_icon);
                    builder1.setMessage(model.response.msg);
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            System.exit(0);
                        }
                    });

                    AlertDialog alert11 = builder1.create();
                    if(!Dashboard_Activity.this.isFinishing())
                        alert11.show();
                }
                else if(Integer.valueOf(model.response.getUser_info().member_status) != null && model.response.getUser_info().member_status==0)
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Dashboard_Activity.this);
                    builder1.setTitle(R.string.app_name);
                    builder1.setIcon(R.mipmap.app_icon);
                    builder1.setMessage(model.response.getUser_info().message);
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent iStart = new Intent(Dashboard_Activity.this, Joinus.class);
                            iStart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(iStart);
                            CommonUtilities.RemoveALlPreference(Dashboard_Activity.this);
                        }
                    });

                    AlertDialog alert11 = builder1.create();
                    if(!Dashboard_Activity.this.isFinishing())
                        alert11.show();
                }
                else
                {
                    setUserInfo(flag);
                }
            } else {
                if (model != null) {
                    if(model.response.code.equals("ERR003"))
                    {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(Dashboard_Activity.this);
                        builder1.setTitle(R.string.app_name);
                        builder1.setIcon(R.mipmap.app_icon);
                        builder1.setMessage(model.response.msg);
                        builder1.setCancelable(false);
                        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                new ValidateLogout().execute();
                            }
                        });

                        AlertDialog alert11 = builder1.create();
                        if(!Dashboard_Activity.this.isFinishing())
                            alert11.show();
                    }
                    else
                    {
                        CommonUtilities.ShowToast(Dashboard_Activity.this,model.response.msg);
                    }
                }
                else
                {
                    if(!Dashboard_Activity.this.isFinishing())
                        CommonUtilities.alertdialog(Dashboard_Activity.this,getString(R.string.val_something));
                }
            }
        }

        @Override
        protected void onPreExecute() {
            if(flag.equals("1"))
            {
                if(dialog!=null && !dialog.isShowing())
                    dialog.show();
            }

        }
    }


    public void setUserInfo(String flag)
    {
        setDrawerItems();
        adapter.notifyDataSetChanged();
        if (flag.equals("1")) {
            for (int i = 0; i < nav_drawer_items.size(); i++) {
                if (nav_drawer_items.get(i).itemNumber == 6) {
                    selectItem(i);
                    mDrawerList.setSelection(i);
                    mDrawerList.setItemChecked(i,true);
                    break;
                }
            }
        }
        SetData();
    }
    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (nav_drawer_items.size() > 0) {
                if (nav_drawer_items.get(selectedPos).itemNumber != 6) {
                    for (int i = 0; i < nav_drawer_items.size(); i++) {
                        if (nav_drawer_items.get(i).itemNumber == 6) {
                            selectItem(i);
                            break;
                        }
                    }
                } else
                    super.onBackPressed();
            } else
                super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_, menu);
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_help).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        return true;
    }

    public void SetData() {

        LoginRespose model;
        model = (LoginRespose) gson.fromJson(CommonUtilities.getPreference(act, CommonUtilities.pref_customer_detail),
                LoginRespose.class);

        if (model != null) {

            TextView notifno = (TextView) findViewById(R.id.notifcount);

            if (model.response.user_info.notifications_count != null) {
                notifno.setText(model.response.user_info.notifications_count);
                if (!(model.response.user_info.notifications_count).equals("0")) {
                    notifno.setVisibility(View.VISIBLE);
                    notificationimg.setImageResource(R.mipmap.notification_icon_active);
                } else {
                    notifno.setVisibility(View.GONE);
                    notificationimg.setImageResource(R.mipmap.notification_icon_disable);
                }
            }

            TextView notifalert = (TextView) findViewById(R.id.tvnotify);
            if (model.response.user_info.notifications_count != null) {
                if ((model.response.user_info.notifications_count).equals("0") ||
                (model.response.user_info.notifications_count).equals("1")){
                    notifalert.setText(" alert");
                    }else{
                    notifalert.setText(" alerts");
                    }
            }

            CommonUtilities.setFontFamily(act, notifno, CommonUtilities.GillSansStd);

            TextView tvnotify = (TextView) findViewById(R.id.tvnotify);
            CommonUtilities.setFontFamily(act, tvnotify, CommonUtilities.GillSansStd);

            TextView tvCompleted = (TextView) findViewById(R.id.tvCompleted);
            CommonUtilities.setFontFamily(act, tvCompleted, CommonUtilities.GillSansStd);

            TextView tvCredits = (TextView) findViewById(R.id.tvCredits);
            CommonUtilities.setFontFamily(act, tvCredits, CommonUtilities.GillSansStd);

            ImageView profimg_complete = (ImageView) findViewById(R.id.profimg_complete);
            String profcount = model.response.getUser_info().profile_completed;

            if (profcount.equals("75")) {
                profimg_complete.setImageResource(R.mipmap.quater_completed_icon);
            } else if (profcount.equals("25")) {
                profimg_complete.setImageResource(R.mipmap.quater_half_completed_icon);
            } else if (profcount.equals("100")) {
                profimg_complete.setImageResource(R.mipmap.full_completed_icon);
            } else if (profcount.equals("50")) {
                profimg_complete.setImageResource(R.mipmap.half_completed_icon);
            } else if (profcount.equals("33")) {
                profimg_complete.setImageResource(R.mipmap.thirtythree_completed_icon);
            } else if (profcount.equals("66")) {
                profimg_complete.setImageResource(R.mipmap.sixtysix_completed_icon);
            }
            else if (profcount.equals("20")) {
                profimg_complete.setImageResource(R.mipmap.twenty_percent_complete);
            }
            else if (profcount.equals("40")) {
                profimg_complete.setImageResource(R.mipmap.forty_percent_complete);
            }
            else if (profcount.equals("60")) {
                profimg_complete.setImageResource(R.mipmap.sixty_percent_completed_icon);
            }
            else if (profcount.equals("80")) {
                profimg_complete.setImageResource(R.mipmap.eighty_percent_complete);
            }
            else {
                profimg_complete.setImageResource(R.mipmap.zero_completed_icon);
            }

            TextView txtcreditrnd = (TextView) findViewById(R.id.txtcreditrnd);
            String txtcreditstr = model.response.user_info.credits;
            if (txtcreditstr.length() > 4) {
                txtcreditstr = txtcreditstr.substring(0, 4);
                txtcreditrnd.setText(txtcreditstr);
            }
            else
            {
                txtcreditrnd.setText(txtcreditstr);
            }

            if (!TextUtils.isEmpty(txtcreditstr)) {
                if (txtcreditstr.equals("0.00")) {
                    creditlayout.setBackgroundResource(R.mipmap.credits_orange_icon);
                    txtcreditrnd.setTextColor(Color.parseColor("#F58000"));
                    // txtcreditrnd.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                    txtcreditrnd.setPadding(12, 0, 0, 0);
                } else if (Float.valueOf(txtcreditstr) > 0) {
                    creditlayout.setBackgroundResource(R.mipmap.blank_credits_icon);
                    txtcreditrnd.setTextColor(Color.parseColor("#8DC73F"));
                    if (txtcreditstr.length() == 2) {
                        txtcreditrnd.setPadding(0, 0, 0, 0);
                    }
                } else if (Float.valueOf(txtcreditstr) < 0) {
                    creditlayout.setBackgroundResource(R.mipmap.credits_red_icon);
                    txtcreditrnd.setTextColor(Color.parseColor("#F62A00"));
                    if (txtcreditstr.length() == 2) {
                        txtcreditrnd.setPadding(0, 0, 0, 0);
                    }
                }
            }
//            if (model.response.getUser_info().is_payment_notification.equals("1")) {
//                notifyimage.setVisibility(View.VISIBLE);
//            } else
//                notifyimage.setVisibility(View.INVISIBLE);

            TextView custname = (TextView) findViewById(R.id.custname);
            custname.setText(model.response.user_info.first_name + " " + model.response.user_info.last_name);

            //  txtcredit,txtmembership,txtliscence,txtsign
            TextView txtliscence = (TextView) findViewById(R.id.txtliscence);
            TextView txtmembership = (TextView) findViewById(R.id.txtmembership);
            TextView txtsign = (TextView) findViewById(R.id.txtsign);
            TextView txtcredit = (TextView) findViewById(R.id.txtcredit);
            TextView txtlicence2 = (TextView) findViewById(R.id.txtlicence2);

            CommonUtilities.setFontFamily(act, txtcredit, CommonUtilities.GillSansStd);
            CommonUtilities.setFontFamily(act, txtmembership, CommonUtilities.GillSansStd);
            CommonUtilities.setFontFamily(act, txtsign, CommonUtilities.GillSansStd);
            CommonUtilities.setFontFamily(act, txtliscence, CommonUtilities.GillSansStd);
            CommonUtilities.setFontFamily(act, txtlicence2, CommonUtilities.GillSansStd);
            CommonUtilities.setFontFamily(act, txtaddress, CommonUtilities.GillSansStd);
            CommonUtilities.setFontFamily(act, txtaddress2, CommonUtilities.GillSansStd);
            CommonUtilities.setFontFamily(act, txtliscenceprofess2, CommonUtilities.GillSansStd);
            CommonUtilities.setFontFamily(act, txtliscenceprofess, CommonUtilities.GillSansStd);
            CommonUtilities.setFontFamily(act, txtaccntpaidfull, CommonUtilities.GillSansStd);
            CommonUtilities.setFontFamily(act, txtmembershipprof, CommonUtilities.GillSansStd);


            if (model.response.user_info.driving_licence_added.equals("1")) {
                txtliscence.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.check_mark_icon, 0, 0, 0);
                txtliscence.setTextColor(Color.parseColor("#3c3c3c"));
                txtlicence2.setTextColor(Color.parseColor("#3c3c3c"));
                txtliscenceprofess.setTextColor(Color.parseColor("#3c3c3c"));
                txtliscenceprofess2.setTextColor(Color.parseColor("#3c3c3c"));
                txtliscenceprofess.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.check_mark_icon, 0, 0, 0);
            } else {
                txtliscence.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.not_valid_icon, 0, 0, 0);
                txtliscence.setTextColor(Color.parseColor("#F21C17"));
                txtlicence2.setTextColor(Color.parseColor("#F21C17"));
                txtliscenceprofess.setTextColor(Color.parseColor("#F21C17"));
                txtliscenceprofess2.setTextColor(Color.parseColor("#F21C17"));
                txtliscenceprofess.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.not_valid_icon, 0, 0, 0);
            }
            if (model.response.user_info.getAddress_filled().equals("1")) {
                txtaddress.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.check_mark_icon, 0, 0, 0);
                txtaddress2.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.check_mark_icon, 0, 0, 0);
                txtaddress.setTextColor(Color.parseColor("#3c3c3c"));
                txtaddress2.setTextColor(Color.parseColor("#3c3c3c"));
            } else {
                txtaddress.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.not_valid_icon, 0, 0, 0);
                txtaddress2.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.not_valid_icon, 0, 0, 0);
                txtaddress.setTextColor(Color.parseColor("#F21C17"));
                txtaddress2.setTextColor(Color.parseColor("#F21C17"));
            }
            if (model.response.user_info.membership_paid.equals("1")) {
                txtmembership.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.check_mark_icon, 0, 0, 0);
                txtmembership.setTextColor(Color.parseColor("#3c3c3c"));
                txtmembershipprof.setTextColor(Color.parseColor("#3c3c3c"));
                txtmembershipprof.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.check_mark_icon, 0, 0, 0);
            } else {
                txtmembership.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.not_valid_icon, 0, 0, 0);
                txtmembership.setTextColor(Color.parseColor("#F21C17"));
                txtmembershipprof.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.not_valid_icon, 0, 0, 0);
                txtmembershipprof.setTextColor(Color.parseColor("#F21C17"));
            }
            if ((model.response.user_info.remaining_invoice.equals("1"))) {
                txtaccntpaidfull.setTextColor(Color.parseColor("#3c3c3c"));
                txtaccntpaidfull.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.check_mark_icon, 0, 0, 0);
            } else {
                txtaccntpaidfull.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.not_valid_icon, 0, 0, 0);
                txtaccntpaidfull.setTextColor(Color.parseColor("#F21C17"));
            }
            if (model.response.user_info.credit_card_added.equals("1")) {
                txtcredit.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.check_mark_icon, 0, 0, 0);
                txtcredit.setTextColor(Color.parseColor("#3c3c3c"));
            } else {
                txtcredit.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.not_valid_icon, 0, 0, 0);
                txtcredit.setTextColor(Color.parseColor("#F21C17"));
            }
            if (model.response.user_info.signup_fee_paid.equals("1")) {
                txtsign.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.check_mark_icon, 0, 0, 0);
                txtsign.setTextColor(Color.parseColor("#3c3c3c"));
            } else {
                txtsign.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.not_valid_icon, 0, 0, 0);
                txtsign.setTextColor(Color.parseColor("#F21C17"));
            }

            final ImageView profimage = (ImageView) findViewById(R.id.profimage);

            if (model.response.user_info.profile_pic != null) {
                String imagename = model.response.user_info.profile_pic;
                if (imagename.equals("")) {
                    profimage.setImageResource(R.mipmap.no_user_image);
                } else {
                    Picasso.with(Dashboard_Activity.this)
                            .load(CommonUtilities.ProfileImageURL +
                                    imagename)
                            .fit().placeholder(R.mipmap.no_user_image)
                            .error(R.mipmap.no_user_image) // optional
                            .into(profimage, new Callback() {

                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onError() {
                                }
                            });
                }
            }
        }
    }

    public void showSettingsAlert() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(Dashboard_Activity.this);

        // Setting Dialog Title
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setIcon(R.mipmap.app_icon);

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setCancelable(false);
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, 1);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Verifymail();
            }
        });

        // Showing Alert Message
        if(!Dashboard_Activity.this.isFinishing())
            alertDialog.show();
    }

    class ValidateLogout extends AsyncTask<String, Void, String> {
        JSONObject response;

        @Override
        protected String doInBackground(String... params) {

            ServerAccess sa = new ServerAccess();
            response = sa.getLogoutDetail(Dashboard_Activity.this);

            if (response != null) {
                try {
                    if (response.getJSONObject("response").get("status")
                            .equals(CommonUtilities.key_Success))
                        return "true";
                    else
                        return "false";
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return "false";
        }


        @Override
        protected void onPostExecute(String result) {

            try
            {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            catch (IllegalArgumentException e)
            {
                e.printStackTrace();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            if (result.equals("true")) {
                try {
                    CommonUtilities.setSecurity_Preference(Dashboard_Activity.this, CommonUtilities.pref_SecurityToken,response.getJSONObject("response").getJSONObject("token_info").getString("security_token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CommonUtilities.RemoveALlPreference(act);
                Intent iStart = new Intent(act, Joinus.class);
                iStart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                iStart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                NotificationManager nMgr = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                nMgr.cancel(9999);
                startActivity(iStart);
                finish();
            } else {
                try {
                    if(response!=null)
                        CommonUtilities.ShowToast(act, response.getJSONObject("response").getString("msg"));
                    else
                    {
                        if(!Dashboard_Activity.this.isFinishing())
                            CommonUtilities.alertdialog(Dashboard_Activity.this,getString(R.string.val_something));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

    }

    class switch_cust_acc extends AsyncTask<String, Void, String> {
        ResponseModel response;

        @Override
        protected String doInBackground(String... params) {

            ServerAccess sa = new ServerAccess();
            response = sa.switch_customer_acc(Dashboard_Activity.this, CommonUtilities.getPreference(Dashboard_Activity.this, CommonUtilities.pref_customer_id), curr_account_id, account_type);

            if (response != null) {

                if (response.response.status.equals(CommonUtilities.key_Success))
                    return "true";
                else
                    return "false";
            }
            return "false";
        }

        @Override
        protected void onPostExecute(String result) {

            if (result.equals("true")) {
                isClick = false;
                for (int i = 0; i < nav_drawer_items.size(); i++) {
                    mDrawerList.setItemChecked(i, false);
                }
                new MenuDetails("1").execute();
            }
            else {
                dialog.dismiss();
                if(response!=null)
                    CommonUtilities.ShowToast(Dashboard_Activity.this,response.response.msg);
                else
                {
                    if(!Dashboard_Activity.this.isFinishing())
                        CommonUtilities.alertdialog(Dashboard_Activity.this,getString(R.string.val_something));
                }
            }

        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

    }

    public class CancelNewUpdate extends AsyncTask<String, Void, String> {
        ResponseModel updateCancelmodel;

        @Override
        protected String doInBackground(String... params) {

            ServerAccess sa = new ServerAccess();

            updateCancelmodel = sa.getCancelNewUpdate(Dashboard_Activity.this);
            if (updateCancelmodel != null) {
                if (updateCancelmodel.response.status.equals(CommonUtilities.key_Success))
                    return "true";
                else
                    return "false";

            } else
                return "false";
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("false"))
            {
                if(updateCancelmodel!=null)
                    CommonUtilities.ShowToast(Dashboard_Activity.this,updateCancelmodel.response.msg);
                else
                {
                    if(!Dashboard_Activity.this.isFinishing())
                        CommonUtilities.alertdialog(Dashboard_Activity.this,getString(R.string.val_something));
                }
            }
        }

        @Override
        protected void onPreExecute() {

        }
    }

    public void permissionToDrawOverlays()
    {
        Log.e("enter","dialog");
        if (Build.VERSION.SDK_INT >= 23) {   //Android M Or Over

            if (!Settings.canDrawOverlays(this)) {
                Log.e("enter","overlay");
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent,1234);
            }
        }
    }
}
