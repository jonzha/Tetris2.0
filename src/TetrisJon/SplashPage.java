package TetrisJon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SplashPage extends JPanel {
	private final int width = 200;
	private final int height = 425;
	private JButton startSingle;
	private JButton startMulti;
	SinglePlayer s1;
	int single;

	public SplashPage() {
		startSingle = new JButton("Single Player");

		startMulti = new JButton("Multiplayer");
		startSingle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Execute when button is pressed
				single = 1;
				System.out.println(single);

				SinglePlayer();

			}
		});

		this.add(startSingle);
	}

	public void invisible() {
		this.setVisible(false);
	}

	public void SinglePlayer() {
		JFrame frame = new JFrame();
		// removeAll();
		s1 = new SinglePlayer();
		frame.add(s1);
		frame.setSize(200, 425);
		frame.setVisible(true);

		// setVisible(false);
		s1.start();
	}

}
