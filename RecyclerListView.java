package com.sudhansu.testrestapi;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sudhansu on 5/20/2017.
 */

public class RecyclerListView extends AppCompatActivity {

    ProgressDialog pDialog;
    boolean isLoading = false;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    RecyclerAdapter mAdapter;
    ArrayList<Model.response.offers> array_List;
    JSONObject res;
    Model respons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pDialog = new ProgressDialog(this);

        array_List = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        call_api(true);

    }

    public void yo(View v){
        call_api(true);
    }

    private void call_api(boolean isDialogShow) {
        try {
            JSONObject params = new JSONObject();

            volley.getResponse(RecyclerListView.this, "", params, isDialogShow, new volley.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    respons = new Model().response(result);

                    mAdapter = new RecyclerAdapter(RecyclerListView.this,respons);
                    mAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(mAdapter);
                }

                @Override
                public void onError(String error) {

                }
            });

        } catch (Exception e) {
        }

    }
}
