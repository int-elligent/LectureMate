package com.intelligent.morning06.lecturemate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.intelligent.morning06.lecturemate.R;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    private ListView categoriesListView;
    private List<String> categoriesList;
    public ArrayAdapter<String> categoryListAdapter;
    private String lectureNameToShow;
    private int lectureId;

    private static final int REQUEST_CODE_PERMISSIONS = 53614;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        categoriesListView = (ListView) findViewById(R.id.categoryList);

        categoriesList = new ArrayList<>();

        categoriesList.add("Dates");
        categoriesList.add("Images");
        categoriesList.add("Notes");
        categoriesList.add("Videos");


        categoryListAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, categoriesList);

        categoriesListView.setAdapter(categoryListAdapter);

        Intent intent = getIntent();
        lectureNameToShow = intent.getStringExtra("LectureName");
        lectureId = intent.getIntExtra("LectureId", 0);
        setTitle(lectureNameToShow);

        categoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String categoryName = categoriesList.get(i);
                if (categoryName.equals("Notes")){
                    openNotesListActivity(lectureNameToShow, lectureId);
                } else if (categoryName.equals("Images")) {

                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS);
                        return;
                    }

                    openImagesListActivity();
                } else if (categoryName.equals("Dates")){
                    openDatesListActivity(lectureNameToShow, lectureId);
                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImagesListActivity();
                } else {
                    Toast.makeText(this, "Cannot show images without permission", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    void openNotesListActivity(String lectureName, int lectureId){
        Intent singleCategoryIntent = new Intent(CategoriesActivity.this, NotesListActivity.class);
        singleCategoryIntent.putExtra("LectureName", lectureName);
        singleCategoryIntent.putExtra("LectureId", lectureId);
        CategoriesActivity.this.startActivity(singleCategoryIntent);
    }

    void openDatesListActivity(String lectureName, int lectureId){
        Intent singleCategoryIntent = new Intent(CategoriesActivity.this, DatesListActivity.class);
        singleCategoryIntent.putExtra("LectureName", lectureName);
        singleCategoryIntent.putExtra("LectureId", lectureId);
        CategoriesActivity.this.startActivity(singleCategoryIntent);
    }

    void openImagesListActivity() {
        Intent activityIntent = new Intent(CategoriesActivity.this, ImagesListActivity.class);
        CategoriesActivity.this.startActivity(activityIntent);
    }

    public List<String> getCategoriesList(){
        return categoriesList;
    }
}
