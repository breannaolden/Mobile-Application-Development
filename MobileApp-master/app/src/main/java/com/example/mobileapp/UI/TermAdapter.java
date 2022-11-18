package com.example.mobileapp.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileapp.Entity.TermEntity;
import com.example.mobileapp.R;

import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {

    public List<TermEntity> mTermsList;
    private Context context;
    private LayoutInflater inflater;

    public TermAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context=context;
    }

    public class TermViewHolder extends RecyclerView.ViewHolder {
        public TextView recyclerViewItemLayout;

        private TermViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerViewItemLayout = itemView.findViewById(R.id.item_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    TermEntity currentTerm = mTermsList.get(position);

                    Intent intent = new Intent(context, TermEdit.class);
                    intent.putExtra("termId", currentTerm.getTermId());
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });

        }

    }

    @NonNull
    @Override
    public TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new TermViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TermViewHolder holder, int position) {
        TermEntity currentTerm = mTermsList.get(position);
        holder.recyclerViewItemLayout.setText(currentTerm.getTermTitle());
    }

    @Override
    public int getItemCount() {
        return mTermsList.size();
    }

    public TermEntity getTermAt(int position) {
        return mTermsList.get(position);
    }

    public void termsSetter(List<TermEntity> terms) {
        mTermsList = terms;
        notifyDataSetChanged();
    }

}