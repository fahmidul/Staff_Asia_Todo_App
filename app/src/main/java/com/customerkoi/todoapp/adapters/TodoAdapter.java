package com.customerkoi.todoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ocufoxtech.firebasetodoruia.R;
import com.customerkoi.todoapp.models.Todo;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.MyViewHolder> {

    public interface OnButtonClickListener {
        public void onUpdateClicked(Todo todo);

        public void onDeleteClicked(Todo todo);

        public void onCompleteClicked(Todo todo);

        public void onDetailsClicked(Todo todo);
    }

    List<Todo> todoList;
    OnButtonClickListener listener;
    Context context;

    public TodoAdapter(List<Todo> todoList, Context context, OnButtonClickListener listener) {
        this.todoList = todoList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Todo todo = todoList.get(position);
        String title = todo.getTitle();
        String category = todo.getCategory_name();
        holder.tvTaskTitle.setText(title);
        holder.tvTaskCategory.setText(category);
        if (todoList.get(position).getStatus().equals("0")) {
            holder.tvTaskTitle.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            holder.tvTaskTitle.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class
    MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaskTitle, tvTaskCategory, tvSeeDetails, tvCompleteTask;
        ImageView btnUpdate, btnDelete;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvTaskTitle = itemView.findViewById(R.id.tvTaskTitle);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            tvTaskCategory = itemView.findViewById(R.id.tvTaskCategory);
            tvSeeDetails = itemView.findViewById(R.id.tvSeeDetails);
            tvCompleteTask = itemView.findViewById(R.id.tvCompleteTask);

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUpdateClicked(todoList.get(getAdapterPosition()));
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteClicked(todoList.get(getAdapterPosition()));
                }
            });

            tvSeeDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDetailsClicked(todoList.get(getAdapterPosition()));

                }
            });

            tvCompleteTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCompleteClicked(todoList.get(getAdapterPosition()));

                }
            });
        }
    }
}
