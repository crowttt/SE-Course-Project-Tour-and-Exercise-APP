package seproject.ccu.seproject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import seproject.ccu.seproject.MySport.database.entity.User;

public class PointMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_menu);

        TextView pointMessage = (TextView)findViewById(R.id.pointScreen);

        User user = User.getInstance(this);
        pointMessage.setText(String.valueOf(user.getPoint()));
    }
}
