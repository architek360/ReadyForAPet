package Model;


public class Dog extends Pet {

	private String name;
	
	public Dog(String name){
		this.name = name; 
	}
	
	public Dog(){
	}

	public String getName() {
		return name;
	}
	
	public void setName1 (String name) { // denna kallas p� i create pet f�r den beh�vs i petactivitey
		this.name = name;
		System.out.println(name);
	}
	
	// ... and additional functions that only applies to dogs
	
}