package com.n0x3u5.SoRandom;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noxius on 13-03-2015.
 */
public class TabFragments extends Fragment implements ViewPager.OnPageChangeListener,
        TabHost.OnTabChangeListener {

    public static final int TAB_LOGIN = 0;
    public static final int TAB_REG = 1;

    private TabHost tabHost;
    private int currentTab = TAB_LOGIN;
    private ViewPager viewPager;
    private TabPagerAdapter pageAdapter;
    private List<Fragment> fragments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, null);
        tabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewPager.setOnPageChangeListener(this);
        fragments = new ArrayList<Fragment>();
        fragments.add(new Search());
        fragments.add(new Stories());


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        pageAdapter = new TabPagerAdapter(getChildFragmentManager(),
                fragments, getArguments());
        pageAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pageAdapter);
        setupTabs();

    }

    private void setupTabs() {
        tabHost.setup();
        tabHost.addTab(newTab(R.string.search_tab));
        tabHost.addTab(newTab(R.string.stories_tab));


        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {

            tabHost.getTabWidget().getChildAt(i)
                    .setBackgroundColor(Color.parseColor("#304c58"));

            // tabHost.setBackgroundResource(R.drawable.tab_selector);
            final View view = tabHost.getTabWidget().getChildTabViewAt(i);
            final View textView = view.findViewById(android.R.id.title);
            ((TextView) textView).setTextColor(Color.parseColor("#e2ebf0"));

            ((TextView) textView).setSingleLine(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                tabHost.getTabWidget().getChildAt(i)
                        .findViewById(android.R.id.icon);
                tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 75;

            } else {

                if (view != null) {
                    // reduce height of the tab
                    view.getLayoutParams().height *= 0.77;

                    if (textView instanceof TextView) {
                        ((TextView) textView).setGravity(Gravity.CENTER);
                        textView.getLayoutParams().height =  ViewGroup.LayoutParams.MATCH_PARENT;
                        textView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    }
                }
            }

        }
        tabHost.setOnTabChangedListener(TabFragments.this);
        tabHost.setCurrentTab(currentTab);
    }

    private TabHost.TabSpec newTab(int titleId) {
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(getString(titleId));
        tabSpec.setIndicator(getString(titleId));
        tabSpec.setContent(new TabFactory(getActivity()));
        return tabSpec;
    }

    @Override
    public void onPageScrollStateChanged(int position) {

    }

    @Override
    public void onPageScrolled(int position, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        tabHost.setCurrentTab(position);
    }

    @Override
    public void onTabChanged(String tabId) {
        currentTab = tabHost.getCurrentTab();
        viewPager.setCurrentItem(currentTab);
        updateTab();
    }

    @SuppressWarnings("unused")
    private void updateTab() {
        switch (currentTab) {
            case TAB_LOGIN:
                Search login = (Search) fragments.get(currentTab);
                break;
            case TAB_REG:
                Stories register = (Stories) fragments
                        .get(currentTab);
                break;
        }
    }

    class TabFactory implements TabHost.TabContentFactory {

        private final Context context;

        public TabFactory(Context context) {
            this.context = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(context);
            v.setMinimumHeight(0);
            v.setMinimumWidth(0);
            return v;
        }

    }
}
