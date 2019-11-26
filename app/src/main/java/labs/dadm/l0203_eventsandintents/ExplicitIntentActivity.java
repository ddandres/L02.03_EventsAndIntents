/*
 * Copyright (c) 2016. David de Andrés and Juan Carlos Ruiz, DISCA - UPV, Development of apps for mobile devices.
 */

package labs.dadm.l0203_eventsandintents;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class ExplicitIntentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explicit_intent);

        // Get the value (String) of the parameter "message" passed within the Intent
        final String message = getIntent().getStringExtra("message");
        // If null then there was no such parameter
        if (message != null) {
            // Otherwise display this message on the TextView
            TextView tv = findViewById(R.id.tvExplicitIntent);
            tv.setText(message);
        }
    }
}
