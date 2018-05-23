package ua.fvadevand.carmaintenance;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ua.fvadevand.carmaintenance.firebase.Const;

public class EditVehicleActivity extends AppCompatActivity
        implements EditVehicleFragment.OnEditVehicleListener {

    //    private static final String LOG_TAG = EditVehicleActivity.class.getSimpleName();
    private CollapsingToolbarLayout mToolbarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mToolbarLayout = findViewById(R.id.toolbar_layout);

        String vehicleId = null;

        if (getIntent().getExtras() != null) {
            vehicleId = getIntent().getStringExtra(Const.KEY_VEHICLE_ID);
        }

        EditVehicleFragment fragment = EditVehicleFragment.newInstance(vehicleId);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container_edit_vehicle_fragment, fragment)
                .commit();
    }

    @Override
    public void onSetTitle(String title) {
        mToolbarLayout.setTitle(title);
    }
}
