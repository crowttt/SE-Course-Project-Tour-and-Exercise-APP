package seproject.ccu.seproject.MySport.SportList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import seproject.ccu.seproject.MySport.CalorieCalculator;
import seproject.ccu.seproject.R;

public class SportListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences spref = getSharedPreferences("Mode", Context.MODE_PRIVATE);
        if (spref.getBoolean("IS_DARK", false)) {
            setTheme(R.style.darkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_list);
    }

    public void onClickWalk(View view){
        Intent intent = new Intent(this, DoSportActivity.class);
        intent.putExtra("sportItem", CalorieCalculator.walking);
        startActivity(intent);
        finish();
    }

    public void onClickRun(View view){
        Intent intent = new Intent(this, DoSportActivity.class);
        intent.putExtra("sportItem", CalorieCalculator.jogging);
        startActivity(intent);
        finish();
    }

    public void onClickBike(View view){
        Intent intent = new Intent(this, DoSportActivity.class);
        intent.putExtra("sportItem", CalorieCalculator.biking);
        startActivity(intent);
        finish();
    }

    public void onClickBadminton(View view){
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.badminton_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(SportListActivity.this, DoSportActivity.class);
                switch(item.getItemId()){
                    // 單 / 雙打
                    case R.id.badminton_practice:
                        intent.putExtra("sportItem", CalorieCalculator.badminton);
                        startActivity(intent);
                        finish();
                        return true;

                    // 競賽
                    case R.id.badminton_contest:
                        intent.putExtra("sportItem", (byte)(CalorieCalculator.badminton + 1));
                        startActivity(intent);
                        finish();
                        return true;

                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    public void onClickTennis(View view){
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.tennis_menu, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(SportListActivity.this, DoSportActivity.class);
                switch(item.getItemId()){
                    // 單打
                    case R.id.tennis_one:
                        intent.putExtra("sportItem", CalorieCalculator.tennis);
                        startActivity(intent);
                        finish();
                        return true;

                    // 雙打
                    case R.id.tennis_double:
                        intent.putExtra("sportItem", (byte)(CalorieCalculator.tennis + 1));
                        startActivity(intent);
                        finish();
                        return true;

                    // 競賽
                    case R.id.tennis_contest:
                        intent.putExtra("sportItem", (byte)(CalorieCalculator.tennis + 2));
                        startActivity(intent);
                        finish();
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    public void onClickPingpong(View view){
        Intent intent = new Intent(this, DoSportActivity.class);
        intent.putExtra("sportItem", CalorieCalculator.tableTennis);
        startActivity(intent);
        finish();
    }
    //
    public void onClickBasketball(View view){
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.basketball_menu, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(SportListActivity.this, DoSportActivity.class);
                switch(item.getItemId()){
                    // 練習賽
                    case R.id.basketball_practice:
                        intent.putExtra("sportItem", CalorieCalculator.basketball);
                        startActivity(intent);
                        finish();
                        return true;

                    // 投籃練習
                    case R.id.basketball_shoot:
                        intent.putExtra("sportItem", (byte)(CalorieCalculator.basketball + 1));
                        startActivity(intent);
                        finish();
                        return true;

                    // 競賽
                    case R.id.basketball_contest:
                        intent.putExtra("sportItem", (byte)(CalorieCalculator.basketball + 2));
                        startActivity(intent);
                        finish();
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    public void onClickYoga(View view){
        Intent intent = new Intent(this, DoSportActivity.class);
        intent.putExtra("sportItem", CalorieCalculator.yoga);
        startActivity(intent);
        finish();
    }
    //
    public void onClickGymnastic(View view){
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.gymnastic_menu, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(SportListActivity.this, DoSportActivity.class);
                switch(item.getItemId()){
                    // 水中有氧
                    case R.id.gymnastic_water:
                        intent.putExtra("sportItem", CalorieCalculator.aerobic);
                        startActivity(intent);
                        finish();
                        return true;

                    // 有氧運動
                    case R.id.gymnastic_oxygen:
                        intent.putExtra("sportItem", (byte)(CalorieCalculator.aerobic + 1));
                        startActivity(intent);
                        finish();
                        return true;

                    default:
                        return false;
                }
            }
        });
    }
}