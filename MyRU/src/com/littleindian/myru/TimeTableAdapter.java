package com.littleindian.myru;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.littleindian.myru.model.RUClass;

public class TimeTableAdapter extends ArrayAdapter<RUClass>
{
	private Context context;
    private int layoutResourceId;
    private ArrayList<RUClass> data = null;
	
	public TimeTableAdapter(Context context, int resource, List<RUClass> data)
	{
		super(context, resource, data);
		this.data = (ArrayList<RUClass>)data;
		this.context = context;
		this.layoutResourceId = resource;
	}
	
	@Override
	public int getCount()
	{
		int count = 0;
		for(RUClass cls : data)
		{
			if(!cls.isOver())
			{
				count++;
			}
		}
		return count;
	}
	
	@Override
	public boolean isEnabled(int position)
	{
		return false;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = convertView;
		ClassHolder holder = null;
		
		if(row == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			
			holder = new ClassHolder();
			holder.typeImage =(ImageView)row.findViewById(R.id.typeImage);
			holder.courseLabel = (TextView)row.findViewById(R.id.courseLabel);
			holder.typeLabel = (TextView)row.findViewById(R.id.typeLabel);
			holder.locationLabel = (TextView)row.findViewById(R.id.locationLabel);
			holder.startLabel = (TextView)row.findViewById(R.id.startLabel);
			holder.endLabel = (TextView)row.findViewById(R.id.endLabel);
			
			row.setTag(holder);
		}
		else
		{
			holder = (ClassHolder)row.getTag();
		}
		
		// Skip the classes that are over
		RUClass cls = data.get(position + (data.size() - getCount()));

		if(cls.getType().equals("Fyrirlestur"))
		{
			holder.typeImage.setImageResource(R.drawable.lecture);
		}
		else if(cls.getType().equals("Dæmatími"))
		{
			holder.typeImage.setImageResource(R.drawable.lab);
		}
		else if(cls.getType().equals("Viðtalstími"))
		{
			holder.typeImage.setImageResource(R.drawable.help);
		}
		else
		{
			holder.typeImage.setImageResource(R.drawable.rulogo);
		}
		
		holder.courseLabel.setText(cls.getCourse());
		holder.typeLabel.setText(cls.getType());
		holder.locationLabel.setText(cls.getLocation());
		holder.startLabel.setText(cls.getStartString());
		holder.endLabel.setText(cls.getEndString());
		
		// Set alpha to indicate that the class is over
		if(cls.isOver())
		{
			row.setAlpha((float) 0.3);
		}
		else
		{
			row.setAlpha((float) 1.0);
		}
		
		// Implement this later?
		/*if(cls.isNow())
		{	
			View line = new View(this.context);
			line.setBackgroundColor(Color.RED);
		}*/
		
		return row;
	}
	
	static class ClassHolder
	{
		ImageView typeImage;
		TextView courseLabel;
		TextView typeLabel;
		TextView locationLabel;
		TextView startLabel;
		TextView endLabel;
	}
}
