package lootab.rememberapp;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import lootab.rememberapp.data.DbOpenHelper;
import lootab.rememberapp.data.InfoClass;
import lootab.rememberapp.list.FileLoadingActivity;

/**
 * Created by ichung-gi on 2015. 12. 26..
 */

public class ListAddActivity extends Activity implements View.OnClickListener, DialogInterface.OnClickListener {
    static final int NOTI_ID = 101;
    final int REQ_CODE_SELECT_IMAGE=100;
    NotificationManager mNotiMgr;
    EditText edit_title, edit_content, edit_word, edit_line;
    String titleStr, contentStr, wordStr, lineStr;
    private InfoClass mInfoClass;
    private ArrayList<InfoClass> mInfoArray;
    private Cursor mCursor;
    private DbOpenHelper mDbOpenHelper;


    private static final String TAG = "NotesDbAdapter";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_layout);
        // 알림 관리자를 구해서 멤버변수에 저장
        mNotiMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        findViewById(R.id.addImageBtn).setOnClickListener(this);
        findViewById(R.id.addFileBtn).setOnClickListener(this);

        edit_content = (EditText) findViewById(R.id.edit_content);
        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_word = (EditText) findViewById(R.id.edit_word);
        edit_line = (EditText) findViewById(R.id.edit_line);

    }

    public void Ok() {
        // 수행할 동작을 생성

        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mInfoArray = new ArrayList<InfoClass>();

        contentStr = edit_content.getText().toString();
        titleStr = edit_title.getText().toString();
        wordStr = edit_word.getText().toString();
        lineStr = edit_line.getText().toString();

        mDbOpenHelper.insertColumn(titleStr, contentStr, wordStr, lineStr);

        doWhileCursorToArray();

        InfoClass infoClass = new InfoClass(titleStr, contentStr, wordStr, lineStr);
        Intent intent = new Intent(ListAddActivity.this, ListViewActivity.class);
        intent.putExtra("infoClass", infoClass);
        startActivity(intent);

    }

    public void Cancel() {
        /*// 수행할 동작을 생성
        Intent intent = new Intent(this, NotiStopActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(ListAddActivity.this, 0, intent, 0);
        // 알림 객체 생성
        Notification noti = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker("New Message2")
                .setContentTitle("Noti - All")
                .setContentText("Other Activity")
                .setStyle(new Notification.BigTextStyle()
                        .bigText(+'\n' +
                                " nasdfjksadkfdsafjasdkldsnfk" +
                                "lsansdlknfasklnfsadlfnsdalkf" +
                                "sadfskajdfnskadfnsad" + '\n' +
                                "fsdnjkfnsdafkjnsdakfnjsadf" +
                                "sdanjfknsdkfnsadkfjnsdaf" +
                                "sadfnjksdanfjkasndfksadfn" +
                                "" + '\n' +
                                "" +
                                "asdfjnksdafnjksdafnsadkfsda" +
                                ""))
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pIntent)
                .build();
        // 알림 방식 지정
        noti.defaults |= Notification.DEFAULT_ALL;
        noti.flags |= Notification.FLAG_INSISTENT;
        // 알림 시작
        mNotiMgr.notify(NOTI_ID, noti);*/

        Intent intent = new Intent(ListAddActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch( v.getId() ) {
            case R.id.addImageBtn :
                // 이미지 버튼 누를 때 넘어가기
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
                break;
            case R.id.addFileBtn :
                Intent intent1 = new Intent(ListAddActivity.this, FileLoadingActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_ok :
                // onBtnNoti1();
                // 5초 후에 알림을 시작
                Ok();
                break;
            case R.id.btn_cancel :
                Cancel();
                break;
        }
    }

    // 선택한 이미지 데이터 받기기
   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getBaseContext(), "resultCode : " + resultCode, Toast.LENGTH_SHORT).show();
        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode==Activity.RESULT_OK)
            {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    //String name_Str = getImageNameToUri(data.getData());

                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap 	= MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    ImageView image = (ImageView)findViewById(R.id.imageView01);

                    //배치해놓은 ImageView에 set
                    image.setImageBitmap(image_bitmap);
                    //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onClick(DialogInterface dialog, int which){
    }

    /**
     * DB에서 받아온 값을 ArrayList에 Add
     */
    private void doWhileCursorToArray(){

        mCursor = null;
        mCursor = mDbOpenHelper.getAllColumns();
        Log.d("Main", "COUNT = " + mCursor.getCount());

        while (mCursor.moveToNext()) {

            mInfoClass = new InfoClass(
                    mCursor.getInt(mCursor.getColumnIndex("_id")),
                    mCursor.getString(mCursor.getColumnIndex("title")),
                    mCursor.getString(mCursor.getColumnIndex("content")),
                    mCursor.getString(mCursor.getColumnIndex("day")),
                    mCursor.getString(mCursor.getColumnIndex("number"))
            );

            mInfoArray.add(mInfoClass);
        }

        mCursor.close();
    }
}
