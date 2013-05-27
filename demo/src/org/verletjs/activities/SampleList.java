package org.verletjs.activities;


import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.verletjs.R;
import org.verletjs.activities.MainActivity;

/**
 * Created with IntelliJ IDEA.
 * User: George
 * Date: 01.05.13
 * Time: 15:50
 * To change this template use File | Settings | File Templates.
 */
public class SampleList extends ListActivity {

    public void onCreate(Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
       // showActionBar();
      //  this.setTheme(com.actionbarsherlock.R.style.Theme_Sherlock);
        String[] samples = getResources().getStringArray(R.array.samples);
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, samples));
    }
//    private void showActionBar() {
//        ActionBar actionBar = getSupportActionBar();
//
//        actionBar.setDisplayHomeAsUpEnabled(true);
//
//        actionBar.setTitle("Sample List");
//    }
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Sample", position);
        startActivity(intent);
    }
}