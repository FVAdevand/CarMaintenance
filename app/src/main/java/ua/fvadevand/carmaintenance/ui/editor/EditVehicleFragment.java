package ua.fvadevand.carmaintenance.ui.editor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.signature.MediaStoreSignature;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ua.fvadevand.carmaintenance.R;
import ua.fvadevand.carmaintenance.dialogs.AlertDeleteDialogFragment;
import ua.fvadevand.carmaintenance.firebase.FirebaseVehicle;
import ua.fvadevand.carmaintenance.firebase.model.Vehicle;
import ua.fvadevand.carmaintenance.utilities.GlideApp;


public class EditVehicleFragment extends Fragment
        implements AlertDeleteDialogFragment.OnClickDeleteDialogListener {

    private static final String LOG_TAG = EditVehicleFragment.class.getSimpleName();

    private static final String ARG_VEHICLE_ID = "vehicleId";
    private String mCurrentVehicleId;

    private EditText mManufacturerView;
    private EditText mModelView;
    private EditText mInitialOdometerView;
    private EditText mYearManufacturerView;
    private List<EditText> mEditTextList;
    private ImageView mToolbarImageView;

    private Vehicle mCurrentVehicle;

    private boolean isEditMode;

    private Uri mPhotoVehicleUri;
    private FloatingActionButton mFab;

    private OnEditVehicleListener mListener;


    public EditVehicleFragment() {
    }

    public static EditVehicleFragment newInstance(String currentVehicleId) {
        EditVehicleFragment fragment = new EditVehicleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VEHICLE_ID, currentVehicleId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentVehicleId = getArguments().getString(ARG_VEHICLE_ID);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_vehicle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mManufacturerView = view.findViewById(R.id.et_vehicle_manufacturer);
        mModelView = view.findViewById(R.id.et_edit_refueling_odometer);
        mInitialOdometerView = view.findViewById(R.id.et_vehicle_initial_odometr);
        mYearManufacturerView = view.findViewById(R.id.et_vehicle_year_manufacture);

        mEditTextList = new ArrayList<>();
        mEditTextList.add(mManufacturerView);
        mEditTextList.add(mModelView);
        mEditTextList.add(mInitialOdometerView);
        mEditTextList.add(mYearManufacturerView);

        FragmentActivity activity = getActivity();
        if (activity != null) {
            mFab = getActivity().findViewById(R.id.fab_edit_vehicle);
            mToolbarImageView = getActivity().findViewById(R.id.iv_edit_vehicle_photo);

            if (mCurrentVehicleId == null) {
                setEditMode(true);
                mListener.onSetTitle("New vehicle");
            } else {
                setEditMode(false);
                FirebaseVehicle.getCurrentVehicleRef(mCurrentVehicleId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                mCurrentVehicle = dataSnapshot.getValue(Vehicle.class);
                                if (mCurrentVehicle != null) {
                                    displayVehicle(mCurrentVehicle);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }

            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEditMode) {
                        updateVehicle();
                    } else {
                        setEditMode(true);
                    }
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit_vehicle, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_vehicle:
                deleteVehicle();
                return true;
            case R.id.action_add_photo:
                dispatchCropImageOrPhoto();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem addPhoto = menu.findItem(R.id.action_add_photo);
        MenuItem deleteVehicle = menu.findItem(R.id.action_delete_vehicle);
        if (isEditMode) {
            deleteVehicle.setVisible(false);
            addPhoto.setVisible(true);
        } else {
            deleteVehicle.setVisible(true);
            addPhoto.setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                mPhotoVehicleUri = result.getUri();
                setPic(mToolbarImageView, mPhotoVehicleUri.getEncodedPath());
                break;
        }
    }

    private void setFocusableView(boolean focusable) {
        for (EditText editText : mEditTextList) {
            editText.setEnabled(focusable);
            if (!focusable) {
                editText.setTextColor(Color.BLACK);
                editText.clearFocus();
            }
        }
    }

    private void updateVehicle() {
        String modelVehicle = mModelView.getText().toString().trim();
        if (TextUtils.isEmpty(modelVehicle)) {
            mModelView.setError("Model cannot be empty");
            mModelView.requestFocus();
            return;
        }

        if (mCurrentVehicle == null) {
            mCurrentVehicle = new Vehicle();
        }
        mCurrentVehicle.setManufacturer(mManufacturerView.getText().toString().trim());
        mCurrentVehicle.setModel(modelVehicle);

        String initialOdometerStr = mInitialOdometerView.getText().toString();
        if (!TextUtils.isEmpty(initialOdometerStr)) {
            mCurrentVehicle.setInitialOdometr(Integer.parseInt(initialOdometerStr));
        }

        mCurrentVehicle.setYearManufacture(mYearManufacturerView.getText().toString());

        FirebaseVehicle.setVehicle(mCurrentVehicle, mPhotoVehicleUri);
        setEditMode(false);
        mListener.onSetTitle(mCurrentVehicle.getModel());
    }

    private void displayVehicle(Vehicle vehicle) {
        mManufacturerView.setText(vehicle.getManufacturer());
        mModelView.setText(vehicle.getModel());
        mYearManufacturerView.setText(vehicle.getYearManufacture());
        mInitialOdometerView.setText(String.format(Locale.getDefault(), "%d", vehicle.getInitialOdometr()));
        long photoTimestamp = vehicle.getPhotoTimestamp();
        if (photoTimestamp > 0) {
            GlideApp.with(this)
                    .load(FirebaseVehicle.getCurrentPhotoVehicleRef(vehicle))
                    .signature(new MediaStoreSignature("image/jpeg", photoTimestamp, 0))
                    .centerCrop()
                    .into(mToolbarImageView);
        }
        mListener.onSetTitle(vehicle.getModel());
    }

    private void clearView() {
        for (EditText editText : mEditTextList) {
            editText.getText().clear();
            editText.setFocusable(false);
        }
        mToolbarImageView.setImageResource(R.drawable.car_default_image);
    }

    private void dispatchCropImageOrPhoto() {
        Context context = getContext();
        if (context != null) {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(18, 10)
                    .start(getContext(), this);
        }
    }

    private void setPic(ImageView view, String imagePath) {

        int targetW = view.getWidth();
        int targetH = view.getHeight();

        Log.i(LOG_TAG, "setPic: W=" + targetW + " H=" + targetH);

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        view.setImageBitmap(bitmap);
    }

    private void deleteVehicle() {
        DialogFragment dialog = new AlertDeleteDialogFragment();
        dialog.show(getChildFragmentManager(), "dialog_delete_vehicle");
    }

    @Override
    public void onClickPositiveDeleteDialog() {
        FirebaseVehicle.deleteVehicle(mCurrentVehicleId);
        clearView();
        mCurrentVehicle = null;
    }

    private void setEditMode(boolean editMode) {
        isEditMode = editMode;
        if (editMode) {
            mFab.setImageResource(R.drawable.ic_action_save);
            setFocusableView(true);
        } else {
            mFab.setImageResource(R.drawable.ic_action_edit);
            setFocusableView(false);
        }
        if (getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEditVehicleListener) {
            mListener = (OnEditVehicleListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEditVehicleListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnEditVehicleListener {
        void onSetTitle(String title);
    }
}
