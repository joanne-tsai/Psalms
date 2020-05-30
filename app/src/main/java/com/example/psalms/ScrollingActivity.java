package com.example.psalms;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Space;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ScrollingActivity extends AppCompatActivity {

    private CommonFunctions f;
    private int[] psalm_numbers = new int[] {
            16, 19,
            24, 25, 27, 29,
            30, 32, 33, 34,
            40, 42, 43,
            50, 51,
            62, 69,
            80, 88, 89,
            95, 98,
            100,
            116, 118,
            122, 123, 126,
            131,
            142, 143,
            0,
    };
    public static Psalm[] psalms;
    public static ArrayList<Integer> favorites = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        readFavList();

        ImageButton play_pause = findViewById(R.id.play_pause_btn);
        TextView title_bar = findViewById(R.id.title);
        ImageButton star = findViewById(R.id.favoriteStar);
        f = new CommonFunctions(this, play_pause, title_bar, star);

        psalms = new Psalm[psalm_numbers.length];
        for (int i = 0; i < psalm_numbers.length; i++) {
            psalms[i] = new Psalm(this, psalm_numbers[i]);
            try {
                psalms[i].getSong().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        f.finish();
                    }
                });
            } catch (NullPointerException npe) {

            }
        }
        f.psalms = psalms;
        f.repeat = false;

        final LinearLayout main  = findViewById(R.id.mainView);
        Switch fav = findViewById(R.id.favoriteButton);

        wipe(main);
        if (fav.isChecked()) {
            makeFavSongButtons(main);
        } else {
            makeSongButtons(main);
        }

        fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wipe(main);
                if (isChecked) {
                    makeFavSongButtons(main);
                } else {
                    makeSongButtons(main);
                }
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        if (f.current_psalm == null){
            f.current_psalm = f.psalms[0];
            f.updateTitleBar();
        }
    }

    public void expand(View view){
        Intent intent = new Intent(ScrollingActivity.this, ExpandedActivity.class);
        intent.putExtra("current_psalm_num", Integer.toString(f.current_psalm.getNumber()));
        intent.putExtra("play_status", Boolean.toString(getPlayStatus()));
        intent.putExtra("repeat_status", Boolean.toString(f.repeat));
        startActivityForResult(intent, 0);
    }

    private Boolean getPlayStatus(){
        try{
            return f.current_psalm.getSong().isPlaying();
        } catch (NullPointerException npe) {
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (0) : {
                if (resultCode == RESULT_OK) {
                    try {
                        if (f.current_psalm.getSong().isPlaying()) {
                            f.pauseBtn();
                        } else {
                            f.playBtn();
                        }
                    } catch (NullPointerException npe) {
                        f.playBtn();
                    }

                    Boolean repeat_status = Boolean.valueOf(data.getStringExtra("repeat_status"));
                    f.repeat = repeat_status;
                }
                break;
            }
        }
    }

    //Play and pause music
    public void playStatus(View view){
        f.playStatus();
    }

    //Change favorite status
    public void favorite (View view){
        f.favorite();
    }

    //Create buttons for all songs
    private void makeSongButtons (final View main){
        //Making the psalm buttons
        for (int i=0; i < f.psalms.length; i++){
            makeButton(main, i);
        }

        float dip = 60f;
        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );
        Space space = new Space(this);
        space.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) px));
        ((LinearLayout) main).addView(space);
    }

    //Create buttons for favorite songs
    private void makeFavSongButtons(final View main){
        for (int i=0; i < f.psalms.length; i++) {
            if (f.favorites.contains(f.psalms[i].getNumber())) {
                makeButton(main, i);
            }
        }

        float dip = 55f;
        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );
        Space space = new Space(this);
        space.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) px));
        ((LinearLayout) main).addView(space);
    }

    private void makeButton(final View main, int idx) {
        Button btn = new Button(this);
        btn.setText(f.psalms[idx].getTitle2());
        btn.setGravity(Gravity.LEFT);
        btn.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
        Typeface chinese_font = getResources().getFont(R.font.chinese_font);
        btn.setTypeface(chinese_font);
        float dip = 50f;
        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );
        btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) px));
        ((LinearLayout) main).addView(btn);

        //Creating button listener
        final int num = idx;
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Check to see if any songs are currently playing, if so stop it
                for (int j=0; j < f.psalms.length; j++) {
                    try {
                        if (f.psalms[j].getSong().isPlaying()) {
                            f.psalms[j].getSong().stop();
                            f.psalms[j].getSong().prepareAsync();
                        }
                    } catch (NullPointerException npe) {
                    }
                }

                //Start song, update title, and change to pause button
                f.current_psalm = f.psalms[num];
                try {
                    f.play();
                } catch (NullPointerException npe) {

                }
                expand(main);
            }
        });
    }

    //Remove content from layout
    private void wipe(View main){
        ((LinearLayout) main).removeAllViews();
    }

    //When app is started, get favorites list from internal storage
    private void readFavList() {
        FileInputStream is;
        BufferedReader reader;
        File file = new File(this.getFilesDir(), "favorites_list.txt");
        try {
            is = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            while(line != null){
                favorites.add(Integer.parseInt(line));
                line = reader.readLine();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.notifications) {
            // inflate the layout of the popup window
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.activity_notifications, null);

            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            View view = findViewById(android.R.id.content).getRootView();
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            // dismiss the popup window when touched
            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popupWindow.dismiss();
                    return true;
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
