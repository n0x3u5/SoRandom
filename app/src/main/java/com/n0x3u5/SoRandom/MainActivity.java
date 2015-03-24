package com.n0x3u5.SoRandom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    boolean check;
    boolean netCheck;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        if(getIntent().getBooleanExtra("EXIT", false))
        {
            finish();
        }
        else
        {

            Toast.makeText(getBaseContext(), "Starting #SoRandom...", Toast.LENGTH_SHORT).show();

            if(isInternetOn())
            {
                netCheck = true;
            }
            else
            {
                netCheck = false;
            }

            sp = getSharedPreferences("HiddenPrefs", Context.MODE_PRIVATE);

            if(sp.contains("loggedIn"))
            {
                check = sp.getBoolean("loggedIn", false);

                if(check == true)
                {
                    SharedPreferences appPrefs = getSharedPreferences("com.n0x3u5.SoRandom_preferences", MODE_PRIVATE);
                    String s = appPrefs.getString("emailPref", "");
                    String u = appPrefs.getString("usernamePref", "");

                    if((s.length() > 0) && (u.length() > 0))
                    {
                        String msg = "Logged in as " + u + " | " + s;
                        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                        Intent i = new Intent(MainActivity.this, HomeScreen.class);
                        startActivity(i);
                    }
                }
                else
                {

                    setContentView(R.layout.activity_main);
                    Toast.makeText(getBaseContext(), "Please Login or Register to continue", Toast.LENGTH_LONG).show();
                }
            }
            else
            {

                setContentView(R.layout.activity_main);
            }
        }
    }

    public boolean isInternetOn()
    {
        ConnectivityManager connec = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        //if we are connected to Internet
        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED || connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED)
        {
            Toast.makeText(getBaseContext(), "Device is connected to the Internet", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED || connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED)
        {
            Toast.makeText(getBaseContext(), "Device is NOT connected to the Internet", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            Toast.makeText(getBaseContext(), "There was an error connecting to the Internet", Toast.LENGTH_SHORT).show();
            return  false;
        }
    }

    public void startRegister(View view) {
        if(netCheck == true)
        {
            Intent intent = new Intent(this,Register.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getBaseContext(), "Device is NOT connected to the Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void startLogin(View view) {
        if(netCheck == true)
        {
            Intent intent = new Intent(this,Login.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getBaseContext(), "Device is NOT connected to the Internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                    }
                }).create().show();
    }
}
