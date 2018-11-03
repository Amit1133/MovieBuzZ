package com.example.amit.moviebuzz;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.login.LoginManager;

public class Homepage extends AppCompatActivity {
    Button searchBtn;
    TextInputLayout inputET;
    MyDBHelper myDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        inputET = findViewById(R.id.input_et);
        searchBtn = findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchingData();
            }
        });


    }

    private void showSearchingData() {
        String text = inputET.getEditText().getText().toString().trim();
        if (text != null || text == "") {
            if (text.length() > 2) {
                Intent i = new Intent(Homepage.this, ResultActivity.class);
                i.putExtra("searchKey", text);
                startActivity(i);
            } else {
                Toast.makeText(this, "need at least 3 letter to search..", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Type a movie name!", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(Account account) {
                Toast.makeText(Homepage.this, "" + account.getId().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(AccountKitError accountKitError) {

            }
        });
    }

    public void logoutNow() {
        if (myDBHelper == null) {
            myDBHelper = new MyDBHelper(this);
            SQLiteDatabase sqLiteDatabase = myDBHelper.getWritableDatabase();
        }
        myDBHelper.deleteTable();
        AccountKit.logOut();
        LoginManager.getInstance().logOut();
        finish();
        startActivity(new Intent(Homepage.this, LoginActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.buzz_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            logoutNow();
        } else if (item.getItemId() == R.id.menu_profile) {
            if (AccessToken.getCurrentAccessToken() == null) {
                Toast.makeText(this, "Use facebook login to see profile update", Toast.LENGTH_SHORT).show();
            } else {
                //String s=getIntent().getStringExtra("username");
                SharedPreferences prefs = getSharedPreferences("myInformation", MODE_PRIVATE);
                String name = prefs.getString("name", "No name obtained");
                String email = prefs.getString("email", "No email found");

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setTitle("Profile Info");
                alertBuilder.setMessage("Name: " + name + "\nEmail: " + email);
                alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertBuilder.create().show();

            }

        }

        else if(item.getItemId()== R.id.menu_bookmark){
            startActivity(new Intent(Homepage.this,BookmarkShowActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
