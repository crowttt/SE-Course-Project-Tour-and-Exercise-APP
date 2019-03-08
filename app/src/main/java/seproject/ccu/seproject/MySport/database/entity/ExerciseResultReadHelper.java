package seproject.ccu.seproject.MySport.database.entity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import seproject.ccu.seproject.MySport.CalorieCalculator;
import seproject.ccu.seproject.MySport.database.AppDatabaseHelper;

import java.util.Calendar;

public class ExerciseResultReadHelper {
    private SQLiteOpenHelper helper;
    private SQLiteDatabase database;

    public ExerciseResultReadHelper(Context context){
        helper = new AppDatabaseHelper(context);
        database = helper.getReadableDatabase();
    }

    public Cursor getCursorOfAllResults() {
        return database.rawQuery("SELECT * FROM EXERCISE_RESULT", null);
    }

    public Cursor getCursorOfTodayResults(){
        SimpleCalendar today = new SimpleCalendar();
        return getCursorOfGivenDateResults(today);
    }

    public Cursor getCursorOfThisWeekResults(){
        SimpleCalendar today = new SimpleCalendar();
        return getCursorOfGivenWeekResults(today);
    }

    public Cursor getCursorOfThisMonthResults(){
        SimpleCalendar today = new SimpleCalendar();
        return getCursorOfGivenMonthResults(today);
    }

    public Cursor getCursorOfThisYearResults(){
        SimpleCalendar today = new SimpleCalendar();
        return getCursorOfGivenYearResults(today);
    }

    public Cursor getCursorOfGivenDateResults(SimpleCalendar date){
        return database.query("EXERCISE_RESULT", new String[] {"ID"},
                "DATE = ?", new String[] {String.valueOf(date.getDate())},
                null, null, "ID DESC"
        );
    }

    public Cursor getCursorOfGivenWeekResults(SimpleCalendar date){
        Calendar weekStartTmp = date.toCalendar();
        Calendar weekEndTmp = date.toCalendar();

        int dayInWeek = weekStartTmp.get(Calendar.DAY_OF_WEEK);

        weekStartTmp.add(Calendar.DAY_OF_MONTH, -1 * (dayInWeek - 1));
        weekEndTmp.add(Calendar.DAY_OF_MONTH, (7 - dayInWeek));

        SimpleCalendar weekStart = new SimpleCalendar(weekStartTmp);
        SimpleCalendar weekEnd = new SimpleCalendar(weekEndTmp);

        return getCursorInGivenRange(weekStart, weekEnd);
    }

    public Cursor getCursorOfGivenMonthResults(SimpleCalendar date){
        Calendar monthStartTmp = date.toCalendar();
        Calendar monthEndTmp = date.toCalendar();

        monthStartTmp.set(Calendar.DAY_OF_MONTH, 1);

        monthEndTmp.set(Calendar.MONTH, date.getMonth());
        monthEndTmp.add(Calendar.DAY_OF_MONTH, -1);

        SimpleCalendar monthStart = new SimpleCalendar(monthStartTmp);
        SimpleCalendar monthEnd = new SimpleCalendar(monthEndTmp);

        return getCursorInGivenRange(monthStart, monthEnd);
    }

    public Cursor getCursorOfGivenYearResults(SimpleCalendar date){
        Calendar yearStartTmp = date.toCalendar();
        Calendar yearEndTmp = date.toCalendar();

        yearStartTmp.set(Calendar.DAY_OF_YEAR, 1);

        yearEndTmp.set(Calendar.YEAR, date.getYear() + 1);
        yearEndTmp.add(Calendar.DAY_OF_YEAR, -1);

        SimpleCalendar yearStart = new SimpleCalendar(yearStartTmp);
        SimpleCalendar yearEnd = new SimpleCalendar(yearEndTmp);

        return getCursorInGivenRange(yearStart, yearEnd);
    }

    public Cursor getCursorInGivenRange(SimpleCalendar start, SimpleCalendar end){
        return database.query("EXERCISE_RESULT", new String[] {"ID"},
                "DATE >= ? & DATE <= ?", new String[] {String.valueOf(start.getDate()), String.valueOf(end.getDate())},
                null, null, "DATE DESC, ID DESC"
        );
    }

    public void close(){
        database.close();
    }

    public void generateTestData(Context context){
        double calorie;
        long tmpSeconds = 1200;
        double distance = 5000;
        byte index = 10;
        User user = User.getInstance(context);
        ExerciseResult result;

        index = CalorieCalculator.biking;
        calorie = CalorieCalculator.calculateCalorie(user, index, tmpSeconds, ((distance / tmpSeconds) * 3.6));
        result = new ExerciseResult(calorie, index, tmpSeconds, (distance / tmpSeconds) * 3.6);
        user.addBurnedCalorie(calorie);
        user.addExerciseTime(tmpSeconds);
        user.addPoint((int)(CalorieCalculator.getMetabolicEquivalentOfTask(index, ((distance / tmpSeconds) * 3.6)) * (tmpSeconds / 600)));
        user.updateData(context);
        result.insertToDatabase(context);

        index = CalorieCalculator.walking;
        calorie = CalorieCalculator.calculateCalorie(user, index, tmpSeconds, ((distance / tmpSeconds) * 3.6));
        result = new ExerciseResult(calorie, index, tmpSeconds, (distance / tmpSeconds) * 3.6);
        user.addBurnedCalorie(calorie);
        user.addExerciseTime(tmpSeconds);
        user.addPoint((int)(CalorieCalculator.getMetabolicEquivalentOfTask(index, ((distance / tmpSeconds) * 3.6)) * (tmpSeconds / 600)));
        user.updateData(context);
        result.insertToDatabase(context);

        index = CalorieCalculator.jogging;
        calorie = CalorieCalculator.calculateCalorie(user, index, tmpSeconds, ((distance / tmpSeconds) * 3.6));
        result = new ExerciseResult(calorie, index, tmpSeconds, (distance / tmpSeconds) * 3.6);
        user.addBurnedCalorie(calorie);
        user.addExerciseTime(tmpSeconds);
        user.addPoint((int)(CalorieCalculator.getMetabolicEquivalentOfTask(index, ((distance / tmpSeconds) * 3.6)) * (tmpSeconds / 600)));
        user.updateData(context);
        result.insertToDatabase(context);

        index = CalorieCalculator.badminton;
        calorie = CalorieCalculator.calculateCalorie(user, index, tmpSeconds);
        result = new ExerciseResult(calorie, index, tmpSeconds);
        user.addBurnedCalorie(calorie);
        user.addExerciseTime(tmpSeconds);
        user.updateData(context);
        result.insertToDatabase(context);

        index = CalorieCalculator.tennis;
        calorie = CalorieCalculator.calculateCalorie(user, index, tmpSeconds);
        result = new ExerciseResult(calorie, index, tmpSeconds);
        user.addBurnedCalorie(calorie);
        user.addExerciseTime(tmpSeconds);
        user.updateData(context);
        result.insertToDatabase(context);

        index = CalorieCalculator.tableTennis;
        calorie = CalorieCalculator.calculateCalorie(user, index, tmpSeconds);
        result = new ExerciseResult(calorie, index, tmpSeconds);
        user.addBurnedCalorie(calorie);
        user.addExerciseTime(tmpSeconds);
        user.updateData(context);
        result.insertToDatabase(context);

        index = CalorieCalculator.basketball;
        calorie = CalorieCalculator.calculateCalorie(user, index, tmpSeconds);
        result = new ExerciseResult(calorie, index, tmpSeconds);
        user.addBurnedCalorie(calorie);
        user.addExerciseTime(tmpSeconds);
        user.updateData(context);
        result.insertToDatabase(context);

        index = CalorieCalculator.aerobic;
        calorie = CalorieCalculator.calculateCalorie(user, index, tmpSeconds);
        result = new ExerciseResult(calorie, index, tmpSeconds);
        user.addBurnedCalorie(calorie);
        user.addExerciseTime(tmpSeconds);
        user.updateData(context);
        result.insertToDatabase(context);

        index = CalorieCalculator.yoga;
        calorie = CalorieCalculator.calculateCalorie(user, index, tmpSeconds);
        result = new ExerciseResult(calorie, index, tmpSeconds);
        user.addBurnedCalorie(calorie);
        user.addExerciseTime(tmpSeconds);
        user.updateData(context);
        result.insertToDatabase(context);

        user.dailyInit(context);
    }
}
