package lootab.rememberapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import lootab.rememberapp.data.DataBases;
import lootab.rememberapp.data.DbOpenHelper;
import lootab.rememberapp.data.InfoClass;
//import lootab.rememberapp.list.CustomAdapter;
import lootab.rememberapp.util.FloatingActionButton;

public class MainActivity extends ActionBarActivity {
    private static final String TAG = "MainActivty";
    private Cursor mCursor;
    private InfoClass mInfoClass;
    private ArrayList<InfoClass> mInfoArray;
  //  private CustomAdapter mAdapter;
    private EditText[] mEditTexts;
    private List<String> strList = new ArrayList<>();
    private String titleStr, contentStr, wordStr, lineStr, appName = "RememberApp";;

    private String[] navItems = { "Brown", "Cadet Blue", "Dark Olive Green",
            "Dark Orange", "Golden Rod", "White" };
    private ListView lvNavList;
    private FrameLayout flContainer;

    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle dtToggle;
    private boolean isopen = false;

    public static DataBases mDatabase = null;
    private DbOpenHelper mDbOpenHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();

        mInfoArray = new ArrayList<InfoClass>();

        doWhileCursorToArray();

        for(InfoClass i : mInfoArray){
            Log.d(TAG, "ID = " + i._id);
            Log.d(TAG, "title = " + i.title);
            Log.d(TAG, "content = " + i.content);
            Log.d(TAG, "day = " + i.day);
            Log.d(TAG, "number = " + i.number);
        }

        /*mListView = (ListView) findViewById(R.id.listview);
        mAdapter = new CustomAdapter(this, mInfoArray);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemLongClickListener(longClickListener);*/

        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF339999));
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //TODO List 에 Info 넣던가, adapter에 붙이던
        SwipeMenuListView listView = (SwipeMenuListView)findViewById(R.id.listView);
        final CustomArrayAdapter adapter = new CustomArrayAdapter(this, R.layout.list_item, strList);
        listView.setAdapter(adapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                //create an action that will be showed on swiping an item in the list
                SwipeMenuItem item1 = new SwipeMenuItem(
                        getApplicationContext());
                item1.setBackground(new ColorDrawable(Color.DKGRAY));
                // set width of an option (px)
                item1.setWidth(200);
                item1.setTitle("Open");
                item1.setTitleSize(18);
                item1.setTitleColor(Color.WHITE);
                menu.addMenuItem(item1);

                SwipeMenuItem item2 = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                item2.setBackground(new ColorDrawable(Color.RED));
                item2.setWidth(200);
                item2.setTitle("Delete");
                item2.setTitleSize(18);
                item2.setTitleColor(Color.WHITE);
                menu.addMenuItem(item2);
            }
        };
        //set MenuCreator
        listView.setMenuCreator(creator);
        // set SwipeListener
        listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start

            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        listView.setOnItemClickListener(new SwipeMenuListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Open for View" , Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                String value = adapter.getItem(position);
                switch (index) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "Open for "+ value , Toast.LENGTH_SHORT).show();
                        mInfoArray.get(position);
                        InfoClass infoClass = new InfoClass(titleStr, contentStr, wordStr, lineStr);
                        Intent intent = new Intent(MainActivity.this, ListViewActivity.class);
                        intent.putExtra("infoClass", infoClass);
                        startActivity(intent);
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "Delete for "+ value , Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }});

        FloatingActionButton mFloatingButton = (FloatingActionButton) findViewById(R.id.mFloatingActionButton);
        mFloatingButton.attachToListView(listView);
        mFloatingButton.setOnClickListener(clickListener);
        Log.d("clickListener", "mListView");


        lvNavList = (ListView) findViewById(R.id.lv_activity_main_nav_list);
        flContainer = (FrameLayout) findViewById(R.id.realcontents);

        lvNavList.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, navItems));
        lvNavList.setOnItemClickListener(new DrawerItemClickListener());


        dlDrawer = (DrawerLayout)findViewById(R.id.dl_main_drawer);

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mFloatingActionButton:
                    startActivity(new Intent(MainActivity.this, ListAddActivity.class));
                    finish();
                    break;
            }
        }
    };

    /*
     * Layout
     */

    /**
     * DB에서 받아온 값을 ArrayList에 Add
     */
    private void doWhileCursorToArray(){

        mCursor = null;
        mCursor = mDbOpenHelper.getAllColumns();
        Log.d("Main", "COUNT = " + mCursor.getCount());

        while (mCursor.moveToNext()) {

            Log.d("mCursor", " " + mCursor.getColumnIndex("_id"));
            Log.d("mCursor", " " + mCursor.getColumnIndex("title"));
            Log.d("mCursor", " " + mCursor.getColumnIndex("content"));
            Log.d("mCursor", " " + mCursor.getColumnIndex("day"));
            Log.d("mCursor", " " + mCursor.getColumnIndex("number"));

            Log.d("main", " " + DataBases.CreateDB._CREATE);

            mInfoClass = new InfoClass(
                    mCursor.getInt(mCursor.getColumnIndex("_id")),
                    mCursor.getString(mCursor.getColumnIndex("title")),
                    mCursor.getString(mCursor.getColumnIndex("content")),
                    mCursor.getString(mCursor.getColumnIndex("day")), // 단어 수
                    mCursor.getString(mCursor.getColumnIndex("number")) // 줄 수
            );

            strList.add(mCursor.getString(mCursor.getColumnIndex("title")));
            mInfoArray.add(mInfoClass);
        }

        mCursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();

        if (id == R.id.action_button){
            if (dlDrawer.isDrawerOpen(Gravity.LEFT)) {
                dlDrawer.closeDrawer(Gravity.LEFT);
            } else {
                dlDrawer.openDrawer(Gravity.LEFT);
            }
            isopen = !isopen;
            return true;
        }

        return  super.onOptionsItemSelected(item);
    }

    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
//		dtToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//		dtToggle.onConfigurationChanged(newConfig);
    }


    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view,
                                int position, long id) {
            switch (position) {
                case 0:
                    flContainer.setBackgroundColor(Color.parseColor("#A52A2A"));
                    break;
                case 1:
                    flContainer.setBackgroundColor(Color.parseColor("#5F9EA0"));
                    break;
                case 2:
                    flContainer.setBackgroundColor(Color.parseColor("#556B2F"));
                    break;
                case 3:
                    flContainer.setBackgroundColor(Color.parseColor("#FF8C00"));
                    break;
                case 4:
                    flContainer.setBackgroundColor(Color.parseColor("#DAA520"));
                    break;
                case 5:
                    flContainer.setBackgroundColor(Color.parseColor("white"));
            }
            dlDrawer.closeDrawer(lvNavList);

        }

    }

    public void openDatabase(){
        if (mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }

        mDatabase = DataBases.getInstance(this);
        boolean isOpen = mDatabase.open();
        if (isOpen) {
            Log.d(TAG, "Memo database is open.");
        } else {
            Log.d(TAG, "Memo database is not open.");
        }
    }

   /* public int loadMemoListData() {
        String SQL = "select _id, " + TITLE + ", " + CONTENT + ", " + DAY + ", " + NUMBER + " from " + _TABLENAME + " order by INPUT_DATE desc";

        int recordCount = -1;
        if (mDatabase != null) {
            Cursor outCursor = mDatabase.rawQuery(SQL);

            recordCount = outCursor.getCount();
            Log.d(TAG, "cursor count : " + recordCount + "\n");

            mMemoListAdapter.clear();
            Resources res = getResources();

            for (int i = 0; i < recordCount; i++) {
                outCursor.moveToNext();

                String memoId = outCursor.getString(0);

                String dateStr = outCursor.getString(1);
                if (dateStr.length() > 10) {
                    dateStr = dateStr.substring(0, 10);
                }

                String memoStr = outCursor.getString(2);
                String photoId = outCursor.getString(3);
                String photoUriStr = getPhotoUriStr(photoId);

                String videoId = outCursor.getString(4);
                String videoUriStr = null;

                String voiceId = outCursor.getString(5);
                String voiceUriStr = null;

                String handwritingId = outCursor.getString(6);
                String handwritingUriStr = null;

                // Stage3 added
                handwritingUriStr = getHandwritingUriStr(handwritingId);

                mMemoListAdapter.addItem(new MemoListItem(memoId, dateStr, memoStr, handwritingId, handwritingUriStr, photoId, photoUriStr, videoId, videoUriStr, voiceId, voiceUriStr));
            }

            outCursor.close();

            mMemoListAdapter.notifyDataSetChanged();
        }

        return recordCount;
    }*/

}
