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

public class AssessmentDetailActivity extends Activity {
    private String passedVar = null;
    private static Long currentCourseID;
    public final static String DETAILTYPE_EXTRA="com.example.bgelfius.c196_wgu.detailType";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessmentdetail);
        passedVar=getIntent().getStringExtra(MainActivity.DETAILTYPE_EXTRA);
        getAssessmentInformation(Long.parseLong(passedVar));

    }
    void getAssessmentInformation(long id) {
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
        TextView tv = (TextView)findViewById(R.id.assessmentName);
        tv.setText(cursor.getString(index));

        index = cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_TYPE);
        tv = (TextView)findViewById(R.id.assessmentType);
        tv.setText(cursor.getString(index));

        index = cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_DATE);
        tv = (TextView)findViewById(R.id.assessmentDate);
        tv.setText(cursor.getString(index));



        index = cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_COURSE_ID);
        tv = (TextView)findViewById(R.id.assessmentCourseName);
        tv.setText(getCourseName( Long.parseLong(cursor.getString(index)))   );

        CheckBox cb = (CheckBox) findViewById(R.id.chkAssessmentNotify);
        cb.setChecked(getAssessmentAlert(Long.parseLong(cursor.getString(index))));


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

    void listAssessmentsByAssessmentID(long id) {
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

    public void onEditAssessment(View view) {
        Intent intent = new Intent(AssessmentDetailActivity.this, EditChangeAssessment.class);
        intent.putExtra(DETAILTYPE_EXTRA,passedVar);
        startActivity(intent);
    }
}
