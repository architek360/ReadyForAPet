package edu.chl.dat255.sofiase.readyforapet.viewcontroller;

import java.util.Timer;
import java.util.TimerTask;
import edu.chl.dat255.sofiase.readyforapet.R;
import edu.chl.dat255.sofiase.readyforapet.util.LocationHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Class WalkActivity that creates an instance of LoationHelper and enables the GPS.
 * Measures how far the user walks with the pet and sends that back to PetActivity.
 *
 * Copyright (C) 2013 Katrin Miettinen, Linnea Pettersson, Sofia Selin, Johanna Ydergard
 * 
 * Licensed under the MIT license. This file must only be used in accordance with the license. 
 *
 */
public class WalkActivity extends Activity{

	private TextView displayDistance;
	private ImageView dogPrints;
	private int delay = 0;
	private int period = 15000;
	private int distance = 0;
	private Timer timer;
	private Handler handler = new Handler();
	private Handler uiHandler = new Handler();
	private LocationHelper location;
	private AnimationDrawable anim;
	private Button startWalking;
	private Button stopWalking;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		setContentView(R.layout.walkactivity);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		dogPrints = (ImageView) findViewById(R.id.dogprints);

		startWalking = (Button) findViewById(R.id.startwalking);
		stopWalking = (Button) findViewById(R.id.stopwalking);
		
		stopWalking.setEnabled(false);
		
		//Start walking button
		startWalking.setOnClickListener(new OnClickListener() {
			/**
			 * Enables GPS when start walking. Starts to measure the distance.
			 * Checks if the user has GPS turned on, otherwise asks if it wants to turn the GPS on.
			 * 
			 */
			public void onClick (View v){
				
				stopWalking.setEnabled(true);
				startWalking.setEnabled(false);
				
				location = new LocationHelper(WalkActivity.this);
				
				//Checking if the GPS is enabled, else let the user start GPS if wanted.
				if (location.gpsEnabled()){
					Toast.makeText(WalkActivity.this, "GPS is Enabled on your devide", Toast.LENGTH_SHORT).show();
				}
				
				else{
					showGPSDisabledAlert();
				}
				
				 //Timer to update the textview with the distance walked.
				try{
					timer = new Timer();
					timer.schedule(myTimerTask, delay, period);
				}
				
				catch (Exception e){
					e.printStackTrace();
				}
				
				//Animated dogprints on the screen
				dogPrints.setVisibility(View.VISIBLE);
				dogPrints.setBackgroundResource(R.anim.animation3);
				anim = (AnimationDrawable) dogPrints.getBackground(); 
				anim.start();
				uiHandler.postDelayed(makeViewStop, 7000);
			}
		}
				);

		

		stopWalking.setOnClickListener(new OnClickListener() {
			/**
			 * Method onClick for the stop walking button.
			 * Stops the GPS and sends a result, the distance, to PetActivity.
			 * 
			 * @param v - View
			 */
			public void onClick (View v){
				distance = (int) Math.round(location.getDistance());
				
				//Turn off GPS
				location.killLocationServices();
				
				//If timer is not initiated, the timer should not be stopped
				if (timer != null){
				timer.cancel();
				}
				
				//setting a resultCode with the distance walked that is sent to PetActivity
				WalkActivity.this.setResult(distance);
				WalkActivity.this.finish();


			}
		}
				);

	}
	
	//Stops the dogprint animation
	Runnable makeViewStop = new Runnable(){
		@Override
		public void run(){
			anim.stop();
		}
	};

	//Updates the textview with how far the user has walked
	TimerTask myTimerTask = new TimerTask() {
		@Override
		public void run() {
			handler.post(new Runnable() {
				@Override
				public void run() {
					displayDistance = (TextView) findViewById(R.id.distance);
					displayDistance.setText("You have walked\n" + Math.round(location.getDistance()) + "\nmeters so far.");
				}
			});

		}

	};


	/**
	 * If GPS is turned off, lets the user either choose to enable GPS or cancel.
	 * 
	 */
	private void showGPSDisabledAlert(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage("GPS is disabled on your device. Would you like to enable it?")
		.setCancelable(false)
		.setPositiveButton("Go to Settings Page To Enable GPS",
				new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int id){
				Intent callGPSSettingIntent = new Intent(
						android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(callGPSSettingIntent);
			}
		});
		alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int id){
				dialog.cancel();
			}
		});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
		
		if (location != null){
			location.killLocationServices();
		}
		
	}


	/**
	 * Configurates the navigate Up button in this activity
	 *
	 * @param item - MenuItem
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
