package com.example.bgelfius.c196_wgu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CourseDetailActivity extends Activity {

    private String passedVar = null;
    public final static String DETAILTYPE_EXTRA="com.example.bgelfius.c196_wgu.detailType";
    private static Long currentCourseID;
    private String courseNotes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coursedetail);
        passedVar=getIntent().getStringExtra(MainActivity.DETAILTYPE_EXTRA);
        currentCourseID = Long.parseLong(passedVar);
        getCourseInformation(currentCourseID);
        getCourseNote(currentCourseID);
        listAssessmentsByCourse(currentCourseID);



    }

    void getCourseInformation(long id) {
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
        TextView tv = (TextView)findViewById(R.id.courseName);
        tv.setText(cursor.getString(index));

        index = cursor.getColumnIndex(DBOpenHelper.COURSE_STARTDATE);
        tv = (TextView)findViewById(R.id.courseStartDate);
        tv.setText(cursor.getString(index));
        CheckBox cb = (CheckBox) findViewById(R.id.chkStartNotify);
        cb.setChecked(getStartAlert(id));


        index = cursor.getColumnIndex(DBOpenHelper.COURSE_ENDDATE);
        tv = (TextView)findViewById(R.id.courseEndDate);
        tv.setText(cursor.getString(index));
        cb = (CheckBox) findViewById(R.id.chkEndNotify);
        cb.setChecked(getEndAlert(id));

        index = cursor.getColumnIndex(DBOpenHelper.COURSE_STATUS);
        tv = (TextView)findViewById(R.id.courseStatus);
        tv.setText(cursor.getString(index));

        index = cursor.getColumnIndex(DBOpenHelper.COURSE_MENTOR_ID);


        listMentorsByCourse(cursor.getLong(index));

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

    void getCourseNote(long id) {
        DBProvider dbProvider = new DBProvider();

        //String selClause = DBOpenHelper.COURSE_TERM_ID + " = ?";
        String selClause = " course_id =" + id;
        String selArgs[] = {""};


        Cursor cursor = getContentResolver().query(dbProvider.COURSENOTE_CONTENT_URI,
                DBOpenHelper.COURSENOTE_ALL_COLUMNS, selClause, selArgs, null, null);
        String[] from = {DBOpenHelper.COURSENOTE_NOTE};
        int[] to = {android.R.id.text1};

        CursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, cursor, from, to, 0);
        //ListView list = (ListView) findViewById(R.id.courses);
        //list.setAdapter(cursorAdapter);
        TextView tv = null;

        if (cursor.getCount() > 0) {
            int index = cursor.getColumnIndex(DBOpenHelper.COURSENOTE_NOTE);
            cursor.moveToNext();
            tv = (TextView) findViewById(R.id.courseNote);
            tv.setText(cursor.getString(index));
            courseNotes = cursor.getString(index);
        } else {
            tv = (TextView) findViewById(R.id.courseNote);
            tv.setText("N/A");
            courseNotes = "No notes available";
        }

    }

    void listMentorsByCourse(long id) {
        DBProvider dbProvider = new DBProvider();

        //String selClause = DBOpenHelper.MENTOR_ID  + " = ?";
        String selClause = " _id =" + id;
        String selArgs[] = {""};


        Cursor cursor = getContentResolver().query(dbProvider.MENTOR_CONTENT_URI,
                DBOpenHelper.MENTOR_ALL_COLUMNS, selClause, selArgs, null, null);
        String[] from = {DBOpenHelper.MENTOR_NAME};
        int[] to = {android.R.id.text1};

        if (cursor.getCount() > 0) {
            CursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1, cursor, from, to, 0);
            cursor.moveToNext();
            int index = cursor.getColumnIndex(DBOpenHelper.MENTOR_NAME);
            TextView tv = (TextView) findViewById(R.id.mentorName);
            tv.setText(cursor.getString(index));

            index = cursor.getColumnIndex(DBOpenHelper.MENTOR_PHONE);
            tv = (TextView) findViewById(R.id.mentorPhone);
            tv.setText(cursor.getString(index));

            index = cursor.getColumnIndex(DBOpenHelper.MENTOR_EMAIL);
            tv = (TextView) findViewById(R.id.mentorEmail);
            tv.setText(cursor.getString(index));
        }

    }

    void listAssessmentsByCourse(long id) {
        DBProvider dbProvider = new DBProvider();

        //String selClause = DBOpenHelper.ASSESSMENT_COURSE_ID  + " = ?";
        String selClause = " course_id = " + id;
        String selArgs[] = {""};


        Cursor cursor = getContentResolver().query(dbProvider.ASSESSMENT_CONTENT_URI,
                DBOpenHelper.ASSESSMENT_ALL_COLUMNS, selClause, selArgs, null, null);
        String[] from = {DBOpenHelper.ASSESSMENT_NAME};
        int[] to = {android.R.id.text1};

        CursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, cursor, from, to, 0);

        ListView list = (ListView) findViewById(R.id.assessmentList);
        list.setAdapter(null);
        list.setAdapter(cursorAdapter);
    }


    public void onAddNote(View view) {
        Intent intent = new Intent(CourseDetailActivity.this, EditChangeCourseNote.class);
        intent.putExtra(DETAILTYPE_EXTRA, String.valueOf(currentCourseID));
        startActivity(intent);
    }

    public void onBack(View view) {

/*
        Intent intent = new Intent(CourseDetailActivity.this,  EditChangeAssessment.class);
        intent.putExtra(DETAILTYPE_EXTRA,passedVar);
        startActivity(intent);*/
    }

    public void onEdit(View view) {

        Intent intent = new Intent(CourseDetailActivity.this, EditChangeCourse.class);
        intent.putExtra(DETAILTYPE_EXTRA,passedVar);
        startActivity(intent);
    }

    public void onShare(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, courseNotes);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Sharing CourseNotes Via"));
    }
}
