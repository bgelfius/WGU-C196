package com.example.bgelfius.c196_wgu;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.CursorAdapter;
import android.database.MatrixCursor;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditChangeTerm extends Activity {
    private String passedVar = null;
    private boolean updateMode = false;
    private String selectedStartDate;
    private String selectedEndDate;
    private ArrayList selectCourses = new ArrayList<Integer>();
    private ArrayList unSelectCourses = new ArrayList<Integer>();
    private Calendar cal;
    private int sDay;
    private int sMonth;
    private int sYear;
    private int eDay;
    private int eMonth;
    private int eYear;

    TextView sdv;
    TextView edv;
    ListView cList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editchangeterm);
        passedVar=getIntent().getStringExtra(MainActivity.DETAILTYPE_EXTRA);
        cal = Calendar.getInstance();

        sdv = (TextView)findViewById(R.id.eStartDate);
        edv = (TextView)findViewById(R.id.eEndDate);
        cList = (ListView)findViewById(R.id.eCourseList);

        sdv.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
                new DatePickerDialog(EditChangeTerm.this, sDatePicker, cal
                        .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        edv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditChangeTerm.this, eDatePicker, cal
                        .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        listCourses();

        if (passedVar.equals("AddTerm")) {
            ;
        } else {
            try {
                updateMode = true;
                getTermInformation(Long.parseLong(passedVar));
            } catch (ParseException e) {
                e.printStackTrace();
            }

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

    /*public void onCancel(View view) {
        Intent intent = new Intent(EditChangeTerm.this, MainActivity.class);
        //intent.putExtra(DETAILTYPE_EXTRA,String.valueOf(l));
        startActivity(intent);
    }*/

    void insertTerm(Term term) {

        DBProvider dbProvider = new DBProvider();

        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERM_TITLE, term.getTermTitle());
        values.put(DBOpenHelper.TERM_STARTDATE, term.getTermStartDate());
        values.put(DBOpenHelper.TERM_ENDDATE, term.getTermEndDate());
        //long insertId = dbProvider.insert(DBOpenHelper.TABLE_TERM, values);
        Uri termUri = getContentResolver().insert(dbProvider.TERM_CONTENT_URI,
                values);
        Log.d("EditChangeTerm", "Inserted term " + termUri.getLastPathSegment());


        // now associated the selected courses
        updateCourses();
    }

    void updateTerm(Term term) {

        DBProvider dbProvider = new DBProvider();

        ContentValues values = new ContentValues();
        String s =  " _id = " + String.valueOf(term.getTermID());
        String[] strings = null; //{term.getTermStartDate(), term.getTermEndDate(), term.getTermTitle()};


        //values.put(DBOpenHelper.TERM_ID, term.getTermID());
        values.put(DBOpenHelper.TERM_STARTDATE, term.getTermStartDate());
        values.put(DBOpenHelper.TERM_ENDDATE, term.getTermEndDate());
        values.put(DBOpenHelper.TERM_TITLE, term.getTermTitle());
        //long insertId = dbProvider.insert(DBOpenHelper.TABLE_TERM, values);
        int rc = getContentResolver().update(dbProvider.TERM_CONTENT_URI,
                values, s, strings);
        Log.d("EditChangeTerm", "updated term return code: " + rc);

        updateCourses();

    }

    void updateCourses() {
        DBProvider dbProvider = new DBProvider();
        ContentValues values = new ContentValues();
        int rc=0;
        //ListView lView;



        // the selected courses update
        for (int l = 0; l < selectCourses.size();l++) {

           // String s =  " _id = " + cList.getAdapter().getItemId(l);
            String s =  " _id = " + selectCourses.get(l);
            String[] strings = null; //{term.getTermStartDate(), term.getTermEndDate(), term.getTermTitle()};


            //values.put(DBOpenHelper.TERM_ID, term.getTermID());
            values.put(DBOpenHelper.COURSE_TERM_ID, Long.parseLong(passedVar));
            rc = getContentResolver().update(dbProvider.COURSE_CONTENT_URI,
                    values, s, strings);
            Log.d("EditChangeTerm", "updated course return code: " + rc);

        }

        // the unselected courses update
        for (int l = 0; l < unSelectCourses.size();l++) {

            String s =  " _id = " + unSelectCourses.get(l); //cList.getAdapter().getItemId(l);
            String[] strings = null; //{term.getTermStartDate(), term.getTermEndDate(), term.getTermTitle()};


            //values.put(DBOpenHelper.TERM_ID, term.getTermID());
            values.put(DBOpenHelper.COURSE_TERM_ID, 0);
            rc = getContentResolver().update(dbProvider.COURSE_CONTENT_URI,
                    values, s, strings);
            Log.d("EditChangeTerm", "updated course unselected return code: " + rc);

        }


    }

    void getTermInformation(long id) throws ParseException {
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
        TextView tv = (TextView)findViewById(R.id.eTermName);
        tv.setText(cursor.getString(index));

        index = cursor.getColumnIndex(DBOpenHelper.TERM_STARTDATE);
       // CalendarView cv = (CalendarView)findViewById(R.id.eStartDate);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//        sdf.parse(cursor.getString(index));
        sdv.setText(cursor.getString(index));

        /*Calendar c = sdf.getCalendar();
        long timeInMilli = c.getTimeInMillis();

        cv.setDate(timeInMilli);
*/
        //tv.setText(cursor.getString(index));

        index = cursor.getColumnIndex(DBOpenHelper.TERM_ENDDATE);
       // cv = (CalendarView)findViewById(R.id.eEndDate);
        sdf = new SimpleDateFormat("MM/dd/yyyy");
//        sdf.parse(cursor.getString(index));
        edv.setText(cursor.getString(index));

/*
        c = sdf.getCalendar();
        timeInMilli = c.getTimeInMillis();

        cv.setDate(timeInMilli);
*/



    }

    void listCourses() {
        DBProvider dbProvider = new DBProvider();

        String selClause;

        if (passedVar.equals("AddTerm")) {
            selClause = " term_id = 0 " ;
        } else {
            selClause = " term_id = 0  or term_id = " + Long.parseLong(passedVar) ;

        }

        String selArgs[] = {""};


        Cursor cursor = getContentResolver().query(dbProvider.COURSE_CONTENT_URI,
                DBOpenHelper.COURSE_ALL_COLUMNS, selClause, selArgs, null, null);
        String[] from = {DBOpenHelper.COURSE_TITLE};
        int[] to = {android.R.id.text1};



        CursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_multiple_choice, cursor, from, to, 0);
        // passedView.setText("count: " + cursor.getCount());
        cList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        cList.setAdapter(cursorAdapter);

        int columnIndex = cursor.getColumnIndex(DBOpenHelper.COURSE_TERM_ID);

        // mark any that currently are associated with this term
        for (int x =0; x < cursor.getCount(); x++) {
            cursor.moveToNext();
            if ( Integer.parseInt(cursor.getString(columnIndex)) > 0) {
                //cList.setSelection(x);
                cList.setItemChecked(x, true);
            }

        }


        //cList.setOnItemClickListener(courseItemListener);
    }

    public void onSave(View view) {
        EditText simpleEditText = (EditText) findViewById(R.id.eTermName);
        String strValue = simpleEditText.getText().toString();
        Term newTerm = new Term();

        newTerm.setTermTitle(simpleEditText.getText().toString());
        //CalendarView cv = (CalendarView) findViewById(R.id.eStartDate);
        //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        //String selectedDate = sdf.format(new Date(cv.getDate()));
        //newTerm.setTermStartDate(selectedDate);
        newTerm.setTermStartDate(sdv.getText().toString());
        //cv = (CalendarView) findViewById(R.id.eEndDate);
        //sdv = new SimpleDateFormat("MM/dd/yyyy");
        //selectedDate = sdf.format(new Date(cv.getDate()));
        newTerm.setTermEndDate(edv.getText().toString());


        SparseBooleanArray sCourses = cList.getCheckedItemPositions();
        Integer numCourses = cList.getCount();
        int idValue = 0;

        for (int x =0; x < numCourses; x++) {
            if (sCourses.get(x)) {
                selectCourses.add(cList.getAdapter().getItemId(x));
            } else {
                unSelectCourses.add(cList.getAdapter().getItemId(x));

            }

        }



        if (updateMode) {
            newTerm.setTermID(Integer.parseInt(passedVar));
            updateTerm(newTerm);
        } else {
            insertTerm(newTerm);
        }


        Intent intent = new Intent(EditChangeTerm.this, MainActivity.class);
        //intent.putExtra(DETAILTYPE_EXTRA,String.valueOf(l));
        startActivity(intent);

    }

}
