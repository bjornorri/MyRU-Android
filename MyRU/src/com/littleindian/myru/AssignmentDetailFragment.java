package com.littleindian.myru;

import java.io.UnsupportedEncodingException;

import com.littleindian.myru.model.RUAssignment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

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
			mWebView.loadUrl("javascript:var page=document.getElementsByClassName('item entry')[0];var bd=document.getElementById('bd');bd.innerHTML=page.innerHTML;var bla=document.getElementById('doc3');bla.setAttribute('id', '');bla.setAttribute('class', '');document.getElementById('ft').style.display='none'");
			
			// Hide progress bar and display mWebView
			mProgressBar.setVisibility(View.GONE);
			mWebView.setVisibility(View.VISIBLE);
		};
		
		public void onReceivedHttpAuthRequest(WebView view, android.webkit.HttpAuthHandler handler, String host, String realm)
		{
			handler.proceed("", "");
		};
		
		public void onReceivedSslError(WebView view, android.webkit.SslErrorHandler handler, android.net.http.SslError error)
		{
			handler.proceed();
		};
	};
}
