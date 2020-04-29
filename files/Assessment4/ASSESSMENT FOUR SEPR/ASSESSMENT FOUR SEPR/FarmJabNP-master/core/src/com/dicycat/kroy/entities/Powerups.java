//Assessment 4 START
package com.dicycat.kroy.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.dicycat.kroy.GameTextures;
import com.dicycat.kroy.Kroy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Powerups extends Entity{

    private String type;
    private Boolean exists = false;
    private float timer;
    private int respawnTimer;
    private int typeNumber = 0;
    public Powerups(Vector2 pos){
        super(pos,  new Texture("speed.png"), new Vector2(20,20), 1000000, 25);
        this.setType();
        this.setRespawnTimer();
        this.exists = true;
    }

    //Checks if has collided with the player, if so calls the powerup method for the player
    private void collision () {
        if (playerInRadius()) {
            Kroy.mainGameScreen.getPlayer().powerup(type);
            this.exists = false;
            this.setTexture(new Texture ("blank.png"));
            this.timer = 0;
        }
    }


    //Sets the type of power up
    private void setType() {
        Random r = new Random();
        typeNumber = r.nextInt(5);
        if (!exists) {
            defineType(typeNumber);
        }
    }

    //Sets stats based on the TypeNumber
    private void defineType(int typeNumber){
        switch(typeNumber){
            case 0:
                this.exists = true;
                this.type = "speed";
                break;
            case 1:
                this.exists = true;
                this.type = "damage";
                break;
            case 2:
                this.exists = true;
                this.type = "shield";
                break;
            case 3:
                this.exists = true;
                this.type = "refill";
                break;
            case 4:
                this.exists = true;
                this.type = "repair";
                break;
        }
        this.setTexture(Kroy.mainGameScreen.textures.getPowerup(typeNumber));
    }

    //Checks if the entity needs respawning
    private void checkspawn(float delta) {
        if (!this.exists){
            timer += delta;
            if (timer >= respawnTimer){
                this.respawn();
            }
        } else {
            timer = 0;
        }
    }

    //Sets the amount of time between collection and respawning
    private void setRespawnTimer(){
        Random r = new Random();
        this.respawnTimer = r.nextInt(10) + 15;
    }


    //Respawns the power up
    private void respawn() {
        this.setType();
        this.setRespawnTimer();
    }

    //Call this function
    public void update(float delta) {
        this.checkspawn(delta);
        if (this.exists) {
            this.collision();
        }
    }

    public boolean getExists(){
        return this.exists;
    }

    public void loadPowerup(ArrayList<String> saveInfo, int lineNo ) {
        exists = Boolean.parseBoolean(saveInfo.get(lineNo));
        if (exists) {
            defineType(Integer.parseInt(saveInfo.get(lineNo + 1)));
        }
    }

    public void savePowerup(File saveFile){
        try(BufferedWriter fileWriter = new BufferedWriter(new FileWriter(saveFile,true))) {
            fileWriter.write(Boolean.toString(this.exists));
            fileWriter.write("\n");
            fileWriter.write(Integer.toString(this.typeNumber));
            fileWriter.write("\n");
        } catch (IOException e) {
            Gdx.app.error("Save", "Could not access file", e);
        }
    }
}
//Assessment 4 END