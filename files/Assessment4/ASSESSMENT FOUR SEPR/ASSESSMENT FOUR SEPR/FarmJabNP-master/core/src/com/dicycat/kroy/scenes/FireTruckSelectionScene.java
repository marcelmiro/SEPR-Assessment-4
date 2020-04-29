package com.dicycat.kroy.scenes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dicycat.kroy.Kroy;

import java.awt.*;


/**
 * Window for selecting FireTruck type
 * 
 * @author Luke Taylor
 *
 */
public class FireTruckSelectionScene {

	public Stage stage;
	public Table table = new Table();
	private SpriteBatch sb;

    private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

    //ASSESSMENT 4 START
	//Added a background to this window
	private NinePatch patch = new NinePatch(new Texture("firetruckselect.jpg"), 3, 3, 3, 3);   // splits texture into nine 'patches' and gives a border of 3 pixels wide/tall on (texture,left,right,top,bottom)
	private NinePatchDrawable background = new NinePatchDrawable(patch);
	//ASSESSMENT 4 END
    
    //Buttons initialised, labelled and stylised
	// TRUCK_SELECT_CHANGE_9 - START OF MODIFICATION - NP STUDIOS - LUCY IVATT----
	// Changed truck selection buttons to labels to show the user which colour firetruck has the stat boost, rather than
	// let them pick a firetruck. Added a button to start the game.
    private Label truck1 = new Label("Speed",skin);
    private Label truck2 = new Label("Damage",skin);
    private Label truck3 = new Label("Capacity",skin);
    private Label truck4 = new Label("Range",skin);
	public TextButton startGameButton = new TextButton("Start Game", skin);

	//ASSESSMENT 4 START
	private TextButton easy = new TextButton("EASY", skin);
	private TextButton medium = new TextButton("NORMAL", skin);
	private TextButton hard = new TextButton("HARD", skin);
	ButtonGroup buttonGroup = new ButtonGroup(easy, medium, hard);
	//Allows other areas of the game to access the difficulty is normal but default
	public static int difficulty = 2;
	//ASSESSMENT 4 END

	// TRUCK_SELECT_CHANGE_9 - END OF MODIFICATION - NP STUDIOS - LUCY IVATT----
    private float width = Gdx.graphics.getWidth();
    private float centre = width* 0.7f;

	public FireTruckSelectionScene(Kroy game) {
		//ASSESSMENT 4 START
		//Ensures only one difficulty can be selected at a time
		//enforces only one button pushed
		buttonGroup.setMaxCheckCount(1);
		buttonGroup.setMinCheckCount(0);
		buttonGroup.setUncheckLast(true);
		//ASSESSMENT 4 END

		sb = game.batch;
		Viewport viewport = new ScreenViewport(new OrthographicCamera());
		stage = new Stage(viewport, sb);

		table.setBackground(background);

		//ASSESSMENT 4 START
		table.add(easy).width(centre/4.0f).colspan(4);
		table.row();
		table.add(medium).width(centre/4.0f).colspan(4);
		table.row();
		table.add(hard).width(centre/4.0f).colspan(4);
		table.row();
		//ASSESSMENT 4 END

		// Images added to the screen
		table.add(new Image(new Texture("fireTruck1.png")));
		table.add(new Image(new Texture("fireTruck2.png")));
		table.add(new Image(new Texture("fireTruck3.png")));
		table.add(new Image(new Texture("fireTruck4.png")));

		table.row();

		// TRUCK_SELECT_CHANGE_10 - START OF MODIFICATION - NP STUDIOS - LUCY IVATT----
		// Set the text align of the labels to be in the center
	    truck1.setAlignment(Align.center);
		truck2.setAlignment(Align.center);
		truck3.setAlignment(Align.center);
		truck4.setAlignment(Align.center);

		// Buttons added to the screen - New: Added padding to the bottom of each cell.
		table.add(truck1).width(centre/3.0f).pad(0,0,50,0);
	    table.add(truck2).width(centre/3.0f).pad(0,0,50,0);
	    table.add(truck3).width(centre/3.0f).pad(0,0,50,0);
	    table.add(truck4).width(centre/3.0f).pad(0,0,50,0);

	    table.row(); // Added a new row to the table

		// Added the startGameButton to the table and centered it in the table.
	    table.add(startGameButton).width(centre/2.0f).colspan(4);
		// TRUCK_SELECT_CHANGE_10 - END OF MODIFICATION - NP STUDIOS - LUCY IVATT----

		table.setFillParent(true);
	    stage.addActor(table);

	    //ASSESSMENT 4 START
		//Upon pushing a button the difficulty is set and the button stays pushed
		easy.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				difficulty = 1;
				easy.getStyle().checked = easy.getStyle().down;
			}});
		medium.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				difficulty = 2;
				medium.getStyle().checked = medium.getStyle().down;
			}});
		hard.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				difficulty = 3;
				hard.getStyle().checked = hard.getStyle().down;
			}});
		//ASSESSMENT 4 END
	}
	
	/** Allows the window to be visible or hidden
	 * @param state true if visible, false if hidden
	 */
	public void visibility(boolean state){
		this.table.setVisible(state);
	}
}
