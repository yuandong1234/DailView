package com.yuong.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DialView dialView;
    private Button button, button2, button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        dialView = findViewById(R.id.dial_view);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                dialView.setState(getString(R.string.air_quality_excellent),90,DialView.COLOR_EXCELLENT);
                break;
            case R.id.button2:
                dialView.setState(getString(R.string.air_quality_medium),60,DialView.COLOR_MEDIUM);
                break;
            case R.id.button3:
                dialView.setState(getString(R.string.air_quality_bad),30,DialView.COLOR_BAD);
                break;
        }
    }
}
