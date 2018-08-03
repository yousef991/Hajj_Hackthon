package com.userapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final Context context;
    private List<UserFirbase> mArrayList;


    public UserAdapter(Context context, List<UserFirbase> mArrayList) {
        this.mArrayList = mArrayList;
        this.context = context;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.us_list_item_layout, parent, false));
    }


    @Override
    public int getItemCount() {
        if (mArrayList != null)
            return mArrayList.size();
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(context, mArrayList.get(position));
    }


    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView coNaTxt, dateTxt;
        private final LinearLayout liLa;

        public ViewHolder(View itemView) {
            super(itemView);
            liLa = (LinearLayout) itemView.findViewById(R.id.linearlayout);
            coNaTxt = (TextView) itemView.findViewById(R.id.pName);
            dateTxt = (TextView) itemView.findViewById(R.id.pdate);
        }

        public void setData(final Context context, final UserFirbase s) {

            coNaTxt.setText(s.getNameIDString());
            dateTxt.setText(s.getCurrentDateTime());


            liLa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /**
                     *  here true means itself user
                     */
                    Intent intent = new Intent(context, UDeatils.class);
                    Gson gson = new Gson();
                    String d = gson.toJson(s, UserFirbase.class);
                    intent.putExtra("obj", d);
                    ((DocAct) context).startActivity(intent);


                }//~~
            });


        }
    }
}
