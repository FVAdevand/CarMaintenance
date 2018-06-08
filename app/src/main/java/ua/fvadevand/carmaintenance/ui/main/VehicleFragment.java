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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.signature.MediaStoreSignature;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

import ua.fvadevand.carmaintenance.R;
import ua.fvadevand.carmaintenance.firebase.FirebaseVehicle;
import ua.fvadevand.carmaintenance.firebase.model.Vehicle;
import ua.fvadevand.carmaintenance.interfaces.OnSetCurrentVehicleListener;
import ua.fvadevand.carmaintenance.managers.ShPrefManager;
import ua.fvadevand.carmaintenance.utilities.GlideApp;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnVehicleItemListener} interface
 * to handle interaction events.
 * Use the {@link VehicleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VehicleFragment extends Fragment
        implements OnSetCurrentVehicleListener {

//    private static final String LOG_TAG = VehicleFragment.class.getSimpleName();

    private OnVehicleItemListener mListener;
    private String mCurrentVehicleId;
    private FirebaseRecyclerAdapter<Vehicle, VehicleViewHolder> mAdapter;

    public VehicleFragment() {
    }

    public static VehicleFragment newInstance() {
        return new VehicleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vehicle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView vehicleList = view.findViewById(R.id.vehicle_list);
        vehicleList.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = FirebaseVehicle.getVehicleRef();

        FirebaseRecyclerOptions<Vehicle> options =
                new FirebaseRecyclerOptions.Builder<Vehicle>()
                        .setQuery(query, Vehicle.class)
                        .setLifecycleOwner(this)
                        .build();

        mCurrentVehicleId = ShPrefManager.getCurrentVehicleId(view.getContext());

        mAdapter = new FirebaseRecyclerAdapter<Vehicle, VehicleViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final VehicleViewHolder holder, int position, @NonNull final Vehicle model) {
                holder.mManufacturerView.setText(model.getManufacturer());
                holder.mModelView.setText(model.getModel());

                long photoTimestamp = model.getPhotoTimestamp();
                if (photoTimestamp > 0) {
                    GlideApp.with(VehicleFragment.this)
                            .load(FirebaseVehicle.getCurrentPhotoVehicleRef(model))
                            .signature(new MediaStoreSignature("image/jpeg", photoTimestamp, 0))
                            .centerCrop()
                            .into(holder.mPhotoView);
                } else {
                    holder.mPhotoView.setImageResource(R.drawable.car_default_image);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();
                        mListener.onClickVehicleItem(getVehicleId(position));
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position = holder.getLayoutPosition();
                        mListener.onLongClickVehicleItem(getVehicleId(position));
                        return false;
                    }
                });

                if (model.getId().equals(mCurrentVehicleId)) {
                    holder.mStarView.setImageResource(android.R.drawable.star_on);
                } else {
                    holder.mStarView.setImageResource(android.R.drawable.star_off);
                }

                holder.mStarView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSetCurrentVehicle(model.getId());
                        mListener.onChangeCurrentVehicle(holder.getLayoutPosition());
                    }
                });
            }

            private String getVehicleId(int position) {
                return getItem(position).getId();
            }


            @NonNull
            @Override
            public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.vehicle_list_item, parent, false);
                return new VehicleViewHolder(v);
            }
        };

        vehicleList.setHasFixedSize(true);
        vehicleList.setAdapter(mAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVehicleItemListener) {
            mListener = (OnVehicleItemListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnVehicleItemListener");
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
        mAdapter.notifyDataSetChanged();
    }

    public interface OnVehicleItemListener {
        void onClickVehicleItem(String vehicleId);

        void onLongClickVehicleItem(String vehicleId);

        void onChangeCurrentVehicle(int position);
    }

    public static class VehicleViewHolder extends RecyclerView.ViewHolder {
        ImageView mPhotoView;
        ImageView mStarView;
        TextView mManufacturerView;
        TextView mModelView;

        VehicleViewHolder(View itemView) {
            super(itemView);

            mPhotoView = itemView.findViewById(R.id.iv_vehicle_photo);
            mManufacturerView = itemView.findViewById(R.id.tv_vehicle_manufacturer);
            mModelView = itemView.findViewById(R.id.tv_vehicle_model);
            mStarView = itemView.findViewById(R.id.iv_current_vehicle_star);
        }
    }
}
