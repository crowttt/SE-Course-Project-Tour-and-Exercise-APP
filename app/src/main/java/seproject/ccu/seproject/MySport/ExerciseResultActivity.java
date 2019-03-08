package seproject.ccu.seproject.MySport;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import seproject.ccu.seproject.MySport.database.entity.ExerciseResultReadHelper;
import seproject.ccu.seproject.R;

public class ExerciseResultActivity extends AppCompatActivity {
    private Cursor cursor;
    private RecyclerView resultCycler;
    private ExerciseResultReadHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences spref = getSharedPreferences("Mode", Context.MODE_PRIVATE);
        if (spref.getBoolean("IS_DARK", false)) {
            setTheme(R.style.darkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_result);

        helper = new ExerciseResultReadHelper(this);
        helper.generateTestData(this);
        cursor = helper.getCursorOfAllResults();

        // spinner 的東西
        Spinner timeUnit = (Spinner)findViewById(R.id.time_unit_spinner);
        timeUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0: //本日
                        cursor = helper.getCursorOfTodayResults();
                        break;
                    case 1: //本周
                        cursor = helper.getCursorOfThisWeekResults();
                        break;
                    case 2: //本月
                        cursor = helper.getCursorOfThisMonthResults();
                        break;
                    case 3: //今年
                        cursor = helper.getCursorOfThisYearResults();
                        break;
                    case 4: // All
                        cursor = helper.getCursorOfAllResults();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){
            }
        });


        resultCycler = (RecyclerView) findViewById(R.id.result_list);
        ExerciseResultAdapter adapter = new ExerciseResultAdapter(cursor);
        resultCycler.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        resultCycler.setLayoutManager(layoutManager);
    }

    public void onClickSend(View view){
        recreate();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        helper.close();
    }
}
