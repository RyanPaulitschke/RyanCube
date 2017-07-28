package display;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Handles importing all the images that need to be drawn on screen.
 * @author Ryan Paulitschke
 */
public class Gfx {
	public static Icon button = null;
	//Button image
	public static BufferedImage butt;
	//Background
	public static BufferedImage bg;
	//Internet image
	public static BufferedImage dl;
	//Cubies
	public static BufferedImage c_yellow, c_orange, c_red, c_purple, c_blue, c_green;
	
	public static BufferedImage selected;
	public static BufferedImage r_arrow, u_arrow, l_arrow, d_arrow;
	public static BufferedImage cube_map, goal;
	
	/*
	 * Pre-loads images so they can be used later
	 */
	public static void preload() {
		//Try to load images
		try {
			butt = ImageIO.read(new File("gfx/button.png"));
			button = new ImageIcon(butt,"Submit");
			bg = ImageIO.read(new File("gfx/bg.png"));
			
			//set cubies
			c_yellow = ImageIO.read(new File("gfx/yellow.png"));
			c_orange = ImageIO.read(new File("gfx/orange.png"));
			c_red = ImageIO.read(new File("gfx/red.png"));
			c_purple = ImageIO.read(new File("gfx/purple.png"));
			c_blue = ImageIO.read(new File("gfx/blue.png"));
			c_green = ImageIO.read(new File("gfx/green.png"));
			
			selected = ImageIO.read(new File("gfx/selected.png"));
			
			r_arrow = ImageIO.read(new File("gfx/r_arrow.png"));
			u_arrow = ImageIO.read(new File("gfx/u_arrow.png"));
			l_arrow = ImageIO.read(new File("gfx/l_arrow.png"));
			d_arrow = ImageIO.read(new File("gfx/d_arrow.png"));

			cube_map = ImageIO.read(new File("gfx/cube_map.png"));
			goal = ImageIO.read(new File("gfx/goal.png"));
			
			System.out.println("Graphics were successfully preloaded");

		} catch (Exception e) { 
			//Let us know if the images failed to load.
			e.printStackTrace();
			System.out.println("Preloading graphics has failed");
		}
	}
}
