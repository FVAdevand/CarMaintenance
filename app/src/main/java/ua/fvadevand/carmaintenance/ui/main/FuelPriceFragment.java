package ua.fvadevand.carmaintenance.ui.main;


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
import android.widget.AdapterView;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.fvadevand.carmaintenance.R;
import ua.fvadevand.carmaintenance.adapters.FuelAdapter;
import ua.fvadevand.carmaintenance.adapters.RegionArrayAdapter;
import ua.fvadevand.carmaintenance.app.CarMaintenance;
import ua.fvadevand.carmaintenance.data.Fuel;
import ua.fvadevand.carmaintenance.utilities.PrivatbankApiUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FuelPriceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FuelPriceFragment extends Fragment {

    private static final String LOG_TAG = FuelPriceFragment.class.getSimpleName();

    private List<Fuel> mFuelList;
    private FuelAdapter mFuelAdapter;
    private RegionArrayAdapter mRegionAdapter;
    private Map<String, String> mRegionMap;

    public FuelPriceFragment() {
    }

    public static FuelPriceFragment newInstance() {
        return new FuelPriceFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fuel_price, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFuelList = new ArrayList<>();
        initView(view);
    }

    private void initView(View v) {
        RecyclerView fuelPriceList = v.findViewById(R.id.rv_fuel_price_list);
        fuelPriceList.setLayoutManager(new LinearLayoutManager(v.getContext()));
        fuelPriceList.setHasFixedSize(true);
        mFuelAdapter = new FuelAdapter(getContext().getApplicationContext(), mFuelList);
        fuelPriceList.setAdapter(mFuelAdapter);

        Spinner regionSpinner = v.findViewById(R.id.spinner_region);
        mRegionMap = PrivatbankApiUtils.getRegionMap(v.getContext());
        final List<String> regionList = new ArrayList<>(mRegionMap.keySet());
        Collections.sort(regionList);
        mRegionAdapter = new RegionArrayAdapter(regionList);
        regionSpinner.setAdapter(mRegionAdapter);
        regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fetchFuelList(mRegionMap.get(regionList.get(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fetchFuelList(String regionCode) {

        CarMaintenance.getPrivatbankApi().getData(regionCode).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        mFuelList.clear();
                        mFuelList.addAll(PrivatbankApiUtils.deserializeFuelList(getContext(), response.body().string()));
                        mFuelAdapter.notifyDataSetChanged();
                    }
                } catch (IOException e) {
                    Log.e(LOG_TAG, "onResponse: ", e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
}
