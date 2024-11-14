package labyrinth.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

class LabStateTest {
    private Labyrinth lab;
    private LabState labState;

    @BeforeEach
    void setUp() {
        lab = new RectLab(20, 15, 0, null);
        labState = new LabState(lab, 3, 5);
    }

    @Test
    void testInitialLabState() {
        assertNotNull(labState.getLab(), "Labyrinth should be initialized.");
        assertNotNull(labState.getObjects(), "Objects list should be initialized.");
        assertNotNull(labState.getItems(), "Items list should be initialized.");
        assertNotNull(labState.getPlayer(), "Player should be initialized.");
        assertNotNull(labState.getStartPos(), "Start position should be initialized.");
        assertEquals(3, labState.getItems().stream().filter(i -> i instanceof Key).count(), "There should be 3 keys in items.");
        assertEquals(1, labState.getItems().stream().filter(i -> i instanceof Exit).count(), "There should be 1 exit in items.");
        assertEquals(1, labState.getItems().stream().filter(i -> i instanceof MapPlan).count(), "There should be 1 map in items.");
        assertEquals(5, labState.getFireflyNum(), "Initial firefly count should be set correctly.");
    }

    @Test
    void testGetUncollectedKeyNum() {
        List<Item> keys = labState.getItems().stream()
                .filter(i -> i instanceof Key)
                .collect(Collectors.toList());
        keys.get(0).setCollected(true);
        
        assertEquals(2, labState.getUncollectedKeyNum(), "Uncollected key count should be 2 after collecting one key.");
    }

    @Test
    void testSetAndGetLineOfSight() {
        labState.setLineOfSight(false);
        assertNull(labState.getLineOfSight(), "Line of sight should be null when turned off.");

        labState.setLineOfSight(true);
        assertNotNull(labState.getLineOfSight(), "Line of sight should be initialized when turned on.");
    }

    @Test
    void testSetAndGetDarknessOpacity() {
        labState.setDarknessOpacity(0.5);
        assertEquals(0.5, labState.getdarknessOpacity(), 0.01, "Darkness opacity should be set to 0.5.");
    }

    @Test
    void testSetAndGetName() {
        labState.setName("Test Labyrinth");
        assertEquals("Test Labyrinth", labState.getName(), "Name should be set correctly.");
    }

    @Test
    void testSetAndGetStartPos() {
        Vector newPos = new Vector(1, 1);
        labState.setStartPos(newPos);
        assertEquals(newPos, labState.getStartPos(), "Start position should be set to new position.");
    }

    @Test
    void testSetAndGetFireflyNum() {
        labState.setFireflyNum(10);
        assertEquals(10, labState.getFireflyNum(), "Firefly number should be set correctly.");
    }

    @Test
    void testSetAndGetUsedFireflyNum() {
        labState.setUsedFireflyNum(3);
        assertEquals(3, labState.getUsedFireflyNum(), "Used firefly number should be set correctly.");
    }

    @Test
    void testSetAndGetMapCollected() {
        labState.setMapCollected(true);
        assertTrue(labState.getMapCollected(), "Map collected should be true after being set.");
    }

    @Test
    void testGetKeys() {
        long keyCount = labState.getKeys().count();
        assertEquals(3, keyCount, "There should be 3 keys in the labyrinth.");
    }

    @Test
    void testGetExits() {
        long exitCount = labState.getExits().count();
        assertEquals(1, exitCount, "There should be 1 exit in the labyrinth.");
    }

    @Test
    void testGetMaps() {
        long mapCount = labState.getMaps().count();
        assertEquals(1, mapCount, "There should be 1 map in the labyrinth.");
    }
}