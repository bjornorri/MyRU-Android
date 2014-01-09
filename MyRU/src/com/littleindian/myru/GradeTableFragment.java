package com.littleindian.myru;

import java.util.ArrayList;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.littleindian.myru.model.RUGrade;

import android.os.AsyncTask;
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
import android.widget.ProgressBar;

public class GradeTableFragment extends ListFragment
{
	private FragmentActivity mActivity;
	private TabContainerFragment2 mParentFragment;
	private PullToRefreshListView mListView;
	private ProgressBar mProgressBar;
	private GradeAdapter mAdapter;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the view
		View view = inflater.inflate(R.layout.grade_tableview, container, false);
		
		// Link mListView and mProgressBar with the UI
		mListView = (PullToRefreshListView) view.findViewById(R.id.gradeListView);
		mProgressBar = (ProgressBar) view.findViewById(R.id.gradeListIndicator);
		
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
			Log.i("GTF", "Initial load");
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
		mParentFragment = (TabContainerFragment2) this.getParentFragment();
		
		// Set the list's adapter
		mAdapter = new GradeAdapter(mActivity, R.layout.grade_cell, RUData.getInstance().getGrades());
		setListAdapter(mAdapter);
	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		// Notify the parent fragment that a detail view is being pushed
		mParentFragment.displayingDetailView = true;
		
		// position counts from 1, we count from 0
		position--;
		
		// Fetch the grade that was clicked
		int courseIndex = 0;
    	ArrayList<ArrayList<RUGrade>> allGrades = RUData.getInstance().getGrades();
    	while(position > allGrades.get(courseIndex).size())
    	{
    		position -= allGrades.get(courseIndex).size() + 1;
    		courseIndex++;
    		Log.i("", "Course index: " + courseIndex);
    	}
    	Log.i("GradeTableFragment", "Getting grade at courseIndex " + courseIndex + " and index " + (position - 1));
    	
    	RUGrade grade = allGrades.get(courseIndex).get(position - 1);
    	
		
		Log.i("GradeTableFragment", "Clicked grade: " + grade.getAssignmentName());
		
		// Replace the list view with a detail view for the assignment
		GradeDetailFragment detailFragment = new GradeDetailFragment();
		detailFragment.setGrade(grade);
		FragmentManager manager = mParentFragment.getChildFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.tab2container, detailFragment);
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
}
