package com.example.tests;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuestionNavigationAdapter extends RecyclerView.Adapter<QuestionNavigationAdapter.ViewHolder> {

    private List<QuestionItem> questionItems;
    private OnQuestionClickListener onQuestionClickListener;

    public QuestionNavigationAdapter(List<QuestionItem> questionItems, OnQuestionClickListener onQuestionClickListener) {
        this.questionItems = questionItems;
        this.onQuestionClickListener = onQuestionClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_number, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuestionItem questionItem = questionItems.get(position);

        // Устанавливаем цвет текста вопроса
        holder.questionNumberTextView.setTextColor(questionItem.getTextColor());
        holder.questionNumberTextView.setText(String.valueOf(questionItem.getQuestionNumber()));

        holder.itemView.setOnClickListener(v -> onQuestionClickListener.onQuestionClick(questionItem));
    }

    @Override
    public int getItemCount() {
        return questionItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView questionNumberTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionNumberTextView = itemView.findViewById(R.id.questionNumberTextView);
        }
    }

    public interface OnQuestionClickListener {
        void onQuestionClick(QuestionItem questionItem);
    }
    public void changeColorForItem(int position, int newColor) {
        QuestionItem questionItem = questionItems.get(position);
        questionItem.setTextColor(newColor);
        notifyItemChanged(position);
    }
    public List<QuestionItem> getQuestionItems() {
        return questionItems;
    }
}



