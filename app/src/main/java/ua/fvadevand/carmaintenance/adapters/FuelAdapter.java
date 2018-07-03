package ua.fvadevand.carmaintenance.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ua.fvadevand.carmaintenance.R;
import ua.fvadevand.carmaintenance.data.Fuel;
import ua.fvadevand.carmaintenance.utilities.TextFormatUtils;

public class FuelAdapter extends RecyclerView.Adapter<FuelAdapter.FuelViewHolder> {

    private List<Fuel> mFuelList;
    private TextFormatUtils mFormatUtils;

    public FuelAdapter(Context context, List<Fuel> fuelList) {
        mFuelList = fuelList;
        mFormatUtils = new TextFormatUtils(context);
    }

    @NonNull
    @Override
    public FuelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fuel_item, parent, false);
        return new FuelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FuelViewHolder holder, int position) {
        Fuel fuel = mFuelList.get(position);
        holder.mTypeView.setText(fuel.getType());
        holder.mPriceView.setText(mFormatUtils.priceUnitFormat(fuel.getPrice()));
    }

    @Override
    public int getItemCount() {
        return mFuelList.size();
    }

    class FuelViewHolder extends RecyclerView.ViewHolder {

        TextView mTypeView;
        TextView mPriceView;

        FuelViewHolder(View itemView) {
            super(itemView);

            mTypeView = itemView.findViewById(R.id.tv_fuel_type);
            mPriceView = itemView.findViewById(R.id.tv_fuel_price);
        }
    }
}
