package ua.fvadevand.carmaintenance.dialogs;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import java.util.Calendar;

public class DatePickerDialogFragment extends DialogFragment {

    private OnDateSetListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), mListener, year, month, day);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            try {
                mListener = (OnDateSetListener) parentFragment;
            } catch (ClassCastException e) {
                throw new ClassCastException(parentFragment.toString()
                        + " OnDateSetListener");
            }

        } else {
            try {
                mListener = (OnDateSetListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString()
                        + " OnDateSetListener");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
