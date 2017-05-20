package com.sudhansu.testrestapi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


    public class volley {

        public static void getResponse(final Activity context, final String method,final JSONObject params, boolean progress, final VolleyCallback callback) {

            final Dialog dialog;
            dialog = new Dialog(context);
            if (progress) {
                if (!dialog.isShowing())
                    dialog.setCancelable(false);
                dialog.show();
            }

            String url = "http://iradar.demowork.com/native/web_service.php?action=getalloffers&lat=23.038111&lon=72.512129";
            JsonObjectRequest stringRequest = new JsonObjectRequest(url + method, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("response :: " , response.toString());

                            if (dialog.isShowing())
                                dialog.dismiss();
                            callback.onSuccess(response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error: " ,error.getMessage());
                    dialog.dismiss();
                    callback.onError(error.getMessage());
                }
            }) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> header = new HashMap<>();
//                    header.put("Content-Type", "application/json; charset=utf-8");
//                    header.put("Accept", "application/json");
//                    return header;
//                }

//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("name", "Androidhive");
//                    params.put("email", "abc@androidhive.info");
//                    params.put("password", "password123");
//
//                    return params;
//                }
            };

            call_webService(context, stringRequest, dialog);

        }


        public static void call_webService(final Activity context, final JsonObjectRequest stringRequest, final Dialog dialog) {
            int socketTimeout = 20000;//20 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                stringRequest.setRetryPolicy(policy);
                requestQueue.add(stringRequest);

        }

        public interface VolleyCallback {
            void onSuccess(String result);

            void onError(String error);

        }

    }

