package com.example.googlekeep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class CreateAccount extends AppCompatActivity {
    EditText email_edit_text, password_edit_text, confirm_passwords_edit_text;
    Button create_account_btn;
    ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        email_edit_text = findViewById(R.id.email_edit_text);
        password_edit_text = findViewById(R.id.password_edit_text);
        confirm_passwords_edit_text = findViewById(R.id.confirm_passwords_edit_text);
        create_account_btn = findViewById(R.id.create_account_btn);
        progress_bar = findViewById(R.id.progress_bar);

        create_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

    }

    void createAccount() {
        String email = email_edit_text.getText().toString();
        String password = password_edit_text.getText().toString();
        String confirmPassword = confirm_passwords_edit_text.getText().toString();

        if (validateData(email, password, confirmPassword)) {
            createAccountInFirebase(email, password);
        }
        else {
            return;
        }
    }

    private void createAccountInFirebase(String email, String password) {
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            changeInProgress(false);
            if (task.isSuccessful()) {
                //success
                firebaseAuth.getCurrentUser().sendEmailVerification();
                firebaseAuth.signOut();
                Toast.makeText(this, "Please verify your Email", Toast.LENGTH_SHORT).show();
                finish();
            }
            else {
                // failure
                Toast.makeText(this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    void changeInProgress(boolean inProgress) {
        if (inProgress) {
            progress_bar.setVisibility(View.VISIBLE);
            create_account_btn.setVisibility(View.GONE);
        }
        else {
            progress_bar.setVisibility(View.GONE);
            create_account_btn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password, String confirmPassword) {
        String specialCharRegex= ".*[@#!$%^&+=].*";
        String UpperCaseRegex= ".*[A-Z].*";
        String NumberRegex= ".*[0-9].*";

        // compiling pattern
        Pattern specialCharPattern = Pattern.compile(specialCharRegex);
        Pattern upperCasePattern = Pattern.compile(UpperCaseRegex);
        Pattern numberPattern = Pattern.compile(NumberRegex);

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_edit_text.setError("Email is invalid");
            return false;
        }

        // checking password field
        if ((password.length() <8)) {
            password_edit_text.setError("Password must be of atleast 8 character");
            return false;
        }

        if (!upperCasePattern.matcher(password).matches()) {
            password_edit_text.setError("Upper Character missing");
            return false;
        }
        if (!numberPattern.matcher(password).matches()) {
            password_edit_text.setError("Number missing");
            return false;
        }
        if (!specialCharPattern.matcher(password).matches()) {
            password_edit_text.setError("Special Character missing");
            return false;
        }

        // checking confirm password field
        if (!password.equals(confirmPassword)) {
            confirm_passwords_edit_text.setError("Password not matched");
            return false;
        }
        return true;
    }


}