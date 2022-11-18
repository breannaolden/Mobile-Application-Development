package com.example.mobileapp.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileapp.Entity.AssessmentEntity;
import com.example.mobileapp.R;

import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    public List<AssessmentEntity> assessmentEntityList;

    public AssessmentAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }
    public class AssessmentViewHolder extends RecyclerView.ViewHolder {
        public TextView recyclerViewItemLayout;
        public AssessmentViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerViewItemLayout = itemView.findViewById(R.id.item_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    AssessmentEntity current = assessmentEntityList.get(position);
                    Intent intent = new Intent(context, AssessmentAddEdit.class);
                    intent.putExtra("assessmentId", current.getAssessmentId());
                    intent.putExtra("position", position);
                    intent.putExtra("assessCourseId", current.getAssessmentCourseId());
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,
                false);
        return new AssessmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {
        AssessmentEntity currentAssessment = assessmentEntityList.get(position);
        holder.recyclerViewItemLayout.setText(currentAssessment.getAssessmentTitle());
    }

    @Override
    public int getItemCount() {
        return assessmentEntityList.size();
    }

    public AssessmentEntity getAssessmentAt(int position) {
        return assessmentEntityList.get(position);
    }
    public void assessmentSetter (List<AssessmentEntity> assessments) {
        assessmentEntityList = assessments;
        notifyDataSetChanged();
    }
}