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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Request code used when waiting for an activity to return a result
    private final static int GET_MESSAGE = 1;

    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // onClickListener association by code
        findViewById(R.id.bShowNotification).setOnClickListener(v -> {
            // Displays a quick little message in a popup
            Toast.makeText(MainActivity.this, R.string.notification_message, Toast.LENGTH_SHORT).show();
        });

        // All Buttons for explicit Intents share the same OnClickListener
        final View.OnClickListener explicitIntentListener = v -> launchNewActivity(v.getId());

        findViewById(R.id.bExplicitIntent).setOnClickListener(explicitIntentListener);
        findViewById(R.id.bExplicitIntentWithParameter).setOnClickListener(explicitIntentListener);
        findViewById(R.id.bExplicitIntentForResultDeprecated)
                .setOnClickListener(explicitIntentListener);
        findViewById(R.id.bExplicitIntentForResult).setOnClickListener(explicitIntentListener);

        // All Buttons for implicit Intents share the same OnClickListener
        final View.OnClickListener implicitIntentListener = v -> navigateToEtsinfLocation(v.getId());

        findViewById(R.id.bImplicitIntentSystemChooses).setOnClickListener(implicitIntentListener);
        findViewById(R.id.bImplicitIntentUserChooses).setOnClickListener(implicitIntentListener);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // We can also check the result code provided by the returning activity
                    if (result.getResultCode() == RESULT_OK) {
                        final Intent intent = result.getData();
                        Toast.makeText(
                                MainActivity.this,
                                intent != null ?
                                        result.getData().getStringExtra("result") :
                                        getResources().getString(R.string.no_message),
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    // This method manages explicit Intents
    private void launchNewActivity(int buttonClicked) {

        final Intent intent;

        // Determine what to do depending on the Button clicked
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
        } else if (buttonClicked == R.id.bExplicitIntentForResultDeprecated) {
            // Launch the new activity and wait for a result
            // Explicit Intent to launch the ForResultActivity
            // Deprecated from androidx.activity 1.2.0+
            intent = new Intent(MainActivity.this, ForResultActivity.class);
            startActivityForResult(intent, GET_MESSAGE);
        } else if (buttonClicked == R.id.bExplicitIntentForResult) {
            // Launch the new activity and wait for a result
            // Explicit Intent to launch the ForResultActivity
            // Using androix.activity 1.2.0+
            intent = new Intent(MainActivity.this, ForResultActivity.class);
            launcher.launch(intent);
        }
    }

    // This callback will be triggered when a previously launched activity returns a result
    // Deprecated from androidx.activity 1.2.0+
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // All the activities launched for result will trigger this method,
        // so we need to check what we asked the activities to do, to know
        // which result we are getting
        if (requestCode == GET_MESSAGE) {
            // We can also check the result code provided by the returning activity
            if (resultCode == RESULT_OK) {
                Toast.makeText(
                        MainActivity.this,
                        data.getStringExtra("result"),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    // This method manages implicit Intents
    private void navigateToEtsinfLocation(int buttonClicked) {

        // Intent to navigate to ETSINF's location
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        // geo:latitude,longitude?z=zoom
        intent.setData(Uri.parse("geo:39.4819305,-0.3469791?z=18"));

        // Determine what to do depending on the Button clicked
        if (buttonClicked == R.id.bImplicitIntentSystemChooses) {
            // Let the system choose the application to launch,
            // if more than one a chooser will be displayed.
            // Check that there exists an activity that can receive this Intent
            if (isIntentSafe(intent)) {
                startActivity(intent);
            }
            // If not, then show an error message to the user
            else {
                Toast.makeText(
                        MainActivity.this,
                        R.string.error_message,
                        Toast.LENGTH_SHORT).show();
            }
        } else if (buttonClicked == R.id.bImplicitIntentUserChooses) {
            // Create a chooser for the user to select the application to handle the Intent
            final Intent chooser = Intent.createChooser(
                    intent, getResources().getString(R.string.chooser_message));

            // Check that there exists an activity that can receive this Intent
            if (isIntentSafe(intent)) {
                startActivity(chooser);
            }
            // If not, then show an error message to the user
            else {
                Toast.makeText(
                        MainActivity.this,
                        R.string.error_message,
                        Toast.LENGTH_SHORT).show();
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
