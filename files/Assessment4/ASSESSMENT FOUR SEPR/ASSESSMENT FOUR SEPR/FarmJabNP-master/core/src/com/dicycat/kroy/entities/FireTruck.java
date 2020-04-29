package com.dicycat.kroy.entities;

import java.util.ArrayList;
import java.util.HashMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dicycat.kroy.GameObject;
import com.dicycat.kroy.Kroy;
import com.dicycat.kroy.misc.StatBar;
import com.dicycat.kroy.misc.WaterStream;
import com.dicycat.kroy.scenes.FireTruckSelectionScene;
import com.dicycat.kroy.screens.GameScreen;

/**
 * GameObject controlled controlled by the player which automatically fires
 * at hostile enemies when they're within range.
 * 
 * @author Riju De
 *
 */
public class FireTruck extends Entity{
	private float speed;	//How fast the truck can move
	private float flowRate;	//How fast the truck can dispense water
	private final float maxWater; //How much water the truck can hold
	private float currentWater; //Current amount of water
	// TRUCK_SELECT_CHANGE_5- START OF MODIFICATION - NP STUDIOS - LUCY IVATT----
	private boolean selected; // Added boolean to say whether or not the truck is selected
	// TRUCK_SELECT_CHANGE_5- END OF MODIFICATION - NP STUDIOS - LUCY IVATT----

	private final Rectangle hitbox = new Rectangle(20, 45, 20, 20);

	private final HashMap<String,Integer> DIRECTIONS = new HashMap<>(); // Dictionary to store the possible directions the truck can face based on a key code created later
	private Integer direction = 0; // Direction the truck is facing

	private WaterStream water;
	private final StatBar tank;
	private final StatBar healthBar;
	private boolean firing, hasSpeed, hasDamage, hasShield; //Booleans to track what powerups the truck has active
	private float speedTimer, damageTimer, shieldTimer; //Timers to track when powerups needs to be removed
	private final float range;
	//ASSESSMENT 4 START
	private final int truckNum; //Used to keep track of the colour of the truck
	//ASSESSMENT 4 END

	public FireTruck(Vector2 spawnPos, Float[] truckStats, int truckNum) {
		super(spawnPos, Kroy.mainGameScreen.textures.getTruck(truckNum), new Vector2(25,50), 100, 500);

		this.truckNum = truckNum;

		DIRECTIONS.put("n",0);			//North Facing Direction (up arrow)
		DIRECTIONS.put("w",90);			//West Facing Direction (left arrow)
		DIRECTIONS.put("s",180);		//South Facing Direction (down arrow)
		DIRECTIONS.put("e",270);		//East Facing Direction (right arrow)

		DIRECTIONS.put("nw",45);		//up and left arrows
		DIRECTIONS.put("sw",135);		//down and left arrows
		DIRECTIONS.put("se",225);		//down and right arrows
		DIRECTIONS.put("ne",315);		//up and right arrows
		DIRECTIONS.put("",0); 			// included so that if multiple keys in the opposite direction are pressed, the truck faces north

		speed = truckStats[0]; 			// Speed value of the truck
		flowRate = truckStats[1];		// Flow rate of the truck (referred to as the damage of the truck in game)
		maxWater = truckStats[2];		// Capacity of the truck
		currentWater = truckStats[2];	// amount of water left, initialised as full in the beginning
		range = truckStats[3];			// Range of the truck

		firing = false;
		water = new WaterStream(Vector2.Zero, "lightBlue.png");

		tank = new StatBar(Vector2.Zero, "Blue.png", 3);
		Kroy.mainGameScreen.addGameObject(tank);

		healthBar= new StatBar(Vector2.Zero, "Green.png", 3);
		Kroy.mainGameScreen.addGameObject(healthBar);

		// TRUCK_SELECT_CHANGE_6 - START OF MODIFICATION - NP STUDIOS - LUCY IVATT----
		selected = false; // initially sets the truck to false
		// TRUCK_SELECT_CHANGE_6 - END OF MODIFICATION - NP STUDIOS - LUCY IVATT----
	}

	/**
	 * This method moves the truck in the direction calculated in updateDirection()
	 */
	public void moveInDirection() {

		Vector2 movement = new Vector2(1,0); // movement represents where the truck is moving to. Initially set to (1,0) as this represents a unit vector

		movement.setAngle(direction+90); // rotates the vector to whatever angle it needs to face. 90 is added in order to get the keys matching up to movement in the right direction

		float posChange = speed * Gdx.graphics.getDeltaTime();	//Sets how far the truck can move this frame in the x and y direction
		Matrix3 distance = new Matrix3().setToScaling(posChange,posChange); // Matrix to scale the final normalised vector to the correct distance

		movement.nor(); // Normalises the vector to be a unit vector
		movement.mul(distance); // Multiplies the directional vector by the correct amount to make sure the truck moves the right amount

		Vector2 newPos = new Vector2(getPosition());
		if (!isOnCollidableTile(newPos.add(movement.x,0))) { // Checks whether changing updating x direction puts truck on a collidable tile
				setPosition(newPos); // updates x direction
		}
		newPos = new Vector2(getPosition());
		if (!isOnCollidableTile(newPos.add(0,movement.y))) { // Checks whether changing updating y direction puts truck on a collidable tile
			setPosition(newPos); // updates y direction
		}

		setRotation(direction);// updates truck direction
	}

	/**
	 * Method checks if any arrow keys currently pressed and then converts them into a integer direction
	 * @return Direction to follow
	 */
	private Integer updateDirection() {
			String directionKey = "";

		//ASSESSMENT 4 START
		//Simplified direction controls checking and changed movement to WASD keys
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			directionKey = "n";
		}if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			directionKey += "s";
		}if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			directionKey += "e";
		}if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			directionKey += "w";
		}

		//ASSESSMENT 4 END

		if (directionKey.contains("ns")) {// makes sure direction doesn't change if both up and down are pressed
			directionKey = directionKey.substring(2);
		}
		if (directionKey.contains("ew")) {// makes sure direction doesn't change if both left and right are pressed
			directionKey = directionKey.substring(0, directionKey.length()-2);
		}

		return DIRECTIONS.get(directionKey);
	}

	/**
	 * Updates the direction in which the firetruck is moving in as well as rendering it, moves it and
	 * its hitbox and checks if any entity is inside its range.
	 */
	public void update(float delta){
		// TRUCK_SELECT_CHANGE_7 - START OF MODIFICATION - NP STUDIOS - LUCY IVATT----
		// Only allows the truck to move, control the camera and attack if selected
		if (selected) {
			if (Gdx.input.isKeyPressed(Input.Keys.W) ||
					Gdx.input.isKeyPressed(Input.Keys.A) ||
					Gdx.input.isKeyPressed(Input.Keys.S) ||
					Gdx.input.isKeyPressed(Input.Keys.D)) { // Runs movement code if any arrow key pressed

				direction = updateDirection(); // updates direction based on current keyboard input
				moveInDirection(); // moves in the direction previously specified
				Kroy.mainGameScreen.updateCamera(); // Updates the screen position to always have the truck roughly centre
			}
			Kroy.mainGameScreen.updateCamera(); // Updates the screen position to always have the truck roughly centre

			//player firing
			ArrayList<GameObject> inRange = entitiesInRange();		//find list of enemies in range

			if(inRange.isEmpty() || (currentWater<=0)){				//Removes the water stream if nothing is in range
				firing = false;
				water.setRemove(true);
			}else if(!firing){					//Adds the water stream if something comes into range
				if (hasDamage) {
					water = new WaterStream(Vector2.Zero, "darkBlue.png");
				} else {
					water = new WaterStream(Vector2.Zero, "lightBlue.png");
				}
				firing =true;
				Kroy.mainGameScreen.addGameObject(water);		//initialises water as a WaterStream
			}

			if (firing) {					//if the player is firing runs the PlayerFire method
				playerFire(inRange);
			}
		}
		else {
			water.setRemove(true);
		}
		// TRUCK_SELECT_CHANGE_7 - END OF MODIFICATION - NP STUDIOS - LUCY IVATT----
		
		//Move the hit box to it's new centred position according to the sprite's position.
        hitbox.setCenter(getCentre().x, getCentre().y);

        // MEMORY LEAK FIX 1 - START OF MODIFICATION - NP STUDIOS - LUCY IVATT -----------------------------------------
		// Deleted debug hitbox being drawn to the screen even if drawDebug in GameScreen == false.
		// MEMORY LEAK FIX 1 - END OF MODIFICATION  - NP STUDIOS -----------------------------------------

		//water bar update
		tank.setPosition(getCentre().add(0,20));
		tank.setBarDisplay((currentWater/maxWater)*50);

		healthBar.setPosition(getCentre().add(0,25));
		healthBar.setBarDisplay( (healthPoints*50) / maxHealthPoints);

		removePowerUps(delta);
	}
	

	/**
	 * Find and aim at the nearest target from an ArrayList of GameObjects
	 * @param targets the list of targets within the firetrucks ranged
	 */
	private void playerFire(ArrayList<GameObject> targets) {		//Method to find and aim at the nearest target from an ArrayList of Gameobjects
		GameObject currentGameObject;
		GameObject nearestEnemy=targets.get(0);				//set nearest enemy to the first gameobject

		for (int i=1;i<targets.size();i=i+1) {									//iterates through inRange to find the closest gameobject
			currentGameObject=targets.get(i);
			if(Vector2.dst(nearestEnemy.getCentre().x, nearestEnemy.getCentre().y, getCentre().x, getCentre().y)>Vector2.dst(currentGameObject.getCentre().x,currentGameObject.getCentre().y,getCentre().x,getCentre().y)) {	//checks if the current enemy is the new nearest enemy
				nearestEnemy=targets.get(i);
			}
		}

		Vector2 direction = new Vector2();
		direction.set(new Vector2(nearestEnemy.getCentre().x,nearestEnemy.getCentre().y).sub(getCentre()));		//creates a vector2 distance of the line between the firetruck and the nearest enemy
		float angle = direction.angle();												//works out the angle of the water stream

		water.setRotation(angle);									//adjusts the water sprite to the correct length, position and angle
		water.setRange(direction.len());
		water.setPosition(getCentre().add(direction.scl(0.5f)));

		((Entity) nearestEnemy).applyDamage(flowRate);			//Applies damage to the nearest enemy
		currentWater=currentWater-flowRate;						//reduces the tank by amount of water used
	}

	/**
	 * @return an array of all enemy GameObjects in range
	 */
	private ArrayList<GameObject> entitiesInRange(){
		ArrayList<GameObject> outputArray = new ArrayList<>();	//create array list to output enemies in range

		for (GameObject currentObject : Kroy.mainGameScreen.getGameObjects()) {		//iterates through all game objects
			// PATROLS_2 - START OF MODIFICATION - NP STUDIOS - LUCY IVATT ------------
			// Added a check for Aliens so that the player can also attack them.
			if ((currentObject instanceof Fortress) && (objectInRange(currentObject))
			|| (currentObject instanceof Alien) && (objectInRange(currentObject))){  	//checks if entity is in range and is an enemy
				outputArray.add(currentObject);												//adds the current entity to the output array list
			}
			// PATROLS_2 - END OF MODIFICATION - NP STUDIOS - LUCY IVATT ------------
		}

		return (outputArray);
	}

	/**
	 * Checks if the firetrucks tank is full of water.
	 * @return true if full, false if not
	 */
	boolean isFull(){
		return this.maxWater == this.currentWater;
	}

	/**
	 * Check if a game object is in range of the fire truck
	 * @param object Object to check
	 * @return Is the object within range?
	 */
	private boolean objectInRange(GameObject object) {
		return (Vector2.dst(object.getCentre().x, object.getCentre().y, getCentre().x, getCentre().y)<range);
	}

	/**
	 * Remove the FireTruck and stat bars when they are destroyed
	 *
	 * Edited by Lucy Ivatt - NP STUDIOS
	 */
	@Override
	public void die() {
		super.die();
		water.setRemove(true);
		tank.setRemove(true);
		healthBar.setRemove(true);
	}

	public Rectangle getHitbox(){
		return this.hitbox;
	}

	/**
	 * Sets the currentWater to maxWater (the maximum tank value)
	 *
	 * Added by Lucy Ivatt - NP STUDIOS
	 */
	// REPLENISH_1: OVER TIME -> INSTANT  - START OF MODIFICATION - NP STUDIOS - BETHANY GILMORE -----------------------------------------
	public void refillWater(){
		this.currentWater = this.maxWater;
	}
	// END OF MODIFICATION  - NP STUDIOS -----------------------------------------

	// REPLENISH_2: OVER TIME -> INSTANT  - START OF MODIFICATION - NP STUDIOS - LUCY IVATT -----------------------------------------

	/**
	 * Repairs the truck overtime by adding 2 to the healthPoints each game tick until it has reached maxHealth
	 *
	 * Added by Lucy Ivatt - NP STUDIOS
	 */
	// Separated refilling water and fixing truck into 2 seperate methods as refilling the truck is now linked to the minigame
	public void repairTruck() {
		if(!(healthPoints >= maxHealthPoints)){
			healthPoints += 2;
		}
	}
	// REPLENISH_2: OVER TIME -> INSTANT  - END OF MODIFICATION - NP STUDIOS - LUCY IVATT -----------------------------------------


	//ASSESSMENT 4 START
	//Simplified calculation
	/**
	 * Checks finds the tile that the coordinate is a part of and checks if that tile is solid
	 * @param pos the coordinate on the game map
	 * @return true if solid tile, otherwise false
	 *
	 * Added by Lucy Ivatt - NP STUDIOS
	 */
	private boolean isOnCollidableTile(Vector2 pos) {
		return GameScreen.gameMap.getTileTypeByLocation(0, pos.x, pos.y).isCollidable()
				|| GameScreen.gameMap.getTileTypeByLocation(0, pos.x + this.getWidth(), pos.y).isCollidable()
				|| GameScreen.gameMap.getTileTypeByLocation(0, pos.x, pos.y + this.getHeight()).isCollidable()
				|| GameScreen.gameMap.getTileTypeByLocation(0, pos.x + this.getWidth(), pos.y + this.getHeight()).isCollidable();
	}
	//ASSESSMENT 4 START

	// TRUCK_SELECT_CHANGE_8 - START OF MODIFICATION - NP STUDIOS - LUCY IVATT----
	// Added a setter for the selected boolean
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	// TRUCK_SELECT_CHANGE_8 - END OF MODIFICATION - NP STUDIOS - LUCY IVATT----


	//ASSESSMENT 4 START

	/**
	 * Applies powerup if they are not already applied, if they are then resets the timer
	 * @param type the type of powerup
	 **/
	void powerup(String type) {
		switch (type) {
			case "speed":
				this.speed += this.speed * 0.3;
				this.hasSpeed = true;
				this.speedTimer = 0;
				break;
			case "damage":
				this.flowRate += this.flowRate * 0.3;
				this.hasDamage = true;
				this.damageTimer = 0;
				break;
			case "shield":
				this.hasShield = true;
				addShield();
				this.shieldTimer = 0;
				break;
			case "refill":
				if (this.currentWater + this.maxWater * 0.3 < this.maxWater) {
					this.currentWater += this.maxWater * 0.3;
				} else {
					this.currentWater = this.maxWater;
				}
				break;
			case "repair":
				if (this.healthPoints + this.maxHealthPoints * 0.5 < this.maxHealthPoints) {
					this.healthPoints += this.maxHealthPoints * 0.5;
				} else {
					this.healthPoints = this.maxHealthPoints;
				}
				break;
		}
	}

	/**
	 * Removes powerups once they have worn off
	 * @param delta the time passed since the last frame
	 **/
	private void removePowerUps(float delta){
		if (this.hasSpeed) {
			this.speedTimer += delta;
			if (this.speedTimer > 10) {
				this.hasSpeed = false;
				this.speedTimer = 0;
				this.speed = (float)(this.speed / 1.3);
			}
		}if (this.hasDamage) {
			this.damageTimer += delta;
			if (this.damageTimer > 10) {
				this.hasDamage = false;
				this.damageTimer = 0;
				this.flowRate = (float)(this.flowRate / 1.3);
			}
		}if (this.hasShield) {
			this.shieldTimer += delta;
			if (this.shieldTimer > 7) {
				removeShield();
				this.shieldTimer = 0;
				this.hasShield = false;
			}
		}
	}

	//Added applyDamage for FireTrucks allowing them to have a shield
	/**
	 * Apply x amount of damage to the FireTruck if it does not have a shield
	 * @param damage Amount of damage to inflict on the Entity
	 **/
	@Override
	public void applyDamage(float damage) {
		if (damage < 0){
			throw new IllegalArgumentException("applyDamage(float damage) cannot be passed a negative float");
		} else if (!this.hasShield){ //Does not take damage while the Truck has a shield
			healthPoints -= damage * (FireTruckSelectionScene.difficulty * 0.5);  //Damage taken is modified based on difficulty
			if (healthPoints <= 0) {
				die();
			}
		}
	}

	//Used when saving the FireTruck
	public float getCurrentWater(){
		return this.currentWater;
	}
	public float getDamageTimer(){
		return this.damageTimer;
	}
	public float getShieldTimer(){
		return this.shieldTimer;
	}
	public float getSpeedTimer(){
		return this.speedTimer;
	}


	//Getters and Setters used for loading and saving
	public void setCurrentWater(float currentWater) {
		this.currentWater = currentWater;
	}
	public void setShieldTimer(float shieldTimer) {
		if (shieldTimer != 0) {
			addShield();
			this.shieldTimer = shieldTimer;
			this.hasShield = true;
		}
	}
	public void setDamageTimer(float shieldTimer) {
		if (shieldTimer != 0) {
			this.damageTimer = shieldTimer;
			this.hasDamage = true;
		}
	}
	public void setSpeedTimer(float shieldTimer) {
		if (shieldTimer != 0) {
			this.speedTimer = shieldTimer;
			this.hasSpeed = true;
		}
	}

	private void addShield(){
		this.setTexture(Kroy.mainGameScreen.textures.getShieldTruck(truckNum));
	}
	private void removeShield(){
		this.setTexture(Kroy.mainGameScreen.textures.getTruck(truckNum));
	}


	//ASSESSMENT 4 END
}


