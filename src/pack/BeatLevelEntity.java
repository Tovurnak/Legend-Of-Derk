/*****************************************************************
 * Name: BeatLevelEntity.java		  							 *
 * Date: March 28, 2013			      							 *
 * Author: Derek Jew											 *
 * Purpose: when player walks over this, they will win the level *
 *****************************************************************/

package pack;

import java.awt.Graphics;

public class BeatLevelEntity extends Entity {
	private Game game;
	
	/****************************************************
	 * Construct a new entity							*
	 * Input: game - the game in which enemy is created *
	 * 		 r - the image representing the enemy		*
	 * 		 x , y - initial locale						*
	 ****************************************************/
	public BeatLevelEntity(Game g, String r, int newX, int newY){
		// call the constructor of superclass
		super(r, newX, newY);
		game = g;
	} // constructor
	
	public void aiAttack(Entity other, Graphics g) {}
	
	public void collidedWith(Entity other) {
		if(other instanceof PlayerEntity) {
			game.notifyVictory();
		} // if
	} // collidedWith
} // BeatLevelEntity