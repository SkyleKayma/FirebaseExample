package fr.skyle.firebaseexample.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import fr.skyle.firebaseexample.R;
import fr.skyle.firebaseexample.model.Article;

public class ActivityAddArticle extends AppCompatActivity {

    private EditText mEditTextTitle;
    private EditText mEditTextContent;
    private Button mButtonAdd;
    private ProgressBar mProgress;

    private DatabaseReference mDatabase;

    // --- LifeCycle

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);

        // Get views
        mEditTextTitle = findViewById(R.id.editTextActivityAddArticleTitle);
        mEditTextContent = findViewById(R.id.editTextActivityAddArticleContent);
        mButtonAdd = findViewById(R.id.buttonActivityAddArticleAdd);
        mProgress = findViewById(R.id.progressBarActivityAddArticle);

        // Get firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setListeners();
    }

    // --- Other methods

    private void setListeners() {
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allFieldsAreFilled()) {
                    addArticle();
                }
            }
        });
    }

    private boolean allFieldsAreFilled() {
        // Check that editText are not empty
        if (mEditTextTitle.getText().length() == 0 || mEditTextContent.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.add_article_error_field_empty), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void addArticle() {
        // Show progress
        mProgress.setVisibility(View.VISIBLE);
        mButtonAdd.setVisibility(View.GONE);

        // Generate unique ID
        String key = mDatabase.child("articles").push().getKey();

        // Create data to send
        HashMap<String, Object> data = new HashMap<>();
        data.put(
                key,
                new Article(
                        key,
                        mEditTextTitle.getText().toString(),
                        mEditTextContent.getText().toString(),
                        System.currentTimeMillis()
                )
        );

        // Send data
        mDatabase.child("articles")
                .updateChildren(data)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            finish();
                        } else {
                            mProgress.setVisibility(View.GONE);
                            mButtonAdd.setVisibility(View.VISIBLE);

                            Toast.makeText(getApplicationContext(), getString(R.string.add_article_error_cant_add), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
