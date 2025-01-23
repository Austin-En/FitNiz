package ca.mohawkcollege.enes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class DBHelper extends SQLiteOpenHelper {

    final static String tag = "==DBHelper Class==";

    public static final String DBNAME = "FitNiz_DATABASE";
    public static final String USERS_TABLE = "users";
    public static final String METRICS_TABLE = "metrics";
    public static final String CALENDAR_TABLE = "calendar";
    public static final String WORKOUT_TABLE = "workout";
    public static final String EXERCISE_TABLE = "exercise";

    public static final String RECORDS_TABLE = "records";



    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    /**
     * This creates the tables in the database but most importantly creates a usable user account as well as some default metrics
     * @param MyDB
     */
    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("CREATE TABLE " + USERS_TABLE + "(userID INTEGER PRIMARY KEY, username TEXT, password TEXT, email TEXT, date TEXT, height TEXT)");
        MyDB.execSQL("CREATE TABLE " + METRICS_TABLE + "(metricID INTEGER PRIMARY KEY, userID INTEGER, name TEXT, unitofmeasurement TEXT)");
        MyDB.execSQL("CREATE TABLE " + RECORDS_TABLE + "(recordID INTEGER PRIMARY KEY, userID INTEGER, date DATE, name TEXT, unitofmeasurement TEXT, value TEXT)");
        MyDB.execSQL("CREATE TABLE " + CALENDAR_TABLE + "(calendarID INTEGER PRIMARY KEY, userID INTEGER, date DATE, image BLOB, workoutstatus BOOLEAN, length DOUBLE)");
        MyDB.execSQL("CREATE TABLE " + WORKOUT_TABLE + "(planID INTEGER PRIMARY KEY, userID INTEGER, name TEXT, length INTEGER)");
        MyDB.execSQL("CREATE TABLE " + EXERCISE_TABLE + "(exerciseID INTEGER PRIMARY KEY, planID INTEGER, userID INTEGER, name TEXT, sets INTEGER, reps INTEGER, weights INTEGER)");
       //Creating default user values
        ContentValues usersValues = new ContentValues();
        usersValues.put("username", "TestAccount55");
        usersValues.put("password", "Password!1");
        usersValues.put("email", "test909@hotmail.com");
        usersValues.put("date", String.valueOf(LocalDate.now()));
        usersValues.put("height", "176.784");
        //Inserting Default user to test with
        MyDB.insert(USERS_TABLE, null, usersValues);
        //Creating default metric
        ContentValues defaultMetrics = new ContentValues();
        defaultMetrics.put("userID", 1);
        defaultMetrics.put("name", "Workout Time");
        defaultMetrics.put("unitofmeasurement", "TIME");
        //Inserting default metric
        long test = MyDB.insert(METRICS_TABLE, null, defaultMetrics);
        //Creating default metric
        ContentValues defaultMetrics2 = new ContentValues();
        defaultMetrics2.put("userID", 1);
        defaultMetrics2.put("name", "BMI");
        defaultMetrics2.put("unitofmeasurement", "M²");
        //Inserting default metric
        test = MyDB.insert(METRICS_TABLE, null, defaultMetrics2);
        //Creating default metric
        ContentValues defaultMetrics3 = new ContentValues();
        defaultMetrics3.put("userID", 1);
        defaultMetrics3.put("name", "Waist");
        defaultMetrics3.put("unitofmeasurement", "CM");
        //Inserting default metric
        test = MyDB.insert(METRICS_TABLE, null, defaultMetrics3);
        //Creating default metric
        ContentValues defaultMetrics4 = new ContentValues();
        defaultMetrics4.put("userID", 1);
        defaultMetrics4.put("name", "Weight");
        defaultMetrics4.put("unitofmeasurement", "LB");
        //Inserting default metric
        test = MyDB.insert(METRICS_TABLE, null, defaultMetrics4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
       MyDB.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
       MyDB.execSQL("DROP TABLE IF EXISTS " + METRICS_TABLE);
       MyDB.execSQL("DROP TABLE IF EXISTS " + RECORDS_TABLE);
       MyDB.execSQL("DROP TABLE IF EXISTS " + CALENDAR_TABLE);
       MyDB.execSQL("DROP TABLE IF EXISTS " + WORKOUT_TABLE);
       MyDB.execSQL("DROP TABLE IF EXISTS " + EXERCISE_TABLE);
    }

    public Boolean insertCalendar(int userID, String date, byte[] image, Boolean workoutstatus, Double length)  {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues userValues = new ContentValues();
        userValues.put("userID", userID);
        userValues.put("date", date);
        userValues.put("image", image);
        userValues.put("workoutstatus", workoutstatus);
        userValues.put("length", length);
        long result = DB.insert(CALENDAR_TABLE, null, userValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkCalendarEntry (int userID, String date) {
        SQLiteDatabase DB = this.getReadableDatabase();
        Boolean fieldToAdd = false;
        Cursor cursor = null;
        if(userID != -1 || !date.equals("")) {
            cursor = DB.rawQuery("Select * from " + CALENDAR_TABLE + " where userID = ? and date = ?", new String[]{String.valueOf(userID), date});
            while (cursor.moveToNext()) {
                fieldToAdd = true;
            }
            cursor.close();
        }
        return fieldToAdd;
    }

    public Boolean updateCalendarRecord(int calendarID, int userID, String date, byte[] image, Boolean workoutstatus, Double length)  {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues userValues = new ContentValues();
        userValues.put("calendarID", calendarID);
        userValues.put("userID", userID);
        userValues.put("date", date);
        userValues.put("image", image);
        userValues.put("workoutstatus", workoutstatus);
        userValues.put("length", length);
        long result = DB.update(CALENDAR_TABLE, userValues, "calendarID=?",  new String[]{String.valueOf(calendarID)});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public byte[] getCalendarDayRecordImage(int userID, String date) {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = null;
        byte[] image = null;
        if(userID != -1) {
            cursor = DB.rawQuery("Select * from " + CALENDAR_TABLE + " where userID = ? and date = ?", new String[]{String.valueOf(userID), date});
            while (cursor.moveToNext()) {
                image =  cursor.getBlob(3);
            }
            cursor.close();
        }
        return image;
    }

    public ArrayList<byte[]> getCalendarDayRecordImageMonth(int userID, String date) {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = null;
        ArrayList<byte[]> images = new ArrayList<>();
        ArrayList<Integer> dates = new ArrayList<>();
        ArrayList<Integer> finalDates = new ArrayList<>();//first finalDate is the early image, second finalDate is the latest
        ArrayList<byte[]> finalImages = new ArrayList<>();//first finalImage is the early image, second finalImage is the latest
        if (userID != -1) {
            cursor = DB.rawQuery("Select * from " + CALENDAR_TABLE + " where userID = ? and date LIKE ?", new String[]{String.valueOf(userID), "%" + date + "%"});
            while (cursor.moveToNext()) {
                if (cursor.getBlob(3) != null) {
                    images.add(cursor.getBlob(3));
                    String[] split = cursor.getString(2).split("-");
                    dates.add(Integer.parseInt(split[2]));
                }
            }
            cursor.close();
            if(dates.size() == 2){
                if(dates.get(0) < dates.get(1)){
                    finalImages = images;
                }
                else{
                    Collections.swap(images, 0 , 1);
                    finalImages = images;
                }
            }
            else if(dates.size() == 1){
                finalImages = images;
            }
            else if (dates.size() != 0) {
                if(dates.get(0) > dates.get(1)){
                    finalImages.add(images.get(1));
                    finalImages.add(images.get(0));
                    finalDates.add(dates.get(1));
                    finalDates.add(dates.get(0));
                }
                else{
                    finalImages.add(0,images.get(0));
                    finalImages.add(1,images.get(1));
                    finalDates.add(0,dates.get(0));
                    finalDates.add(1,dates.get(1));
                }
                for (int currentDate : dates) {
                    int index = dates.indexOf(currentDate);
                    if(currentDate < finalDates.get(0)){
                        finalDates.set(0, currentDate);
                        finalImages.set(0, images.get(index));
                    }
                    else if(currentDate > finalDates.get(1)){
                        finalDates.set(1, currentDate);
                        finalImages.set(1, images.get(index));
                    }
                }

            }
        }
        return finalImages;
    }

    public String getCalendarDayRecord(int userID, String date) {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = null;
        String fields = null;
        if(userID != -1) {
            cursor = DB.rawQuery("Select * from " + CALENDAR_TABLE + " where userID = ? and date = ?", new String[]{String.valueOf(userID), date});
            while (cursor.moveToNext()) {
                fields =  cursor.getString(0) + "/-/" + cursor.getString(1) + "/-/" + cursor.getString(2) + "/-/" + cursor.getBlob(3) + "/-/" + cursor.getInt(4) + "/-/" + cursor.getDouble(5);
            }
            cursor.close();
        }
        return fields;
    }



    public Boolean insertRecord(int userID, String date, String name, String unit, String value)  {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues metricValues = new ContentValues();
        metricValues.put("userID", userID);
        String[] separated = date.split("-");
        metricValues.put("date", date);
        metricValues.put("name", name);
        metricValues.put("unitofmeasurement", unit);
        metricValues.put("value", value);
        long result = DB.insert(RECORDS_TABLE, null, metricValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<String> getRecord(int userID, String date, String name)  {
        SQLiteDatabase DB = this.getWritableDatabase();
        String[] separated = date.split("-");
        Cursor cursor = null;
        ArrayList<String> data = new ArrayList<>();
        if(userID != -1) {
            cursor = DB.rawQuery("Select * from " + RECORDS_TABLE + " where userID = ? and name = ? and date LIKE ?", new String[]{String.valueOf(userID), name, "%" + date + "%"});
            String fieldToAdd = null;
            while (cursor.moveToNext()) {
                fieldToAdd = cursor.getString(2) + "/-/" + cursor.getString(5);
                data.add(fieldToAdd);
            }
            cursor.close();
        }
        return data;
    }

    public Boolean insertMetric(int userID, String name, String unit)  {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues metricValues = new ContentValues();
        metricValues.put("userID", userID);
        metricValues.put("name", name);
        metricValues.put("unitofmeasurement", unit);
        long result = DB.insert(METRICS_TABLE, null, metricValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public String getMetricUnit(int userID, String name) {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = null;
        String data = "";
        if(userID != -1) {
            cursor = DB.rawQuery("Select * from " + METRICS_TABLE + " where userID = ? and name = ?", new String[]{String.valueOf(userID), name});
            while (cursor.moveToNext()) {
                data = cursor.getString(3);
                break;
            }
            cursor.close();
        }
        return data;
    }

    public ArrayList<String> getMetric(int userID) {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = null;
        ArrayList<String> data = new ArrayList<String>();
        if(userID != -1) {
            cursor = DB.rawQuery("Select * from " + METRICS_TABLE + " where userID = ?", new String[]{String.valueOf(userID)});
            String fieldToAdd = null;
            while (cursor.moveToNext()) {
                fieldToAdd = cursor.getString(2) + "/-/" + cursor.getString(3);
                data.add(fieldToAdd);
            }
            cursor.close();
        }
        return data;
    }

    public boolean checkMatchMetric(int userID, String name, String unit) {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = null;
        boolean data = false;
        if(userID != -1) {
            cursor = DB.rawQuery("Select * from " + METRICS_TABLE + " where userID = ? and name = ? and unitofmeasurement = ?", new String[]{String.valueOf(userID), name, unit});
            String fieldToAdd = null;
            while (cursor.moveToNext()) {
                data = true;
            }
            cursor.close();
        }
        return data;
    }
    public Boolean deleteExercise(int planID) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues usersValues = new ContentValues();
        usersValues.put("planID", planID);
        long result = DB.delete(EXERCISE_TABLE, "planID=?", new String[]{String.valueOf(planID)});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean updateExercise(int exerciseID, int planID, int userID, String name, int sets, int reps, int weights) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues usersValues = new ContentValues();
        usersValues.put("exerciseID", exerciseID);
        usersValues.put("planID", planID);
        usersValues.put("userID", userID);
        usersValues.put("name", name);
        usersValues.put("sets", sets);
        usersValues.put("reps", reps);
        usersValues.put("weights", weights);
        long result = DB.update(EXERCISE_TABLE, usersValues, "exerciseID=?",  new String[]{String.valueOf(exerciseID)});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean insertExercise(int planID, int userID, String name, int sets, int reps, int weights)  {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues exerciseValues = new ContentValues();
        exerciseValues.put("planID", planID);
        exerciseValues.put("userID", userID);
        exerciseValues.put("name", name);
        exerciseValues.put("sets", sets);
        exerciseValues.put("reps", reps);
        exerciseValues.put("weights", weights);
        long result = DB.insert(EXERCISE_TABLE, null, exerciseValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<String> getExerciseRecords(int planID, int userID) {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = null;
        ArrayList<String> data = new ArrayList<String>();
        if(planID != -1 && userID != -1) {
            cursor = DB.rawQuery("Select * from " + EXERCISE_TABLE + " where planID = ? and userID = ?", new String[]{String.valueOf(planID), String.valueOf(userID)});
            String fieldToAdd = null;
            while (cursor.moveToNext()) {
                fieldToAdd = cursor.getString(0) + "/-/" + cursor.getString(1) + "/-/" + cursor.getString(3) + "/-/" + cursor.getInt(4) + "/-/" + cursor.getInt(5) + "/-/" + cursor.getInt(6);
                data.add(fieldToAdd);
            }
            cursor.close();
        }
        return data;
    }

    public Boolean insertWorkout(int userID, String name, int length)  {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues workoutValues = new ContentValues();
        workoutValues.put("userID", userID);
        workoutValues.put("name", name);
        workoutValues.put("length", length);
        long result = DB.insert(WORKOUT_TABLE, null, workoutValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Boolean updateWorkout(int planID, int userID, String name, int length) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues usersValues = new ContentValues();
        usersValues.put("planID", planID);
        usersValues.put("userID", userID);
        usersValues.put("name", name);
        usersValues.put("length", length);
        long result = DB.update(WORKOUT_TABLE, usersValues, "planID=?",  new String[]{String.valueOf(planID)});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<String> getWorkoutRecords(int userID) {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = null;
        ArrayList<String> data = new ArrayList<String>();
        if(userID != -1) {
            cursor = DB.rawQuery("Select * from " + WORKOUT_TABLE + " where userID = ?", new String[]{String.valueOf(userID)});
            String fieldToAdd = null;
            while (cursor.moveToNext()) {
                fieldToAdd = cursor.getInt(0) + "/-/" + cursor.getString(2) + "/-/" + cursor.getInt(3);
                data.add(fieldToAdd);
            }
            cursor.close();
        }
        return data;
    }

    public int getUserID (String username, String password) {
        SQLiteDatabase DB = this.getReadableDatabase();
        int fieldToAdd = -1;
        Cursor cursor = null;
        if(!username.equals("") || !password.equals("")) {
            cursor = DB.rawQuery("Select userID from " + USERS_TABLE + " where username = ? and password = ?", new String[]{username, password});
            while (cursor.moveToNext()) {
                fieldToAdd = cursor.getInt(0);
            }
            cursor.close();
        }
        return fieldToAdd;
    }

    public Boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from " + USERS_TABLE +" where username = ? and password = ?", new String[] {username, password});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<String> getUserInfo(int userID) {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = null;
        ArrayList<String> data = new ArrayList<String>();
        if(userID != -1) {
            cursor = DB.rawQuery("Select * from " + USERS_TABLE + " where userID = ?", new String[]{String.valueOf(userID)});
            String fieldToAdd = null;
            while (cursor.moveToNext()) {
                fieldToAdd = cursor.getString(1) + "/-/" + cursor.getString(2) + "/-/" + cursor.getString(3) + "/-/" + cursor.getString(4) + "/-/" + cursor.getString(5);
                data.add(fieldToAdd);
            }
            cursor.close();
        }
        return data;
    }

    public int getNextPlanID() {
        SQLiteDatabase DB = this.getReadableDatabase();
        String query = "SELECT MAX(planID) AS max_id FROM " + WORKOUT_TABLE;
        Cursor cursor = DB.rawQuery(query, null);
        int id = 0;
        if (cursor.moveToFirst())
        {
            do
            {
                id = cursor.getInt(0);
            } while(cursor.moveToNext());
        }
        return id;
    }
}
