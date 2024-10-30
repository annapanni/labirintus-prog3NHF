package view;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import controller.FileManager;
import model.LabState;

public class SimplePopup {
	Component input;
	JLabel inpLabel;
	Runnable action;
	String doneButton;

	public static SimplePopup save(LabState lab) {
		SimplePopup s = new SimplePopup();
		JTextField tf = new JTextField(lab.getName());
		tf.setColumns(20);
		JLabel label = new JLabel("New name:");
		label.setLabelFor(tf);
		s.input = tf;
		s.inpLabel = label;
		s.doneButton = "Save";
		s.action = () -> {
			lab.setName(tf.getText());
			FileManager.save(lab);
		};
		return s;
	}
	public static SimplePopup load(DisplayGraphics disp, EditPanel ep) {
		SimplePopup s = new SimplePopup();
		JComboBox jcb = new JComboBox(FileManager.getLabys());
		JLabel label = new JLabel("Choose file:");
		label.setLabelFor(jcb);
		s.input = jcb;
		s.inpLabel = label;
		s.doneButton = "Load";
		s.action = () -> {
			ep.setLabState(FileManager.load((String)jcb.getSelectedItem()));
			disp.toEditMode();
		};
		return s;
	}

	public void startPopup() {
		JFrame frame = new JFrame();
		JButton done = new JButton(doneButton);
		done.addActionListener((e) -> {
			action.run();
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		});
		JPanel pan = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints con = new GridBagConstraints();
		con.insets = new Insets(10, 10, 10, 10);
		pan.setLayout(gridbag);
		con.gridx = 0;
		con.gridy = 0;
		gridbag.setConstraints(inpLabel, con);
		pan.add(inpLabel);
		con.gridx = 1;
		con.gridy = 0;
		gridbag.setConstraints(input, con);
		pan.add(input);
		con.gridx = 1;
		con.gridy = 1;
		con.anchor = GridBagConstraints.EAST ;
		gridbag.setConstraints(done, con);
		pan.add(done);
		frame.add(pan);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
}
