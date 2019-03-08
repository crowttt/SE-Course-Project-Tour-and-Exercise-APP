package seproject.ccu.seproject.MySport.database.entity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import seproject.ccu.seproject.MySport.database.AppDatabaseHelper;

public class ExerciseResult {
    /* Attributes */
    private byte index; // set to the number of times user exercise in the same day.
    private SimpleCalendar date;
    private double calorie;
    private byte exerciseIndex;
    private long exerciseTime;
    private double speed;

    /* Constructors */
    // Daily Result
    public ExerciseResult(SimpleCalendar date, double calorie, long exerciseTime) {
        this.date = new SimpleCalendar(date);
        this.calorie = calorie;
        this.exerciseIndex = 0;
        this.exerciseTime = exerciseTime;
    }

    // Walking, Running or Cycling
    public ExerciseResult(double calorie, byte exerciseIndex, long exerciseTime, double speed) {
        this.date = new SimpleCalendar();
        this.calorie = calorie;
        this.exerciseIndex = exerciseIndex;
        this.exerciseTime = exerciseTime;
        this.speed = speed;
    }

    // The others
    public ExerciseResult(double calorie, byte exerciseIndex, long exerciseTime) {
        this.date = new SimpleCalendar();
        this.calorie = calorie;
        this.exerciseIndex = exerciseIndex;
        this.exerciseTime = exerciseTime;
    }

    /* Methods (for accessing SQLiteDatabase)*/
    public String[] getPrimaryKeys() {
        return (new String[]{String.valueOf(this.date.getDate()), String.valueOf(this.index)});
    }

    public void insertToDatabase(Context context) {
        SQLiteOpenHelper databaseOpenHelper = new AppDatabaseHelper(context);
        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();

        // get the time of exercises in the day.
        Cursor cursor = database.query("EXERCISE_RESULT", new String[]{"ID"},
                "DATE = ?", new String[]{String.valueOf(this.date.getDate())},
                null, null, "ID DESC"
        );
        this.index = (byte) (cursor.getCount() + 1);
        cursor.close();

        AppDatabaseHelper.insert(database, this);
        database.close();
    }

    //public static List<ExerciseResult> getAll(){

    //}

    public static void deleteAllResultsFromDatabase(Context context) {
        SQLiteOpenHelper databaseOpenHelper = new AppDatabaseHelper(context);
        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();

        database.execSQL("DELETE FROM EXERCISE_RESULT ");
        database.close();
    }

    /* Methods (getters and setters)*/
    public byte getIndex() {
        return this.index;
    }

    public void setIndex(byte index) {
        this.index = index;
    }

    public SimpleCalendar getDate() {
        return this.date;
    }

    public void setDate(SimpleCalendar date) {
        this.date.setDate(date.getDate());
    }

    public double getCalorie() {
        return calorie;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }

    public long getExerciseTime() {
        return this.exerciseTime;
    }

    public void setExerciseTime(long exerciseTime) {
        this.exerciseTime = exerciseTime;
    }

    public byte getExerciseIndex() {
        return this.exerciseIndex;
    }

    public void setExerciseIndex(byte exerciseIndex) {
        this.exerciseIndex = exerciseIndex;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
