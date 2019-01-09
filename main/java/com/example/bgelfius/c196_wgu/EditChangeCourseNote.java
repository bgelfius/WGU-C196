package com.example.bgelfius.c196_wgu;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class EditChangeCourseNote extends Activity {
    private String passedVar = null;
    private static Long currentCourseID;
    private static Long currentCourseNoteID;
    public final static String DETAILTYPE_EXTRA="com.example.bgelfius.c196_wgu.detailType";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editchangecoursenote);
        passedVar=getIntent().getStringExtra(CourseDetailActivity.DETAILTYPE_EXTRA);
        currentCourseID = Long.parseLong(passedVar);
        getCourseInformation(currentCourseID);
        getCourseNote(currentCourseID);

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
        TextView tv = (TextView)findViewById(R.id.eCourseNameNote);
        tv.setText(cursor.getString(index));


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
        TextView tv = null;

        if (cursor.getCount() > 0) {
            int index = cursor.getColumnIndex(DBOpenHelper.COURSENOTE_NOTE);
            cursor.moveToNext();
            tv = (TextView) findViewById(R.id.eCourseNote);
            tv.setText(cursor.getString(index));
            index = cursor.getColumnIndex(DBOpenHelper.COURSENOTE_ID);
            currentCourseNoteID = cursor.getLong(index);
        } else {
            currentCourseNoteID = 0L;
        }



    }


    void insertCourseNote(CourseNote courseNote) {

        DBProvider dbProvider = new DBProvider();
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSENOTE_NOTE, courseNote.getCourseNoteNote());
        values.put(DBOpenHelper.COURSENOTE_COURSE_ID, courseNote.getCourseID());
        Uri courseNoteUri = getContentResolver().insert(dbProvider.COURSENOTE_CONTENT_URI,
                values);
        Log.d("EditChangeCourseNote", "Inserted course note " + courseNoteUri.getLastPathSegment());

    }

    void changeCourseNote(CourseNote courseNote) {

        DBProvider dbProvider = new DBProvider();
        ContentValues values = new ContentValues();
        String selClause = " _id = ? "; // + currentCourseNoteID;
        String selArgs[] = {currentCourseNoteID.toString()};

        values.put(DBOpenHelper.COURSENOTE_NOTE, courseNote.getCourseNoteNote());
        values.put(DBOpenHelper.COURSENOTE_ID, courseNote.getCourseNoteID());

        int returnCode = getContentResolver().update(dbProvider.COURSENOTE_CONTENT_URI, values, selClause, selArgs);
       Log.d("EditChangeCourseNote", "Inserted course note: " + returnCode  );

    }

    public void onSaveNote(View view) {
        EditText simpleEditText = (EditText) findViewById(R.id.eCourseNote);
        String strValue = simpleEditText.getText().toString();
        CourseNote newNote = new CourseNote();
        newNote.setCourseNoteNote(strValue);
        newNote.setCourseID(currentCourseID);
        if (currentCourseNoteID > 0) {
            newNote.setCourseNoteID(currentCourseNoteID);
            changeCourseNote(newNote);
        } else {
            insertCourseNote(newNote);
        }
        Intent intent = new Intent(EditChangeCourseNote.this, CourseDetailActivity.class);
        intent.putExtra(DETAILTYPE_EXTRA,String.valueOf(currentCourseID));
        startActivity(intent);
    }
}
