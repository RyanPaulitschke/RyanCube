package display;

import display.Gfx;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import network.Classifier;
import network.NeuralNet;
import network.SBPropagation;
import search.AStar;
import search.BreadthFirst;
import search.Searchable;
import utility.Encode;
import utility.UnitTest;
import cube.Controller;
import cube.RubiksCube;

//import com.sun.xml.internal.ws.util.StringUtils;
import cube.RubiksCube;
import display.Screen.iObj;
import genetic.GeneticAlg;

/**
 * Window handles the game window that all GUI elements are inside of.
 * @author Ryan Paulitschke
 */
public class Window implements Runnable {
	public int CUBESIZE = 3; // cube size
	NeuralNet nn; //NeuralNet to be shared between windows
	
	public Screen screen = new Screen(); // Screen to draw stuff on
	public JFrame frame = new JFrame();
	public static boolean finished = false; //Finished generating cube
	
	public static LinkedList<Searchable> solution = null;
	

	@Override
	public void run() {
		// Window Properties
		createWindow(430, 100, frame, screen);

	}

	/**
	 * Creates a controller object and attaches it to a specified RubixCube & window
	 * @param rubiks cube to be controlled
	 * @param window window where controls are listened for
	 * @return the controller
	 */
	public Controller createController(RubiksCube rubiks, Window window) {
		iObj obj = screen.draw(8 + CUBESIZE * 32, 8 + CUBESIZE * 32, Gfx.selected, 1);

		Controller controller = new Controller(obj, 8 + CUBESIZE * 32,
				8 + (2 * CUBESIZE - 1) * 32, 8 + CUBESIZE * 32, 8 + (3 * CUBESIZE - 1) * 32,
				screen, rubiks, window);

		screen.revalidate();
		screen.refresh();

		return controller;
	}

	/**
	 * Creates a window of the specified width and height
	 * 
	 * @param width width of window
	 * @param height height of window
	 * @param window window to be modified
	 * @param screen screen to be attached to window
	 */
	public void createWindow(int width, int height, JFrame window, Screen screen) {

		window.setTitle("CPSC371 - RyanCube");
		window.setBackground(new Color(30, 30, 30));
		screen.draw(0, 0, Gfx.bg, 77).scale = true;
		window.setSize(new Dimension(width, height));
		screen.setLayout(null); // Enable Absolute Layout
		window.add(screen);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Displays RubixCube
	 * @param rubiks cube to be drawn
	 */
	public void drawCube(RubiksCube rubiks) {
		screen.remove(2); //Removes old cube

		screen.draw(0, 0, Gfx.bg, 77).scale = true;

		int offset = 32; // cubies size
		int xxOff = 8;
		int yyOff = 8;

		for (int xx = 0; xx < rubiks.size; xx++) {
			for (int yy = 0; yy < rubiks.size; yy++) {
				drawCubies(rubiks, 0, xx, yy, xxOff + rubiks.size * offset, yyOff
						+ 3 * rubiks.size * offset, offset);
				drawCubies(rubiks, 1, xx, yy, xxOff + rubiks.size * offset, yyOff
						+ 2 * rubiks.size * offset, offset);
				drawCubies(rubiks, 2, xx, yy, xxOff + rubiks.size * offset, yyOff
						+ rubiks.size * offset, offset);
				drawCubies(rubiks, 3, xx, yy, xxOff + rubiks.size * offset,
						yyOff, offset);
				drawCubies(rubiks, 4, xx, yy, xxOff, yyOff + 2 * rubiks.size
						* offset, offset);
				drawCubies(rubiks, 5, xx, yy, xxOff + 2 * rubiks.size * offset,
						yyOff + 2 * rubiks.size * offset, offset);

			}
		}

		screen.revalidate();
		screen.refresh();
	}


	/**
	 * draws individual cubies for the cube
	 * @param rubiks cube to be drawn
	 * @param face face of cube to be drawn
	 * @param xx x position of cubie on face
	 * @param yy y position of cubie on face
	 * @param xxOff horizontal screen offset of entire cube
	 * @param yyOff vertical screen offset of entire cube
	 * @param offset size of each cubie in pixels
	 */
	public void drawCubies(RubiksCube rubiks, int face, int xx, int yy,
			int xxOff, int yyOff, int offset) {
		switch (rubiks.cube[face][xx][yy]) {
		case 0:
			screen.draw(xxOff + xx * offset, yyOff + yy * offset, Gfx.c_yellow,
					2);
			break;
		case 1:
			screen.draw(xxOff + xx * offset, yyOff + yy * offset, Gfx.c_orange,
					2);
			break;
		case 2:
			screen.draw(xxOff + xx * offset, yyOff + yy * offset, Gfx.c_red, 2);
			break;
		case 3:
			screen.draw(xxOff + xx * offset, yyOff + yy * offset, Gfx.c_purple,
					2);
			break;
		case 4:
			screen.draw(xxOff + xx * offset, yyOff + yy * offset, Gfx.c_blue, 2);
			break;
		case 5:
			screen.draw(xxOff + xx * offset, yyOff + yy * offset, Gfx.c_green,
					2);
			break;

		}
	}


	/**
	 * Draws the menu screen with the user buttons
	 * @param frameb window to be drawn in
	 * @param screenb the panel that's inside the window
	 */
	public void drawMenu(JFrame frameb, Screen screenb) {
		screenb.draw(8, 8, Gfx.cube_map, 100);

		JComboBox<String> search_type = screenb.createDD(10, 180, "AStar",
				"Breadth First");
		screenb.add(search_type);
		JButton but_search = screenb.createButton(10, 200, "Search");
		screenb.add(but_search);

		JButton but_solve = screenb.createButton(10, 260, "Apply Solu.");
		screenb.add(but_solve);

		JComboBox<String> moves = screenb.createDD(10, 360, "1 Move",
				"2 Moves", "3 Moves", "4 Moves", "5 Moves", "6 Moves",
				"7 Moves", "8 Moves");
		screenb.add(moves);

		JButton but_scramble = screenb.createButton(10, 380, "Scramble");
		screenb.add(but_scramble);
		
		JButton but_unit = screenb.createButton(10, 440, "Unit Tests");
		screenb.add(but_unit);
		
		JButton but_save = screenb.createButton(10, 500, "Save Data");
		screenb.add(but_save);

		screenb.draw(24, 570, Gfx.goal, 98);

		screenb.drawTextCenter(30, 750, "W,A,S,D = Move");
		screenb.drawTextCenter(10, 780, "Arrow Keys = Rotate");

		but_search.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				switch (search_type.getSelectedIndex()){
				//A*
				case 0:
					System.out.println("AStar algorithm is searching possible solutions...");
					AStar as = new AStar();
					long start = System.currentTimeMillis();
					solution = as.search(Gui.rubiks,Gui.srubix);
					System.out.println(((double)(System.currentTimeMillis() - start)) * 0.001+"s");
					System.out.println("Steps to solve: "+(solution.size()-1));
					Gui.rubiks.showSolution(solution);
					break;
				
				//BFS
				case 1:
					System.out.println("BFS algorithm is searching possible solutions...");
					BreadthFirst bf = new BreadthFirst();
					long startb = System.currentTimeMillis();
					solution = bf.search(Gui.rubiks,Gui.srubix);
					System.out.println(((double)(System.currentTimeMillis() - startb)) * 0.001+"s");
					System.out.println("Steps to solve: "+(solution.size()-1));
					Gui.rubiks.showSolution(solution);
					break;
				}
			}
		});

		but_solve.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (solution==null)
					System.out.println("No solution was calculated yet");
				else
				{
					Gui.rubiks.solve(solution);
					drawCube(Gui.rubiks);
					Gui.controller.refresh();
					System.out.println("The calculated solution has been applied");
				}
				
				
					
				
			}
		});

		but_scramble.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				Gui.rubiks.shuffle(moves.getSelectedIndex()+1);
				drawCube(Gui.rubiks);
				Gui.controller.refresh();
			}
		});

		but_unit.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				
				UnitTest.testRotationsTwoX();
				UnitTest.testRotationsTwoY();
				UnitTest.testRotationsTwoZ();
				UnitTest.testRotationsThreeX();
				UnitTest.testRotationsThreeY();
				UnitTest.testRotationsThreeZ();
				UnitTest.testSizeTwoBFS();
				UnitTest.testSizeTwoAS();
				UnitTest.testSizeThreeBFS();
				UnitTest.testSizeThreeAS();
				UnitTest.testCrossOver();
				UnitTest.testMutations();
				UnitTest.testElites();
				
			}
		});
		
		but_save.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				
				Encode.write(solution);
			}
		});
	}

	/**
	 * Asks the user what size cube they
	 * want created and creates it
	 */
	public void initCubeSize() {
		// Background
		screen.draw(0, 0, Gfx.bg, 77).scale = true;

		screen.drawText(8, 56, "Cube size?: ");
		JTextField tboxA = screen.createTextBox(128, 32, 200, "3");
		screen.add(tboxA);

		JButton but = screen.createButton(256, 8, "Submit");
		screen.add(but);

		but.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				try {
					CUBESIZE = java.lang.Integer.valueOf(tboxA.getText());
				} catch (Exception ee) {
					System.out.println("Invalid Size entered, deafaulted to: ");
				}

				if (CUBESIZE < 1)
					CUBESIZE = 1;

				// CREATE CUBE
				frame.setSize(42 + 3 * CUBESIZE * 32, 64 + 4 * CUBESIZE * 32);
				screen.clear(); // removes text & images
				screen.removeAll(); // removes TextBoxes/DropDowns & Background
				
				//screen.drawText(4,110,"CLICK ME\nto bring\nwindow\ninto focus");

				// Draw Menu
				Screen screenb = new Screen();
				JFrame frameb = new JFrame();
				createWindow(180, 800, frameb, screenb);
				frameb.setLocation(0, 0);
				drawMenu(frameb, screenb);
				
				// Draw Cube
				frame.setLocation(screenb.getWidth() + 16, 0);
				screen.draw(0, 0, Gfx.bg, 77).scale = true;
				
				//Draw Training
				Screen screenc = new Screen();
				JFrame framec = new JFrame();
				createWindow(300, 300, framec, screenc);
				framec.setLocation(screenb.getWidth()+ 58 + 3 * CUBESIZE * 32, 0);
				initXOR(framec, screenc);
				
				//Draw GAlg
				Screen screend = new Screen();
				JFrame framed = new JFrame();
				createWindow(270, 300, framed, screend);
				framed.setLocation(screenb.getWidth() + 514 + 3 * CUBESIZE * 32, 0);
				initGA(framed, screend);
				
				screen.revalidate();
				screen.refresh();
				
				finished = true;
			}
		});

	}
	
	//Creates the XOR GUI window
	public void initGA(JFrame frame, Screen screen) {
		// Background
		screen.draw(0, 0, Gfx.bg, 77).scale = true;
		
		GeneticAlg ga = new GeneticAlg();
		
		int yOffset = 40;
		screen.drawText(8, 56, "Initial Population ");
		JTextField tboxA = screen.createTextBox(150, 32, 200, "100");
		screen.add(tboxA);
		
		screen.drawText(20, 56+yOffset, "Generations ");
		JTextField tboxB = screen.createTextBox(150, 32+yOffset, 200, "5000");
		screen.add(tboxB);
		
		screen.drawText(40, 56+yOffset*2, "Elite % ");
		JTextField tboxBA = screen.createTextBox(150, 32+yOffset*2, 200, "0.2");
		screen.add(tboxBA);
		
		screen.drawText(26, 56+yOffset*3, "Mutation % ");
		JTextField tboxBB = screen.createTextBox(150, 32+yOffset*3, 200, "0.4");
		screen.add(tboxBB);
		
		screen.drawText(22, 56+yOffset*4, "CrossOver % ");
		JTextField tboxC = screen.createTextBox(150, 32+yOffset*4, 200, "0.4");
		screen.add(tboxC);

		screen.drawText(16, 56+yOffset*5, "Genetic Alg: ");
		
		JButton but = screen.createButton(120, 8+yOffset*5, "Train");
		screen.add(but);

		but.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int generations=0;
				if (nn!=null){
				try {
					ga.pop = java.lang.Integer.valueOf(tboxA.getText());
					generations = java.lang.Integer.valueOf(tboxB.getText());
					ga.amt_elite = java.lang.Double.valueOf(tboxBA.getText());
					ga.amt_mutation = java.lang.Double.valueOf(tboxBB.getText());
					ga.amt_crossover = java.lang.Double.valueOf(tboxC.getText());		
				} catch (Exception ee) {
					System.out.println("Invalid parameters entered");
				}
				
				if (ga.pop<5 || ga.amt_elite < 0.1 || ga.amt_mutation<0 || ga.amt_crossover<0 || Math.abs(ga.amt_elite+ga.amt_mutation+ga.amt_crossover-1.0)>0.01){
					ga.amt_elite = 0.2;
					ga.amt_mutation = 0.4;
					ga.amt_crossover = 0.4;
					ga.pop=100;
					System.out.println("Invalid parameters entered, defaulted to: \nPop:100, Elite: 0.2, Mutation: 0.4, CrossOver: 0.4\nPop>=5, %'s must total 1.0, Elite must be >=0.1");
				}
				
				ga.GA(nn, nn.inputs,generations);
				
				}else{
					System.out.println("A network structure must be created before it can be trained");
				}
			}
		});
	}
	
	//Creates the XOR GUI window
	public void initXOR(JFrame frame, Screen screen) {
		// Background
		screen.draw(0, 0, Gfx.bg, 77).scale = true;
		
		int yOffset = 40;
		screen.drawText(8, 56, "Input Layer size?: ");
		JTextField tboxA = screen.createTextBox(170, 32, 200, "2");
		screen.add(tboxA);
		
		screen.drawText(8, 56+yOffset, "Hidden Layer size?: ");
		JTextField tboxB = screen.createTextBox(170, 32+yOffset, 200, "2");
		screen.add(tboxB);
		
		screen.drawText(8, 56+yOffset*2, "Hidden Layer2 size?: ");
		JTextField tboxBA = screen.createTextBox(170, 32+yOffset*2, 200, "0");
		screen.add(tboxBA);
		
		screen.drawText(8, 56+yOffset*3, "Hidden Layer3 size?: ");
		JTextField tboxBB = screen.createTextBox(170, 32+yOffset*3, 200, "0");
		screen.add(tboxBB);
		
		screen.drawText(8, 56+yOffset*4, "Output Layer size?: ");
		JTextField tboxC = screen.createTextBox(170, 32+yOffset*4, 200, "1");
		screen.add(tboxC);

		JButton but = screen.createButton(140, 8+yOffset*5, "Submit");
		screen.add(but);

		but.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int ils=0,hls=0,ols=0, hlbs=0, hlcs=0;
				
				try {
					ils = java.lang.Integer.valueOf(tboxA.getText());
					hls = java.lang.Integer.valueOf(tboxB.getText());
					hlbs = java.lang.Integer.valueOf(tboxBA.getText());
					hlcs = java.lang.Integer.valueOf(tboxBB.getText());
					ols = java.lang.Integer.valueOf(tboxC.getText());		
					
				} catch (Exception ee) {
					System.out.println("Invalid Network Size entered, defaulted to: 2-2-1");
				}

				if (ils < 1 || hls<1 || ols<1 || hlbs<0 || hlcs<0){
					ils = 2;
					hls = 2;
					hlbs = 0;
					hlcs = 0;
					ols = 1;
					System.out.println("Invalid Network Size entered, defaulted to: 2-2-1");
					System.out.println("Requirements: Input >0, hidden >0, hidden2 >=0, hidden3 >=0, output >0");
				}
				
				
				nn = new NeuralNet(ils,hls,hlbs,hlcs,ols);
				nn.inputs = new double[][] {{-1,-1},{-1,1},{1,-1},{1,1}};
				nn.expected = new double[][] {{-1}, {1}, {1}, {-1}};
				System.out.println("Default Training Data: XOR ...Loaded");
				SBPropagation bp = new SBPropagation();
				
				frame.setSize(456, 540);
				screen.clear(); // removes text & images
				screen.removeAll(); // removes TextBoxes/DropDowns & Background
				
				screen.drawText(150, 60, "Back Propagation");

				JButton but_import = screen.createButton(10, 10, "Import Data");
				screen.add(but_import);
				
				JButton but_run = screen.createButton(300, 10, "Train");
				screen.add(but_run);
				
				JButton but_save = screen.createButton(10, 70, "Save");
				screen.add(but_save);
				
				JButton but_load = screen.createButton(155, 70, "Load");
				screen.add(but_load);
				
				JButton but_newnn = screen.createButton(300, 70, "New NN");
				screen.add(but_newnn);
				
				int xOff = 300;
				int yOff = 50;
	
				JButton[] but_upd = new JButton[6];
				for(int i=0;i<6;i++){
				but_upd[i] = screen.createButton(xOff, 134+yOff*i, "Update");
				screen.add(but_upd[i]);
				}
				screen.drawText(20, 84+yOff*2, "Learning Rate [0, 1]");
				screen.drawText(30, 84+yOff*3, "Momentum [0, 1]");
				screen.drawText(20, 84+yOff*4, "Weight Decay [-1, 0]");
				screen.drawText(70, 84+yOff*5, "Bias");
				screen.drawText(60, 84+yOff*6, "Epochs");
				screen.drawText(50, 84+yOff*7, "Iterations");
				screen.drawText(50, 84+yOff*8, "Data Index");
				
				JButton but_classify = screen.createButton(xOff, 84+yOff*7, "Classify");
				screen.add(but_classify);
				
				//screen.drawText(8, 150, "Current Network Size: "+ils+" - "+hls+" - "+hlbs+" - "+hlcs+" - "+ols);
				
				
				JTextField[] tbox = new JTextField[7];
				
				for(int i=0;i<7;i++){
					tbox[i] = screen.createTextBox(190, 160+50*i, 200, "0");
					screen.add(tbox[i]);
					
				}
				
				but_upd[0].addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						double lrate=0;
						try {
							lrate = java.lang.Double.valueOf(tbox[0].getText());
						} catch (Exception ee) {
							System.out.println("Invalid Size entered, deafaulted to: 0");
						}
						nn.momentum=lrate;
						System.out.println("Learning rate set to: "+lrate);
					}
				});
				
				but_upd[1].addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						double val=0;
						try {
							val = java.lang.Double.valueOf(tbox[1].getText());
						} catch (Exception ee) {
							System.out.println("Invalid Size entered, deafaulted to: 0");
						}
						nn.momentum=val;
						System.out.println("Momentum set to: "+val);
					}
				});
				
				but_upd[2].addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						double val=0;
						try {
							val = java.lang.Double.valueOf(tbox[2].getText());
						} catch (Exception ee) {
							System.out.println("Invalid Size entered, deafaulted to: 0");
						}
						nn.weightdecay=val;
						System.out.println("Weight decay set to: "+val);
					}
				});
				
				but_upd[3].addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						double val=0;
						try {
							val = java.lang.Double.valueOf(tbox[3].getText());
						} catch (Exception ee) {
							System.out.println("Invalid Size entered, deafaulted to: 0");
						}
						nn.bias=val;
						System.out.println("Bias set to: "+val);
					}
				});
				
				but_upd[4].addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						int val=1;
						try {
							val = java.lang.Integer.valueOf(tbox[4].getText());
						} catch (Exception ee) {
							System.out.println("Invalid Size entered, deafaulted to: 1");
						}
						bp.epochs=val;
						System.out.println("# Epochs set to: "+val);
					}
				});
				
				but_upd[5].addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						int val=20;
						try {
							val = java.lang.Integer.valueOf(tbox[5].getText());
						} catch (Exception ee) {
							System.out.println("Invalid Size entered, deafaulted to: 20");
						}
						bp.iters=val;
						System.out.println("# Iterations set to: "+val);
					}
				});
				
				
				but_run.addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						System.out.println("SBP algorithm is training network...");
						long start = System.currentTimeMillis();
						bp.SBP(nn,nn.inputs);
						System.out.println("Training run time: "+((double)(System.currentTimeMillis() - start)) * 0.001+"s");
					}
				});

				but_load.addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						nn.loadNN();
					}
				});

				but_classify.addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						int val=0;
						try {
							val = java.lang.Integer.valueOf(tbox[6].getText());
						} catch (Exception ee) {
							System.out.println("Invalid Size entered, defaulted to: 0");
						}
						System.out.println("Classifying Data at Index: "+val);
						
						Classifier c = new Classifier();
						c.classify(nn,val);
					}
				});
				
				but_newnn.addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						frame.setSize(300, 300);
						screen.clear();
						screen.removeAll();
						initXOR(frame,screen);
					}
				});
				
				but_save.addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						nn.saveNN(false);
					}
				});
				
				but_import.addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
					nn.importData();
					}
				});
				
				screen.draw(0, 0, Gfx.bg, 77).scale = true;
				
				screen.revalidate();
				screen.refresh();
				
				finished = true;
			}
		});

	}
	

}