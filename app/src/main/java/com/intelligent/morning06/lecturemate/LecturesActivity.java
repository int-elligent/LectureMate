package com.intelligent.morning06.lecturemate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.intelligent.morning06.lecturemate.DataAccess.DataModel;
import com.intelligent.morning06.lecturemate.DataAccess.Exceptions.LectureAlreadyExistsException;
import com.intelligent.morning06.lecturemate.DataAccess.Lecture;

import java.util.List;

public class LecturesActivity extends AppCompatActivity {

    private ListView lectureListView;
    private ArrayAdapter<Lecture> listAdapter;
    private List<Lecture> lectures;

    private AlertDialog lastDialog;


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

        lectureListView = (ListView) findViewById(R.id.lectureList);

        RefreshLectures();

        lectureListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String lectureNameToShowCategories = lectures.get(i).getLectureName();
                openCategoriesActivity(lectureNameToShowCategories, lectures.get(i).getId());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lectures, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    void addLectureAction(){
        AlertDialog.Builder addLectureDialogBuilder = new AlertDialog.Builder(this);
        addLectureDialogBuilder.setTitle("Add Lecture");

        final EditText inputLecture = new EditText(this);
        inputLecture.setInputType(InputType.TYPE_CLASS_TEXT);
        addLectureDialogBuilder.setView(inputLecture);

        final Context appContext = getApplicationContext();

        addLectureDialogBuilder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                try {
                    DataModel.GetInstance().getLectureDataBase().AddLecture(inputLecture.getText().toString());
                    RefreshLectures();
                } catch (LectureAlreadyExistsException exception) {
                    ShowToast("Lecture cannot be added, it already exists");
                } catch (IllegalArgumentException exception) {
                    ShowToast("Lecture name must not be empty.");
                }
            }
        });

        addLectureDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        lastDialog = addLectureDialogBuilder.show();

    }

    public AlertDialog getLastDialog() {
        return lastDialog;
    }

    private void ShowToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void RefreshLectures() {
        lectures = DataModel.GetInstance().getLectureDataBase().GetAllLectures();
        listAdapter = new ArrayAdapter<Lecture>
                (this, android.R.layout.simple_list_item_1, lectures);
        lectureListView.setAdapter(listAdapter);
        lectureListView.invalidateViews();
        listAdapter.notifyDataSetChanged();
    }

    void openCategoriesActivity(String lectureName, int lectureId){
    //void openCategoriesActivity(String lectureName){
        Intent categoryIntent = new Intent(LecturesActivity.this, CategoriesActivity.class);
        categoryIntent.putExtra("LectureName", lectureName);
        categoryIntent.putExtra("LectureId", lectureId);
        MyApplication.setCurrentLecture(lectureId);
        LecturesActivity.this.startActivity(categoryIntent);
    }
}
