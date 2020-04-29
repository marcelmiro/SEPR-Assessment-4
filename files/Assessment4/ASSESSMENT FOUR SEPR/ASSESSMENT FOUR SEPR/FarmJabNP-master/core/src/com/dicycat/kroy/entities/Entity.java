package com.dicycat.kroy.entities;
// JS test for using git with eclipse
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.dicycat.kroy.GameObject;
import com.dicycat.kroy.Kroy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static com.dicycat.kroy.scenes.FireTruckSelectionScene.difficulty;

/**
 * Class for interactive gameObjects
 * 
 * @author Riju De
 *
 */
public abstract class Entity extends GameObject{
	protected int healthPoints;
	protected int radius;
	protected int maxHealthPoints;

	/**
	 * @param spawnPos The position the entity will spawn at.
	 * @param img The texture of the entity.
	 * @param imSize Size of the entity. Can be used to resize large/small textures
	 * @param health Hit points of the entity
	 */
	// RANGE - START OF MODIFICATION - NP STUDIOS - LUCY IVATT ------------
	// Added radius to entity constructor to be able to change the value from the hardcoded 500.
	public Entity(Vector2 spawnPos, Texture img, Vector2 imSize,int health, int radius) {
		super(spawnPos, img, imSize);
		healthPoints = health;
		maxHealthPoints = health;
		this.radius = radius;
		// RANGE - END OF MODIFICATION - NP STUDIOS - LUCY IVATT ------------
		changePosition(spawnPos);
	}

	/**
	 * Method is called every frame (If added to the GameObjects list in GameScreen)
	 */
	@Override
	public void update(float delta){} //Called every frame

	/**
	 * Checks if the Entity still has health and is not marked for removal
	 * @return alive Is health above 0 and is not marked for removal
	 */
	public Boolean isAlive() {
		return (healthPoints > 0) && !remove;
	}

	/**
	 * Apply x amount of damage to the entity
	 * @param damage Amount of damage to inflict on the Entity
	 */
	public void applyDamage(float damage) {
		if (damage < 0){
			throw new IllegalArgumentException("applyDamage(float damage) cannot be passed a negative float");
		} else {
			healthPoints -= damage;
			if (healthPoints <= 0) {
				die();
			}
		}
	}

	/**
	 * Checks if the player is within the radius of the Entity
	 * @return playerInRadius
	 */
	Boolean playerInRadius() {
		Vector2 currentCoords = Kroy.mainGameScreen.getPlayer().getCentre(); // get current player coordinates
		return Vector2.dst(currentCoords.x, currentCoords.y, getCentre().x, getCentre().y) < radius; // returns true if distance between entity and player is less than radius of item
	}
	
	public int getHealthPoints(){
		return healthPoints;
	}

	//ASSESSMENT 4 START
	public void saveEntity(File saveFile){
		if (this instanceof Powerups){
			((Powerups) this).savePowerup(saveFile);
		} else {
			try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(saveFile, true))) {
				//Stats that are specific to a FireTruck
				if (this instanceof FireTruck) {
					fileWriter.write(Float.toString(((FireTruck) this).getCurrentWater()));
					fileWriter.write("\n");
					fileWriter.write(Float.toString(((FireTruck) this).getSpeedTimer()));
					fileWriter.write("\n");
					fileWriter.write(Float.toString(((FireTruck) this).getShieldTimer()));
					fileWriter.write("\n");
					fileWriter.write(Float.toString(((FireTruck) this).getDamageTimer()));
					fileWriter.write("\n");
				} else if (this instanceof Alien) {
					fileWriter.write(Integer.toString(((Alien) this).getCurrentWaypoint()));
					fileWriter.write("\n");
					fileWriter.write(Float.toString(((Alien) this).getMovementCountdown() + Gdx.graphics.getDeltaTime()));
					fileWriter.write("\n");
				}
				fileWriter.write(Integer.toString(healthPoints));
				fileWriter.write("\n");
				fileWriter.write(Float.toString(this.getPosition().x));
				fileWriter.write("\n");
				fileWriter.write(Float.toString(this.getPosition().y));
				fileWriter.write("\n");
			} catch (IOException e) {
				Gdx.app.error("Save", "Could not access file", e);
			}
		}
	}

	public int loadEntity(ArrayList<String> saveInfo, int lineNo) {
		if (this instanceof Powerups) {
			((Powerups) this).loadPowerup(saveInfo, lineNo);
			lineNo += 2;
		} else {
			if (this instanceof FireTruck) {
				((FireTruck) this).setCurrentWater(Float.parseFloat(saveInfo.get(lineNo)));
				lineNo++;
				((FireTruck) this).setSpeedTimer(Float.parseFloat(saveInfo.get(lineNo)));
				lineNo++;
				((FireTruck) this).setShieldTimer(Float.parseFloat(saveInfo.get(lineNo)));
				lineNo++;
				((FireTruck) this).setDamageTimer(Float.parseFloat(saveInfo.get(lineNo)));
				lineNo++;
			} else if (this instanceof Alien) {
				((Alien) this).setCurrentWaypoint(Integer.parseInt(saveInfo.get(lineNo)));
				lineNo++;
				((Alien) this).setMovementCountdown(Float.parseFloat(saveInfo.get(lineNo)));
				lineNo++;
			}
			this.healthPoints = Integer.parseInt(saveInfo.get(lineNo));
			lineNo++;
			float x = Float.parseFloat(saveInfo.get(lineNo));
			lineNo++;
			float y = Float.parseFloat(saveInfo.get(lineNo));
			lineNo++; //Ensures the next call will read from the next line onwards
			this.setPosition(new Vector2(x, y));
			if (this instanceof  Fortress) {
				((Fortress) this).applyDamage(0); //Renders the healthbar on fortresses and kills dead fortresses
			}
		}
		return (lineNo);
	}
	//ASSESSMENT 4 END

}
