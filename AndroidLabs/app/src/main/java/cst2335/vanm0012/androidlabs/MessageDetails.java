package cst2335.vanm0012.androidlabs;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MessageDetails extends AppCompatActivity
{
    protected FrameLayout fl;
    protected MessageFragment messageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        fl = new FrameLayout(this);
        fl.setId(R.id.msg_details_frame);
        setContentView(fl);

        messageFragment= new MessageFragment();
        messageFragment.setArguments(getIntent().getExtras());

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.msg_details_frame, messageFragment);
        ft.commit();
    }
}
