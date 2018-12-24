package com.example.muhammadusama.kotlin_friendly_chat

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.NonNull
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.*

class MainActivity : AppCompatActivity() {

    private val ANONYMOUS  = "anonymous"
    private  val DEFAULT_MESSAGE_LENGTH_LIMIT = 1000

    //Request Code
    private val RC_SIGN_IN : Int = 1;

    //For ImagePicker Button Intent
    private val RC_PHOTO_PICKER : Int =  2;


    private lateinit  var mMessageListView : ListView
    private lateinit  var mMessageAdapter : MessageAdapter

    private lateinit var mProgressBar : ProgressBar
    private lateinit var mPhotoPickerButton : ImageButton
    private lateinit var mMessageEditText : EditText
    private lateinit var mSendButton : Button

    //For Username
    private lateinit var mUsername : String
    private lateinit var mUserID : String

    //For Firebase Features

    //Firebase Database
    private lateinit var mFirebaseDatabase : FirebaseDatabase
    private lateinit var mMessagesDatabaseReference : DatabaseReference
    private var mChildEventListener : ChildEventListener? = null


    //Firebase Storage
    private lateinit var mFirebaseStorage : FirebaseStorage
    private lateinit var mChatPhotosStorageReference : StorageReference


    //Firebase Authentication
    private lateinit var mFirebaseAuth : FirebaseAuth
    private lateinit var mAuthStateListener : FirebaseAuth.AuthStateListener



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mUsername = ANONYMOUS

        //Initialize Firebase Components
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mFirebaseStorage = FirebaseStorage.getInstance()

        mFirebaseAuth = FirebaseAuth.getInstance()


        // Initialize references to views
        mProgressBar =  findViewById(R.id.progressBar)
        mMessageListView =  findViewById(R.id.messageListView)
        mPhotoPickerButton =  findViewById(R.id.photoPickerButton)
        mMessageEditText =  findViewById(R.id.messageEditText)
        mSendButton =  findViewById(R.id.sendButton)


        // Initialize message ListView and its adapter
        var  friendlyMessages : List<FriendlyMessage> =  ArrayList<FriendlyMessage>()
        mMessageAdapter = MessageAdapter(this, R.layout.item_message, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE)


        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(View.OnClickListener {

            var friendlyMessage : FriendlyMessage = FriendlyMessage(mUsername,mMessageEditText.text.toString(), null)
            mMessagesDatabaseReference.push().setValue(friendlyMessage)
            // Clear input box
            mMessageEditText.setText("");

        })

        // ImagePickerButton shows an image picker to upload a image for a message
        mPhotoPickerButton.setOnClickListener(View.OnClickListener {

            var intent = Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);

        })

        mAuthStateListener =  FirebaseAuth.AuthStateListener {

            var user : FirebaseUser?  = it.currentUser

            if (user != null) {

                //user is signed in

                mUserID =user.getUid();
                if (mUserID != null) {
                    mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages").child(mUserID);
                    mChatPhotosStorageReference = mFirebaseStorage.getReference().child("chat_photos").child(mUserID);
                }

                onSignedInInitialize(user.displayName); // User Define Method
            } else {
                //user is signed out
                onSignedOutCleanup();   // User Define Method

                // Code is Copied from Firebase UI auth Github Repository
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(Arrays.asList(
                                        AuthUI.IdpConfig.EmailBuilder().build(),
                                        AuthUI.IdpConfig.GoogleBuilder().build()))
                                .build(),
                        RC_SIGN_IN);
            }
        };


    }

    override fun onResume() {
        super.onResume()
        mFirebaseAuth.addAuthStateListener(mAuthStateListener)

    }

    override fun onPause() {
        super.onPause()

        if(mAuthStateListener!=null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener)
        }

        detachDatabaseReadListener()
        mMessageAdapter.clear()

    }

    private fun onSignedOutCleanup() {

    mUsername = ANONYMOUS;
    mMessageAdapter.clear()
    detachDatabaseReadListener()   // User Define Method


}

private fun onSignedInInitialize(displayName: String?) {

    mUsername = displayName!!
    attachDatabaseReadListener()  // User Define Method

}

    private fun attachDatabaseReadListener() {

        if (mChildEventListener == null) {
            mChildEventListener = object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                    var  friendlyMessage : FriendlyMessage = p0.getValue(FriendlyMessage :: class.java)!!
                    mMessageAdapter.add(friendlyMessage)

                }

                override fun onChildRemoved(p0: DataSnapshot) {
                }

            }

            mMessagesDatabaseReference.addChildEventListener(mChildEventListener!!)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {

                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(this, "Sign in Canceled", Toast.LENGTH_SHORT).show();
                finish()
            }

        } else if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {

           var  selectedImageUri :  Uri = data!!.getData()

            //Get a reference to store file at chat_photos/<FILENAME>
            var photoRef :StorageReference = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment())

            //Upload file to Firebase Storage
            photoRef.putFile(selectedImageUri).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> {

                //var downloadUrl: Uri = it.getDownloadUrl()
                // Storing the URL in Database
                //var friendlyMessage: FriendlyMessage = FriendlyMessage(null, mUsername, downloadUrl.toString())
                //mMessagesDatabaseReference.push().setValue(friendlyMessage);

            })
        }

    }

    private fun detachDatabaseReadListener() {

        if (mChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventListener!!)
           mChildEventListener = null
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         var inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId){

             R.id.sign_out_menu  -> {
                 // sign out
                 AuthUI.getInstance().signOut(this)
                 return true
             }
            else->
            return super.onOptionsItemSelected(item)
        }

    }
}
