package com.littleindian.myru;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.littleindian.myru.model.RUClass;
import com.littleindian.myru.model.RUAssignment;
import com.littleindian.myru.model.RUGrade;

/**
 * Created by bjornorri on 24/12/13.

 */

public class RUData
{
	private Context context;
	private String username;
	private String password;
    private String basicAuthentication;
    private Document timeTablePage;
    private Document page;
    private ArrayList<RUClass> classes;
    private ArrayList<RUAssignment> assignments;
    private ArrayList<ArrayList<RUGrade>> grades;



    private static RUData ourInstance = new RUData();

    public static RUData getInstance()
    {
        return ourInstance;
    }

    private RUData()
    {
    	context = null;
    	username = null;
    	password = null;
    	basicAuthentication = null;
    	page = null;
    	classes = new ArrayList<RUClass>();
        assignments = new ArrayList<RUAssignment>();
        grades = new ArrayList<ArrayList<RUGrade>>();
    }
    
    public void setContext(Context context)
    {
    	this.context = context;
    	
    	// Load the basic authentication string
    	SharedPreferences preferences = context.getSharedPreferences("com.littleindian.myru", Context.MODE_PRIVATE);
    	this.basicAuthentication = preferences.getString("Authentication", null);
    	
    	// Get username and password from basicAuthentication string
    	if(this.basicAuthentication != null)
    	{
	    	String base64Credentials = this.basicAuthentication.substring("Basic".length()).trim();
	        String credentials = new String(Base64.decode(base64Credentials, 0), Charset.forName("UTF-8"));
	        
	        final String[] auth = credentials.split(":",2);
	        
	        // Set the username and password
	        this.username = auth[0];
	        this.password = auth[1];
    	}
    }
    
    public Context getContext()
    {
    	return context;
    }
    
    public boolean userIsLoggedIn()
    {
    	return (basicAuthentication != null);
    }
    
    // Has any data been loaded yet?
    public boolean noDataLoaded()
    {
    	// If the page has been loaded, it can't be null
    	return (page == null);
    }
    
    public String getAuthentication()
    {
    	return basicAuthentication;
    }
    
    public String getUsername()
    {
    	return username;
    }
    
    public String getPassword()
    {
    	return password;
    }
    
    public ArrayList<RUClass> getClasses()
    {
    	return classes;
    }
    
    public ArrayList<ArrayList<RUGrade>> getGrades()
    {
    	return grades;
    }
    
    public ArrayList<RUAssignment> getAssignments()
    {	
    	return assignments;
    }
    
    public void setAuthentication(String basicAuthentication)
    {
    	this.basicAuthentication = basicAuthentication;
    	SharedPreferences preferences = context.getSharedPreferences("com.littleindian.myru", Context.MODE_PRIVATE);
    	SharedPreferences.Editor editor = preferences.edit();
    	
    	if(basicAuthentication == null)
    	{
    		editor.remove("Authentication");
    	}
    	else
    	{
    		editor.putString("Authentication", basicAuthentication);
    	}
    	editor.commit();
    }
    // Page loading
    
    public int loadTimeTablePage()
    {
    	Response response = null;
    	int statusCode = -1;
    	
    	try
		{
			response = Jsoup.connect("https://myschool.ru.is/myschool/?Page=Exe&ID=3.2").header("Authorization", this.basicAuthentication).method(Method.GET).timeout(10 * 1000).execute();
			statusCode = response.statusCode();
		}
    	catch (IOException e)
		{
    		// Network error
			if(e.getClass().getPackage().getName().equalsIgnoreCase("java.net"))
    		{
    			statusCode = 408;
    		}
			// Probably wrong login data
			else
			{
				statusCode = 401;
			}
			e.printStackTrace();
		}
    	
    	if(statusCode == 200)
    	{
    		try
			{
				timeTablePage = response.parse();
			}
    		catch (IOException e)
			{
				e.printStackTrace();
			}
    	}
    	return statusCode;
    }
    
    public int loadPage()
    {
    	Response response = null;
    	int statusCode = -1;
    	try
		{
			response = Jsoup.connect("https://myschool.ru.is/myschool/?Page=Exe&ID=1.12").header("Authorization", this.basicAuthentication).method(Method.GET).timeout(10 * 1000).execute();
			statusCode = response.statusCode();
		}
    	catch (IOException e)
		{
    		// Network error
			if(e.getClass().getPackage().getName().equalsIgnoreCase("java.net"))
    		{
    			statusCode = 408;
    		}
			// Probably wrong login data
			else
			{
				statusCode = 401;
			}
			e.printStackTrace();
		}
    	
    	if(statusCode == 200)
    	{
    		try
			{
				page = response.parse();
			}
    		catch (IOException e)
			{
				e.printStackTrace();
			}
    	}
    	return statusCode;
    }
    
    public int refreshData()
    {
    	int statusCode1 = loadPage();
    	int statusCode2 = loadTimeTablePage();
    	
    	if(statusCode1 == 200 && statusCode2 == 200)
    	{
    		classes.clear();
    		assignments.clear();
    		grades.clear();
    		parseHTML();
    		return statusCode1;
    	}
    	else if(statusCode1 == 200)
    	{
    		return statusCode2;
    	}
    	else if(statusCode2 == 200)
    	{
    		return statusCode1;
    	}
    	return statusCode1;
    }
    
    public void clearData()
    {
    	classes.clear();
    	assignments.clear();
    	grades.clear();
    	timeTablePage = null;
    	page = null;
    	username = null;
    	password = null;
    	setAuthentication(null);
    }
    
    // Parsing

    public void parseHTML()
    {
    	Elements jsoupClasses = null;
    	Elements jsoupAssignments = null;
		Elements jsoupGrades = null;
		
    	if(page != null && timeTablePage != null)
    	{
    		// Parse assignments and grades
    		Elements tables = page.getElementsByClass("ruTable");
    		
    		if(tables.size() == 2)
    		{
    			jsoupAssignments = tables.get(0).select("tbody > tr");
    			jsoupGrades = tables.get(1).select("tbody > tr");
    		}
    		else if(tables.size() == 1)
    		{
    			Elements tableHeader = page.select("div.ruContentPage > h4");
    			String header = tableHeader.get(0).text();
    			
    			if(header.equals("Næstu verkefni"))
    			{
    				jsoupAssignments = page.select("div.ruContentPage > center > table.ruTable > tbody > tr");
    			}
    			else
    			{
    				jsoupGrades = page.select("div.ruContentPage > center > table.ruTable > tbody > tr");
    			}
    		}
    		
    		// Parse classes
    		Elements ruTables = timeTablePage.getElementsByClass("ruTable");
    		if(ruTables.size() > 0)
    		{
    			jsoupClasses = ruTables.get(0).select("tbody > tr");
    		}
    	}
    	parseClasses(jsoupClasses);
    	parseAssignments(jsoupAssignments);
    	parseGrades(jsoupGrades);
    	return;
    }
    
    private void parseClasses(Elements jsoupClasses)
    {
    	if(jsoupClasses != null)
    	{
    		// Get a number for the day of the week, this is used as a column index for the timetable
    		Date now = new Date();
    		Calendar c = Calendar.getInstance();
    		c.setTime(now);
    		int columnIndex = c.get(Calendar.DAY_OF_WEEK);
    		
    		// Extract the rows that contain valuable data
    		ArrayList<Element> rows = new ArrayList<Element>();
    		for(int i = 3; i < jsoupClasses.size() - 1; i++)
    		{
    			rows.add(jsoupClasses.get(i));
    		}
    		
    		for(Element row : rows)
    		{
    			if(row.children().size() > 0)
    			{
    				Elements columns = row.children();
    				
    				// There should be 8 columns
    				if(columns.size() == 8)
    				{
    					// Fetch today's column from the row
    					Element todayColumn = columns.get(columnIndex);
    					Elements columnContents = todayColumn.children();
    					
    					
    					for(Element child : columnContents)
    					{
    						if(child.tagName().equals("span"))
    						{
    							String infoString = child.attr("title");
    							String[] info = infoString.split("\n");
    							
    							// Set RUClass attributes, careful about newlines in strings
    							RUClass cls = new RUClass();
    							
    							// Set course
    							cls.setCourse(info[0].substring(0, info[0].length() - 1));
    							
    							// Set type
    							String type = info[2].substring(0, info[2].length() - 1);
    							if(type.equals("Fyrirlestrar"))
    							{
    								cls.setType("Fyrirlestur");
    							}
    							else if(type.equals("Dæmatímar"))
    							{
    								cls.setType("Dæmatími");
    							}
    							else
    							{
    								cls.setType(type);
    							}
    							
    							// Set teachers
    							// Not going to do this now, it causes errors and it is not really needed as is
    							/*for(int i = 0; i < info.length; i++)
    							{
    								if(info[i].contains("Kennari: "))
    								{
    									cls.getTeachers().add(info[i].substring(9));
    								}
    							}*/
    							
    							// Set location
    							Elements smalls = child.select("a > small");
    							Element small = smalls.first();
    							int indexOfLocation = (small.text()).indexOf("stofa");
    							String locationString = small.text().substring(indexOfLocation);
    							String location = locationString.substring(6);
    							cls.setLocation(location);
    							
    							// Set start and end time strings
    							Element timeColumn = columns.get(0);
    							// split on &nbsp
    							String[] time = timeColumn.text().split("\u00a0");
    							cls.setStartString(time[0]);
    							cls.setEndString(time[1]);
    							
    							// Set actual start and end times
    							String[] startTime = cls.getStartString().split(":");
    							String[] endTime = cls.getEndString().split(":");
    							
    							int startHour = Integer.parseInt(startTime[0]);
    							int startMinute = Integer.parseInt(startTime[1]);
    							int endHour = Integer.parseInt(endTime[0]);
    							int endMinute = Integer.parseInt(endTime[1]);
    							
    							Calendar startDate = new GregorianCalendar();
    							Calendar endDate = new GregorianCalendar();
    							
    							startDate.set(Calendar.HOUR_OF_DAY, startHour);
    							startDate.set(Calendar.MINUTE, startMinute);
    							startDate.set(Calendar.SECOND, 0);
    							startDate.set(Calendar.MILLISECOND, 0);
    							
    							endDate.set(Calendar.HOUR_OF_DAY, endHour);
    							endDate.set(Calendar.MINUTE, endMinute);
    							endDate.set(Calendar.SECOND, 0);
    							endDate.set(Calendar.MILLISECOND, 0);
    							
    							cls.setStartDate(startDate);
    							cls.setEndDate(endDate);
    							
    							this.classes.add(cls);

    							/*
    							log("------------------------------------------------------");
    							log("Course: " + cls.getCourse());
    							log("Type: " + cls.getType());
    							log("Location: " + cls.getLocation());
    							log("Start: " + cls.getStartString());
    							log("End: " + cls.getEndString());
    							*/
    						}
    					}
    				}
    			}
    		}
    	}
    }
    
    private void parseAssignments(Elements jsoupAssignments)
    {
    	if(jsoupAssignments != null)
    	{
    		for(int i = 0; i < jsoupAssignments.size() - 1; i++)
    		{
    			assignments.add(new RUAssignment());
    		}
    		
    		int editing = 0;
    		
    		for(Element element : jsoupAssignments)
    		{
    			if(element.children().size() > 0)
    			{
    				int counter = 0;
    				
    				for(Element child : element.children())
    				{
    					if(child.text() != null)
    					{
    						switch(counter)
    						{
    						case 0:
    							assignments.get(editing).setDueDate(child.text().substring(0, 5));
    							break;
    						case 1:
    							assignments.get(editing).setHandedIn(child.text());
    						case 2:
    							assignments.get(editing).setCourseId(child.text());
    						case 3:
    							assignments.get(editing).setCourseName(child.text());
    						default:
    							break;
    						}
    						counter++;
    					}
    					
    					if(child.children().size() > 0)
    					{
    						for(Element grandChild : child.children())
    						{
    							if(grandChild.text() != null)
    							{
    								assignments.get(editing).setTitle(grandChild.text());
    							}
    							
    							if(grandChild.tagName().equals("a"))
    							{
    								assignments.get(editing).setAssignmentURL(grandChild.attr("href"));
    							}
    						}
    					}
    				}
    				editing++;
    			}
    		}
    		
    		if(assignments.size() > 1)
    		{
    			assignments.remove(0);
    		}
    	}
    	return;
    }

    private void parseGrades(Elements jsoupGrades)
    {
    	if(jsoupGrades != null)
    	{
    		ArrayList<RUGrade> grades = new ArrayList<RUGrade>();
    		String currentCourse = null;
    		
    		for(Element element : jsoupGrades)
    		{
    			RUGrade thisGrade = new RUGrade();
    			
    			if(element.children().size() > 0)
    			{
    				int counter = 0;
    				
    				for(Element child : element.children())
    				{
    					if(child.text() != null)
    					{
    						if(child.tagName().equals("th"))
    						{
    							currentCourse = child.text();
    						}
    						
    						switch(counter)
    						{
    						case 1:
    							thisGrade.setGrade(child.text());
    							break;
    						case 2:
    							thisGrade.setRank(child.text());
    						case 3:
    							if(child.textNodes().size() > 0)
    							{
    								thisGrade.setFeedback(child.textNodes().get(0).getWholeText());
    							}
    							// Think I don't need this...but just to be sure
    							else
    							{
    								thisGrade.setFeedback(child.text());
    							}
    						default:
    							break;
    						}
    						thisGrade.setCourseName(currentCourse);
    						counter++;
    					}
    					
    					if(child.children().size() > 0)
    					{
    						for(Element grandChild : child.children())
    						{
    							if(grandChild.text() != null)
    							{
    								thisGrade.setAssignmentName(grandChild.text());
    							}
    						}
    					}
    				}
    				grades.add(thisGrade);
    			}
    		}
    		Collections.sort(grades, new gradeComparator());
    		String thisCourseName;
    		
    		for(int i = 0; i < grades.size();)
    		{
    			ArrayList<RUGrade> thisCourse = new ArrayList<RUGrade>();
    			thisCourseName = grades.get(i).getCourseName();
    			
    			while(i < grades.size() && grades.get(i).getCourseName().equals(thisCourseName))
    			{
    				if(grades.get(i).getAssignmentName() != null)
    				{
    					thisCourse.add(grades.get(i));
    				}
    				i++;
    			}
    			
    			if(thisCourse.size() > 0)
    			{
    				this.grades.add(thisCourse);
    			}
    			else
    			{
    				i++;
    			}
    		}
    	}
    	return;
    }
    
    private class gradeComparator implements Comparator<RUGrade>
    {
		@Override
		public int compare(RUGrade grade1, RUGrade grade2)
		{
			return grade1.getCourseName().compareTo(grade2.getCourseName());
		}
    }
    
    
   /* // Dummy method
    public ArrayList<RUAssignment> getAssignmentsDummy()
    {	
		ArrayList<RUAssignment> data = new ArrayList<RUAssignment>();
		
		RUAssignment a = new RUAssignment();
		a.setAssignmentURL("https://myschool.ru.is/myschool/?Page=Exe&ID=2.4&ViewMode=2&fagid=25070&verkID=42566");
		a.setTitle("Bomblab");
		a.setCourseName("Tölvuhögun");
		a.setDueDate("13.01");
		a.setHandedIn("Óskilað");
		
		RUAssignment b = new RUAssignment();
		b.setAssignmentURL("https://myschool.ru.is/myschool/?Page=Exe&ID=2.4&ViewMode=2&fagid=25070&verkID=42566");
		b.setTitle("Dæmatímaverkefni 5");
		b.setCourseName("Reiknirit");
		b.setDueDate("24.12");
		b.setHandedIn("Skilað");
		
		RUAssignment c = new RUAssignment();
		c.setAssignmentURL("https://myschool.ru.is/myschool/?Page=Exe&ID=2.4&ViewMode=2&fagid=25070&verkID=42566");
		c.setTitle("Síðannarverkefni");
		c.setCourseName("Hugbúnaðarfræði");
		c.setDueDate("05.05");
		c.setHandedIn("Óskilað");
		
		data.add(a);
		data.add(b);
		data.add(c);
		
		return data;
    }
    
    // Dummy method
    public ArrayList<ArrayList<RUGrade>> getGradesDummy()
    {
    	ArrayList<ArrayList<RUGrade>> grades = new ArrayList<ArrayList<RUGrade>>();
    	
    	ArrayList<RUGrade> tolh = new ArrayList<RUGrade>();
    	ArrayList<RUGrade> reir = new ArrayList<RUGrade>();
    	
    	RUGrade a = new RUGrade();
    	a.setAssignmentName("Datalab");
    	a.setCourseName("Tölvuhögun");
    	a.setFeedback("Hefðir átt að gera þetta betur!");
    	a.setGrade("Einkunn: 9");
    	a.setRank("Röð: 12 - 19");
    	
    	RUGrade b = new RUGrade();
    	b.setAssignmentName("HW2");
    	b.setCourseName("Tölvuhögun");
    	b.setFeedback("");
    	b.setGrade("Einkunn: 11");
    	b.setRank("Röð: 1");
    	
    	tolh.add(a);
    	tolh.add(b);
    	
    	RUGrade c = new RUGrade();
    	c.setAssignmentName("Skilaverkefni 3");
    	c.setCourseName("Reiknirit");
    	c.setFeedback("Frábært!");
    	c.setGrade("Einkunn: 10");
    	c.setRank("Röð: 1 - 15");
    	
    	reir.add(c);
    	
    	grades.add(tolh);
    	grades.add(reir);
    	
    	return grades;
    }*/

    /*public void log(String msg)
    {
    	Log.i("RUData log", msg);
    	return;
    }*/
}
