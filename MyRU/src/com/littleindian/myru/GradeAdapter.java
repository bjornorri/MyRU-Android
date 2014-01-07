package com.littleindian.myru;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.app.Activity;
import com.littleindian.myru.model.RUGrade;


public class GradeAdapter extends ArrayAdapter<ArrayList<RUGrade>>
{
	private static final int VIEW_TYPE_COUNT = 2;
	
	private Context context;
    private int layoutResourceId;
    private ArrayList<ArrayList<RUGrade>> data = null;
    
    public GradeAdapter(Context context, int layoutResourceId, List<ArrayList<RUGrade>> data)
	{
		super(context, layoutResourceId, data);
		this.data = (ArrayList<ArrayList<RUGrade>>)data;
		this.context = context;
		this.layoutResourceId = layoutResourceId;
	}
    
    @Override
    public int getCount()
    {
    	int count = 0;
    	
    	for(int i = 0; i < data.size(); i++)
    	{
    		// Number of headers (courses)
    		count++;
    		
    		for(int j = 0; j < data.get(i).size(); j++)
    		{
    			// Number of grades
    			count++;
    		}
    	}
    	return count;
    }
    
    @Override
    public int getViewTypeCount()
    {
    	// Group headers and normal cells
    	return VIEW_TYPE_COUNT;
    }
    
    @Override
    public int getItemViewType(int position)
    {
    	int courseIndex = 0;
    	
    	while(position > data.get(courseIndex).size())
    	{
    		position -= data.get(courseIndex).size() + 1;
    		courseIndex++;
    	}
    	
    	if(position == 0)
    	{
    		// Header
    		return 1;
    	}
    	// Regular grade
    	return 0;
    }
    
    @Override
    public boolean isEnabled(int position)
    {
    	if(getItemViewType(position) == 0)
    	{
    		return true;
    	}
    	return false;
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
    	View row = convertView;
    	int p = position;
    	int courseIndex = 0;
    	
    	while(position > data.get(courseIndex).size())
    	{
    		position -= data.get(courseIndex).size() + 1;
    		courseIndex++;
    	}
    	
    	// Cell is a regular grade
    	if(getItemViewType(p) == 0)
    	{
    		// Make up for the difference the headers make
    		position--;
    		
    		GradeHolder holder = null;
	    	if(row == null)
	    	{
	    		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
	    		row = inflater.inflate(layoutResourceId, parent, false);
	    		
	    		holder = new GradeHolder();
	    		holder.nameLabel = (TextView)row.findViewById(R.id.gradeNameLabel);
	    		holder.rankLabel = (TextView)row.findViewById(R.id.gradeRankLabel);
	    		holder.gradeLabel = (TextView)row.findViewById(R.id.gradeGradeLabel);
	    		
	    		row.setTag(holder);
	    	}
	    	else
	    	{
	    		holder = (GradeHolder)row.getTag();
	    	}
	    	
	    	
	    	RUGrade grade = data.get(courseIndex).get(position);
	    	holder.nameLabel.setText(grade.getAssignmentName());
	    	holder.rankLabel.setText(grade.getRank());
	    	holder.gradeLabel.setText(grade.getGrade());
    	}
    	
    	// Cell is a header
    	else
    	{
    		CourseHolder holder = null;
    		if(row == null)
	    	{
	    		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
	    		row = inflater.inflate(R.layout.grade_header, parent, false);
	    		
	    		holder = new CourseHolder();
	    		holder.nameLabel = (TextView)row.findViewById(R.id.gradeHeader);
	    		row.setTag(holder);
	    	}
	    	else
	    	{
	    		holder = (CourseHolder)row.getTag();
	    	}
    		
    		String headerTitle = data.get(courseIndex).get(0).getCourse();
    		holder.nameLabel.setText(headerTitle);
    		
    	}
    	return row;
    }
    
    static class GradeHolder
    {
    	TextView nameLabel;
    	TextView rankLabel;
    	TextView gradeLabel;
    }
    
    static class CourseHolder
    {
    	TextView nameLabel;
    }
    
    public void refreshGradeList()
    {
    	this.data = RUData.getInstance().getGrades();
    	notifyDataSetChanged();
    }
}
