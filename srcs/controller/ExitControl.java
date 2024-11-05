package controller;

import model.Exit;
import model.LabState;
import model.Vector;


public class ExitControl implements Interactable{
	private Exit e;

	public ExitControl(Exit ex) {e = ex;}

	public void interact(LabState labState, Vector at) {
		if (! e.getInCell().equals(at)) return ;
		if (labState.getUncollectedKeyNum() == 0)
			e.setCollected(true);
	}
}
