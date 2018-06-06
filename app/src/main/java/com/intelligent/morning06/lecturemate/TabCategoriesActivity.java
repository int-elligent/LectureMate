package com.intelligent.morning06.lecturemate;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import com.intelligent.morning06.lecturemate.Interfaces.ICategoryListFragment;
import com.intelligent.morning06.lecturemate.ListFragments.DatesListFragment;
import com.intelligent.morning06.lecturemate.ListFragments.ImagesListFragment;
import com.intelligent.morning06.lecturemate.ListFragments.NotesListFragment;

public class TabCategoriesActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private int _currentTabIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_categories_toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        if(!MyApplication.getStoragePermissionGranted()) {
            tabLayout.removeTabAt(1);
        }

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                _currentTabIndex = tab.getPosition();

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_categories_activity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment page = getSupportFragmentManager().
                        findFragmentByTag("android:switcher:" + R.id.container + ":" + mViewPager.getCurrentItem());

                ((ICategoryListFragment)page).onFloatingActionButtonClicked();
            }
        });
        getSupportActionBar().setTitle(MyApplication.getCurrentLectureName());
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            boolean permissions = MyApplication.getStoragePermissionGranted();

            if(position == 0) {
                return new NotesListFragment();
            } else if (position == 1 && permissions) {
                return new ImagesListFragment();
            } else {
                return new DatesListFragment();
            }
        }

        @Override
        public int getCount() {
            return MyApplication.getStoragePermissionGranted() ? 3 : 2;
        }
    }
}
