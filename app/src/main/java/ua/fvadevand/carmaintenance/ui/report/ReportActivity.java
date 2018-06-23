package ua.fvadevand.carmaintenance.ui.report;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import ua.fvadevand.carmaintenance.R;
import ua.fvadevand.carmaintenance.dialogs.DatePickerDialogFragment;
import ua.fvadevand.carmaintenance.managers.ShPrefManager;
import ua.fvadevand.carmaintenance.utilities.DateUtils;

public class ReportActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    private static final String LOG_TAG = ReportActivity.class.getSimpleName();

    private static final String KEY_TIMESTAMP_FROM = "TIMESTAMP_FROM";
    private static final String KEY_TIMESTAMP_TO = "TIMESTAMP_TO";

    private static final int ID_DATE_DIALOG_FROM = 0;
    private static final int ID_DATE_DIALOG_TO = 1;


    private SectionsPagerAdapter mSectionsPagerAdapter;

    private String mCurrentVehicleId;
    private Calendar mCalendarFrom;
    private Calendar mCalendarTo;

    private int mCurrentDateDialogId;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TextView mSetDateFromView;
    private TextView mSetDateToView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_report);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mCurrentVehicleId = ShPrefManager.getCurrentVehicleId(this);

        mSetDateFromView = findViewById(R.id.tv_date_from);
        mSetDateToView = findViewById(R.id.tv_date_to);

        mCalendarTo = Calendar.getInstance();
        mCalendarFrom = Calendar.getInstance();

        if (savedInstanceState != null) {
            mCalendarFrom.setTimeInMillis(savedInstanceState.getLong(KEY_TIMESTAMP_FROM));
            mCalendarTo.setTimeInMillis(savedInstanceState.getLong(KEY_TIMESTAMP_TO));
        } else {
            int currentYear = mCalendarTo.get(Calendar.YEAR);
            mCalendarFrom.set(Calendar.YEAR, currentYear - 1);
        }

        displayDate(mSetDateFromView, mCalendarFrom);
        displayDate(mSetDateToView, mCalendarTo);

        mSetDateFromView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(mCalendarFrom.getTimeInMillis(), ID_DATE_DIALOG_FROM);
            }
        });

        mSetDateToView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(mCalendarTo.getTimeInMillis(), ID_DATE_DIALOG_TO);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_TIMESTAMP_FROM, mCalendarFrom.getTimeInMillis());
        outState.putLong(KEY_TIMESTAMP_TO, mCalendarTo.getTimeInMillis());
    }

    private void showDatePickerDialog(long timeInMillis, int id) {
        mCurrentDateDialogId = id;
        DatePickerDialogFragment fragment = DatePickerDialogFragment.newInstance(timeInMillis);
        fragment.show(getSupportFragmentManager(), "DatePicker");
    }

    private void displayDate(TextView view, Calendar calendar) {
        view.setText(DateUtils.formatDate(this, calendar.getTimeInMillis()));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        switch (mCurrentDateDialogId) {
            case ID_DATE_DIALOG_FROM:
                if (isValidDateFrom(year, month, dayOfMonth)) {
                    changeDateFrom(year, month, dayOfMonth);
                } else {
                    showSnackBar("Date \"from\" must precede " + DateUtils.formatDate(this, mCalendarTo.getTimeInMillis()));
                }
                break;
            case ID_DATE_DIALOG_TO:
                if (isValidDateTo(year, month, dayOfMonth)) {
                    changeDateTo(year, month, dayOfMonth);
                } else {
                    showSnackBar("Date \"to\" must be after " + DateUtils.formatDate(this, mCalendarFrom.getTimeInMillis()));
                }
                break;
            default:
                Log.i(LOG_TAG, "onDateSet: unknown date picker");
        }
    }

    private void showSnackBar(String message) {
        Snackbar.make(mViewPager, message, Snackbar.LENGTH_SHORT).show();
    }

    private void changeDateFrom(int year, int month, int dayOfMonth) {
        mCalendarFrom.set(year, month, dayOfMonth);
        displayDate(mSetDateFromView, mCalendarFrom);
        getFragment().changeDateFrom(mCalendarFrom.getTimeInMillis());
    }

    private void changeDateTo(int year, int month, int dayOfMonth) {
        mCalendarTo.set(year, month, dayOfMonth);
        displayDate(mSetDateToView, mCalendarTo);
        getFragment().changeDateTo(mCalendarTo.getTimeInMillis());
    }

    private boolean isValidDateFrom(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return mCalendarTo.after(calendar);
    }

    private boolean isValidDateTo(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return mCalendarFrom.before(calendar);
    }

    private DateChanging getFragment() {
        Fragment fragment = (Fragment) mSectionsPagerAdapter
                .instantiateItem(mViewPager, mViewPager.getCurrentItem());

        if (fragment instanceof DateChanging) {
            return (DateChanging) fragment;
        } else {
            throw new RuntimeException(fragment.toString()
                    + " must implement DateChanging");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public interface DateChanging {
        void changeDateFrom(long timestamp);

        void changeDateTo(long timestamp);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ReportRefuelingFragment.newInstance(mCurrentVehicleId,
                            mCalendarFrom.getTimeInMillis(),
                            mCalendarTo.getTimeInMillis());
                case 1:
                    return null;
                case 2:
                    return null;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    }
}
