package seproject.ccu.seproject.MySport.SportList;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import seproject.ccu.seproject.MySport.CalorieCalculator;
import seproject.ccu.seproject.MySport.ExerciseItem;
import seproject.ccu.seproject.MySport.database.entity.ExerciseResult;
import seproject.ccu.seproject.MySport.database.entity.User;
import seproject.ccu.seproject.R;

import static seproject.ccu.seproject.MySport.ExerciseItem.getItemString;

public class DoSportActivity extends Activity {
    User user;
    private ImageView startButton, finishButton;
    private TextView sportItemText, locationMessage, speedMessage, timerMessage, calorieMessage;
    private BroadcastReceiver broadcastReceiver, timerReceiver;
    private double calorie, distance, speed, lastDistance = 0;
    private int hours = 0, mins = 0, secs = 0;
    private long tmpSeconds;
    private byte exerciseIndex;
    private boolean isServiceRunning = false;
    private final int NOTIFICATION_ID = 426;

    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (exerciseIndex <= 30) {
                        // 接收位置變化之經緯度，可開始計算距離
                        distance = intent.getDoubleExtra("distance", 0.00);
                        String distanceString = String.format("%.2f", distance / 1000);
                        locationMessage.setText(distanceString);

                        // timer 寫好改成五秒更新 speed 一次
                        speed = (distance - lastDistance) * 3.6;
                        String speedString = String.format("%.2f", speed);
                        speedMessage.setText(speedString);
                        lastDistance = distance;
                    }
                }
            };
        }

        if(timerReceiver == null) {
            timerReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // 每次進行計算 or 紀錄每次結果再加 1
                    tmpSeconds = intent.getLongExtra("time", 0);
                        hours = (int) tmpSeconds / 3600;
                        mins = (int) (tmpSeconds % 3600) / 60;
                        secs = (int) (tmpSeconds % 3600) % 60;
                    String timerString = String.format("%02d:%02d:%02d", hours, mins, secs);
                    timerMessage.setText(timerString);

                    if(tmpSeconds % 60 == 0) {

                        if(exerciseIndex <= 30) {
                            calorie += CalorieCalculator.calculateCalorie(user, exerciseIndex, 60, (distance / 1000) * 60);
                        }
                        else{
                            calorie += CalorieCalculator.calculateCalorie(user, exerciseIndex, 60);
                        }

                        calorieMessage.setText(String.format("%.2f", calorie));
                    }
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location"));
        registerReceiver(timerReceiver, new IntentFilter("timer"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
        if(timerReceiver != null){
            unregisterReceiver(timerReceiver);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences spref = getSharedPreferences("Mode", Context.MODE_PRIVATE);
        if (spref.getBoolean("IS_DARK", false)) {
            setTheme(R.style.darkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_sport);
        user = User.getInstance(this);
        sportItemText = (TextView)findViewById(R.id.sportItemText);
        // 宣告 開始 & 結束 按鈕
        startButton = (ImageView) findViewById(R.id.startSportButton);
        finishButton = (ImageView) findViewById(R.id.finishSportButton);

        // 資訊顯示區域
        locationMessage = (TextView) findViewById(R.id.distanceScreen);
        speedMessage = (TextView) findViewById(R.id.speedScreen);
        timerMessage = (TextView) findViewById(R.id.timerScreen);
        calorieMessage = (TextView) findViewById(R.id.calorieScreen);

        Intent sportItem = getIntent();
        exerciseIndex = sportItem.getByteExtra("sportItem", (byte)0);
        //sportItemText.setText(String.valueOf(exerciseIndex));
        sportItemText.setText(getItemString(exerciseIndex));
        // 取得選擇的運動項目
        if(exerciseIndex > CalorieCalculator.jogging){
            ImageView speedImage = (ImageView) findViewById(R.id.speedImage);
            ImageView distanceImage = (ImageView) findViewById(R.id.distanceImage);
            TextView speedText = (TextView)findViewById(R.id.speedText);
            TextView distanceText = (TextView)findViewById(R.id.distanceText);
                    // 顯示距離、速度區域 設為不可見
            speedImage.setVisibility(View.INVISIBLE);
            distanceImage.setVisibility(View.INVISIBLE);
            speedMessage.setVisibility(View.INVISIBLE);
            locationMessage.setVisibility(View.INVISIBLE);
            speedText.setVisibility(View.INVISIBLE);
            distanceText.setVisibility(View.INVISIBLE);
        }

        if(!runtimePermission()){
            enable_Buttons();
        }
    }

    private void enable_Buttons() {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getApplicationContext()  => service 生命週期與應用相同
                // activity.this            => service 生命週期與該activity相同
                if (isServiceRunning == false) {
                    isServiceRunning = true;
                    Intent locationIntent = new Intent(getApplicationContext(), LocationService.class);
                    startService(locationIntent);

                    Intent timerIntent = new Intent(getApplicationContext(), TimerService.class);
                    startService(timerIntent);
                }
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isServiceRunning == true) {
                    if (tmpSeconds < 600) {
                        // 確認是否已達十分鐘的運動標準
                        showNotifyMessage();
                    } else {
                        isServiceRunning = false;
                        Intent locationIntent = new Intent(getApplicationContext(), LocationService.class);
                        stopService(locationIntent);

                        Intent timerIntent = new Intent(getApplicationContext(), TimerService.class);
                        stopService(timerIntent);

                        // 跳出視窗顯示運動結果
                        showExerciseResult();
                    }
                }
            }
        });
    }

    private boolean runtimePermission() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

            return true;
        }
        return false;
    }

    // 在進行要求位置時，先檢查是否有讀取位置之權限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // 將按鈕與 service 進行綁定
                enable_Buttons();
            }
            else{
                runtimePermission();
            }
        }
    }

    private void showNotifyMessage(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setIcon(R.drawable.ic_warning);
        dialog.setTitle("本次運動記錄將不會被存取\n");
        dialog.setMessage("根據衛福部國民健康署建議，運動應持續達十分鐘以上才有效。\n" +
                          "您本次運動未達十分鐘，建議您繼續努力！\n" +
                          "是否確定要結束本次運動？\n")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isServiceRunning = false;
                        if (exerciseIndex <= 30) {
                            Intent locationIntent = new Intent(getApplicationContext(), LocationService.class);
                            stopService(locationIntent);
                        }

                        Intent timerIntent = new Intent(getApplicationContext(), TimerService.class);
                        stopService(timerIntent);

                        // 跳出視窗顯示運動結果
                        showExerciseResult();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dialog.show();
    }

    private void showExerciseResult(){
        ExerciseResult exerciseResult;
        // 運動結果
        String item;
        StringBuffer result = new StringBuffer("\n運動項目 - ");
        item = getItemString(exerciseIndex);
        result.append(item);

        result.append("\n運動時間 - ");
        item = ExerciseItem.getTimeString(hours, mins, secs);
        result.append(item);

        if(exerciseIndex <= 30){
            result.append("\n距離 - ");
            result.append(String.format("%.2f", distance / 1000));
            result.append(" km");

            result.append("\n平均速度 - ");
            result.append(String.format("%.2f", ((distance / tmpSeconds) * 3.6)));
            result.append(" km/hr");
        }

        result.append("\n消耗熱量 - ");
        if(exerciseIndex <= 30){
            calorie = CalorieCalculator.calculateCalorie(user, exerciseIndex, tmpSeconds, ((distance / tmpSeconds) * 3.6));
            exerciseResult = new ExerciseResult(calorie, exerciseIndex, tmpSeconds, (distance / tmpSeconds) * 3.6);
        }
        else{
            calorie = CalorieCalculator.calculateCalorie(user, exerciseIndex, tmpSeconds);
            exerciseResult = new ExerciseResult(calorie, exerciseIndex, tmpSeconds);
        }
        user.addBurnedCalorie(calorie);
        user.addExerciseTime(tmpSeconds);
        user.addPoint((int)(CalorieCalculator.getMetabolicEquivalentOfTask(exerciseIndex, (distance / tmpSeconds) * 3.6) * (tmpSeconds / 600)));
        user.updateData(this);

        result.append(String.format("%.2f", calorie));
        result.append(" 大卡");

        // 建立視窗
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setIcon(R.drawable.ic_description);
        dialog.setTitle("\n運動結果");
        dialog.setMessage(result);
        // 更新資料庫
        if(tmpSeconds >= 600){
            exerciseResult.insertToDatabase(this);
        }

        dialog.setPositiveButton("完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 回到運動主頁
                finish();
            }
        });
        dialog.show();
    }
}