package com.intelligent.morning06.lecturemate;

import android.app.ListActivity;
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
                    openListActivity(lectureNameToShow, lectureId, NotesListActivity.class);
                }else if (categoryName.equals("Dates")){
                    openListActivity(lectureNameToShow, lectureId, DatesListActivity.class);
                }
            }
        });
    }

    void openListActivity(String lectureName, int lectureId, Class activity){
        Intent singleCategoryIntent = new Intent(CategoriesActivity.this, activity);
        singleCategoryIntent.putExtra("LectureName", lectureName);
        singleCategoryIntent.putExtra("LectureId", lectureId);
        CategoriesActivity.this.startActivity(singleCategoryIntent);
    }

    public List<String> getCategoriesList(){
        return categoriesList;
    }
}