package seproject.ccu.seproject.MySport;

import seproject.ccu.seproject.MySport.database.entity.User;
import seproject.ccu.seproject.R;

public class CalorieCalculator {

    public static final byte biking = 10;
    //  8-     km/hr, 3.5
    // 15 ~ 16 km/hr, 5.8
    // 16 ~ 19 km/hr, 6.8
    // 19 ~ 23 km/hr, 8.0
    // 23 ~ 26 km/hr, 10.0
    // 26 ~ 32 km/hr, 12
    // 32+     km/hr, 15.8

    public static final byte walking = 20;
    // 3-      km/hr, 2
    // 3 ~ 5.5 km/hr, 3.5
    // 5.5 ~ 6 km/hr, 4.3

    public static final byte jogging = 30;
    // 6-           km/hr, 6.0
    // 6 ~ 8        km/hr, 8.3
    // 8 ~ 9.7      km/hr, 9.0
    // 9.7 ~ 10.8   km/hr, 9.8
    // 10.8 ~ 11    km/hr, 10.5
    // 11 ~ 12      km/hr, 11
    // 12 ~ 13      km/hr, 11.5
    // 13 ~ 14      km/hr, 11.8
    // 14 ~ 14.5    km/hr, 12.3
    // 14.5 ~ 16    km/hr, 12.8
    // 16 ~ 17.7    km/hr, 14.5
    // 17.7 ~ 19.3  km/hr, 16
    // 19.3 ~ 20.9  km/hr, 19
    // 21 ~ 22.6    km/hr, 19.8
    // 22.6+        km/hr, 23

    public static final byte badminton = 40;
    private static final double [] badmintonMET = {5.5, 7.0};
    // social singles and doubles, general 5.5
    // competitive 7.0

    public static final byte tennis = 50;
    private static final double [] tennisMET = {7.3, 4.5, 5.0};
    // single 7.3
    // double 4.5
    // hitting balls, non-game play 5

    public static final byte tableTennis = 60;
    private static final double tableTennisMET = 4.0;
    // always 4

    public static final byte basketball = 70;
    private static final double [] basketballMET = {9.3, 4.5, 8.0};
    // drills 9.3
    // shooting 4.5
    // game 8

    public static final byte aerobic = 80;
    private static final double [] aerobicMET = {5.3, 7.3};
    // water 5.3
    // dancing 7.3

    public static final byte yoga = 90;
    private static final double yogaMET = 2.3;
    // always 2.3

    // calculate user's BMR, which is the energy required for essential physiological functioning.
    public static double getBasalMetabolicRate(User user) {
        double BMR;

        double age = (double) user.getAge();
        double height = user.getHeight();
        double weight = user.getWeight();

        if (user.isMale()) {
            BMR = (13.397 * weight) + (4.799 * height) + (5.677 * age) + 88.362;
        } else {
            BMR = (9.247 * weight) + (3.098 * height) + (4.33 * age) + 447.593;
        }
        return BMR;
    }

    // Metabolic Equivalent Of Task, which is used to calculate calorie.
    public static double calculateCalorie(User user, byte exerciseIndex, long seconds, double speed) {
        double BMR = getBasalMetabolicRate(user);
        double MET = getMetabolicEquivalentOfTask(exerciseIndex, speed);

        // ((BMR * MET) / 24) * (seconds / 3600);
        return ((BMR * MET * seconds) / 86400);
    }

    // Metabolic Equivalent Of Task, which is used to calculate calorie.
    public static double calculateCalorie(User user, byte exerciseIndex, long seconds) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        double BMR = getBasalMetabolicRate(user);
        double MET = getMetabolicEquivalentOfTask(exerciseIndex);

        // ((BMR * MET) / 24) * (seconds / 3600);
        return ((BMR * MET * seconds) / 86400);
    }

    public static double getMetabolicEquivalentOfTask(byte index) throws IllegalArgumentException, ArrayIndexOutOfBoundsException{
        byte exerciseWay = (byte) (index % 10);
        byte exerciseType = (byte) (index - exerciseWay);
        switch (exerciseType) {
            case badminton:
                return badmintonMET[exerciseWay];
            case tennis:
                return tennisMET[exerciseWay];
            case tableTennis:
                return tableTennisMET;
            case basketball:
                return basketballMET[exerciseWay];
            case aerobic:
                return aerobicMET[exerciseWay];
            case yoga:
                return yogaMET;
            default:
                throw new IllegalArgumentException("No such Exercise.");
        }
    }

    public static double getMetabolicEquivalentOfTask(byte index, double speed){
        if (index == biking) {
            return getBikingMetabolicEquivalentOfTask(speed);
        } else { // Running or Walking
            return getWalkingMetabolicEquivalentOfTask(speed);
        }
    }

    private static double getBikingMetabolicEquivalentOfTask(double speed) {
        if(speed < 3.0){
            return 0.0;
        }
        else if (speed < 15.0) {
            return 3.5;
        } else if (speed < 16.0) {
            return 5.8;
        } else if (speed < 19.0) {
            return 6.8;
        } else if (speed < 23.0) {
            return 8.0;
        } else if (speed < 26.0) {
            return 10.0;
        } else if (speed < 32.0) {
            return 12.0;
        } else if (speed < 40.0) {
            return 15.8;
        } else {
            throw new IllegalArgumentException("Are you a guy?");
        }
    }

    private static double getWalkingMetabolicEquivalentOfTask(double speed) {
        if(speed < 1.0){
            return 0.0;
        } else if (speed < 3.0) {
            return 2.0;
        } else if (speed < 5.5) {
            return 3.5;
        } else if (speed < 6.0) {
            return 4.3;
        } else if (speed < 8.0) {
            return 8.3;
        } else if (speed < 9.7) {
            return 9.0;
        } else if (speed < 11.0) {
            return 10.5;
        } else if (speed < 12.0) {
            return 11.0;
        } else if (speed < 13.0) {
            return 11.5;
        } else if (speed < 14.0) {
            return 11.8;
        } else if (speed < 14.5) {
            return 12.3;
        } else if (speed < 16.0) {
            return 12.8;
        } else if (speed < 17.7) {
            return 14.5;
        } else if (speed < 19.3) {
            return 16.0;
        } else if (speed < 20.9) {
            return 19;
        } else if (speed < 22.6) {
            return 19.8;
        } else if (speed < 36.0) {
            return 23.0;
        } else {
            throw new IllegalArgumentException("Are you a guy?");
        }
    }
    public static String getExerciseName(int exerciseIndex) throws ArrayIndexOutOfBoundsException {
        switch (exerciseIndex) {
            case 0:
                return "當日總計";
            case walking:
                return "健走";
            case jogging:
                return "跑步";
            case biking:
                return "單車";
            case badminton:
                return "羽球:單/雙打";
            case badminton + 1:
                return "羽球:競賽";
            case tennis:
                return "網球:單打";
            case tennis + 1:
                return "網球:雙打";
            case tennis + 2:
                return "網球:擊球練習";
            case tableTennis:
                return "桌球";
            case basketball:
                return "籃球:練習賽";
            case basketball + 1:
                return "籃球:投籃";
            case basketball + 2:
                return "籃球:比賽";
            case yoga:
                return "瑜珈";
            case aerobic:
                return "有氧:水中有氧";
            case aerobic + 1:
                return "有氧:有氧舞蹈";
            default:
                throw new ArrayIndexOutOfBoundsException("No Such Exercise");
        }
    }

    public static int getExerciseIconID(int exerciseIndex) throws ArrayIndexOutOfBoundsException {
        exerciseIndex -= (exerciseIndex % 10);
        switch (exerciseIndex) {
            case 0:
                return R.drawable.exercise_result_sum;
            case walking:
                return R.drawable.exercise_result_walking;
            case jogging:
                return R.drawable.exercise_result_jogging;
            case biking:
                return R.drawable.exercise_result_cycling;
            case badminton:
                return R.drawable.exercise_result_badminton;
            case tennis:
                return R.drawable.exercise_result_tennis;
            case tableTennis:
                return R.drawable.exercise_result_table_tennis;
            case basketball:
                return R.drawable.exercise_result_basketball;
            case aerobic:
                return R.drawable.exercise_result_aerobic;
            case yoga:
                return R.drawable.exercise_result_yoga;
            default:
                throw new ArrayIndexOutOfBoundsException("No Such Exercise");
        }
    }
}