package com.example.bgelfius.c196_wgu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class TermDetailActivity extends Activity {
    public final static String DETAILTYPE_EXTRA="com.example.bgelfius.c196_wgu.detailType";
    private String passedVar = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termdetail);
        passedVar=getIntent().getStringExtra(MainActivity.DETAILTYPE_EXTRA);
        getTermInformation(Long.parseLong(passedVar));
        listCoursesByTerm(Long.parseLong(passedVar));


    }


    void getTermInformation(long id) {
        DBProvider dbProvider = new DBProvider();

        //String selClause = DBOpenHelper.COURSE_TERM_ID + " = ?";
        String selClause = " _id =" + id;
        String selArgs[] = {""};


        Cursor cursor = getContentResolver().query(dbProvider.TERM_CONTENT_URI,
                DBOpenHelper.TERM_ALL_COLUMNS, selClause, selArgs, null, null);
        String[] from = {DBOpenHelper.TERM_TITLE, DBOpenHelper.TERM_STARTDATE, DBOpenHelper.TERM_ENDDATE};
        int[] to = {android.R.id.text1};

        CursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, cursor, from, to, 0);

        int index = cursor.getColumnIndex(DBOpenHelper.TERM_TITLE);
        cursor.moveToNext();
        TextView tv = (TextView)findViewById(R.id.termName);
        tv.setText(cursor.getString(index));

        index = cursor.getColumnIndex(DBOpenHelper.TERM_STARTDATE);
        tv = (TextView)findViewById(R.id.termStartDate);
        tv.setText(cursor.getString(index));

        index = cursor.getColumnIndex(DBOpenHelper.TERM_ENDDATE);
        tv = (TextView)findViewById(R.id.termEndDate);
        tv.setText(cursor.getString(index));



    }

    void listCoursesByTerm(long id) {
        DBProvider dbProvider = new DBProvider();

        //String selClause = DBOpenHelper.COURSE_TERM_ID + " = ?";
        String selClause = " term_id =" + id;
        String selArgs[] = {""};


        Cursor cursor = getContentResolver().query(dbProvider.COURSE_CONTENT_URI,
                DBOpenHelper.COURSE_ALL_COLUMNS, selClause, selArgs, null, null);
        String[] from = {DBOpenHelper.COURSE_TITLE};
        int[] to = {android.R.id.text1};

        CursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, cursor, from, to, 0);
        // passedView.setText("count: " + cursor.getCount());
        ListView list = (ListView) findViewById(R.id.courseList);
        list.setAdapter(cursorAdapter);
        list.setOnItemClickListener(courseItemListener);
    }
    private AdapterView.OnItemClickListener courseItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //String item = ((TextView)view).getText().toString();
            Intent intent = new Intent(TermDetailActivity.this, CourseDetailActivity.class);
            intent.putExtra(DETAILTYPE_EXTRA,String.valueOf(l));
            startActivity(intent);

        }
    };

    public void editTerm(View view) {
        Intent intent = new Intent(TermDetailActivity.this, EditChangeTerm.class);
        intent.putExtra(DETAILTYPE_EXTRA,passedVar);
        startActivity(intent);
    }

    public void deleteTerm(View view) {
        DBProvider dbProvider = new DBProvider();

        String s = " _id= " + passedVar;
        String [] strings = null;

        // check to see if term has any courses

        //String selClause = DBOpenHelper.COURSE_TERM_ID + " = ?";
        String selClause = " term_id =" + passedVar;
        String selArgs[] = {""};


        Cursor cursor = getContentResolver().query(dbProvider.COURSE_CONTENT_URI,
                DBOpenHelper.COURSE_ALL_COLUMNS, selClause, selArgs, null, null);
        String[] from = {DBOpenHelper.COURSE_TITLE};
        int[] to = {android.R.id.text1};

        CursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, cursor, from, to, 0);

        if (cursorAdapter.getCount() > 0) {
            Toast.makeText(getApplicationContext(), "Term not empty, can't be deleted", Toast.LENGTH_SHORT).show();
        } else {
            int rc = getContentResolver().delete(dbProvider.TERM_CONTENT_URI  ,s, strings);
            Log.d("EditChangeTerm", "updated term " + passedVar);
            Intent intent = new Intent(TermDetailActivity.this, MainActivity.class);
            intent.putExtra(DETAILTYPE_EXTRA,passedVar);
            startActivity(intent);

        }

        //


    }
}
