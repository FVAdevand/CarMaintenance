package ua.fvadevand.carmaintenance.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RegionArrayAdapter extends BaseAdapter {

    private List<String> mRegionList;

    public RegionArrayAdapter(List<String> regionList) {
        mRegionList = regionList;
    }

    @Override
    public int getCount() {
        return mRegionList.size();
    }

    @Override
    public String getItem(int position) {
        return mRegionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getRowView(position, convertView, parent, android.R.layout.simple_spinner_item);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getRowView(position, convertView, parent, android.R.layout.simple_spinner_dropdown_item);
    }

    private View getRowView(int position, View convertView, ViewGroup parent, int layoutResource) {
        View rowView = convertView;
        if (rowView == null) {
            rowView = LayoutInflater.from(parent.getContext())
                    .inflate(layoutResource, parent, false);
        }
        TextView regionView = (TextView) rowView;
        regionView.setText(getItem(position));
        return rowView;
    }
}
