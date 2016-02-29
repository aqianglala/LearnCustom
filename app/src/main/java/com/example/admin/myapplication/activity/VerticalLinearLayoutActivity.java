package com.example.admin.myapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.widget.VerticalLinearLayout;

public class VerticalLinearLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_linear_layout);

        VerticalLinearLayout vl= (VerticalLinearLayout) findViewById(R.id.id_main_ly);
        vl.setOnPageChangeListener(new VerticalLinearLayout.OnPageChangeListener() {
            @Override
            public void onPageChange(int currentPage) {
                Toast.makeText(VerticalLinearLayoutActivity.this,"第"+currentPage+"页",Toast
                        .LENGTH_LONG).show();
            }
        });
    }
}
