package com.example.courseregistrationwaitinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

public class CourseWaitingList extends AppCompatActivity{
    private Database cr_db;
    private AlertDialog.Builder alert;
    private AlertDialog alert_control;
    private ArrayAdapter<CharSequence> priority_list_type, course_list_type, semester_list_type;
    private ListAdapter registration_list_type;
    private Cursor registration_data;
    private View registration_dialog;
    private ListView course_waiting_list;
    private EditText input_name;
    private Spinner input_course, input_priority, input_semester;
    private Button register_student_button;
    private ArrayList<String> registration_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); //hide the title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_course_waiting_list);
        cr_db = new Database(this);
        registration_data = cr_db.getAllData();
        registration_dialog = getLayoutInflater().inflate(R.layout.register_student_alert, null, false);
        input_name = registration_dialog.findViewById(R.id.name_field);
        input_course = registration_dialog.findViewById(R.id.course_field);
        input_priority = registration_dialog.findViewById(R.id.priority_field);
        input_semester = registration_dialog.findViewById(R.id.semester_field);
        course_waiting_list = findViewById(R.id.course_waiting_list);
        register_student_button = findViewById(R.id.register_student_button);
        //To insert data for each drop down list
        course_list_type = ArrayAdapter.createFromResource(this, R.array.courses, android.R.layout.simple_spinner_item);
        course_list_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        input_course.setAdapter(course_list_type);
        priority_list_type = ArrayAdapter.createFromResource(this, R.array.priority, android.R.layout.simple_spinner_item);
        priority_list_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        input_priority.setAdapter(priority_list_type);
        semester_list_type = ArrayAdapter.createFromResource(this, R.array.semesters, android.R.layout.simple_spinner_item);
        semester_list_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        input_semester.setAdapter(semester_list_type);
        //To print the data from the database
        registration_list.clear();
        while (registration_data.getCount() > 0 && registration_data.moveToNext()){
            registration_list.add(registration_data.getString(0) + "\n" + registration_data.getString(1) + " | " + registration_data.getString(2) + " | " + registration_data.getString(3));
        }
        registration_list_type = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, registration_list);
        course_waiting_list.setAdapter(registration_list_type);
        //To bring up a form for the user to register a student
        register_student_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_name.setText("");
                input_name.setEnabled(true);
                alert = new AlertDialog.Builder(CourseWaitingList.this);
                alert.setView(registration_dialog);
                alert.setTitle("REGISTER NEW STUDENT");
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alert_control.cancel();
                        ((ViewGroup)registration_dialog.getParent()).removeView(registration_dialog);
                    }
                });
                alert.setPositiveButton("ADD", null);
                alert_control = alert.create();
                alert_control.setCanceledOnTouchOutside(false);
                alert_control.show();
                Button pb = alert_control.getButton(AlertDialog.BUTTON_POSITIVE);
                pb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (input_name.getText().toString().isEmpty()) {
                            input_name.setError("This field is empty!");
                        }
                        else if (!Pattern.compile("^[A-Z][a-z]+ [A-Z][a-z]+$").matcher(input_name.getText().toString()).matches()) {
                            input_name.setError("All names should start with a capital letter follow by a series of lower case letters. FORMAT:[FIRST NAME] [LAST NAME]");
                        }
                        if (Pattern.compile("^(?!^$)[A-Z][a-z]+ [A-Z][a-z]+$").matcher(input_name.getText().toString()).matches()) {
                            input_name.setError(null);
                            boolean insertion_result = cr_db.insertData(input_name.getText().toString(), input_course.getSelectedItem().toString(), input_priority.getSelectedItem().toString(), input_semester.getSelectedItem().toString());
                            if (insertion_result) {
                                Toast.makeText(CourseWaitingList.this, "INSERTION COMPLETE", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(CourseWaitingList.this, "INSERTION ERROR", Toast.LENGTH_SHORT).show();
                            }
                            alert_control.dismiss();
                            ((ViewGroup)registration_dialog.getParent()).removeView(registration_dialog);
                            startActivity(getIntent());
                        }
                    }
                });
            }
        });
        //To bring up a form to edit a selected item in the ListView
        course_waiting_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                input_name.setText(parent.getItemAtPosition(position).toString().replaceFirst("\\n[\\w| ]*$", ""));
                input_name.setEnabled(false);
                alert = new AlertDialog.Builder(CourseWaitingList.this);
                alert.setView(registration_dialog);
                alert.setTitle("EDIT REGISTERED STUDENT");
                alert.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean deletion_result = cr_db.deleteData(input_name.getText().toString());
                        if (deletion_result) {
                            Toast.makeText(CourseWaitingList.this, "DELETION COMPLETE", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(CourseWaitingList.this, "DELETION ERROR", Toast.LENGTH_SHORT).show();
                        }
                        ((ViewGroup)registration_dialog.getParent()).removeView(registration_dialog);
                        startActivity(getIntent());
                    }
                });
                alert.setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean update_result = cr_db.updateData(input_name.getText().toString(), input_course.getSelectedItem().toString(), input_priority.getSelectedItem().toString(), input_semester.getSelectedItem().toString());
                        if (update_result) {
                            Toast.makeText(CourseWaitingList.this, "UPDATE COMPLETE", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(CourseWaitingList.this, "UPDATE ERROR", Toast.LENGTH_SHORT).show();
                        }
                        ((ViewGroup)registration_dialog.getParent()).removeView(registration_dialog);
                        startActivity(getIntent());
                    }
                });
                alert_control = alert.create();
                alert_control.setCanceledOnTouchOutside(false);
                alert_control.show();
            }
        });
    }
}