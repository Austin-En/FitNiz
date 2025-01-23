package ca.mohawkcollege.enes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity {
TextView accountDate;
TextView accountUsername;
TextView accountEmail;
TextView accountHeight;
ImageView backButtonAccount;
DBHelper DB;

    /**
     * This displays the users information to the user getting the information via a Database call
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        accountDate = findViewById(R.id.accountDate);
        accountUsername = findViewById(R.id.accountUsername);
        accountEmail = findViewById(R.id.accountEmail);
        accountHeight = findViewById(R.id.accountHeight);
        backButtonAccount = findViewById(R.id.backButtonAccount);
        backButtonAccount.setOnClickListener(this::sendToHomePage);
        DB = new DBHelper(this);
        if(MainActivity.currentUserID != -1){
            ArrayList<String> userInfoList =  DB.getUserInfo(MainActivity.currentUserID);
            for(String user : userInfoList){
                String[] split = user.split("/-/");
                accountUsername.setText(split[0]);
                accountEmail.setText(split[2]);
                accountDate.setText(split[3]);
                accountHeight.setText(split[4]);
            }
        }
        else{
            Toast.makeText(AccountActivity.this, "No ID found!", Toast.LENGTH_SHORT).show();//Displays to the user that there is not an ID for the current user
            startActivity(new Intent(AccountActivity.this, HomeActivity.class));
        }
    }

    private void sendToHomePage(View view){
        startActivity(new Intent(AccountActivity.this, HomeActivity.class));
    }
}