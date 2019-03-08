package seproject.ccu.seproject.MySport;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import seproject.ccu.seproject.MySport.database.entity.User;
import seproject.ccu.seproject.R;

import static seproject.ccu.seproject.MySport.CalorieCalculator.calculateCalorie;

public class SportCalculatorResultActivity extends Activity {
    private Spinner sportItem, itemDetail;
    private TextView speedText, calorieScreen;
    private EditText speedValue, hoursValue, minsValue, secsValue, heightValue, weightValue, ageValue;
    private RadioGroup rg;
    private boolean isReferSpeed;
    private int exerciseType;
    private ArrayAdapter<CharSequence> itemDetailSelection;
    private int exerciseWay = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences spref = getSharedPreferences("Mode", Context.MODE_PRIVATE);
        if (spref.getBoolean("IS_DARK", false)) {
            setTheme(R.style.darkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_calculator_result);
        itemDetail = (Spinner)findViewById(R.id.ItemDetailSpinner);
        sportItem = (Spinner)findViewById(R.id.SportItemSpinner);
        speedText = (TextView)findViewById(R.id.sportSpeedText);
        speedValue = (EditText)findViewById(R.id.speedValue);
        calorieScreen = (TextView)findViewById(R.id.calorieScreen);

        sportItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemDetailSelection = null;
                isReferSpeed = false;
                exerciseType = 0;
                exerciseWay = 0;

                switch (position) {
                    case 0:
                        isReferSpeed = true;
                        break;
                    case 1:
                        isReferSpeed = true;
                        break;
                    case 2:
                        isReferSpeed = true;
                        break;
                    case 3:
                        itemDetailSelection = ArrayAdapter.createFromResource(SportCalculatorResultActivity.this, R.array.badmintonSelection, android.R.layout.simple_spinner_dropdown_item);
                        getIndexItem();
                        break;
                    case 4:
                        itemDetailSelection = ArrayAdapter.createFromResource(SportCalculatorResultActivity.this, R.array.tennisSelection, android.R.layout.simple_spinner_dropdown_item);
                        getIndexItem();
                        break;
                    case 6:
                        itemDetailSelection = ArrayAdapter.createFromResource(SportCalculatorResultActivity.this, R.array.basketballSelection, android.R.layout.simple_spinner_dropdown_item);
                        getIndexItem();
                        break;
                    case 8:
                        itemDetailSelection = ArrayAdapter.createFromResource(SportCalculatorResultActivity.this, R.array.gymnasticSelection, android.R.layout.simple_spinner_dropdown_item);
                        getIndexItem();
                        break;
                }
                if(itemDetailSelection != null) {
                    itemDetail.setVisibility(View.VISIBLE);
                    itemDetail.setAdapter(itemDetailSelection);
                }
                else{
                    itemDetail.setVisibility(View.INVISIBLE);
                }

                if(isReferSpeed == true){
                    speedText.setVisibility(View.VISIBLE);
                    speedValue.setVisibility(View.VISIBLE);
                }
                else{
                    speedValue.setText("");
                    speedText.setVisibility(View.INVISIBLE);
                    speedValue.setVisibility(View.INVISIBLE);
                }
                exerciseType = (position + 1) * 10;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){
                return;
            }
        });
    }

    public void onClickConfirm(View view){
        StringBuffer errorMessage = new StringBuffer("請輸入正確");
        boolean isError = false;
        int hour = 0, min = 0, sec = 0, age = 0;
        double height = 0, weight = 0, speed = 0;

        heightValue = (EditText)findViewById(R.id.heightNum);
        weightValue = (EditText)findViewById(R.id.weightNum);
        ageValue = (EditText)findViewById(R.id.ageNum);

        byte exerciseIndex = (byte) (exerciseType + exerciseWay);

        if(heightValue.getText().toString().isEmpty()){
            errorMessage.append(" 身高");
        }
        else{
            height = Double.parseDouble(heightValue.getText().toString());
        }
        if(weightValue.getText().toString().isEmpty()){
            errorMessage.append(" 體重");
        }
        else{
            weight = Double.parseDouble(weightValue.getText().toString());
        }
        if(ageValue.getText().toString().isEmpty()){
            errorMessage.append(" 年齡");
        }
        else{
            age = Integer.parseInt(ageValue.getText().toString());
        }

        if(isReferSpeed == true) {
            if ((speedValue.getText().toString().isEmpty())) {
                errorMessage.append(" 速度");
                isError = true;
            } else {
                speed = Double.parseDouble(speedValue.getText().toString());
            }
        }

        rg = (RadioGroup)findViewById(R.id.radioGroup);
        byte gender = 0;

        switch(rg.getCheckedRadioButtonId()){
            case R.id.maleRadioButton:
                gender = User.male;
                break;
            case R.id.femaleRadioButton:
                gender = User.female;
                break;

        }

        // get Time text
        hoursValue = (EditText)findViewById(R.id.editHour);
        minsValue = (EditText)findViewById(R.id.editMin);
        secsValue = (EditText)findViewById(R.id.editSec);


        if(hoursValue.getText().toString().isEmpty() && minsValue.getText().toString().isEmpty() && secsValue.getText().toString().isEmpty()){
            errorMessage.append( " 時間");
            isError = true;
        }
        hour = (hoursValue.getText().toString().isEmpty()) ? 0 : Integer.parseInt(hoursValue.getText().toString());
        min = (minsValue.getText().toString().isEmpty()) ? 0: Integer.parseInt(minsValue.getText().toString());
        sec = (secsValue.getText().toString().isEmpty()) ? 0: Integer.parseInt(secsValue.getText().toString());

        if(isError == true){
            errorMessage.append(" 格式");
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_LONG);
            toast.show();
            calorieScreen.setText("資料不足");
        }
        else {
            User tmpUser = new User(height, weight, age, gender);
            double result = 0;
            long time = ((hour * 3600) + (min * 60) + sec);

            if(isReferSpeed == true){
                result = CalorieCalculator.calculateCalorie(tmpUser, exerciseIndex, time, speed);
            }
            else {
                result = calculateCalorie(tmpUser, exerciseIndex, time);
            }

            String resultString = String.format("%.2f", result);
            calorieScreen.setText(String.valueOf(resultString) + "大卡");
        }
    }

    public void getIndexItem(){
        itemDetail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                exerciseWay = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }
}
