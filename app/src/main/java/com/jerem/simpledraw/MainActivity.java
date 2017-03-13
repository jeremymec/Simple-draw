package com.jerem.simpledraw;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {
    DrawingView dv;
    ViewGroup parent;
    Button dv_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parent = (ViewGroup) findViewById(R.id.activity_main);
    }

    public void crossClass(View v) {

        dv = new DrawingView(getApplicationContext());
        parent.addView(dv);

        View b = findViewById(R.id.refreshButton);
        b.setVisibility(View.VISIBLE);

        View b3 = findViewById(R.id.undoButton);
        b3.setVisibility(View.VISIBLE);

        View b2 = findViewById(R.id.startButton);
        b2.setVisibility(View.INVISIBLE);

    }


    public void refreshCallback(View v){
        dv.refresh();
    }

    public void undoCallback(View v) { dv.undo(); }


}
