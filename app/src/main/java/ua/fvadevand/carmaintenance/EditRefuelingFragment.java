package ua.fvadevand.carmaintenance;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import ua.fvadevand.carmaintenance.dialogs.DatePickerDialogFragment;
import ua.fvadevand.carmaintenance.dialogs.SetInfoDialogFragment;
import ua.fvadevand.carmaintenance.firebase.FirebaseRefueling;
import ua.fvadevand.carmaintenance.firebase.model.Refueling;
import ua.fvadevand.carmaintenance.utilities.CalculationUtils;
import ua.fvadevand.carmaintenance.utilities.DateUtils;
import ua.fvadevand.carmaintenance.utilities.TextFormatUtils;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditRefuelingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditRefuelingFragment extends Fragment
        implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener,
        SetInfoDialogFragment.OnClickPositiveInfoDialogListener {

    private static final String LOG_TAG = EditRefuelingFragment.class.getSimpleName();
    private static final String KEY_CURRENT_VEHICLE_ID = "CURRENT_VEHICLE_ID";

    private static final int MAIN_PRICE = 0;
    private static final int MAIN_VOLUME = 1;
    private static final int MAIN_PRICE_UNIT = 2;


    private String mCurrentVehicleId;
    private Refueling mLastRefueling;
    private Refueling mCurrentRefueling;
    private Calendar mCalendar;
    private int mCalculationModePrice;

    private TextView mDateView;
    private EditText mOdometerView;
    private EditText mDistanceView;
    private Spinner mFuelBrandSpinner;
    private Spinner mGasSationSpinner;
    private EditText mPriceView;
    private EditText mVolumeView;
    private EditText mPriceUnitView;
    private EditText mFuelBalanceView;

    public EditRefuelingFragment() {
    }

    public static EditRefuelingFragment newInstance(String currentVehicleId) {
        EditRefuelingFragment fragment = new EditRefuelingFragment();
        Bundle args = new Bundle();
        args.putString(KEY_CURRENT_VEHICLE_ID, currentVehicleId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentVehicleId = getArguments().getString(KEY_CURRENT_VEHICLE_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_refueling, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        addTextChangeListener();
        fetchLastRefueling();

        FragmentActivity activity = getActivity();
        if (activity != null) {
            FloatingActionButton fab = activity.findViewById(R.id.fab_edit_maintenance);
            fab.setImageResource(R.drawable.ic_action_save);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateRefueling();
                }
            });
        }
    }

    private void initView(View view) {
        mDateView = view.findViewById(R.id.tv_edit_refueling_date);
        mDateView.setOnClickListener(this);
        mCalendar = Calendar.getInstance();
        mDateView.setText(DateUtils.formatDate(view.getContext(), mCalendar.getTimeInMillis()));

        mOdometerView = view.findViewById(R.id.et_edit_refueling_odometer);
        mDistanceView = view.findViewById(R.id.et_edit_refueling_distance);
        mFuelBrandSpinner = view.findViewById(R.id.sp_edit_refueling_fuel_brand);
        mGasSationSpinner = view.findViewById(R.id.sp_edit_refueling_gas_station);
        //TODO: set spinners
        mPriceView = view.findViewById(R.id.et_edit_refueling_price);
        mVolumeView = view.findViewById(R.id.et_edit_refueling_volume);
        mPriceUnitView = view.findViewById(R.id.et_edit_refueling_price_unit);
        mFuelBalanceView = view.findViewById(R.id.et_edit_refueling_fuel_balance);
        mFuelBalanceView.setText("0");

        ImageButton addFuelBrandBtn = view.findViewById(R.id.ibtn_add_fuel_brand);
        addFuelBrandBtn.setOnClickListener(this);

        ImageButton addGasStationBtn = view.findViewById(R.id.ibtn_add_gas_station);
        addGasStationBtn.setOnClickListener(this);
    }

    private void fetchLastRefueling() {
        Query curVehRefuelingRef = FirebaseRefueling.getCurVehRefuelingRef(mCurrentVehicleId).limitToLast(1);
        curVehRefuelingRef
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot refuelingSnapshot : dataSnapshot.getChildren()) {
                            mLastRefueling = refuelingSnapshot.getValue(Refueling.class);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_add_fuel_brand:
                DialogFragment dialog = new SetInfoDialogFragment();
                dialog.show(getChildFragmentManager(), "dialog_add_fuel_brand");
                break;
            case R.id.ibtn_add_gas_station:
                break;
            case R.id.tv_edit_refueling_date:
                showDatePickerDialog();
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mCalendar.set(year, month, dayOfMonth);
        if (getContext() != null) {
            mDateView.setText(DateUtils.formatDate(getContext(), mCalendar.getTimeInMillis()));
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialogFragment fragment = new DatePickerDialogFragment();
        fragment.show(getChildFragmentManager(), "DatePicker");
    }

    private void addTextChangeListener() {
        mOdometerView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mOdometerView.hasFocus()
                        || s.toString().isEmpty()
                        || mLastRefueling == null) return;

                int lastOdometer = mLastRefueling.getOdometer();
                int currentOdometer = Integer.parseInt(s.toString());
                int distance = currentOdometer - lastOdometer;
                if (distance >= 0) {
                    mDistanceView.setText(String.valueOf(distance));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDistanceView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mDistanceView.hasFocus()
                        || s.toString().isEmpty()
                        || mLastRefueling == null) return;

                int lastOdometer = mLastRefueling.getOdometer();
                int distance = Integer.parseInt(s.toString());
                mOdometerView.setText(String.valueOf(lastOdometer + distance));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPriceView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mPriceView.hasFocus() || s.toString().isEmpty()) return;

                double price = Double.parseDouble(s.toString());
                String priceUnitStr = mPriceUnitView.getText().toString();
                String volumeStr = mVolumeView.getText().toString();

                if (volumeStr.isEmpty()) mCalculationModePrice = MAIN_PRICE_UNIT;
                if (priceUnitStr.isEmpty()) mCalculationModePrice = MAIN_VOLUME;

                if (!volumeStr.isEmpty() && mCalculationModePrice == MAIN_VOLUME) {
                    double volume = Double.parseDouble(volumeStr);
                    mPriceUnitView.setText(TextFormatUtils.decimalFormatWithDot(price / volume));
                } else if (!priceUnitStr.isEmpty() && mCalculationModePrice == MAIN_PRICE_UNIT) {
                    double priceUnit = Double.parseDouble(priceUnitStr);
                    mVolumeView.setText(TextFormatUtils.decimalFormatWithDot(price / priceUnit));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mVolumeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mVolumeView.hasFocus() || s.toString().isEmpty()) return;

                double volume = Double.parseDouble(s.toString());
                String priceUnitStr = mPriceUnitView.getText().toString();
                String priceStr = mPriceView.getText().toString();

                if (priceStr.isEmpty()) mCalculationModePrice = MAIN_PRICE_UNIT;
                if (priceUnitStr.isEmpty()) mCalculationModePrice = MAIN_PRICE;

                if (!priceStr.isEmpty() && mCalculationModePrice == MAIN_PRICE) {
                    double price = Double.parseDouble(priceStr);
                    mPriceUnitView.setText(TextFormatUtils.decimalFormatWithDot(price / volume));
                } else if (!priceUnitStr.isEmpty() && mCalculationModePrice == MAIN_PRICE_UNIT) {
                    double priceUnit = Double.parseDouble(priceUnitStr);
                    mPriceView.setText(TextFormatUtils.decimalFormatWithDot(volume * priceUnit));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPriceUnitView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mPriceUnitView.hasFocus() || s.toString().isEmpty()) return;

                double priceUnit = Double.parseDouble(s.toString());
                String priceStr = mPriceView.getText().toString();
                String volumeStr = mVolumeView.getText().toString();

                if (priceStr.isEmpty()) mCalculationModePrice = MAIN_VOLUME;
                if (volumeStr.isEmpty()) mCalculationModePrice = MAIN_PRICE;

                if (!priceStr.isEmpty() && mCalculationModePrice == MAIN_PRICE) {
                    double price = Double.parseDouble(priceStr);
                    mVolumeView.setText(TextFormatUtils.decimalFormatWithDot(price / priceUnit));
                } else if (!volumeStr.isEmpty() && mCalculationModePrice == MAIN_VOLUME) {
                    double volume = Double.parseDouble(volumeStr);
                    mPriceView.setText(TextFormatUtils.decimalFormatWithDot(volume * priceUnit));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void updateRefueling() {
        String odometerStr = mOdometerView.getText().toString();
        if (TextUtils.isEmpty(odometerStr)) {
            setError(mOdometerView, getString(R.string.error_edit_refueling_write_in));
            return;
        }

        String priceStr = mPriceView.getText().toString();
        if (TextUtils.isEmpty(priceStr)) {
            setError(mPriceView, getString(R.string.error_edit_refueling_write_in));
            return;
        }

        String volumeStr = mVolumeView.getText().toString();
        if (TextUtils.isEmpty(volumeStr)) {
            setError(mVolumeView, getString(R.string.error_edit_refueling_write_in));
            return;
        }

        String priceUnitStr = mPriceUnitView.getText().toString();
        if (TextUtils.isEmpty(priceUnitStr)) {
            setError(mPriceUnitView, getString(R.string.error_edit_refueling_write_in));
            return;
        }

        String fuelBalanceStr = mFuelBalanceView.getText().toString();
        if (TextUtils.isEmpty(fuelBalanceStr)) {
            setError(mFuelBalanceView, getString(R.string.error_edit_refueling_write_in));
            return;
        }

        //TODO: get data from spinners

        if (mLastRefueling != null && mCalendar.getTimeInMillis() < mLastRefueling.getTimeStamp()) {
            mDateView.setError("Incorrect date");
            return;
        }

        int odometer = Integer.parseInt(odometerStr);
        if (mLastRefueling != null && odometer < mLastRefueling.getOdometer()) {
            setError(mOdometerView, getString(R.string.error_edit_refueling_incorrect_value));
            return;
        }

        double price = Double.parseDouble(priceStr);
        double volume = Double.parseDouble(volumeStr);
        double priceUnit = Double.parseDouble(priceUnitStr);
        if (price - volume * priceUnit > 0.5) {
            setError(mPriceView, getString(R.string.error_edit_refueling_incorrect_value));
            return;
        }

        if (mCurrentRefueling == null) {
            mCurrentRefueling = new Refueling();
        }

        mCurrentRefueling.setBrandFuel("АИ 95");
        mCurrentRefueling.setGasStation("OKKO");
        mCurrentRefueling.setTimeStamp(mCalendar.getTimeInMillis());
        mCurrentRefueling.setOdometer(odometer);
        mCurrentRefueling.setCoast(price);
        mCurrentRefueling.setVolume(volume);
        mCurrentRefueling.setPriceUnit(priceUnit);
        mCurrentRefueling.setFuelBalance(Double.parseDouble(fuelBalanceStr));

        if (mLastRefueling != null) {
            double fuelRate = CalculationUtils.calculationFuelRate(mLastRefueling, mCurrentRefueling);
            mLastRefueling.setFuelRate(fuelRate);
            FirebaseRefueling.setRefueling(mLastRefueling, mCurrentVehicleId);
        }

        FirebaseRefueling.setRefueling(mCurrentRefueling, mCurrentVehicleId);
    }

    private void setError(EditText view, String errorMsg) {
        view.setError(errorMsg);
        view.requestFocus();
    }

    @Override
    public void onClickPositiveInfoDialog(String info) {

    }
}
