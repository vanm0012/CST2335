package cst2335.vanm0012.androidlabs;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import static java.lang.Math.toIntExact;

public class ChatWindow extends AppCompatActivity
{
    protected static final String ACTIVITY_NAME = "ChatWindowActivity";
    protected static final int REQUEST_MSG_DETAILS = 1;
    protected static final int REQUEST_MSG_DELETE = 2;

    protected SQLiteDatabase db;
    protected ChatDatabaseHelper dbHelper;

    protected ListView chatListView;
    protected EditText chatEditText;
    protected Button chatSendButton;
    protected ArrayList<String> chatMessages;
    protected ArrayList<Long> chatIDs;

    protected boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        String[] allColumns = {ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE};
        dbHelper = new ChatDatabaseHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();

        chatListView = (ListView) findViewById(R.id.chatListView);
        chatEditText = (EditText) findViewById(R.id.chatEditText);
        chatSendButton = (Button) findViewById(R.id.chatSendButton);
        chatMessages = new ArrayList<>();
        chatIDs = new ArrayList<>();

        final ChatAdapter messageAdapter = new ChatAdapter(this);
        chatListView.setAdapter(messageAdapter);

        isTablet = (findViewById(R.id.messageDetailFrame) != null);
        Log.i(ACTIVITY_NAME, "Is messageDetailFrame on the screen: " +
                isTablet);

        // Get any messages from the database
        Cursor cursor = db.query(ChatDatabaseHelper.TABLE_MESSAGES,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + cursor.getString(cursor.getColumnIndex(
                    ChatDatabaseHelper.KEY_MESSAGE)));
            chatMessages.add(cursor.getString(cursor.getColumnIndex(
                    ChatDatabaseHelper.KEY_MESSAGE)));
            chatIDs.add(cursor.getLong(cursor.getColumnIndex(
                ChatDatabaseHelper.KEY_ID)));
            cursor.moveToNext();
        }
        cursor.close();

        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Bundle newBundle = new Bundle();
                newBundle.putString("message", messageAdapter.getItem(position));
                newBundle.putLong("id", messageAdapter.getItemID(position));

                // Action if tablet
                if (isTablet)
                {
                    MessageFragment messageFragment = new MessageFragment(ChatWindow.this);
                    messageFragment.setArguments(newBundle);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.messageDetailFrame, messageFragment);
                    ft.commit();
                }
                else
                {
                    Intent msgDetailsIntent = new Intent(getApplicationContext(), MessageDetails.class);
                    msgDetailsIntent.putExtras(newBundle);
                    startActivityForResult(msgDetailsIntent, REQUEST_MSG_DETAILS);
                }
            }
        });

        chatSendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // If the chat text field has text, submit it
                if (!chatEditText.getText().toString().trim().equals(""))
                {
                    // add text to messages array
                    chatMessages.add(chatEditText.getText().toString());

                    // add text to database
                    ContentValues insertValues = new ContentValues();
                    insertValues.put(ChatDatabaseHelper.KEY_MESSAGE,
                            chatEditText.getText().toString());
                    // insert into the db while adding to the chatIDs array
                    chatIDs.add(db.insert(ChatDatabaseHelper.TABLE_MESSAGES, null, insertValues));

                    Log.i(ACTIVITY_NAME,
                            "Added string to chat messages " + chatEditText.getText().toString());

                    // Update the chat adapter
                    messageAdapter.notifyDataSetChanged();
                    // and clear the edit text
                    chatEditText.setText("");
                }
            }
        });
    }

    private class ChatAdapter extends ArrayAdapter<String>
    {
        private ChatAdapter(Context ctx)
        {
            super(ctx, 0);
        }

        public int getCount()
        {
            return chatMessages.size();
        }

        public String getItem(int position)
        {
            return chatMessages.get(position);
        }

        public Long getItemID(int position)
        {
            return chatIDs.get(position);
        }

        @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent)
        {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result;
            if (position % 2 == 0)
            {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            }
            else
            {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }
            TextView message = (TextView) result.findViewById(R.id.messageText);
            message.setText(getItem(position));
            return result;
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        db.close();
        dbHelper.close();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        if (resultCode == REQUEST_MSG_DELETE)
        {
            Long msgID = data.getLongExtra("msgID",-1);
            Log.i(ACTIVITY_NAME, "Request Deletion of message with id: " + Long.toString(msgID));
            deleteItem(msgID);
        }
    }

    public void deleteItem(long id)
    {
        db.delete(ChatDatabaseHelper.TABLE_MESSAGES, ChatDatabaseHelper.KEY_ID + "=" + id, null);

        int position = chatIDs.indexOf(id);
        chatMessages.remove(position);
        chatIDs.remove(position);

        final ChatAdapter adapter = (ChatAdapter) chatListView.getAdapter();
        adapter.notifyDataSetChanged();
    }
}
