package com.example.tests;

import java.util.List;

public class QuestionInfo {
    private String QuestionText;
    private List<Boolean> SelectedOptions;
    private List<String> CorrectAnswer;
    private List<String> Answer;
    private boolean AnswerGiven;
    private String Result;
    public QuestionInfo (String QuestionText, List<Boolean> SelectedOptions, List<String> CorrectAnswer, List<String> Answer, boolean AnswerGiven, String Result){
        this.QuestionText = QuestionText;
        this.SelectedOptions = SelectedOptions;
        this.CorrectAnswer = CorrectAnswer;
        this.Answer = Answer;
        this.AnswerGiven = AnswerGiven;
        this.Result = Result;
    }
    public boolean isAnswerGiven() {
        return AnswerGiven;
    }
    public void setAnswerGiven(boolean answerGiven) {
        this.AnswerGiven = answerGiven;
    }
    public List<Boolean> GetSelectedOptions() {
        return SelectedOptions;
    }
    public void setSelectedOptions(List<Boolean> SelectedOptions) {
        this.SelectedOptions = SelectedOptions;
    }
    public List<String> getAnswer() {
        return Answer;
    }
    public String getQuestionText() {
        return QuestionText;
    }
    public List<String> getCorrectAnswer() {
        return CorrectAnswer;
    }
    public void setAnswer(List<String> answer) {
        Answer = answer;
    }
    public void setCorrectAnswer(List<String> correctAnswer) {
        CorrectAnswer = correctAnswer;
    }
    public void setQuestionText(String questionText) {
        QuestionText = questionText;
    }
    public void setResult (String Res){
        Result = Res;
    }
    public String getResult() {
        return Result;
    }
}
