package com.example.android.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class LoginActivity extends AppCompatActivity {

    SQLiteDatabase db;
    SQLiteOpenHelper openHelper;
    EditText  passwordView;
    TextView emailView;
    Button loginButton, regButton;
    Cursor cursor;
    String stringForEditView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Creates a new instance of the database which can be used by the whole application
        openHelper = new DatabaseHelper(this);
//        Intent i = getIntent();
//        String email = i.getStringExtra("email");

//        try{
//            Log.d("Email is ", email);
//        }catch (Exception e){
//            Log.d("Email Error:", e.toString());
//        }

        //Attempt to grab the first row's email column into the edit text view using the cursor
        db = openHelper.getReadableDatabase();

        String pasteEmail = "SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE ID = 1;";
        Log.d("The tag line is", pasteEmail);

        Cursor c = db.rawQuery(pasteEmail, null);

        c.moveToFirst();

        try{
            stringForEditView = c.getString(3);
            Log.d("The Email is",stringForEditView);
        }catch (Exception e){
            Log.d("Email Error 2",e.toString());
        };


        if(!c.isAfterLast()){
            do{
                stringForEditView = c.getString(3);

            } while(c.moveToNext());

        }

        c.close();


        //Casting the xml elements to be usable here
        emailView = (TextView) findViewById(R.id.emailTextView);
        emailView.setText(stringForEditView);

        passwordView = (EditText) findViewById(R.id.passwordTextView);
        loginButton = (Button) findViewById(R.id.loginButtonView);
//        regButton = (Button) findViewById(R.id.regButtonView);

        //Here is where the trouble begins
        //Create a click listener for the log in button here
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Opening the database for view
                db = openHelper.getReadableDatabase();
                //the parameters to be used are established here
                String email = emailView.getText().toString();
                String passWord = passwordView.getText().toString();
                String query1 = "SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " +
                        DatabaseHelper.COLUMN_EMAIL + " = ? AND " + DatabaseHelper.COLUMN_PASSWORD +
                        " = ?";
                //Use the cursor here to point at a specific location in the database
                cursor = db.rawQuery(query1, new String[]{email, passWord});

                if (cursor != null){
                    if(cursor.getCount() > 0){
                        cursor.moveToNext();
                        Toast.makeText(getApplicationContext(), "Login Successful",
                                Toast.LENGTH_SHORT).show();
                        //Clear data on the xml
                        passwordView.setText("");

                        //Redirect to Home page xml
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Error in password",
                                Toast.LENGTH_SHORT).show();
                        //Clear data on the xml
                        passwordView.setText("");
                    }
                }
            }
        });

        //This code reverts to the Register XML just in Case user has not registered
//        regButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Redirects to the login xml
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });


    }

    //These lines allows the user to leave the app after pressing the back key twice.
    public int backPress;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {

        if(!checkIfDataBaseIsEmpty()){
            finish();
        }
        backPress = backPress + 1;
        Toast.makeText(getApplicationContext(),"Press again to exit", Toast.LENGTH_SHORT).show();

        if(doesDatabaseExist(getApplicationContext(), DatabaseHelper.DATABASE_NAME) && backPress> 1){
            finishAffinity();
        }

    }

    //THESE LINES SEEM NOT TO WORK. JUST IGNORE. DOES NOT AFFECT THE CODE.. UNFORTUNATELY!cdm

    //This method checks if a database exists or not
    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    //Checking if there database if empty
    public boolean checkIfDataBaseIsEmpty(){
        boolean check;

        openHelper = new DatabaseHelper(this);
        db = openHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME;
        Cursor c = db.rawQuery(query,null);

        if(c == null){
            check = false;
        } else {
            check = true;
        }

        return check;
    }
}
