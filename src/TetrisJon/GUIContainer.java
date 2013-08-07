package TetrisJon;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUIContainer extends JFrame implements ActionListener {
	JButton replace;
	GUI[] GUIs = new GUI[8];
	private static GUIContainer instance = null;
	int chosenChromo;

	private GUIContainer() {
		JPanel masterPanel = new JPanel();
		GridLayout gridLayout = new GridLayout(4, 2);
		JPanel chromos = new JPanel();
		chromos.setLayout(gridLayout);
		String[] GUINames = { "Hole", "Blockade", "Clear", "Height", "Flat",
				"Well", "Wall", "Floor" };
		for (int i = 0; i < 8; i++) {
			GUIs[i] = new GUI(GUINames[i], -100, 100);
			chromos.add(GUIs[i]);
		}
		String[] numbers = new String[19];
		for (int i = 0; i < 19; i++) {
			numbers[i] = "" + (i + 2);
		}

		// Chromonumpanel stuff
		JPanel chromoNumPanel = new JPanel();
		chromoNumPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Which chromo to replace?"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		JComboBox chromoNum = new JComboBox(numbers);
		chromoNum.setSelectedIndex(0);
		// Default chosen value
		chosenChromo = 2;
		chromoNum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				chosenChromo = Integer.parseInt((String) cb.getSelectedItem());
			}
		});
		chromoNumPanel.add(chromoNum);
		// chromoNum.setMaximumSize(chromoNum.getPreferredSize());

		// Replace button
		replace = new JButton("Replace");
		replace.addActionListener(this);

		// Add the things and display

		BoxLayout boxLayout = new BoxLayout(masterPanel, BoxLayout.PAGE_AXIS);
		masterPanel.setLayout(boxLayout);
		masterPanel.add(chromos);
		masterPanel.add(chromoNumPanel);
		masterPanel.add(replace);
		add(masterPanel);
		setSize(450, 600);
		setVisible(true);
		setTitle("Add a chromo");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setAlwaysOnTop(true);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent winEvt) {
				instance = null;
			}
		});
	}

	// Note might not work for threads? In which case change constructor back to
	// private
	public static GUIContainer getInstance() {
		if (instance == null) {
			instance = new GUIContainer();
		}
		return instance;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == replace) {
			AITester.insertedValue = chosenChromo - 1;
			// Note don't select 0 because that'll give an error
			try {
				AITester.nextGen[chosenChromo - 1] = new Chromo(
						Double.parseDouble(GUIs[0].textField.getText()),
						Double.parseDouble(GUIs[1].textField.getText()),
						Double.parseDouble(GUIs[2].textField.getText()),
						Double.parseDouble(GUIs[3].textField.getText()),
						Double.parseDouble(GUIs[4].textField.getText()),
						Double.parseDouble(GUIs[5].textField.getText()),
						Double.parseDouble(GUIs[6].textField.getText()),
						Double.parseDouble(GUIs[7].textField.getText()));
			} catch (NumberFormatException nfe) {
				System.out.println();
				System.out
						.println("Please input a number value between -100 and 100");
			}
		}
	}
}
