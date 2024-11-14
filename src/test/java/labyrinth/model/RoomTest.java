package labyrinth.model;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class RoomTest {
    RectRoom rect;
    ConcaveRoom conc;
    Labyrinth lab;

    @BeforeEach
    void setup() {
        lab = new RectLab(20, 20, 0, null);
        rect = new RectRoom(lab, new Vector(0, 0), 10, 8);
        conc = new ConcaveRoom(lab, List.of(
            new Vector(0, 0), 
            new Vector(0, 1), 
            new Vector(1, 0),
            new Vector(1, 1),
            new Vector(1, 2),
            new Vector(2, 0),
            new Vector(2, 1),
            new Vector(2, 2)
        ));
    }

    @Test
    void gettersTest() {
        assertSame(lab, rect.getLab());
        assertSame(lab, conc.getLab());
        assertEquals(80, rect.size());
        assertEquals(8, conc.size());
    }

    @Test
    void idxInRoomTest() {
        assertTrue(rect.idxInRoom(new Vector(0,0)));
        assertTrue(rect.idxInRoom(new Vector(9,7)));
        assertFalse(rect.idxInRoom(new Vector(10,8)));
        assertTrue(conc.idxInRoom(new Vector(0,0)));
        assertTrue(conc.idxInRoom(new Vector(2,0)));
        assertFalse(conc.idxInRoom(new Vector(0,2)));   
    }

    @Test 
    void borderPolyTest() {
        assertEquals(List.of(
            new Vector(0,0),
            new Vector(9,0),
            new Vector(9,7),
            new Vector(0,7)
        ), rect.getBorderPoly());
        assertEquals(List.of(
            new Vector(0,0),
            new Vector(1,0),
            new Vector(2,0),
            new Vector(2,1),
            new Vector(2,2),
            new Vector(1,2),
            new Vector(1,1),
            new Vector(0,1)
        ), conc.getBorderPoly());
    }

}
