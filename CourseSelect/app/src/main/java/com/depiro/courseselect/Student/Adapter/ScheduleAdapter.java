package com.depiro.courseselect.Student.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.depiro.courseselect.R;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    Context context;
    ArrayList<Course> courses;

    public ScheduleAdapter(Context context, ArrayList<Course> courses) {
        this.context = context;
        this.courses = courses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.s_course_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cname.setText(courses.get(position).getCname());
        holder.tname.setText("Teacher : "+courses.get(position).getTname());
        holder.hour.setText(courses.get(position).getCreditHour());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView cname, tname, hour;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cname = itemView.findViewById(R.id.student_name);
            tname = itemView.findViewById(R.id.student_no);
            hour = itemView.findViewById(R.id.course_credit);
        }
    }
}
