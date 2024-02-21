package com.depiro.courseselect.Teacher.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.depiro.courseselect.MyClickInterface;
import com.depiro.courseselect.R;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    Context context;
    ArrayList<Course> courses;
    MyClickInterface myClickInterface;

    public CourseAdapter(Context context, ArrayList<Course> courses, MyClickInterface myClickInterface) {
        this.context = context;
        this.courses = courses;
        this.myClickInterface = myClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.t_course_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(courses.get(position).getName());
        holder.hour.setText(courses.get(position).getCreditHour());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, hour;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.student_name);
            hour = itemView.findViewById(R.id.course_credit);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myClickInterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
