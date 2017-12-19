package com.example.android.register;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    EditText firstName, lastName, email, phoneNumber, password;
    Button register, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Does database exist? If yes, move to the login page promptly
        if(doesDatabaseExist(getApplicationContext(),DatabaseHelper.DATABASE_NAME)) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }


        //Create a new instance of the DatabaseHelper Class to be used by the application
        openHelper = new DatabaseHelper(MainActivity.this);

        //Instantiating the xml elements
        firstName = (EditText) findViewById(R.id.firstNameText);
        lastName = (EditText) findViewById(R.id.lastNameText);
        email = (EditText) findViewById(R.id.emailText);
        phoneNumber = (EditText) findViewById(R.id.phoneNumberText);
        password = (EditText) findViewById(R.id.passwordText);
        register = (Button) findViewById(R.id.registerButton);
        login = (Button) findViewById(R.id.loginButton);

        //Creating a Click listener for the registration button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Use the validate method to check if there are any values
                if(!validateData()){
                    register.setEnabled(true);
                    return;
                } else {
                    register.setEnabled(false);
                }

                //"Open" the db for data to be "written" in
                db = openHelper.getWritableDatabase();

                // These are the parameters that we want in the Insert data method
                String first_name = firstName.getText().toString().trim();
                String last_name = lastName.getText().toString().trim();
                String email_name = email.getText().toString();
                String phone_name = phoneNumber.getText().toString().trim();
                String password_name = password.getText().toString();

                //Call the method here
                insertData(first_name, last_name, email_name, phone_name, password_name);

                //Create a Toast message to alert the user that his/her data has been saved to db
                Toast.makeText(MainActivity.this, "Registration Successful",
                        Toast.LENGTH_SHORT).show();

                //After data has been saved and the toast appears, delete the content on screen
                firstName.setText("");lastName.setText("");email.setText("");phoneNumber.setText("");
                password.setText("");

                //After successful registration, redirect focus to the Login xml
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        //Create a click listener for the log in button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Redirects to the login xml
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    //Creating a method that will insert these values into the data base
    public void insertData(String f_name, String l_name, String e_mail, String phone_number,
                           String pass_word) {
        ContentValues values = new ContentValues();

        //Put the values that you want inside the database in the order you want
        values.put(DatabaseHelper.COLUMN_FIRST_NAME, f_name);
        values.put(DatabaseHelper.COLUMN_LAST_NAME, l_name);
        values.put(DatabaseHelper.COLUMN_EMAIL, e_mail);
        values.put(DatabaseHelper.COLUMN_PHONE_NUMBER, phone_number);
        values.put(DatabaseHelper.COLUMN_PASSWORD, pass_word);

        //This is where the data is being passed into the database
        db.insert(DatabaseHelper.TABLE_NAME, null, values);
        db.close();
    }

    public boolean validateData() {
        boolean valid = true;
        if(firstName.getText().toString().isEmpty()){
            valid = false;
            firstName.setError("Fill in this field.");
        }if(lastName.getText().toString().isEmpty()){
            valid = false;
            lastName.setError("Fill in this field.");
        }if(email.getText().toString().isEmpty()){
            valid = false;
            email.setError("Fill in this field.");
        }if(!isValidEmailId(email.getText().toString().trim())){
            valid = false;
            email.setError("Invalid email pattern");
        }if(phoneNumber.getText().toString().isEmpty()){
            valid = false;
            phoneNumber.setError("Fill in this field.");
        }if(password.getText().toString().isEmpty()){
            valid = false;
            password.setError("Fill in this field.");
        } if(password.getText().length() < 7){
            valid = false;
            password.setError("7 character or more only.");
        }

        return valid;
    }

    //This method attempts to check if an email is valid
    private boolean isValidEmailId(String email){

         return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();

    }

    //This method checks if a database exists or not
    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

}
