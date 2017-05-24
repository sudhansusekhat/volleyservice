package com.GoCarDev.serverAccess;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.GoCarDev.BuildConfig;
import com.GoCarDev.Maintenance_Activity;
import com.GoCarDev.model.AuthorizedDrivers;
import com.GoCarDev.model.BookingCheckInvers;
import com.GoCarDev.model.BookingDetail;
import com.GoCarDev.model.BuyCreditsModel;
import com.GoCarDev.model.CancelChargeModel;
import com.GoCarDev.model.CancelTrip;
import com.GoCarDev.model.Car_availability;
import com.GoCarDev.model.ChangePin;
import com.GoCarDev.model.CheckCarPlaceResponse;
import com.GoCarDev.model.CheckMyCarStatus;
import com.GoCarDev.model.Check_customer_status;
import com.GoCarDev.model.Check_trip;
import com.GoCarDev.model.CommonUtilities;
import com.GoCarDev.model.CountryResponse;
import com.GoCarDev.model.CreatePersonalProfileResponse;
import com.GoCarDev.model.CreditCardResponse;
import com.GoCarDev.model.CreditModel;
import com.GoCarDev.model.CurrentTrip;
import com.GoCarDev.model.CustomerPastTrip;
import com.GoCarDev.model.CustomerTrip;
import com.GoCarDev.model.DriverCharges;
import com.GoCarDev.model.FAQModel;
import com.GoCarDev.model.FinalTripDetailsBooking;
import com.GoCarDev.model.FoundMyCar;
import com.GoCarDev.model.Geo_code;
import com.GoCarDev.model.GetTripTimeToStart;
import com.GoCarDev.model.GoBase;
import com.GoCarDev.model.LikeBase;
import com.GoCarDev.model.LocateMyCarResponse;
import com.GoCarDev.model.LoginRespose;
import com.GoCarDev.model.MembershipDetailsResponse;
import com.GoCarDev.model.NotificationReadModel;
import com.GoCarDev.model.NotificationSettingModel;
import com.GoCarDev.model.NotificationSwitchModel;
import com.GoCarDev.model.PromoCodeResponse;
import com.GoCarDev.model.ResponseModel;
import com.GoCarDev.model.ShareTrip;
import com.GoCarDev.model.SignupResponse;
import com.GoCarDev.model.TokenInfoModel;
import com.GoCarDev.model.TripDetailsBooking;
import com.GoCarDev.model.TripInfo;
import com.GoCarDev.model.TripInsurance_response;
import com.GoCarDev.model.VerifyMail;
import com.GoCarDev.model.authorized_booking_response;
import com.GoCarDev.model.booking_promo_response;
import com.GoCarDev.model.car_types;
import com.GoCarDev.model.confirm_booking_response;
import com.GoCarDev.model.go_base_search_list;
import com.GoCarDev.model.go_bases;
import com.GoCarDev.model.goinfo_model;
import com.GoCarDev.model.pre_payment_response;
import com.GoCarDev.model.rating_model;
import com.GoCarDev.model.reduce_extend_response;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class ServerAccess {

    public final static int GET = 1;
    public final static int POST = 2;
    private static JSONObject jsonResponse = null;
    private static HttpPost httppost;

    BookingDetail bookingDetail;
    public static Context act;
    public static String ConvertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {

        } finally {
            try {
                is.close();
            } catch (IOException e) {

            }
        }
        return sb.toString();
    }

    /**
     * Making service call
     *
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     */
    @SuppressWarnings("deprecation")
    public static String PutRequest(String Method, List<NameValuePair> params,
                                    int method) {
        String response = null;
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
            String url = CommonUtilities.WebServiceURL + Method;

            HttpClient httpclient = new DefaultHttpClient();
            ClientConnectionManager ccm = httpclient.getConnectionManager();
            SchemeRegistry schReg = ccm.getSchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            schReg.register(new Scheme("https", new EasySSLSocketFactory(), 443));
            DefaultHttpClient sslClient = new DefaultHttpClient(ccm,
                    httpclient.getParams());

            // Checking http request method type
            params.add(new BasicNameValuePair(CommonUtilities.key_DeviceType,
                    CommonUtilities.Source_Android));
            params.add(new BasicNameValuePair(CommonUtilities.key_AndroidUDID,CommonUtilities.getSecurity_Preference(act, CommonUtilities.key_AndroidUDID)));
            params.add(new BasicNameValuePair(CommonUtilities.PushNotificationToken,CommonUtilities.getPreference(act, CommonUtilities.pref_reg_id)));
            params.add(new BasicNameValuePair(CommonUtilities.security_token,CommonUtilities.getSecurity_Preference(act, CommonUtilities.pref_SecurityToken)));
            params.add(new BasicNameValuePair(CommonUtilities.key_version,BuildConfig.VERSION_NAME));
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                }
                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
                httpResponse = httpClient.execute(httpGet);
            }

            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

            /*Crashlytics.log(1, "service", Method);
            Crashlytics.log(1, "params", params.toString());
            Crashlytics.log(1, "Response", response);*/

            Log.e("Service",Method);
            Log.e("", "Params :> " + params.toString());
            Log.e("", "Response :> " + response);

            if(!response.startsWith("{"))
            {
                //Answers.getInstance().logCustom(new CustomEvent("Server Error"));
                return null;
            }
            if(response.contains("ERR002"))
            {
                Intent intent = new Intent(act, Maintenance_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                act.startActivity(intent);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //Crashlytics.log(1, "catch","exception");
            //Answers.getInstance().logCustom(new CustomEvent("Connection Error"));
            e.printStackTrace();
        }
        return response;
    }

    public static String PutContextRequest(Activity act, String Method, List<NameValuePair> params,
                                           int method) {
        String response = null;
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
            String url = CommonUtilities.WebServiceURL + Method;

            HttpClient httpclient = new DefaultHttpClient();
            ClientConnectionManager ccm = httpclient.getConnectionManager();
            SchemeRegistry schReg = ccm.getSchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            schReg.register(new Scheme("https", new EasySSLSocketFactory(), 443));
            DefaultHttpClient sslClient = new DefaultHttpClient(ccm,
                    httpclient.getParams());

            // Checking http request method type
            params.add(new BasicNameValuePair(CommonUtilities.key_DeviceType,
                    CommonUtilities.Source_Android));
            params.add(new BasicNameValuePair(CommonUtilities.key_AndroidUDID,CommonUtilities.getSecurity_Preference(act, CommonUtilities.key_AndroidUDID)));
            params.add(new BasicNameValuePair(CommonUtilities.PushNotificationToken,
                    CommonUtilities.getPreference(act, CommonUtilities.pref_reg_id)));
            params.add(new BasicNameValuePair(CommonUtilities.security_token,
                    CommonUtilities.getSecurity_Preference(act, CommonUtilities.pref_SecurityToken)));
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
                }
                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
                httpResponse = httpClient.execute(httpGet);
            }

            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

            /*Crashlytics.log(1, "service", Method);
            Crashlytics.log(1, "params", params.toString());
            Crashlytics.log(1, "Response", response);*/

            Log.e("Service",Method);
            Log.e("", "Params :> " + params.toString());
            Log.e("", "Response :> " + response);

            if(!response.startsWith("{"))
            {
              /*  Intent intent = new Intent(act, StatusCodeActivity.class);
                act.startActivity(intent);*/
               return null;
            }
            if(response.contains("ERR002"))
            {
                Intent i = new Intent(act, Maintenance_Activity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                act.startActivity(i);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //Crashlytics.log(1, "catch","exception");
            e.printStackTrace();
        }

        return response;

    }

    public static String PutGeoCodeRequest(String Method, List<NameValuePair> params,
                                           int method) {
        String response = null;
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
            String url = Method;
            // Checking http request method type


            HttpGet httpGet = new HttpGet(url);
            httpResponse = httpClient.execute(httpGet);

            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }

    public car_types car_types(Context context) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();


            String response = ServerAccess.PutRequest(CommonUtilities.key_car_types, params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }

            car_types res = (car_types) gson.fromJson(response,
                    car_types.class);

            return res;

        } catch (Exception ex) {
            return null;
        }
    }

    public rating_model rating_app(Activity act, String customer_id, String is_rated, String app_version) {
        this.act = act;
        try {
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, customer_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_is_rated, is_rated));
            params.add(new BasicNameValuePair(CommonUtilities.key_version,app_version));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_rate_application, params,
                    ServerAccess.POST);

            if (response == null) {
                return null;
            }
            rating_model res = (rating_model) gson.fromJson(response,
                    rating_model.class);

            return res;

        } catch (Exception ex) {
            return null;
        }
    }

    public go_bases go_bases(Context context,String id, String latitude, String longitude, String text) {
        this.act = context;
        try {
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id,
                    id));
            params.add(new BasicNameValuePair(CommonUtilities.key_latitude,
                    latitude));
            params.add(new BasicNameValuePair(CommonUtilities.key_longitude,
                    longitude));

            params.add(new BasicNameValuePair("flag",
                    text));

            String response = ServerAccess.PutRequest(CommonUtilities.key_go_bases, params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }
            go_bases res = (go_bases) gson.fromJson(response,
                    go_bases.class);

            return res;

        } catch (Exception ex) {
            return null;
        }
    }

    public TokenInfoModel GenerateToken(Context context,String token,String ipaddr,String customer_id) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.encoded_key, token));
            params.add(new BasicNameValuePair("IPAddress", ipaddr));
            params.add(new BasicNameValuePair(CommonUtilities.key_version,BuildConfig.VERSION_NAME));
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id,customer_id));

            String response = ServerAccess.PutRequest(CommonUtilities.key_generate_token, params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }
            TokenInfoModel res = (TokenInfoModel) gson.fromJson(response,
                    TokenInfoModel.class);

            return res;

        } catch (Exception ex) {
            return null;
        }
    }


    public LoginRespose GetAuthenticate(Activity act, String customerid, String pin,String version) {
        try {
            this.act = act;
            MCrypt aesCrypt = new MCrypt();
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id,
                    aesCrypt.bytesToHex(aesCrypt.encrypt(customerid))));
            params.add(new BasicNameValuePair(CommonUtilities.key_password,
                    aesCrypt.bytesToHex(aesCrypt.encrypt(pin))));
            params.add(new BasicNameValuePair(CommonUtilities.key_version,
                    version));

            params.add(new BasicNameValuePair(CommonUtilities.key_device_os_version,
                    Build.VERSION.RELEASE));
            params.add(new BasicNameValuePair(CommonUtilities.key_device_name,
                    Build.MANUFACTURER + " " + Build.MODEL));

            String response = ServerAccess.PutContextRequest(act,
                    CommonUtilities.key_authenticate_customer, params,
                    ServerAccess.POST);

            if (response == null) {
                return null;
            }
            LoginRespose res = (LoginRespose) gson.fromJson(response,
                    LoginRespose.class);
            return res;
        } catch (Exception ex) {
            return null;
        }

    }

    public CheckMyCarStatus GetCarStatus(Activity act, String customerid,String bookingid) {
        try {
            this.act = act;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id,
                    customerid));
            params.add(new BasicNameValuePair(CommonUtilities.key_booking_id,
                    bookingid));

            String response = ServerAccess.PutRequest(CommonUtilities.key_check_my_car_status, params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }
            CheckMyCarStatus res = (CheckMyCarStatus) gson.fromJson(response,
                    CheckMyCarStatus.class);
            return res;
        } catch (Exception ex) {
            return null;
        }

    }

    public JSONObject GetValidateUser(Context context,String MobileNo) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id,MobileNo));
            params.add(new BasicNameValuePair("pre_login","0"));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_authenticate_customer_id, params,
                    ServerAccess.POST);

            if (response == null) {
                return null;
            }
            JSONObject obj = new JSONObject(response);

            return obj;


        } catch (Exception ex) {
            return null;
        }
    }

    public JSONObject GetForgotPasswordEmail(Context context,int flag,String customerno) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_flag,
                    String.valueOf(flag)));
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id,
                    customerno));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_forgot_password, params,
                    ServerAccess.POST);

            if (response == null) {
                return null;
            }
            JSONObject obj = new JSONObject(response);

            return obj;

        } catch (Exception ex) {
            return null;
        }
    }
    public JSONObject GetForgotCustId(Context context,String email_address) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_email_address,
                    email_address));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_forgot_account, params,
                    ServerAccess.POST);

            if (response == null) {
                return null;
            }
            JSONObject obj = new JSONObject(response);

            return obj;

        } catch (Exception ex) {
            return null;
        }
    }


    public SignupResponse GetRegister(Activity act, String first_name, String surname, String email, String phone, String date_of_birth, String driving_licence, String driving_licence2, String terms_agreed, String privacy_agreed) {
        try {
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonUtilities.key_first_name,
                    first_name));
            params.add(new BasicNameValuePair(CommonUtilities.key_last_name,
                    surname));
            params.add(new BasicNameValuePair(CommonUtilities.key_email,
                    email));
            params.add(new BasicNameValuePair(CommonUtilities.key_phone,
                    phone));
            params.add(new BasicNameValuePair(CommonUtilities.key_date_of_birth,
                    date_of_birth));

            params.add(new BasicNameValuePair(CommonUtilities.key_driving_licence,
                    driving_licence));
            params.add(new BasicNameValuePair(CommonUtilities.key_driving_licence2,
                    driving_licence2));
            params.add(new BasicNameValuePair(CommonUtilities.key_terms_agreed,
                    terms_agreed));
            params.add(new BasicNameValuePair(CommonUtilities.key_privacy_agreed,
                    privacy_agreed));
            params.add(new BasicNameValuePair(CommonUtilities.key_device_os_version,
                    Build.VERSION.RELEASE));
            params.add(new BasicNameValuePair(CommonUtilities.key_device_name,
                    Build.MANUFACTURER + " " + Build.MODEL));
            params.add(new BasicNameValuePair(CommonUtilities.key_version,BuildConfig.VERSION_NAME));
            String response = ServerAccess.PutContextRequest(act,
                    CommonUtilities.key_register, params,
                    ServerAccess.POST);

            if (response == null) {
                return null;
            }

            SignupResponse res = (SignupResponse) gson.fromJson(response, SignupResponse.class);

            return res;

        } catch (Exception ex) {
            return null;
        }

    }

    public authorized_booking_response GetDriverBooking(Context context,String customer_id) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id,
                    customer_id));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_authorized_drivers, params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }

            authorized_booking_response res = (authorized_booking_response) gson.fromJson(response, authorized_booking_response.class);

            return res;

        } catch (Exception ex) {
            return null;
        }

    }

    public TripInsurance_response GetTripInsurance(Context context,String customer_id, String startTime, String endTime) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, customer_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_booking_start, startTime));

            params.add(new BasicNameValuePair(CommonUtilities.key_booking_end, endTime));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_trip_insurance, params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }

            TripInsurance_response res = (TripInsurance_response) gson.fromJson(response, TripInsurance_response.class);

            return res;

        } catch (Exception ex) {
            return null;
        }
    }


    public ChangePin GetChangePin(Context context, String customer_id, String oldPin, String newPin,String forceFlag) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, customer_id));
            params.add(new BasicNameValuePair("old_pin", oldPin));
            params.add(new BasicNameValuePair("new_pin", newPin));
            params.add(new BasicNameValuePair("force_flag", forceFlag));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_change_pin, params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }
            ChangePin res = (ChangePin) gson.fromJson(response, ChangePin.class);

            return res;

        } catch (Exception ex) {
            return null;
        }
    }


    public TripInsurance_response GetForgetSomething(Context context,String booking_id,String customer_id) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, customer_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_booking_id, booking_id));



            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_forget_something, params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }

            TripInsurance_response res = (TripInsurance_response) gson.fromJson(response, TripInsurance_response.class);

            return res;

        } catch (Exception ex) {
            return null;
        }
    }
    public reduce_extend_response GetExtendsReduce(Context context,String booking_start,String booking_stop,String customer_id,String extend_flag,String booking_type,String booking_id,String number) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_booking_start, booking_start));
            params.add(new BasicNameValuePair(CommonUtilities.key_booking_stop, booking_stop));
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, customer_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_extend_flag, extend_flag));
            params.add(new BasicNameValuePair(CommonUtilities.key_booking_type, booking_type));
            params.add(new BasicNameValuePair(CommonUtilities.key_booking_id, booking_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_inverse_call_no, number));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_extend_trip, params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }

            reduce_extend_response res = (reduce_extend_response) gson.fromJson(response, reduce_extend_response.class);

            return res;

        } catch (Exception ex) {
            return null;
        }

    }
    public booking_promo_response apply_promo_confirm(Context context,String driver_id,String promo_code,String customer_id,String base_id) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonUtilities.key_driver_id, driver_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_promo_code, promo_code));
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, customer_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_selected_base_id, base_id));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_apply_promo_on_trip, params,
                    ServerAccess.POST);

            if (response == null) {
                return null;
            }

            booking_promo_response res = (booking_promo_response) gson.fromJson(response, booking_promo_response.class);

            return res;

        } catch (Exception ex) {
            return null;
        }

    }
    public confirm_booking_response ConfirmTrip(Context context,String customer_id,String startTime, String endTime,String car_type_id,String base_id,String car_id,String driver_id,String add_Insurance,String promo_code_id, String slot_id ) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, customer_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_booking_start, startTime));
            params.add(new BasicNameValuePair(CommonUtilities.key_booking_stop, endTime));

            params.add(new BasicNameValuePair(CommonUtilities.key_car_type_id, car_type_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_base_id, base_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_car_id, car_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_driver_id, driver_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_add_Insurance, add_Insurance));
            params.add(new BasicNameValuePair(CommonUtilities.key_promo_code_id, promo_code_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_slot_id, slot_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_version,BuildConfig.VERSION_NAME));
            params.add(new BasicNameValuePair(CommonUtilities.key_device_os_version,Build.VERSION.RELEASE));
            params.add(new BasicNameValuePair(CommonUtilities.key_device_name,Build.MANUFACTURER + " " + Build.MODEL));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_confirm_booking, params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }

            confirm_booking_response res = (confirm_booking_response) gson.fromJson(response, confirm_booking_response.class);

            return res;

        } catch (Exception ex) {
            return null;
        }

    }
    public pre_payment_response pre_payment(Context context,String customer_id,String startTime, String endTime,String car_type_id,String base_id,String car_id,String driver_id,String add_Insurance,String promo_code_id,String slot_id ) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, customer_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_booking_start, startTime));
            params.add(new BasicNameValuePair(CommonUtilities.key_booking_stop, endTime));

            params.add(new BasicNameValuePair(CommonUtilities.key_car_type_id, car_type_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_base_id, base_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_car_id, car_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_driver_id, driver_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_add_Insurance, add_Insurance));
            params.add(new BasicNameValuePair(CommonUtilities.key_promo_code_id, promo_code_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_slot_id, slot_id));
            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_pay_prepayment, params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }

            pre_payment_response res = (pre_payment_response) gson.fromJson(response, pre_payment_response.class);

            return res;

        } catch (Exception ex) {
            return null;
        }

    }
    public BookingCheckInvers get_invers_status_booking(Context context,String booking_id,String inverse_call_no) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();


            params.add(new BasicNameValuePair(CommonUtilities.key_booking_id, booking_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_inverse_call_no, inverse_call_no));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_check_booking_status, params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }

            BookingCheckInvers res = (BookingCheckInvers) gson.fromJson(response, BookingCheckInvers.class);

            return res;

        } catch (Exception ex) {
            return null;
        }

    }

    public VerifyMail GetEmailVerify(Context context,String Custid) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();


            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, Custid));
            params.add(new BasicNameValuePair("pre_login","1"));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.verify_email, params,
                    ServerAccess.POST);


            if (response == null) {
                return null;
            }

            VerifyMail res = (VerifyMail) gson.fromJson(response,
                    VerifyMail.class);
            return res;
        } catch (Exception ex) {
            return null;
        }

    }


    public LoginRespose GetBurgerMenuDetails(Context context,String Custid,String ipadd) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id,
                    Custid));
            params.add(new BasicNameValuePair("IPAddress",
                    ipadd));
            params.add(new BasicNameValuePair(CommonUtilities.key_version,
                    BuildConfig.VERSION_NAME));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.burgermenu_details, params,
                    ServerAccess.POST);

            if (response == null) {
                return null;
            }

            LoginRespose res = (LoginRespose) gson.fromJson(response,
                    LoginRespose.class);
            return res;
        } catch (Exception ex) {
            return null;
        }
    }

    public MembershipDetailsResponse GetJoinusdetails(Context context,String custid){
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, custid));
            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_membership_details, params,
                    ServerAccess.POST);

            if (response == null) {
                return null;
            }
            MembershipDetailsResponse res = (MembershipDetailsResponse) gson.fromJson(response,
                    MembershipDetailsResponse.class);
            return res;
        } catch (Exception ex) {
            return null;
        }

    }

    public ResponseModel GetPay(Context context,String custid,String triptype,String membershp_type){
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, custid));
            params.add(new BasicNameValuePair("type_id", triptype));
            params.add(new BasicNameValuePair("membership_type", membershp_type));
            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_pay_now, params,
                    ServerAccess.POST);

            if (response == null) {
                return null;
            }
            ResponseModel res = (ResponseModel) gson.fromJson(response,
                    ResponseModel.class);
            return res;
        } catch (Exception ex) {
            return null;
        }

    }

    public SignupResponse GetPayment(Context context,String addcard, String user_id, String txtcardNumber, String expiry, String cardname, String cvv, String cardType, String signup_process) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonUtilities.key_add_card,
                    addcard));
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id,
                    user_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_card_number,
                    txtcardNumber));

            params.add(new BasicNameValuePair(CommonUtilities.key_expiry,
                    expiry));
            params.add(new BasicNameValuePair(CommonUtilities.key_card_holder_name,
                    cardname));
            params.add(new BasicNameValuePair(CommonUtilities.key_cvv,
                    cvv));
            params.add(new BasicNameValuePair(CommonUtilities.key_card_type,
                    cardType)); params.add(new BasicNameValuePair(CommonUtilities.key_signup_process,
                    signup_process));


            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_add_edit_credit_card, params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }

            SignupResponse res = (SignupResponse) gson.fromJson(response, SignupResponse.class);

            return res;

        } catch (Exception ex) {
            return null;
        }

    }

    public CustomerTrip getMyTrips(Context context,String customerId, String tripType, String pageNo) {
        this.act = context;
        String response = "";
        Gson gson = new Gson();
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, customerId));
        params.add(new BasicNameValuePair(CommonUtilities.key_trip_type, tripType));
        params.add(new BasicNameValuePair(CommonUtilities.key_page_no, pageNo));

        response = ServerAccess.PutRequest(CommonUtilities.key_customer_trips, params, ServerAccess.POST);
        if (response == null) {
            return null;
        }
        CustomerTrip trip = (CustomerTrip) gson.fromJson(response, CustomerTrip.class);
        return trip;
    }

    public TripInfo getTripInfo(Context context,String customerId, String bookinId) {
        this.act = context;
        String response = "";
        Gson gson = new Gson();
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, customerId));
        params.add(new BasicNameValuePair(CommonUtilities.key_booking_id, bookinId));

        response = ServerAccess.PutRequest(CommonUtilities.key_trip_info, params, ServerAccess.POST);

        if (response == null) {
            return null;
        }
        TripInfo trip = (TripInfo) gson.fromJson(response, TripInfo.class);
        return trip;
    }

    public CustomerPastTrip getMyPastTrips(Context context,String customerId, String tripType, String pageNo) {
        this.act = context;
        String response = "";
        Gson gson = new Gson();
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, customerId));
        params.add(new BasicNameValuePair(CommonUtilities.key_trip_type, tripType));
        params.add(new BasicNameValuePair(CommonUtilities.key_page_no, pageNo));

        response = ServerAccess.PutRequest(CommonUtilities.key_customer_trips, params, ServerAccess.POST);
        if (response == null) {
            return null;
        }
        CustomerPastTrip trip = (CustomerPastTrip) gson.fromJson(response, CustomerPastTrip.class);
        return trip;
    }

    public ShareTrip getShareUrl(Context context,String bookingId) {
        this.act = context;
        String response = "";
        Gson gson = new Gson();
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(CommonUtilities.key_booking_id, bookingId));

        response = ServerAccess.PutRequest(CommonUtilities.key_share_trip, params, ServerAccess.POST);

        if (response == null) {
            return null;
        }
        ShareTrip trip = (ShareTrip) gson.fromJson(response, ShareTrip.class);
        return trip;
    }

    public LoginRespose GetUpdateName(Context context,String[] keys, String[] values) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(keys[0], values[0]));
            params.add(new BasicNameValuePair(CommonUtilities.key_flag,
                    CommonUtilities.key_flag_name));
            params.add(new BasicNameValuePair(CommonUtilities.key_user_flag,
                    CommonUtilities.key_customer_flag));
            params.add(new BasicNameValuePair(keys[1], values[1]));
            params.add(new BasicNameValuePair(keys[2], values[2]));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.update_profile, params,
                    ServerAccess.POST);


            if (response == null) {
                return null;
            }
            LoginRespose res = (LoginRespose) gson.fromJson(response,
                    LoginRespose.class);
            return res;
        } catch (Exception ex) {
            return null;
        }
    }

    public LoginRespose GetUpdateDetails(Context context,String[] keys, String[] values) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(keys[0], values[0]));
            params.add(new BasicNameValuePair(CommonUtilities.key_user_flag,
                    CommonUtilities.key_customer_flag));
            params.add(new BasicNameValuePair(keys[1], values[1]));
            params.add(new BasicNameValuePair(keys[2], values[2]));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.update_profile, params,
                    ServerAccess.POST);


            if (response == null) {
                return null;
            }
            LoginRespose res = (LoginRespose) gson.fromJson(response,
                    LoginRespose.class);
            return res;
        } catch (Exception ex) {
            return null;
        }

    }

    public LoginRespose GetUpdateAddress(Context context,String[] values) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id,
                    values[0]));
            params.add(new BasicNameValuePair(CommonUtilities.key_user_flag,
                    CommonUtilities.key_customer_flag));
            params.add(new BasicNameValuePair(CommonUtilities.key_flag,
                    CommonUtilities.key_flag_address));
            params.add(new BasicNameValuePair(CommonUtilities.key_address_line1,
                    values[1]));
            params.add(new BasicNameValuePair(CommonUtilities.key_address_line2,
                    values[2]));
            params.add(new BasicNameValuePair(CommonUtilities.key_city,
                    values[3]));
            params.add(new BasicNameValuePair(CommonUtilities.key_country_id,
                    values[4]));
            params.add(new BasicNameValuePair(CommonUtilities.key_zipcode,
                    values[5]));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.update_profile, params,
                    ServerAccess.POST);


            if (response == null) {
                return null;
            }
            LoginRespose res = (LoginRespose) gson.fromJson(response,
                    LoginRespose.class);
            return res;
        } catch (Exception ex) {
            return null;
        }

    }

    public CountryResponse GetCountry(Context context) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_countries, params,
                    ServerAccess.POST);


            if (response == null) {
                return null;
            }
            CountryResponse res = (CountryResponse) gson.fromJson(response,
                    CountryResponse.class);
            return res;
        } catch (Exception ex) {
            return null;
        }

    }


    public LoginRespose GetUserDetails(Context context) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            String customer_id = CommonUtilities.getPreference(context, CommonUtilities.pref_customer_id);

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id,
                    customer_id));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.update_profile, params,
                    ServerAccess.POST);


            if (response == null) {
                return null;
            }

            LoginRespose res = (LoginRespose) gson.fromJson(response,
                    LoginRespose.class);
            return res;
        } catch (Exception ex) {
            return null;
        }

    }

    public String reverse_geocode(String latitude, String longitude) {
        try {
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();


            String response = ServerAccess.PutGeoCodeRequest(CommonUtilities.getPostalCodeURL(latitude, longitude), params,
                    ServerAccess.GET);

            if (response == null) {
                return null;
            }

            Geo_code res = (Geo_code) gson.fromJson(response,
                    Geo_code.class);


            StringBuilder address_name = new StringBuilder();
            if (res != null) {
                if (res.results != null)
                    if (res.results.size() > 0) {
                        if (res.results.get(0).formatted_address.contains(",")) {

                            String[] address = res.results.get(0).formatted_address.split(",");
                            Double length = Math.ceil(Double.parseDouble(String.valueOf(address.length)) / Double.parseDouble("2"));
                            for (int i = 0; i < address.length; i++) {
                                address_name.append(address[i]);

                                if (i == (length - 1)) {
                                    address_name.append("\n");
                                } else {
                                    if (i != (address.length - 1))
                                        address_name.append(",");
                                }
                            }
                            return address_name.toString();

                        } else {
                            return res.results.get(0).formatted_address.toString();
                        }

                    }

            }
            return "";
        } catch (Exception ex) {
            return null;
        }

    }

    public Check_customer_status check_customer_status(Context context,String id) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id,
                    id));


            String response = ServerAccess.PutRequest(CommonUtilities.key_check_customer_status, params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }

            Check_customer_status res = (Check_customer_status) gson.fromJson(response,
                    Check_customer_status.class);

            return res;

        } catch (Exception ex) {
            return null;
        }
    }

    public Car_availability car_availability(Context context,BookingDetail detail) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, detail.customer_id));
            params.add(new BasicNameValuePair("booking_start", detail.departure_time));
            params.add(new BasicNameValuePair("booking_stop", detail.arrival_time));
            params.add(new BasicNameValuePair("car_type_id", detail.car_type_id));
            params.add(new BasicNameValuePair("base_id", detail.base_id));


            String response = ServerAccess.PutRequest("car_availability", params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }

            Car_availability res = (Car_availability) gson.fromJson(response,
                    Car_availability.class);

            return res;

        } catch (Exception ex) {
            return null;
        }
    }

    public FAQModel GetFAQ(Context context) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            String response = ServerAccess.PutRequest(CommonUtilities.key_Faqs, params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }
            FAQModel res = (FAQModel) gson.fromJson(response,
                    FAQModel.class);

            return res;

        } catch (Exception ex) {
            return null;
        }
    }

    public ResponseModel ResendMail(Context context,String custid, String bookid) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id,
                    custid));
            params.add(new BasicNameValuePair(CommonUtilities.pref_booking_id,
                    bookid));

            String response = ServerAccess.PutRequest(CommonUtilities.key_Resendmails, params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }
            ResponseModel res = (ResponseModel) gson.fromJson(response,
                    ResponseModel.class);

            return res;

        } catch (Exception ex) {
            return null;
        }
    }

    public CancelTrip CancelBooking(Context context,String custid, String bookid,String number) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id,
                    custid));
            params.add(new BasicNameValuePair(CommonUtilities.pref_booking_id,
                    bookid));
            params.add(new BasicNameValuePair(CommonUtilities.key_inverse_call_no,
                    number));

            String response = ServerAccess.PutRequest(CommonUtilities.key_cancel_booking, params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }
            CancelTrip res = (CancelTrip) gson.fromJson(response,
                    CancelTrip.class);

            return res;

        } catch (Exception ex) {
            return null;
        }
    }

    public CancelChargeModel CancelBookingCharge(Context context,String bookid) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.pref_booking_id,
                    bookid));
            String response = ServerAccess.PutRequest(CommonUtilities.key_cancel_booking_charges, params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }
            CancelChargeModel res = (CancelChargeModel) gson.fromJson(response,
                    CancelChargeModel.class);

            return res;

        } catch (Exception ex) {
            return null;
        }
    }

    public TripDetailsBooking EditBooking(Context context,String custid, String bookid, String bookstr, String bookend,String number) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id,
                    custid));
            params.add(new BasicNameValuePair(CommonUtilities.pref_booking_id,
                    bookid));
            params.add(new BasicNameValuePair(CommonUtilities.key_booking_start,
                    bookstr));
            params.add(new BasicNameValuePair(CommonUtilities.key_booking_stop,
                    bookend));
            params.add(new BasicNameValuePair(CommonUtilities.key_inverse_call_no,
                    number));

            String response = ServerAccess.PutRequest(CommonUtilities.key_edit_trip, params,
                    ServerAccess.POST);

            if (response == null) {
                return null;
            }
            TripDetailsBooking res = (TripDetailsBooking) gson.fromJson(response,
                    TripDetailsBooking.class);

            return res;

        } catch (Exception ex) {
            return null;
        }
    }

    public goinfo_model GetGoCarInfo(Context context,String Custid) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id,
                    Custid));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.gocar_information, params,
                    ServerAccess.POST);


            if (response == null) {
                return null;
            }
            goinfo_model res = (goinfo_model) gson.fromJson(response,
                    goinfo_model.class);
            return res;
        } catch (Exception ex) {
            return null;
        }

    }

    public Check_trip Check_trip(Context context,BookingDetail detail,String cid,String slot_id) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, cid));
            params.add(new BasicNameValuePair("booking_start", detail.departure_time));
            params.add(new BasicNameValuePair("booking_stop", detail.arrival_time));
            params.add(new BasicNameValuePair("car_type_id", detail.car_type_id));
            params.add(new BasicNameValuePair("base_id", detail.base_id));
            params.add(new BasicNameValuePair("slot_id",slot_id));


            String response = ServerAccess.PutRequest(CommonUtilities.key_check_trip, params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }

            Check_trip res = (Check_trip) gson.fromJson(response,
                    Check_trip.class);

            return res;

        } catch (Exception ex) {
            return null;
        }
    }


    public CreatePersonalProfileResponse GetUpdatePersonalEmail(Context context,String[] keys, String[] values) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(keys[0], values[0]));
            params.add(new BasicNameValuePair(keys[1], values[1]));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.create_personal_profile, params,
                    ServerAccess.POST);

            if (response == null) {
                return null;
            }
            CreatePersonalProfileResponse res = (CreatePersonalProfileResponse) gson.fromJson(response,
                    CreatePersonalProfileResponse.class);
            return res;
        } catch (Exception ex) {
            return null;
        }

    }


    public AuthorizedDrivers getAuthorozedDrivers(Context context,String customerId, String pageno) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, customerId));
            params.add(new BasicNameValuePair(CommonUtilities.key_page_no, pageno));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_customer_drivers, params,
                    ServerAccess.POST);


            if (response == null) {
                return null;
            }
            AuthorizedDrivers authorizedDrivers = (AuthorizedDrivers) gson.fromJson(response, AuthorizedDrivers.class);
            return authorizedDrivers;
        } catch (Exception ex) {
            return null;
        }
    }

    public DriverCharges GetDriverCharges(Context context,String customerId) {
        try {
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, customerId));

            String response = ServerAccess.PutRequest(CommonUtilities.key_driver_charges, params, ServerAccess.POST);

            if (response == null) {
                return null;
            }
            DriverCharges authorizedDrivers = (DriverCharges) gson.fromJson(response, DriverCharges.class);
            return authorizedDrivers;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public LoginRespose UpdateDriverProfile(Context context,String[] keys, String[] values) {
        this.act = context;
        LoginRespose model;
        try {
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(keys[0], values[0]));
            params.add(new BasicNameValuePair(CommonUtilities.key_user_flag, CommonUtilities.key_driver_flag));
            params.add(new BasicNameValuePair(keys[1], values[1]));
            params.add(new BasicNameValuePair(keys[2], values[2]));

            String response = ServerAccess.PutRequest(CommonUtilities.update_profile, params, ServerAccess.POST);

            if (response == null) {
                return null;
            }
            model = (LoginRespose) gson.fromJson(response, LoginRespose.class);
            return model;
        } catch (Exception ex) {
            return null;
        }

    }

    public AuthorizedDrivers AddDriver(Context context,String[] keys, String[] values) {
        AuthorizedDrivers model;
        this.act = context;
        try {
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            for (int i = 0; i <= 13; i++) {
                params.add(new BasicNameValuePair(keys[i], values[i]));
            }
            String response = ServerAccess.PutRequest(CommonUtilities.key_add_driver, params, ServerAccess.POST);

            model = (AuthorizedDrivers) gson.fromJson(response, AuthorizedDrivers.class);
            if (response == null) {
                return null;
            }
            return model;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public AuthorizedDrivers UpdateDriverProfile(Context context,String[] keys, String[] values, int flag) {
        this.act = context;
        AuthorizedDrivers model;
        try {
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(keys[0], values[0]));
            params.add(new BasicNameValuePair(CommonUtilities.key_user_flag, CommonUtilities.key_driver_flag));
            if (flag == 1) {
                params.add(new BasicNameValuePair(keys[1], values[1]));
                params.add(new BasicNameValuePair(keys[2], values[2]));
                params.add(new BasicNameValuePair(keys[3], values[3]));
            } else if (flag == 2 || flag == 3 || flag == 5) {
                params.add(new BasicNameValuePair(keys[1], values[1]));
                params.add(new BasicNameValuePair(keys[2], values[2]));
            } else if (flag == 4) {
                params.add(new BasicNameValuePair(keys[1], values[1]));
                params.add(new BasicNameValuePair(keys[2], values[2]));
                params.add(new BasicNameValuePair(keys[3], values[3]));
                params.add(new BasicNameValuePair(keys[4], values[4]));
                params.add(new BasicNameValuePair(keys[5], values[5]));
                params.add(new BasicNameValuePair(keys[6], values[6]));
            }

            String response = ServerAccess.PutRequest(CommonUtilities.update_profile, params, ServerAccess.POST);
            if (response == null) {
                return null;
            }
            model = (AuthorizedDrivers) gson.fromJson(response, AuthorizedDrivers.class);
            return model;
        } catch (Exception ex) {
            return null;
        }

    }

    public CreditCardResponse GetCreditCardsDetails(Context context,String[] key, String[] value) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();


            params.add(new BasicNameValuePair(key[0], value[0]));
            params.add(new BasicNameValuePair(key[1], value[1]));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.credit_cards, params,
                    ServerAccess.POST);


            if (response == null) {
                return null;
            }
            CreditCardResponse res = (CreditCardResponse) gson.fromJson(response, CreditCardResponse.class);
            return res;


        } catch (Exception ex) {
            return null;
        }
    }


    public CreditCardResponse GetUploadCreditCard(Context context,String[] values) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_addcard, CommonUtilities.key_addcardvalue));

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, values[0]));
            params.add(new BasicNameValuePair(CommonUtilities.key_card_number, values[1]));
            params.add(new BasicNameValuePair(CommonUtilities.key_expiry, values[2]));
            params.add(new BasicNameValuePair(CommonUtilities.key_card_holder_name, values[3]));
            params.add(new BasicNameValuePair(CommonUtilities.key_cvv, values[4]));
            params.add(new BasicNameValuePair(CommonUtilities.key_default, values[5]));
            params.add(new BasicNameValuePair(CommonUtilities.key_card_type, values[6]));
            params.add(new BasicNameValuePair(CommonUtilities.key_card_name, values[7]));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_add_edit_credit_card, params,
                    ServerAccess.POST);


            if (response == null) {
                return null;
            }
            CreditCardResponse res = (CreditCardResponse) gson.fromJson(response,
                    CreditCardResponse.class);
            return res;
        } catch (Exception ex) {
            return null;
        }

    }

    public JSONObject getDeleteDefaultCard(Context context,String[] values) {
        try {
            this.act = context;
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, values[0]));
            params.add(new BasicNameValuePair(CommonUtilities.key_card_id, values[1]));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_remove_credit_card, params,
                    ServerAccess.POST);


            if (response == null) {
                return null;
            }
            JSONObject obj = new JSONObject(response);

            return obj;


        } catch (Exception ex) {
            return null;
        }
    }

    public JSONObject getUpdateDefaultCard(Context context,String[] values) {
        try {
            this.act = context;
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_addcard, CommonUtilities.key_updatecardvalue));

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, values[0]));
            params.add(new BasicNameValuePair(CommonUtilities.key_card_id, values[1]));
            params.add(new BasicNameValuePair(CommonUtilities.key_card_name, values[2]));
            params.add(new BasicNameValuePair(CommonUtilities.key_default, values[3]));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_add_edit_credit_card, params,
                    ServerAccess.POST);


            if (response == null) {
                return null;
            }
            JSONObject obj = new JSONObject(response);

            return obj;


        } catch (Exception ex) {
            return null;
        }
    }
    public PromoCodeResponse GetPromoCodeResponse(Context context,String[] keys, String[] values) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            for (int i=0;i<values.length;i++)
            {
                params.add(new BasicNameValuePair(keys[i], values[i]));
            }

            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_promo_code, params,
                    ServerAccess.POST);


            if (response == null) {
                return null;
            }
            PromoCodeResponse res = (PromoCodeResponse) gson.fromJson(response,
                    PromoCodeResponse.class);
            return res;
        } catch (Exception ex) {
            return null;
        }

    }

    public JSONObject getLogoutDetail(Activity act) {
        try {

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            String customer_id = CommonUtilities.getPreference(act, CommonUtilities.pref_customer_id);
            String login_history_id = CommonUtilities.getSecurity_Preference(act, CommonUtilities.pref_login_history_id);

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, customer_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_login_history_id, login_history_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_version, BuildConfig.VERSION_NAME));

            String response = ServerAccess.PutContextRequest(act,
                    CommonUtilities.logout, params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }
            JSONObject obj = new JSONObject(response);


            return obj;


        } catch (Exception ex) {
            return null;
        }
    }
    public CreditModel BuyCredits(Context context){
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_buy_credits_range, params,
                    ServerAccess.POST);

            if (response == null) {
                return null;
            }
            CreditModel res = (CreditModel) gson.fromJson(response,
                    CreditModel.class);
            return res;
        } catch (Exception ex) {
            return null;
        }

    }

    public BuyCreditsModel GetCredits(Context context,String custid,String amnt){
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, custid));
            params.add(new BasicNameValuePair("amount", amnt));
            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_buy_credits, params,
                    ServerAccess.POST);

            if (response == null) {
                return null;
            }
            BuyCreditsModel res = (BuyCreditsModel) gson.fromJson(response,
                    BuyCreditsModel.class);
            return res;
        } catch (Exception ex) {
            return null;
        }

    }

    public ResponseModel GetAddCredits(Context context,String custid,String amnt){
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, custid));
            params.add(new BasicNameValuePair("amount", amnt));
            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_add_credits, params,
                    ServerAccess.POST);

            if (response == null) {
                return null;
            }
            ResponseModel res = (ResponseModel) gson.fromJson(response,
                    ResponseModel.class);
            return res;
        } catch (Exception ex) {
            return null;
        }

    }

    public NotificationSettingModel GetNotificationList(Context context,String custid,String pushmsgid){
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, custid));
            params.add(new BasicNameValuePair("push_message_id", pushmsgid));
            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_get_customer_notifications, params,
                    ServerAccess.POST);

            if (response == null) {
                return null;
            }
            NotificationSettingModel res = (NotificationSettingModel) gson.fromJson(response,
                    NotificationSettingModel.class);
            return res;
        } catch (Exception ex) {
            return null;
        }

    }

    public NotificationSwitchModel GetNotification(Context context,String custid,Integer status){
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, custid));
            params.add(new BasicNameValuePair("status", String.valueOf(status)));
            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_set_notification_status, params,
                    ServerAccess.POST);

            if (response == null) {
                return null;
            }
            NotificationSwitchModel res = (NotificationSwitchModel) gson.fromJson(response,
                    NotificationSwitchModel.class);
            return res;
        } catch (Exception ex) {
            return null;
        }

    }

    public NotificationReadModel GetNotfRead(Context context,String custid){
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, custid));
            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_read_notifications, params,
                    ServerAccess.POST);

            if (response == null) {
                return null;
            }
            NotificationReadModel res = (NotificationReadModel) gson.fromJson(response,
                    NotificationReadModel.class);
            return res;
        } catch (Exception ex) {
            return null;
        }

    }
    public GoBase getFavouriteGoBase(Context context,String customerId,String pageno)
    {
        this.act = context;
        try{
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id,customerId));
            params.add(new BasicNameValuePair(CommonUtilities.key_page_no, pageno));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_favourite_gobase, params,
                    ServerAccess.POST);


            if (response == null) {
                return null;
            }
            GoBase goBase = (GoBase) gson.fromJson(response,GoBase.class);
            return goBase;

        }catch (Exception ex) {
            return null;
        }
    }
    public LikeBase likeGobase(Context context,String customerId,String flag,String baseId)
    {
        this.act = context;
        LikeBase go_base_search_list;
        try{
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id,customerId));
            params.add(new BasicNameValuePair(CommonUtilities.key_flag,flag));
            params.add(new BasicNameValuePair(CommonUtilities.key_base_id,baseId));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_like_base, params,
                    ServerAccess.POST);


            if (response == null) {
                return null;
            }
            go_base_search_list = (LikeBase) gson.fromJson(response,LikeBase.class);
            return go_base_search_list;

        }catch (Exception ex) {
            return null;
        }
    }
    public CurrentTrip startTrip(Context context,String[] keys, String[] values,String method) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(keys[0], values[0]));
            params.add(new BasicNameValuePair(keys[1], values[1]));
            params.add(new BasicNameValuePair(keys[2], values[2]));
            params.add(new BasicNameValuePair(keys[3], values[3]));
            params.add(new BasicNameValuePair(keys[4], values[4]));
            params.add(new BasicNameValuePair(keys[5], values[5]));
            params.add(new BasicNameValuePair(keys[6], values[6]));
            params.add(new BasicNameValuePair(keys[7], values[7]));

            if(method.equals(CommonUtilities.key_end_trip))
            {
                params.add(new BasicNameValuePair(keys[8], values[8]));
            }
            String response = ServerAccess.PutRequest(method, params, ServerAccess.POST);


            if (response == null) {
                return null;
            }
            CurrentTrip model = (CurrentTrip) gson.fromJson(response, CurrentTrip.class);
            return model;
        } catch (Exception ex) {
            return null;
        }
    }
    public CheckCarPlaceResponse checkCarPlace(Context context,String[] keys, String[] values) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(keys[0], values[0]));
            params.add(new BasicNameValuePair(keys[1], values[1]));
            params.add(new BasicNameValuePair(keys[2], values[2]));
            params.add(new BasicNameValuePair(keys[3], values[3]));
            params.add(new BasicNameValuePair(keys[4], values[4]));

            String response = ServerAccess.PutRequest(CommonUtilities.key_check_car_place, params, ServerAccess.POST);


            if (response == null) {
                return null;
            }
            CheckCarPlaceResponse model = (CheckCarPlaceResponse) gson.fromJson(response, CheckCarPlaceResponse.class);
            return model;
        } catch (Exception ex) {
            return null;
        }
    }
    public JSONObject getLatLong(Context context,double[] LatLong,String mode) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("origin",LatLong[0]+","+LatLong[1]));
            params.add(new BasicNameValuePair("destination",LatLong[2]+","+LatLong[3]));
            params.add(new BasicNameValuePair("sensor","false"));
            params.add(new BasicNameValuePair("region","ie"));
            params.add(new BasicNameValuePair("mode",mode));
            params.add(new BasicNameValuePair("key",CommonUtilities.google_api_key));

            String response = ServerAccess.PutMapRequest(
                    "json", params,
                    ServerAccess.GET);

            if (response == null) {
                return null;
            }
            JSONObject obj = new JSONObject(response);

            return obj;


        } catch (Exception ex) {
            return null;
        }
    }
    public static String PutMapRequest(String Method, List<NameValuePair> params, int method) {
        String response = null;
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
            String url = CommonUtilities.getMapDirectionURL + Method;
            // Checking http request method type

            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
                httpResponse = httpClient.execute(httpGet);
            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }

    public LocateMyCarResponse getLocateMyCar(Context context,String[] values) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_booking_id, values[0]));
            params.add(new BasicNameValuePair(CommonUtilities.key_latitude, values[1]));
            params.add(new BasicNameValuePair(CommonUtilities.key_longitude, values[2]));
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, values[3]));
            String response = ServerAccess.PutRequest(CommonUtilities.locate_car, params,
                    ServerAccess.POST);

            if (response == null) {
                return null;
            }

            LocateMyCarResponse res = (LocateMyCarResponse) gson.fromJson(response,
                    LocateMyCarResponse.class);

            return res;

        } catch (Exception ex) {
            return null;
        }
    }
    public go_base_search_list getGobaseList(Context context,String customerId,String pageNo) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id,customerId));
            params.add(new BasicNameValuePair(CommonUtilities.key_page_no, pageNo));

            String response = ServerAccess.PutRequest(CommonUtilities.key_go_bases_list, params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }

            go_base_search_list goBase = (go_base_search_list) gson.fromJson(response,go_base_search_list.class);

            return goBase;

        } catch (Exception ex) {
            return null;
        }
    }
    public CurrentTrip GetTripTime(Context context,String booking_id,String customer_id) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonUtilities.key_booking_id,
                    booking_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id,
                    customer_id));

            String response = ServerAccess.PutRequest(CommonUtilities.key_get_trip_time, params,
                    ServerAccess.POST);


            if (response == null) {
                return null;
            }
            CurrentTrip res = (CurrentTrip) gson.fromJson(response,
                    CurrentTrip.class);
            return res;
        } catch (Exception ex) {
            return null;
        }
    }

    public FoundMyCar GetFoundMyCar(Context context, String booking_id, String customer_id) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonUtilities.key_booking_id,
                    booking_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id,
                    customer_id));
            String response = ServerAccess.PutRequest(CommonUtilities.key_found_my_car, params,
                    ServerAccess.POST);

            if (response == null) {
                return null;
            }
            FoundMyCar res = (FoundMyCar) gson.fromJson(response,
                    FoundMyCar.class);
            return res;
        } catch (Exception ex) {
            return null;
        }
    }

    public ResponseModel checkEmailExists(Context context,String email) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonUtilities.key_email,
                    email));

            String response = ServerAccess.PutRequest(CommonUtilities.key_check_email, params,
                    ServerAccess.POST);

            if (response == null) {
                return null;
            }
            ResponseModel res = (ResponseModel) gson.fromJson(response,
                    ResponseModel.class);
            return res;
        } catch (Exception ex) {
            return null;
        }
    }


    public GetTripTimeToStart GetTripTimeToStartResponse(Context context,String booking_id, String customer_id) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_booking_id, booking_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, customer_id));

            String response = ServerAccess.PutRequest(CommonUtilities.key_get_trip_time_to_start, params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }

            GetTripTimeToStart res = (GetTripTimeToStart) gson.fromJson(response,
                    GetTripTimeToStart.class);

            return res;

        } catch (Exception ex) {
            return null;
        }
    }

    public FinalTripDetailsBooking FinalEditBooking(Context context,String custid, String bookid, String bookstr, String bookend,String number
            ,String CarId) {
        try {
            this.act = context;
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id,
                    custid));
            params.add(new BasicNameValuePair(CommonUtilities.pref_booking_id,
                    bookid));
            params.add(new BasicNameValuePair(CommonUtilities.key_booking_start,
                    bookstr));
            params.add(new BasicNameValuePair(CommonUtilities.key_booking_stop,
                    bookend));
            params.add(new BasicNameValuePair(CommonUtilities.key_inverse_call_no,
                    number));
            params.add(new BasicNameValuePair(CommonUtilities.key_car_id,
                    CarId));

            String response = ServerAccess.PutRequest(CommonUtilities.key_pay_prepayment_on_edit, params,
                    ServerAccess.POST);

            if (response == null) {
                return null;
            }
            FinalTripDetailsBooking res = (FinalTripDetailsBooking) gson.fromJson(response,
                    FinalTripDetailsBooking.class);

            return res;

        } catch (Exception ex) {
            return null;
        }
    }

    public LoginRespose getAcceptedTermsandCondition(Context context,String id, String terms_agreed, String privacy_agreed) {
        this.act = context;
        try {
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, id));
            params.add(new BasicNameValuePair(CommonUtilities.key_terms_agreed, terms_agreed));
            params.add(new BasicNameValuePair(CommonUtilities.key_privacy_agreed, privacy_agreed));

            String response = ServerAccess.PutRequest(CommonUtilities.key_set_privacy_terms, params,
                    ServerAccess.POST);
            if (response == null) {
                return null;
            }

            LoginRespose res = (LoginRespose) gson.fromJson(response,
                    LoginRespose.class);

            return res;

        } catch (Exception ex) {
            return null;
        }
    }

    public ResponseModel switch_customer_acc(Activity act,String customer_id, String new_customer_id, String account_type) {
        this.act = act;
        try {
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(CommonUtilities.key_customer_id, new_customer_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_new_customer_id, customer_id));
            params.add(new BasicNameValuePair(CommonUtilities.key_account_type,account_type));
            params.add(new BasicNameValuePair("profile_switch_flag","1"));

            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_switch_customer_account, params,
                    ServerAccess.POST);

            if (response == null) {
                return null;
            }
            ResponseModel res = (ResponseModel) gson.fromJson(response,
                    ResponseModel.class);

            return res;

        } catch (Exception ex) {
            return null;
        }
    }

    public ResponseModel getCancelNewUpdate(Context context)
    {
        this.act = context;
        try{
            Gson gson = new Gson();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            String response = ServerAccess.PutRequest(
                    CommonUtilities.key_customer_cancel_app_version, params,
                    ServerAccess.POST);


            if (response == null) {
                return null;
            }
            ResponseModel data = (ResponseModel) gson.fromJson(response,ResponseModel.class);
            return data;

        }catch (Exception ex) {
            return null;
        }
    }

}



