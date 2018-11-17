package com.example.dima.newreader;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NotesAdapter.ItemClickListener, NotesAdapter.ItemLongClickListener {

    private static final String SHARED_NOTES = "shared notes";
    private static final String NOTES = "notes";
    private static final String NOTE_POSITION = "note_position";
    private static final String NOTE_TEXT = "note_text";
    NotesAdapter notesAdapter;
    ArrayList<News> notes;
    RecyclerView notesList;
    SharedPreferences prefs;
    ArrayList<Integer> topArticles;
    Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notes = new ArrayList<>();



        notesList = findViewById(R.id.notesList);
        notesList.setLayoutManager(new LinearLayoutManager(this));
        notesAdapter = new NotesAdapter(this,notes);
        notesAdapter.setClickListener(this);
        notesAdapter.setLongClickListener(this);
        notesList.setAdapter(notesAdapter);
        retrofit = new Retrofit.Builder()
                .baseUrl("https://hacker-news.firebaseio.com/v0/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TopApi messagesApi = retrofit.create(TopApi.class);
        Call<List<Integer>> messages = messagesApi.articles();

        messages.enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                if (response.isSuccessful()) {
                    Log.d("RF","response " + response.body().size());
                    topArticles = (ArrayList<Integer>) response.body();
                    for (Integer topArticle : topArticles) {
                        ArticleApi articlesApi = retrofit.create(ArticleApi.class);
                        Call<News> article = articlesApi.getArticle(topArticle);
                        article.enqueue(new Callback<News>() {
                            @Override
                            public void onResponse(Call<News> call, Response<News> response) {
                                if (response.isSuccessful()){
                                    notes.add(response.body());
                                    notesAdapter.notifyItemInserted(notes.size()-1);
                                } else {
                                    Log.d("RF","response code " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<News> call, Throwable t) {

                            }
                        });
                    }
                } else {
                    Log.d("RF","response code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {
                Log.d("RF","failure " + t);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_item:
                editNote(-1);

        }


        return super.onOptionsItemSelected(item);
    }

    private void editNote(int i) {
        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra(NOTE_POSITION, i);
        if (i >= 0) {
            intent.putExtra(NOTE_TEXT, notesAdapter.getItem(i));
        } else {
            intent.putExtra(NOTE_TEXT, "");
        }
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        String textItem = data.getStringExtra("textItem");
        int posItem = data.getIntExtra("posItem",-1);
        /*
        if (posItem < 0) {
            notes.add(textItem);
        }  else {
            notes.set(posItem,textItem);
        }
        notesAdapter.notifyDataSetChanged();
        */
    }

    @Override
    public void onItemClick(View view, int position) {
        //   Toast.makeText(this, "You clicked " + notesAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        editNote(position);
    }

    @Override
    public void onItemLongClick(View view, final int position) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you?")
                .setMessage("Are you ure want to delete this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notes.remove(position);
                        notesAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show()
        ;


    }


}
