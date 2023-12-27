package com.example.tests;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuestionNavigationAdapter extends RecyclerView.Adapter<QuestionNavigationAdapter.ViewHolder> {

    private List<Integer> questionNumbers;
    private OnQuestionClickListener onQuestionClickListener;

    public QuestionNavigationAdapter(List<Integer> questionNumbers, OnQuestionClickListener onQuestionClickListener) {
        this.questionNumbers = questionNumbers;
        this.onQuestionClickListener = onQuestionClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_number, parent, false);

        // Задайте ориентацию горизонтальную для элементов списка
        LinearLayoutManager layoutManager = new LinearLayoutManager(parent.getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = new RecyclerView(parent.getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Установите фон внутреннего RecyclerView
        recyclerView.setBackgroundResource(R.drawable.ic_launcher_background);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int questionNumber = questionNumbers.get(position);
        holder.bind(questionNumber);

        holder.itemView.setOnClickListener(v -> onQuestionClickListener.onQuestionClick(questionNumber));
    }

    @Override
    public int getItemCount() {
        return questionNumbers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView questionNumberTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionNumberTextView = itemView.findViewById(R.id.questionNumberTextView);
        }

        public void bind(int questionNumber) {
            questionNumberTextView.setText(String.valueOf(questionNumber));
        }
    }

    public interface OnQuestionClickListener {
        void onQuestionClick(int questionNumber);
    }
}


