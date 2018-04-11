package com.intelligent.morning06.lecturemate;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class LecturesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectures);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLectureAction();
            }
        });

        ListView lectureListView = (ListView) findViewById(R.id.lectureList);

        lectureListView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                addCategoriesList();
            }
        });

        List<String> lectureList = new ArrayList<String>();

        //lectureList.remove(2);

        lectureList.add("Lecture 1");
        lectureList.add("Lecture 2");
        lectureList.add("Lecture 3");
        lectureList.add("Lecture 4");
        lectureList.add("Lecture 1");
        lectureList.add("Lecture 2");
        lectureList.add("Lecture 3");
        lectureList.add("Lecture 4");
        lectureList.add("Lecture 1");
        lectureList.add("Lecture 2");
        lectureList.add("Lecture 3");
        lectureList.add("Lecture 4");
        lectureList.add("Lecture 1");
        lectureList.add("Lecture 2");
        lectureList.add("Lecture 3");
        lectureList.add("Lecture 4");
        lectureList.add("Lecture 1");
        lectureList.add("Lecture 2");
        lectureList.add("Lecture 3");
        lectureList.add("Lecture 4");
        lectureList.add("Lecture 1");
        lectureList.add("Lecture 2");
        lectureList.add("Lecture 3");
        lectureList.add("Lecture 4");

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, lectureList);

        lectureListView.setAdapter(listAdapter);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lectures, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    void addLectureAction(){
        AlertDialog.Builder addLectureDialogBuilder = new AlertDialog.Builder(this);
        addLectureDialogBuilder.setTitle("Add Lecture");

        EditText inputLecture = new EditText(this);
        inputLecture.setInputType(InputType.TYPE_CLASS_TEXT);
        addLectureDialogBuilder.setView(inputLecture);

        addLectureDialogBuilder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //save the string from inputLecture
                //
            }
        });

        addLectureDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        addLectureDialogBuilder.show();

    }

    void addCategoriesList(){
        List<String> categoriesList = new ArrayList<String>();

        categoriesList.add("Categorie1");

        ListView categoriesListView = (ListView) findViewById(R.id.categoriesList);

        ArrayAdapter<String> clistAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, categoriesList);

        categoriesListView.setAdapter(clistAdapter);
    }
}
