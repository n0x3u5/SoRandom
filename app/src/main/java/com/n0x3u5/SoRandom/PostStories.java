package com.n0x3u5.SoRandom;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
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

public class PostStories extends ActionBarActivity {

    EditText et1, et2;
    String postText, sub, username;
    private ProgressDialog pDialog;
    SharedPreferences sp;

    JSONParser jsonParser = new JSONParser();

    private static String url_post_idea = "http://onclave.byethost11.com/sorandom/internal/post.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_stories);

        et1 = (EditText) findViewById(R.id.et_sub);
        et2 = (EditText) findViewById(R.id.et_story);

        SharedPreferences appPrefs = getSharedPreferences("com.n0x3u5.SoRandom_preferences", MODE_PRIVATE);
        username = appPrefs.getString("usernamePref", "");
    }

    public void postStory(View v)
    {
        postText = et2.getText().toString().trim();
        sub =et1.getText().toString().trim();

        if(TextUtils.isEmpty(postText))
        {
            et2.setError("Stories can never go blank!");
        }
        else if(TextUtils.isEmpty(sub))
        {
            et1.setError("We need a suject!");
        }
        else
        {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("story", postText));
            params.add(new BasicNameValuePair("subject", sub));
            params.add(new BasicNameValuePair("username", username));
            PostAStory pas = new PostAStory(params);
            pas.execute();
        }
    }

    public class PostAStory extends AsyncTask<String, String, JSONObject>
    {
        List<NameValuePair> params;

        public PostAStory(List<NameValuePair> params)
        {
            this.params = params;
        }

        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(PostStories.this);
            pDialog.setMessage("Posting your Story...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... args)
        {
            JSONObject json = jsonParser.makeHttpRequest(url_post_idea, "POST", params);
            return json;
        }

        protected void onPostExecute(JSONObject result) {
            pDialog.dismiss();

            String message = null;

            try{
                message = result.getString("message");
            }
            catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            boolean success = false;
            try {
                success = result.getBoolean("success");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if(success == true)
            {
                finish();
            }
            else
            {
                et2.setError("Failed to post. Try again.");
            }

            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
