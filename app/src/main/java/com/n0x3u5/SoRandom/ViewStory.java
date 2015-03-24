package com.n0x3u5.SoRandom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewStory extends ActionBarActivity {

    String id;
    EditText et;
    String comment;
    private ProgressDialog pDialog;

    private static String get_notice = "http://onclave.byethost11.com/sorandom/internal/story.php";
    private static String url_post_comment = "http://onclave.byethost11.com/sorandom/internal/comment.php";
    private static String url_vote = "http://onclave.byethost11.com/sorandom/internal/vote.php";
    private ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();
    TextView tv;

    ArrayAdapter<String> mCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_story);

        String notification = "Getting Notification. Please wait...";
        id = getIntent().getStringExtra("id");

        tv = (TextView)findViewById(R.id.tv_hero_post);

        //build Params
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", id));

        tv.setText(notification);

        CreateNewProduct productTask = new CreateNewProduct(params);
        productTask.execute();
    }

    public void Upvote(View v)
    {
        String uv = "1";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("upv", uv));
        params.add(new BasicNameValuePair("id", id));
        Vote pac = new Vote(params);
        pac.execute();
    }

    public void Downvote(View v)
    {
        String dv = "1";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("dwv", dv));
        params.add(new BasicNameValuePair("id", id));
        Vote pac = new Vote(params);
        pac.execute();
    }

    class Vote extends AsyncTask<String, String, JSONObject>
    {
        List<NameValuePair> params;

        public Vote(List<NameValuePair> params){
            this.params = params;
        }

        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewStory.this);
            pDialog.setMessage("Voting ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... args){

            JSONObject json = jsonParser.makeHttpRequest(url_vote, "POST", params);

            return json;
        }

        protected void onPostExecute(JSONObject result)
        {
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
                Intent res = getIntent();
                finish();
                startActivity(res);
            }
            else
            {
                et.setError("Failed to post. Try again.");
            }

            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    public void PostAnswer(View view)
    {
        et = (EditText) findViewById(R.id.et_comments);

        comment = et.getText().toString().trim();

        if(TextUtils.isEmpty(comment))
        {
            et.setError("Comments can never go blank!");
        }
        else
        {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("com", comment));
            params.add(new BasicNameValuePair("id", id));
            PostAComment pac = new PostAComment(params);
            pac.execute();
        }
    }

    class PostAComment extends AsyncTask<String, String, JSONObject> {
        List<NameValuePair> params;

        public PostAComment(List<NameValuePair> params){
            this.params = params;
        }

        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewStory.this);
            pDialog.setMessage("Posting your Comment ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... args){

            JSONObject json = jsonParser.makeHttpRequest(url_post_comment, "POST", params);

            return json;
        }

        protected void onPostExecute(JSONObject result)
        {
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
                Intent res = getIntent();
                finish();
                startActivity(res);
            }
            else
            {
                et.setError("Failed to post. Try again.");
            }

            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    class CreateNewProduct extends AsyncTask<String, String, JSONObject>
    {
        List<NameValuePair> params;

        public CreateNewProduct(List<NameValuePair> params){
            this.params = params;
        }

        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(ViewStory.this);
            dialog.setMessage("Getting full story. Please wait . . .");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        protected JSONObject doInBackground(String... args)
        {

            JSONObject json = jsonParser.makeHttpRequest(get_notice, "POST", this.params);

            return json;
        }

        protected void onPostExecute(JSONObject result)
        {
            dialog.dismiss();
            //this assumes that the response looks like this:
            //{"success" : true }
            String message = null;
           String username = null;
            try {
                message = result.getString("subject") + "\n\n" + result.getString("message") + "\n\n**********\n\n" + result.getString("comment");
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try
            {
                username = result.getString("username");
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
            boolean success = false;
            try {
                success = result.getBoolean("success");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), success ? "We are good to go." : "Something went wrong!",
            //		Toast.LENGTH_SHORT).show();
            Toast.makeText(getBaseContext(), "Done", Toast.LENGTH_SHORT).show();

            if(success == true)
            {
                tv.setText(message + "\n\n" + "-" + username);
            }
            else if(success == false)
            {
                tv.setText("Unable to get Story. Something went Wrong! :(\n\n" + message);
            }
            else
            {
                tv.setText("Unable to get Story. Something went vehemently Wrong! :'(\n\n" + message);
            }
        }
    }
}
