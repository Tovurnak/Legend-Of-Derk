/********************************************************
 * Name: SwordEntity.java								*
 * Date: Apr 9, 2013									*
 * Author: Derek Jew									*
 * Purpose: Represents the sword that the player wields *
 ********************************************************/

package pack;

import java.awt.Graphics;

public class SwordEntity extends Entity {

	private double moveSpeed = 0; // vertical speed
	private boolean used = false; // true if collision
	private Game game; 			  // game in which sword exists

	/*************************************************************
	 * Constructor												 *
	 * Input: game - the game in which shot is created			 *
	 *		  r - a string with the name of the image associated *
	 *		  x, y - initial location of shot					 *
	 *************************************************************/
	public SwordEntity (Game g, String r, int newX, int newY) {
		super(r, newX, newY);
		game = g;
		dy = moveSpeed;
	} // SwordEntity
	
	public void aiAttack(Entity other, Graphics g) {}

	/******************************************************
	 * Name: collidedWith							  	  *
	 * Input: the entity with which the shot has collided *
	 * Purpose: notification that the shot has collided   *
	 ******************************************************/
	public void collidedWith(Entity other) {
		// prevents double kills
		if(used) {
			return;
		} // if

		// if it hits an enemy, kill the enemy
		if(other instanceof MonsterEntity || other instanceof Monster2Entity 
				|| other instanceof Monster3Entity || other instanceof JeanEntity) {
			// remove affected entities from entity list
			other.hp--;
			other.hp--;
			if(other.hp <= 10 && other instanceof JeanEntity) {
				game.jeanFiringInterval = 50;
				game.isMad = true;
			} // if
			if(other.hp <= 0) {
				game.removeEntity(other);
				game.notifyMonsterKilled();
			} // if
			
			// if the enemy was wielding a sword, kill it
			other.swordActive = false;
			game.removeEntities.add(game.enemySword);

			used = true;
		} // if
	} // collidedWith
} // SwordEntity
