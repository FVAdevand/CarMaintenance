package ua.fvadevand.carmaintenance.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ua.fvadevand.carmaintenance.R;
import ua.fvadevand.carmaintenance.firebase.model.Vehicle;

public class VehicleArrayAdapter extends BaseAdapter {

    private List<Vehicle> mVehicleList;
    private final Context mContext;

    public VehicleArrayAdapter(Context context, List<Vehicle> vehicleList) {
        mContext = context;
        mVehicleList = vehicleList;
    }

    @Override
    public int getCount() {
        return mVehicleList.size();
    }

    @Override
    public Object getItem(int position) {
        return mVehicleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getRowView(position, convertView, parent, R.layout.spinner_item);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getRowView(position, convertView, parent, android.R.layout.simple_spinner_dropdown_item);
    }

    private View getRowView(int position, View convertView, ViewGroup parent, int layoutResource) {
        View rowView = convertView;
        if (rowView == null) {
            rowView = LayoutInflater.from(mContext)
                    .inflate(layoutResource, parent, false);
        }
        TextView modelVehicleView = (TextView) rowView;
        modelVehicleView.setText(mVehicleList.get(position).getModel());
        return rowView;
    }

    public int getPosition(Vehicle vehicle) {
        return mVehicleList.indexOf(vehicle);
    }
}
