package com.example.admin.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.admin.myapplication.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_custom_viewgroup1;
    private Button btn_flowLayout;
    private Button btn_verticalLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_custom_viewgroup1 = (Button) findViewById(R.id.btn_custom_viewgroup1);
        btn_flowLayout = (Button) findViewById(R.id.btn_flowLayout);
        btn_verticalLinearLayout = (Button) findViewById(R.id.btn_verticalLinearLayout);

        btn_custom_viewgroup1.setOnClickListener(this);
        btn_flowLayout.setOnClickListener(this);

        btn_verticalLinearLayout.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_define1:
                startActivity(new Intent(this, RandomTextActivity.class));
                break;
            case R.id.menu_define2:
                startActivity(new Intent(this, ImgTextActivity.class));
                break;
            case R.id.menu_define3:
                startActivity(new Intent(this, CircleProgressActivity.class));
                break;
            case R.id.menu_define4:
                startActivity(new Intent(this, MusicProgressActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_custom_viewgroup1:
                startActivity(new Intent(this, CustomViewgroup01Activity.class));
                break;
            case R.id.btn_flowLayout:
                startActivity(new Intent(this,FlowLayoutActivity.class));
                break;
            case R.id.btn_verticalLinearLayout:
                startActivity(new Intent(this,VerticalLinearLayoutActivity.class));
                break;
//            case R.id.btn_custom_viewgroup1:
//
//                break;
//            case R.id.btn_custom_viewgroup1:
//
//                break;

        }
    }
}
