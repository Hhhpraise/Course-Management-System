package com.depiro.courseselect.Teacher.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.depiro.courseselect.R;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    Context context;
    ArrayList<Student> students;

    public StudentAdapter(Context context, ArrayList<Student> students) {
        this.context = context;
        this.students = students;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_card, parent, false);
       ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(students.get(position).getName());
        holder.number.setText("Student Number : "+students.get(position).getNumber());
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, number;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.student_name);
            number = itemView.findViewById(R.id.student_no);
        }
    }
}
