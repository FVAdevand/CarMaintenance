package ua.fvadevand.carmaintenance.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import ua.fvadevand.carmaintenance.R;

public class SetInfoDialogFragment extends DialogFragment {

    private OnClickPositiveInfoDialogListener mListener;
    private static final String ARG_TITLE = "title";

    private String mTitle;

    public static SetInfoDialogFragment newInstance(String title) {
        SetInfoDialogFragment fragment = new SetInfoDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_fragment_edit_additional_info, null);
        final EditText additionalInfoView = view.findViewById(R.id.et_dialog_additional_info);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(mTitle)
                .setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save",null);
//                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mListener.onClickPositiveInfoDialog(additionalInfoView.getText().toString().trim());
//                    }
//                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String info = additionalInfoView.getText().toString().trim();
                        if (TextUtils.isEmpty(info)) {
                            additionalInfoView.setError("Empty field");
                            additionalInfoView.requestFocus();
                        } else {
                            mListener.onClickPositiveInfoDialog(info);
                            dismiss();
                        }
                    }
                });
            }
        });

        return alertDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            try {
                mListener = (OnClickPositiveInfoDialogListener) parentFragment;
            } catch (ClassCastException e) {
                throw new ClassCastException(parentFragment.toString()
                        + " OnClickPositiveInfoDialogListener");
            }

        } else {
            try {
                mListener = (OnClickPositiveInfoDialogListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString()
                        + " OnClickPositiveInfoDialogListener");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnClickPositiveInfoDialogListener {
        void onClickPositiveInfoDialog(String info);
    }
}
