package com.example.psalms;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Psalm extends AppCompatActivity {
    private int number;
    private String name;
    private String title;
    private MediaPlayer song;
    private String lyrics;
    private String chinese;
    private String english;

    public Psalm(Context context, int num){
        setNumber(num);
        setTitle(context, num);
        setName(num);
        setSong(context, num);
        setLyrics(context, num);
        setChinese(context, num);
        setEnglish(context, num);
    }

    private void setNumber(int num){
        number = num;
    }

    private void setTitle(Context context, int num){
        title = readFile(context, "title_" + num + ".txt");
        if (title == null) {
            title = "P-" + String.format ("%03d", num);;
        }
    }

    private void setName(int num){
        name = "Psalm " + num;
    }

    private void setSong(Context context, int num){
        Resources res = context.getResources();
        try {
            int id = res.getIdentifier("p" + num, "raw", context.getPackageName());
            song = MediaPlayer.create(context, id);
        } catch (Exception e) {

        }
    }

    private void setLyrics(Context context, int num){
        lyrics = readFile(context, num + ".txt");
    }

    private void setChinese(Context context, int num){
        chinese = readFile(context, "ch_" + num + ".txt");
    }

    private void setEnglish(Context context, int num){
        english = readFile(context, "en_" + num + ".txt");
    }

    private String readFile(Context context, String filename){
        String content = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));
            String line = reader.readLine();
            while(line != null){
                content += line + "\n";
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            content = null;
        }

        return content;
    }

    public int getNumber(){
        return number;
    }

    public String getTitle2() {return title;}

    public String getName(){
        return name;
    }

    public MediaPlayer getSong(){
        return song;
    }

    public String getLyrics(){
        return lyrics;
    }

    public String getChinese(){
        return chinese;
    }

    public String getEnglish(){
        return english;
    }
}
