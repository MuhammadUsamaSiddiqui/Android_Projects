package com.example.muhammadusama.parking_booking_system;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;

public class ViewUsersActivity extends AppCompatActivity {

    private ListView mUsersListView;
    private ArrayAdapter<String> mUsersAdapter;
    private List<User> users;
    private TextView mEmptyTextView;


    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        mUsersListView = (ListView) findViewById(R.id.list);
        mEmptyTextView = (TextView) findViewById(R.id.empty_view);
        mUsersListView.setEmptyView(mEmptyTextView);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mUsersAdapter!=null){

            mUsersAdapter.clear();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mAuth.getCurrentUser() != null) {

            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {

                users = new ArrayList<User>();
                attachDatabaseReadListener();
            }else{

                Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void attachDatabaseReadListener() {

        final List<String> userNames = new ArrayList<String>();
        mUsersAdapter = new ArrayAdapter<String>(this,R.layout.list_item,R.id.list_item_textView,userNames);
        mUsersListView.setAdapter(mUsersAdapter);

        mUserDatabaseReference = mFirebaseDatabase.getReference().child("Users");

        mUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {
                    mEmptyTextView.setText("No Users Found!");
                    return;
                } else {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                        User currrentUser = postSnapshot.getValue(User.class);
                        users.add(currrentUser);
                        String userName = currrentUser.getName();
                        mUsersAdapter.add(userName);

                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mUsersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                displayUserInformation(i);


            }
        });

    }

    private void displayUserInformation(int position) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogueView = inflater.inflate(R.layout.users_information_dialog,null);

        builder.setView(dialogueView);

        builder.setTitle(R.string.user);

        final AlertDialog userDialog = builder.create();
        userDialog.show();

        TextView nameTextView = (TextView) dialogueView.findViewById(R.id.user_name_textView);
        TextView emailTextView = (TextView) dialogueView.findViewById(R.id.user_email_textView);
        TextView phoneTextView = (TextView) dialogueView.findViewById(R.id.user_phone_textView);

        User selectedUser =users.get(position);

        nameTextView.setText(selectedUser.getName());
        emailTextView.setText(selectedUser.getEmail());
        phoneTextView.setText(selectedUser.getPhone());

    }
}
