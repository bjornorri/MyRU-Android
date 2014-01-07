package com.littleindian.myru.model;

/**
 * Created by bjornorri on 24/12/13.
 */
public class RUAssignment
{
    private String title;
    private String courseId;
    private String courseName;
    private String handedIn;
    private String dueDate;
    private String assignmentURL;

    public String getTitle()
    {
        return title;
    }

    public String getCourseId()
    {
        return courseId;
    }
    
    public String getCourseName()
    {
        return courseName;
    }

    public String getHandedIn()
    {
        return handedIn;
    }

    public String getDueDate()
    {
        return dueDate;
    }

    public String getAssignmentURL()
    {
        return assignmentURL;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setCourseId(String courseId)
    {
        this.courseId = courseId;
    }
    
    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }

    public void setHandedIn(String handedIn)
    {
        this.handedIn = handedIn;
    }

    public void setDueDate(String dueDate)
    {
        this.dueDate = dueDate;
    }

    public void setAssignmentURL(String assignmentURL)
    {
        this.assignmentURL = assignmentURL;
    }
}
