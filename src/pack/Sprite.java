/**********************************
 * Name: Sprite.java			  *
 * Date: Mar 25, 2013			  *
 * Author: Derek Jew		 	  *
 * Purpose: Store image for reuse *
 **********************************/

package pack;
import java.awt.Graphics;
import java.awt.Image;

public class Sprite {
	private Image image; // the image to be drawn for this sprite

	public Sprite(Image i) {
		image = i;
	} // Sprite

	public int getWidth() {
		return image.getWidth(null);
	} // getWidth
	public int getHeight() {
		return image.getHeight(null);
	} // getHeight

	public void draw(Graphics g, int x, int y){
		g.drawImage(image, x, y, null);
	} // draw
} //Sprite
