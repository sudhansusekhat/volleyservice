package com.GoCarDev.booking;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.GoCarDev.BuildConfig;
import com.GoCarDev.CustomTimePicker;
import com.GoCarDev.Dashboard_Activity;
import com.GoCarDev.Foreground;
import com.GoCarDev.Gpstracker.GPSTracker;
import com.GoCarDev.MyApplication;
import com.GoCarDev.R;
import com.GoCarDev.getIP;
import com.GoCarDev.login.Joinus;
import com.GoCarDev.login.Signup_Membership;
import com.GoCarDev.model.BookingDetail;
import com.GoCarDev.model.ChangePin;
import com.GoCarDev.model.CheckMyCarStatus;
import com.GoCarDev.model.Check_customer_status;
import com.GoCarDev.model.Check_trip;
import com.GoCarDev.model.CommonUtilities;
import com.GoCarDev.model.Dialog;
import com.GoCarDev.model.LoginRespose;
import com.GoCarDev.model.car_types;
import com.GoCarDev.model.go_bases;
import com.GoCarDev.model.rating_model;
import com.GoCarDev.serverAccess.ServerAccess;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Booking_fragment extends Fragment implements OnMapReadyCallback, OnMapClickListener {

    static TextView txtSelDeparture;
    static TextView txtSeleArrival;
    static boolean booking_flag;
    static Double pinLat, pinlong;
    Dashboard_Activity mActivity;
    static int sdYear;
    static int sdMonth;
    static int sdDay;
    static int saYear;
    static int saMonth;
    static int saDay;
    static int sHour;
    static int sMinute;
    String slot_id = "0";
    public int flag = 0;
    public int carAvailabilityFlag = 0;
    public BookingDetail booking_detail;
    public double latitude, longitude;
    public String formated_address;
    ImageView btnBookNow, cityCar;
    TextView txtHeyImAt;
    TextView txt_model;
    TextView gobaseName;
    TextView gobaseLocationName;
    TextView val_empty_gobase;
    public int rating_anim;
    TextView txtselectdep;
    TextView txtselectarr, txt_plus_km;
    LinearLayout booknowDialog, bookNowChargeDialog;
    Calendar c;
    ImageView BlackG, btnNavigation, btnMakeBooking, btn_gobas, btn_favouritebas;
    RelativeLayout transparentView, mapLayout;
    LinearLayout NavigationDialog;
    TextView txtNewBases, txtMostUsedBases, txtReset, txt_current_address, txtHourlyCost, txtCost;
    boolean NavigationDialog_visibility = false;
    Activity act;
    go_bases go_base_obj;
    Gson gson;
    ListView list_gobase;
    AutoCompleteTextView autoComplete_searchtext;
    View v;
    String Customer_id;
    Marker selected_marker;
    ArrayList<Marker> markerList = new ArrayList<>();
    ArrayList<Marker> markerGoBaseList = new ArrayList<>();
    Menu menu;
    LatLngBounds.Builder builder;
    CameraUpdate cu;
    GPSTracker gps;
    int car_model_pos;
    LinearLayout car_layout;
    String strGobaseName, strGoBaseAddress;
    boolean is_departure_selected, is_arrival_selected;
    String booking_date, select_arrive_date, select_departure_date;
    Dialog dialog;
    RelativeLayout layout_carAvailibity;
    TextView msgRemember1Hr;
    ImageView ivCancel;
    LinearLayout iFoundCar;
    TextView btnLocateMyCar;
    Check_trip trip_model;
    int position = 0, car_availibility_mode_size = 0;
    car_types car_obj;
    go_bases go_nearbaselist;
    int i;
    car_types cartype;
    go_bases go_list;
    String booking_id;
    LoginRespose loginRespose;
    int slotPos = 0;
    boolean validTime = false;
    TimePickerDialog tpd;
    boolean onActivityResultCalledBeforeOnResume;
    public Dialog ratingdialog;
    getIP getip;
    public static boolean drivercall;
    public static boolean show_insurance_sc;
    public LoginRespose model;
    public int noOfTimesCalled = 0;
    String versionRelease = Build.VERSION.RELEASE;
    TimePickerDialog.OnTimeSetListener DepartureTimePicker = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        }
    };

    TimePickerDialog.OnTimeSetListener ArrivalTimePicker = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            txtselectarr.setVisibility(View.GONE);

            is_arrival_selected = true;
            txtSeleArrival.setText(txtSeleArrival.getText() + "\n" + String.valueOf(hourOfDay) + "h" + String.valueOf(minute));
            txtSeleArrival.setTextColor((getResources().getColor(R.color.lime)));

        }
    };
    private GoogleMap mMap;
    private ArrayList<Marker> mMarkerArray = new ArrayList<Marker>();
    private RelativeLayout header;
    private ImageView imgPrevious;
    private TextView basename, txtGoBase;
    private TextView baseaddress;
    private ImageView imgNext;
    private LinearLayout headerMsg;
    private ImageView alertIcon;
    private LinearLayout carDetail;
    private FrameLayout temp_layout;
    private ImageView othercarIcon;
    private TextView carname;
    private LinearLayout slotdetail;
    private LinearLayout other_car_type_layout;
    private ImageView carIcon;
    private LinearLayout startDate;
    private TextView fromDate;
    private TextView fromTo;
    private LinearLayout endDate;
    RelativeLayout snackbar_action10;
    private TextView toDate, detailheader;
    private ImageView daySepersatertIcon;
    private TextView toTime;
    private SlidingUpPanelLayout mLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        act = getActivity();

        v = inflater.inflate(R.layout.activity_maps, container, false);
        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        CommonUtilities.key_security_token = CommonUtilities.getSecurity_Preference(mActivity, CommonUtilities.pref_SecurityToken);
        gson = new Gson();
        booking_detail = new BookingDetail();
        ((Dashboard_Activity)mActivity).getSupportActionBar().setTitle(null);
        ((Dashboard_Activity)mActivity).goImage.setVisibility(View.VISIBLE);
        dialog = new Dialog(mActivity);
        gps = new GPSTracker(mActivity);
        getip=new getIP(mActivity);
        mLayout = (SlidingUpPanelLayout) v.findViewById(R.id.sliding_layout);
        BlackG = (ImageView) v.findViewById(R.id.blackG);
        btnMakeBooking = (ImageView) v.findViewById(R.id.btnMakeBooking);
        btnNavigation = (ImageView) v.findViewById(R.id.btnNavigation);
        btnBookNow = (ImageView) v.findViewById(R.id.btnBookNow);
        transparentView = (RelativeLayout) v.findViewById(R.id.transparentView);
        NavigationDialog = (LinearLayout) v.findViewById(R.id.NavigationDialog);
        temp_layout = (FrameLayout) v.findViewById(R.id.temp_layout);
        msgRemember1Hr = (TextView) v.findViewById(R.id.msgRemember1Hr);
        ivCancel = (ImageView) v.findViewById(R.id.ivCancel);
        iFoundCar = (LinearLayout) v.findViewById(R.id.iFoundCar);
        snackbar_action10 = (RelativeLayout) v.findViewById(R.id.snackbar_action10);

        drivercall=true;
        show_insurance_sc=true;

        booknowDialog = (LinearLayout) v.findViewById(R.id.booknowDialog);
        bookNowChargeDialog = (LinearLayout) v.findViewById(R.id.bookNowChargeDialog);
        txtNewBases = (TextView) v.findViewById(R.id.txtNewBases);
        header = (RelativeLayout) v.findViewById(R.id.header);
        imgPrevious = (ImageView) v.findViewById(R.id.img_previous);
        basename = (TextView) v.findViewById(R.id.basename);
        baseaddress = (TextView) v.findViewById(R.id.baseaddress);
        imgNext = (ImageView) v.findViewById(R.id.img_next);
        headerMsg = (LinearLayout) v.findViewById(R.id.header_msg);
        alertIcon = (ImageView) v.findViewById(R.id.alertIcon);
        carDetail = (LinearLayout) v.findViewById(R.id.carDetail);
        carIcon = (ImageView) v.findViewById(R.id.carIcon);
        carname = (TextView) v.findViewById(R.id.carname);
        slotdetail = (LinearLayout) v.findViewById(R.id.slotdetail);
        other_car_type_layout = (LinearLayout) v.findViewById(R.id.other_car_type_layout);
        othercarIcon = (ImageView) v.findViewById(R.id.othercarIcon);
        startDate = (LinearLayout) v.findViewById(R.id.startDate);
        detailheader = (TextView) v.findViewById(R.id.detailheader);
        fromDate = (TextView) v.findViewById(R.id.fromDate);
        fromTo = (TextView) v.findViewById(R.id.fromTo);
        endDate = (LinearLayout) v.findViewById(R.id.endDate);
        toDate = (TextView) v.findViewById(R.id.toDate);
        daySepersatertIcon = (ImageView) v.findViewById(R.id.daySepersatertIcon);
        toTime = (TextView) v.findViewById(R.id.toTime);
        txtMostUsedBases = (TextView) v.findViewById(R.id.txtMostUsedBases);
        val_empty_gobase = (TextView) v.findViewById(R.id.val_empty_gobase);
        txtselectdep = (TextView) v.findViewById(R.id.txtselectdep);
        txtselectarr = (TextView) v.findViewById(R.id.txtselectarr);
        txtGoBase = (TextView) v.findViewById(R.id.txtGoBase);
        btnLocateMyCar = (TextView) v.findViewById(R.id.btnLocateMyCar);

        txtReset = (TextView) v.findViewById(R.id.txtReset);
        txt_current_address = (TextView) v.findViewById(R.id.txt_current_address);

        txtSelDeparture = (TextView) v.findViewById(R.id.txtSelDeparture);
        txtSeleArrival = (TextView) v.findViewById(R.id.txtSeleArrival);
        txtHeyImAt = (TextView) v.findViewById(R.id.txtHeyImAt);
        txt_model = (TextView) v.findViewById(R.id.txt_model);
        gobaseName = (TextView) v.findViewById(R.id.gobaseName);
        txtHourlyCost = (TextView) v.findViewById(R.id.txtHourlyCost);

        car_layout = (LinearLayout) v.findViewById(R.id.linear);
        txtCost = (TextView) v.findViewById(R.id.txtCost);

        gobaseLocationName = (TextView) v.findViewById(R.id.gobaseLocationName);
        btn_gobas = (ImageView) v.findViewById(R.id.btn_gobas);
        btn_favouritebas = (ImageView) v.findViewById(R.id.btn_favouritebas);
        list_gobase = (ListView) v.findViewById(R.id.gobaseList);
        BlackG = (ImageView) v.findViewById(R.id.blackG);
        btnMakeBooking = (ImageView) v.findViewById(R.id.btnMakeBooking);
        btnNavigation = (ImageView) v.findViewById(R.id.btnNavigation);
        btnBookNow = (ImageView) v.findViewById(R.id.btnBookNow);
        transparentView = (RelativeLayout) v.findViewById(R.id.transparentView);
        NavigationDialog = (LinearLayout) v.findViewById(R.id.NavigationDialog);
        booknowDialog = (LinearLayout) v.findViewById(R.id.booknowDialog);
        bookNowChargeDialog = (LinearLayout) v.findViewById(R.id.bookNowChargeDialog);
        txtNewBases = (TextView) v.findViewById(R.id.txtNewBases);
        txtSelDeparture.setPaintFlags(txtSelDeparture.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtSeleArrival.setPaintFlags(txtSeleArrival.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        booking_flag = false;
        txtMostUsedBases = (TextView) v.findViewById(R.id.txtMostUsedBases);

        val_empty_gobase = (TextView) v.findViewById(R.id.val_empty_gobase);
        txtselectdep = (TextView) v.findViewById(R.id.txtselectdep);
        txtselectarr = (TextView) v.findViewById(R.id.txtselectarr);

        txtReset = (TextView) v.findViewById(R.id.txtReset);
        txt_current_address = (TextView) v.findViewById(R.id.txt_current_address);

        txtSelDeparture = (TextView) v.findViewById(R.id.txtSelDeparture);
        txtSeleArrival = (TextView) v.findViewById(R.id.txtSeleArrival);
        txtHeyImAt = (TextView) v.findViewById(R.id.txtHeyImAt);
        txt_model = (TextView) v.findViewById(R.id.txt_model);
        gobaseName = (TextView) v.findViewById(R.id.gobaseName);
        txtHourlyCost = (TextView) v.findViewById(R.id.txtHourlyCost);

        txtCost = (TextView) v.findViewById(R.id.txtCost);
        txt_plus_km = (TextView) v.findViewById(R.id.txt_plus_km);
        imgPrevious = (ImageView) v.findViewById(R.id.img_previous);
        gobaseLocationName = (TextView) v.findViewById(R.id.gobaseLocationName);
        btn_gobas = (ImageView) v.findViewById(R.id.btn_gobas);
        btn_favouritebas = (ImageView) v.findViewById(R.id.btn_favouritebas);
        list_gobase = (ListView) v.findViewById(R.id.gobaseList);
        layout_carAvailibity = (RelativeLayout) v.findViewById(R.id.layout_carAvailibity);
        c = Calendar.getInstance();

        sdYear = c.get(Calendar.YEAR);
        sdMonth = c.get(Calendar.MONTH);
        sdDay = c.get(Calendar.DAY_OF_MONTH);

        saYear = c.get(Calendar.YEAR);
        saMonth = c.get(Calendar.MONTH);
        saDay = c.get(Calendar.DAY_OF_MONTH);

        sHour = c.get(Calendar.HOUR_OF_DAY);
        sMinute = c.get(Calendar.MINUTE);

        BlackG.setVisibility(View.VISIBLE);
        booknowDialog.setVisibility(View.GONE);
        bookNowChargeDialog.setVisibility(View.GONE);
        NavigationDialog.setVisibility(View.GONE);
        transparentView.setVisibility(View.GONE);
        layout_carAvailibity.setVisibility(View.GONE);

        set_font_style();

        btnLocateMyCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager)mActivity.getSystemService(Context.LOCATION_SERVICE);
                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (!isGPSEnabled) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mActivity);
                    builder1.setTitle(R.string.app_name);
                    builder1.setIcon(R.mipmap.app_icon);
                    builder1.setMessage("GPS is not enabled.Please enable it");
                    builder1.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 2);
                        }
                    });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else
                {
                    if (CommonUtilities.isConnectingToInternet(mActivity)) {
                        new GetCarStatus(booking_id).execute();
                    } else
                        CommonUtilities.ShowToast(mActivity, getString(R.string.val_internet));
                }
            }
        });

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iFoundCar.setVisibility(View.GONE);
            }
        });

        imgPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0)

                {
                    --position;
                    carAvailibility(position);
                    imgNext.setAlpha((float) 1);
                    if (position == 0)
                        //imgPrevious.setVisibility(View.INVISIBLE);
                        imgPrevious.setAlpha((float) 0.4);
                } else {
                    imgPrevious.setAlpha((float) 0.4);
                }
            }
        });

        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position < (car_availibility_mode_size - 1)) {
                    ++position;
                    carAvailibility(position);
                    imgPrevious.setAlpha((float) 1);

                    if (position == (car_availibility_mode_size - 1))
                        imgNext.setAlpha((float) 0.4);

                } else {
                    imgNext.setAlpha((float) 0.4);

                }

            }

        });
        txtselectdep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtSelDeparture.setPaintFlags(txtSelDeparture.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                bookNowChargeDialog.setVisibility(View.GONE);
                pickDeparture();

            }
        });
        txtselectarr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtSelDeparture.setPaintFlags(txtSelDeparture.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                bookNowChargeDialog.setVisibility(View.GONE);
                pickArrival();

            }
        });

        txtSelDeparture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtSelDeparture.setPaintFlags(txtSelDeparture.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                bookNowChargeDialog.setVisibility(View.GONE);
                pickDeparture();
            }
        });

        txtSeleArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtSeleArrival.setPaintFlags(txtSeleArrival.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                bookNowChargeDialog.setVisibility(View.GONE);
                pickArrival();
            }
        });
        btnMakeBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag == 0) {
                    Toast.makeText(mActivity, R.string.txt_plz_select_atleast_on_gobase, Toast.LENGTH_SHORT).show();
                } else if (carAvailabilityFlag == 0) {
                    Toast.makeText(mActivity, R.string.txt_no_car_avaliable_for_this_gobase, Toast.LENGTH_SHORT).show();
                } else {
                    btnNavigation.setVisibility(View.GONE);
                    NavigationDialog.setVisibility(View.GONE);
                    LatLng latlong = new LatLng(pinLat, pinlong);
                    flag = 0;
                    int x = (int) getResources().getDimension(R.dimen.dpx);
                    int y = (int) getResources().getDimension(R.dimen.dpy);
                    animateLatLngZoom(latlong, 14, x, y);
                    makeBooking();
                }
            }
        });
        //markerList.add(Double.parseDouble(Dashboard_Activity.latitude),Double.parseDouble(Dashboard_Activity.longitude));

        mMap = mapFragment.getMap();

        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!is_departure_selected)
                    CommonUtilities.alertdialog(act, "Please select your departure date");
                else if (!is_arrival_selected)
                    CommonUtilities.alertdialog(act, "Please select your arrival date");
                else {
                    if (isDateValid()) {

                        booking_detail.customer_id = CommonUtilities.getPreference(act, CommonUtilities.pref_customer_id);
                        if (booking_flag == true) {
                            Gson gson = new Gson();
                            String detail = gson.toJson(booking_detail);
                            CommonUtilities.RemovePreference(act, CommonUtilities.pref_booking_detail);
                            CommonUtilities.setPreference(mActivity, CommonUtilities.booking_detail_authorise, detail);
                            //booking_flag = false;
                            Intent i = new Intent(act, booking_autorised_driver.class);
                            startActivityForResult(i, 0);
                        } else {
                            String C_id = CommonUtilities.getPreference(act, CommonUtilities.pref_customer_id);
                            if (TextUtils.isEmpty(C_id)) {

                                Gson gson = new Gson();
                                String detail = gson.toJson(booking_detail);
                                // CommonUtilities.RemovePreference(act, CommonUtilities.pref_booking_detail);
                                CommonUtilities.setPreference(mActivity, CommonUtilities.booking_detail_authorise, detail);
                                CommonUtilities.setPreference(mActivity, CommonUtilities.pref_booking_detail, detail);

                                Intent i = new Intent(act, booking_autorised_driver.class);
                                startActivityForResult(i, 0);
                            } else {
                                if (CommonUtilities.isConnectingToInternet(act)) {
                                    new ResponseModel().execute();
                                } else
                                    CommonUtilities.alertdialog(act,
                                            getString(R.string.val_internet));

                            }
                        }

                    } else {
                        CommonUtilities.alertdialog(act, "Please select a valid arrival date / time");
                    }
                }
            }
        });
        // Inflate the laxcvyout for this fragment

        transparentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transparentView.setVisibility(View.GONE);
                BlackG.setVisibility(View.VISIBLE);
            }
        });


        autoComplete_searchtext = (AutoCompleteTextView) v.findViewById(R.id.search_text);
        BlackG.setVisibility(View.VISIBLE);

        NavigationDialog.setVisibility(View.GONE);
        transparentView.setVisibility(View.GONE);

        autoComplete_searchtext.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {

                                                       }
                                                   }
        );

        BlackG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transparentView.setVisibility(View.VISIBLE);
                BlackG.setVisibility(View.GONE);
            }
        });
        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtilities.hideSoftKeyboard(act);
                autoComplete_searchtext.setVisibility(View.GONE);

                if (!checkLocationPermission()) {
                    requestCameraPermission();
                } else {
                    gps = new GPSTracker(act);

                    if (gps.canGetLocation()) {

                        transparentView.setVisibility(View.GONE);
                        BlackG.setVisibility(View.GONE);
                        btnMakeBooking.setVisibility(View.GONE);
                        btn_gobas.setSelected(true);
                        btn_favouritebas.setSelected(false);
                        if (mMap!=null && !NavigationDialog_visibility) {


                            if (CommonUtilities.isConnectingToInternet(act)) {

                                new NearGobaseModel().execute();
                            } else {

                                CommonUtilities.alertdialog(act, getString(R.string.val_internet));
                            }


                        } else {

                            NavigationDialog.setVisibility(View.GONE);
                            btnMakeBooking.setVisibility(View.VISIBLE);

                            BlackG.setVisibility(View.VISIBLE);
                            NavigationDialog_visibility = false;
                        }
                    } else {
                        showSettingsAlert(false);
                    }
                }

            }
        });

        autoComplete_searchtext.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {

                CommonUtilities.hideSoftKeyboard(act);

                autoComplete_searchtext.setVisibility(View.GONE);

                if (booknowDialog.getVisibility() == View.VISIBLE) {
                    btnNavigation.setVisibility(View.GONE);
                } else {
                    btnNavigation.setVisibility(View.VISIBLE);
                }
                if (go_base_obj != null) {
                    flag = 1;
                    for (int i = 0; i < go_base_obj.response.bases_info.all_bases.size(); i++) {
                        String search_name = autoComplete_searchtext.getText().toString();
                        String base_name = go_base_obj.response.bases_info.all_bases.get(i).base_name;
                        if (search_name.equals(base_name)) {
                            double latitude = Double.valueOf(go_base_obj.response.bases_info.all_bases.get(i).latitude);
                            double longitude = Double.valueOf(go_base_obj.response.bases_info.all_bases.get(i).longitude);

                            if (markerGoBaseList.get(i).getPosition().latitude == latitude && markerGoBaseList.get(i).getPosition().longitude == longitude) {
                                /*if (booknowDialog.getVisibility() != View.VISIBLE)
                                    markerGoBaseList.get(i).showInfoWindow();*/
                                if (Integer.valueOf(go_base_obj.response.bases_info.all_bases.get(i).cars_count) == 0) {
                                    Toast.makeText(mActivity, R.string.txt_no_car_avaliable_for_this_gobase, Toast.LENGTH_SHORT).show();

                                }
                            }

                            if (Integer.valueOf(go_base_obj.response.bases_info.all_bases.get(i).cars_count) > 0) {
                                booking_detail.base_name = go_base_obj.response.bases_info.all_bases.get(i).base_name;
                                booking_detail.address_line1 = go_base_obj.response.bases_info.all_bases.get(i).address_line1;
                                booking_detail.base_id = go_base_obj.response.bases_info.all_bases.get(i).base_id;
                                booking_detail.base_type = go_base_obj.response.bases_info.all_bases.get(i).base_type;
                                booking_detail.car_types = go_base_obj.response.bases_info.all_bases.get(i).car_types;
                                booking_detail.selected_base_lat = go_base_obj.response.bases_info.all_bases.get(i).latitude;
                                booking_detail.selected_base_long = go_base_obj.response.bases_info.all_bases.get(i).longitude;
                                strGobaseName = booking_detail.base_name;
                                strGoBaseAddress = booking_detail.address_line1;
                                gobaseName.setText(strGobaseName);
                                gobaseLocationName.setText(strGoBaseAddress);
                                booking_flag = false;
                                mLayout.setVisibility(View.GONE);
                                carmodel(booking_detail.car_types);


                                makeBooking();
                            } else {
                                if (booknowDialog.getVisibility() == View.VISIBLE) {
                                    Toast.makeText(mActivity, R.string.txt_no_car_avaliable_for_this_gobase, Toast.LENGTH_SHORT).show();
                                    btnNavigation.setVisibility(View.VISIBLE);
                                    btnMakeBooking.setVisibility(View.VISIBLE);
                                    BlackG.setVisibility(View.VISIBLE);
                                }
                            }


                            if (booknowDialog.getVisibility() == View.VISIBLE && bookNowChargeDialog.getVisibility() == View.GONE) {
                                LatLng latlong = new LatLng(latitude, longitude);
                                int x = (int) getResources().getDimension(R.dimen.dpx);
                                int y = (int) getResources().getDimension(R.dimen.dpy);
                                animateLatLngZoom(latlong, 14, x, y);
                            } else {
                                zoomMap(latitude, longitude);
                                refresh_page();
                            }
                        }


                    }
                }
            }


//                new ResponseModel().execute();

        });
        autoComplete_searchtext.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

        btn_favouritebas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btn_favouritebas.isSelected()) {

                    btn_favouritebas.setSelected(true);
                    btn_gobas.setSelected(false);

                    if (go_nearbaselist != null && go_nearbaselist.response.bases_info.favourite_bases != null && go_nearbaselist.response.bases_info.favourite_bases.size() > 0) {


                        Favourite_gobase_model model = new Favourite_gobase_model(act, go_nearbaselist.response.bases_info.favourite_bases);
                        val_empty_gobase.setVisibility(View.GONE);
                        list_gobase.setAdapter(model);

                    } else {

                        ArrayList<go_bases.response.bases_info.favourite_bases> favourite_bases = new ArrayList<go_bases.response.bases_info.favourite_bases>();
                        Favourite_gobase_model model = new Favourite_gobase_model(act, favourite_bases);
                        val_empty_gobase.setVisibility(View.VISIBLE);
                        val_empty_gobase.setText("No My favourite GoBase found");

                        list_gobase.setAdapter(model);

                    }
                }
            }
        });
        btn_gobas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btn_gobas.isSelected()) {
                    val_empty_gobase.setVisibility(View.GONE);

                    btn_gobas.setSelected(true);
                    btn_favouritebas.setSelected(false);


                    if (go_nearbaselist != null && go_nearbaselist.response.bases_info.all_bases != null && go_nearbaselist.response.bases_info.all_bases.size() > 0) {


                        Go_base_model model = new Go_base_model(act, go_nearbaselist.response.bases_info.all_bases);
                        list_gobase.setAdapter(model);

                    } else {

                        ArrayList<go_bases.response.bases_info.all_bases> all_bases = new ArrayList<go_bases.response.bases_info.all_bases>();
                        Go_base_model model = new Go_base_model(act, all_bases);
                        list_gobase.setAdapter(model);
                        val_empty_gobase.setVisibility(View.VISIBLE);
                        val_empty_gobase.setText("No Nearby GoBase found");


                    }

                }

            }
        });
        temp_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgNext.setAlpha((float) 1);
                imgPrevious.setAlpha((float) 1);
                mLayout.setVisibility(View.GONE);
                booknowDialog.setVisibility(View.VISIBLE);
            }
        });

        setHasOptionsMenu(true);
        return v;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 21:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    btnNavigation.performClick();

                } else {
                    CommonUtilities.ShowToast(mActivity, "Permission Denied booking");
                    if (!checkLocationPermission()) {
                        requestCameraPermission();
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(act)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onActivityResultCalledBeforeOnResume = true;
        switch (requestCode) {
            case 1:
                btnNavigation.performClick();
                break;
        }

    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 21);

    }

    private boolean checkLocationPermission() {
        int result = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (Dashboard_Activity) activity;
    }

    public void carmodel(String type) {

        Gson gson1 = new Gson();
        String json1 = CommonUtilities.getPreference(mActivity, CommonUtilities.pref_car_type);
        car_obj = gson1.fromJson(json1, car_types.class);
        Log.e("type,",type);
        if(car_obj!=null && car_obj.response!=null && car_obj.response.car_types_info!=null)
            car_obj.response.car_types_info.clear();
        if (!TextUtils.isEmpty(type) && cartype!=null) {
            String cartypeid[] = type.split(",");
            for (int j = 0; j < cartype.response.car_types_info.size(); j++) {

                for (int j1 = 0; j1 < cartypeid.length; j1++) {
                    if (cartypeid[j1].equals(cartype.response.car_types_info.get(j).car_type_id)) {
                        car_obj.response.car_types_info.add(cartype.response.car_types_info.get(j));
                    }
                }
            }
        }

        if (car_obj != null) {
            car_layout.removeAllViews();
            for (car_model_pos = 0; car_model_pos < car_obj.response.car_types_info.size(); car_model_pos++) {


                final View convertView = LayoutInflater.from(mActivity).inflate(R.layout.car_type_row, null);
                ImageView i = (ImageView) convertView.findViewById(R.id.imageView);
                TextView txtCarType = (TextView) convertView.findViewById(R.id.textCarType);
                Picasso.with(mActivity)
                        .load(CommonUtilities.CarTypeImageURL + car_obj.response.getCar_types_info().get(car_model_pos).getCar_image()).into(i);
                convertView.setId(Integer.valueOf(car_obj.response.car_types_info.get(car_model_pos).getCar_type_id()));

                txtCarType.setText(car_obj.response.getCar_types_info().get(car_model_pos).getType_name());
                txtCarType.setTypeface(null, Typeface.BOLD);
                txtCarType.setTextColor(Color.parseColor("#3c3c3c"));


                if (car_model_pos == 0) {
                    convertView.setBackgroundResource(R.mipmap.car_selection_bg);
                    booking_detail.car_type_id = car_obj.response.car_types_info.get(car_model_pos).car_type_id;
                    booking_detail.car_type_name = car_obj.response.car_types_info.get(car_model_pos).getType_name();
                    booking_detail.car_type_img = car_obj.response.car_types_info.get(car_model_pos).getCar_image();
                }


                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  CommonUtilities.ShowToast(act, "" + convertView.getId());
                        for (int i = 0; i < car_obj.response.car_types_info.size(); i++) {

                            if (convertView.getId() == car_layout.getChildAt(i).getId()) {
                                ((View) car_layout.getChildAt(i)).setBackgroundResource(R.mipmap.car_selection_bg);
                                booking_detail.car_type_id = car_obj.response.car_types_info.get(i).car_type_id;
                                booking_detail.car_type_name = car_obj.response.car_types_info.get(i).getType_name();
                                booking_detail.car_type_img = car_obj.response.car_types_info.get(i).getCar_image();
                                booking_flag = false;
                                bookNowChargeDialog.setVisibility(View.GONE);
                                layout_carAvailibity.setVisibility(View.GONE);
                                other_car_type_layout.setVisibility(View.GONE);
                                txtCost.setText("");

                            } else {
                                ((View) car_layout.getChildAt(i)).setBackgroundResource(0);
                            }
                        }

                    }
                });
                car_layout.addView(convertView);
            }
        }
        time_date_car_click(true);
    }

    public void showSettingsAlert(final boolean iscall) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(act);

        // Setting Dialog Title
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setIcon(R.mipmap.app_icon);

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

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
                if (mMap!=null && iscall)
                    if (CommonUtilities.isConnectingToInternet(act)) {
                        new allGobaseModel().execute();
                    } else {

                        CommonUtilities.alertdialog(act, getString(R.string.val_internet));
                    }

            }
        });

        // Showing Alert Message
        if(!mActivity.isFinishing())
            alertDialog.show();
    }

    private void check_user_state() {
        String car_type_id = "";
        Gson gson1 = new Gson();
        String json1 = CommonUtilities.getPreference(act, CommonUtilities.pref_booking_detail);
        BookingDetail obj = gson1.fromJson(json1, BookingDetail.class);
        if (obj != null) {

            booking_detail = obj;
            flag = 0;
            btnNavigation.setVisibility(View.GONE);
            Customer_id = CommonUtilities.getPreference(act, CommonUtilities.pref_customer_id);
            obj.customer_id = CommonUtilities.getPreference(act, CommonUtilities.pref_customer_id);
            bookNowChargeDialog.setVisibility(View.GONE);
            booknowDialog.setVisibility(View.VISIBLE);
            gobaseName.setText(obj.base_name);
            gobaseLocationName.setText(obj.address_line1);
            txtSeleArrival.setText(obj.formated_arrival_time);
            txtSeleArrival.setTextColor((getResources().getColor(R.color.lime)));

            txtSelDeparture.setText(obj.formated_departure_time);
            txtSelDeparture.setTextColor((getResources().getColor(R.color.lime)));

            txtselectdep.setVisibility(View.GONE);
            txtselectarr.setVisibility(View.GONE);
            is_departure_selected = true;
            is_arrival_selected = true;
            car_type_id = obj.car_type_id;
            carmodel(obj.car_types);
            if (car_obj != null) {
                for (int i = 0; i < car_obj.response.car_types_info.size(); i++) {
                    if (car_type_id.equals(String.valueOf(car_layout.getChildAt(i).getId()))) {
                        ((View) car_layout.getChildAt(i)).setBackgroundResource(R.mipmap.car_selection_bg);
                        booking_detail.car_type_id = car_obj.response.car_types_info.get(i).car_type_id;
                        booking_detail.car_type_name = car_obj.response.car_types_info.get(i).getType_name();
                        booking_detail.car_type_img = car_obj.response.car_types_info.get(i).getCar_image();
                    } else {
                        ((View) car_layout.getChildAt(i)).setBackgroundResource(0);
                    }
                }
            }
        }
    }

    private boolean isDateValid() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String departure = booking_detail.departure_time;
        Date date_departure = null;


        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String Curent_date = df.format(c.getTime());
        Date curent_date = null;


        try {
            curent_date = df.parse(Curent_date);
            date_departure = formatter.parse(departure);


            String arrival = booking_detail.arrival_time;
            Date date_arrival = formatter.parse(arrival);

            if (date_departure.before(curent_date)) {
                return false;
            }

            if (date_departure.equals(curent_date) || date_departure.after(curent_date)) {
                if (date_departure.compareTo(date_arrival) < 0) {
                    return true;
                }
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void refresh_page() {

        autoComplete_searchtext.setVisibility(View.GONE);
        menu.findItem(R.id.action_edit).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(true);
        CommonUtilities.getPreference(act, CommonUtilities.pref_customer_id);
        NavigationDialog.setVisibility(View.GONE);
        btnMakeBooking.setVisibility(View.VISIBLE);
        btnNavigation.setVisibility(View.VISIBLE);
        booknowDialog.setVisibility(View.GONE);
        BlackG.setVisibility(View.VISIBLE);
        CommonUtilities.hideSoftKeyboard(mActivity);
        is_departure_selected = false;
        txtSelDeparture.setText(getString(R.string.txt_Select_Your_Departure));
        booking_flag = false;
        txtselectdep.setVisibility(View.VISIBLE);
        txtselectarr.setVisibility(View.VISIBLE);
        is_arrival_selected = false;
        txtSeleArrival.setText(getString(R.string.txt_Select_Your_Arrival));
        NavigationDialog_visibility = false;
    }

    public void time_date_car_click(boolean status) {
        txtSeleArrival.setClickable(status);
        txtSelDeparture.setClickable(status);
        car_layout.setFocusable(status);
        car_layout.setClickable(status);
        if (car_obj != null)
            for (int i = 0; i < car_obj.response.car_types_info.size(); i++) {
                ((View) car_layout.getChildAt(i)).setClickable(status);
            }
    }

    private void carAvailibility(int pos) {
        menu.findItem(R.id.action_edit).setVisible(false);

        LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        layout_carAvailibity.setVisibility(View.VISIBLE);
        final Check_trip.response.trip_info.Trips_details info = trip_model.response.trip_info.Trips_details.get(pos);
        basename.setText(info.base_details.base_name);
        baseaddress.setText(info.base_details.address_line1);
        detailheader.setText(info.base_details.message);

        if (info.base_details.is_available == 0) {
            alertIcon.setVisibility(View.VISIBLE);
            headerMsg.setBackgroundColor(getResources().getColor(R.color.orange));
        } else {
            headerMsg.setBackgroundColor(getResources().getColor(R.color.lime));
            alertIcon.setVisibility(View.INVISIBLE);
        }

        Picasso.with(act)
                .load(CommonUtilities.CarTypeImageURL + info.car_details.car_type_image)
                .error(R.mipmap.car_image).placeholder(R.mipmap.car_image).into(carIcon);
        carname.setText(info.car_details.type_name);

        CommonUtilities.setFontFamily(act, basename, CommonUtilities.GillSansStd);
        CommonUtilities.setFontFamily(act, baseaddress, CommonUtilities.GillSansStd);
        CommonUtilities.setFontFamily(act, detailheader, CommonUtilities.GillSansStd_Light);
        CommonUtilities.setFontFamily(act, carname, CommonUtilities.Roboto_Light);


        slotdetail.removeAllViews();
        if (info.slot_details != null) {
            if (info.slot_details.size() > 0) {
                slotdetail.setVisibility(View.VISIBLE);
                booknowDialog.setVisibility(View.GONE);
                for (slotPos = 0; slotPos < info.slot_details.size(); slotPos++) {
                    View slotview = inflater.inflate(R.layout.slotdetail, slotdetail, false);
                    final TextView fromDate = (TextView) slotview.findViewById(R.id.fromDate);
                    final LinearLayout slot_linear = (LinearLayout) slotview.findViewById(R.id.slot_linear);

                    TextView fromTo = (TextView) slotview.findViewById(R.id.fromTo);
                    TextView toTime = (TextView) slotview.findViewById(R.id.toTime);
                    fromTo.setTextColor(Color.parseColor("#ffa500"));
                    toTime.setTextColor(Color.parseColor("#ffa500"));

                    TextView toDate = (TextView) slotview.findViewById(R.id.toDate);

                    slot_linear.setId(slotPos);


                    String[] s_start = info.slot_details.get(slotPos).suggest_start.split("\\s");
                    final String[] start_time = s_start[1].split(":");

                    String[] s_end = info.slot_details.get(slotPos).suggest_stop.split("\\s");
                    final String[] end_time = s_end[1].split(":");

                    // String Deptime = String.format("%02dh%02d", hourOfDay, minute);
                    fromDate.setText(info.slot_details.get(slotPos).suggest_start);
                    fromTo.setText(start_time[0] + "h" + start_time[1]);
                    toDate.setText(info.slot_details.get(slotPos).suggest_stop);
                    toTime.setText(end_time[0] + "h" + end_time[1]);
                    fromDate.setText(DateFormating(info.slot_details.get(slotPos).suggest_start));
                    toDate.setText(DateFormating(info.slot_details.get(slotPos).suggest_stop));
                    SetSlotFont(fromDate, fromTo, toDate, toTime);

                    slot_linear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            int id = slot_linear.getId();
                            String start = trip_model.response.trip_info.Trips_details.get(position).slot_details.get(Integer.valueOf(slot_linear.getId())).suggest_start;
                            String end = trip_model.response.trip_info.Trips_details.get(position).slot_details.get(Integer.valueOf(slot_linear.getId())).suggest_stop;
                            String car_id = trip_model.response.trip_info.Trips_details.get(position).car_details.car_type_id;
                            String base_id = trip_model.response.trip_info.Trips_details.get(position).base_details.base_id;
                            booking_detail.formated_departure_time = DateFormating(start) + " " + start_time[0] + "h" + start_time[1];
                            booking_detail.formated_arrival_time = DateFormating(end) + " " + end_time[0] + "h" + end_time[1];


                            booking_detail.departure_time = start;
                            booking_detail.arrival_time = end;
                            booking_detail.car_id = car_id;
                            booking_detail.base_id = base_id;
                            txtSelDeparture.setText(booking_detail.formated_departure_time);
                            txtSeleArrival.setText(booking_detail.formated_arrival_time);


                            booking_detail.base_name = trip_model.response.trip_info.Trips_details.get(position).base_details.base_name;
                            booking_detail.address_line1 = trip_model.response.trip_info.Trips_details.get(position).base_details.address_line1;
                            gobaseName.setText(" " + booking_detail.base_name);
                            gobaseLocationName.setText(booking_detail.address_line1);
                            booking_flag = false;
                            for (int i = 0; i < car_obj.response.car_types_info.size(); i++) {
                                if (car_id.equals(String.valueOf(car_layout.getChildAt(i).getId()))) {
                                    ((View) car_layout.getChildAt(i)).setBackgroundResource(R.mipmap.car_selection_bg);
                                    break;
                                } else {
                                    ((View) car_layout.getChildAt(i)).setBackgroundResource(0);
                                }
                            }
                            booknowDialog.setVisibility(View.VISIBLE);
                            mLayout.setVisibility(View.GONE);
                            String s1 = start + "  " + end + "car id: " + car_id + " base id: " + base_id;
                            btnBookNow.performClick();
                            imgNext.setAlpha((float) 1);
                            imgPrevious.setAlpha((float) 1);
                        }


                    });
                    slotdetail.addView(slotview);

                }

            } else

            {
                slotdetail.setVisibility(View.GONE);

            }
        } else {

            if (info.base_details.is_available == 0) {
                View view1 = inflater.inflate(R.layout.slotdetail, slotdetail, false);

                final TextView fromDate = (TextView) view1.findViewById(R.id.fromDate);

                TextView fromTo = (TextView) view1.findViewById(R.id.fromTo);
                final LinearLayout slot_linear = (LinearLayout) view1.findViewById(R.id.slot_linear);

                TextView toDate = (TextView) view1.findViewById(R.id.toDate);
                TextView toTime = (TextView) view1.findViewById(R.id.toTime);
                fromTo.setTextColor(Color.parseColor("#ffa500"));
                toTime.setTextColor(Color.parseColor("#ffa500"));


                String[] s_start = booking_detail.departure_time.split("\\s");
                final String[] start_time = s_start[1].split(":");

                String[] s_end = booking_detail.arrival_time.split("\\s");
                final String[] end_time = s_end[1].split(":");


                fromDate.setText(booking_detail.departure_time);
                fromTo.setText(start_time[0] + "h" + start_time[1]);
                toDate.setText(booking_detail.arrival_time);
                toTime.setText(end_time[0] + "h" + end_time[1]);
                fromDate.setText(DateFormating(booking_detail.departure_time));
                toDate.setText(DateFormating(booking_detail.arrival_time));
                slot_linear.setId(slotPos);
                SetSlotFont(fromDate, fromTo, toDate, toTime);
                slotdetail.addView(view1);

            }
        }

        if (info.other_car_types != null) {
            if (info.other_car_types.size() > 0) {
                other_car_type_layout.setVisibility(View.VISIBLE);
                other_car_type_layout.removeAllViews();
                for (int otherPos = 0; otherPos < info.other_car_types.size(); otherPos++) {


                    View view = inflater.inflate(R.layout.other_car_layout, other_car_type_layout, false);
                    final LinearLayout slot_linear = (LinearLayout) view.findViewById(R.id.slot_linear);

                    final TextView fromDate = (TextView) view.findViewById(R.id.fromDate);


                    TextView toDate = (TextView) view.findViewById(R.id.toDate);
                    TextView toTime = (TextView) view.findViewById(R.id.toTime);
                    TextView fromTo = (TextView) view.findViewById(R.id.fromTo);

                    fromTo.setTextColor(Color.parseColor("#8DC73F"));
                    toTime.setTextColor(Color.parseColor("#8DC73F"));

                    String[] s_start = info.other_car_types.get(otherPos).suggestion.suggest_start.split("\\s");
                    final String[] start_time = s_start[1].split(":");

                    String[] s_end = info.other_car_types.get(otherPos).suggestion.suggest_stop.split("\\s");
                    final String[] end_time = s_end[1].split(":");


                    fromDate.setText(DateFormating(info.other_car_types.get(otherPos).suggestion.suggest_start));
                    fromTo.setText(start_time[0] + "h" + start_time[1]);
                    toDate.setText(DateFormating(info.other_car_types.get(otherPos).suggestion.suggest_stop));
                    toTime.setText(end_time[0] + "h" + end_time[1]);

                    TextView other_header = (TextView) view.findViewById(R.id.other_header);
                    ImageView othercarIcon = (ImageView) view.findViewById(R.id.othercarIcon);
                    TextView carname = (TextView) view.findViewById(R.id.carname);
                    other_header.setText(info.other_car_types.get(otherPos).message);
                    carname.setText(info.other_car_types.get(otherPos).car_details.type_name);


                    Picasso.with(act)
                            .load(CommonUtilities.CarTypeImageURL + info.other_car_types.get(otherPos).car_details.car_type_image)
                            .error(R.mipmap.car_image).placeholder(R.mipmap.car_image).into(othercarIcon);
                    slot_linear.setId(otherPos);


                    SetSlotFont(fromDate, fromTo, toDate, toTime);

                    CommonUtilities.setFontFamily(act, carname, CommonUtilities.Roboto_Light);
                    CommonUtilities.setFontFamily(act, other_header, CommonUtilities.GillSansStd_Light);

                    slot_linear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            int id = slot_linear.getId();
                            String start = trip_model.response.trip_info.Trips_details.get(position).other_car_types.get(Integer.valueOf(slot_linear.getId())).suggestion.suggest_start;
                            String end = trip_model.response.trip_info.Trips_details.get(position).other_car_types.get(Integer.valueOf(slot_linear.getId())).suggestion.suggest_stop;
                            String car_id = trip_model.response.trip_info.Trips_details.get(position).other_car_types.get(Integer.valueOf(slot_linear.getId())).car_details.car_type_id;
                            String base_id = trip_model.response.trip_info.Trips_details.get(position).base_details.base_id;
                            String s1 = start + "  " + end + "car id: " + car_id + " base id: " + base_id;

                            booking_detail.formated_departure_time = DateFormating(start) + " " + start_time[0] + "h" + start_time[1];
                            booking_detail.formated_arrival_time = DateFormating(end) + " " + end_time[0] + "h" + end_time[1];
                            booking_detail.departure_time = start;
                            booking_detail.arrival_time = end;
                            booking_detail.car_id = car_id;
                            booking_detail.base_id = base_id;


                            carmodel(car_id);

                            String detail = gson.toJson(booking_detail);
                            CommonUtilities.setPreference(mActivity, CommonUtilities.pref_booking_detail, detail);
                            mLayout.setVisibility(View.GONE);


                            booking_detail.base_name = trip_model.response.trip_info.Trips_details.get(position).base_details.base_name;
                            booking_detail.address_line1 = trip_model.response.trip_info.Trips_details.get(position).base_details.address_line1;
                            gobaseName.setText(" " + booking_detail.base_name);
                            gobaseLocationName.setText(booking_detail.address_line1);
                            booking_flag = false;

                            // layout_carAvailibity.setVisibility(View.GONE);
                            // slotdetail.setVisibility(View.GONE);
                            booknowDialog.setVisibility(View.VISIBLE);
                            txtSelDeparture.setText(booking_detail.formated_departure_time);
                            txtSeleArrival.setText(booking_detail.formated_arrival_time);
                            for (int i = 0; i < car_obj.response.car_types_info.size(); i++) {


                                if (booking_detail.car_id.equals(String.valueOf(car_layout.getChildAt(i).getId()))) {
                                    ((View) car_layout.getChildAt(i)).setBackgroundResource(R.mipmap.car_selection_bg);
                                    break;
                                } else {
                                    ((View) car_layout.getChildAt(i)).setBackgroundResource(0);
                                }

                            }
                            booknowDialog.setVisibility(View.VISIBLE);
                            btnBookNow.performClick();
                            imgNext.setAlpha((float) 1);
                            imgPrevious.setAlpha((float) 1);
                        }
                    });
                    other_car_type_layout.addView(view);
                }
            }
        } else {
            other_car_type_layout.setVisibility(View.GONE);
        }


    }

    public String DateFormating(String Date) {
        String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String[] DateString = Date.split(" ");

        String[] _Date = DateString[0].split("/");

        int year = Integer.parseInt(_Date[2]);
        int month = Integer.parseInt(_Date[1]);
        int day = Integer.parseInt(_Date[0]);


        return DateFormat.format("EEEE", new Date(year, month - 1, day - 1)).toString() + ", " + String.valueOf(MONTHS[month - 1]).toString() + ". " + String.valueOf(day).toString();

    }

    public void SetSlotFont(TextView fromDate, TextView fromTo, TextView toDate, TextView toTime) {
        CommonUtilities.setFontFamily(act, fromDate, CommonUtilities.GillSansStd);
        CommonUtilities.setFontFamily(act, fromTo, CommonUtilities.GillSansStd);
        CommonUtilities.setFontFamily(act, toDate, CommonUtilities.GillSansStd);
        CommonUtilities.setFontFamily(act, toTime, CommonUtilities.GillSansStd);
    }

    public void makeBooking() {
        flag = 0;
        btnNavigation.setVisibility(View.GONE);
        Customer_id = CommonUtilities.getPreference(act, CommonUtilities.pref_customer_id);
        booking_detail.customer_id = CommonUtilities.getPreference(act, CommonUtilities.pref_customer_id);
        bookNowChargeDialog.setVisibility(View.GONE);
        booknowDialog.setVisibility(View.VISIBLE);
        gobaseName.setText(strGobaseName);
        gobaseLocationName.setText(strGoBaseAddress);


    }

    private void zoomMap(Double latitude, Double longitude) {
        LatLng latlong = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlong));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);

    }

    @Override
    public void onMapClick(LatLng latLng) {
        menu.findItem(R.id.action_edit).setVisible(false);
        autoComplete_searchtext.setVisibility(View.GONE);
        booknowDialog.setVisibility(View.GONE);
        transparentView.setVisibility(View.GONE);
        CommonUtilities.getPreference(act, CommonUtilities.pref_customer_id);
        NavigationDialog.setVisibility(View.GONE);
        btnMakeBooking.setVisibility(View.VISIBLE);
        btnNavigation.setVisibility(View.VISIBLE);
        BlackG.setVisibility(View.VISIBLE);
        CommonUtilities.hideSoftKeyboard(mActivity);
        is_departure_selected = false;
        txtSelDeparture.setText(getString(R.string.txt_Select_Your_Departure));
        booking_flag = false;
        txtselectdep.setVisibility(View.VISIBLE);
        txtselectarr.setVisibility(View.VISIBLE);
        is_arrival_selected = false;
        txtSeleArrival.setText(getString(R.string.txt_Select_Your_Arrival));
        layout_carAvailibity.setVisibility(View.GONE);
        other_car_type_layout.setVisibility(View.GONE);
        CommonUtilities.RemovePreference(act, CommonUtilities.pref_booking_detail);
        NavigationDialog_visibility = false;

        sdYear = c.get(Calendar.YEAR);
        sdMonth = c.get(Calendar.MONTH);
        sdDay = c.get(Calendar.DAY_OF_MONTH);

        saYear = c.get(Calendar.YEAR);
        saMonth = c.get(Calendar.MONTH);
        saDay = c.get(Calendar.DAY_OF_MONTH);

        sHour = c.get(Calendar.HOUR_OF_DAY);
        sMinute = c.get(Calendar.MINUTE);

    }

    public void alertdialog(Context context, String msg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(R.string.app_name);
        builder1.setIcon(R.mipmap.app_icon);
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                Intent i = new Intent(act, Signup_Membership.class);
                i.putExtra("fromSignup", false);
                startActivityForResult(i, 0);
            }
        });

        AlertDialog alert11 = builder1.create();
        if(!mActivity.isFinishing())
            alert11.show();
    }

    public void pickDeparture() {

        final int mCYear = c.get(Calendar.YEAR);
        final int mCMonth = c.get(Calendar.MONTH);
        final int mCDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(mActivity,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener() {
                    String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

                    @Override
                    public void onDateSet(DatePicker picker, int year, int monthOfYear, int dayOfMonth) {
                        // onDateChangedListner is not working in android 5.0+
                        if (year < mCYear || (monthOfYear < mCMonth && year == mCYear) || (dayOfMonth < mCDay && year == mCYear
                                && monthOfYear == mCMonth)) {
                            //CommonUtilities.showSnackbar(view, "You can not set previous date.");
                            Toast.makeText(mActivity, R.string.txt_you_can_not_select_past_dates, Toast.LENGTH_SHORT).show();
                        } else {

                            select_departure_date = DateFormat.format("EEEE", new Date(year, monthOfYear, dayOfMonth - 1)).toString() + ", " + String.valueOf(MONTHS[monthOfYear]).toString() + ". " + String.valueOf(dayOfMonth).toString();

                           // booking_date = (dayOfMonth) + "/" + (monthOfYear + 1) + "/" + year;
                            booking_date = String.format("%02d/%02d", dayOfMonth, (monthOfYear + 1)) + "/" + year;
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
                            try {
                                Date select_date = sdf.parse((dayOfMonth) + "/" + (monthOfYear) + "/" + year);
                                Date current_date = sdf.parse(mCDay + "/" + mCMonth + "/" + mCYear);

                                boolean dateEqual = isCurrentDate(select_date, current_date);
                                sdYear = year;
                                sdMonth = monthOfYear;
                                sdDay = dayOfMonth;

                                if (!txtSelDeparture.getText().toString().equals("departure")) {
                                    c = Calendar.getInstance();
                                    String arr = txtSelDeparture.getText().toString();
                                    int cdHour = Integer.parseInt(arr.substring(arr.length() - 5, arr.length() - 3));
                                    int cdMinute = Integer.parseInt(arr.substring(arr.length() - 2));
                                    Log.e("cdHour",cdHour+" " + cdMinute + "shour" + sHour + " " + sMinute);
                                    if(cdHour>c.get(Calendar.HOUR_OF_DAY))
                                    {
                                        Log.e("enter","if");
                                        sHour = cdHour;
                                        sMinute = cdMinute;
                                    }
                                    else if(cdHour==c.get(Calendar.HOUR_OF_DAY) && cdMinute>=c.get(Calendar.MINUTE))
                                    {
                                        Log.e("enter","else if");
                                        sHour = cdHour;
                                        sMinute = cdMinute;
                                    }
                                    else
                                    {
                                        Log.e("enter","else");
                                        sHour = c.get(Calendar.HOUR_OF_DAY);
                                        sMinute = c.get(Calendar.MINUTE);
                                        Log.e("shour",sHour + " " + sMinute);
                                    }
                                }

                                if (versionRelease.equals("4.4.4")) {
                                    if(noOfTimesCalled%2==0) {
                                        show_timpepicker(0, dateEqual);
                                    }
                                    noOfTimesCalled++;
                                }else{
                                    show_timpepicker(0, dateEqual);
                                }


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                }, sdYear, sdMonth, sdDay) {
            @Override
            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                if (year < mCYear)
                    view.updateDate(mCYear, mCMonth, mCDay);

                if (monthOfYear < mCMonth && year == mCYear)
                    view.updateDate(mCYear, mCMonth, mCDay);

                if (dayOfMonth < mCDay && year == mCYear
                        && monthOfYear == mCMonth)
                    view.updateDate(mCYear, mCMonth, mCDay);
            }
        };
        dpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dpd.show();
    }

    public boolean isCurrentDate(Date select_date, Date current_date) {
        if (select_date.equals(current_date)) {
            return true;
        }
        return false;

    }

    public void pickArrival() {
        final int mCYear = c.get(Calendar.YEAR);
        final int mCMonth = c.get(Calendar.MONTH);
        final int mCDay = c.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");

        DatePickerDialog dpd = new DatePickerDialog(mActivity,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener() {
                    String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE");

                    @Override
                    public void onDateSet(DatePicker picker, int year, int monthOfYear, int dayOfMonth) {
                        // onDateChangedListner is not working in android 5.0+
                        if (year < mCYear || (monthOfYear < mCMonth && year == mCYear) || (dayOfMonth < mCDay && year == mCYear
                                && monthOfYear == mCMonth)) {
                            Toast.makeText(mActivity, R.string.txt_you_can_not_select_past_dates, Toast.LENGTH_SHORT).show();
                        } else {
                            select_arrive_date = DateFormat.format("EEEE", new Date(year, monthOfYear, dayOfMonth - 1)).toString() + ", " + String.valueOf(MONTHS[monthOfYear]).toString() + ". " + String.valueOf(dayOfMonth).toString();

                            //String Deptime = String.format("%02d/%02d", dayOfMonth, (monthOfYear + 1));

                            //booking_date = (dayOfMonth) + "/" + (monthOfYear + 1) + "/" + year;
                            booking_date = String.format("%02d/%02d", dayOfMonth, (monthOfYear + 1)) + "/" + year;
                            booknowDialog.setVisibility(View.VISIBLE);

                            SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
                            try {
                                Date select_date = sdf.parse((dayOfMonth) + "/" + (monthOfYear) + "/" + year);
                                Date current_date = sdf.parse(mCDay + "/" + mCMonth + "/" + mCYear);

                                boolean dateEqual = isCurrentDate(select_date, current_date);
                                saYear = year;
                                saMonth = monthOfYear;
                                saDay = dayOfMonth;

                                if (!txtSeleArrival.getText().toString().equals("arrival")) {
                                    c = Calendar.getInstance();
                                    String arr = txtSeleArrival.getText().toString();
                                    int cdHour = Integer.parseInt(arr.substring(arr.length() - 5, arr.length() - 3));
                                    int cdMinute = Integer.parseInt(arr.substring(arr.length() - 2));
                                    if(cdHour>c.get(Calendar.HOUR_OF_DAY))
                                    {
                                        sHour = cdHour;
                                        sMinute = cdMinute;
                                    }
                                    else if(cdHour==c.get(Calendar.HOUR_OF_DAY) && cdMinute>=c.get(Calendar.MINUTE))
                                    {
                                        sHour = cdHour;
                                        sMinute = cdMinute;
                                    }
                                    else
                                    {
                                        sHour = c.get(Calendar.HOUR_OF_DAY);
                                        sMinute = c.get(Calendar.MINUTE);
                                    }
                                }

                                if (versionRelease.equals("4.4.4")) {
                                    if(noOfTimesCalled%2==0) {
                                        show_timpepicker(1, dateEqual);
                                    }
                                    noOfTimesCalled++;
                                }else
                                    show_timpepicker(1, dateEqual);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                }, saYear, saMonth, saDay) {
            @Override
            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                if (year < mCYear)
                    view.updateDate(mCYear, mCMonth, mCDay);

                if (monthOfYear < mCMonth && year == mCYear)
                    view.updateDate(mCYear, mCMonth, mCDay);

                if (dayOfMonth < mCDay && year == mCYear
                        && monthOfYear == mCMonth)
                    view.updateDate(mCYear, mCMonth, mCDay);


            }
        };
        dpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dpd.show();
    }

    private void show_timpepicker(final int type, final boolean isDateEqual) {

        final int mHour = c.get(Calendar.HOUR_OF_DAY);
        final int mMinute = c.get(Calendar.MINUTE);

        tpd= new CustomTimePicker(mActivity, new TimePickerDialog.OnTimeSetListener(){

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        if (isDateEqual) {

                            if (hourOfDay < mHour) {
                                validTime = false;
                            } else if (hourOfDay == mHour && type==0) {
                                if (mMinute >= 00 && mMinute < 15) {
                                    validTime =minute >= 00;
                                }
                                else if(mMinute>=15 && mMinute<30)
                                {
                                    validTime = minute>=15;
                                    Log.e("validtime","else if " + validTime);
                                }
                                else if(mMinute>=30 && mMinute<45)
                                {
                                    validTime =minute>=30;
                                    Log.e("validtime","else if 2" + validTime);
                                }
                                else if(mMinute>=45)
                                {
                                    validTime = minute>=45;
                                    Log.e("validtime","else " + validTime);
                                }
                            }
                            else if(hourOfDay == mHour)
                            {
                                validTime = minute >= mMinute;
                            }
                            else {
                                validTime = true;
                            }

                            if (!validTime) {

                                if (versionRelease.equals("4.4.4")) {
                                    if(noOfTimesCalled%2==0) {
                                        CommonUtilities.ShowToast(act, "Time selections must be in the future");
                                        show_timpepicker(type, isDateEqual);
                                    }
                                    noOfTimesCalled++;
                                }else{
                                    CommonUtilities.ShowToast(act, "Time selections must be in the future");
                                    show_timpepicker(type, isDateEqual);
                                }
                            }
                            else
                            {
                                setDateLable(type, hourOfDay, minute);
                            }
                        } else {
                            booking_flag = false;
                            setDateLable(type, hourOfDay, minute);
                        }
                    }
                }, sHour, sMinute, true) {
            /*@Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay,
                                      int minute) {
                super.onTimeChanged(timePicker, hourOfDay, minute);
                if (isDateEqual) {

                    if (hourOfDay < mHour) {
                        validTime = false;
                    } else if (hourOfDay == mHour) {
                        validTime = minute >= mMinute;
                    } else if (hourOfDay == mHour) {
                        validTime = minute <= mMinute;
                    } else {
                        validTime = true;
                    }

                    if (validTime) {
                    } else {
                        updateTime(mHour, mMinute);
                    }
                }

            }*/
        };
        tpd.show();
    }

    private void setDateLable(int type, int hourOfDay, int minute) {
        if (type == 0) {
            txtselectdep.setVisibility(View.GONE);

            is_departure_selected = true;
            String Deptime = String.format("%02dh%02d", hourOfDay, minute);
            txtSelDeparture.setText(select_departure_date + "\n" + Deptime);
            txtSelDeparture.setTextColor((getResources().getColor(R.color.lime)));
            booking_detail.departure_time = booking_date + " " + String.format("%02d:%02d", hourOfDay, minute);


            booking_detail.formated_departure_time = select_departure_date + "\n" + String.format("%02dh%02d", hourOfDay, minute);
        }
        if (type == 1) {
            txtselectarr.setVisibility(View.GONE);
            String Arrtime = String.format("%02dh%02d", hourOfDay, minute);
            is_arrival_selected = true;
            txtSeleArrival.setText(select_arrive_date + "\n" + Arrtime);
            txtSeleArrival.setTextColor((getResources().getColor(R.color.lime)));


            booking_detail.arrival_time = booking_date + " " + String.format("%02d:%02d", hourOfDay, minute);
            booking_detail.formated_arrival_time = select_arrive_date + "\n" + String.format("%02dh%02d", hourOfDay, minute);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }
        if (CommonUtilities.isConnectingToInternet(act)) {
            if(!CommonUtilities.getPreference(act,CommonUtilities.pref_customer_id).equals(""))
            {
                if (CommonUtilities.isConnectingToInternet(act)) {
                    new MenuDetails().execute();
                } else {

                    CommonUtilities.alertdialog(act, getString(R.string.val_internet));
                }


            }
            else
            {
                if (CommonUtilities.isConnectingToInternet(act)) {

                    if (mMap!=null && !onActivityResultCalledBeforeOnResume) {
                        if (CommonUtilities.isConnectingToInternet(act)) {
                            new allGobaseModel().execute();
                        } else {

                            CommonUtilities.alertdialog(act, getString(R.string.val_internet));
                        }

                    }

                    onActivityResultCalledBeforeOnResume = false;
                } else
                    CommonUtilities.alertdialog(act,
                            getString(R.string.val_internet));

                if(loginRespose!=null && loginRespose.response.user_info.next_trip!=null && !loginRespose.response.user_info.next_trip.equals("0"))
                {
                    booking_id = loginRespose.response.user_info.next_trip;
                    if (CommonUtilities.isConnectingToInternet(act)) {
                        new GetTripTimeToStart(loginRespose.response.user_info.next_trip).execute();
                    } else {

                        CommonUtilities.alertdialog(act, getString(R.string.val_internet));
                    }

                }
            }
        }
        else
            CommonUtilities.alertdialog(act,getString(R.string.val_internet));
    }

    @Override
    public void startActivity(Intent intent) {
        startActivityForResult(intent, 0);
    }

    private void set_font_style() {
        CommonUtilities.setFontFamily(act, txtNewBases, CommonUtilities.GillSansStd);
        CommonUtilities.setFontFamily(act, txtMostUsedBases, CommonUtilities.GillSansStd);
        CommonUtilities.setFontFamily(act, txtReset, CommonUtilities.GillSansStd);
        CommonUtilities.setFontFamily(act, txtHourlyCost, CommonUtilities.GillSansStd);
        CommonUtilities.setFontFamily(act, txt_plus_km, CommonUtilities.GillSansStd);
        CommonUtilities.setFontFamily(act, txtHeyImAt, CommonUtilities.GillSansStd);
        CommonUtilities.setFontFamily(act, txt_model, CommonUtilities.GillSansStd);
        CommonUtilities.setFontFamily(act, txtSelDeparture, CommonUtilities.GillSansStd_BoldItalic);
        CommonUtilities.setFontFamily(act, txtSeleArrival, CommonUtilities.GillSansStd_BoldItalic);
        CommonUtilities.setFontFamily(act, gobaseLocationName, CommonUtilities.GillSansStd);
        CommonUtilities.setFontFamily(act, gobaseName, CommonUtilities.GillSansStd);
        CommonUtilities.setFontFamily(act, txtGoBase, CommonUtilities.GillSansStd);
        CommonUtilities.setFontFamily(act, txtselectdep, CommonUtilities.GillSansStd_LightItalic);
        CommonUtilities.setFontFamily(act, txtselectarr, CommonUtilities.GillSansStd_LightItalic);
        CommonUtilities.setFontFamily(act, txt_current_address, CommonUtilities.GillSansStd);
    }

    protected void createMarker(double latitude, double longitude, String title, BitmapDescriptor icon, String id, int i) {

        LatLng latlong = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlong));

        //mMap.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);


        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(title)
                .snippet(id)
                .icon(icon));


        markerGoBaseList.add(marker);
        if (i <= 4)
            markerList.add(marker);


    }

    public void SetDateONmakebooking(go_bases.response.bases_info.all_bases model) {
        booking_detail.base_id = model.base_id;
        booking_detail.base_name = model.base_name;
        booking_detail.address_line1 = model.address_line1;
        booking_detail.selected_base_lat = model.latitude;
        booking_detail.selected_base_long = model.longitude;
        booking_detail.base_type = model.base_type;
        booking_detail.car_types = model.car_types;

        //makeBooking();
        carmodel(model.car_types);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                // TODO Auto-generated method stub
                selected_marker = arg0;
                flag = 1;
                NavigationDialog.setVisibility(View.GONE);


                if (go_base_obj != null) {

                    for (int i = 0; i < go_base_obj.response.bases_info.all_bases.size(); i++) {

                        String base_id = go_base_obj.response.bases_info.all_bases.get(i).base_id;
                        if (selected_marker.getSnippet().equals(base_id)) {
                            //CommonUtilities.ShowToast(act,go_base_obj.response.bases_info.all_bases.get(i).base_name);
                            strGobaseName = go_base_obj.response.bases_info.all_bases.get(i).base_name.toString();
                            strGoBaseAddress = go_base_obj.response.bases_info.all_bases.get(i).address_line1;


                            if (go_base_obj.response.bases_info.all_bases.get(i).cars_count.equals("0")) {
                                flag = 0;
                            } else {
                                flag = 1;
                            }
                            if (flag == 1) {
                                booking_detail.base_id = go_base_obj.response.bases_info.all_bases.get(i).base_id;
                                booking_detail.base_name = go_base_obj.response.bases_info.all_bases.get(i).base_name;
                                booking_detail.address_line1 = go_base_obj.response.bases_info.all_bases.get(i).address_line1;
                                booking_detail.selected_base_lat = go_base_obj.response.bases_info.all_bases.get(i).latitude;
                                booking_detail.selected_base_long = go_base_obj.response.bases_info.all_bases.get(i).longitude;
                                booking_detail.base_type = go_base_obj.response.bases_info.all_bases.get(i).base_type;
                                booking_detail.car_types = go_base_obj.response.bases_info.all_bases.get(i).car_types;
                                LatLng latlong = new LatLng(Double.valueOf(go_base_obj.response.bases_info.all_bases.get(i).latitude), Double.valueOf(go_base_obj.response.bases_info.all_bases.get(i).longitude));

                                int x = (int) getResources().getDimension(R.dimen.dpx);
                                int y = (int) getResources().getDimension(R.dimen.dpy);
                                animateLatLngZoom(latlong, 14, x, y);
                                makeBooking();

                                carmodel(go_base_obj.response.bases_info.all_bases.get(i).car_types);
                            } else {
                                Toast.makeText(mActivity, R.string.txt_no_car_avaliable_for_this_gobase, Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                    }

                }
            }
        });


    }

    private void showAll_gobase() {
        if (mMap != null)
            mMap.clear();

        String json = CommonUtilities.getPreference(mActivity,CommonUtilities.pref_go_base_list);
        go_base_obj = gson.fromJson(json, go_bases.class);
        if(latitude==0.0 && longitude==0.0)
        {
            markerList.add(mMap.addMarker(new MarkerOptions().visible(false)
                    .position(new LatLng(53.349805, -6.260310))));
        }
        else
        {
            markerList.add(mMap.addMarker(new MarkerOptions().visible(false)
                    .position(new LatLng(latitude, longitude))));
        }

        if (go_base_obj != null && go_base_obj.response != null && go_base_obj.response.bases_info!=null) {

            for (int i = 0; i < go_base_obj.response.bases_info.all_bases.size(); i++) {
                go_bases.response.bases_info.all_bases detail = go_base_obj.response.bases_info.all_bases.get(i);
                BitmapDescriptor icon = null;
                if (detail.base_type.equals("1"))

                    icon = BitmapDescriptorFactory.fromResource(R.mipmap.orange_map_pin);
                else if (detail.base_type.equals("2"))
                    icon = BitmapDescriptorFactory.fromResource(R.mipmap.blue_map_pin);
                else
                    icon = BitmapDescriptorFactory.fromResource(R.mipmap.green_map_pin);

                createMarker(Double.valueOf(detail.latitude), Double.valueOf(detail.longitude), detail.base_id, icon, detail.base_id, i);
                builder = new LatLngBounds.Builder();
                for (Marker m : markerList) {
                    builder.include(m.getPosition());
                }
                /**initialize the padding for map boundary*/
                int padding = 200;
                /**create the bounds from latlngBuilder to set into map camera*/
                LatLngBounds bounds = builder.build();
                /**create the camera with bounds and padding to set into map*/
            try
            {
                cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            }
            catch(IllegalStateException e)
            {

            }
                /**call the map call back to know map is loaded or not*/
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(true);
        menu.findItem(R.id.action_help).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search) {

            autoComplete_searchtext.setText("");

            NavigationDialog.setVisibility(View.GONE);
            if (autoComplete_searchtext.getVisibility() == View.VISIBLE) {
                autoComplete_searchtext.setVisibility(View.GONE);
                CommonUtilities.hideSoftKeyboard(act);
            } else {
                autoComplete_searchtext.setVisibility(View.VISIBLE);
                autoComplete_searchtext.requestFocus();
                InputMethodManager mgr = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
                // only will trigger it if no physical keyboard is open
                mgr.showSoftInput(act.getCurrentFocus(), InputMethodManager.SHOW_IMPLICIT);
                ((InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(act.getCurrentFocus(), 0);

            }

            return true;
        }
        if (id == R.id.action_edit) {
            time_date_car_click(true);
            booking_flag = false;
            bookNowChargeDialog.setVisibility(View.GONE);
            menu.findItem(R.id.action_search).setVisible(true);
            menu.findItem(R.id.action_edit).setVisible(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void animateLatLngZoom(LatLng latlng, int reqZoom, int offsetX, int offsetY) {

        // Save current zoom
        float originalZoom = mMap.getCameraPosition().zoom;

        // Move temporarily camera zoom
        mMap.moveCamera(CameraUpdateFactory.zoomTo(reqZoom));

        Point pointInScreen = mMap.getProjection().toScreenLocation(latlng);

        Point newPoint = new Point();
        newPoint.x = pointInScreen.x + offsetX;
        newPoint.y = pointInScreen.y + offsetY;

        LatLng newCenterLatLng = mMap.getProjection().fromScreenLocation(newPoint);

        // Restore original zoom
        mMap.moveCamera(CameraUpdateFactory.zoomTo(originalZoom));

        // Animate a camera with new latlng center and required zoom.
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newCenterLatLng, reqZoom));

    }

    @Override
    public void onStart() {
        super.onStart();

        ((MyApplication) getActivity().getApplication()).getDefaultTracker().setScreenName("Home Screen");
        ((MyApplication) getActivity().getApplication()).getDefaultTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    private class NearGobaseModel extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            ServerAccess sa = new ServerAccess();
            formated_address = sa.reverse_geocode(String.valueOf(latitude), String.valueOf(longitude));
            String Customer_id = CommonUtilities.getPreference(mActivity, CommonUtilities.pref_customer_id);

            go_nearbaselist = sa.go_bases(mActivity, Customer_id, String.valueOf(latitude), String.valueOf(longitude), "2");
            if (go_nearbaselist != null) {
                if (go_nearbaselist.response.status!=null && go_nearbaselist.response.status.equals(CommonUtilities.key_Success)) {
                    return "true";
                } else
                    return "false";
            }
            return "false";
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            txt_current_address.setText(formated_address);

            if (result.equals("true")) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                if (checkLocationPermission())
                    mMap.setMyLocationEnabled(true);
                booking_flag = false;
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                zoomMap(latitude, longitude);

                NavigationDialog.setVisibility(View.VISIBLE);
                NavigationDialog_visibility = true;
                if (go_nearbaselist != null && go_nearbaselist.response.bases_info.all_bases.size() == 0) {
                    val_empty_gobase.setText("No Nearby GoBase found");
                    val_empty_gobase.setVisibility(View.VISIBLE);
                    ArrayList<go_bases.response.bases_info.favourite_bases> favourite_bases = new ArrayList<go_bases.response.bases_info.favourite_bases>();
                    Favourite_gobase_model model = new Favourite_gobase_model(act, favourite_bases);
                    list_gobase.setAdapter(model);
                } else {
                    val_empty_gobase.setVisibility(View.GONE);
                    Go_base_model go_base_model1 = new Go_base_model(act, go_nearbaselist.response.bases_info.all_bases);
                    list_gobase.setAdapter(go_base_model1);
                }
            }
            else {
                if(go_nearbaselist!=null)
                    CommonUtilities.ShowToast(mActivity,go_nearbaselist.response.msg);
                else
                {
                    if(!mActivity.isFinishing())
                        CommonUtilities.alertdialog(mActivity,getString(R.string.val_something));
                }
            }
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }
    }

    private class allGobaseModel extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            ServerAccess sa = new ServerAccess();
            String Customer_id = CommonUtilities.getPreference(mActivity, CommonUtilities.pref_customer_id);
            cartype = sa.car_types(mActivity);
            if (cartype != null)
                if (cartype.response.status.equals(CommonUtilities.key_Success)) {
                    String car_type = gson.toJson(cartype);
                    CommonUtilities.setPreference(mActivity,CommonUtilities.pref_car_type, car_type);
                }
            go_list = sa.go_bases(mActivity, Customer_id, String.valueOf(latitude), String.valueOf(longitude), "1");

            if (go_list != null) {
                if (go_list.response.status.equals(CommonUtilities.key_Success)) {
                    String detail = gson.toJson(go_list);
                    CommonUtilities.setPreference(mActivity,CommonUtilities.pref_go_base_list, detail);
                    return "true";
                } else
                    return "false";
            }
            else
                return "false";

        }


        @Override
        protected void onPostExecute(String result) {
            try {
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result.equals("true")) {
                autoComplete_searchtext.setClickable(true);
                btnNavigation.setClickable(true);
                btnNavigation.setVisibility(View.VISIBLE);
                btnMakeBooking.setClickable(true);

                if (CommonUtilities.isConnectingToInternet(act))
                    showAll_gobase();
                if (mMap != null) {
                    if (gps.canGetLocation()) {
                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();
                        if (checkLocationPermission())
                            mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }

                    if (getFragmentManager() != null)
                        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
                    mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                            /**set animated zoom camera into map*/
                            if (markerList != null)
                                if (markerList.size() > 1)
                                {
                                    try
                                    {
                                        mMap.animateCamera(cu);
                                    }
                                    catch(IllegalStateException e)
                                    {

                                    }
                                }
                            loginRespose = (LoginRespose) gson.fromJson(CommonUtilities.getPreference(mActivity, CommonUtilities.pref_customer_detail), LoginRespose.class);
                            if(loginRespose!=null && loginRespose.response.user_info.is_force_pin!=null && loginRespose.response.user_info.is_force_pin.equals("1"))
                            {
                                showForcePinChange();
                            }
                            else if(loginRespose!=null && loginRespose.response.getUser_info().is_rate_screen != null && loginRespose.response.getUser_info().is_rate_screen.equals("1"))
                            {
                                rating_anim = 0;
                                showRatingBox();
                            }
                        }
                    });
                }

                String[] search = new String[0];
                if (go_base_obj != null) {
                    if (go_base_obj.response.bases_info.all_bases != null)
                    {
                        search = new String[go_base_obj.response.bases_info.all_bases.size()];
                        for (int i = 0; i < go_base_obj.response.bases_info.all_bases.size(); i++) {
                            search[i] = go_base_obj.response.bases_info.all_bases.get(i).base_name;
                        }
                    }
                    ArrayAdapter searchAdapter = new ArrayAdapter(mActivity, android.R.layout.simple_list_item_1, search);
                    autoComplete_searchtext.setAdapter(searchAdapter);
                }

                if (booknowDialog.getVisibility() != View.VISIBLE && mLayout.getVisibility() != View.VISIBLE)
                    check_user_state();
            }
            else
            {
                if(!mActivity.isFinishing())
                    CommonUtilities.alertdialog(mActivity,getString(R.string.val_something));
            }
        }

        @Override
        protected void onPreExecute() {

            CommonUtilities.key_security_token = CommonUtilities.getSecurity_Preference(mActivity, CommonUtilities.pref_SecurityToken);
            if(!mActivity.isFinishing())
                dialog.show();
            autoComplete_searchtext.setClickable(false);
            btnNavigation.setClickable(false);
            btnMakeBooking.setClickable(false);
            btnNavigation.setVisibility(View.GONE);
        }
    }

    private class check_trip extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            ServerAccess sa = new ServerAccess();
            String Customer_id = CommonUtilities.getPreference(mActivity, CommonUtilities.pref_customer_id);

            trip_model = sa.Check_trip(mActivity, booking_detail, Customer_id, slot_id);
            if (trip_model != null) {
                if (trip_model.response.status.equals(CommonUtilities.key_Success)) {
                    return "true";
                } else {
                    return "false";
                }
            } else {
                return "false";
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if (dialog != null) {
                try {
                    if (dialog.isShowing())
                        dialog.dismiss();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (result.equals("true")) {
                if (trip_model.response.trip_info != null) {
                    if (trip_model.response.trip_info.ResponseFlag != null) {
                        if (trip_model.response.trip_info.ResponseFlag.equals("1")) {
                            bookNowChargeDialog.setVisibility(View.VISIBLE);
                            menu.findItem(R.id.action_search).setVisible(false);
                            menu.findItem(R.id.action_edit).setVisible(true);
                            txtCost.setText("€" + trip_model.response.trip_info.trip_cost);
                            booking_detail.booking_cost = trip_model.response.trip_info.trip_cost.toString();
                            booking_detail.car_id = trip_model.response.trip_info.car_id.toString();
                            booking_detail.car_name = trip_model.response.trip_info.car_name.toString();
                            booking_detail.slot_id = trip_model.response.trip_info.slot_id;
                            slot_id = trip_model.response.trip_info.slot_id;
                            booking_flag = true;
                            time_date_car_click(false);
                            if(trip_model.response.trip_info.driver_role.equals("0"))
                            {
                                drivercall=false;
                            }
                            else
                            {
                                drivercall=true;
                            }
                            if(trip_model.response.trip_info.yearly_insurance.equals("1"))
                            {
                                show_insurance_sc=false;
                            }
                            else
                            {
                                show_insurance_sc=true;
                            }

                        } else if (trip_model.response.trip_info.ResponseFlag.equals("3")) {
                            booking_flag = false;
                            mLayout.setVisibility(View.VISIBLE);
                            mLayout.setAnchorPoint(0.5f);
                            bookNowChargeDialog.setVisibility(View.GONE);
                            booknowDialog.setVisibility(View.GONE);
                            car_availibility_mode_size = trip_model.response.trip_info.Trips_details.size();
                            position = 0;

                            if (position == 0)
                                imgPrevious.setAlpha((float) 0.4);
                            if (car_availibility_mode_size == 1) {
                                imgPrevious.setVisibility(View.INVISIBLE);
                                imgNext.setVisibility(View.INVISIBLE);

                            } else {
                                imgPrevious.setVisibility(View.VISIBLE);
                                imgNext.setVisibility(View.VISIBLE);
                            }
                            carAvailibility(position);
                        }
                    }
                }
            } else {
                booking_flag = false;
                bookNowChargeDialog.setVisibility(View.GONE);
                if (trip_model != null)
                    CommonUtilities.alertdialog(mActivity, trip_model.response.msg);
                else
                {
                    if(!mActivity.isFinishing())
                        CommonUtilities.alertdialog(mActivity,getString(R.string.val_something));
                }
            }
        }

        @Override
        protected void onPreExecute() {

        }

    }

    public class ResponseModel extends AsyncTask<String, Void, String> {
        Check_customer_status status;

        @Override
        protected String doInBackground(String... params) {
            ServerAccess sa = new ServerAccess();

            String Customer_id = CommonUtilities.getPreference(mActivity, CommonUtilities.pref_customer_id);
            status = sa.check_customer_status(mActivity, Customer_id);
            if (status != null) {
                if (status.response.status.equals(CommonUtilities.key_Success)) {
                    return "true";
                } else
                    return "false";
            }
            return "false";

        }


        @Override
        protected void onPostExecute(String result) {


            if (result.equals("true")) {
                if (CommonUtilities.isConnectingToInternet(act)) {

                    if (status.response.customer_info.ResponseFlag == 1) {

                        if (CommonUtilities.isConnectingToInternet(act)) {
                            new check_trip().execute();
                        } else {

                            CommonUtilities.alertdialog(act, getString(R.string.val_internet));
                        }

                    } else if (status.response.customer_info.ResponseFlag == 2) {
                        try {
                            if (dialog.isShowing())
                                dialog.dismiss();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        alertdialog(act, status.response.msg);
                    }
                } else

                {
                    try {
                        if (dialog.isShowing())
                            dialog.dismiss();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    CommonUtilities.alertdialog(act, getString(R.string.val_internet));
                }
            } else {

                Gson gson = new Gson();
                String detail = gson.toJson(booking_detail);
                CommonUtilities.setPreference(mActivity, CommonUtilities.pref_booking_detail, detail);
                try {
                    if (dialog.isShowing())
                        dialog.dismiss();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (status != null)
                    CommonUtilities.alertdialog(act, status.response.msg);
                else
                {
                    if(!mActivity.isFinishing())
                        CommonUtilities.alertdialog(mActivity,getString(R.string.val_something));
                }
            }


        }

        @Override
        protected void onPreExecute() {

            dialog.show();

        }

    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private View view;

        public CustomInfoWindowAdapter() {
            view = getActivity().getLayoutInflater().inflate(R.layout.custom_info_window,
                    null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();

            }
            return null;
        }

        @Override
        public View getInfoWindow(final Marker marker) {

            marker.getSnippet();
            go_bases.response.bases_info.all_bases detail = null;
            for (int i = 0; i < go_base_obj.response.bases_info.all_bases.size(); i++) {
                if (go_base_obj.response.bases_info.all_bases.get(i).base_id.equals(marker.getTitle())) {
                    detail = go_base_obj.response.bases_info.all_bases.get(i);
                }

            }

            if (detail != null) {

                final String title = detail.base_name;
                final TextView titleUi = ((TextView) view.findViewById(R.id.title));
                final TextView car_num = ((TextView) view.findViewById(R.id.car_num));
                final TextView snippet = ((TextView) view.findViewById(R.id.snippet));
                final TextView txtMarkerCars = ((TextView) view.findViewById(R.id.txtMarkerCars));


                titleUi.setText(title);
                car_num.setText(detail.cars_count);
                snippet.setText(detail.address_line1);
                flag = 1;
                strGobaseName = detail.base_name;
                strGoBaseAddress = detail.address_line1;
                pinLat = Double.valueOf(detail.latitude);
                pinlong = Double.valueOf(detail.longitude);
                booknowDialog.setVisibility(View.GONE);
                if (detail.cars_count.equals("0")) {
                    txtMarkerCars.setText("car ");
                    carAvailabilityFlag = 0;
                } else if (detail.cars_count.equals("1")) {
                    txtMarkerCars.setText("car ");
                    carAvailabilityFlag = 1;
                    SetDateONmakebooking(detail);
                } else {
                    carAvailabilityFlag = 1;
                    txtMarkerCars.setText("cars ");
                    SetDateONmakebooking(detail);

                }

            }
            return view;
        }
    }

    public class Favourite_gobase_model extends BaseAdapter {

        private Context context;
        private ArrayList<go_bases.response.bases_info.favourite_bases> navDrawerItems;

        public Favourite_gobase_model(Context context, ArrayList<go_bases.response.bases_info.favourite_bases> navDrawerItems) {
            this.context = context;
            this.navDrawerItems = navDrawerItems;
        }

        @Override
        public int getCount() {
            return navDrawerItems.size();
        }

        @Override
        public Object getItem(int position) {
            return navDrawerItems.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater)
                        context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.near_go_base_layout, null);

            }

            ImageView distance_image = (ImageView) convertView.findViewById(R.id.distance_image);
            TextView distrance = (TextView) convertView.findViewById(R.id.distrance);
            TextView go_basename = (TextView) convertView.findViewById(R.id.go_basename);

            TextView car_count = (TextView) convertView.findViewById(R.id.car_count);

            go_basename.setText(navDrawerItems.get(position).base_name);
            if (navDrawerItems.get(position).cars_count.equals("0")) {
                car_count.setText(navDrawerItems.get(position).cars_count + " car");

                flag = 0;
            } else if (navDrawerItems.get(position).cars_count.equals("1")) {
                car_count.setText(navDrawerItems.get(position).cars_count + " car");

                flag = 1;

            } else {

                car_count.setText(navDrawerItems.get(position).cars_count + " cars");
                flag = 1;
            }
            int distance = 0;
            if (!TextUtils.isEmpty(navDrawerItems.get(position).distance))
                distance = (int) (Double.valueOf(navDrawerItems.get(position).distance) * 1000);
            distrance.setText(distance + "m");
            if (distance < 500)
                distance_image.setImageResource(R.mipmap.walk_one);
            else if (distance < 750)
                distance_image.setImageResource(R.mipmap.walk_two);
            else
                distance_image.setImageResource(R.mipmap.walk_three);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavigationDialog.setVisibility(View.GONE);
                    btnNavigation.setVisibility(View.GONE);
                    strGobaseName = navDrawerItems.get(position).base_name;
                    strGoBaseAddress = navDrawerItems.get(position).address_line1;

                    if (navDrawerItems.get(position).cars_count.equals("0")) {

                        Toast.makeText(getActivity(), R.string.txt_no_car_avaliable_for_this_gobase, Toast.LENGTH_SHORT).show();
                        btnNavigation.setVisibility(View.VISIBLE);
                        btnMakeBooking.setVisibility(View.VISIBLE);
                        BlackG.setVisibility(View.VISIBLE);
                    } else {
                        btnNavigation.setVisibility(View.GONE);
                        NavigationDialog.setVisibility(View.GONE);
                        booking_detail.base_id = navDrawerItems.get(position).base_id;
                        booking_detail.base_type = navDrawerItems.get(position).base_type;
                        booking_detail.base_name = navDrawerItems.get(position).base_name;
                        booking_detail.address_line1 = navDrawerItems.get(position).address_line1;
                        booking_detail.selected_base_lat = navDrawerItems.get(position).latitude;
                        booking_detail.selected_base_long = navDrawerItems.get(position).longitude;
                        booking_detail.car_types = navDrawerItems.get(i).car_types;
                        LatLng latlong = new LatLng(Double.valueOf(navDrawerItems.get(position).latitude), Double.valueOf(navDrawerItems.get(position).longitude));

                        int x = (int) getResources().getDimension(R.dimen.dpx);
                        int y = (int) getResources().getDimension(R.dimen.dpy);
                        animateLatLngZoom(latlong, 14, x, y);
                        makeBooking();
                        carmodel(navDrawerItems.get(position).car_types);
                    }
                }
            });

            return convertView;
        }

    }

    public class Go_base_model extends BaseAdapter {

        private Context context;
        private ArrayList<go_bases.response.bases_info.all_bases> navDrawerItems;

        public Go_base_model(Context context, ArrayList<go_bases.response.bases_info.all_bases> navDrawerItems) {
            this.context = context;
            this.navDrawerItems = navDrawerItems;
        }

        @Override
        public int getCount() {
            return navDrawerItems.size();
        }

        @Override
        public Object getItem(int position) {
            return navDrawerItems.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater)
                        context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.near_go_base_layout, null);
            }
            ImageView distance_image = (ImageView) convertView.findViewById(R.id.distance_image);

            TextView distrance = (TextView) convertView.findViewById(R.id.distrance);
            TextView go_basename = (TextView) convertView.findViewById(R.id.go_basename);

            TextView car_count = (TextView) convertView.findViewById(R.id.car_count);


            go_basename.setText(navDrawerItems.get(position).base_name);

            if (navDrawerItems.get(position).cars_count.equals("0")) {
                car_count.setText(navDrawerItems.get(position).cars_count + " car");
                flag = 0;
            } else if (navDrawerItems.get(position).cars_count.equals("1")) {
                flag = 1;
                car_count.setText(navDrawerItems.get(position).cars_count + " car");

            } else {
                flag = 1;
                car_count.setText(navDrawerItems.get(position).cars_count + " cars");
            }
            int distance = 0;
            if (!TextUtils.isEmpty(navDrawerItems.get(position).distance))
                distance = (int) (Double.valueOf(navDrawerItems.get(position).distance) * 1000);
            distrance.setText(distance + "m");
            if (distance < 500)
                distance_image.setImageResource(R.mipmap.walk_one);
            else if (distance < 750)
                distance_image.setImageResource(R.mipmap.walk_two);
            else
                distance_image.setImageResource(R.mipmap.walk_three);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavigationDialog.setVisibility(View.GONE);
                    btnNavigation.setVisibility(View.GONE);
                    strGobaseName = navDrawerItems.get(position).base_name;
                    strGoBaseAddress = navDrawerItems.get(position).address_line1;
                    if ((navDrawerItems.get(position).cars_count.equals("0"))) {
                        Toast.makeText(getActivity(), R.string.txt_no_car_avaliable_for_this_gobase, Toast.LENGTH_SHORT).show();
                        btnNavigation.setVisibility(View.VISIBLE);
                        btnMakeBooking.setVisibility(View.VISIBLE);
                        BlackG.setVisibility(View.VISIBLE);
                    } else {
                        booking_detail.base_id = navDrawerItems.get(position).base_id;
                        booking_detail.base_type = navDrawerItems.get(position).base_type;
                        booking_detail.base_name = navDrawerItems.get(position).base_name;
                        booking_detail.address_line1 = navDrawerItems.get(position).address_line1;
                        booking_detail.selected_base_lat = navDrawerItems.get(position).latitude;
                        booking_detail.selected_base_long = navDrawerItems.get(position).longitude;
                        booking_detail.car_types = navDrawerItems.get(position).car_types;
                        LatLng latlong = new LatLng(Double.valueOf(navDrawerItems.get(position).latitude), Double.valueOf(navDrawerItems.get(position).longitude));

                        int x = (int) getResources().getDimension(R.dimen.dpx);
                        int y = (int) getResources().getDimension(R.dimen.dpy);
                        animateLatLngZoom(latlong, 14, x, y);
                        makeBooking();
                        carmodel(navDrawerItems.get(position).car_types);

                    }
                }
            });
            return convertView;
        }
    }

    public class ImageAdapter extends BaseAdapter {
        car_types model;

        public ImageAdapter(car_types model) {
            this.model = model;
        }

        @Override
        public int getCount() {
            return model.response.getCar_types_info().size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.car_type_row, null);
                ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
                TextView txtCarType = (TextView) convertView.findViewById(R.id.textCarType);
                Picasso.with(getActivity())
                        .load(CommonUtilities.CarTypeImageURL + model.response.getCar_types_info().get(position).getCar_image()).into(imageView);

                txtCarType.setText(model.response.getCar_types_info().get(position).getType_name());
                txtCarType.setTypeface(null, Typeface.BOLD);
                txtCarType.setTextColor(Color.parseColor("#3c3c3c"));
                //imageView.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.car_selector));
            }
            return convertView;
        }
    }

    public class GetTripTimeToStart extends AsyncTask<String, Void, String> {
        com.GoCarDev.model.GetTripTimeToStart responseModel;
        String booking_id;

        public GetTripTimeToStart(String booking_id) {
            this.booking_id = booking_id;
        }

        @Override
        protected String doInBackground(String... params) {
            ServerAccess sa = new ServerAccess();
            responseModel = sa.GetTripTimeToStartResponse(mActivity, this.booking_id, CommonUtilities.getPreference(mActivity, CommonUtilities.pref_customer_id));
            if (responseModel != null) {
                if (responseModel.response.status.equals(CommonUtilities.key_Success)) {
                    return "true";
                } else
                    return "false";
            } else
                return "false";


        }

        @Override
        protected void onPostExecute(String result) {

            try {
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result.equals("true")) {
                iFoundCar.setVisibility(View.VISIBLE);
                if (responseModel.response.trip_info.message != null) {
                    msgRemember1Hr.setText("" + responseModel.response.trip_info.message);
                }
                if (!responseModel.response.trip_info.alert_message.equals("")) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(act);
                    builder1.setTitle(R.string.app_name);
                    builder1.setIcon(R.mipmap.app_icon);
                    builder1.setMessage(responseModel.response.trip_info.alert_message);
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alert11 = builder1.create();
                    if(!mActivity.isFinishing())
                        alert11.show();
                }
            }
            else
            {
                if(responseModel!=null)
                    CommonUtilities.ShowToast(mActivity,responseModel.response.msg);
                else
                {
                    if(!mActivity.isFinishing())
                        CommonUtilities.alertdialog(mActivity,getString(R.string.val_something));
                }
            }

        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }
    }

    public class GetCarStatus extends AsyncTask<String, Void, String> {
        CheckMyCarStatus responseModel;
        String booking_id;

        public GetCarStatus(String booking_id) {
            this.booking_id = booking_id;
        }

        @Override
        protected String doInBackground(String... params) {
            ServerAccess sa = new ServerAccess();
            responseModel = sa.GetCarStatus(mActivity, CommonUtilities.getPreference(mActivity, CommonUtilities.pref_customer_id), this.booking_id);
            if (responseModel != null) {
                if (responseModel.response.status.equals(CommonUtilities.key_Success)) {
                    return "true";
                } else
                    return "false";
            } else
                return "false";


        }

        @Override
        protected void onPostExecute(String result) {

            try {
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result.equals("true")) {

                if(responseModel.response.car_status.message!=null && responseModel.response.car_status.message.equals(""))
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "1");
                    bundle.putString("booking_id", booking_id);
                    loginRespose = (LoginRespose) gson.fromJson(CommonUtilities.getPreference(mActivity, CommonUtilities.pref_customer_detail), LoginRespose.class);
                    if (loginRespose.response.user_info.current_trip!=null && !loginRespose.response.user_info.current_trip.equals("0") && !loginRespose.response.user_info.current_trip.equals(booking_id))
                        bundle.putString("currentJourney", "currentJourney");
                    ((Dashboard_Activity)mActivity).changeFragment(bundle);
                }
                else
                {
                    CommonUtilities.alertdialog(mActivity,responseModel.response.car_status.message);
                }
            }
            else
            {
                if(responseModel!=null)
                    CommonUtilities.ShowToast(mActivity, responseModel.response.msg);
                else
                {
                    if(!mActivity.isFinishing())
                        CommonUtilities.alertdialog(mActivity,getString(R.string.val_something));
                }
            }
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }
    }

    public class MenuDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            ServerAccess sa = new ServerAccess();
            model = sa.GetBurgerMenuDetails(mActivity, CommonUtilities.getPreference(mActivity, CommonUtilities.pref_customer_id),getip.NetwordDetect());
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

            if (result.equals("true")) {
                if(model.response.getUser_info().force_download!=null && !model.response.getUser_info().force_download.equals("0"))
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mActivity);
                    builder1.setTitle(R.string.app_name);
                    builder1.setIcon(R.mipmap.app_icon);
                    builder1.setMessage(model.response.getUser_info().message);
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                            Uri uri = Uri.parse("market://details?id=" + mActivity.getPackageName());
                            Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                            try {
                                startActivity(myAppLinkToMarket);
                                System.exit(0);
                            } catch (ActivityNotFoundException e) {
                                CommonUtilities.ShowToast(mActivity, "unable to find app");
                            }
                        }
                    });
                    if(!model.response.getUser_info().force_download.equals("1"))
                    {
                        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if (CommonUtilities.isConnectingToInternet(act)) {
                                    new CancelNewUpdate().execute();
                                } else {

                                    CommonUtilities.alertdialog(act, getString(R.string.val_internet));
                                }

                                setInfo();
                            }
                        });
                    }
                    AlertDialog alert11 = builder1.create();
                    if(!mActivity.isFinishing())
                        alert11.show();
                    if(dialog.isShowing())
                        dialog.dismiss();
                }
                else if(Integer.valueOf(model.response.getUser_info().ResponseFlag) != null && model.response.getUser_info().ResponseFlag==0)
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(act);
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
                    if(!mActivity.isFinishing())
                        alert11.show();
                    if(dialog.isShowing())
                        dialog.dismiss();
                }
                else if(Integer.valueOf(model.response.getUser_info().member_status) != null && model.response.getUser_info().member_status==0)
                {
                     AlertDialog.Builder builder1 = new AlertDialog.Builder(act);
                        builder1.setTitle(R.string.app_name);
                        builder1.setIcon(R.mipmap.app_icon);
                        builder1.setMessage(model.response.getUser_info().message);
                        builder1.setCancelable(false);
                        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent iStart = new Intent(act, Joinus.class);
                                iStart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(iStart);
                                CommonUtilities.RemoveALlPreference(act);
                            }
                        });

                        AlertDialog alert11 = builder1.create();
                        if(!mActivity.isFinishing())
                            alert11.show();
                    if(dialog.isShowing())
                        dialog.dismiss();
                }
                else {
                    setInfo();
                }
            } else {
                if(dialog.isShowing())
                    dialog.dismiss();
                if (model != null) {
                    if(model.response.code.equals("ERR003"))
                    {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(act);
                        builder1.setTitle(R.string.app_name);
                        builder1.setIcon(R.mipmap.app_icon);
                        builder1.setMessage(model.response.msg);
                        builder1.setCancelable(false);
                        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if (CommonUtilities.isConnectingToInternet(act)) {
                                    new ValidateLogout().execute();
                                } else {
                                    if(!mActivity.isFinishing())
                                        CommonUtilities.alertdialog(act, getString(R.string.val_internet));
                                }
                            }
                        });

                        AlertDialog alert11 = builder1.create();
                        if(!mActivity.isFinishing())
                            alert11.show();
                    }
                    else
                    {
                        CommonUtilities.ShowToast(act,model.response.msg);
                    }
                }
                else
                {
                    if(!mActivity.isFinishing())
                        CommonUtilities.alertdialog(mActivity,getString(R.string.val_something));
                }
            }
        }

        @Override
        protected void onPreExecute() {
            if(dialog!=null && !dialog.isShowing())
                dialog.show();
        }
    }


    public void setInfo()
    {
        Gson gson = new Gson();
        String detail = gson.toJson(model);
        CommonUtilities.setPreference(act, CommonUtilities.pref_customer_detail, detail);
        if (!model.response.user_info.current_trip.equals("0") && Foreground.instance.checkBackground) {
            for (int i = 0; i < ((Dashboard_Activity)mActivity).nav_drawer_items.size(); i++) {
                if (((Dashboard_Activity)mActivity).nav_drawer_items.get(i).itemNumber == 5) {
                    ((Dashboard_Activity)mActivity).selectItem(i);
                    ((Dashboard_Activity)mActivity).mDrawerList.setSelection(i);
                    ((Dashboard_Activity)mActivity).mDrawerList.setItemChecked(i, true);
                    break;
                }
            }
            if(dialog.isShowing())
                dialog.dismiss();
        }
        else {
            if (CommonUtilities.isConnectingToInternet(act)) {
                if (mMap!=null && !onActivityResultCalledBeforeOnResume) {
                    new allGobaseModel().execute();
                }
                else
                {
                    if(dialog.isShowing())
                        dialog.dismiss();
                }
                onActivityResultCalledBeforeOnResume = false;
            } else
            {
                if(!mActivity.isFinishing())
                    CommonUtilities.alertdialog(act,getString(R.string.val_internet));
                if(dialog.isShowing())
                    dialog.dismiss();
            }

            if (model.response.user_info.next_trip != null && !model.response.user_info.next_trip.equals("0")) {
                booking_id = model.response.user_info.next_trip;
                if (CommonUtilities.isConnectingToInternet(act)) {
                    new GetTripTimeToStart(model.response.user_info.next_trip).execute();
                } else {

                    CommonUtilities.alertdialog(act, getString(R.string.val_internet));
                }

            }
            else
            {
                iFoundCar.setVisibility(View.GONE);
            }
        }
    }

    private void showForcePinChange() {
        final Dialog dialog = new Dialog(mActivity);
        dialog.setContentView(R.layout.change_pin_dialog);

        final EditText edCurrentPin,edNewPin,edConfirmNewPin;
        TextView tvApply,tvReset;
        edCurrentPin = (EditText)dialog.findViewById(R.id.edCurrentPin);
        edNewPin = (EditText)dialog.findViewById(R.id.edNewPin);
        edConfirmNewPin = (EditText)dialog.findViewById(R.id.edConfirmNewPin);

        tvReset = (TextView) dialog.findViewById(R.id.tvReset);
        tvApply = (TextView)dialog.findViewById(R.id.tvApply);
        dialog.show();
        tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtilities.hideSoftKeyboard(mActivity,edCurrentPin);
                if (CommonUtilities.isConnectingToInternet(act)) {
                    if (edCurrentPin.getText().toString().trim().isEmpty()) {
                        edCurrentPin.requestFocus();
                        CommonUtilities.ShowToast(mActivity, getString(R.string.val_current_pin));
                    }
                    else if (edNewPin.getText().toString().trim().isEmpty()) {
                        edNewPin.requestFocus();
                        CommonUtilities.ShowToast(mActivity,getString(R.string.val_new_pin));
                    }
                    else if (edConfirmNewPin.getText().toString().trim().isEmpty()) {
                        edConfirmNewPin.requestFocus();
                        CommonUtilities.ShowToast(mActivity, getString(R.string.val_new_confirm_pin));
                    }
                    else if (edNewPin.getText().toString().length()!=4) {
                        edNewPin.requestFocus();
                        CommonUtilities.ShowToast(mActivity,"Please enter 4 digit New Pin");
                    }
                    else if (edConfirmNewPin.getText().toString().length()!=4) {
                        edConfirmNewPin.requestFocus();
                        CommonUtilities.ShowToast(mActivity,"Please enter 4 digit Confirm New Pin");
                    }
                    else if(!edNewPin.getText().toString().equals(edConfirmNewPin.getText().toString()))
                    {
                        CommonUtilities.ShowToast(mActivity,"New Pin and Confirm New Pin must be same");
                    }
                    else
                    {
                        dialog.dismiss();
                        if (CommonUtilities.isConnectingToInternet(act)) {
                            new GetChangePin(edCurrentPin.getText().toString(),edNewPin.getText().toString()).execute();
                        } else {

                            CommonUtilities.alertdialog(act, getString(R.string.val_internet));
                        }

                    }
                } else
                    CommonUtilities.alertdialog(mActivity,getString(R.string.val_internet));
            }
        });
        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edCurrentPin.setText("");
                edNewPin.setText("");
                edConfirmNewPin.setText("");
                edCurrentPin.requestFocus();
            }
        });
    }

    public class GetRating extends AsyncTask<String, Void, String> {
        rating_model ratingResponse;
        String isRating;
        public GetRating(String isRating)
        {
            this.isRating=isRating;
        }

        @Override
        protected String doInBackground(String... params) {
            ServerAccess sa = new ServerAccess();
            ratingResponse = sa.rating_app(act, CommonUtilities.getPreference(act, CommonUtilities.pref_customer_id), isRating, BuildConfig.VERSION_NAME);
            if (ratingResponse != null) {
                if (ratingResponse.response.status.equals(CommonUtilities.key_Success)) {
                    return "true";
                } else
                    return "false";
            } else
                return "false";
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result.equals("true")) {
                loginRespose = (LoginRespose) gson.fromJson(CommonUtilities.getPreference(act, CommonUtilities.pref_customer_detail), LoginRespose.class);
                loginRespose.response.user_info.is_rate_screen = "0";
                Gson gson = new Gson();
                String detail = gson.toJson(loginRespose);
                CommonUtilities.setPreference(act, CommonUtilities.pref_customer_detail, detail);
                if(isRating.equals("1"))
                {
                    Uri uri = Uri.parse("market://details?id=" + act.getPackageName());
                    Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        mActivity.startActivity(myAppLinkToMarket);
                    } catch (ActivityNotFoundException e) {
                        CommonUtilities.ShowToast(act, "unable to find app");
                    }

                    ratingdialog.dismiss();
                }
            }
            else
            {
                if(ratingResponse!=null)
                    CommonUtilities.alertdialog(act, ratingResponse.response.msg);
                else
                {
                    if(!mActivity.isFinishing())
                        CommonUtilities.alertdialog(mActivity,getString(R.string.val_something));
                }
            }
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

    }

    public class GetChangePin extends AsyncTask<String, Void, String> {
        ChangePin model;
        String oldpin,newpin;

        public GetChangePin(String oldpin,String newpin) {
            this.oldpin = oldpin;
            this.newpin = newpin;
        }

        @Override
        protected String doInBackground(String... params) {
            ServerAccess sa = new ServerAccess();
            model = sa.GetChangePin(mActivity, CommonUtilities.getPreference(act, CommonUtilities.pref_customer_id),oldpin, newpin,"1");
            if (model != null) {
                if (model.response.status.equals(CommonUtilities.key_Success)) {
                    return "true";
                } else
                    return "false";
            } else
                return "false";

        }

        @Override
        protected void onPostExecute(String result) {

            try {
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result.equals("true")) {
                CommonUtilities.ShowToast(mActivity,model.response.user_info.message);
            }
            else
            {
                if(model!=null)
                {
                    showForcePinChange();
                    CommonUtilities.ShowToast(mActivity, model.response.msg);
                }
                else
                {
                    if(!mActivity.isFinishing())
                        CommonUtilities.alertdialog(mActivity,getString(R.string.val_something));
                }
            }
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

    }

    public void showRatingBox() {
        ratingdialog = new Dialog(act);
        ratingdialog.setContentView(R.layout.rating);
        ratingdialog.setCancelable(false);
        ratingdialog.show();

        final LinearLayout layoutratingplaystore;
        RatingBar ratingBar;
        final LinearLayout ratingOptions;
        TextView tvNoThanks, tvRateNow;


        layoutratingplaystore = (LinearLayout) ratingdialog.findViewById(R.id.layoutratingplaystore);
        ratingBar = (RatingBar) ratingdialog.findViewById(R.id.ratingBar);
        ratingOptions = (LinearLayout) ratingdialog.findViewById(R.id.ratingOptions);
        tvRateNow = (TextView) ratingdialog.findViewById(R.id.tvRateNow);
        tvNoThanks = (TextView) ratingdialog.findViewById(R.id.tvNoThanks);
        CommonUtilities.setFontFamily(act, tvNoThanks, CommonUtilities.GillSansStd);
        CommonUtilities.setFontFamily(act, tvRateNow, CommonUtilities.GillSansStd);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(final RatingBar ratingBar, float rating, boolean fromUser) {

                if (rating_anim == 0) {
                    rating_anim = 1;
                    final Animation anim = AnimationUtils.loadAnimation(act, R.anim.bounce_animation_in);
                    anim.setFillAfter(true);
                    layoutratingplaystore.startAnimation(anim);

                    anim.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {
                            ratingOptions.setVisibility(View.VISIBLE);
                            ratingBar.setIsIndicator(true);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            }
        });

        tvNoThanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutratingplaystore.clearAnimation();
                if (CommonUtilities.isConnectingToInternet(act)) {
                    new GetRating("0").execute();
                } else {

                    CommonUtilities.alertdialog(act, getString(R.string.val_internet));
                }

                ratingdialog.dismiss();
            }
        });

        tvRateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtilities.isConnectingToInternet(act)) {
                    new GetRating("1").execute();
                } else {

                    CommonUtilities.alertdialog(act, getString(R.string.val_internet));
                }

                layoutratingplaystore.clearAnimation();
            }
        });
    }

    public class CancelNewUpdate extends AsyncTask<String, Void, String> {
        com.GoCarDev.model.ResponseModel updateCancelmodel;

        @Override
        protected String doInBackground(String... params) {

            ServerAccess sa = new ServerAccess();

            updateCancelmodel = sa.getCancelNewUpdate(mActivity);
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
                    CommonUtilities.ShowToast(mActivity,updateCancelmodel.response.msg);
                else
                {
                    if(!mActivity.isFinishing())
                        CommonUtilities.alertdialog(mActivity,getString(R.string.val_something));
                }
            }
        }

        @Override
        protected void onPreExecute() {

        }
    }

    class ValidateLogout extends AsyncTask<String, Void, String> {
        JSONObject response;

        @Override
        protected String doInBackground(String... params) {

            ServerAccess sa = new ServerAccess();
            response = sa.getLogoutDetail(mActivity);

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
                    CommonUtilities.setSecurity_Preference(mActivity, CommonUtilities.pref_SecurityToken,response.getJSONObject("response").getJSONObject("token_info").getString("security_token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CommonUtilities.RemoveALlPreference(act);
                Intent iStart = new Intent(act, Joinus.class);
                iStart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                iStart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                NotificationManager nMgr = (NotificationManager)mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
                nMgr.cancel(9999);
                startActivity(iStart);
            } else {
                try {
                    if(response!=null)
                        CommonUtilities.ShowToast(act, response.getJSONObject("response").getString("msg"));
                    else
                    {
                        if(!mActivity.isFinishing())
                            CommonUtilities.alertdialog(mActivity,getString(R.string.val_something));
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
}
