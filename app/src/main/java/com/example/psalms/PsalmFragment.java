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
public class PsalmFragment extends Fragment {


    public PsalmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_psalm, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView english = getView().findViewById(R.id.english);
        TextView chinese = getView().findViewById(R.id.chinese);

        String eng_txt = ((ExpandedActivity)this.getActivity()).f.current_psalm.getEnglish();
        String ch_txt = ((ExpandedActivity)this.getActivity()).f.current_psalm.getChinese();

        english.setText(eng_txt);
        chinese.setText(ch_txt);

        Typeface english_font = getResources().getFont(R.font.english_font);
        Typeface chinese_font = getResources().getFont(R.font.chinese_font);

        english.setTypeface(english_font);
        chinese.setTypeface(chinese_font);
    }

}
