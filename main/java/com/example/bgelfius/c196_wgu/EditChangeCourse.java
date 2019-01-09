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
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditChangeCourse extends Activity {
    private String currentCheckStatus;
    private Boolean startNotify;
    private Boolean endNotify;
    private static Boolean startNotifyBegin;
    private static Boolean endNotifyBegin;
    private String passedVar = null;
    boolean updateMode = false;
    private Calendar cal;
    private int sDay;
    private int sMonth;
    private int sYear;
    private int eDay;
    private int eMonth;
    private int eYear;
    TextView sdv;
    TextView edv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editchangecourse);
        passedVar=getIntent().getStringExtra(MainActivity.DETAILTYPE_EXTRA);
        cal = Calendar.getInstance();

        sdv = (TextView)findViewById(R.id.eStartDate);
        edv = (TextView)findViewById(R.id.eEndDate);

        sdv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditChangeCourse.this, sDatePicker, cal
                        .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        edv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditChangeCourse.this, eDatePicker, cal
                        .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        if (passedVar.equals("AddCourse")) {
            ;
        } else {
            try {
                updateMode = true;
                getCourseInformation(Long.parseLong(passedVar));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        RadioGroup getDefault = (RadioGroup) findViewById(R.id.grpStatus);
        RadioButton defaultButton = (RadioButton) findViewById(getDefault.getCheckedRadioButtonId());
        currentCheckStatus = defaultButton.getText().toString();
        CheckBox tmpBox = (CheckBox) findViewById(R.id.cbStartNotify);
        startNotify = tmpBox.isChecked();
        tmpBox = (CheckBox) findViewById(R.id.cbEndNotify);
        endNotify = tmpBox.isChecked();



    }

    public void onSave(View view) {
        EditText simpleEditText = (EditText) findViewById(R.id.eCourseName);
        String strValue = simpleEditText.getText().toString();
        Course newCourse = new Course();
        newCourse.setCourseTitle(simpleEditText.getText().toString());
        newCourse.setStartDate(sdv.getText().toString());
        newCourse.setEndDate(edv.getText().toString());
        newCourse.setStatus(currentCheckStatus);
        CheckBox tmpBox = (CheckBox) findViewById(R.id.cbStartNotify);
        startNotify = tmpBox.isChecked();
        tmpBox = (CheckBox) findViewById(R.id.cbEndNotify);
        endNotify = tmpBox.isChecked();


        if (updateMode) {
            newCourse.setCourseID(Long.parseLong(passedVar));
            updateCourse(newCourse);
        } else {
            insertCourse(newCourse);
        }






        Intent intent = new Intent(EditChangeCourse.this, MainActivity.class);
        //intent.putExtra(DETAILTYPE_EXTRA,String.valueOf(l));
        startActivity(intent);
    }

    public void onStatusClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked

        switch(view.getId()) {
            case R.id.rdoComplete:
                if (checked)
                    currentCheckStatus = "Completed";
                    break;
            case R.id.rdoDrop:
                if (checked)
                    currentCheckStatus = "Dropped";
                    break;
            case R.id.rdoPlan:
                if (checked)
                    currentCheckStatus = "Plan to take";
                    break;
            case R.id.rdoProgress:
                if (checked)
                    currentCheckStatus = "In Progress";
                    break;

        }

    }

    final DatePickerDialog.OnDateSetListener sDatePicker = new DatePickerDialog.OnDateSetListener() {

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
            sdv.setText(sdf.format(cal.getTime()));


        }
    };

    final DatePickerDialog.OnDateSetListener eDatePicker = new DatePickerDialog.OnDateSetListener() {

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
            edv.setText(sdf.format(cal.getTime()));


        }
    };

    void insertCourse(Course course) {

        DBProvider dbProvider = new DBProvider();
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_TITLE, course.getCourseTitle());
        values.put(DBOpenHelper.COURSE_STATUS, course.getStatus());
        values.put(DBOpenHelper.COURSE_STARTDATE, course.getStartDate());
        values.put(DBOpenHelper.COURSE_ENDDATE, course.getEndDate());
        values.put(DBOpenHelper.COURSE_TERM_ID, course.getTermID());
        values.put(DBOpenHelper.COURSE_MENTOR_ID, course.getMentorID());
        Uri courseUri = getContentResolver().insert(dbProvider.COURSE_CONTENT_URI,
                values);
        Log.d("EditChangeCourse", "Inserted course " + courseUri.getLastPathSegment());

        // add alert
        TextView tv;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        if (endNotify || startNotify) {
            Alert alert = new Alert();
            alert.setCourseID(Long.parseLong(courseUri.getLastPathSegment()));
            if (endNotify) {
                alert.setAlertType("Course End Alert");
                tv = (TextView) findViewById(R.id.eEndDate);
                //String selectedDate = sdf.format(new Date(cv.getDate()));
                alert.setAlertDate(tv.getText().toString());
            } else {
                tv = (TextView) findViewById(R.id.eStartDate);
                //String selectedDate = sdf.format(new Date(cv.getDate()));
                alert.setAlertDate(tv.getText().toString());

                alert.setAlertType("Course Start Alert");
            }

            insertAlert(alert);


        }
    }

    void updateCourse(Course course) {

        DBProvider dbProvider = new DBProvider();

        ContentValues values = new ContentValues();
        String s =  " _id = " + String.valueOf(course.getCourseID());
        String[] strings = null; //{term.getTermStartDate(), term.getTermEndDate(), term.getTermTitle()};


        //values.put(DBOpenHelper.TERM_ID, term.getTermID());
        values.put(DBOpenHelper.COURSE_TITLE, course.getCourseTitle());
        values.put(DBOpenHelper.COURSE_STARTDATE, course.getStartDate());
        values.put(DBOpenHelper.COURSE_ENDDATE, course.getEndDate());
        values.put(DBOpenHelper.COURSE_STATUS, course.getStatus());
        TextView tv;



        if (endNotify || startNotify) {
            Alert alert = new Alert();
            alert.setCourseID(course.getCourseID());
            if (startNotify != startNotifyBegin) {
                alert.setAlertType("Course Start Alert");
                tv = (TextView) findViewById(R.id.eStartDate);
                //String selectedDate = sdf.format(new Date(cv.getDate()));
                alert.setAlertDate(tv.getText().toString());
                insertAlert(alert);
            }


            if (endNotify != endNotifyBegin) {
                alert.setAlertType("Course End Alert");
                tv = (TextView) findViewById(R.id.eEndDate);
                //String selectedDate = sdf.format(new Date(cv.getDate()));
                alert.setAlertDate(tv.getText().toString());
                insertAlert(alert);
            }
        }

        //long insertId = dbProvider.insert(DBOpenHelper.TABLE_TERM, values);
        int rc = getContentResolver().update(dbProvider.COURSE_CONTENT_URI,
                values, s, strings);
        Log.d("EditChangeCourse", "updated course return code: " + rc);


    }

    boolean getStartAlert(long id) {
        boolean rc = false;
        DBProvider dbProvider = new DBProvider();

        //String selClause = DBOpenHelper.COURSE_TERM_ID + " = ?";
        String selClause = " course_id =" + id + " and alertType='Course Start Alert' ";
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

    boolean getEndAlert(long id) {
        boolean rc = false;
        DBProvider dbProvider = new DBProvider();

        //String selClause = DBOpenHelper.COURSE_TERM_ID + " = ?";
        String selClause = " course_id =" + id + " and alertType='Course End Alert' ";
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

    int getStartID(long courseID) {
        DBProvider dbProvider = new DBProvider();
        int rc = 0;

        //String selClause = DBOpenHelper.COURSE_TERM_ID + " = ?";
        String selClause = " course_id =" + courseID + "  and  alertType='Course Start Alert' ";
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

    int getEndID(long courseID) {
        DBProvider dbProvider = new DBProvider();
        int rc = 0;

        //String selClause = DBOpenHelper.COURSE_TERM_ID + " = ?";
        String selClause = " course_id =" + courseID + "  and  alertType='Course End Alert' ";
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
        int endAlertID = 0;

        startAlertID = getStartID(alert.getCourseID());
        endAlertID = getEndID(alert.getCourseID());


        if (startNotify  != startNotifyBegin) {
            if (alert.getAlertType().equals("Course Start Alert") && startAlertID > 0  ) {
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

        if (endNotify != endNotifyBegin) {
            if (alert.getAlertType().equals("Course End Alert") && endAlertID > 0) {
                alert.setAlertID(endAlertID);
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

        if (alert.getAlertType().equals("Course Start Alert") && !startNotify  ) {
            rc = getContentResolver().delete(dbProvider.ALERT_CONTENT_URI, s, strings);
            Log.d("EditChangeCourse", "deleted alert : " + String.valueOf(alert.getAlertID()));

        } else if (alert.getAlertType().equals("Course End Alert") && !endNotify) {
            rc = getContentResolver().delete(dbProvider.ALERT_CONTENT_URI, s, strings);
            Log.d("EditChangeCourse", "deleted alert : " + String.valueOf(alert.getAlertID()));

        } else {
            rc = getContentResolver().update(dbProvider.ALERT_CONTENT_URI,
                    values, s, strings);
            Log.d("EditChangeCourse", "updated alert return code: " + rc);

        }




    }


    void getCourseInformation(long id) throws ParseException {
        DBProvider dbProvider = new DBProvider();

        //String selClause = DBOpenHelper.COURSE_TERM_ID + " = ?";
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
        TextView tv = (TextView)findViewById(R.id.eCourseName);
        tv.setText(cursor.getString(index));

        index = cursor.getColumnIndex(DBOpenHelper.COURSE_STARTDATE);
        tv = (TextView)findViewById(R.id.eStartDate);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        tv.setText(cursor.getString(index));



        index = cursor.getColumnIndex(DBOpenHelper.COURSE_ENDDATE);
        tv = (TextView)findViewById(R.id.eEndDate);
        tv.setText(cursor.getString(index));

        index = cursor.getColumnIndex(DBOpenHelper.COURSE_STATUS);

        RadioGroup rg = (RadioGroup)findViewById(R.id.grpStatus);
        RadioButton rb;

        switch (cursor.getString(index)) {
            case "In Progress":
                rb =   (RadioButton) findViewById(R.id.rdoProgress);
                rb.setChecked(true);
                break;
            case "Completed":
                rb =   (RadioButton) findViewById(R.id.rdoComplete);
                rb.setChecked(true);
                break;
            case "Dropped":
                rb =   (RadioButton) findViewById(R.id.rdoDrop);
                rb.setChecked(true);
                break;
            case "Plan to take":
                rb =   (RadioButton) findViewById(R.id.rdoPlan);
                rb.setChecked(true);
                break;
        }

        CheckBox cb = (CheckBox) findViewById(R.id.cbStartNotify);
        cb.setChecked(getStartAlert(id));
        startNotifyBegin = cb.isChecked();


        CheckBox cb2 = (CheckBox) findViewById(R.id.cbEndNotify);
        cb2.setChecked(getEndAlert(id));
        endNotifyBegin = cb2.isChecked();

        //tv.setText(cursor.getString(index));
        //RadioGroup getDefault = (RadioGroup) findViewById(R.id.grpStatus);
        //RadioButton defaultButton = (RadioButton) findViewById(getDefault.getCheckedRadioButtonId());

        index = cursor.getColumnIndex(DBOpenHelper.COURSE_MENTOR_ID);


    }

    public void onClickEnd(View view) {
        CheckBox cb = (CheckBox) findViewById(R.id.cbEndNotify);
        endNotify = cb.isChecked();
    }

    public void onClickStart(View view) {
        CheckBox cb = (CheckBox) findViewById(R.id.cbStartNotify);
        startNotify = cb.isChecked();
    }
}
