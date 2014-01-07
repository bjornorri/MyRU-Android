package com.littleindian.myru;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;

public class MainActivity extends FragmentActivity
{
	private FragmentTabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Set the context of RUData
		RUData.getInstance().setContext(this);
		
		/* TabHost setup begins*/
		
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		
		// Remove dividers between tabs
		mTabHost.getTabWidget().setStripEnabled(false);
		mTabHost.getTabWidget().setDividerDrawable(null);
		
		/* TabHost setup ends */
		
		/* Tab creation begins */
		
		// Assignments tab
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
		((TextView)tabIndicator.findViewById(R.id.tabTitle)).setText("Assignments");
		((ImageView)tabIndicator.findViewById(R.id.tabIcon)).setImageResource(R.drawable.assignments_icon);
		mTabHost.addTab(mTabHost.newTabSpec(ApplicationConstants.tab1).setIndicator(tabIndicator), TabContainerFragment1.class, null);
		
		// Grades tab
		tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
		((TextView)tabIndicator.findViewById(R.id.tabTitle)).setText("Grades");
		((ImageView)tabIndicator.findViewById(R.id.tabIcon)).setImageResource(R.drawable.grades_icon);
		mTabHost.addTab(mTabHost.newTabSpec(ApplicationConstants.tab2).setIndicator(tabIndicator), TabContainerFragment2.class, null);
		
		// Málið tab
		tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
		((TextView)tabIndicator.findViewById(R.id.tabTitle)).setText("Málið");
		((ImageView)tabIndicator.findViewById(R.id.tabIcon)).setImageResource(R.drawable.menu_icon);
		mTabHost.addTab(mTabHost.newTabSpec(ApplicationConstants.tab3).setIndicator(tabIndicator), MenuFragment.class, null);
		
		/* Tab creation ends */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onBackPressed()
	{
		// If assignment tab
		if(mTabHost.getCurrentTab() == 0)
		{
			TabContainerFragment1 container = (TabContainerFragment1) getSupportFragmentManager().findFragmentByTag(ApplicationConstants.tab1);
			container.onBackPressed();
		}
		// If grade tab
		else if(mTabHost.getCurrentTab() == 1)
		{
			TabContainerFragment2 container = (TabContainerFragment2) getSupportFragmentManager().findFragmentByTag(ApplicationConstants.tab2);
			container.onBackPressed();
		}
		else
		{
			super.onBackPressed();
		}
	}
}
