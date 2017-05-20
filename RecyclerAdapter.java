package com.sudhansu.testrestapi;

import android.app.Activity;
import android.support.v7.widget.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter extends android.support.v7.widget.RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Activity mContext;
    Model response;

    public RecyclerAdapter(Activity mContext, Model response) {
        this.mContext = mContext;
        this.response = response;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txt_name.setText(response.response.offers.get(position).storename);
        holder.txt_detail.setText(response.response.offers.get(position).title);
        String imgurl = "http://iradar.demowork.com/images/";

        Picasso.with(mContext).
                load(imgurl+response.response.offers.get(position).imgurl).
                into(holder.imgview, new Callback() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.response.response.offers.size();
    }

    public class MyViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        TextView txt_name, txt_detail;
        ImageView imgview;


        public MyViewHolder(View view) {
            super(view);
            txt_name = (TextView) view.findViewById(R.id.text1);
            txt_detail = (TextView) view.findViewById(R.id.text);
            imgview = (ImageView) view.findViewById(R.id.imgview);
        }
    }
}