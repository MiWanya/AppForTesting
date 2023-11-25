package com.example.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserAnswer {
    private int questionId;
    private List<String> selectedOptions;

    public  UserAnswer(){
        selectedOptions = new ArrayList<>();
    };
    public UserAnswer(List<String> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    public void setUserOption(String selectedOption) {
        this.selectedOptions.add(selectedOption);
    }

    public int getQuestionId() {
        return questionId;
    }

    public List<String> getSelectedOptions() {
        return selectedOptions;
    }

    @Override
    public String toString() {
        return "UserAnswer{" +
                ", selectedOptions=" + selectedOptions +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        UserAnswer otherAnswer = (UserAnswer) obj;

        // Сравниваем содержимое объектов
        return Objects.equals(this.selectedOptions, otherAnswer.selectedOptions);
    }

}
