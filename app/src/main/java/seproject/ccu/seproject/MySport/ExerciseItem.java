package seproject.ccu.seproject.MySport;

public class ExerciseItem {
    public static String getItemString(byte exerciseIndex){
            String itemString = new String();
            switch(exerciseIndex){
                case 10:
                    itemString = "單車";
                    break;
                case 20:
                    itemString = "健走";
                    break;
                case 30:
                    itemString = "跑步";
                    break;
                case 40:
                    itemString = "羽球:單/雙打";
                    break;
                case 41:
                    itemString = "羽球:競賽";
                    break;
                case 50:
                    itemString = "網球:單打";
                    break;
                case 51:
                    itemString = "網球:雙打";
                    break;
                case 52:
                    itemString = "網球:擊球練習";
                    break;
                case 60:
                    itemString = "桌球";
                    break;
                case 70:
                    itemString = "籃球:練習賽";
                    break;
                case 71:
                    itemString = "籃球:投籃";
                    break;
                case 72:
                    itemString = "籃球:比賽";
                    break;
                case 80:
                    itemString = "有氧:水中有氧";
                    break;
                case 81:
                    itemString = "有氧:有氧舞蹈";
                    break;
                case 90:
                    itemString = "瑜珈";
                    break;
            }
            return itemString;
    }

    public static String getTimeString(int hours, int mins, int secs){
        String timeString = new String();
        if (hours != 0) {
            timeString = String.valueOf(hours) + " 小時 " + String.valueOf(mins) + " 分 " + String.valueOf(secs) + "秒";
        }
        else{
            timeString = String.valueOf(mins) + " 分 " + String.valueOf(secs) + "秒";
        }
        return timeString;
    }
}
