package lootab.rememberapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import lootab.rememberapp.data.InfoClass;

/**
 * Created by ichung-gi on 2016. 1. 26..
 */
public class ListViewActivity extends Activity{

    Bundle bundle;
    InfoClass infoClass;
    TextView tv_title, tv_content;
    EditText edit_word, edit_number;
    String titleStr, contentStr, wordStr, numberStr;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_layout);

        bundle = getIntent().getExtras();
        infoClass = bundle.getParcelable("infoClass");

        titleStr = infoClass.title;
        contentStr = infoClass.content;
        wordStr = infoClass.day;
        numberStr = infoClass.number;


        tv_title = (TextView) findViewById(R.id.tv_view_title);
        tv_title.setText(titleStr);
        tv_content = (TextView) findViewById(R.id.tv_view_content);
        tv_content.setText(contentStr);
        edit_number = (EditText) findViewById(R.id.edit_view_number);
        edit_word = (EditText) findViewById(R.id.edit_view_word);

        

    }



}

