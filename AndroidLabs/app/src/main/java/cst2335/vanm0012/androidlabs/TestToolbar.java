package cst2335.vanm0012.androidlabs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity
{
    final static String ACTIVITY_NAME = "TestToolbar";

    protected  String action_one_msg;

    protected FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        action_one_msg = "You Selected item 1";

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Finished sending spam emails", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void setActionOneMsg(String newMsg)
    {
        this.action_one_msg = newMsg;
    }

    public boolean onCreateOptionsMenu(Menu m)
    {
        getMenuInflater().inflate(R.menu.toolbar_menu, m);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi)
    {
        boolean action_performed = false;

        switch (mi.getItemId())
        {
            case R.id.action_one:
                Log.i(ACTIVITY_NAME, "Starting action_one");
                Snackbar.make(findViewById(R.id.activity_test_toolbar), action_one_msg, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                action_performed = true;
                break;
            case R.id.action_two:
                Log.i(ACTIVITY_NAME, "Starting action_two");
                AlertDialog.Builder action_two_builder = new AlertDialog.Builder(this)
                        .setTitle(R.string.test_toolbar_action_two_dialog_title)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.cancel();
                            }
                        });
                AlertDialog action_two_dialog = action_two_builder.create();
                action_two_dialog.show();
                action_performed = true;
                break;
            case R.id.action_three:
                Log.i(ACTIVITY_NAME, "Starting action_three");
                LayoutInflater inflater =
                        (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_test_toolbar_action_three, null);

                final EditText newMsg =
                        (EditText) dialogView.findViewById(R.id.test_toolbar_action_three_new_msg);

                AlertDialog.Builder action_three_builder = new AlertDialog.Builder(this)
                        .setView(dialogView)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                setActionOneMsg(newMsg.getText().toString());
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.cancel();
                            }
                        });
                AlertDialog action_three_dialog = action_three_builder.create();
                action_three_dialog.show();
                action_performed = true;
                break;
            case R.id.action_about:
                Log.i(ACTIVITY_NAME, "Starting action_about");
                Toast.makeText(this, R.string.toolbar_about_string, Toast.LENGTH_SHORT).show();
                action_performed = true;
                break;
        }

        return action_performed;
    }

}
