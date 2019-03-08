package seproject.ccu.seproject.MySport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import seproject.ccu.seproject.MySport.SportList.SportListActivity;
import seproject.ccu.seproject.MySport.database.entity.User;
import seproject.ccu.seproject.R;

public class MyExerciseActivity extends Activity {
    private  User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences spref = getSharedPreferences("Mode", Context.MODE_PRIVATE);
        if (spref.getBoolean("IS_DARK", false)) {
            setTheme(R.style.darkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_exercise);



        try {
            user = User.getInstance(this);  // 取得當前使用者，若無實例，從資料庫載入。
            TextView userName = (TextView)findViewById(R.id.userName);
            userName.setText(user.getGivenName());

            // 顯示熱量資訊
            TextView dayCal = (TextView) findViewById(R.id.dayCalNum);
            dayCal.setText(String.format("%.2f", user.getBurnedCalorie()));

            TextView distanceDayCal = (TextView) findViewById(R.id.distanceDayCalNum);
            distanceDayCal.setText(String.format("%.2f", user.getDailyGoal() - user.getBurnedCalorie()));
        }
        catch (Exception e){
            Toast toast = Toast.makeText(this, "尚未建立使用者", Toast.LENGTH_LONG);
            toast.show();

            Intent createUser = new Intent(this, UserDataActivity.class);
            startActivity(createUser);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

            user = User.getInstance(this);  // 取得當前使用者，若無實例，從資料庫載入。
            TextView userName = (TextView)findViewById(R.id.userName);
            userName.setText(user.getGivenName());

            // 顯示熱量資訊
            TextView dayCal = (TextView) findViewById(R.id.dayCalNum);
            dayCal.setText(String.format("%.2f", user.getBurnedCalorie()));

            TextView distanceDayCal = (TextView) findViewById(R.id.distanceDayCalNum);
            distanceDayCal.setText(String.format("%.2f", user.getDailyGoal() - user.getBurnedCalorie()));

    }

    public void onClickSportList(View view){
    //    shareTheme();
        Intent intent = new Intent(this, SportListActivity.class);
        startActivity(intent);
    }

    public void onClickBmiCalculator(View view){
    //    shareTheme();
        Intent intent = new Intent(this, BMICalculatorActivity.class);
        startActivity(intent);

    }
    // 可能不會在這裡使用
    public void onClickUserProfile(View view){
    //    shareTheme();
        Intent intent = new Intent(this, UserDataActivity.class);
        startActivity(intent);

    }

    public void onClickSportCalculator(View view){
        Intent intent = new Intent(this, SportCalculatorResultActivity.class);
        startActivity(intent);

    }

    public void onClickSportGrade(View view){
        Intent intent = new Intent(this, ExerciseResultActivity.class);
        startActivity(intent);

    }
}
