package seproject.ccu.seproject.MySport.database.entity;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;

import seproject.ccu.seproject.MySport.CalorieCalculator;
import seproject.ccu.seproject.MySport.database.AppDatabaseHelper;

public class User {
    /* Singleton Instance */
    public static User instance = null;

    /* Constants (for Gender) */
    public static final byte female = 0;
    public static final byte male = 1;

    /* Attributes */
    private String surname;
    private String givenName;
    private SimpleCalendar birthday;
    private byte gender;
    private double height;
    private double weight;
    private double dailyGoal;
    private double burnedCalorie;
    private long exerciseTime;
    private int point;
    private SimpleCalendar lastAccessDate;

    /* Constructor */
    public User(String surname, String givenName, int birthYear, int birthMonth, int birthday , byte gender, double height, double weight, double dailyGoal){
        this.surname = surname;
        this.givenName = givenName;
        this.birthday = new SimpleCalendar(birthYear, birthMonth, birthday);
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.dailyGoal = dailyGoal;
        this.burnedCalorie = 0;
        this.exerciseTime = 0;
        this.point= 0;
        this.lastAccessDate = new SimpleCalendar();
    }
    public User(String surname, String givenName, SimpleCalendar birthday, byte gender, double height, double weight, double dailyGoal){
        this.surname = surname;
        this.givenName = givenName;
        this.birthday = new SimpleCalendar(birthday);
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.dailyGoal = dailyGoal;
        this.burnedCalorie = 0;
        this.exerciseTime = 0;
        this.point= 0;
        this.lastAccessDate = new SimpleCalendar();
    }
    // Constructor (temporarily user)
    public User(double height, double weight, int age, byte gender){
        this.height = height;
        this.weight = weight;
        this.birthday = new SimpleCalendar();
        this.birthday.setYear(this.birthday.getYear() - (age - 1));
        this.gender = gender;
    }

    // Constructor (database user)
    public User(){
    }

    /* Methods (for actual use) */
    public int getAge(){
        SimpleCalendar today = new SimpleCalendar();
        int age = today.getYear() - this.birthday.getYear();

        if(!today.isBeforeAnniversary(this.birthday)){
            age++;
        }

        return age;
    }

    // We should not favor any gender, right?
    public boolean isFemale(){
        return (gender == female);
    }
    public boolean isMale(){
        return (gender == male);
    }

    public void addBurnedCalorie(double increment){
        this.burnedCalorie += increment;
    }

    public void addExerciseTime(long increment){
        this.exerciseTime += increment;
    }

    public void addPoint(int increment){
        this.point += increment;
    }
    public boolean subtractPoint(int decrement){
        if(decrement <= this.point){
            this.point -= decrement;
            return true;
        }else{
            return false;
        }
    }

    /* Methods (for accessing SQLiteDatabase) */
    public String[] getPrimaryKeys() {
        return (new String[] {this.surname, this.givenName});
    }

    public static User getInstance(Context context) throws SQLException {
        if(instance == null){
            instance = new User();
            if(!instance.loadFromDatabase(context.getApplicationContext())){
                throw new SQLException("No user in database.");
            }
        }

        SimpleCalendar today = new SimpleCalendar();
        if(instance.lastAccessDate.getDate() != today.getDate()){
            instance.dailyInit(context);
        }
        return instance;
    }

    public void updateInformation(Context context){
        SQLiteOpenHelper databaseOpenHelper = new AppDatabaseHelper(context);
        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();

        database.execSQL("DELETE FROM USER");   // User may modify the primary keys (that is, his/her name), this ensures that old data won't exist.
        AppDatabaseHelper.insert(database, this);
        database.close();

        instance.loadFromDatabase(context);
    }

    public void updateData(Context context){
        SQLiteOpenHelper databaseOpenHelper = new AppDatabaseHelper(context);
        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();

        AppDatabaseHelper.update(database, this);
        database.close();
    }

    public void dailyInit(Context context){
        ExerciseResult yesterdayResult = new ExerciseResult(lastAccessDate, burnedCalorie, exerciseTime);

        burnedCalorie = 0.0;
        exerciseTime = 0;
        lastAccessDate = new SimpleCalendar();

        this.updateData(context);
        yesterdayResult.insertToDatabase(context);
    }

    public void deleteFromDatabase(Context context){
        SQLiteOpenHelper databaseOpenHelper = new AppDatabaseHelper(context);
        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();

        AppDatabaseHelper.delete(database, this);
        database.close();
    }

    private boolean loadFromDatabase(Context context){
        SQLiteOpenHelper databaseOpenHelper = new AppDatabaseHelper(context);
        SQLiteDatabase database = databaseOpenHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM USER", null);
        if(cursor.moveToFirst()) {
            this.surname = cursor.getString(0);
            this.givenName = cursor.getString(1);
            this.birthday = new SimpleCalendar(cursor.getInt(2));
            this.gender = (byte) cursor.getInt(3);
            this.height = cursor.getDouble(4);
            this.weight = cursor.getDouble(5);
            this.dailyGoal = cursor.getDouble(6);
            this.burnedCalorie = cursor.getDouble(7);
            this.exerciseTime = cursor.getLong(8);
            this.point = cursor.getInt(9);
            this.lastAccessDate = new SimpleCalendar(cursor.getInt(10));

            cursor.close();
            database.close();
            return true;
        }else{
            cursor.close();
            database.close();
            return false;
        }
    }


    /* Methods (getters and setters of attributes.) */
    public String getSurname() {
        return this.surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivenName() {
        return this.givenName;
    }
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public SimpleCalendar getBirthday() {
        return this.birthday;
    }
    public void setBirthday(SimpleCalendar birthday) {
        this.birthday = new SimpleCalendar(birthday);
    }

    public byte getGender() {
        return this.gender;
    }
    public void setGender(byte gender) {
        this.gender = gender;
    }

    public double getHeight() {
        return this.height;
    }
    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return this.weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getDailyGoal() {
        return this.dailyGoal;
    }
    public void setDailyGoal(double dailyGoal) {
        this.dailyGoal = dailyGoal;
    }

    public double getBurnedCalorie() {
        return this.burnedCalorie;
    }
    public void setBurnedCalorie(double burnedCalorie) {
        this.burnedCalorie = burnedCalorie;
    }

    public long getExerciseTime() {
        return exerciseTime;
    }
    public void setExerciseTime(long exerciseTime) {
        this.exerciseTime = exerciseTime;
    }

    public int getPoint() {
        return point;
    }
    public void setPoint(int point) {
        this.point = point;
    }

    public SimpleCalendar getLastAccessDate(){
        return this.lastAccessDate;
    }
}