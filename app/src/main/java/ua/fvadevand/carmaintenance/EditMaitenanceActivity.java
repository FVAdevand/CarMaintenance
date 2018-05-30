package ua.fvadevand.carmaintenance;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;

import ua.fvadevand.carmaintenance.firebase.FirebaseRefueling;
import ua.fvadevand.carmaintenance.firebase.model.Refueling;
import ua.fvadevand.carmaintenance.managers.ShPrefManager;

public class EditMaitenanceActivity extends AppCompatActivity {

    private static final String LOG_TAG = EditMaitenanceActivity.class.getSimpleName();

    private DatabaseReference mRefuelingRef;
    private FirebaseUser mUser;
    private EditText mVolumeView;
    private EditText mPriceUnitView;
    private EditText mOdometrView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_maitenance);

        String currentVehicleId = ShPrefManager.getCurrentVehicleId(this);
        mRefuelingRef = FirebaseRefueling.getCurVehRefuelingRef(currentVehicleId);

        mVolumeView = findViewById(R.id.et_edit_volume);
        mPriceUnitView = findViewById(R.id.et_edit_price_unit);
        mOdometrView = findViewById(R.id.et_edit_odometr);

        Button saveRefuelingBtn = findViewById(R.id.btn_save_refueling);
        saveRefuelingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String volumeStr = mVolumeView.getText().toString();
                String priceUnitStr = mPriceUnitView.getText().toString();
                String odometrStr = mOdometrView.getText().toString();
                double volume = Double.parseDouble(volumeStr);
                double priceUnit = Double.parseDouble(priceUnitStr);
                int odometr = Integer.parseInt(odometrStr);

                Calendar calendar = Calendar.getInstance();
                long timeInMillis = calendar.getTimeInMillis();
                Refueling refueling = new Refueling(timeInMillis,
                        volume,
                        priceUnit,
                        volume * priceUnit,
                        4.5,
                        odometr,
                        345,
                        "OKKO",
                        "95 pulse");

                mRefuelingRef.push().setValue(refueling).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditMaitenanceActivity.this, "refueling saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditMaitenanceActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
