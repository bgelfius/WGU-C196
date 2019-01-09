package com.example.bgelfius.c196_wgu;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditChangeMentor  extends Activity {

    private String passedVar = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_editchangementor);
       // passedVar=getIntent().getStringExtra(MainActivity.DETAILTYPE_EXTRA);

    }

    public void onSaveMentor(View view) {
        EditText simpleEditText = (EditText) findViewById(R.id.eMentorName);
        String strValue = simpleEditText.getText().toString();
        Mentor newMentor = new Mentor();

        newMentor.setMentorName(strValue);

        simpleEditText = (EditText) findViewById(R.id.eMentorEmail);
        strValue = simpleEditText.getText().toString();
        newMentor.setMentorEmail(strValue);

        simpleEditText = (EditText) findViewById(R.id.eMentorPhone);
        strValue = simpleEditText.getText().toString();
        newMentor.setMentorPhone(strValue);


        insertMentor(newMentor);
        Intent intent = new Intent(EditChangeMentor.this, MainActivity.class);
        //intent.putExtra(DETAILTYPE_EXTRA,String.valueOf(l));
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

}
