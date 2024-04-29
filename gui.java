import java.io.IOException;
import java.awt.*;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;

public class gui {
	public static void main(String[] args) {

		int horiz = 500;
		int vert = 500;

		JFrame obj = new JFrame();
		proj game = new proj(horiz, vert);
		game.setPreferredSize(new Dimension(horiz, vert));
		obj.setResizable(false);
		obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		obj.add(game);
		obj.pack();
		obj.setVisible(true);
	}
}
