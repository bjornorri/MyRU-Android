package com.littleindian.myru;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class MenuFragment extends Fragment
{
	private WebView mWebView;
	private FragmentActivity mActivity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.menu_webview, container, false);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Save the activity
		mActivity = getActivity();
		
		// Link mWebView with the UI WebView
		mWebView = (WebView) mActivity.findViewById(R.id.menuWebView);
		
		if(mWebView == null)
		{
			Log.i("Menu", "mWebView is null!!!");
		}
		
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
	}
	
	
	// Custom WebViewClient for mWebView
	WebViewClient mWebViewClient = new WebViewClient()
	{
		public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon)
		{
			// Hide mWebView and display progress bar
			mActivity.findViewById(R.id.menuProgressBar).setVisibility(View.VISIBLE);
			mActivity.findViewById(R.id.menuWebView).setVisibility(View.GONE);
		};
		
		public void onPageFinished(WebView view, String url)
		{
			// Hide progress bar and display mWebView
			mActivity.findViewById(R.id.menuProgressBar).setVisibility(View.GONE);
			mActivity.findViewById(R.id.menuWebView).setVisibility(View.VISIBLE);
			
			// Execute javascript to hide unnecessary elements on page
			mWebView.loadUrl("javascript:var page=document.getElementsByClassName('item entry')[0];var bd=document.getElementById('bd');bd.innerHTML=page.innerHTML;var bla=document.getElementById('doc3');bla.setAttribute('id', '');bla.setAttribute('class', '');document.getElementById('ft').style.display='none'");
		};
	};
}
