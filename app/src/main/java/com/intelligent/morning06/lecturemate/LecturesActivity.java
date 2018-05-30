package com.intelligent.morning06.lecturemate;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
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
    private static final int REQUEST_CODE_PERMISSIONS = 5322;
    private static boolean _checkedPermissions = false;


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
                MyApplication.setCurrentLectureName(lectures.get(i).getLectureName());
                MyApplication.setCurrentLecture(lectures.get(i).getId());
                checkPermissions();
            }
        });
    }

    void addLectureAction(){
        AlertDialog.Builder addLectureDialogBuilder = new AlertDialog.Builder(this);
        addLectureDialogBuilder.setTitle(getResources().getString(R.string.title_activity_Lectures_ButtonAddLecture));

        final EditText inputLecture = new EditText(this);
        inputLecture.setInputType(InputType.TYPE_CLASS_TEXT);
        addLectureDialogBuilder.setView(inputLecture);

        final Context appContext = getApplicationContext();

        addLectureDialogBuilder.setPositiveButton(getResources().getString(R.string.title_activity_Lectures_ButtonAdd), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                try {
                    DataModel.GetInstance().getLectureDataBase().AddLecture(inputLecture.getText().toString());
                    RefreshLectures();
                } catch (LectureAlreadyExistsException exception) {
                    ShowToast(getResources().getString(R.string.error_activity_lectures_lectureExists));
                } catch (IllegalArgumentException exception) {
                    ShowToast(getResources().getString(R.string.error_activity_lectures_lectureNameEmpty));
                }
            }
        });

        addLectureDialogBuilder.setNegativeButton(getResources().getString(R.string.title_activity_Lectures_ButtonCancel), new DialogInterface.OnClickListener() {
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

    void checkPermissions() {

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if(_checkedPermissions) {
                openCategoriesActivity(false);
            }
            else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_PERMISSIONS);
                _checkedPermissions = true;
            }
        }
        else {
            openCategoriesActivity(true);
        }
    }

    private void openCategoriesActivity(boolean hasPermissions) {
        MyApplication.setStoragePermissionGranted(hasPermissions);
        Intent categoryIntent = new Intent(LecturesActivity.this, TabCategoriesActivity.class);
        LecturesActivity.this.startActivity(categoryIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                } else {
                    Toast.makeText(getApplicationContext(), "Image feature will be disabled without permissions", Toast.LENGTH_LONG).show();
                    checkPermissions();
                }
                return;
            }
        }
    }
}
