package com.mood.moods;

import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class ArtistFragment extends Fragment {

    GridView gd;
    TextView noArtists;
    String[] artists;
    public ArtistFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_artist_fragment, container, false);
        gd = (GridView) rootView.findViewById(R.id.gridView2);
        noArtists = (TextView) rootView.findViewById(R.id.textView2);

        artists = getArtists();
        if(artists.length > 0 || artists != null){
            noArtists.setVisibility(getView().INVISIBLE);
        }
        else{
            noArtists.setVisibility(getView().VISIBLE);
        }

        ListAdapter adapter = new ArtistCustomAdapter(getActivity(), artists);
        gd.setAdapter(adapter);

        gd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in =new Intent(getActivity(), SongsArtists.class);
                in.putExtra("Artists", artists[position]);
                startActivity(in);
            }
        });

        return rootView;
    }

    String[] getArtists(){
        Cursor mCursor = getActivity().getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                new String[] {MediaStore.Audio.Artists.ARTIST}, null, null,
                "LOWER(" + MediaStore.Audio.Albums.ARTIST + ")ASC");

        int count = mCursor.getCount();
        String[] artists = new String[count];

        int i=0;
        if(mCursor.moveToFirst()){
            do {
                String s = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST));
                artists[i] = s;
                i++;
            }while (mCursor.moveToNext());
        }
        mCursor.close();
        return artists;
    }
}
