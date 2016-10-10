package com.rya.toggledemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.rya.toggledemo.view.ToggleView;

public class ToggleDemo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toggle_demo);

        ToggleView tgv_toggle = (ToggleView) findViewById(R.id.tgv_toggle);

        if (tgv_toggle != null) {
            tgv_toggle.setOnStateChangeListener(new ToggleView.OnStateChangeListener() {
                @Override
                public void onStateChange(boolean state) {
                    Toast.makeText(getApplicationContext(), "State >>> " + state, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
