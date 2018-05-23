package ua.fvadevand.carmaintenance.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;


public class AlertDeleteDialogFragment extends DialogFragment {

    private static final String LOG_TAG = AlertDeleteDialogFragment.class.getSimpleName();

    private OnClickDeleteDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete vehicle?")
                .setMessage("It will be deleted all data related with vehicle")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onClickPositiveDeleteDialog();
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            try {
                mListener = (OnClickDeleteDialogListener) parentFragment;
            } catch (ClassCastException e) {
                throw new ClassCastException(parentFragment.toString()
                        + " OnClickDeleteDialogListener");
            }

        } else {
            try {
                mListener = (OnClickDeleteDialogListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString()
                        + " OnClickDeleteDialogListener");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnClickDeleteDialogListener {
        void onClickPositiveDeleteDialog();
    }
}
