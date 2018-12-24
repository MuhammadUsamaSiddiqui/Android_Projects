package com.example.muhammadusama.parking_booking_system;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button viewButton = (Button) findViewById(R.id.view);
        viewButton.setVisibility(View.VISIBLE);

        Button bookParkingButton = (Button) findViewById(R.id.book_parking_space);
        Button cancelBookingButton = (Button)findViewById(R.id.cancel_booking);
        Button feedbackButton = (Button) findViewById(R.id.feedback);

        bookParkingButton.setOnClickListener(this);
        cancelBookingButton.setOnClickListener(this);
        viewButton.setOnClickListener(this);
        feedbackButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.book_parking_space:

                Intent areaIntent = new Intent(AdminActivity.this, AreasActivity.class);
                startActivity(areaIntent);

                break;

            case R.id.cancel_booking:

                Intent cancelBookingIntent = new Intent(AdminActivity.this, CancelBookingActivity.class);
                startActivity(cancelBookingIntent);

                break;

            case R.id.view:

                Intent viewIntent = new Intent(AdminActivity.this, AdminViewActivity.class);
                startActivity(viewIntent);

                break;

            case R.id.feedback:

                Intent feedbackIntent = new Intent(AdminActivity.this,FeedbacksListActivity.class);
                startActivity(feedbackIntent);

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuLogout:

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(AdminActivity.this,MainActivity.class));
                break;
        }
        return true;
    }

}
