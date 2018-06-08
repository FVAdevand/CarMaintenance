package ua.fvadevand.carmaintenance.ui.editor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ua.fvadevand.carmaintenance.R;
import ua.fvadevand.carmaintenance.managers.ShPrefManager;

public class EditMaintenanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_maintenance);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditRefuelingFragment fragment = EditRefuelingFragment.newInstance(ShPrefManager.getCurrentVehicleId(this));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_edit_maintenance_fragment, fragment)
                .commit();
    }

}
