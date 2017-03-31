package cst2335.vanm0012.androidlabs;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MessageFragment extends Fragment
{
    protected TextView viewMessage;
    protected TextView viewID;
    protected Button deleteButton;

    protected ChatWindow chatWindow;

    public MessageFragment()
    {
        super();
    }

    public MessageFragment(ChatWindow chatWindow)
    {
        super();
        this.chatWindow = chatWindow;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_message_layout, container, false);
        final Bundle args = getArguments();

        viewMessage = (TextView) view.findViewById(R.id.message_detail_message);
        viewID = (TextView) view.findViewById(R.id.message_detail_id);
        deleteButton = (Button) view.findViewById(R.id.message_detail_btn_delete);

        String messageText = getString(R.string.message_detail_msg) + " " + args.getString("message");
        String idText = getString(R.string.message_detail_id) + " " + Long.toString(args.getLong("id"));

        viewMessage.setText(messageText);
        viewID.setText(idText);

        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (chatWindow == null)
                {
                    Intent msgDetailsIntent = new Intent(getActivity(), MessageFragment.class);
                    msgDetailsIntent.putExtra("msgID", args.getLong("id"));

                    getActivity().setResult(ChatWindow.REQUEST_MSG_DELETE, msgDetailsIntent);
                    getActivity().finish();
                }
                else
                {
                    chatWindow.deleteItem(args.getLong("id"));

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.remove(MessageFragment.this);
                    ft.commit();
                }
            }
        });

        return view;
    }
}
