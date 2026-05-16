package com.abruce.manille;

import android.app.Application;
import android.content.Context;

public class ManilleApp extends Application
{
	private static ManilleApp instance;
	private static ManilleActivity ma;

	public static ManilleApp getInstance()	{ return instance; }

	public static Context getContext()	{ return instance; }

	public static void setActivity(ManilleActivity m)  { ma = m; }
	public static ManilleActivity getActivity()  { return ma; }
	@Override
	public void onCreate()
	{
		instance = this;
		super.onCreate();
	}
}
