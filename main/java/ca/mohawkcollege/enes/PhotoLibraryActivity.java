package ca.mohawkcollege.enes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;


public class PhotoLibraryActivity extends AppCompatActivity {
    ImageView backButtonPhotoLibrary;
    ListView photoLibraryListView;
    DBHelper DB;

    /**
     * This activity is used to display the photos for each month for the current and past year, the activity calls the database and gets two images
     * one being the oldest in the month and one newest in the month
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String[] monthsNames = getResources().getStringArray(R.array.Months);
        String[] displayMonths =  new String[24];
        setContentView(R.layout.activity_photo_library);
        backButtonPhotoLibrary = findViewById(R.id.backButtonPhotoLibrary);
        photoLibraryListView = findViewById(R.id.photoLibraryListView);
        backButtonPhotoLibrary.setOnClickListener(this::sendToMyProgressPage);
        DB = new DBHelper(this);
        Bitmap defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.startup_wallpaper);
        String[] months = new String[24];//24 months in two years
        for(int i = 0; i <= 23; i++){
           if(i >= 12) {//This code ensures to add a second of each month because we are displaying both current and last year
               months[i] = months[i - 12];
               String[] split = monthsNames[i - 12].split(" ");
               displayMonths[i] = split[0] + " " + String.valueOf(year);
           }
           else{
               months[i] = monthsNames[i];
               String[] split = monthsNames[i].split(" ");
               displayMonths[i] = split[0] + " " + String.valueOf(year - 1);
           }

        }
        ArrayList<byte[]> images = new ArrayList<>();
        ArrayList<Bitmap[]> imageBitmaps = new ArrayList<Bitmap[]>();
        int p = 0;
        for(String month : months){
            Bitmap[] imageBMPArray = new Bitmap[2];
            String[] separated = month.split(" ");
            String date = "";
            if(p >= 12) {//This code ensures to add a second of each month because we are displaying both current and last year
                date = String.valueOf(year + "-" + separated[1] + "-");
            }
            else{
                date = String.valueOf(year - 1 + "-" + separated[1] + "-");
            }
            images = DB.getCalendarDayRecordImageMonth(MainActivity.currentUserID, date);
            p++;
            if (images.size() == 1 && images.get(0) != null) {
                byte[] imageByteArray1 = images.get(0);
                Bitmap bmp = BitmapFactory.decodeByteArray(imageByteArray1, 0, imageByteArray1.length);
                imageBMPArray[0] = bmp;
                imageBMPArray[1] = defaultImage;
            }
            else if(images.size() == 2  && images.get(1) != null && images.get(0) != null){
                byte[] imageByteArray1 = images.get(0);
                Bitmap bmp = BitmapFactory.decodeByteArray(imageByteArray1, 0, imageByteArray1.length);
                imageBMPArray[0] = bmp;
                byte[] imageByteArray2 = images.get(1);
                Bitmap bmp2 = BitmapFactory.decodeByteArray(imageByteArray2, 0, imageByteArray2.length);
                imageBMPArray[1] = bmp2;
            }
            else {
                imageBMPArray[0] = defaultImage;
                imageBMPArray[1] = defaultImage;
            }
            imageBitmaps.add(imageBMPArray);
        }
        MyPhotoLibraryAdapter adapter = new MyPhotoLibraryAdapter(this, displayMonths, imageBitmaps);
        photoLibraryListView.setAdapter(adapter);
        photoLibraryListView.setDividerHeight(2);
}


    private void sendToMyProgressPage(View view){
        startActivity( new Intent(PhotoLibraryActivity.this, ProgressActivity.class));
    }
}
