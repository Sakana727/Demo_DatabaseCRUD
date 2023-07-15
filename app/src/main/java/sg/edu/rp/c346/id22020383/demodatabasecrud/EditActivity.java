package sg.edu.rp.c346.id22020383.demodatabasecrud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    TextView tvID;
    EditText etContent;
    Button btnUpdate, btnDelete;
    Note data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        tvID = findViewById(R.id.tvID);
        etContent = findViewById(R.id.etContent);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        data = (Note) getIntent().getSerializableExtra("data");

        tvID.setText("ID: " + data.getId());
        etContent.setText(data.getNoteContent());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedContent = etContent.getText().toString().trim();
                if (updatedContent.isEmpty()) {
                    Toast.makeText(EditActivity.this, "Note content cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    data.setNoteContent(updatedContent);
                    DBHelper dbHelper = new DBHelper(EditActivity.this);
                    int rowsAffected = dbHelper.updateNote(data);
                    if (rowsAffected > 0) {
                        Toast.makeText(EditActivity.this, "Note updated successfully", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(EditActivity.this, "Failed to update note", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(EditActivity.this);
                int rowsAffected = dbHelper.deleteNote(data.getId());
                if (rowsAffected > 0) {
                    Toast.makeText(EditActivity.this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(EditActivity.this, "Failed to delete note", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}