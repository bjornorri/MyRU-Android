package com.littleindian.myru;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Gravity;
import android.view.LayoutInflater;
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
		
		
		// Log out at startup to test login screen
		//RUData.getInstance().setAuthentication(null);
		
		// If no user is logged in, finish this activity and go to login activity
		if(!RUData.getInstance().userIsLoggedIn())
		{
			finish();
			// No animation
			overridePendingTransition(0, 0);
			startActivity(new Intent(getBaseContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
		}
		
		
		/* TabHost setup begins*/
		
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		
		// Remove dividers between tabs
		mTabHost.getTabWidget().setStripEnabled(false);
		mTabHost.getTabWidget().setDividerDrawable(null);
		
		/* TabHost setup ends */
		
		/* Tab creation begins */
		
		// Timetable tab
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
		((TextView)tabIndicator.findViewById(R.id.tabTitle)).setText("Stundatafla");
		((ImageView)tabIndicator.findViewById(R.id.tabIcon)).setImageResource(R.drawable.timetable_icon);
		mTabHost.addTab(mTabHost.newTabSpec(ApplicationConstants.tab0).setIndicator(tabIndicator), TimeTableFragment.class, null);
		
		// Assignments tab
		tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
		((TextView)tabIndicator.findViewById(R.id.tabTitle)).setText("Verkefni");
		((ImageView)tabIndicator.findViewById(R.id.tabIcon)).setImageResource(R.drawable.assignments_icon);
		mTabHost.addTab(mTabHost.newTabSpec(ApplicationConstants.tab1).setIndicator(tabIndicator), TabContainerFragment1.class, null);
		
		// Grades tab
		tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
		((TextView)tabIndicator.findViewById(R.id.tabTitle)).setText("Einkunnir");
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
	public void onBackPressed()
	{
		// If assignment tab
		if(mTabHost.getCurrentTab() == 1)
		{
			TabContainerFragment1 container = (TabContainerFragment1) getSupportFragmentManager().findFragmentByTag(ApplicationConstants.tab1);
			container.onBackPressed();
		}
		// If grade tab
		else if(mTabHost.getCurrentTab() == 2)
		{
			TabContainerFragment2 container = (TabContainerFragment2) getSupportFragmentManager().findFragmentByTag(ApplicationConstants.tab2);
			container.onBackPressed();
		}
		// If timetable or málið tab (no internal navigation here)
		else
		{
			super.onBackPressed();
		}
	}
	
	public void logOut(View view)
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(R.string.logoutTitle);
		dialog.setMessage(R.string.logoutMessage);
		dialog.setPositiveButton(R.string.Yes, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// Clear all data
				RUData.getInstance().clearData();
				finish();
				startActivity(new Intent(getBaseContext(), LoginActivity.class));
			}
		});
		dialog.setNegativeButton(R.string.No, null);
		AlertDialog shown = dialog.show();
		
		TextView message = (TextView)shown.findViewById(android.R.id.message);
        message.setGravity(Gravity.CENTER);
        message.setTextSize((float) 16.0);
	}
}
