package com.intelligent.morning06.lecturemate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.intelligent.morning06.lecturemate.R;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    private ListView categoriesListView;
    private List<String> categoriesList;
    public ArrayAdapter<String> categoryListAdapter;
    private String lectureNameToShow;
    private int lectureId;

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
                    openImagesListActivity();
                }
            }
        });

    }
    void openNotesListActivity(String lectureName, int lectureId){
        Intent singleCategoryIntent = new Intent(CategoriesActivity.this, NotesListActivity.class);
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
