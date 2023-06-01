package ca.mohawkcollege.enes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DBHelper DB;
    EditText usernameInput;
    EditText passwordInput;
    public static int currentUserID;

    /**
     * The main activity is just the login page where the user is able to enter in a username and password and is later checked inside the database to verify
     * if the entered information matches with any account on the database
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button login = findViewById(R.id.loginButton);
        usernameInput = findViewById(R.id.usernameText);
        passwordInput = findViewById(R.id.passwordText);
        login.setOnClickListener(this::sendToHomePage);
        DB = new DBHelper(this);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void sendToHomePage(View view){
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        if(username.equals("") || password.equals("")){
            Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
        }
        else if(DB.checkUsernamePassword(username,password)){
            Toast.makeText(MainActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
            currentUserID = DB.getUserID(username, password);
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
        }

    }
}