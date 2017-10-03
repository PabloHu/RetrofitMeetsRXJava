package com.example.admin.retrofitmeetsrxjava;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.retrofitmeetsrxjava.data.modelhomeworklocation.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 9/13/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{


    private static final String TAG = "RecyclerViewAdapter";
    List<Place> placeList = new ArrayList<>();

    public RecyclerViewAdapter(List<Place> personList) {
        this.placeList = personList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvPersonName ;//=// //view.findViewById(R.id.tvPersonName);
        private TextView tvPersonAge; //= //vi/ew.findViewById(R.id.tvPersonAge);




        public ViewHolder(View itemView) {
            super(itemView);
            tvPersonName = itemView.findViewById(R.id.tvPersonName);
            tvPersonAge = itemView.findViewById(R.id.tvPersonAge);



        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.rv_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder position: "+position);
        Place place = placeList.get(position);

        holder.tvPersonName.setText(String.valueOf(place.getLatitude()));

        holder.tvPersonAge.setText(String.valueOf(place.getLongitude()));


    }

    @Override
    public int getItemCount() {
        //    Log.d(TAG, "getItemCount: "+ personList.size());
        return placeList.size();
    }


}
