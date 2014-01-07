package com.littleindian.myru;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabContainerFragment2 extends Fragment
{
	private FragmentActivity mActivity;
	
	// This is set to true when a detail view is pushed...
	// ...determines the behaviour of the back button
	public boolean displayingDetailView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.tab2_container, container, false);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Save the activity
		mActivity = this.getActivity();
		
		// The ltViewis is displayed initially
		this.displayingDetailView = false;
		
		GradeTableFragment fragment = new GradeTableFragment();
		FragmentManager manager = getChildFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(R.id.tab2container, fragment);
		transaction.commit();
	}

	public void onBackPressed()
	{
		// If a detail view is being displayed, display the list view again...
		if(displayingDetailView == true)
		{
			displayingDetailView = false;
			GradeTableFragment fragment = new GradeTableFragment();
			FragmentManager manager = getChildFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.replace(R.id.tab1container, fragment);
			transaction.commit();
		}
		// If the list view is being displayed, close the app
		else
		{
			mActivity.finish();
		}
	}
}
