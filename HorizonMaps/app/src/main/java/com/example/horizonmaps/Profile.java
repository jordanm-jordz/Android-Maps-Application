package com.example.horizonmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile extends AppCompatActivity {

    TextInputLayout fullName, email, phoneNo, password;
    TextView fullNameLabel, usernameLabel;

    String _NAME, _EMAIL, _PHONENO, _PASSWORD;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        reference = FirebaseDatabase.getInstance().getReference("Users");

        //Hooks
        fullName = findViewById(R.id.full_name_profile);
        email = findViewById(R.id.email_profile);
        phoneNo = findViewById(R.id.phone_no_profile);
        password = findViewById(R.id.full_name_profile);
        fullNameLabel = findViewById(R.id.full_name);
        usernameLabel = findViewById(R.id.username_field);
        //ShowAllData
        showAllUserData();
    }
    private void showAllUserData() {
        Intent intent = getIntent();
        _NAME = intent.getStringExtra("fullname");
        _EMAIL = intent.getStringExtra("email");
        _PHONENO = intent.getStringExtra("phone");
        _PASSWORD = intent.getStringExtra("password");

        fullNameLabel.setText(_NAME);
        usernameLabel.setText(_EMAIL);
        fullName.getEditText().setText(_NAME);
        email.getEditText().setText(_EMAIL);
        phoneNo.getEditText().setText(_PHONENO);
        password.getEditText().setText(_PASSWORD);

    }


    public void update(View view) {
        if (isNameChanged() || isPasswordChanged()) {
            Toast.makeText(this, "Data has been updated", Toast.LENGTH_LONG).show();
        }
        else Toast.makeText(this, "Data has not been changed", Toast.LENGTH_LONG).show();

    }

    private boolean isPasswordChanged() {
        if (!_PASSWORD.equals(password.getEditText().getText().toString())){
            reference.child(_NAME).child("password").setValue(password.getEditText().getText().toString());
            _PASSWORD = password.getEditText().getText().toString();
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isNameChanged() {
        if (!_NAME.equals(fullName.getEditText().getText().toString())){
            reference.child(_NAME).child("name").setValue(fullName.getEditText().getText().toString());
            _NAME = fullName.getEditText().getText().toString();
            return true;
        }
        else {
            return false;
        }
    }
}