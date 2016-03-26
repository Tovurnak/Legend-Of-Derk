/********************************************************************
 * Name: Entity.java												*
 * Date: Mar 25, 2013												*
 * Author: Derek Jew												*
 * Purpose: An entity is any object that appears in the game   		*
 * 			It is responsible for resolving collisions and movement *
 ********************************************************************/

package pack;
import java.awt.*;

public abstract class Entity {

	// protected allows the variable to be seen by this class,
	// any classes in the same package, and any subclasses
	protected double x; 	 // current x locale
	protected double y; 	 // fine y locale
	protected Sprite sprite; // this entity's sprite
	protected double dx; 	 // horizontal speed (px/s) + -> right
	protected double dy; 	 // vertical speed (px/s) + -> down
	protected boolean swordActive = false;
	protected int frame;
	protected long lastAnimation;
	protected byte hp;
	private Rectangle me = new Rectangle(); // bounding rectangle of this entity
	private Rectangle it = new Rectangle(); // bounding rectangle of other entities

	/*************************************************
	 * Constructor									 *
	 * Input: reference to the image for this entity *
	 * 		  initial x and y locale				 *
	 *************************************************/
	public Entity(String r, int newX, int newY){
		x = newX;
		y = newY;
		sprite = (SpriteStore.get()).getSprite(r);
	} // constructor

	/**********************************************************
	 * Name: move											  *
	 * Input: the amount of time that has passed in ms (long) *
	 * Output: none											  *
	 * Purpose: after a certain amount of time, update locale *
	 **********************************************************/
	public void move(long delta, Graphics g) {
		x += (dx * delta) / 1000;
		y += (dy * delta) / 1000;
	} // move

	// get and set methods for dx and dy
	public void setHorizontalMovement(double newDX) {
		dx = newDX;
	} // setHorizontalMovement
	public double getHorizontalMovement() {
		return dx;
	} // getHorizontalMovement
	public void setVerticalMovement(double newDY) {
		dy = newDY;
	} // setVerticalMovement
	public double getVerticalMovement() {
		return dy;
	} // getVerticalMovement

	// getand set methods for x and y
	public double getX() {
		return (int)x;
	} // getX

	public double getY() {
		return (int)y;
	} // getY

	public void setX(double newX) {
		x = newX;
	} // setX

	public void setY(double newY) {
		y = newY;
	} // setY
	
	public int getWidth() {
		return sprite.getWidth();
	} // getWidth

	// draw this entity to the graphics object provided x and y
	public void draw(Graphics g) {
		sprite.draw(g, (int)x, (int)y);
	} // draw
	public void draw(Graphics g, int f) {

	} // draw
	
	public abstract void aiAttack(Entity other, Graphics g);

	/*********************************************************
	 * Name: collidesWith									 *
	 * Input: the other entity to check collision against	 *
	 * Output: true if entities collide						 *
	 * Purpose: check if this entity collides with the other *
	 *********************************************************/
	public boolean collidesWith(Entity other) {
		me.setBounds((int)x, (int)y, sprite.getWidth(), sprite.getHeight());
		it.setBounds((int)other.getX(), (int)other.getY(),
				other.sprite.getWidth(), other.sprite.getHeight());
		return me.intersects(it);
	} // collidesWith
	/****************************************************************
	 * Name: collidedWith											*
	 * Input: the entity with which this has collided				*
	 * Purpose: notification that this entity has collided			*
	 * Note: abstract methods must be implemented by any class that *
	 *  		extends this class									*
	 ****************************************************************/
	public abstract void collidedWith(Entity other);
} // Entity