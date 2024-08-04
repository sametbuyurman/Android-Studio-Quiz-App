package com.mediol.bilgiuygulama;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mediol.bilgiuygulama.Model.QuestionModel;
import com.mediol.bilgiuygulama.viewModel.QuestionViewModel;

import java.util.HashMap;
import java.util.List;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    private QuestionViewModel viewModel;
    private ProgressBar progressBar;
    private Button option1Btn, option2Btn, option3Btn, option4Btn, nextQueBtn;
    private TextView questionTv, ansFeedBackTv, questionNumberTv, timerCountTv;
    private ImageView closeQuizBtn;
    private String quizId;
    private long totalQuestions;
    private int currentQuesNo = 0;
    private boolean canAnswer = false;
    private int notAnswerd=0;

    private long timer;
    private CountDownTimer countDownTimer;

    private int correctAnswer = 0;
    private int wrongAnswer = 0;
    private String answer = "";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())).get(QuestionViewModel.class);

        closeQuizBtn = findViewById(R.id.imageView3);
        option1Btn = findViewById(R.id.option1Btn);
        option2Btn = findViewById(R.id.option2Btn);
        option3Btn = findViewById(R.id.option3Btn);
        option4Btn = findViewById(R.id.option4Btn);
        nextQueBtn = findViewById(R.id.nextQueBtn);
        ansFeedBackTv = findViewById(R.id.ansFeedbackTv);
        questionTv = findViewById(R.id.quizQuestionTv);
        timerCountTv = findViewById(R.id.countTimeQuiz);
        questionNumberTv = findViewById(R.id.quizQuestionsCount);
        progressBar = findViewById(R.id.quizCoutProgressBar);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("quizId")) {
            quizId = intent.getStringExtra("quizId");
            totalQuestions = intent.getLongExtra("totalQueCount", 0);

            viewModel.setQuizId(quizId);
        }
        viewModel.setQuizId(quizId);
        option1Btn.setOnClickListener(this);
        option2Btn.setOnClickListener(this);
        option3Btn.setOnClickListener(this);
        option4Btn.setOnClickListener(this);
        nextQueBtn.setOnClickListener(this);

        closeQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent closeIntent = new Intent(QuizActivity.this,ListActivity.class);
                startActivity(closeIntent);
            }
        });

        loadData(); // loadData metodunu onCreate içinde çağırın

    }

    private void loadData() {
        enableOptions();
        loadQuestions(1); // loadQuestions metodunu çağırın ve ilk soruyu yükleyin
    }

    private void enableOptions() {
        option1Btn.setVisibility(View.VISIBLE);
        option2Btn.setVisibility(View.VISIBLE);
        option3Btn.setVisibility(View.VISIBLE);
        option4Btn.setVisibility(View.VISIBLE);

        option1Btn.setEnabled(true);
        option2Btn.setEnabled(true);
        option3Btn.setEnabled(true);
        option4Btn.setEnabled(true);

        ansFeedBackTv.setVisibility(View.INVISIBLE);
        nextQueBtn.setVisibility(View.INVISIBLE);
    }

    private void loadQuestions(int i) {
        currentQuesNo =i;
        viewModel.getQuestionMutableLiveData().observe(this, new Observer<List<QuestionModel>>() {
            @Override
            public void onChanged(List<QuestionModel> questionModels) {
                // Güncellenmiş LiveData gözlemleyicisi içinde soruların yüklenmesi
                questionTv.setText(String.valueOf(currentQuesNo)+") "+questionModels.get(i - 1).getQuestion());
                option1Btn.setText(questionModels.get(i - 1).getOption_a());
                option2Btn.setText(questionModels.get(i - 1).getOption_b());
                option3Btn.setText(questionModels.get(i - 1).getOption_c());
                option4Btn.setText(questionModels.get(i - 1).getOption_d());
                timer=questionModels.get(i-1).getTimer();
                answer = questionModels.get(i-1).getAnswer();

                questionNumberTv.setText(String.valueOf(currentQuesNo));
                startTimer();
            }
        });


        canAnswer=true;
    }

    private void startTimer(){
        timerCountTv.setText(String.valueOf(timer));
        progressBar.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(timer*1000,1000) {
            @Override
            public void onTick(long l) {
                timerCountTv.setText(l/1000+"");

                long percent = l / (timer * 10);
                progressBar.setProgress((int) percent); // long türünü int türüne dönüştürme

            }

            @Override
            public void onFinish() {
                canAnswer=false;
                ansFeedBackTv.setText("Süre Doldu !! Cevap Seçilmedi");
                notAnswerd ++;
                showNextBtn();

            }
        }.start();

    }

    private void showNextBtn() {
        if(currentQuesNo == totalQuestions){
            nextQueBtn.setText("Bitir");
            nextQueBtn.setEnabled(true);
            nextQueBtn.setVisibility(View.VISIBLE);
        }else{
            nextQueBtn.setVisibility(View.VISIBLE);
            nextQueBtn.setEnabled(true);
            ansFeedBackTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.option1Btn) {
            verifyAnswer(option1Btn);
        } else if (view.getId() == R.id.option2Btn) {
            verifyAnswer(option2Btn);
        } else if (view.getId() == R.id.option3Btn) {
            verifyAnswer(option3Btn);
        } else if (view.getId() == R.id.option4Btn) {
            verifyAnswer(option4Btn);
        } else if (view.getId()==R.id.nextQueBtn) {
            if (currentQuesNo==totalQuestions){
                submitResults();
            }else{
                currentQuesNo ++;
                loadQuestions(currentQuesNo);
                resetOptions();
            }
            
        }

    }

    private void resetOptions() {
        ansFeedBackTv.setVisibility(View.INVISIBLE);
        nextQueBtn.setVisibility(View.VISIBLE);
        nextQueBtn.setEnabled(false);
        option1Btn.setBackground(ContextCompat.getDrawable(this, R.color.light_sky));
        option2Btn.setBackground(ContextCompat.getDrawable(this, R.color.light_sky));
        option3Btn.setBackground(ContextCompat.getDrawable(this, R.color.light_sky));
        option4Btn.setBackground(ContextCompat.getDrawable(this, R.color.light_sky));
    }


    private void submitResults() {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("correct", correctAnswer);
        resultMap.put("wrong", wrongAnswer);
        resultMap.put("notAnswered", totalQuestions - currentQuesNo); // Cevaplanmamış soruları ekleyin

        viewModel.addResults(resultMap);
        Intent submitIntent = new Intent(this, SonucActivity.class);
        submitIntent.putExtra("quizId", quizId); // Quiz kimliğini iletilmesi
        startActivity(submitIntent);
    }


    private void verifyAnswer(Button button){
        if (canAnswer){
            if (answer.equals(button.getText())){
                button.setBackground(ContextCompat.getDrawable(this,R.color.green));
                correctAnswer++;
                    ansFeedBackTv.setText("Doğru Cevap");
            }else{
                button.setBackground(ContextCompat.getDrawable(this,R.color.red));
                wrongAnswer++;
                ansFeedBackTv.setText("Yanlış Cevap \nDoğru Cevap :"+answer);
            }
        }
        canAnswer=false;
        countDownTimer.cancel();
        showNextBtn();



    }
}
