package cst2335.vanm0012.androidlabs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity
{
    protected static final String ACTIVITY_NAME = "ChatWindowActivity";
    protected ListView chatListView;
    protected EditText chatEditText;
    protected Button chatSendButton;
    protected ArrayList<String> chatMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        chatListView = (ListView) findViewById(R.id.chatListView);
        chatEditText = (EditText) findViewById(R.id.chatEditText);
        chatSendButton = (Button) findViewById(R.id.chatSendButton);
        chatMessages = new ArrayList<>();

        final ChatAdapter messageAdapter = new ChatAdapter(this);
        chatListView.setAdapter(messageAdapter);

        chatSendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // If the chat text field has text, add it to the messages array
                if (!chatEditText.getText().toString().trim().equals(""))
                {
                    chatMessages.add(chatEditText.getText().toString());
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

        @NonNull
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
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
}
