/*
 * Copyright (c) 2016. David de Andrés and Juan Carlos Ruiz, DISCA - UPV, Development of apps for mobile devices.
 */

package labs.dadm.l0203_eventsandintents;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Request code used when waiting for an activity to return a result
    private final static int GET_MESSAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // onClickListener association by code
        Button notificationButton = findViewById(R.id.bShowNotification);
        notificationButton.setOnClickListener(v -> {
            // Displays a quick little message in a popup
            Toast.makeText(MainActivity.this, R.string.notification_message, Toast.LENGTH_SHORT).show();
        });
    }

    /*
        onClickListener association by the onClick attribute
        This method will manage explicit Intents
    */
    public void launchNewActivity(View v) {

        Intent intent;

        // Determine what to do depending on the Button clicked
        final int buttonClicked = v.getId();
        if (buttonClicked == R.id.bExplicitIntent) {
            // Nothing special, just launch the new activity
            // Explicit Intent to launch the ExplicitIntentActivity
            intent = new Intent(MainActivity.this, ExplicitIntentActivity.class);
            startActivity(intent);
        } else if (buttonClicked == R.id.bExplicitIntentWithParameter) {
            // Pass a parameter within the Intent
            // Explicit Intent to launch the ExplicitIntentActivity
            intent = new Intent(MainActivity.this, ExplicitIntentActivity.class);
            // Include the value of a String called "message" as parameter
            intent.putExtra("message", "Hello this is David!");
            startActivity(intent);
        } else if (buttonClicked == R.id.bExplicitIntentForResult) {
            // Launch the new activity and wait for a result
            // Explicit Intent to launch the ForResultActivity
            intent = new Intent(MainActivity.this, ForResultActivity.class);
            startActivityForResult(intent, GET_MESSAGE);
        }
    }

    // This callback will be triggered when a previously launched activity returns a result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    /*
        All the activities launched for result will trigger this method,
        so we need to check what we asked the activities to do, to know
        which result we are getting
    */
        if (requestCode == GET_MESSAGE) {
            // We can also check the result code provided by the returning activity
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, data.getStringExtra("result"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
        onClickListener association by the onClick attribute
        This method will manage implicit Intents
    */
    public void browseWebsite(View v) {

        // Intent to access the UPV's website
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.upv.es"));

        // Determine what to do depending on the Button clicked
        final int buttonClicked = v.getId();
        if (buttonClicked == R.id.bImplicitIntentSystemChooses) {
            // Let the system choose the application to launch
            // Check that there exists an activity that can receive this Intent
            if (isIntentSafe(intent)) {
                startActivity(intent);
            }
            // If not, then show an error message to the user
            else {
                Toast.makeText(MainActivity.this, R.string.error_message, Toast.LENGTH_SHORT).show();
            }
        } else if (buttonClicked == R.id.bImplicitIntentUserChooses) {
            // Create a chooser for the user to select the application to handle the Intent
            Intent chooser = Intent.createChooser(intent, getResources().getString(R.string.chooser_message));

            // Check that there exists an activity that can receive this Intent
            if (isIntentSafe(intent)) {
                startActivity(chooser);
            }
            // If not, then show an error message to the user
            else {
                Toast.makeText(MainActivity.this, R.string.error_message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Checks that there is an activity that can handle this intent
    private boolean isIntentSafe(Intent intent) {
        List<ResolveInfo> activities = getPackageManager().
                queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return (activities.size() > 0);
    }

}
