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
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.intelligent.morning06.lecturemate.DataAccess.CategoriesActivity;
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
                openCategoriesActivity(lectureNameToShowCategories); //TODO pass Lecture
            }
        });
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

        final EditText inputLecture = new EditText(this);
        inputLecture.setInputType(InputType.TYPE_CLASS_TEXT);
        addLectureDialogBuilder.setView(inputLecture);

        final Context appContext = getApplicationContext();

        addLectureDialogBuilder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //save the string from inputLecture
                //
                try {
                    DataModel.GetInstance().getDataBase().AddLecture(inputLecture.getText().toString());
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
        lectures = DataModel.GetInstance().getDataBase().GetAllLectures();
        lectureListView.invalidateViews();
        listAdapter = new ArrayAdapter<Lecture>
                (this, android.R.layout.simple_list_item_1, lectures);
        lectureListView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    void openCategoriesActivity(String lectureName){
        Log.d("DEBUG", "Open Cetegories Activity " + lectureName); //TODO remove this line
        Intent categoryIntent = new Intent(LecturesActivity.this, CategoriesActivity.class);
        categoryIntent.putExtra("LectureName", lectureName);
        LecturesActivity.this.startActivity(categoryIntent);
    }
}
