package ua.fvadevand.carmaintenance.ui.report;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;

import ua.fvadevand.carmaintenance.R;
import ua.fvadevand.carmaintenance.managers.ShPrefManager;
import ua.fvadevand.carmaintenance.utilities.DateUtils;

public class ReportActivity extends AppCompatActivity {

//    public static final int DATE_FROM = 0;
//    public static final int DATE_TO = 0;



    private SectionsPagerAdapter mSectionsPagerAdapter;

    private String mCurrentVehicleId;
    private Calendar mCalendarFrom;
    private Calendar mCalendarTo;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

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

        TextView setDataFromView = findViewById(R.id.tv_date_from);
        TextView setDataToView = findViewById(R.id.tv_date_to);
        mCalendarTo = Calendar.getInstance();
        mCalendarFrom = Calendar.getInstance();
        setDataToView.setText(DateUtils.formatDate(this, mCalendarTo.getTimeInMillis()));
        int currentYear = mCalendarTo.get(Calendar.YEAR);
        mCalendarFrom.set(Calendar.YEAR, currentYear - 1);

        setDataFromView.setText(DateUtils.formatDate(this, mCalendarFrom.getTimeInMillis()));
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
