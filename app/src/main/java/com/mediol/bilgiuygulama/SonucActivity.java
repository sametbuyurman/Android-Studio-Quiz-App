package com.mediol.bilgiuygulama;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mediol.bilgiuygulama.viewModel.QuestionViewModel;

import java.util.HashMap;

public class SonucActivity extends AppCompatActivity {

    private QuestionViewModel viewModel;
    private TextView correctAnswer , wrongAnswer , notAnswered;
    private TextView percentTv;
    private ProgressBar scoreProgressbar;
    private String quizId;
    private Button homeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())).get(QuestionViewModel.class);

        correctAnswer = findViewById(R.id.correctAnswerTv);
        wrongAnswer = findViewById(R.id.wrongAnswersTv);
        notAnswered = findViewById(R.id.notAnsweredTv);
        percentTv = findViewById(R.id.resultPercentageTv);
        scoreProgressbar = findViewById(R.id.resultCoutProgressBar);
        homeBtn = findViewById(R.id.home_btn);

        // QuizActivity'den gelen verileri al
        quizId = getIntent().getStringExtra("quizId");
        if (quizId != null) {
            viewModel.setQuizId(quizId);
        } else {
            Toast.makeText(this, "hata", Toast.LENGTH_SHORT).show();
        }

        viewModel.getResults();
        viewModel.getResultMutableLiveData().observe(this, new Observer<HashMap<String, Long>>() {
            @Override
            public void onChanged(HashMap<String, Long> stringLongHashMap) {

                Long correct = stringLongHashMap.get("correct");
                Long wrong = stringLongHashMap.get("wrong");
                Long noAnswer = stringLongHashMap.get("notAnswered");

                correctAnswer.setText(correct.toString());
                wrongAnswer.setText(wrong.toString());
                notAnswered.setText(noAnswer.toString());

                Long total = correct + wrong + noAnswer;
                Long percent = (correct * 100) / total;

                percentTv.setText(String.valueOf(percent));
                scoreProgressbar.setProgress(percent.intValue());

            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeintent = new Intent(SonucActivity.this, ListActivity.class);
                startActivity(homeintent);
                finish();

            }
        });




    }}

