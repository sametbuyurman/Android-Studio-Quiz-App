package com.mediol.bilgiuygulama;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.mediol.bilgiuygulama.Adapter.QuizListAdapter;
import com.mediol.bilgiuygulama.Model.QuizListModel;
import com.mediol.bilgiuygulama.viewModel.QuizListViewModel;

import java.util.List;

public class ListActivity extends AppCompatActivity implements QuizListAdapter.OnItemClickedListner {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private QuizListViewModel viewModel;
    private QuizListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())).get(QuizListViewModel.class);

        recyclerView = findViewById(R.id.listQuizRecyclerview);
        progressBar = findViewById(R.id.quizListProgressbar);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuizListAdapter(this);

        recyclerView.setAdapter(adapter);

        viewModel.getQuizListLiveData().observe(this, new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizListModels) {
                progressBar.setVisibility(View.GONE);
                adapter.setQuizListModels(quizListModels);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, DetayActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }}
