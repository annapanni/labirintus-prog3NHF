package view;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;

import controller.FileManager;
import model.LabState;

public class SimplePopup {
	Component input;
	JLabel inpLabel;
	Runnable action;
	String doneButton;
	String errorMsg;

	public static SimplePopup save(LabState lab) {
		SimplePopup s = new SimplePopup();
		JTextField tf = new JTextField(lab.getName());
		tf.setColumns(15);
		JLabel label = new JLabel("New name:");
		label.setLabelFor(tf);
		s.input = tf;
		s.inpLabel = label;
		s.doneButton = "Save";
		s.action = () -> {
			if (! tf.getText().matches("^[a-zA-Z0-9_\\-\\(\\)]*$")) {
				s.errorMsg = "Invalid input";
				return;
			}
			try {
				lab.setName(tf.getText());
				FileManager.save(lab);
			} catch (IOException e) {s.errorMsg = "File error";}
		};
		return s;
	}
	public static SimplePopup load(DisplayGraphics disp, ModePanel pan) {
		SimplePopup s = new SimplePopup();
		JComboBox jcb = new JComboBox(FileManager.getLabys());
		JLabel label = new JLabel("Choose file:");
		label.setLabelFor(jcb);
		s.input = jcb;
		s.inpLabel = label;
		s.doneButton = "Load";
		s.action = () -> {
			try {
				pan.setLabState(FileManager.load((String)jcb.getSelectedItem()));
				disp.switchTo(pan);
			} catch (IOException e) {s.errorMsg = "File error";}
			catch (ClassNotFoundException e) {s.errorMsg = "Could not find map";}
			catch (NullPointerException e) {s.errorMsg = "No such file";}

		};
		return s;
	}

	public void startPopup() {
		JFrame frame = new JFrame();
		JLabel errorLabel = new JLabel("");
		errorLabel.setForeground(Color.RED);
		JButton done = new JButton(doneButton);
		done.addActionListener((e) -> {
			action.run();
			if (errorMsg != null) {
				errorLabel.setText(errorMsg);
				errorMsg = null;
				frame.pack();
			} else {
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
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
		con.gridx = 0;
		con.gridy = 1;
		gridbag.setConstraints(errorLabel, con);
		pan.add(errorLabel);
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
