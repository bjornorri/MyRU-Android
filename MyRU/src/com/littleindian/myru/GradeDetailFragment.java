package com.littleindian.myru;

import com.littleindian.myru.model.RUGrade;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GradeDetailFragment extends Fragment
{
	private RUGrade grade;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.grade_detailview, container, false);
	}
	
	public void setGrade(RUGrade grade)
	{
		this.grade = grade;
	}
}
