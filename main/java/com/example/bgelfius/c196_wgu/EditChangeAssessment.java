package com.example.bgelfius.c196_wgu;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EditChangeAssessment extends Activity {
    private String passedVar = null;
    private String currentAssessmentType;
    private String currentSelectedCourse;
    public final static String DETAILTYPE_EXTRA="com.example.bgelfius.c196_wgu.detailType";
    private boolean updateMode = false;
    private boolean notifyAssessment = false;
    private Calendar cal;
    private int sDay;
    private int sMonth;
    private int sYear;
    private int eDay;
    private int eMonth;
    private int eYear;
    private TextView adv;
    private boolean notifyAssessmentStart;
    private boolean notifyAssessmentCurrent;
    private Integer currentClassID = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editchangeassessment);
        passedVar=getIntent().getStringExtra(CourseDetailActivity.DETAILTYPE_EXTRA);
        cal = Calendar.getInstance();

        // build list of Courses
        Spinner mSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayList<String> options; //=new ArrayList<String>();

        options = listCourses();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,options);
        mSpinner.setAdapter(adapter);
        mSpinner.setSelection(0);
        RadioGroup getDefault = (RadioGroup) findViewById(R.id.grpType);
        RadioButton defaultButton = (RadioButton) findViewById(getDefault.getCheckedRadioButtonId());
        currentAssessmentType = defaultButton.getText().toString();

        adv = (TextView)findViewById(R.id.eAssessmentDate);

        adv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditChangeAssessment.this, aDatePicker, cal
                        .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        notifyAssessmentStart = false;
        notifyAssessmentCurrent = false;

        if (passedVar.equals("AddAssessment")) {
            ;
        } else {
            updateMode = true;
            getAssessmentInformation(Long.parseLong(passedVar));

        }

    }

    final DatePickerDialog.OnDateSetListener aDatePicker = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            sDay = dayOfMonth;
            sMonth = monthOfYear+1;
            sYear = year;

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            adv.setText(sdf.format(cal.getTime()));


        }
    };

    private void getAssessmentInformation(long id) {
        DBProvider dbProvider = new DBProvider();

        //String selClause = DBOpenHelper.COURSE_TERM_ID + " = ?";
        String selClause = " _id =" + id;
        String selArgs[] = {""};


        Cursor cursor = getContentResolver().query(dbProvider.ASSESSMENT_CONTENT_URI,
                DBOpenHelper.ASSESSMENT_ALL_COLUMNS, selClause, selArgs, null, null);
        String[] from = {DBOpenHelper.ASSESSMENT_TYPE, DBOpenHelper.ASSESSMENT_NAME, DBOpenHelper.ASSESSMENT_DATE, DBOpenHelper.ASSESSMENT_COURSE_ID};
        int[] to = {android.R.id.text1};

        CursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, cursor, from, to, 0);
        //ListView list = (ListView) findViewById(R.id.courses);
        //list.setAdapter(cursorAdapter);
        int index = cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_NAME);
        cursor.moveToNext();
        TextView tv = (TextView)findViewById(R.id.eAssessmentName);
        tv.setText(cursor.getString(index));

        index = cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_DATE);
        tv = (TextView)findViewById(R.id.eAssessmentDate);
        tv.setText(cursor.getString(index));


        index = cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_DATE);
        tv = (TextView)findViewById(R.id.eAssessmentDate);
        tv.setText(cursor.getString(index));


        index = cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_COURSE_ID);
        Spinner mSpinner = (Spinner) findViewById(R.id.spinner);
        mSpinner.setSelection(Integer.parseInt(cursor.getString(index)) -1 );
        currentClassID = Integer.parseInt(cursor.getString(index));

        CheckBox cb = (CheckBox) findViewById(R.id.cbAssessmentNotify);
        cb.setChecked(getAssessmentAlert(currentClassID));
        notifyAssessmentStart = cb.isChecked();


        //index = cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_COURSE_ID);
        //tv = (TextView)findViewById(R.id.assessmentCourseName);
        //tv.setText(getCourseName( Long.parseLong(cursor.getString(index)))   );

        index = cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_TYPE);
        RadioButton rb;

        switch(cursor.getString(index)) {
            case "Performance Assessment":
                rb = (RadioButton) findViewById(R.id.rdoPerformance);
                rb.setChecked(true);
                break;
            case "Objective Assessment":
                rb = (RadioButton) findViewById(R.id.rdoObjective);
                rb.setChecked(true);
                break;

        }



    }
    int getAlertID(long courseID) {
        DBProvider dbProvider = new DBProvider();
        int rc = 0;

        //String selClause = DBOpenHelper.COURSE_TERM_ID + " = ?";
        String selClause = " course_id =" + courseID + "  and alertType like '%Assessment' ";
        String selArgs[] = {""};


        Cursor cursor = getContentResolver().query(dbProvider.ALERT_CONTENT_URI,
                DBOpenHelper.ALERT_ALL_COLUMNS, selClause, selArgs, null, null);
        String[] from = {DBOpenHelper.ALERT_TYPE, DBOpenHelper.ALERT_ID, DBOpenHelper.ALERT_DATE, DBOpenHelper.ALERT_COURSEID};
        int[] to = {android.R.id.text1};

        CursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, cursor, from, to, 0);
        //ListView list = (ListView) findViewById(R.id.courses);
        //list.setAdapter(cursorAdapter);
        int index = cursor.getColumnIndex(DBOpenHelper.ALERT_ID);
        cursor.moveToNext();
        if (cursor.getCount() > 0) {
            rc = Integer.parseInt(cursor.getString(index));
        }

        return rc;
    }

    void insertAlert(Alert alert) {
        int startAlertID = 0;

        startAlertID = getAlertID(alert.getCourseID());

        if ((notifyAssessmentStart  != notifyAssessment) &&
           (startAlertID > 0)) {
                alert.setAlertID(startAlertID);
                updateAlert(alert);
            } else {
                DBProvider dbProvider = new DBProvider();
                ContentValues values = new ContentValues();
                values.put(DBOpenHelper.ALERT_TYPE, alert.getAlertType());
                values.put(DBOpenHelper.ALERT_COURSEID, alert.getCourseID());
                values.put(DBOpenHelper.ALERT_DATE, alert.getAlertDate());
                Uri alertUri = getContentResolver().insert(dbProvider.ALERT_CONTENT_URI,
                        values);
                Log.d("EditChangeCourse", "Inserted alert " + alertUri.getLastPathSegment());
        }


    }

    void updateAlert(Alert alert) {
        DBProvider dbProvider = new DBProvider();
        ContentValues values = new ContentValues();
        String s =  " _id = " + String.valueOf(alert.getAlertID());
        String[] strings = null;
        //values.put(DBOpenHelper.TERM_ID, term.getTermID());
        values.put(DBOpenHelper.ALERT_TYPE, alert.getAlertType());
        values.put(DBOpenHelper.ALERT_COURSEID, alert.getCourseID());
        values.put(DBOpenHelper.ALERT_DATE, alert.getAlertDate());
        int rc = 0;

        if (!notifyAssessmentCurrent  ) {
            rc = getContentResolver().delete(dbProvider.ALERT_CONTENT_URI, s, strings);
            Log.d("EditChangeAssessment", "deleted alert : " + String.valueOf(alert.getAlertID()));
       } else {
            rc = getContentResolver().update(dbProvider.ALERT_CONTENT_URI,
                    values, s, strings);
            Log.d("EditChangeAssessment", "updated alert return code: " + rc);

        }




    }

    String getCourseName(long id) {
        String name="Name not available";
        DBProvider dbProvider = new DBProvider();

        String selClause = " _id =" + id;
        String selArgs[] = {""};


        Cursor cursor = getContentResolver().query(dbProvider.COURSE_CONTENT_URI,
                DBOpenHelper.COURSE_ALL_COLUMNS, selClause, selArgs, null, null);
        String[] from = {DBOpenHelper.COURSE_TITLE, DBOpenHelper.COURSE_STARTDATE, DBOpenHelper.COURSE_ENDDATE, DBOpenHelper.COURSE_STATUS};
        int[] to = {android.R.id.text1};

        CursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, cursor, from, to, 0);
        //ListView list = (ListView) findViewById(R.id.courses);
        //list.setAdapter(cursorAdapter);
        int index = cursor.getColumnIndex(DBOpenHelper.COURSE_TITLE);
        cursor.moveToNext();
        name = cursor.getString(index);

        return name;
    }

    boolean getAssessmentAlert(long id) {
        boolean rc = false;
        DBProvider dbProvider = new DBProvider();

        String selClause = " course_id =" + id + " and alertType like '%Assessment' ";
        String selArgs[] = {""};


        Cursor cursor = getContentResolver().query(dbProvider.ALERT_CONTENT_URI,
                DBOpenHelper.ALERT_ALL_COLUMNS, selClause, selArgs, null, null);
        String[] from = {};
        int[] to = {};

        CursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, cursor, from, to, 0);
        //ListView list = (ListView) findViewById(R.id.courses);
        //list.setAdapter(cursorAdapter);
        TextView tv = null;

        if (cursor.getCount() > 0) {
            rc = true;
        } else {
            rc = false;
        }

        return rc;

    }

    public void onTypeClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rdoPerformance:
                if (checked)
                    currentAssessmentType = "Performance Assessment";
                break;
            case R.id.rdoObjective:
                if (checked)
                    currentAssessmentType = "Objective Assessment";
                break;

        }

    }




    void insertAssessment(Assessment assessment) {

        DBProvider dbProvider = new DBProvider();
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ASSESSMENT_TYPE, assessment.getAssessmentType());
        values.put(DBOpenHelper.ASSESSMENT_COURSE_ID, assessment.getCourseID());
        values.put(DBOpenHelper.ASSESSMENT_NAME, assessment.getAssessmentName());
        values.put(DBOpenHelper.ASSESSMENT_DATE, assessment.getAssessmentDate());
        Uri assessmentUri = getContentResolver().insert(dbProvider.ASSESSMENT_CONTENT_URI,
                values);
        Log.d("EditChangeAssessment", "Inserted assessment " + assessmentUri.getLastPathSegment());

    }


    ArrayList listCourses() {
        DBProvider dbProvider = new DBProvider();
        ArrayList<String> options=new ArrayList<String>();

        //String selClause = DBOpenHelper.COURSE_TERM_ID + " = ?";
        String selClause = "";
        String selArgs[] = {""};


        Cursor cursor = getContentResolver().query(dbProvider.COURSE_CONTENT_URI,
                DBOpenHelper.COURSE_ALL_COLUMNS, selClause, selArgs, null, null);
        String[] from = {DBOpenHelper.COURSE_TITLE};
        int[] to = {android.R.id.text1};
        int idIndex = cursor.getColumnIndex(DBOpenHelper.COURSE_ID);
        int nameIndex = cursor.getColumnIndex(DBOpenHelper.COURSE_TITLE);
       cursor.moveToFirst();
        while (cursor.isAfterLast() == false)
        {
            options.add(String.valueOf(cursor.getString(nameIndex)));
            //options.add(String.valueOf(cursor.getLong(idIndex)) + " - " + cursor.getString(nameIndex));
            cursor.moveToNext();
        }

        return options;

        //CursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
        //        android.R.layout.simple_list_item_1, cursor, from, to, 0);
        // passedView.setText("count: " + cursor.getCount());
        //ListView list = (ListView) findViewById(R.id.courseList);
        //list.setAdapter(cursorAdapter);
        //list.setOnItemClickListener(courseItemListener);
    }

    public void onSaveAssessment(View view) {
        Assessment newAssessment = new Assessment();

        EditText simpleEditText = (EditText) findViewById(R.id.eAssessmentName);
        String strValue = simpleEditText.getText().toString();

        newAssessment.setAssessmentName(strValue);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        newAssessment.setCourseID( getCourseID(spinner.getSelectedItem().toString()) );


        //newAssessment.setCourseID(spinner.getSelectedItemId());
        CheckBox cb = (CheckBox) findViewById(R.id.cbAssessmentNotify);
        notifyAssessmentCurrent = cb.isChecked();

        newAssessment.setAssessmentType(currentAssessmentType);

        newAssessment.setAssessmentDate(adv.getText().toString());

        if (updateMode) {
            newAssessment.setAssessmentID(Integer.parseInt(passedVar));
            updateAssessment(newAssessment);
        } else {
            insertAssessment(newAssessment);
        }

        Alert alert = new Alert();
        alert.setCourseID(newAssessment.getCourseID());
        TextView tv;

        if (notifyAssessmentCurrent != notifyAssessmentStart) {
            alert.setAlertType(newAssessment.getAssessmentType());
            tv = (TextView) findViewById(R.id.eAssessmentDate);
            alert.setAlertDate(tv.getText().toString());
            insertAlert(alert);
        }


        Intent intent = new Intent(EditChangeAssessment.this, MainActivity.class);
        //intent.putExtra(DETAILTYPE_EXTRA,"AddAssessment");
        startActivity(intent);
    }

    private void updateAssessment(Assessment newAssessment) {
        DBProvider dbProvider = new DBProvider();
        ContentValues values = new ContentValues();
        String s =  " _id = " + String.valueOf(newAssessment.getAssessmentID());
        String[] strings = null; //{term.getTermStartDate(), term.getTermEndDate(), term.getTermTitle()};


        values.put(DBOpenHelper.ASSESSMENT_TYPE, newAssessment.getAssessmentType());
        values.put(DBOpenHelper.ASSESSMENT_COURSE_ID, newAssessment.getCourseID());
        values.put(DBOpenHelper.ASSESSMENT_NAME, newAssessment.getAssessmentName());
        values.put(DBOpenHelper.ASSESSMENT_DATE, newAssessment.getAssessmentDate());
        int rc = getContentResolver().update(dbProvider.ASSESSMENT_CONTENT_URI,
                values, s, strings);
        Log.d("EditChangeAssessment", "updated course return code: " + rc);

    }

    long getCourseID(String coursename) {
        long courseID = 0;

        DBProvider dbProvider = new DBProvider();

        //String selClause = DBOpenHelper.COURSE_TERM_ID + " = ?";
        String selClause = " courseTitle = '" + coursename + "' ";
        String selArgs[] = {};


        Cursor cursor = getContentResolver().query(dbProvider.COURSE_CONTENT_URI,
                DBOpenHelper.COURSE_ALL_COLUMNS, selClause, selArgs, null, null);
        String[] from = {DBOpenHelper.COURSE_TITLE};
        int[] to = {android.R.id.text1};
        int idIndex = cursor.getColumnIndex(DBOpenHelper.COURSE_ID);
        int nameIndex = cursor.getColumnIndex(DBOpenHelper.COURSE_TITLE);
        cursor.moveToFirst();
        courseID = cursor.getInt(idIndex);


        return courseID;
    }

    public void onClickNotify(View view) {
        CheckBox cb = (CheckBox) findViewById(R.id.cbAssessmentNotify);
        notifyAssessment = cb.isChecked();
    }
}
