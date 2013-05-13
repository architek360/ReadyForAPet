package edu.chl.dat255.sofiase.readyforapet;

import Model.Dog;
import Model.Pet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.EditText;

public class CreatePet extends Activity {

	//public final static String EXTRA_MESSAGE = "edu.chl.dat255.sofiase.readyforapet.NAME";
	//unique if the app interacts with other apps
	String petName; 
	private static Dog dog; 


	//eftersom att hunden skapas h�r och vi inte har SPARAT spelet s� fungerar inte EAT() nr� man g�r via Continue game
	// vi m�ste l�sa s� att spelet sparas!
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		setContentView(R.layout.createpet);
		dog = new Dog(petName);

		Button create = (Button) findViewById(R.id.puppy_settings);
		create.setOnClickListener(new OnClickListener() {

			public void onClick (View v){
				startActivity(new Intent(CreatePet.this, PetActivity.class));
				EditText setName = (EditText) findViewById(R.id.edit_pet_name);
				petName = setName.getText().toString();
				dog.setName1(petName); // s� att namnet sparas tillagt av linnet och soffan
			}
		});
	}

	//public void saveSettings(View v){
	//Intent intent = new Intent(CreatePet.this, PetActivity.class);
	//EditText setName = (EditText) findViewById(R.id.edit_pet_name);
	//name = setName.getText().toString();
	//dog.setName1(name); // s� att namnet sparas tillagt av linnet och soffan
	//	startActivity(intent);
	//}


	public static Pet getPet(){
		return dog;
	}
}
