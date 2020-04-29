package com.dicycat.kroy.bullets;

import com.badlogic.gdx.math.Vector2;

import static com.dicycat.kroy.scenes.FireTruckSelectionScene.difficulty;

/**
 * Stores patterns of bullets to fire
 * 
 * @author Riju De
 *
 */
public class Pattern {
	private Bullet[][] bullets;	//Bullets to fire
	private float waitTime;		//Time between bullets
	private Boolean aim;		//Should the bullets be targeted towards the player
	private float cooldown; 		//Time to wait after firing pattern
	private int offset;
	private int xtra;
	private int patternDamage;

	/**
	 * Create a static directional pattern, fires in a single defined direction
	 * @param degree Direction to shoot
	 * @param speed Speed of the bullets
	 * @param range Distance each bullet travels
	 * @param timeBetweenShots Time before the next shot
	 * @param patternLength How many shots in the pattern
	 * @param multi How many bullets per shot (spread)
	 * @param cooldown Time after pattern to wait before firing the next pattern
	 */
	public Pattern(int degree, int speed, int range, float timeBetweenShots, int patternLength, int multi, float cooldown, int damage) {
		this.patternDamage = damage;
		aim = true;
		//ASSESSMENT 4 START
		//Shots are more spaced on lower difficulties
		waitTime = (float)(timeBetweenShots * 1/(difficulty * 0.5 ));
		//ASSESSMENT 4 END
		bullets = new Bullet[patternLength][multi];
		this.cooldown = cooldown;
		offset = (multi - (multi % 2)) / 2;
		xtra = (1-(multi % 2)) * 5;
		degree = 90 - degree;	//Convert normal bearings (0 is up, clockwise) to LIBGDX Vector2 degrees (0 is right, anti-clockwise)

		// CODE_REFACTOR_2 - START OF MODIFICATION - NP STUDIOS - LUCY IVATT
		// Deleted redundant code initializing this vector to 0 rather than just defining the variable.
		Vector2 direction;
		// CODE_REFACTOR_2 - END OF MODIFICATION - NP STUDIOS - LUCY IVATT
		for (int i = 0; i < patternLength; i++) {
			for (int j = 0; j < multi; j++) {
				direction = new Vector2(1, 1);
				direction.setAngle(degree + ((j - offset) * 10) + xtra);
				bullets[i][j] = new Bullet(Vector2.Zero, direction, speed, range, patternDamage); //Create bullet
			}
		}
	}
	
	/**
	 * Create an aimed pattern
	 * @param speed Speed of the bullets
	 * @param range Distance each bullet travels
	 * @param timeBetweenShots Time before the next shot
	 * @param patternLength How many shots in the pattern
	 * @param multi How many bullets per shot (spread)
	 * @param cooldown Time after pattern to wait before firing the next pattern
	 */
	public Pattern(int speed, int range, float timeBetweenShots, int patternLength, int multi, float cooldown, int damage) {
		this.patternDamage = damage;
		this.aim = true;
		//ASSESSMENT 4 START
		//Shots are more spaced on lower difficulties
		waitTime = (float)(timeBetweenShots * 1/(difficulty * 0.5));
		//ASSESSMENT 4 END
		bullets = new Bullet[patternLength][multi];
		this.cooldown = cooldown;
		offset = (multi - (multi % 2)) / 2;
		xtra = (1-(multi % 2)) * 5;

		Vector2 direction = Vector2.Zero;
		for (int i = 0; i < patternLength; i++) {
			for (int j = 0; j < multi; j++) {
				bullets[i][j] = new Bullet(Vector2.Zero, direction, speed, range, patternDamage); //Create bullet
			}
		}
	}

	/**
	 * Create a spiral pattern
	 * @param clockwise Should the spiral spin clockwise?
	 * @param startAngle Starting direction of the spiral
	 * @param rotations How many full rotations to perform
	 * @param speed Speed of the bullets
	 * @param range Distance each bullet travels
	 * @param timeBetweenShots Time before the next shot
	 * @param multi How many bullets per shot (spread)
	 * @param cooldown Time after pattern to wait before firing the next pattern
	 */

	// FORTRESS_DAMAGE_4 - START OF MODIFICATION - NP STUDIOS - CASSANDRA LILLYSTONE ----
	// Created a new attribute "patternDamage" above and set it as damage which is passed in from Fortress. Done this in all 3 constructors
	public Pattern(Boolean clockwise, int startAngle, int rotations, int speed, int range, float timeBetweenShots, int multi, float cooldown, int damage) {
		this.patternDamage = damage;
	// FORTRESS_HEALTH_4 - END OF MODIFICATION - NP STUDIOS
		aim = false;
		//ASSESSMENT 4 START
		//Shots are more spaced on lower difficulties
		waitTime = (float)(timeBetweenShots * 1/(difficulty * 0.5));
		//ASSESSMENT 4 END
		int patternLength = rotations * 36;
		bullets = new Bullet[patternLength][multi];
		this.cooldown = cooldown;
		offset = (multi - (multi % 2)) / 2;
		xtra = (1-(multi % 2)) * 5;

		int degree;	//Convert normal bearings (0 is up, clockwise) to LIBGDX Vector2 degrees (0 is right, anti-clockwise)

		Vector2 direction;
		for (int i = 0; i < patternLength; i++) {
			degree = (clockwise) ? 10 : -10;
			degree = 90 - (i*degree) + startAngle;
			for (int j = 0; j < multi; j++) {
				direction = new Vector2(1, 1);
				direction.setAngle(degree + ((j - offset) * 10) + xtra);

				// FORTRESS_DAMAGE_5 - START OF MODIFICATION - NP STUDIOS - CASSANDRA LILLYSTONE ----
				// Pass patternDamage to the bullet constructor. Done this in all 3 constructors

				bullets[i][j] = new Bullet(Vector2.Zero, direction, speed, range, patternDamage); //Create bullet

				// FORTRESS_HEALTH_5 - END OF MODIFICATION - NP STUDIOS
			}
		}
	}

	/**
	 * @param set The set of bullets to fire
	 * @return Set of bullets to fire
	 */
	Bullet[] bulletSet(int set) {
		return bullets[set];
	}

	/**
	 * Sets the direction of the bullets in the aimedSet
	 * @param set The set of bullets to fire
	 * @param aimDir The direction the bullets should fire
	 * @return Set of aimed bullets to fire
	 */
	Bullet[] aimedSet(int set, Vector2 aimDir) {
		Vector2 direction;
		for (int i = 0; i < bullets[set].length; i++) {
			direction = new Vector2(1, 1);
			direction.setAngle(aimDir.angle() + ((i - offset) * 10) + xtra);
			bullets[set][i].changeDirection(direction);
		}
		return bullets[set];
	}

	Boolean getAim() { return aim; }
	Bullet[][] getBullets(){return bullets;}
	float getWaitTime(){return waitTime;}
	public float getCooldown(){return cooldown;}
}
