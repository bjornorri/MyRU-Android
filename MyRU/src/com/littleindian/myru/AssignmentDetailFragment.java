package com.littleindian.myru;

import com.littleindian.myru.model.RUAssignment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressLint("SetJavaScriptEnabled")
public class AssignmentDetailFragment extends Fragment
{
	private RUAssignment assignment;
	private WebView mWebView;
	private ProgressBar mProgressBar;
	
	public void setAssignment(RUAssignment assignment)
	{
		this.assignment = assignment;
	}
	
	public RUAssignment getAssignment()
	{
		return this.assignment;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{		
		// Inflate the view and link mWebView and mProgressBar to the UI
		View view = inflater.inflate(R.layout.assignment_detailview, container, false);
		mWebView = (WebView) view.findViewById(R.id.assignmentWebView);
		mProgressBar = (ProgressBar) view.findViewById(R.id.assignmentDetailProgressBar);
		
		// Configure settings for mWebView
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setDisplayZoomControls(false);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setUseWideViewPort(true);
		
		// Set the customized WebViewClient for mWebView
		mWebView.setWebViewClient(mWebViewClient);
		
		// Load the page
		mWebView.loadUrl(assignment.getAssignmentURL());
		
		// Return the view
		return view;
	};

	
	// Custom WebViewClient for mWebView
	WebViewClient mWebViewClient = new WebViewClient()
	{
		public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon)
		{
			// Hide mWebView and display progress bar
			mProgressBar.setVisibility(View.VISIBLE);
			mWebView.setVisibility(View.GONE);
		};
		
		public void onPageFinished(WebView view, String url)
		{
			// Execute javascript to hide unnecessary elements on page
			mWebView.loadUrl("javascript:$('.ruHeader a').click(function(e){e.preventDefault()});$('.ruLeft').hide();$('.ruRight').hide();$('.ruFooter').hide();$('#headersearch').hide();$('.level1').hide();$('.resetSize').click();$('.increaseSize').click();$('.increaseSize').click()");
			
			// Hide progress bar and display mWebView
			mProgressBar.setVisibility(View.GONE);
			mWebView.setVisibility(View.VISIBLE);
		};
		
		public void onReceivedHttpAuthRequest(WebView view, android.webkit.HttpAuthHandler handler, String host, String realm)
		{
			handler.proceed(RUData.getInstance().getUsername(), RUData.getInstance().getPassword());
		};
		
		public void onReceivedSslError(WebView view, android.webkit.SslErrorHandler handler, android.net.http.SslError error)
		{
			handler.proceed();
		};
	};
	
	@Override
	public void onResume()
	{
		// Set the action bar title
		TextView title = (TextView) getActivity().findViewById(R.id.actionBarTitle);
		title.setText(this.assignment.getTitle());
		super.onResume();
	}
}
