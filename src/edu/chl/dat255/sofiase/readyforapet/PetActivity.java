package edu.chl.dat255.sofiase.readyforapet;


import Model.Dog;
import Model.Pet;
import Model.PetMood;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PetActivity extends Activity {

	TextView petgreeting, respondingOnEat, respondingOnPlay, respondingOnWalk;
	Handler uiHandler = new Handler();

	private ProgressBar moodBar;
	private PetMood petMood = new PetMood();
	private Dog dog = (Dog) CreatePet.getPet();



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

		String petName = dog.getName();

		petgreeting = (TextView) findViewById(R.id.petgreeting);

		if(petName != null){
			petgreeting.setText("Hello, my name is " + petName + "!");		
		}

		else{
			petgreeting.setText("Hi buddy, I've missed you!");	
		}



		petgreeting = (TextView) findViewById(R.id.petgreeting);
		uiHandler.postDelayed(makeTextGone, 5000);	


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

				// Updating the moodbar
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

				// Updating the moodbar
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







