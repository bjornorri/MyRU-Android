package com.littleindian.myru;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class AssignmentTableFragment extends ListFragment
{
	private FragmentActivity mActivity;
	private TabContainerFragment1 mParentFragment;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		Log.i("", "Creating view");
		return inflater.inflate(R.layout.assignment_tableview, container, false);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Save the activity and parent fragment
		mActivity = this.getActivity();
		mParentFragment = (TabContainerFragment1) this.getParentFragment();
		
		// Set the list's adapter
		AssignmentAdapter adapter = new AssignmentAdapter(mActivity, R.layout.assignment_cell, null);
		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		// Notify the parent fragment that a detail view is being pushed
		mParentFragment.displayingDetailView = true;
		
		AssignmentDetailFragment detailFragment = new AssignmentDetailFragment();
		FragmentManager manager = mParentFragment.getChildFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.tab1container, detailFragment);
		transaction.commit();
	}
	
}
