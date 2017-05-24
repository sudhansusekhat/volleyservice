package com.sudhansu.videorecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sudhansu on 5/24/2017.
 */

public class service extends AppCompatActivity{
    boolean isLoading = false;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    Adapter mAdapter;
    JSONObject res;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String url = "https://api.myjson.com/bins/lodux";
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());

                        Model model =  new Model().response(response.toString());

                        Log.e("test",model.response.offers.get(0).storeid);
                        JSONArray jsonarray = new JSONArray();
                        try {
                            jsonarray = response.getJSONObject("response").getJSONArray("offers");
                            Log.e("gr8",jsonarray.toString());
                            mAdapter = new Adapter(service.this,jsonarray);
                            mAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = null;
                            try {
                                jsonobject = jsonarray.getJSONObject(i);
                                String name = jsonobject.getString("storeid");
                                String url = jsonobject.getString("storename");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());

            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
}
