package com.example.mobileapp.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileapp.Entity.CourseEntity;
import com.example.mobileapp.R;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    public List<CourseEntity> courseEntitiesList;
    public CourseAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    //recycler view
    public class CourseViewHolder extends RecyclerView.ViewHolder {
        public TextView recyclerViewItemLayout;
        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerViewItemLayout = itemView.findViewById(R.id.item_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    CourseEntity currentCourse = courseEntitiesList.get(position);
                    Intent intent = new Intent(context, CourseAddEdit.class);
                    intent.putExtra("courseId", currentCourse.getCourseId());
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,
                false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        CourseEntity currentCourse = courseEntitiesList.get(position);
        holder.recyclerViewItemLayout.setText(currentCourse.getCourseName());
    }

    @Override
    public int getItemCount() {
        return courseEntitiesList.size();
    }

    public CourseEntity getCourseAt(int position) {
        return courseEntitiesList.get(position);
    }

    public void courseSetter(List<CourseEntity> courses) {
        //
        courseEntitiesList = courses;
        notifyDataSetChanged();
    }
}