package com.example.psalms;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class CommonFunctions {

    Psalm[] psalms;
    ArrayList<Integer> favorites;
    Psalm current_psalm;
    Boolean repeat;
    private ImageButton play_pause;
    private TextView title_bar;
    private ImageButton star;
    private Context context;
    ImageButton repeat_btn;

    CommonFunctions(Context c, ImageButton play_pause_btn, TextView title_bar_text, ImageButton star_btn){
        context = c;
        play_pause = play_pause_btn;
        title_bar = title_bar_text;
        star = star_btn;
        favorites = ScrollingActivity.favorites;
    }

    void play(){
        try {
            current_psalm.getSong().start();
            pauseBtn();
        } catch (NullPointerException npe) {
            playBtn();
        }
        updateTitleBar();
    }

    //Play and pause music
    void playStatus(){
        try {
            if (current_psalm.getSong().isPlaying()) {
                current_psalm.getSong().pause();
                playBtn();
            } else {
                play();
            }
        } catch (NullPointerException npe) {
            playBtn();
        }
    }

    //Change button to play button
    void playBtn(){
        play_pause.setImageResource(R.drawable.play);
    }

    //Change button to pause button
    void pauseBtn(){
        play_pause.setImageResource(R.drawable.pause);
    }

    //Change title to song playing
    void updateTitleBar(){
        title_bar.setText(current_psalm.getName());
        starStatus(); //Check if song is favorited
    }

    void finish() {
        current_psalm.getSong().start();
        if (repeat) {
            pauseBtn();
        } else {
            current_psalm.getSong().pause();
            playBtn();
        }
        repeat = !repeat;
    }

    //Change favorite status
    void favorite(){
        int idx = favorites.indexOf(current_psalm.getNumber());
        if (idx == -1) {
            star.setImageResource(R.drawable.full_star);
            favorites.add(current_psalm.getNumber());
        } else {
            star.setImageResource(R.drawable.empty_star);
            favorites.remove(idx);
        }

        //Save new list to internal storage
        updateFavList();
    }

    //Display empty or full star
    private void starStatus (){
        if (favorites.contains(current_psalm.getNumber())){
            star.setImageResource(R.drawable.full_star);
        } else {
            star.setImageResource(R.drawable.empty_star);
        }
    }

    //Given psalm number, returns index in psalms
    //Raises exception if number doesn't exist
    int getPsalmIdx(int psalm) throws IOException {
        for (int i = 0; i < psalm && i < psalms.length; i++){
            if (psalms[i].getNumber() == psalm){
                return i;
            }
        }
        throw new IOException();
    }

    //Save favorites list to internal storage
    private void updateFavList() {
        File file = new File(context.getFilesDir() + "/favorites_list.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileWriter fw = new FileWriter(file);
            PrintWriter pw = new PrintWriter(fw);
            for (int i = 0; i < favorites.size(); i++){
                pw.println(favorites.get(i));
            }
            pw.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
