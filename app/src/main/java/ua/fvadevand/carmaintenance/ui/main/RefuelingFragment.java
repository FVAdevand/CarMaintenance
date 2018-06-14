package ua.fvadevand.carmaintenance.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

import ua.fvadevand.carmaintenance.R;
import ua.fvadevand.carmaintenance.firebase.FirebaseRefueling;
import ua.fvadevand.carmaintenance.firebase.model.Refueling;
import ua.fvadevand.carmaintenance.interfaces.OnSetCurrentVehicleListener;
import ua.fvadevand.carmaintenance.managers.ShPrefManager;
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
public class RefuelingFragment extends Fragment
        implements OnSetCurrentVehicleListener {

    private static final String LOG_TAG = RefuelingFragment.class.getSimpleName();

    private OnClickRefuelingItemListener mListener;
    private String mCurrentVehicleId;
    private RecyclerView mRefuelingListView;

    public RefuelingFragment() {
    }

    public static RefuelingFragment newInstance() {
        return new RefuelingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refueling, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCurrentVehicleId = ShPrefManager.getCurrentVehicleId(view.getContext());

        mRefuelingListView = view.findViewById(R.id.refueling_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRefuelingListView.setLayoutManager(linearLayoutManager);

        mRefuelingListView.setHasFixedSize(true);
        mRefuelingListView.setAdapter(getAdapter());
    }

    private FirebaseRecyclerAdapter<Refueling, RefuelingViewHolder> getAdapter() {
        Query query = FirebaseRefueling.getCurVehRefuelingRef(mCurrentVehicleId);

        FirebaseRecyclerOptions<Refueling> options =
                new FirebaseRecyclerOptions.Builder<Refueling>()
                        .setQuery(query, Refueling.class)
                        .setLifecycleOwner(this)
                        .build();

        return new FirebaseRecyclerAdapter<Refueling, RefuelingViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull RefuelingViewHolder holder, int position, @NonNull Refueling model) {
                String date = DateUtils.formatDate(RefuelingFragment.this.getContext(), model.getTimestamp());
                holder.mDateView.setText(date);
                holder.mCoastView.setText(TextFormatUtils.costFormat(model.getCost()));
                holder.mOdometerView.setText(TextFormatUtils.odometerFormat(model.getOdometer()));
                holder.mFuelRateView.setText(TextFormatUtils.fuelRateFormat(model.getFuelRate()));
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
    }

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

    @Override
    public void onSetCurrentVehicle(String currentVehicleId) {
        mCurrentVehicleId = currentVehicleId;
        mRefuelingListView.setAdapter(getAdapter());
    }

    public interface OnClickRefuelingItemListener {
        void onClickRefuelingItem(int position);
    }

    public static class RefuelingViewHolder extends RecyclerView.ViewHolder {

        TextView mDateView;
        TextView mCoastView;
        TextView mOdometerView;
        TextView mFuelRateView;
        TextView mVolumeView;
        TextView mGasStationView;
        TextView mBrandFuelView;
        TextView mPriceUnitView;

        RefuelingViewHolder(View itemView) {
            super(itemView);

            mDateView = itemView.findViewById(R.id.tv_refueling_date);
            mCoastView = itemView.findViewById(R.id.tv_refueling_coast);
            mOdometerView = itemView.findViewById(R.id.tv_refueling_odometr);
            mFuelRateView = itemView.findViewById(R.id.tv_refueling_fuel_rate);
            mVolumeView = itemView.findViewById(R.id.tv_refueling_volume);
            mGasStationView = itemView.findViewById(R.id.tv_refueling_gas_station);
            mBrandFuelView = itemView.findViewById(R.id.tv_refueling_brand_fuel);
            mPriceUnitView = itemView.findViewById(R.id.tv_refueling_price_unit);
        }
    }
}
