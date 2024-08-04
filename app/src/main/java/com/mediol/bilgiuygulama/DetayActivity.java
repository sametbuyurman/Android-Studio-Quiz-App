package com.mediol.bilgiuygulama;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mediol.bilgiuygulama.Model.QuizListModel;
import com.mediol.bilgiuygulama.viewModel.QuizListViewModel;

import java.util.List;

public class DetayActivity extends AppCompatActivity {

    private TextView title, difficulty, totalQuestions;
    private Button startQuizBtn;
    private NavController navController;
    private int position;
    private ProgressBar progressBar;
    private QuizListViewModel viewModel;
    private ImageView topicImage;
    private String quizId;
    private long totalQueCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())).get(QuizListViewModel.class);

        title = findViewById(R.id.detailFragmentTitle);
        difficulty = findViewById(R.id.detailFragmentDifficulty);
        totalQuestions = findViewById(R.id.detailFragmentQuestions);
        startQuizBtn = findViewById(R.id.startQuizBtn);
        progressBar = findViewById(R.id.detailProgressBar);
        topicImage = findViewById(R.id.detailFragmentImage);


        position = getIntent().getIntExtra("position", -1);

        viewModel.getQuizListLiveData().observe(this, new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizListModels) {
                if (position != -1 && position < quizListModels.size()) {
                    QuizListModel quiz = quizListModels.get(position);
                    difficulty.setText(quiz.getDifficulty());
                    title.setText(quiz.getTitle());
                    totalQuestions.setText(String.valueOf(quiz.getQuestions()));
                    Glide.with(DetayActivity.this).load(quiz.getImage()).into(topicImage);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    }, 2000);

                    totalQueCount = quiz.getQuestions();
                    quizId = quiz.getQuizId();
                }
            }
        });

        startQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetayActivity.this, QuizActivity.class);
                intent.putExtra("quizId", quizId);
                intent.putExtra("totalQueCount", totalQueCount);
                startActivity(intent);
            }
        });
    }
}
