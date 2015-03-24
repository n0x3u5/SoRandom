package com.n0x3u5.SoRandom;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class StoryPanel extends ListActivity {

    String[] items;
    String[] id_list;
    String[] upvote_list;
    String[] downvote_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        items = extras.getStringArray("items");
        id_list = extras.getStringArray("id_list");
        //upvote_list = extras.getStringArray("upvote_list");
        //downvote_list = extras.getStringArray("downvote_list");

        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));
    }

    public  void onListItemClick(ListView parent, View v, int position, long id)
    {
        Toast.makeText(this, "You have selected " + items[position], Toast.LENGTH_SHORT)
                .show();

        Intent show = new Intent(StoryPanel.this, ViewStory.class);
        show.putExtra("notification", items[position]);
        show.putExtra("id", id_list[position]);
       // show.putExtra("upvote", upvote_list[position]);
       // show.putExtra("downvote", downvote_list[position]);
        startActivity(show);
    }
}
