package edu.chl.dat255.sofiase.readyforapet.viewcontroller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import edu.chl.dat255.sofiase.readyforapet.R;
import edu.chl.dat255.sofiase.readyforapet.model.Dog;
import edu.chl.dat255.sofiase.readyforapet.model.Pet;
import edu.chl.dat255.sofiase.readyforapet.model.PetMood;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Class PetActivity is the main play activity.
 * Contains a picture and animations of the pet, a MoodBar, and eat, play, walk and sleep buttons.
 * Sends the user to the other play activities PlayActivity, WalkActivity and SleepActivity.
 *
 * Copyright (C) 2013 Katrin Miettinen, Linnea Pettersson, Sofia Selin, Johanna Ydergard
 * 
 * Licensed under the MIT license. This file must only be used in accordance with the license. 
 *
 */
public class PetActivity extends Activity implements Serializable{ 

	private static final long serialVersionUID = 1L;
	private TextView petResponse, showPetAge;
	private Handler uiHandler = new Handler();
	private ImageView dogBiscuit, dogPicture;
	private ProgressBar moodBar;
	private Dog dog;
	private String petName;
	private int petAge;
	private CheckBox musicCheckBox;
	private PetMood petMood;
	private Button play;
	private Button walk;
	private Button eat;
	private Button sleep;
	
	//Variables for playing music in Pet Activity
	private MediaPlayer player;
	private MediaPlayer deathplayer;
	private AssetFileDescriptor afd;
	private AssetFileDescriptor deathAfd;

	//Variables for LogCat outputs when testing
	private final String LOG_TAG1 = "Information about the file when saving";
	private final String LOG_TAG2 = "Information about the file when deleting";

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.petactivity);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		//Receiving the new or saved pet
		if (CreatePetActivity.getPet() != null){
			dog = (Dog) CreatePetActivity.getPet();
		}
		else{
			try {
				dog = (Dog) Pet.load("pet_file.dat", PetActivity.this);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		//Getting the petMood
		petMood = dog.getPetMood();

		//Getting the pet name
		petName = dog.getName();

		//Connecting variables to xml objects
		play = (Button) findViewById(R.id.play);
		walk = (Button) findViewById(R.id.walk);
		eat = (Button) findViewById(R.id.eat);
		sleep = (Button) findViewById(R.id.sleep);
		showPetAge = (TextView) findViewById(R.id.petage);
		petResponse = (TextView) findViewById(R.id.petresponse);
		dogBiscuit = (ImageView) findViewById(R.id.dogbiscuit);
		dogPicture = (ImageView) findViewById(R.id.dogpicture);
		dogBiscuit.setVisibility(View.GONE);
		dogPicture.setVisibility(View.VISIBLE);

		//Initializing the background music
		try {
			afd = getAssets().openFd("readyforapetsong6.m4v");
			player = new MediaPlayer();
			player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			player.setLooping(true);
			player.prepare();
			player.start();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		//Making it possible to turn off music with a checkbox
		addListenerOnMusic();
		musicCheckBox.setChecked(true);

		//Getting the age of the pet if it has not already died
		petAge = (int) (petMood.getCurrentHour() - dog.getBirthHour()) / 24;

		//Setting textview with welcome message
		petResponse.setText("Hello, my name is " + petName + "!");
		petResponse.setVisibility(View.VISIBLE);
		uiHandler.postDelayed(makeTextGone, 2500);

		//Setting textview with current age of the pet
		showPetAge.setText(petName + " is " + petAge + " days old.");
		petResponse.setVisibility(View.VISIBLE);

		//Changing the picture and enabling/disabling buttons depending on mood
		changePicture();

		//Decreasing the moodBar depending on how much time has passed since last eat, walk, play and sleep
		petMood.setFoodMood(petMood.getFoodMood() + petMood.moodBarDecrease(petMood.getLastEatHour(), petMood.getCurrentHour()));
		petMood.setWalkMood(petMood.getWalkMood() + petMood.moodBarDecrease(petMood.getLastWalkHour(), petMood.getCurrentHour()));
		petMood.setPlayMood(petMood.getPlayMood() + petMood.moodBarDecrease(petMood.getLastPlayHour(), petMood.getCurrentHour()));
		petMood.setSleepMood(petMood.getSleepMood() + petMood.moodBarDecrease(petMood.getLastSleepHour(), petMood.getCurrentHour()));
		moodBar = (ProgressBar) findViewById(R.id.moodbar);
		moodBar.setProgress(petMood.getSumMood()); 


		eat.setOnClickListener(new OnClickListener() {
			/**
			 * Making the dog feel less hungry if it is hungry and else give the message i'm full.
			 * Also shows a picture of a dogbisquit when eating.
			 *
			 * @param v - View
			 */
			@Override
			public void onClick (View v){

				petResponse = (TextView) findViewById(R.id.petresponse);
				
				//Disabling buttons when eating
				if(petMood.getFoodMood() < 5){
					play.setEnabled(false);
					eat.setEnabled(false);
					walk.setEnabled(false);
					sleep.setEnabled(false);
					new Handler().postDelayed(new Runnable() { 
						@Override
						public void run() {
							eat.setEnabled(true);
							walk.setEnabled(true);
							play.setEnabled(true);
							sleep.setEnabled(true);
							changePicture();
						}
					}, 10000);
					
					//Getting the pet response of eating
					petResponse.setText(dog.eat());
					petResponse.setVisibility(View.VISIBLE);
					uiHandler.postDelayed(makeTextGone, 10000);
					dogBiscuit.setVisibility(View.VISIBLE);
					dogBiscuit.setBackgroundResource(R.anim.animation);
					
					//Starting animation eating dogbisquit
					final AnimationDrawable anim = (AnimationDrawable) dogBiscuit.getBackground(); 
					anim.start();	
					uiHandler.postDelayed(makeTextGone, 10000);
				}


				else{
					petResponse.setText(dog.eat());
					petResponse.setVisibility(View.VISIBLE);
					uiHandler.postDelayed(makeTextGone, 5000);
				}
				//Updating the moodbar
				moodBar = (ProgressBar) findViewById(R.id.moodbar);
				moodBar.setProgress(petMood.getSumMood());
			}
		});

		
		play.setOnClickListener(new OnClickListener() {
			/**
			 * Sends the user to PlayActivity if the pet is not too hungry.
			 *
			 * @param v - View
			 */
			@Override
			public void onClick (View v){
				//Continuing to playActivity only of the dog has not died and is not too hungry
				if((petMood.getPlayMood() < 5 && petMood.getFoodMood() >= 3) && dog.isAlive()){
					//Opening PlayActivity and receives a requestCode when resuming this activity
					PetActivity.this.startActivityForResult(new Intent(PetActivity.this, PlayActivity.class), 0);
				}
				else{
					petResponse.setText(dog.play(0));
					petResponse.setVisibility(View.VISIBLE);
					uiHandler.postDelayed(makeTextGone, 2000);
				}
				
				changePicture();
			}
		});


		walk.setOnClickListener(new OnClickListener() {
			/**
			 * Sends the user to WalkActivity if the pet wants to walk.
			 * When resuming PetActivity a result is received that tells how far the pet walked.
			 *
			 * @param v - View
			 */
			@Override
			public void onClick (View v){

				petResponse = (TextView) findViewById(R.id.petresponse);

				// Moving to the WalkActivity class if foodMood is high enough and petMood is below 5.
				if(((petMood.getFoodMood() < 3 && petMood.getWalkMood() < 5) || petMood.getWalkMood() == 5)){
					petResponse.setText(dog.walk(0));
					petResponse.setVisibility(View.VISIBLE);
					uiHandler.postDelayed(makeTextGone, 2000);
				}

				else if (dog.isAlive()){
					//Opening PlayActivity and receives a requestCode when resuming this activity
					PetActivity.this.startActivityForResult(new Intent(PetActivity.this, WalkActivity.class), 1);
				}

				else{
					petResponse.setText(dog.walk(0));
					petResponse.setVisibility(View.VISIBLE);
					uiHandler.postDelayed(makeTextGone, 2000);
				}
				changePicture();
			}
		});


		sleep.setOnClickListener(new OnClickListener() {
			/**
			 * Sending the user to SleepActivity if sleepMood is below 5.
			 * When resuming PetActivity a result is received that tells how much the pet has slept.
			 *
			 * @param v - View
			 */
			@Override
			public void onClick (View v){
				
				if (dog.isAlive() && petMood.getSleepMood() < 5){
					//Opening PlayActivity if the dog is alive and receives a requestCode when resuming this activity
					PetActivity.this.startActivityForResult(new Intent(PetActivity.this, SleepActivity.class), 2);
				}

				else{
					//Set the pet's response if it is either dead or not sleepy
					petResponse = (TextView) findViewById(R.id.petresponse);
					petResponse.setText(dog.sleep(0));
					petResponse.setVisibility(View.VISIBLE);
					uiHandler.postDelayed(makeTextGone, 2000);
				}
				changePicture();
			}
		});
		
	}

	Runnable makeTextGone = new Runnable(){
		@Override
		public void run(){
			petResponse.setVisibility(View.GONE);
			dogBiscuit.setVisibility(View.GONE);
		}
	};

	/**
	 * Receives a requestCode when resuming PetActivity from either PlayActivity, WalkActivity or SleepActivity.
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		petResponse = (TextView) findViewById(R.id.petresponse);

		//When coming from the PlayActivity and the dog is done playing, which is when resultCode is 1.
		if(requestCode == 0 && resultCode == 1){
			//Gets the dog's response
			petResponse.setText(dog.play(1));
		}
		//When coming from PetActivity but is not done playing, there is no resultcode.
		else if (requestCode == 0){
			petResponse.setText(dog.play(0));
		}

		//When coming from the WalkActivity
		else if (requestCode == 1){
			//Gets the dog's response
			petResponse.setText(dog.walk(resultCode));
		}

		//When coming from the SleepActivity
		else if (requestCode == 2){
			//Gets the dog's response
			petResponse.setText(dog.sleep(resultCode));
		}
		
		//Sets the value the moodBar should have after playing, walking or taking a nap
		petResponse.setVisibility(View.VISIBLE);
		uiHandler.postDelayed(makeTextGone, 2000);
		moodBar.setProgress(petMood.getSumMood());
		changePicture();
	}

	/**
	 * Connecting the checkbox to the background music.
	 * Checkbox ticked - music is playing
	 * Checkbox not ticked - music is turned off
	 * 
	 */
	public void addListenerOnMusic() {
		musicCheckBox = (CheckBox) findViewById(R.id.checkbox1);
		musicCheckBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					player.start();	
				}
				else {
					player.pause();
				}
			}
		});
	}

	/**
	 * Pauses music player and saves the pet when pausing activity
	 * 
	 */
	@Override
	public void onPause() {
		super.onPause();
		player.pause();

		//Turning off death sound if it is turned on
		if (deathplayer != null){
			deathplayer.pause();
		}

		//Saving the pet
		try { 
			dog.save("pet_file.dat", PetActivity.this);
			
			//Test to see if the file is saved
			File file = getBaseContext().getFileStreamPath("pet_file.dat");
			if(file.exists()){
				Log.i(LOG_TAG1,"is saved on internal memory");
			}
			else{
				Log.i(LOG_TAG1,"is not saved on internal memory");
			}  

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Starts music player when resuming activity
	 * 
	 */
	@Override
	public void onResume() {
		super.onResume();
		player.start();
		musicCheckBox.setChecked(true);
	}

	/**
	 * Pauses the music when exiting activity
	 * 
	 */
	@Override
	protected void onStop() {
		super.onStop();
		player.stop();
		try {
			player.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stops the music and saves the pet when activity is destroyed
	 * 
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		player.stop();
		
		//Saving the pet
				try { 
					dog.save("pet_file.dat", PetActivity.this);
				} catch (IOException e) {
					e.printStackTrace();
				}
	}

	/**
	 * Method onOptionsItemSelected 
	 * 
	 * How the app navigates when clicking the backward button
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

	/**
	 * Method called to change image depending on the pet's mood and alive status.
	 * 
	 */
	private void changePicture(){

		//Sets the dead picture and animation and kills the pet if it has died
		if (!dog.isAlive()){	
			dogPicture.setImageDrawable(getResources().getDrawable(R.drawable.dogdead));
			
			//Starts the dead animation
			final Animation anim = AnimationUtils.loadAnimation(PetActivity.this, R.anim.animation1);
			dogPicture.startAnimation(anim);
			killPet();

			//Test to see if the file is deleted
			File file = getBaseContext().getFileStreamPath("pet_file.dat");

			if(file.exists()){
				Log.i(LOG_TAG2,"is still saved on internal memory");
			}
			else{
				Log.i(LOG_TAG2,"is deleted from on internal memory");
			}  

		}
		else if(petMood.getWalkMood() < 4 && petMood.getFoodMood() > 3){
			dogPicture.setImageDrawable(getResources().getDrawable(R.drawable.dogpoop));
		}
		
		else if(petMood.getSumMood() < 10){
			dogPicture.setImageDrawable(getResources().getDrawable(R.drawable.dogsad));
		}
		
		else{
			dogPicture.setImageDrawable(getResources().getDrawable(R.drawable.doghappy));
		}
	}

	/**
	 * Method called when dog dies.
	 * Sets text, disables buttons, starts deathmusic and deletes saved file.
	 * 
	 */
	private void killPet(){
		petResponse.setText(petName + " has unfortunately died!");
		showPetAge.setVisibility(View.GONE);

		//Disabling buttons
		play.setEnabled(false);
		eat.setEnabled(false);
		walk.setEnabled(false);
		sleep.setEnabled(false);
		musicCheckBox.setEnabled(false);
		
		//Stops the default background music
		player.stop();

		//Music that plays when dog dies
		try {
			deathAfd = getAssets().openFd("deathsound.m4v");
			deathplayer = new MediaPlayer();
			deathplayer.setDataSource(deathAfd.getFileDescriptor(), deathAfd.getStartOffset(), deathAfd.getLength());
			deathplayer.prepare();
			deathplayer.start();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

		//Deleting the existing saved dog
		deleteFile("pet_file.dat");
	}

}

