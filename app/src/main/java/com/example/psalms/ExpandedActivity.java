package com.example.psalms;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.io.IOException;

public class ExpandedActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    public CommonFunctions f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded);

        ImageButton play_pause = findViewById(R.id.play_pause_btn);
        TextView title_bar = findViewById(R.id.title);
        ImageButton star = findViewById(R.id.favoriteStar);
        f = new CommonFunctions(this, play_pause, title_bar, star);

        for (int i = 0; i < ScrollingActivity.psalms.length; i++) {
            try {
                ScrollingActivity.psalms[i].getSong().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        f.finish();
                    }
                });
            } catch (NullPointerException npe) {

            }
        }
        f.psalms = ScrollingActivity.psalms;

        int current_psalm_num = Integer.parseInt(getIntent().getStringExtra("current_psalm_num"));
        try {
            f.current_psalm = f.psalms[f.getPsalmIdx(current_psalm_num)];
            f.updateTitleBar();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Boolean play_status = Boolean.valueOf(getIntent().getStringExtra("play_status"));
        if (play_status) {
            f.pauseBtn();
        } else {
            f.playBtn();
        }

        Boolean repeat_status = Boolean.valueOf(getIntent().getStringExtra("repeat_status"));
        f.repeat = repeat_status;
        f.repeat_btn = findViewById(R.id.repeat_btn);

        if (f.repeat) {
            f.repeat_btn.setImageResource(R.drawable.repeat_on);
        } else {
            f.repeat_btn.setImageResource(R.drawable.repeat);
        }

        mViewPager = findViewById(R.id.view_pager);
        setupViewPager(mViewPager);
    }

    public void favorite(View view) {
        f.favorite();
    }

    public void playStatus(View view) {
        f.playStatus();
    }

    public void repeatStatus(View view) {
        f.repeat = !f.repeat;

        if (f.repeat) {
            f.repeat_btn.setImageResource(R.drawable.repeat_on);
        } else {
            f.repeat_btn.setImageResource(R.drawable.repeat);
        }

    }

    public void collapse(View view) {
        ScrollingActivity.psalms = f.psalms;
        Intent intent = new Intent();
        intent.putExtra("repeat_status", Boolean.toString(f.repeat));
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        if (f.current_psalm.getLyrics() != null) {
            adapter.addFragment(new LyricsFragment(), "Lyrics");
        }
        adapter.addFragment(new PsalmFragment(), "Psalm");
        viewPager.setAdapter(adapter);
    }
}
