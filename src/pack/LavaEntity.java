/****************************
 * Name: LavaEntity.java	*
 * Date: March 28, 2013		*
 * Author: Derek Jew  		*
 * Purpose: represents lava * 
 ****************************/

package pack;
import java.awt.Graphics;

public class LavaEntity extends Entity {

	/*************************************************************
	 * Constructor												 *
	 * Input: game - the game in which Lava is created			 *
	 *		  r - a string with the name of the image associated *
	 *		  x, y - initial location of Lava					 *
	 *************************************************************/
	public LavaEntity (Game g, String r, int newX, int newY) {
		super(r, newX, newY);
	} // ShotEntity

	/****************************************************
	 * Name: move										*
	 * Input: delta - time elapsed since last move (ms) *
	 * Purpose: to NOT move the Lava					*
	 ****************************************************/
	public void move (long delta) {
		dx = 0;
		dy = 0;
	} // move
	
	public void aiAttack(Entity other, Graphics g) {}

	/******************************************************
	 * Name: collidedWith							  	  *
	 * Input: the entity with which the Lava has collided *
	 * Purpose: notification that the Lava has collided   *
	 ******************************************************/
	public void collidedWith (Entity other){
		// will be dealt with in ShipEntity
	} // collidedWith
} // LavaEntity