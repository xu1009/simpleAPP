package com.example.xq.action;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
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
    private ArrayList<Boolean> isClicks;
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
        isClicks = new ArrayList<>();
        for(int i=0;i<ele.size();i++)
            isClicks.add(false);
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
                                                       int pos = holder.getLayoutPosition();   //view 会随着滑动变化，因此不能直接设置
                                                       isClicks.set(pos,true);
                                                       notifyDataSetChanged();
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
        if(isClicks.get(position))
            holder.element.setBackgroundColor(Color.parseColor("#90EE90"));
        else
            holder.element.setBackgroundColor(Color.parseColor("#FFFFFF"));
       // holder.element.setBackgroundColor(Color.parseColor("#4EEE94"));
    }

    @Override
    public int getItemCount() {
        return ele.size();
    }
}
