package com.example.muhammadusama.parking_booking_system;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminViewActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);

        Button viewUsersButton = (Button) findViewById(R.id.view_users);
        Button viewBookingsButton = (Button) findViewById(R.id.view_bookings);

        viewUsersButton.setOnClickListener(this);
        viewBookingsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.view_users:

                Intent viewUsersIntent = new Intent(AdminViewActivity.this, ViewUsersActivity.class);
                startActivity(viewUsersIntent);

                break;

            case R.id.view_bookings:

                Intent viewBookingsIntent = new Intent(AdminViewActivity.this,ViewBookingsActivity.class);
                startActivity(viewBookingsIntent);

                break;
        }
    }
}
