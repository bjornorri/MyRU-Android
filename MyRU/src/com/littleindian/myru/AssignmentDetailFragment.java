package com.littleindian.myru;

import com.littleindian.myru.model.RUAssignment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AssignmentDetailFragment extends Fragment
{
	private RUAssignment assignment;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.assignment_detailview, container, false);
	}
	
	
	public void setAssignment(RUAssignment assignment)
	{
		this.assignment = assignment;
	}
}
