package com.littleindian.myru;

import com.littleindian.myru.model.RUGrade;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GradeDetailFragment extends Fragment
{
	private RUGrade grade;
	private TextView mCourseLabel;
	private TextView mGradeLabel;
	private TextView mRankLabel;
	private TextView mFeedbackView;
	
	
	public void setGrade(RUGrade grade)
	{
		this.grade = grade;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the view
		View view = inflater.inflate(R.layout.grade_detailview, container, false);
		
		// Link the TextViews to the UI
		mCourseLabel = (TextView) view.findViewById(R.id.courseLabel);
		mGradeLabel = (TextView) view.findViewById(R.id.gradeLabel);
		mRankLabel = (TextView) view.findViewById(R.id.rankLabel);
		mFeedbackView = (TextView) view.findViewById(R.id.feedbackView);
		
		// Display the information about the RUGrade
		mCourseLabel.setText(grade.getCourse());
		mGradeLabel.setText(grade.getGrade());
		mRankLabel.setText(grade.getRankOnly());
		
		// If the feedback is empty it contains &nbsp (&#x00a0;)
		if(!grade.getFeedback().equals(" "))
		{
			mFeedbackView.setText((grade.getFeedback()));
		}
		else
		{
			mFeedbackView.setText("No feedback");
		}
		
		
		// Return the view
		return view;
	}
	
	@Override
	public void onResume()
	{
		// Set the action bar title
		TextView title = (TextView) getActivity().findViewById(R.id.actionBarTitle);
		title.setText(this.grade.getAssignmentName());
		super.onResume();
	}
}
