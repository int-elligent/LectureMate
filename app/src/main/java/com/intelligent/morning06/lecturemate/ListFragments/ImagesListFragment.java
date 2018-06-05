package com.intelligent.morning06.lecturemate.ListFragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.intelligent.morning06.lecturemate.Adapters.ImagesAdapter;
import com.intelligent.morning06.lecturemate.DataAccess.DataBaseAccessImage;
import com.intelligent.morning06.lecturemate.DataAccess.DataModel;
import com.intelligent.morning06.lecturemate.DataAccess.Image;
import com.intelligent.morning06.lecturemate.ImageViewActivity;
import com.intelligent.morning06.lecturemate.ImagesCreateActivity;
import com.intelligent.morning06.lecturemate.Interfaces.ICategoryListFragment;
import com.intelligent.morning06.lecturemate.MyApplication;
import com.intelligent.morning06.lecturemate.R;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import static com.theartofdev.edmodo.cropper.CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE;
import static com.theartofdev.edmodo.cropper.CropImage.getCameraIntent;
import static com.theartofdev.edmodo.cropper.CropImage.getGalleryIntents;


public class ImagesListFragment extends Fragment implements ICategoryListFragment {
    ArrayList<Image> allImages = null;

    private String _currentCameraFileName;
    public static final int EMPTY_REQUEST_CODE = 24523;
    public static final int REQUEST_PERMISSION_CODE = 6147;
    private Uri _imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.images_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateImageList();
        ((ListView)getActivity().findViewById(R.id.images_list_listview)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //String lectureNameToShowCategories = lectures.get(i).getLectureName();
                openImages(i);
            }
        });
    }

    public void openImages(int selectedIndex) {
        int size = selectedIndex;
        for( int j = 0; j < size; j++)
            if(((ListView)getActivity().findViewById(R.id.images_list_listview)).getAdapter().getItemViewType(j) == 1) selectedIndex--;
        Intent intent = new Intent(getActivity(), ImageViewActivity.class);
        Bundle ImageBundle = new Bundle();
        ImageBundle.putSerializable("ALL_IMAGES", allImages);
        intent.putExtra("SERIALIZED_DATA", ImageBundle);
        intent.putExtra("SELECTED_INDEX", selectedIndex);
        //Log.e("ERROR",selectedIndex+"");
        startActivity(intent);
    }

    private void updateImageList() {
        Cursor imageCursor = DataModel.GetInstance().getImageDataBase().
                GetImageCursorForLecture(MyApplication.getCurrentLecture());

        allImages = new ArrayList<Image>();

        while(imageCursor.moveToNext()) {
            String title = imageCursor.
                    getString(imageCursor.getColumnIndex(DataBaseAccessImage.ImageTable.COLUMN_NAME_TITLE));
            String filePath = imageCursor.
                    getString(imageCursor.getColumnIndex(DataBaseAccessImage.ImageTable.COLUMN_NAME_FILEPATH));
            int id = imageCursor.
                    getInt(imageCursor.getColumnIndex(DataBaseAccessImage.ImageTable.COLUMN_NAME_ID));
            LocalDateTime creationDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(
                    imageCursor.getLong(imageCursor.getColumnIndex(DataBaseAccessImage.ImageTable.COLUMN_NAME_CREATIONDATE))),
                    TimeZone.getDefault().toZoneId());

            Image image = new Image(id, title, filePath, creationDate, MyApplication.getCurrentLecture());

            allImages.add(image);
        }

        ImagesAdapter imagesListAdapter = new ImagesAdapter(allImages, getActivity().getApplicationContext());

        ((ListView)this.getView().findViewById(R.id.images_list_listview)).setAdapter(imagesListAdapter);
    }

    private Intent createCameraIntent()
    {
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }

        _currentCameraFileName = "LectureMate_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")) + ".png";

        File imagesDirectory = new File(getActivity().getApplicationContext().getExternalFilesDir(null), "images");
        imagesDirectory.mkdirs();
        File imageFile = new File(imagesDirectory, _currentCameraFileName);
        try {
            imageFile.createNewFile();
        } catch(IOException exception) {
            return null;
        }
        Uri imageFileUri;
        try {
            imageFileUri = FileProvider.getUriForFile(
                    getActivity().getApplicationContext(),
                    getActivity().getPackageName() + ".provider",
                    imageFile);
        } catch(Exception exception) {
            return null;
        }
        Intent cameraIntent = getCameraIntent(getActivity().getApplicationContext(), imageFileUri);
        return cameraIntent;
    }

    private void startImagePicker()
    {
        List<Intent> intentsToInclude;
        PackageManager packageManager = getActivity().getPackageManager();

        intentsToInclude = getGalleryIntents(packageManager, Intent.ACTION_GET_CONTENT, false);
        Intent cameraIntent;
        if((cameraIntent = createCameraIntent()) != null)
            intentsToInclude.add(cameraIntent);

        Intent targetIntent = (intentsToInclude.isEmpty()) ? new Intent() : intentsToInclude.get(intentsToInclude.size() - 1);
        if(!intentsToInclude.isEmpty())
            intentsToInclude.remove(intentsToInclude.size() - 1);

        Intent chooserIntent = Intent.createChooser(targetIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentsToInclude.toArray(new Parcelable[intentsToInclude.size()]));
        startActivityForResult(chooserIntent, PICK_IMAGE_CHOOSER_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                boolean fromCamera = true;
                if (data != null && data.getData() != null) {
                    String action = data.getAction();
                    fromCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
                }

                Uri imageUri;
                if(fromCamera || data.getData() == null) {
                    File imagePath = new File(getActivity().getApplicationContext().getExternalFilesDir(null), "images/" + _currentCameraFileName);
                    imageUri = Uri.fromFile(imagePath);
                } else {
                    imageUri = data.getData();
                }

                Intent intent = new Intent(getActivity(), ImagesCreateActivity.class);
                intent.putExtra("IMAGE_URI", imageUri.toString());
                startActivityForResult(intent, EMPTY_REQUEST_CODE);
            }
        } else {
            updateImageList();
        }
    }

    @Override
    public void onFloatingActionButtonClicked() {
        startImagePicker();
    }
}
