/*****************************
 * Name: WaterEntity.java	 *
 * Date: March 28, 2013		 *
 * Author: Derek Jew  		 *
 * Purpose: represents water * 
 *****************************/

package pack;

import java.awt.Graphics;

public class WaterEntity extends Entity {
	// it says that these aren't being used, but who knows...
	//	private double moveSpeed = 0; // it isn't moving...
	//	private Game game;			  // game in which Water exists

	/*************************************************************
	 * Constructor												 *
	 * Input: game - the game in which Water is created			 *
	 *		  r - a string with the name of the image associated *
	 *		  x, y - initial location of Water					 *
	 *************************************************************/
	public WaterEntity (Game g, String r, int newX, int newY) {
		super(r, newX, newY);
		// this too		game = g;
	} // ShotEntity

	/****************************************************
	 * Name: move										*
	 * Input: delta - time elapsed since last move (ms) *
	 * Purpose: to NOT move the Water					*
	 ****************************************************/
	public void move (long delta) {
		dx = 0;
		dy = 0;
	} // move
	
	public void aiAttack(Entity other, Graphics g) {}

	/******************************************************
	 * Name: collidedWith							  	  *
	 * Input: the entity with which the Water has collided *
	 * Purpose: notification that the Water has collided   *
	 ******************************************************/
	public void collidedWith (Entity other){
		// will be dealt with in ShipEntity
	} // collidedWith
} // WaterEntity