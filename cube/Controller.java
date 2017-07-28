package cube;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import display.Gfx;
import display.Screen;
import display.Screen.iObj;
import display.Window;
/**
 * Controller to allow user to interact with a RubixCube 
 * by making selections with WASD and rotations 
 * with the arrow keys
 * @author Ryan Paulitschke
 */
public class Controller implements ActionListener, KeyListener {
	public iObj obj; //image to control
	
	//Restricted area on screen
	public int lxb; 
	public int uxb;
	public int lyb;
	public int uyb;
	
	public Screen screen;
	
	public RubiksCube rubiks;
	public Window window;
	
	/**
	 * Controller object to allow user to control a cube
	 * @param object image object of user selection box
	 * @param lower_xbound lower horizontal value the object is bound within
	 * @param upper_xbound upper horizontal value the object is bound within
	 * @param lower_ybound lower vertical value the object is bound within
	 * @param upper_ybound upper vertical value the object is bound within
	 * @param sscreen the screen that the selection box is to be drawn in
	 * @param rubixcube the RubixCube the controller is to control
	 * @param frame the window that the controller is attached to (Window needs to be in focus for controls to work)
	 */
	public Controller(iObj object,int lower_xbound, int upper_xbound, int lower_ybound, int upper_ybound,Screen sscreen, RubiksCube rubixcube, Window frame){
		obj = object;
		lxb=lower_xbound; 
		uxb=upper_xbound;
		lyb=lower_ybound;
		uyb=upper_ybound;
		
		screen = sscreen;
		rubiks = rubixcube;
		window = frame;
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {

	}

	@Override
	public void keyReleased(KeyEvent arg0) {

		int key = arg0.getKeyCode();
		switch (key) {
		// Rotates cube
		case KeyEvent.VK_RIGHT:
			right();
			break;
		case KeyEvent.VK_UP:
			up();
			break;
		case KeyEvent.VK_LEFT:
			left();
			break;
		case KeyEvent.VK_DOWN:
			down();
			break;

		// moves selection box
		case KeyEvent.VK_W:
			if (obj.yy>lyb){
			obj.yy-=32;
			screen.revalidate();
			screen.refresh();
			}
			break;
		case KeyEvent.VK_A:
			if (obj.xx>lxb){
			obj.xx-=32;
			screen.revalidate();
			screen.refresh();
			}
			break;
		case KeyEvent.VK_S:
			if (obj.yy<uyb){
			obj.yy+=32;
			screen.revalidate();
			screen.refresh();
			}
			break;
		case KeyEvent.VK_D:
			if (obj.xx<uxb){
			obj.xx+=32;
			screen.revalidate();
			screen.refresh();
			}
			break;
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

	}

	/**
	 * Rotates the selected part of cube to the right
	 */
	public void right() {
		int x = obj.xx;
		int y = obj.yy;
		
		int xgrid=((x-8)/32)%rubiks.size;
		int ygrid=((y-8)/32)%rubiks.size;
		
		if ((y-10)<(uyb-lyb))
			rubiks.rotate(xgrid, ygrid, 2, 1);
		else
			rubiks.rotate(xgrid, ygrid, 1, 0);
		
		//redraw modified cube
		window.drawCube(rubiks);
		screen.remove(1);
		obj = screen.draw(x,y,Gfx.selected,1);
		screen.remove(3);
		screen.draw(x, y, Gfx.r_arrow, 3);
		
		screen.revalidate();
		screen.refresh();

	}

	/**
	 * Rotates the selected part of cube upwards
	 */
	public void up() {
		int x = obj.xx;
		int y = obj.yy;
		
		int xgrid=((x-8)/32)%rubiks.size;
		int ygrid=((y-8)/32)%rubiks.size;
		
		if ((y-10)<(uyb-lyb))
			rubiks.rotate(xgrid, ygrid, 0, 1);
		else
			rubiks.rotate(xgrid, ygrid, 0, 1);
		
		//redraw modified cube
		window.drawCube(rubiks);
		screen.remove(1);
		obj = screen.draw(x,y,Gfx.selected,1);
		screen.remove(3);
		screen.draw(x, y, Gfx.u_arrow, 3);
		screen.revalidate();
		screen.refresh();

	}

	/**
	 * Rotates the selected part of cube to the left
	 */
	public void left() {
		int x = obj.xx;
		int y = obj.yy;
		
		int xgrid=((x-8)/32)%rubiks.size;
		int ygrid=((y-8)/32)%rubiks.size;
		
		if ((y-10)<(uyb-lyb))
			rubiks.rotate(xgrid, ygrid, 2, 0);
		else
			rubiks.rotate(xgrid, ygrid, 1, 1);
		
		//redraw modified cube
		window.drawCube(rubiks);
		screen.remove(1);
		obj = screen.draw(x,y,Gfx.selected,1);
		screen.remove(3);
		screen.draw(x, y, Gfx.l_arrow, 3);
		screen.revalidate();
		screen.refresh();

	}

	/**
	 * Rotates the selected part of cube downwards
	 */
	public void down() {
		int x = obj.xx;
		int y = obj.yy;
		
		int xgrid=((x-8)/32)%rubiks.size;
		int ygrid=((y-8)/32)%rubiks.size;
		
		if ((y-10)<(uyb-lyb))
			rubiks.rotate(xgrid, ygrid, 0, 0);
		else
			rubiks.rotate(xgrid, ygrid, 0, 0);
		
		//redraw modified cube
		window.drawCube(rubiks);
		screen.remove(1);
		obj = screen.draw(x,y,Gfx.selected,1);
		screen.remove(3);
		screen.draw(x, y, Gfx.d_arrow, 3);
		screen.revalidate();
		screen.refresh();

	}
	
	/**
	 * Redraws the selection box above all other graphics on screen
	 */
	public void refresh(){
		screen.remove(1);
		obj = screen.draw(obj.xx,obj.yy,Gfx.selected,1);
	}

}
