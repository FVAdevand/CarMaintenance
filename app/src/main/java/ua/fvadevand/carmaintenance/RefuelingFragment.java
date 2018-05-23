package ua.fvadevand.carmaintenance;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import ua.fvadevand.carmaintenance.firebase.Const;
import ua.fvadevand.carmaintenance.firebase.model.Refueling;
import ua.fvadevand.carmaintenance.utilities.DateUtils;
import ua.fvadevand.carmaintenance.utilities.TextFormatUtils;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnClickRefuelingItemListener} interface
 * to handle interaction events.
 * Use the {@link RefuelingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RefuelingFragment extends Fragment {

    private static final String LOG_TAG = RefuelingFragment.class.getSimpleName();

    //    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    private String mParam1;
//    private String mParam2;

    private OnClickRefuelingItemListener mListener;

    public RefuelingFragment() {
    }

    public static RefuelingFragment newInstance() {
        RefuelingFragment fragment = new RefuelingFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onCreateView: cretae fragment");
        return inflater.inflate(R.layout.fragment_refueling, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView refuelingListView = view.findViewById(R.id.refueling_list);
        refuelingListView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child(user.getUid())
                .child(Const.REFUELING_REF)
                .child(Const.CURRENT_CAR);

        FirebaseRecyclerOptions<Refueling> options =
                new FirebaseRecyclerOptions.Builder<Refueling>()
                        .setQuery(query, Refueling.class)
                        .setLifecycleOwner(this)
                        .build();

        FirebaseRecyclerAdapter<Refueling, RefuelingViewHolder> adapter =
                new FirebaseRecyclerAdapter<Refueling, RefuelingViewHolder>(options) {

                    @Override
                    protected void onBindViewHolder(@NonNull RefuelingViewHolder holder, int position, @NonNull Refueling model) {
                        Context context = view.getContext();
                        String date = DateUtils.formatDate(context, model.getTimeStamp());
                        holder.mDateView.setText(date);
                        holder.mCoastView.setText(TextFormatUtils.coastFormat(model.getCoast()));
                        holder.mOdometrView.setText(TextFormatUtils.odometrFormat(model.getOdometr()));
                        holder.mDistanceView.setText(TextFormatUtils.distanceFormat(model.getDistance()));
                        holder.mVolumeView.setText(TextFormatUtils.volumeFormat(model.getVolume()));
                        holder.mGasStationView.setText(model.getGasStation());
                        holder.mBrandFuelView.setText(model.getBrandFuel());
                        holder.mPriceUnitView.setText(TextFormatUtils.priceUnitFormat(model.getPriceUnit()));
                    }

                    @NonNull
                    @Override
                    public RefuelingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View v = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.refueling_list_item, parent, false);
                        return new RefuelingViewHolder(v);
                    }
                };

        refuelingListView.setHasFixedSize(true);
        refuelingListView.setAdapter(adapter);
    }

    //    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onClickRefuelingItem(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnClickRefuelingItemListener) {
            mListener = (OnClickRefuelingItemListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnClickRefuelingItemListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnClickRefuelingItemListener {
        void onClickRefuelingItem(int position);
    }

    public static class RefuelingViewHolder extends RecyclerView.ViewHolder {

        TextView mDateView;
        TextView mCoastView;
        TextView mOdometrView;
        TextView mDistanceView;
        TextView mVolumeView;
        TextView mGasStationView;
        TextView mBrandFuelView;
        TextView mPriceUnitView;

        public RefuelingViewHolder(View itemView) {
            super(itemView);

            mDateView = itemView.findViewById(R.id.tv_refueling_date);
            mCoastView = itemView.findViewById(R.id.tv_refueling_coast);
            mOdometrView = itemView.findViewById(R.id.tv_refueling_odometr);
            mDistanceView = itemView.findViewById(R.id.tv_refueling_distance);
            mVolumeView = itemView.findViewById(R.id.tv_refueling_volume);
            mGasStationView = itemView.findViewById(R.id.tv_refueling_gas_station);
            mBrandFuelView = itemView.findViewById(R.id.tv_refueling_brand_fuel);
            mPriceUnitView = itemView.findViewById(R.id.tv_refueling_price_unit);
        }
    }
}
