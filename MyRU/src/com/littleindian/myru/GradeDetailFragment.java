package com.littleindian.myru;

import com.littleindian.myru.model.RUGrade;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GradeDetailFragment extends Fragment
{
	private RUGrade grade;
	private TextView mCourseLabel;
	private TextView mGradeLabel;
	private TextView mRankLabel;
	private TextView mFeedbackView;
	private ImageView mImage;
	
	
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
		mImage = (ImageView) view.findViewById(R.id.ruimage);
		
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
}
