/*******************************************************
 * Name: EnemySwordEntity.java						   *
 * Date: Apr 9, 2013								   *
 * Author: Derek Jew								   *
 * Purpose: Represents the sword that the enemy wields *
 *******************************************************/

package pack;

import java.awt.Graphics;

public class EnemySwordEntity extends Entity {
	private double moveSpeed = 0; // vertical speed
	private boolean used = false; // true if collision
	private Game game; 			  // game in which sword exists

	/*************************************************************
	 * Constructor												 *
	 * Input: game - the game in which shot is created			 *
	 *		  r - a string with the name of the image associated *
	 *		  x, y - initial location of shot					 *
	 *************************************************************/
	public EnemySwordEntity (Game g, String r, int newX, int newY) {
		super(r, newX, newY);
		game = g;
		dy = moveSpeed;
	} // EnemySwordEntity

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
		if(other instanceof PlayerEntity) {

			// notify the game that the player was hit
			if(game.isInvincible == false){
				game.isInvincible = true;
				game.player.hp--;
				game.knockBack("sword");
				game.InvincibleTimer = System.currentTimeMillis();	
				game.stopKnockBackTimer = System.currentTimeMillis();
				game.playOther();
			} // if

			// notify the game that the player is dead
			other.swordActive = false;
			game.removeEntities.add(game.playerSword);

			used = true;
		} // if
	} // collidedWith
} // EnemySwordEntity