package labyrinth.controller;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Optional;

import labyrinth.model.*;

/**
 * Controller class responsible for managing the game state and player interactions in the labyrinth.
 */
public class LabGameControl {
	private LabState labState;
	private List<Mover> toMove;
	private List<Interactable> interactors;
	private CharMover playerMover;
	private int dTime;
	private double mouseX;
	private double mouseY;

	/** Toggles whether the player's position is locked or not*/
	public void switchLockedPos() {playerMover.switchLockedPos();}

	public void setLabState(LabState ls) {
		labState = ls;
		initMovers(ls.getObjects());
		initInteract(ls.getObjects());
	}

	/**
     * Constructs a new LabGameControl for a given labyrinth state and delta time.
     * @param laby the initial labyrinth state
     * @param dt the delta time for movement updates
     */
	public LabGameControl(LabState laby, int dt) {
		dTime = dt;
		setLabState(laby);
	}

	/**
     * Initializes the list of movers from the labyrinth's objects.
     * Movers are responsible for animating or updating certain objects in the game.
     * @param objs the list of objects in the labyrinth
     */
	private void initMovers(List<Storable> objs) {
		toMove = new LinkedList<>();
		for (Storable obj : objs) {
			Mover mv = Mover.create(obj);
			if (mv != null) {
				toMove.add(mv);
				if (mv instanceof CharMover) playerMover = (CharMover)mv;
			}
			if (obj.getLight() != null) {
				toMove.add(Mover.create(obj.getLight()));
			}
		}
	}

	/**
     * Initializes the list of interactable objects from the labyrinth's objects.
     * @param objs the list of objects in the labyrinth
     */
	private void initInteract(List<Storable> objs){
		interactors = new LinkedList<>();
		for (Storable obj : objs) {
			Interactable i = InteractFactory.create(obj);
			if (i != null) {
				interactors.add(i);
			}
		}
	}

	/**
     * Checks if the player has exited the labyrinth and returns the corresponding exit object.
     * @return the Exit object the player has exited through, or null if none
     */
	public Exit exitedOn() {
		Optional<Exit> exited = labState.getExits().filter(Item::getCollected).findFirst();
		if (exited.isPresent()) {
			return exited.get();
		} else {
			return null;
		}
	}

	/**
     * Triggers interactions with objects at a specified position if the player is located there.
     * @param x the x-coordinate of the interaction
     * @param y the y-coordinate of the interaction
     */
	public void interactAt(double x, double y){
		Vector vclick = labState.getLab().posToVec(x, y);
		if (! labState.getLab().inBound(vclick)) {return;}
		if (! labState.getPlayer().getInCell().equals(vclick)) {return;}
		for (Interactable i : interactors) {
			i.interact(labState, vclick);
		}
	}

	/**
     * Creates and launches a firefly towards a specific position in the labyrinth.
     * Fireflies guide the player towards keys or exits.
     * @param to the target position for the firefly
     */
	private void startFireflyTo(Vector to){
		if (labState.getFireflyNum() == labState.getUsedFireflyNum()) return;
		Firefly fly = new Firefly(labState.getLab(), labState.getPlayer().getInCell(), to, 0.003);
		labState.getObjects().add(fly);
		toMove.add(Mover.create(fly));
		toMove.add(Mover.create(fly.getLight()));
		labState.setUsedFireflyNum(labState.getUsedFireflyNum() + 1);
	}

	/**
     * Starts a firefly to guide the player, prioritizing uncollected keys.
     * If no uncollected keys are available, the firefly targets the nearest exit.
     */
	public void startFirefly(){
		Optional<Key> optK = labState.getKeys().filter(k -> !k.getCollected()).findFirst();
		if (optK.isPresent()) {
			startFireflyTo(optK.get().getInCell());
			return;
		}
		startFireflyTo(labState.getExits().findFirst().get().getInCell());
	}

	/**
     * Updates the mouse position, used to determine the player's movement direction.
     * @param x the x-coordinate of the mouse
     * @param y the y-coordinate of the mouse
     */
 	public void setMousePos(double x, double y) {
		mouseX = x;
		mouseY = y;
	}

	/**
     * Advances the game state by a single step. Updates the player's direction based on the mouse position
     * and moves all active movers.
     */
	public void step(){
		double dx = (mouseX - labState.getPlayer().getXPos()) / 10;
		double dy = (mouseY - labState.getPlayer().getYPos()) / 10;
		double fi = Math.atan2(dy, dx);
		labState.getPlayer().setDir(fi);
		Iterator<Mover> it = toMove.iterator();
		while (it.hasNext()) {
			Mover m = it.next();
			m.step(dTime);
		}
	}
}
