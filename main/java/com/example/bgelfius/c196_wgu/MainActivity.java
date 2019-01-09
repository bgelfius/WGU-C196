package com.example.bgelfius.c196_wgu;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //private CursorAdapter cursorAdapter;
    public final static String DETAILTYPE_EXTRA="com.example.bgelfius.c196_wgu.detailType";
    //public final static String COURSEID_EXTRA="com.example.bgelfius.c196_wgu.courseID";
    private static boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (firstTime) {
            checkForAlert();
            firstTime = false;
        }

    }

    public void checkForAlert() {

        DBProvider dbProvider = new DBProvider();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String currDate = sdf.format(new Date());
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 1);
        String nextDay = sdf.format(c.getTime());


        String selClause = " alertDate >= '" + currDate + "'  and alertDate < '" + nextDay + "'";
        String selArgs[] = {""};



        Cursor cursor = getContentResolver().query(dbProvider.ALERT_CONTENT_URI,
                DBOpenHelper.ALERT_ALL_COLUMNS, selClause, selArgs, null, null);
        String[] from = {DBOpenHelper.ALERT_COURSEID, DBOpenHelper.ALERT_DATE, DBOpenHelper.ALERT_TYPE};
        int[] to = {};

        CursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, cursor, from, to, 0);

        int index = cursor.getColumnIndex(DBOpenHelper.ALERT_TYPE);
        cursor.moveToNext();
       // TextView tv = (TextView)findViewById(R.id.eTermName);
       // tv.setText(cursor.getString(index));
        if (cursor.getCount() > 0) {
            Toast.makeText(getApplicationContext(), "Alert: " + cursor.getString(index), Toast.LENGTH_LONG).show();
        } else {
            ;
        }







    }

    public void listTerms(View view) {

        //list.setOnItemClickListener(termItemListener);
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        intent.putExtra(DETAILTYPE_EXTRA,"Term");
        startActivity(intent);

    }
    void insertTerm(Term term) {

        DBProvider dbProvider = new DBProvider();

        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERM_TITLE, term.getTermTitle());
        values.put(DBOpenHelper.TERM_STARTDATE, term.getTermStartDate());
        values.put(DBOpenHelper.TERM_ENDDATE, term.getTermEndDate());
        //long insertId = dbProvider.insert(DBOpenHelper.TABLE_TERM, values);
        Uri termUri = getContentResolver().insert(dbProvider.TERM_CONTENT_URI,
                values);
        Log.d("MainActivity", "Inserted term " + termUri.getLastPathSegment());

    }

    public void listCourses(View view) {
        //list.setOnItemClickListener(courseItemListener);
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        intent.putExtra(DETAILTYPE_EXTRA,"Course");
        startActivity(intent);


    }


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
        Log.d("MainActivity", "Inserted course " + courseUri.getLastPathSegment());

    }

    void insertCourseNote(CourseNote courseNote) {

        DBProvider dbProvider = new DBProvider();
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSENOTE_NOTE, courseNote.getCourseNoteNote());
        values.put(DBOpenHelper.COURSENOTE_COURSE_ID, courseNote.getCourseID());
        Uri courseNoteUri = getContentResolver().insert(dbProvider.COURSENOTE_CONTENT_URI,
                values);
        Log.d("MainActivity", "Inserted course note " + courseNoteUri.getLastPathSegment());

    }


    public void listMentors(View view) {

        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        intent.putExtra(DETAILTYPE_EXTRA,"Mentor");
        startActivity(intent);

    }
    void insertMentor(Mentor mentor) {

        DBProvider dbProvider = new DBProvider();
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.MENTOR_NAME, mentor.getMentorName());
        values.put(DBOpenHelper.MENTOR_PHONE, mentor.getMentorPhone());
        values.put(DBOpenHelper.MENTOR_EMAIL, mentor.getMentorEmail());
        Uri mentorUri = getContentResolver().insert(dbProvider.MENTOR_CONTENT_URI,
                values);
        Log.d("MainActivity", "Inserted mentor " + mentorUri.getLastPathSegment());

    }

    void listAssessments(View view) {
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        intent.putExtra(DETAILTYPE_EXTRA,"Assessment");
        startActivity(intent);
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
        Log.d("MainActivity", "Inserted assessment " + assessmentUri.getLastPathSegment());

    }

    void insertAlert(Alert alert) {

        DBProvider dbProvider = new DBProvider();
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ALERT_TYPE, alert.getAlertType());
        values.put(DBOpenHelper.ALERT_COURSEID, alert.getCourseID());
        values.put(DBOpenHelper.ALERT_DATE, alert.getAlertDate());
        Uri alertUri = getContentResolver().insert(dbProvider.ALERT_CONTENT_URI,
                values);
        Log.d("MainActivity", "Inserted alert " + alertUri.getLastPathSegment());

    }

    public void addTerm(View view) {
        //list.setOnItemClickListener(courseItemListener);
        Intent intent = new Intent(MainActivity.this, EditChangeTerm.class);
        intent.putExtra(DETAILTYPE_EXTRA,"AddTerm");
        startActivity(intent);

    }

    public void addCourse(View view) {
        Intent intent = new Intent(MainActivity.this, EditChangeCourse.class);
        intent.putExtra(DETAILTYPE_EXTRA,"AddCourse");
        startActivity(intent);

    }

    public void addMentor(View view) {
        Intent intent = new Intent(MainActivity.this, EditChangeMentor.class);
        intent.putExtra(DETAILTYPE_EXTRA,"AddMentor");
        startActivity(intent);
    }

    public void addAssessment(View view) {
        Intent intent = new Intent(MainActivity.this, EditChangeAssessment.class);
        intent.putExtra(DETAILTYPE_EXTRA,"AddAssessment");
        startActivity(intent);
    }

    public void addAlert(View view) {
    }

    public void addTestData(View view) {
        //build some terms
        Term term = new Term("Term1", "01/01/2018", "06/01/2018");
        insertTerm(term);
        term = new Term("Term2", "06/01/2018", "12/01/2018");
        insertTerm(term);
        term = new Term("Term3", "12/01/2018", "05/01/2019");
        insertTerm(term);

        Mentor mentor = new Mentor("Mentor Name1", "123-112-1234", "name1@wgu.edu");
        insertMentor(mentor);
        mentor = new Mentor("Mentor Name2", "321-412-4321", "name2@wgu.edu");
        insertMentor(mentor);
        mentor = new Mentor("Mentor Name3", "725-132-4534", "name3@wgu.edu");
        insertMentor(mentor);

        Course course   = new Course("(A101) - Application Development", 1, 1,"01/01/2018", "02/01/2018", "In Progress");
        insertCourse(course);
        course   = new Course("(A102) - Application Refactoring", 2, 2,"02/01/2018", "03/01/2018", "Completed");
        insertCourse(course);
        course   = new Course("(A103) - Application Sunsetting", 3,2, "03/01/2018", "04/01/2018", "Planned to Take");
        insertCourse(course);

        CourseNote courseNote = new CourseNote("How to build an application", 1L);
        insertCourseNote(courseNote);

        Assessment assessment = new Assessment(1L, "Objective Assessment", "Application Development Objective", "12/23/2018");
        insertAssessment(assessment);
        assessment = new Assessment(2L, "Performance Assessment", "Application Refactoring Performance", "12/30/2018");
        insertAssessment(assessment);
        assessment = new Assessment(3L, "Objective Assessment", "Application Sunsetting Objective", "12/24/2018");
        insertAssessment(assessment);
        assessment = new Assessment(3L, "Performance Assessment", "Application Sunsetting Performance", "12/26/2018");
        insertAssessment(assessment);

        Alert alert  = new Alert("Performance Assessment","12/23/2018", 1L );
        insertAlert(alert);
        alert  = new Alert("Objective Assessment","12/24/2018", 3L );
        insertAlert(alert);

    }

    public void onExit(View view) {
        finish();
    }
}

