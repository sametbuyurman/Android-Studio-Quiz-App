package com.mediol.bilgiuygulama;

public class QuizResults {

    private int correct;
    private int wrong;
    private int notAnswered;

    public QuizResults(int correct, int wrong, int notAnswered) {
        this.correct = correct;
        this.wrong = wrong;
        this.notAnswered = notAnswered;
    }
}
