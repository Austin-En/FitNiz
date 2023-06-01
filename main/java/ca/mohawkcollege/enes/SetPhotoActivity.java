package ca.mohawkcollege.enes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class SetPhotoActivity extends AppCompatActivity {
ImageView backButtonSetPhoto;
ImageView setUserImage;
Button setPhotoButton;
Button photoButton;
Button photoLibraryButton;
DBHelper DB;
    int SELECT_PICTURE = 200;
    String[] separatedData;
    String day;
    String month;
    String monthNumber;
    String year;
    String date;
    byte[] image;
    int checked = 0;

    Boolean photoChanged = false;

    /**
     * This method is used to verify after the user gives a response to the permissions access
     * @param permissions The permission that is been given
     * @param grantResults The result of the user answer
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (grantResults[0]) {
            case -1://This case is for when permission is denied
                checked = -1;
            default:
                checked = 0;
        }
    }

    /**
     * This activity is used to set a photo to a calendar day, this allows the user to access an image from there phone or take one to be saved to the database
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_photo);
        DB = new DBHelper(this);
        Intent intent = getIntent();
        day = intent.getStringExtra("day");
        String dayText = day;
        if(dayText.length() != 2){
            dayText = "0" + dayText;
        }
        separatedData = intent.getStringArrayExtra("dbinfo");
        monthNumber = intent.getStringExtra("monthnumber");
        month = intent.getStringExtra("month");
        year = intent.getStringExtra("year");
        image = intent.getByteArrayExtra("image");
        date = year + "-" + monthNumber + "-" + dayText;
        backButtonSetPhoto = findViewById(R.id.backButtonSetPhoto);
        setUserImage = findViewById(R.id.setUserImage);
        if(image != null){
            Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
            setUserImage.setImageBitmap(bmp);
        }
        setPhotoButton = findViewById(R.id.setPhotoButton);
        setPhotoButton.setOnClickListener(this::sendToViewCalendarDayPageWithSave);
        photoButton = findViewById(R.id.photoButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(SetPhotoActivity.this, Manifest.permission.CAMERA) != -1){
                    imageTaker();
                }
                else{
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
                }
            }
        });
        photoLibraryButton = findViewById(R.id.photoLibraryButton);
        backButtonSetPhoto.setOnClickListener(this::sendToViewCalendarDayPage);
        photoLibraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(SetPhotoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != -1) {
                    imageChooser();
                }
                else{
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }
        });
    }


    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    void imageTaker() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, 102);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            photoChanged = true;
            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    setUserImage.setImageURI(selectedImageUri);
                }
            }
            if(requestCode == 102){
                Bitmap image = (Bitmap) data.getExtras().get("data");
                setUserImage.setImageBitmap(image);
            }
        }
    }

    @Override
    public void onBackPressed() {moveTaskToBack(true);}

    /**
     * This method ensures to return back to the page with the previous month information to view the saved image
     * @param view
     */
    private void sendToViewCalendarDayPageWithSave(View view) {
        Drawable drawable = setUserImage.getDrawable();
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        image = stream.toByteArray();
        Boolean workStatus = false;
        if(separatedData[4].equals("1")){
            workStatus = true;
        }
        if(photoChanged) {
            DB.updateCalendarRecord(Integer.parseInt(separatedData[0]), MainActivity.currentUserID, date, image, workStatus, Double.parseDouble(separatedData[5]));
        }
        Intent intent = new Intent(SetPhotoActivity.this, ViewCalendarDayActivity.class);
        intent.putExtra("day", day);
        intent.putExtra("monthnumber", monthNumber);
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        startActivity(intent);
    }

    private void sendToViewCalendarDayPage(View view){
        Intent intent = new Intent(SetPhotoActivity.this, ViewCalendarDayActivity.class);
        intent.putExtra("day", day);
        intent.putExtra("monthnumber", monthNumber);
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        startActivity(intent);
    }
}