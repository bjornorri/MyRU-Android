package com.littleindian.myru;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.View.OnTouchListener;

public class LoginActivity extends Activity
{
	// Notifies the user that the login failed
	private TextView invalidLabel;
	// Indicates that a login attempt is being made
	private ProgressBar spinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setupUI(findViewById(R.id.parent));

		// Fix password field (stupid Android)
		EditText password = (EditText) findViewById(R.id.passwordField);
		password.setTypeface(Typeface.DEFAULT);
		password.setTransformationMethod(new PasswordTransformationMethod());
		
		// Link invalidLabel and spinner to UI
		invalidLabel = (TextView)findViewById(R.id.invalidLabel);
		spinner = (ProgressBar)findViewById(R.id.spinner);
	}
	
	
	public void loginButtonPressed(View view)
	{	
		// Get the username and password and construct the basic authentication string needed for the requeset header
		EditText usernameField = (EditText)findViewById(R.id.usernameField);
		EditText passwordField = (EditText)findViewById(R.id.passwordField);
		
		String username = usernameField.getText().toString();
		String password = passwordField.getText().toString();
		
		String stringToEncode = username + ":" + password;
		
		String basicAuthentication = "Basic " + Base64.encodeToString(stringToEncode.getBytes(), 0);
		
		Log.i("loginActivity", "Basic Auth: " + basicAuthentication);
		
		// Save it for login persistence
		RUData.getInstance().setAuthentication(basicAuthentication);
		
		// Attempt to log in
		new login().execute();
	}
	
	// This AsyncTask tries to fetch the data from MySchool. If successful, the user is logged in
	private class login extends AsyncTask<Void, Void, Integer>
	{
		
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			
			// Hide the invalidLabel if visible and display the spinner
			spinner.setVisibility(View.VISIBLE);
			invalidLabel.setVisibility(View.INVISIBLE);
		}
		
		@Override
		protected Integer doInBackground(Void... arg0)
		{
			int statusCode = RUData.getInstance().refreshData();
			return statusCode;
		}
		
		@Override
		protected void onPostExecute(Integer statusCode)
		{
			super.onPostExecute(statusCode);
			
			// Log in if successful
			if(statusCode == 200)
			{
				finish();
				startActivity(new Intent(getBaseContext(), MainActivity.class));
			}
			// If unsuccessful, show the invalidLabel
			else
			{
				invalidLabel.setVisibility(View.VISIBLE);
				spinner.setVisibility(View.INVISIBLE);
				
				// Clear all data
				RUData.getInstance().clearData();
			}
		}
	}
	
	
	/* UI keyboard code starts */
	
	public void setupUI(View view)
	{

		// Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText))
		{

			view.setOnTouchListener(new OnTouchListener()
			{
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1)
				{
					dismissKeyboard();
					return false;
				}

			});
		}

		// If a layout container, iterate over children and seed recursion.
		if (view instanceof ViewGroup)
		{

			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
			{

				View innerView = ((ViewGroup) view).getChildAt(i);

				setupUI(innerView);
			}
		}
	}
	
	
	public void dismissKeyboard()
	{
		dismissKeyboard(this);
	}
	
	// Hides the keyboard
	public static void dismissKeyboard(Activity activity)
	{
	    InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}
	
	/* UI keyboard code ends */

}
