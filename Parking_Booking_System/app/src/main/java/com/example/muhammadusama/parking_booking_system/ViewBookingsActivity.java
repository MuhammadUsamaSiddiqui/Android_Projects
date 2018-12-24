package com.example.muhammadusama.parking_booking_system;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ViewBookingsActivity extends AppCompatActivity {

    private ListView mBookingsListView;
    private ArrayAdapter<String> mBookingsAdapter;
    private TextView mEmptyTextView;

    private List<BookingDetails> mBookings;
    private List<User> mUsers;
    private List<String> mUsersID;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mBookingsDatabaseReference,mUserDatabaseReference;

    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        mBookingsListView = (ListView)findViewById(R.id.list);
        mEmptyTextView = (TextView) findViewById(R.id.empty_view);
        mBookingsListView.setEmptyView(mEmptyTextView);

        mAuth = FirebaseAuth.getInstance();
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
                mUsers = new ArrayList<User>();
                mUsersID = new ArrayList<String>();

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

                            mBookings.add(currentBookingDetails);
                            String location = currentBookingDetails.getArea()+" : "+currentBookingDetails.getSlot();
                            mBookingsAdapter.add(location);
                        }

                    getUserInfromation();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mBookingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                position =i;
                displayBookingInformation();

            }
        });

    }

    private void getUserInfromation() {

        mUserDatabaseReference = mFirebaseDatabase.getReference().child("Users");

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                 User currentUser = dataSnapshot.getValue(User.class);
                 mUsers.add(currentUser);
                 mUsersID.add(dataSnapshot.getKey());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mUserDatabaseReference.addChildEventListener(childEventListener);


    }

    private void displayBookingInformation() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogueView = inflater.inflate(R.layout.bookings_information_dialog, null);

        builder.setView(dialogueView);

        builder.setTitle(R.string.bookingInformation);

        final AlertDialog bookingDialog = builder.create();
        builder.show();

        TextView nameTextView = (TextView) dialogueView.findViewById(R.id.name_textView);
        TextView emailTextView = (TextView) dialogueView.findViewById(R.id.email_textView);
        TextView phoneTextView = (TextView) dialogueView.findViewById(R.id.phone_textView);
        TextView areaTextView = (TextView) dialogueView.findViewById(R.id.area_textView);
        TextView slotTextView = (TextView) dialogueView.findViewById(R.id.slot_textView);
        TextView dateTextView = (TextView) dialogueView.findViewById(R.id.date_textView);
        TextView startTimeTextView = (TextView) dialogueView.findViewById(R.id.startTime_textView);
        TextView endTimeTextView = (TextView) dialogueView.findViewById(R.id.endTime_textView);
        View user_information = dialogueView.findViewById(R.id.user_information);
        user_information.setVisibility(View.VISIBLE);

        BookingDetails selectedBooking = mBookings.get(position);

        for (int i = 0; i < mUsersID.size(); i++) {

            if (selectedBooking.getUserID().equals(mUsersID.get(i))) {

                User user = mUsers.get(i);

                nameTextView.setText(user.getName());
                emailTextView.setText(user.getEmail());
                phoneTextView.setText(user.getPhone());
                areaTextView.setText(selectedBooking.getArea());
                slotTextView.setText(selectedBooking.getSlot());
                dateTextView.setText(selectedBooking.getDate());
                startTimeTextView.setText(selectedBooking.getStart());
                endTimeTextView.setText(selectedBooking.getEnd());

                break;
            }

        }


    }
}
