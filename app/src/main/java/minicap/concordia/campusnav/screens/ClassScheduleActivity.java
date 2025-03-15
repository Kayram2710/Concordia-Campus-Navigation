package minicap.concordia.campusnav.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import minicap.concordia.campusnav.R;


public class ClassScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        ImageButton menuButton = findViewById(R.id.button_menu);
        menuButton.setOnClickListener(v -> finish());

        // Import button to handle Google Calendar import
        Button importButton = findViewById(R.id.button_import_calendar);
        importButton.setOnClickListener(v -> {
            // TODO:Google Calendar or handle import logic
        });
    }
}
