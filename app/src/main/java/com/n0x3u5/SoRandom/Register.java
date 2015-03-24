package com.n0x3u5.SoRandom;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Register extends ActionBarActivity {

    EditText et1, et2, et3, et4;
    private ProgressDialog pDialog;
    boolean netCheck;
    String email, username, password, cnf_password;
    SharedPreferences sp;
    JSONParser jsonParser = new JSONParser();

    private static String register_user_url = "http://onclave.byethost11.com/sorandom/internal/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //check Internet connection
        if (isInternetOn())
        {
            netCheck = true;
        }
        else
        {
            netCheck = false;
        }

        et1 = (EditText) findViewById(R.id.et_username);
        et2 = (EditText) findViewById(R.id.et_email);
        et3 = (EditText) findViewById(R.id.et_password);
        et4 = (EditText) findViewById(R.id.et_cnf_password);
    }

    public void registerMe(View v)
    {
        if(netCheck == true)
        {
            //Check internet connectivity
            ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            {
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                if(netInfo == null)
                {
                    new AlertDialog.Builder(Register.this)
                            .setTitle(getResources()
                                    .getString(R.string.app_name))
                            .setMessage(getResources().getString(R.string.internet_error))
                            .setPositiveButton("OK", null).show();
                }
                else
                {
                    email = et2.getText().toString().trim();
                    username = et1.getText().toString().trim();
                    password = et3.getText().toString();
                    cnf_password = et4.getText().toString();

                    if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(cnf_password) || TextUtils.isEmpty(username))
                    {
                        if(TextUtils.isEmpty(email))
                        {
                            et2.setError("This field cannot be empty");
                        }
                        if(TextUtils.isEmpty(password))
                        {
                            et3.setError("This field cannot be empty");
                        }
                        if(TextUtils.isEmpty(cnf_password))
                        {
                            et4.setError("This field cannot be empty");
                        }
                        if(TextUtils.isEmpty(username))
                        {
                            et1.setError("This field cannot be empty");
                        }
                    }
                    else
                    {
                        boolean check = checkPass(password, cnf_password);

                        if(check == false)
                        {
                            et3.setError("Passwords do not match");
                            et4.setError("Passwords do not match");
                        }
                        else if(check == true)
                        {
                            //build Params
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("email", email));
                            params.add(new BasicNameValuePair("password", password));
                            params.add(new BasicNameValuePair("username", username));

                            RegisterNewUser reg_user = new RegisterNewUser(params);
                            reg_user.execute();
                        }
                    }
                }
            }
        }
        else
        {
            Toast.makeText(getBaseContext(), "Device is NOT connected to the Internet", Toast.LENGTH_SHORT).show();
        }
    }

    class RegisterNewUser extends AsyncTask<String, String, JSONObject>
    {
        List<NameValuePair> params;

        public RegisterNewUser(List<NameValuePair> params)
        {
            this.params = params;
        }

        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Registering Ourselves. Please wait . . .");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... args)
        {
            JSONObject json = jsonParser.makeHttpRequest(register_user_url, "POST", this.params);
            return json;
        }

        protected void onPostExecute(JSONObject result)
        {
            pDialog.dismiss();

            String message = null;
            boolean success = false;

            try
            {
                message = result.getString("message");
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }

            try
            {
                success = result.getBoolean("success");
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }

            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();

            if(success == true)
            {
                SharedPreferences appPrefs = getSharedPreferences("com.n0x3u5.SoRandom_preferences", MODE_PRIVATE);
                SharedPreferences.Editor prefEd = appPrefs.edit();
                prefEd.putString("emailPref", email);
                prefEd.putString("passwordPref",  password);
                prefEd.putString("usernamePref", username);
                prefEd.commit();

                sp = getSharedPreferences("HiddenPrefs", Context.MODE_PRIVATE);
                Editor editor = sp.edit();
                editor.putBoolean("loggedIn", true);
                editor.commit();

                Intent i = new Intent(Register.this, HomeScreen.class);
                startActivity(i);
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

    public boolean checkPass(String a, String b)
    {
        if(a.equals(b))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
