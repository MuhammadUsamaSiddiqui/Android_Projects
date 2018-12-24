package com.example.muhammadusama.parking_booking_system;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AreasActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_areas);

        Button area1Button = (Button) findViewById(R.id.area1);
        Button area2Button = (Button) findViewById(R.id.area2);
        Button area3Button = (Button) findViewById(R.id.area3);

        area1Button.setOnClickListener(this);
        area2Button.setOnClickListener(this);
        area3Button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.area1:

                Intent area1Intent = new Intent(AreasActivity.this, BookingActivity.class);

                Bundle area1Bundle = new Bundle();
                area1Bundle.putString("Area","Area 1");
                area1Intent.putExtras(area1Bundle);
                startActivity(area1Intent);
                break;

            case R.id.area2:

                Intent area2Intent = new Intent(AreasActivity.this, BookingActivity.class);

                Bundle area2Bundle = new Bundle();
                area2Bundle.putString("Area","Area 2");
                area2Intent.putExtras(area2Bundle);
                startActivity(area2Intent);
                break;

            case R.id.area3:

                Intent area3Intent = new Intent(AreasActivity.this, BookingActivity.class);

                Bundle area3Bundle = new Bundle();
                area3Bundle.putString("Area","Area 3");
                area3Intent.putExtras(area3Bundle);
                startActivity(area3Intent);
                break;
        }
    }
}
