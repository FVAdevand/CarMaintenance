package ua.fvadevand.carmaintenance.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.signature.MediaStoreSignature;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ua.fvadevand.carmaintenance.R;
import ua.fvadevand.carmaintenance.adapters.VehicleArrayAdapter;
import ua.fvadevand.carmaintenance.dialogs.AlertDeleteDialogFragment;
import ua.fvadevand.carmaintenance.firebase.Firebase;
import ua.fvadevand.carmaintenance.firebase.FirebaseVehicle;
import ua.fvadevand.carmaintenance.firebase.model.Vehicle;
import ua.fvadevand.carmaintenance.interfaces.OnSetCurrentVehicleListener;
import ua.fvadevand.carmaintenance.managers.ShPrefManager;
import ua.fvadevand.carmaintenance.ui.editor.EditMaintenanceActivity;
import ua.fvadevand.carmaintenance.ui.editor.EditVehicleActivity;
import ua.fvadevand.carmaintenance.utilities.GlideApp;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        RefuelingFragment.OnClickRefuelingItemListener,
        VehicleFragment.OnVehicleItemListener,
        AlertDeleteDialogFragment.OnClickDeleteDialogListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 1458;
    private FloatingActionButton mFab;

    private String mDeletedVehicleId;
    private String mCurrentVehicleId;
    private ImageView mBackgroundNavBar;
    private List<Vehicle> mVehicleList;
    private VehicleArrayAdapter mSpinnerAdapter;
    private Spinner mSpinnerVehicleModel;

    private OnSetCurrentVehicleListener mListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mVehicleList = new ArrayList<>();

        mFab = findViewById(R.id.fab_edit_vehicle);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        mBackgroundNavBar = headerView.findViewById(R.id.iv_background_main_nav_bar);
        mSpinnerVehicleModel = headerView.findViewById(R.id.spinner_vehicle_model_nav_bar);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startSignInActivity();
        } else {
            fetchVehicleList();
            Log.i(LOG_TAG, "onCreate: ");
        }

        mSpinnerAdapter = new VehicleArrayAdapter(this, mVehicleList);
        mSpinnerVehicleModel.setAdapter(mSpinnerAdapter);
        mSpinnerVehicleModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    mListener.onSetCurrentVehicle(mVehicleList.get(position).getId());
                }
                onChangeCurrentVehicle(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        mCurrentVehicleId = ShPrefManager.getCurrentVehicleId(this);
        if (mCurrentVehicleId != null) {
            fetchCurrentVehicle();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_sign_out:
                signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startSignInActivity();
                }
            }
        });
    }

    private void startSignInActivity() {
        startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setLogo(R.drawable.car_key)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_vehicle:
                showVehicleFragment();
                break;
            case R.id.nav_refueling:
                showRefuelingFragment();
                break;
            case R.id.nav_slideshow:
                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN)
            Log.i(LOG_TAG, "onActivityResult: " + resultCode);
        if (resultCode == RESULT_OK) {
            fetchVehicleList();
            Log.i(LOG_TAG, "onActivityResult: RESULT_OK");
        } else {
            finish();
        }
    }

    @Override
    public void onClickRefuelingItem(int position) {

    }

    @Override
    public void onClickVehicleItem(String vehicleId) {
        Intent activityIntent = new Intent(this, EditVehicleActivity.class);
        activityIntent.putExtra(Firebase.KEY_VEHICLE_ID, vehicleId);
        startActivity(activityIntent);
    }

    @Override
    public void onLongClickVehicleItem(String vehicleId) {
        DialogFragment dialog = new AlertDeleteDialogFragment();
        dialog.show(getSupportFragmentManager(), "dialog_delete_vehicle");
        mDeletedVehicleId = vehicleId;
    }

    @Override
    public void onChangeCurrentVehicle(int position) {
        Vehicle currentVehicle = mVehicleList.get(position);
        mCurrentVehicleId = currentVehicle.getId();
        ShPrefManager.setCurrentVehicleId(this, mCurrentVehicleId);
        mSpinnerVehicleModel.setSelection(position);
        displayVehicle(currentVehicle);
    }

    @Override
    public void onClickPositiveDeleteDialog() {
        FirebaseVehicle.deleteVehicle(mDeletedVehicleId);
    }

    private void showRefuelingFragment() {
        Fragment refuelingFragment = RefuelingFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, refuelingFragment)
                .commit();
        setListener(refuelingFragment);
        mFab.setImageResource(R.drawable.ic_action_add);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EditMaintenanceActivity.class));
            }
        });
    }

    private void showVehicleFragment() {
        Fragment vehicleFragment = VehicleFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, vehicleFragment)
                .commit();
        setListener(vehicleFragment);
        mFab.setImageResource(R.drawable.ic_action_add);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EditVehicleActivity.class));
            }
        });
    }

    private void displayVehicle(Vehicle vehicle) {
        long photoTimestamp = vehicle.getPhotoTimestamp();
        if (photoTimestamp > 0) {
            GlideApp.with(this)
                    .load(FirebaseVehicle.getCurrentPhotoVehicleRef(vehicle))
                    .signature(new MediaStoreSignature("image/jpeg", photoTimestamp, 0))
                    .centerCrop()
                    .into(mBackgroundNavBar);
        } else {
            mBackgroundNavBar.setImageResource(R.drawable.default_background_nav_bar);
        }
    }

    private void fetchCurrentVehicle() {
        FirebaseVehicle.getCurrentVehicleRef(mCurrentVehicleId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Vehicle currentVehicle = dataSnapshot.getValue(Vehicle.class);
                        if (currentVehicle != null) {
                            displayVehicle(currentVehicle);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void fetchVehicleList() {
        FirebaseVehicle.getVehicleRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot vehicleSnapshot : dataSnapshot.getChildren()) {
                    Vehicle vehicle = vehicleSnapshot.getValue(Vehicle.class);
                    mVehicleList.add(vehicle);
                }
                mSpinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setListener(Fragment fragment) {
        if (fragment instanceof OnSetCurrentVehicleListener) {
            mListener = (OnSetCurrentVehicleListener) fragment;
        } else {
            throw new RuntimeException(fragment.toString()
                    + " must implement OnSetCurrentVehicleListener");
        }
    }

}
