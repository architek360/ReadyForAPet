package edu.chl.dat255.sofiase.readyforapet;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

import java.io.IOException;


import Model.Dog;
import Model.Pet;
import Model.PetMood;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PetActivity extends Activity implements Serializable{


	
	private static final long serialVersionUID = 1L;
	
	TextView petgreeting, respondingOnEat, respondingOnPlay, respondingOnWalk;
	Handler uiHandler = new Handler();

	private ProgressBar moodBar;

	private PetMood petMood = new PetMood();
	private Dog dog = (Dog) CreatePet.getPet();

	//Variables for playing music in the Pet Activity
	private MediaPlayer player;
	private AssetFileDescriptor afd;

	Runnable makeTextGone = new Runnable(){

		@Override
		public void run(){
			petgreeting.setVisibility(View.GONE);
		}
	};
	/**
	 * onCreate Method
	 *
	 * @param savedInstanceState - Bundle
	 */
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.petactivity);
		

		respondingOnEat = (TextView) findViewById(R.id.pet_response);
		respondingOnEat.setVisibility(View.GONE);

		Dog pet = (Dog) CreatePet.getPet();
		String petName = pet.getName();

		petgreeting = (TextView) findViewById(R.id.petgreeting);
		petgreeting.setText("Hello, my name is " + petName + "!");		


		petgreeting = (TextView) findViewById(R.id.petgreeting);
		uiHandler.postDelayed(makeTextGone, 5000);	

		moodBar = (ProgressBar) findViewById(R.id.moodbar);
		moodBar.setProgress(petMood.getSumMood());

		// Making the eat button
		Button eat = (Button) findViewById(R.id.eat);
		eat.setOnClickListener(new OnClickListener() {

			/**
			 * Making the dog feel less hungry if it is hungry and else give the message i'm full
			 *
			 * @param v - View
			 */
			@Override
			public void onClick (View v){

				respondingOnEat = (TextView) findViewById(R.id.pet_response);
				respondingOnEat.setText(dog.eat());
				respondingOnEat.setVisibility(View.VISIBLE);
				//uiHandler.postDelayed(makeTextGone, 5000);	

				//Updating the moodbar
				moodBar = (ProgressBar) findViewById(R.id.moodbar);
				moodBar.setProgress(petMood.getSumMood());
			}
		}
				);


		// Making the play button
		Button play = (Button) findViewById(R.id.play);
		play.setOnClickListener(new OnClickListener() {

			/**
			 * Making the dog feel happier when it plays
			 *
			 * @param v - View
			 */
			@Override
			public void onClick (View v){

				respondingOnPlay = (TextView) findViewById(R.id.pet_response);
				respondingOnPlay.setText(dog.play());
				respondingOnPlay.setVisibility(View.VISIBLE);
				//uiHandler.postDelayed(makeTextGone, 5000);

				//Updating the moodbar
				moodBar = (ProgressBar) findViewById(R.id.moodbar);
				moodBar.setProgress(petMood.getSumMood());
			}
		}
				);			


		// Making the walk button
		Button walk = (Button) findViewById(R.id.walk);
		walk.setOnClickListener(new OnClickListener() {

			/**
			 * Making the dog feel happier when it plays
			 *
			 * @param v - View
			 */
			@Override
			public void onClick (View v){

				respondingOnWalk = (TextView) findViewById(R.id.pet_response);
				respondingOnWalk.setText(dog.walk());
				respondingOnWalk.setVisibility(View.VISIBLE);
				//uiHandler.postDelayed(makeTextGone, 5000);

				// Updating the moodbar
				moodBar = (ProgressBar) findViewById(R.id.moodbar);
				moodBar.setProgress(petMood.getSumMood());
			}
		}
				);

		//Music
		try {
			afd = getAssets().openFd("readyforapetsong4.m4v");
			player = new MediaPlayer();

			player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),afd.getLength());

			player.setLooping(true);
			player.prepare();
			player.start();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method onPause for the activity
	 * 
	 * Pauses music player when pausing activity
	 */
	public void onPause() {
		super.onPause();
		player.pause();
	}
	
	/**
	 * Method onResume for the activity
	 * 
	 * Starts music player when reuming activity
	 */
	public void onResume() {
		super.onResume();
		player.start();
	}

	/**
	 * Method onStop for the activity
	 * 
	 * Stops music when exiting activity
	 */
	protected void onStop() {
		super.onStop();
		player.stop();
		player = null;
	}

	/**
	 * Making the dog feel less hungry if it is hungry and else give the message i'm full
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
