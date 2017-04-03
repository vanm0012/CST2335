package cst2335.vanm0012.androidlabs;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class TestToolbar extends AppCompatActivity
{
    final static String ACTIVITY_NAME = "TestToolbar";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
                Log.d(ACTIVITY_NAME, "Starting action_one");
                action_performed = true;
                break;
            case R.id.action_two:
                Log.d(ACTIVITY_NAME, "Starting action_two");
                action_performed = true;
                break;
            case R.id.action_three:
                Log.d(ACTIVITY_NAME, "Starting action_three");
                action_performed = true;
                break;
        }

        return action_performed;
    }

}
