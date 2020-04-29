package com.dicycat.kroy.misc;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.dicycat.kroy.GameObject;
import com.dicycat.kroy.screens.GameScreen;

/**
* Sprite used to represent where the player is shooting
* @author IsaacAlbiston
*
*/
public class WaterStream extends GameObject{

	//ASSESSMENT 4 START
	/**
	 * initialises the water stream at a given position
	 * @param spawnPos the position to add the stream
	 * @param textureString the colour of the stream
	 */
	public WaterStream(Vector2 spawnPos, String textureString) {
			super(spawnPos, new Texture(textureString), new Vector2(1, 2));
	}
	//ASSESSMENT 4 END
	
	/**
	 * Changes the length of the sprite to x
	 * @param x the length to set it to
	 */
	public void setRange(float x){
		sprite.setScale(x,2);
	}

	// CODE_REFACTOR_4 - START OF MODIFICATION - NP STUDIOS - LUCY IVATT
	// Added override tag
	@Override
	public void update(float delta) {}
	// CODE_REFACTOR_4 - END OF MODIFICATION - NP STUDIOS - LUCY IVATT
}
