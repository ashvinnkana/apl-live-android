package com.example.apl_live_scores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BatsmanAdpater extends RecyclerView.Adapter<BatsmanAdpater.BatViewHolder> {

    private ArrayList<BatsmanModel> batsmanList;
    public BatsmanAdpater(ArrayList<BatsmanModel> batsmanList) {
        this.batsmanList = batsmanList;
    }

    public class BatViewHolder extends RecyclerView.ViewHolder{
        private TextView name, runs, status;

        public BatViewHolder(final View view) {
            super(view);
            name = view.findViewById(R.id.batsmanName);
            runs = view.findViewById(R.id.batsmanRuns);
            status = view.findViewById(R.id.batsmanStatus);
        }
    }

    @NonNull
    @Override
    public BatsmanAdpater.BatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View batsmanView = LayoutInflater.from(parent.getContext()).inflate(R.layout.batsman_record, parent, false);
        return new BatViewHolder(batsmanView);
    }

    @Override
    public void onBindViewHolder(@NonNull BatsmanAdpater.BatViewHolder holder, int position) {
        String name = batsmanList.get(position).getName();
        String runs = batsmanList.get(position).getRuns();
        String status = batsmanList.get(position).getStatus();

        holder.name.setText(name);
        holder.runs.setText(runs);
        holder.status.setText(status);
        if (status.equals("OUT")) {
            holder.status.setTextColor(0xff773C3C);
            holder.runs.setTextColor(0xff773C3C);
            holder.name.setTextColor(0xff773C3C);
        }

    }

    @Override
    public int getItemCount() {
        return batsmanList.size();
    }
}
