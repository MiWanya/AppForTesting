package com.example.tests;

import java.util.List;

public class UserAnswer {
    private int questionId;
    private List<String> selectedOptions;

    public UserAnswer(int questionId, List<String> selectedOptions) {
        this.questionId = questionId;
        this.selectedOptions = selectedOptions;
    }

    public int getQuestionId() {
        return questionId;
    }

    public List<String> getSelectedOptions() {
        return selectedOptions;
    }
}
