package com.example.android.register;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    String userNameTextView;
    TextView welcomeTextView;
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Gets permission to use the Database
        openHelper = new DatabaseHelper(this);

        //Gets access to 'read' rows in the database
        db = openHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE ID = 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if(!c.isAfterLast()){
            do{
                userNameTextView = c.getString(1) + " " + c.getString(2);
            } while(c.moveToNext());
        }

        welcomeTextView = (TextView) findViewById(R.id.welcome);
        welcomeTextView.setText(userNameTextView);

        c.close();

    }

    //These lines allows the user to leave the app after pressing the back key twice.
    public int backPress;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        backPress = backPress + 1;
        Toast.makeText(getApplicationContext(),"Press again to exit", Toast.LENGTH_SHORT).show();

        if(backPress> 1){
            finishAffinity();
        }
    }
}
