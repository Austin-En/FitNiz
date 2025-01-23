package ca.mohawkcollege.enes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

public class PermissionsActivity extends AppCompatActivity {
ImageView backButton;
Switch notificationSwitch;
Switch cameraSwitch;
Switch photoLibrarySwitch;
Switch locationSwitch;
int checked = 0;

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
     * This activity displays the current permissions used in the project and displays it checked if the permissions are already given
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        backButton = findViewById(R.id.backButtonPermission);
        cameraSwitch = findViewById(R.id.cameraSwitch);
        cameraSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cameraSwitch.isChecked() == true) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
                    if(checked == -1){
                        cameraSwitch.setChecked(false);
                    }
                    else{
                        cameraSwitch.setChecked(true);
                        cameraSwitch.setClickable(false);
                    }
                    checked = 0;
                }
                else{

                }
            }
        });
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != -1){
            cameraSwitch.setChecked(true);
            cameraSwitch.setClickable(false);
        }
        else{
            cameraSwitch.setChecked(false);
        }
        photoLibrarySwitch = findViewById(R.id.photoLibrarySwitch);
        photoLibrarySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(photoLibrarySwitch.isChecked() == true) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    if(checked == -1){
                        photoLibrarySwitch.setChecked(false);
                    }
                    else{
                        photoLibrarySwitch.setChecked(true);
                        photoLibrarySwitch.setClickable(false);
                    }
                    checked = 0;
                }
                else{

                }
            }
        });
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != -1){
            photoLibrarySwitch.setChecked(true);
            photoLibrarySwitch.setClickable(false);
        }
        else{
            photoLibrarySwitch.setChecked(false);
        }

        backButton.setOnClickListener(this::sendToHomePage);
    }

    private void sendToHomePage(View view){
        startActivity(new Intent(PermissionsActivity.this, HomeActivity.class));
    }
}