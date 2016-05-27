package com.mood.moods;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity implements Filterable{

    ListView items;
    TextView nothing;
    ArrayAdapter adapter;
    static ArrayList<HashMap<String, String>> playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search All Songs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        items = (ListView) findViewById(R.id.listView5);
        playlist = new ArrayList<>();
        playlist = AllSongsFragment.songsDetails;
        nothing = (TextView) findViewById(R.id.textView10);
        nothing.setVisibility(View.INVISIBLE);

        adapter = new SongsArtistsCustomAdapter(this, playlist);
        items.setAdapter(adapter);
    }


    @Override
    public Filter getFilter() {
          Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.toString().toLowerCase();
                FilterResults filterResults = new FilterResults();
                ArrayList<HashMap<String, String>> f;
                ArrayList<HashMap<String, String>> p;
                p = playlist;
                f = playlist;
                if(constraint != null || constraint.length() > 0){
                    for(int i=0; i<p.size(); i++){
                        HashMap<String, String> product = p.get(i);
                        if(product.get("Name").contains(constraint)){
                            f.add(product);
                        }
                    }
                    filterResults.count = f.size();
                    filterResults.values = f;
                }else{
                    filterResults.count = p.size();
                    filterResults.values = p;
                    items.setVisibility(View.INVISIBLE);
                    nothing.setVisibility(View.VISIBLE);
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                playlist = (ArrayList<HashMap<String, String>>) results.values;
                ((BaseAdapter)adapter).notifyDataSetChanged();
            }
        };

        return filter;

    }

    Menu menu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        this.menu = menu;
        SearchManager sm = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView sv = (SearchView) menu.findItem(R.id.action_search).getActionView();
        sv.setSearchableInfo(sm.getSearchableInfo(getComponentName()));
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SearchActivity.this.adapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
