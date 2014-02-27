package com.littleindian.myru.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class RUClass
{
	private String course;
	private ArrayList<String> teachers;
	private String type;
	private String location;
	private String startString;
	private String endString;
	private Calendar startDate;
	private Calendar endDate;
	
	
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
	public Calendar getStartDate()
	{
		return startDate;
	}
	public void setStartDate(Calendar startDate)
	{
		this.startDate = startDate;
	}
	public Calendar getEndDate()
	{
		return endDate;
	}
	public void setEndDate(Calendar endDate)
	{
		this.endDate = endDate;
	}
	
	public boolean isOver()
	{
		Calendar now = new GregorianCalendar();
		return now.after(this.endDate);
	}
	
	public boolean isNow()
	{
		Calendar now = new GregorianCalendar();
		return (now.after(this.startDate) && now.before(this.endDate));
	}
}


