package com.example.apl_live_scores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BowlerAdpater extends RecyclerView.Adapter<BowlerAdpater.BallViewHolder> {

    private ArrayList<BowlerModel> bowlerList;
    public BowlerAdpater(ArrayList<BowlerModel> bowlerList) {
        this.bowlerList = bowlerList;
    }

    public class BallViewHolder extends RecyclerView.ViewHolder{
        private TextView name, runsWicket;

        public BallViewHolder(final View view) {
            super(view);
            name = view.findViewById(R.id.bowlerName);
            runsWicket = view.findViewById(R.id.bowlerRunsWicket);
        }
    }

    @NonNull
    @Override
    public BowlerAdpater.BallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View bowlerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bowler_record, parent, false);
        return new BallViewHolder(bowlerView);
    }

    @Override
    public void onBindViewHolder(@NonNull BowlerAdpater.BallViewHolder holder, int position) {
        String name = bowlerList.get(position).getName();
        String runsWicket = bowlerList.get(position).getRuns_wicket();

        holder.name.setText(name);
        holder.runsWicket.setText(runsWicket);

        if (!(name.charAt(name.length()-1) == '*')) {
            holder.name.setTextColor(0xff636363);
            holder.runsWicket.setTextColor(0xff636363);
        }

    }

    @Override
    public int getItemCount() {
        return bowlerList.size();
    }
}
