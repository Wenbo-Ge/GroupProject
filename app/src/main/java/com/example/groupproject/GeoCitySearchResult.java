package com.example.groupproject;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class GeoCitySearchResult extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_result);

        ListView cityListView = findViewById(R.id.resultListView);
        //cityListView.setAdapter();
/*
        loadDataFromDatabase();

        myListAdaptor = new MessageListAdaptor(this, R.layout.row_layout, msgList);
        chatListView.setAdapter(myListAdaptor);

        chatListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                onListItemCheck(position);
                return true;
            }
        });

        Button sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(click -> {
            Log.e(CHAT_ROOM_ACTIVITY, "In sendButton onClick");

            msgField = findViewById(R.id.msgField);
            String message = msgField.getText().toString();
            boolean isSent = true;

            // When message typed
            if(!message.isEmpty()) {
                ContentValues newRowValues = new ContentValues();
                newRowValues.put(MyOpener.COL_MESSAGE, message);
                newRowValues.put(MyOpener.COL_ISSENT, isSent);
                long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

                Log.e(CHAT_ROOM_ACTIVITY, "textField:  " + message);
                Log.e(CHAT_ROOM_ACTIVITY, "isSent:  " + isSent);
                msgList.add(new Message(isSent, message));
                Log.e(CHAT_ROOM_ACTIVITY, "msgList count:  " + msgList.size());
                myListAdaptor.notifyDataSetChanged();
                msgField.getText().clear();
            }
        });

        Button receiveButton = findViewById(R.id.receiveButton);
        receiveButton.setOnClickListener(click -> {
            Log.e(CHAT_ROOM_ACTIVITY, "In receiveButton onClick");

            msgField = findViewById(R.id.msgField);
            String message = msgField.getText().toString();
            boolean isSent = false;

            if(!message.isEmpty()) {
                ContentValues newRowValues = new ContentValues();
                newRowValues.put(MyOpener.COL_MESSAGE, message);
                newRowValues.put(MyOpener.COL_ISSENT, isSent);
                long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

                Log.e(CHAT_ROOM_ACTIVITY, "textField:  " + message);
                Log.e(CHAT_ROOM_ACTIVITY, "isSent:  " + isSent);
                msgList.add(new Message(isSent, message));
                Log.e(CHAT_ROOM_ACTIVITY, "msgList count:  " + msgList.size());
                myListAdaptor.notifyDataSetChanged();
                msgField.getText().clear();
            }
        });

 */
    }

    /*
    private void loadDataFromDatabase() {
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase();

        // Get all the columns of the table
        String [] columns = {MyOpener.COL_ID, MyOpener.COL_MESSAGE, MyOpener.COL_ISSENT};
        // Query all the results from the table
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);
        printCursor(results, db.getVersion());

        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);
        int messageIndex = results.getColumnIndex(MyOpener.COL_MESSAGE);
        int isSentIndex = results.getColumnIndex(MyOpener.COL_ISSENT);

        if(results.isFirst()) {
            do {
                String message = results.getString(messageIndex);
                boolean isSent = "1".equals(results.getString(isSentIndex));
                long id = results.getLong(idColIndex);

                msgList.add(new Message(id, isSent, message));
            } while (results.moveToNext());
        }
    }
*/
}
