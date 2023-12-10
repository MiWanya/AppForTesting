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

    public static boolean isPartialMatch(UserAnswer userAnswer1, UserAnswer userAnswer2) {
        List<String> list1 = userAnswer1.getSelectedOptions();
        List<String> list2 = userAnswer2.getSelectedOptions();

        for (String item : list1) {
            if (list2.contains(item)) {
                return true;
            }
        }
        return false;
    }

}
