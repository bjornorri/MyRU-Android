package com.littleindian.myru;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.littleindian.myru.model.RUAssignment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AssignmentTableFragment extends ListFragment
{
	private FragmentActivity mActivity;
	private TabContainerFragment1 mParentFragment;
	private PullToRefreshListView mListView;
	private ProgressBar mProgressBar;
	private AssignmentAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the view
		View view = inflater.inflate(R.layout.assignment_tableview, container, false);
		
		// Link mListView and mProgressBar with the UI
		mListView = (PullToRefreshListView) view.findViewById(R.id.assignmentListView);
		mProgressBar = (ProgressBar) view.findViewById(R.id.assignmentListIndicator);
		
		// Set the PullToRefresh listener
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>()
		{
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView)
			{
				new Refresh().execute();
			}
		});
		
		
		// Load data if no data has been loaded since launch
		if(RUData.getInstance().noDataLoaded())
		{
			new Refresh().execute();
		}

		// Return the view
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Save the activity and parent fragment
		mActivity = this.getActivity();
		mParentFragment = (TabContainerFragment1) this.getParentFragment();
		
		// Set the list's adapter
		mAdapter = new AssignmentAdapter(mActivity, R.layout.assignment_cell, RUData.getInstance().getAssignments());
		setListAdapter(mAdapter);
	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		//
		// Notify the parent fragment that a detail view is being pushed
		mParentFragment.displayingDetailView = true;
		
		// Fetch the assignment that was clicked
		RUAssignment assignment = RUData.getInstance().getAssignments().get(position - 1);
		
		// Replace the list view with a detail view for the assignment
		AssignmentDetailFragment detailFragment = new AssignmentDetailFragment();
		detailFragment.setAssignment(assignment);
		FragmentManager manager = mParentFragment.getChildFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.tab1container, detailFragment);
		transaction.commit();
	}
	
	// AsyncTask to execute on PullToRefresh event
	private class Refresh extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute()
		{
			// If its not a pull refresh, show the indicator
			if(!mListView.isRefreshing())
			{
				mProgressBar.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);
			}

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0)
		{
			RUData.getInstance().refreshData();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			if(!mListView.isRefreshing())
			{
				mProgressBar.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
			}
			else
			{
				// Hide the pullToRefresh view
				mListView.onRefreshComplete();
			}
			
			// Refresh the list with the new data
			mAdapter.notifyDataSetChanged();
			
			super.onPostExecute(result);
		}
	}
	
	@Override
	public void onResume()
	{
		// Set the action bar title
		TextView title = (TextView) getActivity().findViewById(R.id.actionBarTitle);
		title.setText("Verkefni");
		super.onResume();
	}
}
