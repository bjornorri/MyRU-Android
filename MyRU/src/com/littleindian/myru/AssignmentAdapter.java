package com.littleindian.myru;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;

import com.littleindian.myru.model.RUAssignment;

public class AssignmentAdapter extends ArrayAdapter<RUAssignment>
{
	private Context context;
    private int layoutResourceId;
    private ArrayList<RUAssignment> data = null;
	
    public AssignmentAdapter(Context context, int resource, List<RUAssignment> data)
	{
		super(context, resource, data);
		this.data = (ArrayList<RUAssignment>)data;
		this.context = context;
		this.layoutResourceId = resource;
		
		
		/* Dummy code begins */
		
		if(data == null)
		{
			this.data = new ArrayList<RUAssignment>();
			
			RUAssignment a = new RUAssignment();
			a.setAssignmentURL("http://malid.ru.is");
			a.setTitle("Bomblab");
			a.setCourseName("Tölvuhögun");
			a.setDueDate("13.01");
			a.setHandedIn("Óskilað");
			
			RUAssignment b = new RUAssignment();
			b.setAssignmentURL("http://malid.ru.is");
			b.setTitle("Dæmatímaverkefni 5");
			b.setCourseName("Reiknirit");
			b.setDueDate("24.12");
			b.setHandedIn("Skilað");
			
			RUAssignment c = new RUAssignment();
			c.setAssignmentURL("http://malid.ru.is");
			c.setTitle("Síðannarverkefni");
			c.setCourseName("Hugbúnaðarfræði");
			c.setDueDate("05.05");
			c.setHandedIn("Óskilað");
			
			this.data.add(a);
			this.data.add(b);
			this.data.add(c);
		}
		
		/* Dummy code ends */
	}
	
	@Override
	public int getCount()
	{
		return data.size();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = convertView;
		AssignmentHolder holder = null;
		
		if(row == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			
			holder = new AssignmentHolder();
			holder.checkImage =(ImageView)row.findViewById(R.id.assignmentCheckImage);
			holder.nameLabel = (TextView)row.findViewById(R.id.assignmentNameLabel);
			holder.courseLabel = (TextView)row.findViewById(R.id.assignmentCourseLabel);
			holder.dateLabel = (TextView)row.findViewById(R.id.assignmentDateLabel);
			
			row.setTag(holder);
		}
		else
		{
			holder = (AssignmentHolder)row.getTag();
		}
		
		RUAssignment assignment = data.get(position);

		if(assignment.getHandedIn().equals("Skilað"))
		{
			// Make check image visible
			holder.checkImage.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.checkImage.setVisibility(View.INVISIBLE);
		}
		
		holder.nameLabel.setText(assignment.getTitle());
		holder.courseLabel.setText(assignment.getCourseName());
		holder.dateLabel.setText(assignment.getDueDate());
		
		return row;
	}
	
	static class AssignmentHolder
	{
		ImageView checkImage;
		TextView nameLabel;
		TextView courseLabel;
		TextView dateLabel;
	}
}
