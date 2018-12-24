package com.example.muhammadusama.parking_booking_system;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    private TextView mSignUpTextview;
    private ProgressBar mProgressBar;
    private Button mLoginButton;
    private EditText mEmailEditText,mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mSignUpTextview =(TextView) findViewById(R.id.textViewSignup);
        mEmailEditText = (EditText) findViewById(R.id.editTextEmail);
        mPasswordEditText = (EditText) findViewById(R.id.editTextPassword);
        mProgressBar =(ProgressBar) findViewById(R.id.progressbar);
        mLoginButton = (Button) findViewById(R.id.buttonLogin);

        mSignUpTextview.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();

       if (mAuth.getCurrentUser()!=null){

            if(mAuth.getCurrentUser().getEmail().equals("admin@gmail.com")){

                Intent adminIntent = new Intent(MainActivity.this, AdminActivity.class);
                adminIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(adminIntent);
                finish();

            }else{

                Intent userIntent = new Intent(MainActivity.this, UserActivity.class);
                userIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(userIntent);
                finish();

            }
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.textViewSignup:

                Intent signuUpIntent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(signuUpIntent);
                finish();
                break;

            case R.id.buttonLogin:

                Login();
                break;
        }
    }

    private void Login() {

        String email = mEmailEditText.getText().toString().trim();
        final String password = mPasswordEditText.getText().toString().trim();

        if (email.isEmpty()){

            mEmailEditText.setError("Enter your Email!");
            mEmailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            mEmailEditText.setError("Invalid Email!");
            mEmailEditText.requestFocus();
            return;
        }
        if (password.isEmpty()){
            mPasswordEditText.setError("Enter your Password!");
            mPasswordEditText.requestFocus();
            return;
        }

        if (password.length()<6){
            mPasswordEditText.setError("Minimum Password Limit is 6");
            mPasswordEditText.requestFocus();
            return;
        }

        mProgressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        mProgressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()){

                            if (mAuth.getCurrentUser().getEmail().equals("admin@gmail.com") && password.equals("123456")) {

                                    Toast.makeText(MainActivity.this, "Login Successfull!", Toast.LENGTH_SHORT).show();

                                    Intent adminIntent = new Intent(MainActivity.this, AdminActivity.class);
                                    adminIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(adminIntent);
                                    finish();

                            }else {

                                Toast.makeText(MainActivity.this, "Login Successfull!", Toast.LENGTH_SHORT).show();

                                Intent userIntent = new Intent(MainActivity.this, UserActivity.class);
                                userIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(userIntent);
                                finish();

                            }
                        }else{

                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }
}
