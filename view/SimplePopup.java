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
	boolean cancellable = true;

	public static SimplePopup save(LabState lab) {
		SimplePopup s = new SimplePopup();
		JTextField tf = new JTextField(lab.getName());
		tf.setColumns(15);
		JLabel label = new JLabel("Save as:");
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

	public static SimplePopup message(String msg) {
		SimplePopup s = new SimplePopup();
		JLabel label = new JLabel(msg);
		s.inpLabel = label;
		s.doneButton = "Ok";
		s.action = () -> {};
		s.cancellable = false;
		return s;
	}

	public static SimplePopup ask(String msg, Runnable a) {
		SimplePopup s = new SimplePopup();
		s.inpLabel = new JLabel(msg);
		s.doneButton = "Continue";
		s.action = a;
		return s;
	}

	public static SimplePopup from(Component inp, Runnable a, String done) {
		SimplePopup s = new SimplePopup();
		s.input = inp;
		s.action = a;
		s.doneButton = done;
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
		JButton canc = new JButton("Cancel");
		canc.addActionListener((e) -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));
		JPanel pan = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints con = new GridBagConstraints();
		con.insets = new Insets(10, 10, 10, 10);
		pan.setLayout(gridbag);
		con.gridx = 0;
		con.gridy = 0;
		if (input == null) {
			con.gridwidth = 3;
		}
		if (inpLabel != null) {
			gridbag.setConstraints(inpLabel, con);
			pan.add(inpLabel);
			con.gridx = 1;
			con.gridy = 0;
			con.gridwidth = 2;
		} else {
			con.gridwidth = 3;
		}
		if (input != null) {
			gridbag.setConstraints(input, con);
			pan.add(input);
		}
		con.gridx = 0;
		con.gridy = 1;
		con.gridwidth = 1;
		if (!cancellable) {
			con.gridwidth = 2;
		}
		gridbag.setConstraints(errorLabel, con);
		pan.add(errorLabel);
		if (cancellable) {
			con.gridx = 1;
			con.gridy = 1;
			con.anchor = GridBagConstraints.EAST;
			gridbag.setConstraints(canc, con);
			pan.add(canc);
		}
		con.gridx = 2;
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
