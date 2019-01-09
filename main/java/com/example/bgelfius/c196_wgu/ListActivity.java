package com.example.bgelfius.c196_wgu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ListActivity extends Activity {
    public final static String DETAILTYPE_EXTRA="com.example.bgelfius.c196_wgu.detailType";

    private String passedVar = null;
    private TextView passedView=null;

    public PassableData po;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        passedVar=getIntent().getStringExtra(MainActivity.DETAILTYPE_EXTRA);
        detailList(passedVar);
    }

    public void detailList(String screen) {

        Cursor cursor = null;
        String[] from = null;
        DBProvider dbProvider = new DBProvider();
        int index = 0;
        CursorAdapter cursorAdapter;

        switch (screen) {
            case "Term":

                cursor = getContentResolver().query(dbProvider.TERM_CONTENT_URI,
                        DBOpenHelper.TERM_ALL_COLUMNS, null, null, null, null);

                from = new String[]{DBOpenHelper.TERM_TITLE};


                break;
            case "Course":
                 cursor = getContentResolver().query(dbProvider.COURSE_CONTENT_URI,
                        DBOpenHelper.COURSE_ALL_COLUMNS, null, null, null, null);
                from = new String[] {DBOpenHelper.COURSE_TITLE};


                break;
            case "Mentor":
                 cursor = getContentResolver().query(dbProvider.MENTOR_CONTENT_URI,
                        DBOpenHelper.MENTOR_ALL_COLUMNS, null, null, null, null);
                 from = new String[] {DBOpenHelper.MENTOR_NAME};


                 break;
            case "Assessment":
                cursor = getContentResolver().query(dbProvider.ASSESSMENT_CONTENT_URI,
                        DBOpenHelper.ASSESSMENT_ALL_COLUMNS, null, null, null, null);
                from = new String[] {DBOpenHelper.ASSESSMENT_NAME};


                break;

            default:
                break;
        }

        int[] to = {android.R.id.text1};
        cursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, cursor, from, to, 0);
       // po.setKeyID(cursor.getLong(index));

        TextView tv = (TextView) findViewById(R.id.detailName);
        tv.setText(screen);
        //tv.setTag(po);

        ListView list = (ListView) findViewById(R.id.detailList);
        list.setAdapter(cursorAdapter);
        list.setOnItemClickListener(detailItemListener);

    }

    private AdapterView.OnItemClickListener detailItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //String item = ((TextView)view).getText().toString();
            Intent intent = null;

            switch (passedVar) {
                case "Term":
                    intent = new Intent(ListActivity.this, TermDetailActivity.class);
                    intent.putExtra(DETAILTYPE_EXTRA,String.valueOf(l));
                    break;
                case "Course":
                    intent = new Intent(ListActivity.this, CourseDetailActivity.class);
                    intent.putExtra(DETAILTYPE_EXTRA,String.valueOf(l));
                    break;
                case "Assessment":
                    intent = new Intent(ListActivity.this, AssessmentDetailActivity.class);
                    intent.putExtra(DETAILTYPE_EXTRA,String.valueOf(l));
                    break;
                case "Mentor":
                    /*Intent intent = new Intent(ListActivity.this, TermDetailActivity.class);
                    intent.putExtra(DETAILTYPE_EXTRA,String.valueOf(l));*/
                    break;
                default:
                    break;
            }

            startActivity(intent);

        }
    };


    public void onBack(View view) {
        Intent intent = new Intent(ListActivity.this,  MainActivity.class);
        //intent.putExtra(DETAILTYPE_EXTRA,"Course");
        startActivity(intent);
    }
}
