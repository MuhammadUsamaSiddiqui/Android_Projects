package com.example.muhammadusama.uber;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mDriver, mCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDriver = (Button) findViewById(R.id.driver);
        mCustomer = (Button) findViewById(R.id.customer);

        mDriver.setOnClickListener(this);
        mCustomer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.driver:

                Intent driverIntent = new Intent(MainActivity.this, DriverLoginActivity.class);
                startActivity(driverIntent);
                finish();
                break;

            case R.id.customer:

                Intent customerIntent = new Intent(MainActivity.this, CustomerLoginActivity.class);
                startActivity(customerIntent);
                finish();
                break;


        }
    }
}
