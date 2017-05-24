package com.sudhansu.videorecord;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;


public class Adapter extends android.support.v7.widget.RecyclerView.Adapter<Adapter.MyViewHolder> {

    private Activity mContext;
    JSONArray jsonArray;

    public Adapter(Activity mContext, JSONArray jsonArray) {
        this.mContext = mContext;
        this.jsonArray = jsonArray;
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


        try {
            holder.txt_detail.setText(jsonArray.getJSONObject(position).getString("storename"));
            holder.txt_name.setText(jsonArray.getJSONObject(position).getString("storeid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return this.jsonArray.length();
    }

    public class MyViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        TextView txt_name, txt_detail;


        public MyViewHolder(View view) {
            super(view);
            txt_name = (TextView) view.findViewById(R.id.text1);
            txt_detail = (TextView) view.findViewById(R.id.text2);
        }
    }
}
