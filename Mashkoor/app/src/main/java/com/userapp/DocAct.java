package com.userapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.userapp.com.userapp.utility.Singleton;

import java.util.List;

public class DocAct extends Activity {


    private RecyclerView rvVideos;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_layout);
        rvVideos = (RecyclerView) findViewById(R.id.recyclerViewId);
        setupRecyclerView();
    }

    private void setupRecyclerView() {


        mProgress = new ProgressDialog(DocAct.this,
                R.style.CutomAlert);
        mProgress.setTitle("Requesting OTP");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.show();


        rvVideos.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(DocAct.this);
        rvVideos.setLayoutManager(mLayoutManager);
        int[] ATTRS = new int[]{android.R.attr.listDivider};
        TypedArray a = obtainStyledAttributes(ATTRS);
        Drawable divider = a.getDrawable(0);
        InsetDrawable insetDivider = new InsetDrawable(divider, getResources().getDimensionPixelSize(R.dimen._55sdp)
                , 0, getResources().getDimensionPixelSize(R.dimen.margin_value), 0);
        a.recycle();
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(rvVideos.getContext(),
                DividerItemDecoration.VERTICAL);
        horizontalDecoration.setDrawable(insetDivider);
        rvVideos.addItemDecoration(horizontalDecoration);

        Singleton.getOurInstance().getUsers(DocAct.this, new Singleton.SL() {
            @Override
            public void onSuccess(List<UserFirbase> us) {
                mProgress.dismiss();
                if (us != null && us.size() > 0) {
                    UserAdapter mAdapter = new UserAdapter(DocAct.this, us);
                    rvVideos.setAdapter(mAdapter);
                } else {
                    Toast.makeText(DocAct.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(String message) {
                mProgress.dismiss();
            }
        });


        //~~
    }
}
