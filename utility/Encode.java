package utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import cube.RubiksCube;
import search.Searchable;

/**
 * Encoder for writing to files
 * 
 * @author Ryan Paulitschke*
 */
public class Encode {

	public static void write(LinkedList<Searchable> steps) {
		if (steps == null) {
			System.out.println("No data to save, save was cancelled");
			return;
		}
		JFrame frame = new JFrame();

		JFileChooser chooser = new JFileChooser();

		if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
			File selected = chooser.getSelectedFile();
			System.out.println("Saved data to: " + selected.getAbsolutePath());
			try (BufferedWriter writer = Files.newBufferedWriter(Paths
					.get(selected.getAbsolutePath()))) {

				for (int i=0;i<steps.size()-1;i++) {
					if (!(steps.get(i) instanceof RubiksCube))
						break;

					RubiksCube cube = (RubiksCube) steps.get(i);
					RubiksCube cubeNext = (RubiksCube) steps.get(i+1);

					writer.write("" + cube.encode() +"|"+cubeNext.encodeMove()+"\n");
					}
		

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Save cancelled");
		}

	}

}
