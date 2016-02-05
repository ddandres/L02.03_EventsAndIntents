/*
 * Copyright (c) 2016. David de Andrés and Juan Carlos Ruiz, DISCA - UPV, Development of apps for mobile devices.
 */

package labs.sdm.l0203_eventsandintents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ForResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_result);
    }

    // This callback is triggered when the user presses the back button
    @Override
    public void onBackPressed() {
        // Create a new Intent and include the result to return
        Intent intent = new Intent();
        intent.putExtra("result", "Incoming result!");
        // Set this Intent as the result to be returned by the activity
        setResult(RESULT_OK, intent);
        // Continue with the normal onBackPressed() procedure, so the activity will be destroyed
        super.onBackPressed();
    }
}
