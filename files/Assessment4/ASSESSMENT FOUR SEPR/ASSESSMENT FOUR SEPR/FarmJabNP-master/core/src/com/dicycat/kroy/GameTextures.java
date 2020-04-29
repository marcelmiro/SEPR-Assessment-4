package com.dicycat.kroy;

import com.badlogic.gdx.graphics.Texture;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Stores textures for classes to reference.
 * This means multiple of the same sprite use the same reference.
 * Because of this, render calls are reduced.
 * 
 * @author Riju De
 *
 */
public class GameTextures {

	// TRUCK_SELECT_CHANGE_1 - START OF MODIFICATION - NP STUDIOS - LUCY IVATT----
	// Changed truck texture to truck0, truck1, truck2, truck3 as all the different textures will be used with our
	// new selection method and defined the array list which will be used to store them
	private Texture truck0, truck1, truck2, truck3, ufo, bullet, fireStation, fireStationDead, shieldtruck0, shieldtruck1, shieldtruck2,shieldtruck3, pup0, pup1, pup2, pup3, pup4;
	private ArrayList<Texture> trucks;
	//ASSESSMENT 4 START
	private ArrayList<Texture> shieldTrucksList;
	private ArrayList<Texture> pupsList;
	//ASSESSMENT 4 END

	// TRUCK_SELECT_CHANGE_1 - END OF MODIFICATION - NP STUDIOS - LUCY IVATT----
	// NEW_FORTRESSES_1 - START OF MODIFICATION - NP STUDIOS - Alasdair Pilmore-Bedford ---------------------------
	// Added new textures add for new fortresses
	private Texture[] livingFortresses = {new Texture("cliffords tower.png"), new Texture("york minster.png"),
			new Texture("york museum.png"), new Texture ("YorkRailStationWithEvilAliens.png"),
			new Texture ("YorkHospital.png"), new Texture ("CentralHall.png")};

	private Texture[] deadFortresses = {new Texture("cliffords tower dead.png"), new Texture("york minster dead.png"),
			new Texture("york museum dead.png"), new Texture ("YorkRailStationDestoryed.png"),
			new Texture ("YorkHospitalDeaded.png"), new Texture ("CentralHallDeaded.png")};
	// NEW_FORTRESSES_1 - END OF MODIFICATION - NP STUDIOS - Alasdair Pilmore-Bedford ---------------------------

	private String[] truckAddress = {"fireTruck1.png", "fireTruck2.png", "fireTruck3.png", "fireTruck4.png"};
	//ASSESSMENT 4 START
	private String[] shieldTrucks = {"fireTruck1Shield.png","fireTruck2Shield.png","fireTruck3Shield.png","fireTruck4Shield.png"};
	private String[] powerups = {"speed.png", "damage.png", "shield.png","refill.png","repair.png"};
	//ASSESSMENT 4 END

	public GameTextures() {
		// TRUCK_SELECT_CHANGE_2 - START OF MODIFICATION - NP STUDIOS - LUCY IVATT----
		// Initialises all 4 truck texures and adds them to an array list rather than just the single texture used previously.
		truck0 = new Texture(truckAddress[0]);
		truck1 = new Texture(truckAddress[1]);
		truck2 = new Texture(truckAddress[2]);
		truck3 = new Texture(truckAddress[3]);
		trucks = new ArrayList<>(Arrays.asList(truck0, truck1, truck2, truck3));
		// TRUCK_SELECT_CHANGE_2 - END OF MODIFICATION - NP STUDIOS - LUCY IVATT----
		//ASSESSMENT 4 START
		shieldtruck0 = new Texture(shieldTrucks[0]);
		shieldtruck1 = new Texture(shieldTrucks[1]);
		shieldtruck2 = new Texture(shieldTrucks[2]);
		shieldtruck3 = new Texture(shieldTrucks[3]);
		shieldTrucksList = new ArrayList<>(Arrays.asList(shieldtruck0, shieldtruck1, shieldtruck2, shieldtruck3));
		//ASSESSMENT 4 END
		pup0 = new Texture(powerups[0]);
		pup1 = new Texture(powerups[1]);
		pup2 = new Texture(powerups[2]);
		pup3 = new Texture(powerups[3]);
		pup4 = new Texture(powerups[4]);
		pupsList = new ArrayList<>(Arrays.asList(pup0, pup1, pup2, pup3, pup4));
		ufo = new Texture("ufo.png");
		bullet = new Texture("bullet.png");
		fireStation = new Texture("FireStationTemp.png");
		fireStationDead = new Texture("FireStationTempdead.png");
	}

	// TRUCK_SELECT_CHANGE_3 - START OF MODIFICATION - NP STUDIOS - LUCY IVATT----
	// Slightly edited the getter for the truck texture as it needs to access the arraylist trucks with the index
	// of which truck was needed, whereas previously it would only access the texture of the truck the user selected
	// before the game began.
	public Texture getTruck(int truckNum) {
		return trucks.get(truckNum);
	}
	// TRUCK_SELECT_CHANGE_13 - END OF MODIFICATION - NP STUDIOS - LUCY IVATT----

	//ASSESSMENT 4 START
	public Texture getShieldTruck(int truckNum) {
		return shieldTrucksList.get(truckNum);
	}
	public Texture getPowerup(int powerupNum){ return pupsList.get(powerupNum); }
	//ASSESSMENT 4 END
	
	public Texture getUFO() {
		return ufo;
	}
	
	public Texture getBullet() {
		return bullet;
	}
	
	public Texture getFortress(int fortress) {
		return livingFortresses[fortress];
	}
	
	public Texture getDeadFortress(int fortress) {
		return deadFortresses[fortress];
	}
	
	public Texture getFireStation() {
		return fireStation;
	}
	public Texture getFireStationDead() {
		return fireStationDead;
	}
}
