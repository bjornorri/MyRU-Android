package com.littleindian.myru.model;

/**
 * Created by bjornorri on 24/12/13.
 */
public class RUGrade
{
    private String assignmentName;
    private String grade;
    private String rank;
    private String feedback;
    private String courseName;

    public String getAssignmentName()
    {
        return assignmentName;
    }

    public String getGrade()
    {
    	if(grade.length() > 1)
    	{
    		return grade.substring(9);
    	}
    	return "";
    }

    public String getRank()
    {
        return rank;
    }
    
    // Returns the rank without "Röð: "
    public String getRankOnly()
    {
    	if(rank.length() > 1)
    	{
    		return rank.substring(5);
    	}
    	return "";
    }

    public String getFeedback()
    {
        return feedback;
    }

    public String getCourse()
    {
    	String[] arr = courseName.split(" ");
    	String name;
    	
    	if(arr.length > 1)
    	{
    		name = arr[1];
    		
    		for(int i = 2; i < arr.length; i++)
    		{
    			name += " " + arr[i];
    		}
    	}
    	else if(arr.length == 1)
    	{
    		name = arr[0];
    	}
    	else
    	{
    		name = "";
    	}
    	
    	return name;
    }
    
    // Returns the full name of the course, for example T-301-REIR REIKNIRIT instead of REIKNIRIT
    public String getCourseName()
    {
    	return courseName;
    }

    public void setAssignmentName(String assignmentName)
    {
        this.assignmentName = assignmentName;
    }

    public void setGrade(String grade)
    {
        this.grade = grade;
    }

    public void setRank(String rank)
    {
        this.rank = rank;
    }

    public void setFeedback(String feedback)
    {
        this.feedback = feedback;
    }

    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }
}
