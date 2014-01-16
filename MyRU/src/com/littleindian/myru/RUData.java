package com.littleindian.myru;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.littleindian.myru.model.RUAssignment;
import com.littleindian.myru.model.RUGrade;

/**
 * Created by bjornorri on 24/12/13.

 */
//
public class RUData
{
	private Context context;
	private String username;
	private String password;
    private String basicAuthentication;
    private Document page;
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
			statusCode = 401;
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
    	Log.i("", "Finished loading page");
    	return statusCode;
    }
    
    public int refreshData()
    {
    	int statusCode = loadPage();
    	
    	if(statusCode == 200)
    	{
    		assignments.clear();
    		grades.clear();
    		parseHTML();
    	}
    	return statusCode;
    }
    
    public void clearData()
    {
    	assignments.clear();
    	grades.clear();
    	page = null;
    	setAuthentication(null);
    }
    
    // Parsing

    public void parseHTML()
    {
    	Elements jsoupAssignments = null;
		Elements jsoupGrades = null;
		
    	if(page != null)
    	{
    		Elements tables = page.getElementsByClass("ruTable");
    		
    		if(tables.size() == 2)
    		{
    			Log.i("", "Found 2 tables");
    			jsoupAssignments = tables.get(0).select("tbody > tr");
    			jsoupGrades = tables.get(1).select("tbody > tr");
    		}
    		else if(tables.size() == 1)
    		{
    			Log.i("", "Found 1 table");
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
    		else
    		{
    			return;
    		}
    	}
    	parseAssignments(jsoupAssignments);
    	parseGrades(jsoupGrades);
    	return;
    }
    
    // Test if this works correctly as soon as an assignment is put up on MySchool
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
    							Log.i("", "Staða skila: " + child.text());
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

    
    // This definitely works correctly
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
}
