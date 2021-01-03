package com.example.globalapp.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.globalapp.database.DataBaseHelper;
import com.example.globalapp.R;
import com.example.globalapp.model.Restaurant;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Registration extends AppCompatActivity {

    DataBaseHelper db;

    EditText email;
    EditText password;
    EditText confirmPassword;
    Button register;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        bindWidget();
        registrationOnClickListener();
    }

    private void bindWidget(){
        db = DataBaseHelper.getInstance(this);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        register = findViewById(R.id.register);
    }

    private void registrationOnClickListener(){
        register.setOnClickListener(v -> {
            String email = this.email.getText().toString();
            String password = this.password.getText().toString();
            String confirmPassword = this.confirmPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please provide all fields", Toast.LENGTH_SHORT).show();
            } else {
                if (password.equals(confirmPassword)) {
                    if (!db.emailExists(email)) {
                        if (db.insert(email, password)) {
                            Toast.makeText(getApplicationContext(), "Registered successfully", Toast.LENGTH_SHORT).show();
                            this.email.getText().clear();
                            this.password.getText().clear();
                            this.confirmPassword.getText().clear();
                            Intent intent = new Intent(this, Login.class);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Email already exits", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Password do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}