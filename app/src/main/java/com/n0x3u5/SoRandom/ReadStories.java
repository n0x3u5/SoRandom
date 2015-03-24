package com.n0x3u5.SoRandom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadStories extends ActionBarActivity {

    String message3 = "";
    String[] items;
    String[] id_list;
   // String[] upvote_list;
   // String[] downvote_list;
    TextView tv;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_stories);

        String localhost = "http://onclave.byethost11.com/sorandom/internal/show.php";
        tv = (TextView) findViewById(R.id.textView1);

        tv.setText("Downloading Recent Stories. Please wait...");
        new ReadJSONFeedTask().execute(localhost);
    }

    public void showStory(View v)
    {
        Intent i = new Intent(ReadStories.this, StoryPanel.class);
        Bundle b = new Bundle();

        b.putStringArray("items", items);
        b.putStringArray("id_list", id_list);
      //  b.putStringArray("upvote_list", upvote_list);
       // b.putStringArray("downvote_list", downvote_list);
        i.putExtras(b);
        startActivity(i);
    }

    private class ReadJSONFeedTask extends AsyncTask<String, Void, String>
    {
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(ReadStories.this);
            dialog.setMessage("Downloading Posted Stories. Please wait . . .");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        protected String doInBackground(String...urls)
        {
            return readJSONFeed(urls[0]);
        }

        protected void onPostExecute(String result)
        {
            dialog.dismiss();

            try
            {
                JSONArray jsonArray = new JSONArray(result);

                items = new String[jsonArray.length()];
                id_list = new String[jsonArray.length()];
             //   upvote_list = new String[jsonArray.length()];
             //   downvote_list = new String[jsonArray.length()];

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jobj = (JSONObject)jsonArray.get(i);

                    message3 = "SUBJECT : " + jobj.getString("subject");

                    Log.i("position", "position:"+ i);

                    items[i] = message3;
                    id_list[i] = jobj.getString("id");
                  //  upvote_list[i] = "Upvotes : " + jobj.getString("upvote_string");
                  //  downvote_list[i] = "Downvotes : " + jobj.getString("downvote_string");
                }
                message3 = Integer.toString(jsonArray.length());
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Log.e("error", "error parsing JSON");
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            tv.setText("Done! Click to view Stories.");
        }
    }

    public String readJSONFeed(String URL)
    {
        StringBuilder sb = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet hg = new HttpGet(URL);
        try
        {
            HttpResponse response = client.execute(hg);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if(statusCode == 200)
            {
                HttpEntity en = response.getEntity();
                InputStream content = en.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while((line = reader.readLine()) != null)
                {
                    sb.append(line);
                }
            }
            else
            {
                Log.e("JSON", "Failed to download File");
            }
        }
        catch(ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
