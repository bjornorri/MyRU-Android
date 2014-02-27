package com.littleindian.myru;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class TimeTableFragment extends ListFragment
{
	private FragmentActivity mActivity;
	private PullToRefreshListView mListView;
	private ProgressBar mProgressBar;
	private TimeTableAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the view
		View view = inflater.inflate(R.layout.timetable_tableview, container, false);
		
		// Link mListView and mProgressBar with the UI
		mListView = (PullToRefreshListView) view.findViewById(R.id.timeTableListView);
		mProgressBar = (ProgressBar) view.findViewById(R.id.timeTableListIndicator);
		
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
		
		// Set the list's adapter
		mAdapter = new TimeTableAdapter(mActivity, R.layout.class_cell, RUData.getInstance().getClasses());
		setListAdapter(mAdapter);
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
		title.setText("Stundatafla");
		
		// Refresh the timetable
		mAdapter.notifyDataSetChanged();
		
		super.onResume();
	}
}
