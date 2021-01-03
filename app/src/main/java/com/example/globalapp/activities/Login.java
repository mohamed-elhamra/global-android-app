package com.example.globalapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.globalapp.database.DataBaseHelper;
import com.example.globalapp.R;

public class Login extends AppCompatActivity {

    private DataBaseHelper db;
    private SharedPreferences preferences;
    private static final String PREFS_NAME = "PrefsFile";

    private EditText email;
    private EditText password;
    private Button signIn;
    private CheckBox checkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        db = DataBaseHelper.getInstance(this);

        bindWidget();
        signInOnClickListener();
        getPreferencesData();
    }

    private void bindWidget(){
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.signIn);
        checkBoxRememberMe = findViewById(R.id.checkBoxRememberMe);
    }

    private void getPreferencesData() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(preferences.contains("pref_email")){
            String email = preferences.getString("pref_email", "not_found");
            this.email.setText(email);
        }
        if(preferences.contains("pref_password")){
            String password = preferences.getString("pref_password", "not_found");
            this.password.setText(password);
        }
        if(preferences.contains("pref_check")){
            boolean checked = preferences.getBoolean("pref_check", false);
            this.checkBoxRememberMe.setChecked(checked);
        }
    }

    private void signInOnClickListener(){
        signIn.setOnClickListener(v -> {
            String email = this.email.getText().toString();
            String password = this.password.getText().toString();
            Boolean userExist = db.userExist(email, password);
            if(userExist){
                if(checkBoxRememberMe.isChecked()){
                    boolean boolIsChecked = checkBoxRememberMe.isChecked();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("pref_email", email);
                    editor.putString("pref_password", password);
                    editor.putBoolean("pref_check", boolIsChecked);
                    editor.apply();
                }else{
                    preferences.edit().clear().apply();
                }

                Toast.makeText(getApplicationContext(), "Successfully login", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), Category.class);
                startActivity(intent);
                return;
            }
            Toast.makeText(getApplicationContext(), "Wrong credentials", Toast.LENGTH_SHORT).show();
        });
    }

}