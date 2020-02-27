package com.jisu.tenantmanager;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private ArrayList<Dictionary> mArrayList;

    public RecyclerAdapter(ArrayList<Dictionary> arrayList) {
        this.mArrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);

        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Resources resources = holder.itemView.getContext().getResources();
        holder.numberTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        holder.nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
        holder.numberTextView.setTextColor(resources.getColor(R.color.Black));
        holder.nameTextView.setTextColor(resources.getColor(R.color.Black));
        holder.numberTextView.setGravity(Gravity.CENTER);
        holder.nameTextView.setGravity(Gravity.CENTER);

        holder.numberTextView.setText(String.valueOf(position+1));
        holder.nameTextView.setText(mArrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return (null !=mArrayList? mArrayList.size(): 0);
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        protected TextView numberTextView;
        protected TextView nameTextView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            this.nameTextView = itemView.findViewById(R.id.username_textview);
            this.numberTextView = itemView.findViewById(R.id.usernumber_textview);
        }
    }
}
