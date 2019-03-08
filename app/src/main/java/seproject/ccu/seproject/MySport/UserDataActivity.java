package seproject.ccu.seproject.MySport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import seproject.ccu.seproject.MySport.database.entity.User;
import seproject.ccu.seproject.R;

public class UserDataActivity extends Activity {
    private RadioGroup rg;
    private EditText surname;
    private EditText givenName;
    private EditText birthdayYear;
    private EditText birthdayMonth;
    private EditText birthdayDay;
    private EditText height;
    private EditText weight;
    private EditText dailyGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences spref = getSharedPreferences("Mode", Context.MODE_PRIVATE);
        if (spref.getBoolean("IS_DARK", false)) {
            setTheme(R.style.darkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        rg = (RadioGroup)findViewById(R.id.genderRadioGroup);
        surname = (EditText)findViewById(R.id.surname);
        givenName = (EditText)findViewById(R.id.givenName);
        birthdayYear = (EditText)findViewById(R.id.birthdayYear);
        birthdayMonth = (EditText)findViewById(R.id.birthdayMonth);
        birthdayDay = (EditText)findViewById(R.id.birthdayDay);
        height = (EditText)findViewById(R.id.userHeightNum);
        weight = (EditText)findViewById(R.id.userWeightNum);
        dailyGoal = (EditText)findViewById(R.id.dailyGoalNum);


        try{
            User user = User.getInstance(this);

            surname.setText(user.getSurname());
            givenName.setText(user.getGivenName());
            birthdayYear.setText(String.valueOf(user.getBirthday().getYear()));
            birthdayMonth.setText(String.valueOf(user.getBirthday().getMonth()));
            birthdayDay.setText(String.valueOf(user.getBirthday().getDayOfMonth()));
            height.setText(String.valueOf(user.getHeight()));
            weight.setText(String.valueOf(user.getWeight()));
            dailyGoal.setText(String.valueOf(user.getDailyGoal()));
            rg.check(user.isMale() ? R.id.male : R.id.female);
        }catch (Exception exception){

        }
    }

    public void onClickConfirm(View view){
        // 取得輸入之所有資料
        try {
            User newUser = new User(
                    surname.getText().toString(),
                    givenName.getText().toString(),
                    Integer.parseInt(birthdayYear.getText().toString()),
                    Integer.parseInt(birthdayMonth.getText().toString()),
                    Integer.parseInt(birthdayDay.getText().toString()),
                    (rg.getCheckedRadioButtonId() == R.id.male) ? User.male : User.female,
                    Double.parseDouble(height.getText().toString()),
                    Double.parseDouble(weight.getText().toString()),
                    Double.parseDouble(dailyGoal.getText().toString())
            );

            // 填入資料庫
            newUser.updateInformation(this);
            finish();

        }catch(Exception e){
            Toast toast = Toast.makeText(this, "請確實填寫資訊", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
