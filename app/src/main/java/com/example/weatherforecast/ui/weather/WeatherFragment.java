package com.example.weatherforecast.ui.weather;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weatherforecast.R;
import com.example.weatherforecast.ui.weather.recommend.RecommendFragment;
import com.example.weatherforecast.ui.weather.recommend.RecommendViewModel;
import com.example.weatherforecast.ui.weather.today.TodayWeatherFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class WeatherFragment extends Fragment {
    private WeatherViewModel mViewModel;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewpager;
    ArrayList fragmentList = new ArrayList<Fragment>();
    String[] temp = {"今日","推荐"};
    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.weather_fragment, container, false);
        tabLayout = view.findViewById(R.id.tab);
        viewpager = view.findViewById(R.id.vp_main);
        MPagerAdapter mPagerAdapter = new MPagerAdapter(getChildFragmentManager());
        initFragment();
        tabLayout.setupWithViewPager(viewpager);
        viewpager.setAdapter(mPagerAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
    }
    private void initFragment() {
        fragmentList.add(new TodayWeatherFragment());
        fragmentList.add(new RecommendFragment());
    }


    class MPagerAdapter extends FragmentPagerAdapter {


        public MPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return (Fragment) fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        //返回tablayout的标题文字;
        @Override
        public CharSequence getPageTitle(int position) {
            return temp[position];
        }
    }
}