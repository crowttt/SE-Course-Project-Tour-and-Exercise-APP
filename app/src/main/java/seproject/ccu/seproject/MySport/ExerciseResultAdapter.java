package seproject.ccu.seproject.MySport;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import seproject.ccu.seproject.MySport.database.entity.SimpleCalendar;
import seproject.ccu.seproject.R;

import java.util.Locale;

public class ExerciseResultAdapter extends RecyclerView.Adapter<ExerciseResultAdapter.ViewHolder> {
    private Cursor cursor;

    public ExerciseResultAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public int getItemCount() {
        return this.cursor.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView time;
        private TextView speed;
        private TextView calorie;
        private TextView exerciseName;
        private TextView speedTitle;
        private ImageView exerciseIcon;


        public ViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.result_in_list_view_date);
            exerciseName = view.findViewById(R.id.result_in_list_view_exerciseName);
            time = view.findViewById(R.id.result_in_list_view_time);
            speed = view.findViewById(R.id.result_in_list_view_speed);
            speedTitle = view.findViewById(R.id.result_in_list_view_speed_title);
            calorie = view.findViewById(R.id.result_in_list_view_calorie);
            exerciseIcon = view.findViewById(R.id.result_in_list_view_icon);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_result_in_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {

            SimpleCalendar date = new SimpleCalendar(cursor.getInt(1));
            long time = cursor.getLong(4);
            byte exerciseIndex = (byte) cursor.getInt(3);

            holder.date.setText(date.toString());
            holder.exerciseName.setText(CalorieCalculator.getExerciseName(exerciseIndex));
            holder.time.setText(String.format(Locale.TAIWAN, "%02d:%02d:%02d", (time / 3600), ((time % 3600) / 60), ((time % 3600) % 60)));

            holder.exerciseIcon.setImageResource(CalorieCalculator.getExerciseIconID(exerciseIndex));
            if (exerciseIndex >= CalorieCalculator.biking && exerciseIndex <= CalorieCalculator.jogging) {
                holder.speed.setText(String.valueOf(cursor.getDouble(5)));
            } else {
                holder.speed.setVisibility(View.GONE);
                holder.speedTitle.setVisibility(View.GONE);
            }

            holder.calorie.setText(String.valueOf(cursor.getDouble(2)));
        }
    }
}
