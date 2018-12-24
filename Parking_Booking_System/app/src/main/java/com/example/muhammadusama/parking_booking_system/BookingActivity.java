package com.example.muhammadusama.parking_booking_system;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookingActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mBookingsDatabaseReference;

    private View slotsView;
    private Calendar calendar;
    private SimpleDateFormat dateFormat, timeFormat;

    private TextView dateTextView,timeTextView,durationTextView;

    private int bookingYear, bookingMonth, bookingDay, bookingStartHour, bookingStartMinute, bookingEndHour;

    private String Area, mUserID, bookingDate, bookingStartTime, bookingEndTime, bookingDuration;

    private Button slot1, slot2, slot3, slot4, slot5, slot6, slot7, slot8, slot9, slot10, slot11, slot12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slots);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserID = mAuth.getUid();

        //Getting Area
        Bundle bundle = getIntent().getExtras();
        Area = bundle.getString("Area");

        //Date and Time Formats
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        timeFormat = new SimpleDateFormat("HH:mm");

        //Text View
        dateTextView = (TextView) findViewById(R.id.date_textView);
        timeTextView = (TextView) findViewById(R.id.time_textView);
        durationTextView = (TextView) findViewById(R.id.duration_textView);

        //Image View
        ImageView calenderImageView = (ImageView) findViewById(R.id.calender_image);
        ImageView clockImageView = (ImageView) findViewById(R.id.clock_image);
        ImageView durationImageView = (ImageView) findViewById(R.id.durationClock_image);

        calenderImageView.setOnClickListener(this);
        clockImageView.setOnClickListener(this);
        durationImageView.setOnClickListener(this);

        //Slots Linear Layout
        slotsView = findViewById(R.id.slots_view);

        //Buttons
        Button searchSlots = (Button) findViewById(R.id.searchSlots_button);
        Button clearButton = (Button) findViewById(R.id.clear_button);
        slot1 = (Button) findViewById(R.id.slot1_button);
        slot2 = (Button) findViewById(R.id.slot2_button);
        slot3 = (Button) findViewById(R.id.slot3_button);
        slot4 = (Button) findViewById(R.id.slot4_button);
        slot5 = (Button) findViewById(R.id.slot5_button);
        slot6 = (Button) findViewById(R.id.slot6_button);
        slot7 = (Button) findViewById(R.id.slot7_button);
        slot8 = (Button) findViewById(R.id.slot8_button);
        slot9 = (Button) findViewById(R.id.slot9_button);
        slot10 = (Button) findViewById(R.id.slot10_button);
        slot11 = (Button) findViewById(R.id.slot11_button);
        slot12 = (Button) findViewById(R.id.slot12_button);

        searchSlots.setOnClickListener(this);
        clearButton.setOnClickListener(this);
        slot1.setOnClickListener(this);
        slot2.setOnClickListener(this);
        slot3.setOnClickListener(this);
        slot4.setOnClickListener(this);
        slot5.setOnClickListener(this);
        slot6.setOnClickListener(this);
        slot7.setOnClickListener(this);
        slot8.setOnClickListener(this);
        slot9.setOnClickListener(this);
        slot10.setOnClickListener(this);
        slot11.setOnClickListener(this);
        slot12.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.calender_image:

                slotsView.setVisibility(View.GONE);
                displayDatePickerDialog();
                break;

            case R.id.clock_image:

                if (bookingDate == null) {

                    Toast.makeText(this, "Select Date First!", Toast.LENGTH_SHORT).show();
                } else {

                    slotsView.setVisibility(View.GONE);
                    displayTimePickerDialog();
                }
                break;

            case R.id.durationClock_image:

                if (bookingDate == null && bookingStartTime == null) {

                    Toast.makeText(this, "Select Date and Time First!", Toast.LENGTH_SHORT).show();
                } else if (bookingStartTime == null) {

                    Toast.makeText(this, "Select Time First!", Toast.LENGTH_SHORT).show();
                } else {

                    slotsView.setVisibility(View.GONE);
                    durationDialog();
                }
                break;

            case R.id.clear_button:

                dateTextView.setText(null);
                timeTextView.setText(null);
                durationTextView.setText(null);

                dateTextView.setHint("BOOKING DATE");
                timeTextView.setHint("BOOKING TIME");
                durationTextView.setHint("BOOKING DURATION");

                slotsView.setVisibility(View.GONE);

                bookingDate = null;
                bookingStartTime = null;
                bookingEndTime = null;
                bookingDuration = null;

                break;

            case R.id.searchSlots_button:

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {

                    try {

                        if (bookingDuration != null) {

                            calculateEndTime();
                        }

                        if (bookingDate == null || bookingStartTime == null || bookingEndTime == null || bookingDuration == null) {

                            Toast.makeText(this, "Kindly Select Date, Time and Duration!", Toast.LENGTH_SHORT).show();
                        } else {

                            Date date = dateFormat.parse(bookingDate);

                            if (calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()
                                    && !date.after(new Date())) {

                                Toast.makeText(this, "Invalid Time Selection", Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(BookingActivity.this, "End: " + bookingEndTime, Toast.LENGTH_SHORT).show();
                                checkSlotAvailability();
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{

                    Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                }
                    break;

                    case R.id.slot1_button:
                    case R.id.slot2_button:
                    case R.id.slot3_button:
                    case R.id.slot4_button:
                    case R.id.slot5_button:
                    case R.id.slot6_button:
                    case R.id.slot7_button:
                    case R.id.slot8_button:
                    case R.id.slot9_button:
                    case R.id.slot10_button:
                    case R.id.slot11_button:
                    case R.id.slot12_button:

                        Button slotButton = (Button) findViewById(v.getId());
                        String slot = slotButton.getText().toString();
                        bookSlot(slot);
                break;
        }
    }

    private void bookSlot(String slot) {

        String pushKey = mBookingsDatabaseReference.push().getKey();

        BookingDetails currentBookingDetails =
                new BookingDetails(bookingDate, bookingStartTime, bookingEndTime, mUserID, pushKey, Area, slot);


        mBookingsDatabaseReference.child(pushKey).setValue(currentBookingDetails);

        Toast.makeText(BookingActivity.this, "Booking Successful!", Toast.LENGTH_SHORT).show();

    }


    private void checkSlotAvailability() {

        mBookingsDatabaseReference = mFirebaseDatabase.getReference().child("Bookings");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {

                    resetButtonsColor();
                    slotsView.setVisibility(View.VISIBLE);
                    return;

                } else {

                    resetButtonsColor();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        BookingDetails bookingDetails = postSnapshot.getValue(BookingDetails.class);


                        if (bookingDetails.getArea().equals(Area)) {

                            if (bookingDetails.getDate().equals(bookingDate)) {

                                int comparingStartTimes = bookingStartTime.compareTo(bookingDetails.getStart());
                                int comparingEndTimes = bookingEndTime.compareTo(bookingDetails.getEnd());
                                int comparingStartToEnd = bookingStartTime.compareTo(bookingDetails.getEnd());
                                int comparingEndToStart = bookingEndTime.compareTo(bookingDetails.getStart());

                                //Slot Booked  3-6

                                //3-6
                                if (comparingStartTimes == 0 && comparingEndTimes == 0) {

                                    findSlot(bookingDetails.getUserID(), bookingDetails.getSlot());
                                }

                                //3-5
                                else if (comparingStartTimes == 0 && comparingEndTimes < 0) {

                                    findSlot(bookingDetails.getUserID(), bookingDetails.getSlot());
                                }

                                //3-7
                                else if (comparingStartTimes == 0 && comparingEndTimes > 0) {

                                    findSlot(bookingDetails.getUserID(), bookingDetails.getSlot());
                                }

                                //2-6
                                else if (comparingStartTimes < 0 && comparingEndTimes == 0) {

                                    findSlot(bookingDetails.getUserID(), bookingDetails.getSlot());
                                }

                                //4-6
                                else if (comparingStartTimes > 0 && comparingEndTimes == 0) {

                                    findSlot(bookingDetails.getUserID(), bookingDetails.getSlot());
                                }


                                //2-4
                                else if (comparingStartTimes < 0 && (comparingEndTimes  < 0 && comparingEndToStart > 0)) {

                                    findSlot(bookingDetails.getUserID(), bookingDetails.getSlot());
                                }

                                //5-7
                                else if ( (comparingStartTimes > 0 && comparingStartToEnd <0 ) && comparingEndTimes  > 0){

                                    findSlot(bookingDetails.getUserID(), bookingDetails.getSlot());
                                }

                                //4-5
                                else if ((comparingStartTimes > 0 && comparingEndTimes  < 0) && (comparingStartToEnd < 0 && comparingEndToStart > 0)) {

                                    findSlot(bookingDetails.getUserID(), bookingDetails.getSlot());
                                }

                                //4-7
                                else if (comparingStartTimes < 0 && comparingEndTimes  > 0) {

                                    findSlot(bookingDetails.getUserID(), bookingDetails.getSlot());
                                }else{
                                    // Do Nothing
                                }

                            }
                        } else {

                            // Do Nothing
                        }
                    }
                    slotsView.setVisibility(View.VISIBLE);
                }
            }

            private void findSlot(String uid, String slot) {

                switch (slot) {

                    case "Slot 1":
                        disableSlot(uid, slot1);
                        break;

                    case "Slot 2":
                        disableSlot(uid, slot2);
                        break;

                    case "Slot 3":
                        disableSlot(uid, slot3);
                        break;

                    case "Slot 4":
                        disableSlot(uid, slot4);
                        break;

                    case "Slot 5":
                        disableSlot(uid, slot5);
                        break;

                    case "Slot 6":
                        disableSlot(uid, slot6);
                        break;

                    case "Slot 7":
                        disableSlot(uid, slot7);
                        break;

                    case "Slot 8":
                        disableSlot(uid, slot8);
                        break;

                    case "Slot 9":
                        disableSlot(uid, slot9);
                        break;

                    case "Slot 10":
                        disableSlot(uid, slot10);
                        break;

                    case "Slot 11":
                        disableSlot(uid, slot11);
                        break;

                    case "Slot 12":
                        disableSlot(uid, slot12);
                        break;
                }
            }

            private void disableSlot(String uid, Button slotButton) {

                if (mUserID.equals(uid)) {

                    int greenColor = ContextCompat.getColor(BookingActivity.this, R.color.colorDarkGreen);
                    slotButton.setBackgroundColor(greenColor);

                } else {
                    slotButton.setBackgroundColor(Color.RED);
                }
                slotButton.setTextColor(Color.WHITE);
                slotButton.setEnabled(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mBookingsDatabaseReference.addValueEventListener(eventListener);

    }

    private void resetButtonsColor() {

        // Reset to default Button
        slot1.setBackgroundResource(android.R.drawable.btn_default);
        slot2.setBackgroundResource(android.R.drawable.btn_default);
        slot3.setBackgroundResource(android.R.drawable.btn_default);
        slot4.setBackgroundResource(android.R.drawable.btn_default);
        slot5.setBackgroundResource(android.R.drawable.btn_default);
        slot6.setBackgroundResource(android.R.drawable.btn_default);
        slot7.setBackgroundResource(android.R.drawable.btn_default);
        slot8.setBackgroundResource(android.R.drawable.btn_default);
        slot9.setBackgroundResource(android.R.drawable.btn_default);
        slot10.setBackgroundResource(android.R.drawable.btn_default);
        slot11.setBackgroundResource(android.R.drawable.btn_default);
        slot12.setBackgroundResource(android.R.drawable.btn_default);

        // Reset Button Text Color
        slot1.setTextColor(Color.BLACK);
        slot2.setTextColor(Color.BLACK);
        slot3.setTextColor(Color.BLACK);
        slot4.setTextColor(Color.BLACK);
        slot5.setTextColor(Color.BLACK);
        slot6.setTextColor(Color.BLACK);
        slot7.setTextColor(Color.BLACK);
        slot8.setTextColor(Color.BLACK);
        slot9.setTextColor(Color.BLACK);
        slot10.setTextColor(Color.BLACK);
        slot11.setTextColor(Color.BLACK);
        slot12.setTextColor(Color.BLACK);

        //Enable all Buttons
        slot1.setEnabled(true);
        slot2.setEnabled(true);
        slot3.setEnabled(true);
        slot4.setEnabled(true);
        slot5.setEnabled(true);
        slot6.setEnabled(true);
        slot7.setEnabled(true);
        slot8.setEnabled(true);
        slot9.setEnabled(true);
        slot10.setEnabled(true);
        slot11.setEnabled(true);
        slot12.setEnabled(true);


    }

    private void calculateEndTime() {

        try {

            bookingEndHour = bookingStartHour + Integer.parseInt(bookingDuration);
            bookingEndTime = bookingEndHour + ":" + bookingStartMinute;

            Date endTime = timeFormat.parse(bookingEndTime);

            bookingEndTime = timeFormat.format(endTime);

            String maxTime = "24:00";

            if (maxTime.compareTo(bookingEndTime) < 0) {

                bookingEndHour = (bookingStartHour + Integer.parseInt(bookingDuration)) - 24;
                bookingEndTime = bookingEndHour + ":" + bookingStartMinute;

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void displayDatePickerDialog() {

        int day, month, year;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);
        datePickerDialog.setTitle("Booking Date");
        // Disable Past Dates
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        bookingYear = year;
        bookingMonth = month + 1;
        bookingDay = dayOfMonth;

        bookingDate = bookingDay + "/" + bookingMonth + "/" + bookingYear;

        dateTextView.setText(bookingDate);

        Toast.makeText(this, "Date: " + bookingDate, Toast.LENGTH_SHORT).show();


    }

    private void displayTimePickerDialog() {

        int hour, minute;

        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.setTitle("Booking Time");
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        try {

            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);


            Date date = dateFormat.parse(bookingDate);

            if (calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis() && !date.after(new Date())) {

                Toast.makeText(this, "Invalid Time Selection", Toast.LENGTH_SHORT).show();

                TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), DateFormat.is24HourFormat(this));
                timePickerDialog.setTitle("Booking Time");
                timePickerDialog.show();
            } else {

                bookingStartHour = hourOfDay;
                bookingStartMinute = minute;

                bookingStartTime = bookingStartHour + ":" + bookingStartMinute;

                Date startTime = timeFormat.parse(bookingStartTime);

                bookingStartTime = timeFormat.format(startTime);

                timeTextView.setText(bookingStartTime);

                Toast.makeText(this, "start " + bookingStartTime, Toast.LENGTH_SHORT).show();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void durationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogueView = inflater.inflate(R.layout.duration_dialog, null);

        builder.setView(dialogueView);

        builder.setTitle(R.string.duration);

        final AlertDialog durationDialogue = builder.create();
        durationDialogue.show();

        Button okButton = (Button) dialogueView.findViewById(R.id.ok_button);
        Spinner durationSpinner = (Spinner) dialogueView.findViewById(R.id.duration_spinner);

        // Spinner Drop down elements
        final List<String> duration = new ArrayList<String>();
        duration.add("1");
        duration.add("2");
        duration.add("3");
        duration.add("4");
        duration.add("5");

        // Creating adapter for spinner
        ArrayAdapter<String> durationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, duration);

        // Drop down layout style - list view with radio button
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        durationSpinner.setAdapter(durationAdapter);

        durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                bookingDuration = parent.getItemAtPosition(position).toString();

                durationTextView.setText(bookingDuration + " Hour(s)");
                Toast.makeText(BookingActivity.this, "Booking Duration : " + bookingDuration + " Hour(s)", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(BookingActivity.this, "Select the Duration!", Toast.LENGTH_SHORT).show();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(BookingActivity.this, "Booking Duration : " + bookingDuration + " Hour(s)", Toast.LENGTH_SHORT).show();
                durationDialogue.dismiss();

            }
        });

    }
}