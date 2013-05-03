package edu.chl.dat255.sofiase.readyforapet;

import android.app.Activity;
import android.content.Intent;
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
	
	TextView petgreeting, respondingOnEat;
	Dog dog;
	Handler uiHandler = new Handler();
	
	private ProgressBar moodBar;
	private PetMood petmood = new PetMood();
	
	
	Runnable makeTextGone = new Runnable(){
		@Override
		public void run(){
			petgreeting.setVisibility(View.GONE);
		}
	};


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.petactivity);
		
		
		respondingOnEat = (TextView) findViewById(R.id.not_hungry);
		respondingOnEat.setVisibility(View.GONE);
		
		// Get the pet name from the intent
		Intent nameintent = getIntent();
		String petname = nameintent.getStringExtra(CreatePet.EXTRA_MESSAGE);
		
		//Jag fick ta bort den h�r typen av textruta och g�ra en s�n annan f�r att den skulle kunna f�rsvinna
		//Det g�r s�kert med detta s�ttet ocks� men jag hittade ingenstans hur..
		
		//Create the text view
		//petgreeting = new TextView(this);
		//petgreeting.setTextSize(40);
		
		petgreeting = (TextView) findViewById(R.id.petgreeting);
		
		if(petname != null){
			petgreeting.setText("Hello, my name is " + petname + "!");		
		}
		
		else{
			petgreeting.setText("Hi buddy, I've missed you!");	
		}
		
		
		//�ven detta togs bort n�r jag bytte textruta
		//Set the pet greeting as the activity layout
		//setContentView(petgreeting);
		

		petgreeting = (TextView) findViewById(R.id.petgreeting);
		uiHandler.postDelayed(makeTextGone, 5000);	
	
		
		//Food
		// Making the eat button
		Button eat = (Button) findViewById(R.id.eat);
		eat.setOnClickListener(new OnClickListener() {
			
					
					//making the dog feel less hungry if it is hungry and else give the message i'm full for 5 sek
					@Override
					public void onClick (View v){
							PetMood petMoodInActivity = new PetMood();
							dog = (Dog) CreatePet.getPet();
							respondingOnEat = (TextView) findViewById(R.id.not_hungry);
							respondingOnEat.setText(dog.eat(petMoodInActivity));
							respondingOnEat.setVisibility(View.VISIBLE);
							//uiHandler.postDelayed(makeTextGone, 5000);	
							
							// Updating the moodbar
							moodBar = (ProgressBar) findViewById(R.id.moodbar);
							moodBar.setProgress(petmood.getSumMood());
					}
		}
		);			
			
	}

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
	

	//"Add the title string" - 75% av scrollern




