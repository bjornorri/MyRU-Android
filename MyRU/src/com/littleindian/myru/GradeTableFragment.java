package com.littleindian.myru;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class GradeTableFragment extends ListFragment
{
	private FragmentActivity mActivity;
	private TabContainerFragment1 mParentFragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		// Notify the parent fragment that a detail view is being pushed
		mParentFragment.displayingDetailView = true;
	}
}
