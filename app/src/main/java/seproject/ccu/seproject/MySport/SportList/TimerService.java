package seproject.ccu.seproject.MySport.SportList;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;

import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {

    private Timer timer;
    private TimerTask timerTask;
    long seconds = 0;

    @NonNull
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        super.onStartCommand(intent, flags, startID);
        startTimer();
        return START_STICKY;
    }

    public void startTimer(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                seconds++;
                Intent intent = new Intent("timer");
                intent.putExtra("time", seconds);
                sendBroadcast(intent);
            }
        };
        timer.schedule(timerTask, 1000, 1000);
    }

    public void stopTimertask(){
        if(timer != null){
            timer.cancel();
            timer = null;
            seconds = 0;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        stopTimertask();
    }
}
