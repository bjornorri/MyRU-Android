package com.littleindian.myru.model;

import java.util.ArrayList;
import java.util.Date;

public class RUClass
{
	private String course;
	private ArrayList<String> teachers;
	private String type;
	private String location;
	private String startString;
	private String endString;
	private Date startDate;
	private Date endDate;
	
	
	public String getCourse()
	{
		return course;
	}
	public void setCourse(String course)
	{
		this.course = course;
	}
	public ArrayList<String> getTeachers()
	{
		return teachers;
	}
	public void setTeachers(ArrayList<String> teachers)
	{
		this.teachers = teachers;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getLocation()
	{
		return location;
	}
	public void setLocation(String location)
	{
		this.location = location;
	}
	public String getStartString()
	{
		return startString;
	}
	public void setStartString(String startString)
	{
		this.startString = startString;
	}
	public String getEndString()
	{
		return endString;
	}
	public void setEndString(String endString)
	{
		this.endString = endString;
	}
	public Date getStartDate()
	{
		return startDate;
	}
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}
	public Date getEndDate()
	{
		return endDate;
	}
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}
	
	public boolean isOver()
	{
		Date now = new Date();
		return now.after(this.endDate);
	}
	
	public boolean isNow()
	{
		Date now = new Date();
		return (now.after(this.startDate) && now.before(this.endDate));
	}
}


