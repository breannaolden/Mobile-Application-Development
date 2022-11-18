package com.example.mobileapp.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileapp.Entity.InstructorEntity;
import com.example.mobileapp.R;

import java.util.List;

public class InstructorAdapter extends RecyclerView.Adapter<InstructorAdapter.InstructorViewHolder> {

    public List<InstructorEntity> instructorEntityList;
    private Context context;
    private LayoutInflater inflater;
    public InstructorAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }
    public class InstructorViewHolder extends RecyclerView.ViewHolder {
        public TextView recyclerViewItemLayout;

        public InstructorViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerViewItemLayout = itemView.findViewById(R.id.item_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    InstructorEntity current = instructorEntityList.get(position);

                    Intent intent = new Intent(context, InstructorAddEdit.class);
                    intent.putExtra("InstructorId", current.getInstructorId());
                    intent.putExtra("position", position);
                    intent.putExtra("assessCourseId", current.getInstructorCourseId());
                    context.startActivity(intent);

                }
            });
        }
    }

    @NonNull
    @Override
    public InstructorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new InstructorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructorViewHolder holder, int position) {
        InstructorEntity currentInstructor = instructorEntityList.get(position);
        holder.recyclerViewItemLayout.setText(currentInstructor.getInstructorName());
    }

    @Override
    public int getItemCount() {
        return instructorEntityList.size();
    }

    public InstructorEntity getInstructorAt(int position) {
        return instructorEntityList.get(position);
    }

    public void instructorSetter(List<InstructorEntity> instructor) {
        instructorEntityList = instructor;
        notifyDataSetChanged();
    }

}