package com.zx.freetime.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zx.freetime.R;

public abstract class BaseHeaderActivity extends AppCompatActivity {

    protected RelativeLayout headContainer;
    protected View headerView;


    protected RelativeLayout contentContainer;
    protected View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_header);

        headContainer = (RelativeLayout) findViewById(R.id.header_container);
        contentContainer = (RelativeLayout) findViewById(R.id.content_container);
        setHeaderLayout();
        setContentLayout();

        intiView();

    }

    protected abstract void intiView();

    protected abstract int getHeaderLayout();

    protected abstract int getContentLayout();

    protected void setHeaderLayout() {
        if (getHeaderLayout() > 0) {
            RelativeLayout.LayoutParams headerParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams
                    .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            headerView = LayoutInflater.from(this).inflate(getHeaderLayout(), null, false);
            headerView.setLayoutParams(headerParams);
            headContainer.addView(headerView);
        }
    }

    protected void setContentLayout() {
        if (getContentLayout() > 0) {
            RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams
                    .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            contentView = LayoutInflater.from(this).inflate(getContentLayout(), contentContainer, false);
            contentView.setLayoutParams(contentParams);
            contentContainer.addView(contentView);
        }
    }
}
