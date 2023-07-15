package sg.edu.rp.c346.id22020383.demodatabasecrud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnAdd, btnEdit, btnRetrieve;
    EditText etContent;
    ArrayList<Note> noteList;
    ArrayAdapter<Note> adapter;
    ListView listView;

    private static final int REQUEST_CODE_EDIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnRetrieve = findViewById(R.id.btnRetrieve);
        etContent = findViewById(R.id.etContent);
        listView = findViewById(R.id.lv);
        noteList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noteList);
        listView.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etContent.getText().toString().trim();
                if (!content.isEmpty()) {
                    DBHelper dbHelper = new DBHelper(MainActivity.this);
                    long rowId = dbHelper.insertNote(content);
                    if (rowId != -1) {
                        Note note = new Note((int) rowId, content);
                        noteList.add(note);
                        adapter.notifyDataSetChanged();
                        etContent.setText("");
                        Toast.makeText(MainActivity.this, "Note added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to add note", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Note content cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noteList.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No notes available", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    intent.putExtra("data", noteList.get(0));
                    startActivityForResult(intent, REQUEST_CODE_EDIT);
                }
            }
        });

        btnRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(MainActivity.this);
                noteList.clear();
                noteList.addAll(dbHelper.getAllNotes());
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Notes retrieved successfully", Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("data", noteList.get(position));
                startActivityForResult(intent, REQUEST_CODE_EDIT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK) {
            DBHelper dbHelper = new DBHelper(MainActivity.this);
            noteList.clear();
            noteList.addAll(dbHelper.getAllNotes());
            adapter.notifyDataSetChanged();
        }
    }
}
