package com.example.xq.action;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by xq on 2017/4/27.
 */

public class recycleview extends RecyclerView.Adapter<recycleview.ViewHolder>{
    private ArrayList<String> ele;
    private Uri imageUri;
    public interface OnRecycleViewListener
    {
        void onItemClick(View view,int position);
    }
    private OnRecycleViewListener onRecycleViewListener;

    public void setOnRecycleViewListener(OnRecycleViewListener monRecycleViewListener)
    {
     this.onRecycleViewListener = monRecycleViewListener;
    }
    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView element;
        View dataview;
        public ViewHolder(View view)
        {
            super(view);
            dataview = view;
            element = (TextView) view.findViewById(R.id.ele);
        }

    }
    public recycleview(ArrayList<String> data)
    {
        this.ele = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        if(onRecycleViewListener!=null)
        {
            holder.dataview.setOnClickListener(new View.OnClickListener()
                                               {
                                                   @Override
                                                   public void onClick(View v) {
                                                       int pos = holder.getLayoutPosition();
                                                       onRecycleViewListener.onItemClick(holder.itemView,pos);
                                                   }
                                               }

            );


        }

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String dta = ele.get(position);
        holder.element.setText(dta);
    }

    @Override
    public int getItemCount() {
        return ele.size();
    }
}
