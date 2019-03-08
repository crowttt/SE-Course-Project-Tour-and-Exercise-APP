package seproject.ccu.seproject.MySport.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import seproject.ccu.seproject.MySport.database.entity.ExerciseResult;
import seproject.ccu.seproject.MySport.database.entity.User;

public class AppDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AppDatabase";
    private static final int DATABASE_VERSION = 1;

    public AppDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database){
        database.execSQL("CREATE TABLE USER ("
                + "SURNAME TEXT, "
                + "GIVEN_NAME TEXT, "
                + "BIRTHDAY INTEGER, "
                + "GENDER NUMERIC, "
                + "HEIGHT DOUBLE, "
                + "WEIGHT DOUBLE, "
                + "DAILY_GOAL DOUBLE, "
                + "BURNED_CALORIE DOUBLE, "
                + "EXERCISE_TIME INTEGER, "
                + "POINT INTEGER, "
                + "LAST_ACCESS_DATE INTEGER, "
                + "PRIMARY KEY (SURNAME, GIVEN_NAME));"
        );
        database.execSQL("CREATE TABLE EXERCISE_RESULT ("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "DATE INTEGER, "
                + "CALORIE DOUBLE, "
                + "EXERCISE_INDEX NUMERIC, "
                + "EXERCISE_TIME INTEGER, "
                + "SPEED DOUBLE);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){

    }

    @Override
    public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion){

    }

    /* Methods (Insertion) */
    public static void insert(SQLiteDatabase database, User user) {
        database.insert("USER", null, fillAndGetContentValues(user));
    }
    public static void insert(SQLiteDatabase database, ExerciseResult result) {
        database.insert("EXERCISE_RESULT", null, fillAndGetContentValues(result));
    }

    /* Methods (Deletion) */
    public static void delete(SQLiteDatabase database, User user){
        database.delete("USER", "SURNAME = ? AND GIVEN_NAME = ?", user.getPrimaryKeys());
    }
    public static void delete(SQLiteDatabase database, ExerciseResult result){
        database.delete("EXERCISE_RESULT", "ID = ?", result.getPrimaryKeys());
    }

    /* Methods (Update) */
    public static void update(SQLiteDatabase database, User user){
        database.update("USER", fillAndGetContentValues(user), "SURNAME = ? AND GIVEN_NAME = ?", user.getPrimaryKeys());
    }
    public static void update(SQLiteDatabase database, ExerciseResult result){
        database.update("EXERCISE_RESULT", fillAndGetContentValues(result), "ID = ?", result.getPrimaryKeys());
    }

    /* Methods (Tools) */
    private static ContentValues fillAndGetContentValues(User user){
        ContentValues value = new ContentValues();

        value.put("SURNAME", user.getSurname());
        value.put("GIVEN_NAME", user.getGivenName());
        value.put("BIRTHDAY", user.getBirthday().getDate());
        value.put("GENDER", user.getGender());
        value.put("HEIGHT", user.getHeight());
        value.put("WEIGHT", user.getWeight());
        value.put("DAILY_GOAL", user.getDailyGoal());
        value.put("BURNED_CALORIE", user.getBurnedCalorie());
        value.put("EXERCISE_TIME", user.getExerciseTime());
        value.put("POINT", user.getPoint());
        value.put("LAST_ACCESS_DATE", user.getLastAccessDate().getDate());

        return value;
    }
    private static ContentValues fillAndGetContentValues(ExerciseResult result){
        ContentValues value = new ContentValues();

        value.put("DATE", result.getDate().getDate());
        value.put("CALORIE", result.getCalorie());
        value.put("EXERCISE_INDEX", result.getExerciseIndex());
        value.put("EXERCISE_TIME", result.getExerciseTime());
        value.put("SPEED", result.getSpeed());
        return value;
    }
}