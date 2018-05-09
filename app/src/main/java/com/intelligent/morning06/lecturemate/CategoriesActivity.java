package com.intelligent.morning06.lecturemate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        categoriesList.add(getResources().getString(R.string.title_activity_DatesList));
        categoriesList.add(getResources().getString(R.string.title_activity_ImagesList));
        categoriesList.add(getResources().getString(R.string.title_activity_NotesList));
        categoriesList.add(getResources().getString(R.string.title_activity_VideosList));


        categoryListAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, categoriesList);

        categoriesListView.setAdapter(categoryListAdapter);

        Intent intent = getIntent();
        lectureNameToShow = intent.getStringExtra(getResources().getString(R.string.intent_extra_lectureName));
        lectureId = intent.getIntExtra(getResources().getString(R.string.intent_extra_lectureId), 0);
        setTitle(lectureNameToShow);

        categoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String categoryName = categoriesList.get(i);
                if (categoryName.equals(getResources().getString(R.string.title_activity_NotesList))){
                    openNotesListActivity(lectureNameToShow, lectureId);
                } else if (categoryName.equals(getResources().getString(R.string.title_activity_ImagesList))) {
                    openImagesListActivity();
                }
            }
        });

    }
    void openNotesListActivity(String lectureName, int lectureId){
        Intent singleCategoryIntent = new Intent(CategoriesActivity.this, NotesListActivity.class);
        singleCategoryIntent.putExtra(getResources().getString(R.string.intent_extra_lectureName), lectureName);
        singleCategoryIntent.putExtra(getResources().getString(R.string.intent_extra_lectureId), lectureId);
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
