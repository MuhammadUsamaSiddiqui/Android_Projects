package com.example.muhammadusama.parking_booking_system;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CancelBookingActivity extends AppCompatActivity {

    private ListView mBookingsListView;
    private ArrayAdapter<String> mBookingsAdapter;
    private List<BookingDetails> mBookings;
    private TextView mEmptyTextView;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mBookingsDatabaseReference;

    private String mUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        mBookingsListView = (ListView)findViewById(R.id.list);
        mEmptyTextView = (TextView) findViewById(R.id.empty_view);
        mBookingsListView.setEmptyView(mEmptyTextView);

        mAuth = FirebaseAuth.getInstance();
        mUserID = mAuth.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mBookingsAdapter!=null){

            mBookingsAdapter.clear();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mAuth.getCurrentUser() != null) {

            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {

                mBookings = new ArrayList<BookingDetails>();
                attachDatabaseReadListener();
            }else{
                Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void attachDatabaseReadListener() {

        mBookingsDatabaseReference = mFirebaseDatabase.getReference().child("Bookings");

        mBookingsDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final List<String> locations = new ArrayList<String>();
                mBookingsAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.list_item,R.id.list_item_textView,locations);
                mBookingsListView.setAdapter(mBookingsAdapter);

                if (!dataSnapshot.exists()) {

                    mEmptyTextView.setText("No Bookings Found!");
                    return;
                } else {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                        BookingDetails currentBookingDetails = postSnapshot.getValue(BookingDetails.class);

                        if (currentBookingDetails.getUserID().equals(mUserID)) {

                            mBookings.add(currentBookingDetails);
                            String location = currentBookingDetails.getArea()+" : "+currentBookingDetails.getSlot();
                            mBookingsAdapter.add(location);
                        }
                    }

                    if (mBookings.size() == 0){

                        mEmptyTextView.setText("No Bookings Found!");

                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mBookingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                displayBookingInformation(i);


            }
        });

        mBookingsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                cancelBooking(position);
                return true;
            }
        });

    }

    private void cancelBooking(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogueView = inflater.inflate(R.layout.cancel_dialog,null);
        builder.setView(dialogueView);
        builder.setTitle("Cancel Booking");
        final AlertDialog deleteDialog = builder.create();
        deleteDialog.show();

       Button yesButton = dialogueView.findViewById(R.id.yes_button);
       Button noButton = dialogueView.findViewById(R.id.no_button);

       yesButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               BookingDetails currentBookingDetails = mBookings.get(position);
               String pushKey = currentBookingDetails.getPushKey();
                mBookingsDatabaseReference =mBookingsDatabaseReference.child(pushKey);
                mBookingsDatabaseReference.removeValue();

                mBookingsAdapter.clear();
                mBookingsListView.setAdapter(null);
                deleteDialog.dismiss();
           }
       });

    noButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            deleteDialog.dismiss();
        }
    });
    }

    private void displayBookingInformation(int position) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogueView = inflater.inflate(R.layout.bookings_information_dialog,null);

        builder.setView(dialogueView);

        builder.setTitle(R.string.bookingInformation);

        final AlertDialog bookingDialog = builder.create();
        builder.show();

        TextView areaTextView = (TextView) dialogueView.findViewById(R.id.area_textView);
        TextView slotTextView = (TextView) dialogueView.findViewById(R.id.slot_textView);
        TextView dateTextView = (TextView) dialogueView.findViewById(R.id.date_textView);
        TextView startTimeTextView = (TextView) dialogueView.findViewById(R.id.startTime_textView);
        TextView endTimeTextView = (TextView) dialogueView.findViewById(R.id.endTime_textView);

        BookingDetails selectedBooking = mBookings.get(position);

        areaTextView.setText(selectedBooking.getArea());
        slotTextView.setText(selectedBooking.getSlot());
        dateTextView.setText(selectedBooking.getDate());
        startTimeTextView.setText(selectedBooking.getStart());
        endTimeTextView.setText(selectedBooking.getEnd());


    }
}
