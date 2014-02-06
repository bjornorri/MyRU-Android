package com.littleindian.myru;

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
public class MenuFragment extends Fragment
{
	private WebView mWebView;
	private ProgressBar mProgressBar;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{		
		// Inflate the view and link mWebView and mProgressBar to the UI
		View view = inflater.inflate(R.layout.menu_webview, container, false);
		mWebView = (WebView) view.findViewById(R.id.menuWebView);
		mProgressBar = (ProgressBar) view.findViewById(R.id.menuProgressBar);
		
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
		mWebView.loadUrl("http://malid.ru.is");
		
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
			mWebView.loadUrl("javascript:(function(){var page=document.getElementsByClassName('item entry')[0];var bd=document.getElementById('bd');bd.innerHTML=page.innerHTML;var bla=document.getElementById('doc3');bla.setAttribute('id', '');bla.setAttribute('class', '');document.getElementById('ft').style.display='none'})()");
			
			// Hide progress bar and display mWebView
			mProgressBar.setVisibility(View.GONE);
			mWebView.setVisibility(View.VISIBLE);
		};
	};
	
	@Override
	public void onResume()
	{
		// Set the action bar title
		TextView title = (TextView) getActivity().findViewById(R.id.actionBarTitle);
		title.setText("Málið");
		super.onResume();
	}
}
