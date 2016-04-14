package com.example.lijinhua.ljicustomviewdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.lijinhua.ljicustomviewdemo.ui.FaceBookCustomViewGroupActivity;
import com.example.lijinhua.ljicustomviewdemo.ui.google.GoogleCustomGroupActivity;
import com.example.lijinhua.ljicustomviewdemo.ui.google.GoogleCustomViewActivity;
import com.example.lijinhua.ljicustomviewdemo.ui.mediumnet.CustomViewActivity;
import com.example.lijinhua.ljicustomviewdemo.ui.mediumnet.CustomViewGroupActivity;
import com.example.lijinhua.ljicustomviewdemo.ui.other.TagLayoutActivity;

public class MainActivity extends Activity implements View.OnClickListener {

    Button btnFaceBook, btnMedimViewGroup, btnMedimView,
            btnGoogleCustomView, btnGoogleCustomGroupView, btnTagLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnFaceBook = (Button) findViewById(R.id.btnFaceBook);
        btnMedimViewGroup = (Button) findViewById(R.id.btnMedimViewGroup);
        btnMedimView = (Button) findViewById(R.id.btnMedimView);
        btnFaceBook.setOnClickListener(this);

        btnGoogleCustomView = (Button) findViewById(R.id.btnGoogleCustomView);
        btnGoogleCustomView.setOnClickListener(this);
        btnTagLayout = (Button) findViewById(R.id.btnTagLayout);
        btnTagLayout.setOnClickListener(this);

        btnGoogleCustomGroupView = (Button) findViewById(R.id.btnGoogleCustomGroupView);
        btnGoogleCustomGroupView.setOnClickListener(this);
        btnMedimViewGroup.setOnClickListener(this);
        btnMedimView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFaceBook:
                startActivity(new Intent(this, FaceBookCustomViewGroupActivity.class));
                break;
            case R.id.btnMedimViewGroup:
                startActivity(new Intent(this, CustomViewGroupActivity.class));
                break;
            case R.id.btnMedimView:
                startActivity(new Intent(this, CustomViewActivity.class));
                break;
            case R.id.btnGoogleCustomView:
                startActivity(new Intent(this, GoogleCustomViewActivity.class));
                break;
            case R.id.btnGoogleCustomGroupView:
                startActivity(new Intent(this, GoogleCustomGroupActivity.class));
                break;
            case R.id.btnTagLayout:
                startActivity(new Intent(this, TagLayoutActivity.class));
                break;

        }
    }
}
