package com.cmps121.rideplanner;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileManager {
// Source - https://www.youtube.com/watch?v=YmRPIGFftp0&feature=youtu.be&t=864s
/*
https://stackoverflow.com/questions/30417810/reading-from-a-text-file-in-android-studio-java
https://stackoverflow.com/questions/14376807/how-to-read-write-string-from-a-file-in-android

Some of my 12B Code logic was used here as well, Program 4
 */
    // Android documetation as well

    public static final String filepath = "data.";
    private static ArrayList<String> photoList;

    //names of my methods and logic happened to be the same as the tutorial. Not much I can do
    public static void writeFile(ArrayList<String> items, Context context, String personName){
        try {
            FileOutputStream input = context.openFileOutput(filepath, Context.MODE_PRIVATE);
            ObjectOutputStream output = new ObjectOutputStream(input);
            output.writeObject(items);
            output.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    //reads the data from the file
    public static ArrayList<String> readFile(Context context){
        photoList = null;
        try {
            FileInputStream input = context.openFileInput(filepath);
            ObjectInputStream output = new ObjectInputStream(input);
            photoList = (ArrayList<String>) output.readObject();
        } catch (FileNotFoundException ex) {
            photoList = new ArrayList<>();
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return photoList;

    }



}