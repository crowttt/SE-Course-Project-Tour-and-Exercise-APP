package seproject.ccu.seproject.MySport;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import seproject.ccu.seproject.R;


public class BMICalculatorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences spref = getSharedPreferences("Mode", Context.MODE_PRIVATE);
        if (spref.getBoolean("IS_DARK", false)) {
            setTheme(R.style.darkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_calculator);

    }

    public void onClickConfirm(View view){
        StringBuffer errorMessage = new StringBuffer("請輸入 ");

        double height = 0, weight = 0;

        // 取得性別 selection 的 string
        Spinner sexual = (Spinner) findViewById(R.id.sexualSelection);
        String sexualType = String.valueOf(sexual.getSelectedItem());

        // 取得身高
        EditText heightNumAccessor = (EditText) findViewById(R.id.heightNum);
        if(isEmpty(heightNumAccessor)) errorMessage.append("身高 ");

        // 取得體重
        EditText weightNumAccessor = (EditText) findViewById(R.id.weightNum);
        if(isEmpty(weightNumAccessor)) errorMessage.append("體重");

        try{
            height = Double.parseDouble(heightNumAccessor.getText().toString());
            weight = Double.parseDouble(weightNumAccessor.getText().toString());

            // 將 BMI 運算結果傳至Screen
            TextView BMIscreen = (TextView) findViewById(R.id.BMIscreen);
            // 取至小數點第二位
            double BMI = Double.parseDouble(String.format("%.2f",(double)(weight / ((height / 100) * (height / 100)))));
            String BMIString = Double.toString(BMI);
            BMIscreen.setText(BMIString);

            diagonseBMI(BMI, sexualType);
        }
        catch(Exception e){
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_LONG);
            toast.show();
        }


    }

    private boolean isEmpty(EditText numAccessor){
        if(numAccessor.getText().toString().isEmpty()){
            return true;
        }
        return false;
    }

    private void diagonseBMI(double BMI, String sexualType){
        TextView resultScreen = (TextView) findViewById(R.id.resultScreen);

        if(BMI < 18.5){
            resultScreen.setText("體重過輕");
            resultScreen.setTextColor(this.getResources().getColor(R.color.colorLight));
        }
        else if(BMI >= 18.5 && BMI < 24){
            resultScreen.setText("正常範圍");
            resultScreen.setTextColor(this.getResources().getColor(R.color.colorNormal));
        }
        else if(BMI >= 24 && BMI < 27){
            resultScreen.setText("體重稍重");
            resultScreen.setTextColor(this.getResources().getColor(R.color.colorHeavy));
        }
        else if(BMI >= 27 & BMI < 30){
            resultScreen.setText("輕度肥胖");
            resultScreen.setTextColor(this.getResources().getColor(R.color.colorLittleFat));
        }
        else if(BMI >= 30 & BMI < 35){
            resultScreen.setText("中度肥胖");
            resultScreen.setTextColor(this.getResources().getColor(R.color.colorMiddleFat));
        }
        else{
            resultScreen.setText("重度肥胖");
            resultScreen.setTextColor(this.getResources().getColor(R.color.colorVeryFat));
        }

    }
}
