package seproject.ccu.seproject.MySport.database.entity;

import java.util.Calendar;
import java.util.Locale;

public class SimpleCalendar {
    private static final byte shiftOffsetForSettingYear  = 15;
    private static final byte shiftOffsetForSettingMonth = 11;
    private static final byte shiftOffsetForSettingWeek  =  5;
    private static final int maskForRetrieveYear  = 0b11111111111111111000000000000000;
    private static final int maskForRetrieveMonth = 0b00000000000000000111100000000000;
    private static final int maskForRetrieveWeek  = 0b00000000000000000000011111100000;
    private static final int maskForRetrieveDay   = 0b00000000000000000000000000011111;

    private int date;

    /* Constructors */
    // get instance with current time.
    public SimpleCalendar(){
        Calendar today = Calendar.getInstance(Locale.TAIWAN);

        this.set(today.get(Calendar.YEAR),
                today.get(Calendar.MONTH) + 1,
                today.get(Calendar.WEEK_OF_YEAR),
                today.get(Calendar.DAY_OF_MONTH)
        );
    }

    public SimpleCalendar(int year, int month, int day){
        this.set(year, month, day);
    }

    // get instance with the given calendar.
    public SimpleCalendar(Calendar calendar){
        this.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.WEEK_OF_YEAR),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
    }

    // Copy Constructor.
    public SimpleCalendar(SimpleCalendar calendar){
        this.date = calendar.date;
    }

    // get instance with an already verified integer. (only for accessing database, do not use it.)
    public SimpleCalendar(int date){
        this.date = date;
    }

    /* Methods (getter and setter) */
    public int getDate() {
        return date;
    }
    public void setDate(int date) {
        this.date = date;
    }

    public Calendar toCalendar(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(getYear(), getMonth() - 1, getDayOfMonth());
        return calendar;
    }

    /* Methods (get year, month, day of month) */
    public void set(int year, int month, int week, int day) throws IllegalArgumentException{
        // just a simple check.
        if(!isInRange(month, 12) || !isInRange(week, 53) || !isInRange(day, 31)){
            throw new IllegalArgumentException("Wrong Date");
        }

        this.date = getShiftedYear(year)
                +   getShiftedMonth(month)
                +   getShiftedWeek(week)
                +   day;
    }
    public void set(int year, int month, int day) throws IllegalArgumentException{
        // just a simple check.
        if(!isInRange(month, 12) || !isInRange(day, 31)){
            throw new IllegalArgumentException("Wrong Date");
        }

        Calendar tmp = Calendar.getInstance();

        tmp.set(year, month - 1, day);

        this.date = getShiftedYear(year)
                +   getShiftedMonth(month)
                +   getShiftedWeek(tmp.get(Calendar.WEEK_OF_YEAR))
                +   day;
    }

    public int getYear(){
        return (this.date & maskForRetrieveYear) >> shiftOffsetForSettingYear;
    }
    public void setYear(int year){
        this.date = getShiftedYear(year) + (this.date & (maskForRetrieveMonth | maskForRetrieveWeek | maskForRetrieveDay));
    }

    public int getMonth(){
        return (this.date & maskForRetrieveMonth) >> shiftOffsetForSettingMonth;
    }
    public void setMonth(int month){
        this.date = getShiftedMonth(month) + (this.date & (maskForRetrieveYear | maskForRetrieveWeek | maskForRetrieveDay));
    }

    public int getWeekOfYear(){
        return (this.date & maskForRetrieveWeek) >> shiftOffsetForSettingWeek;
    }
    public void setWeekOfYear(int week){
        this.date = getShiftedWeek(week) + (this.date & (maskForRetrieveYear | maskForRetrieveMonth | maskForRetrieveDay));
    }

    public int getDayOfMonth(){
        return (this.date & maskForRetrieveDay);
    }
    public void setDayOfMonth(int day){
        this.date = day + (this.date & (maskForRetrieveYear | maskForRetrieveMonth | maskForRetrieveWeek));
    }

    public boolean isAnniversary(SimpleCalendar date){
        return (date.getAnniversary() == this.getAnniversary());
    }
    public boolean isBeforeAnniversary(SimpleCalendar date){
        return (date.getAnniversary() < this.getAnniversary());
    }
    public boolean isAfterAnniversary(SimpleCalendar date){
        return (date.getAnniversary() > this.getAnniversary());
    }

    /* Methods (Tools) */
    @Override
    public String toString(){
        return String.format(Locale.TAIWAN, "%d/%02d/%02d", getYear(), getMonth(), getDayOfMonth());
    }

    /* Methods (Private Tools) */
    private int getShiftedYear(int year){
        return year << shiftOffsetForSettingYear;
    }
    private int getShiftedMonth(int month){
        return month << shiftOffsetForSettingMonth;
    }
    private int getShiftedWeek(int week){
        return week << shiftOffsetForSettingWeek;
    }
    private int getAnniversary(){
        return this.date & (maskForRetrieveMonth | maskForRetrieveDay);
    }
    private boolean isInRange(int number, int range){
        return (number > 0 && number <= range);
    }
}