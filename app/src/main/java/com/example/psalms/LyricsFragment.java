package com.example.psalms;


import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class LyricsFragment extends Fragment {


    public LyricsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lyrics, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView lyrics = getView().findViewById(R.id.lyrics);

        String lyr_txt = ((ExpandedActivity)this.getActivity()).f.current_psalm.getLyrics();
        lyrics.setText(lyr_txt);

        Typeface custom_font = getResources().getFont(R.font.chinese_font);
        lyrics.setTypeface(custom_font);
    }

}
