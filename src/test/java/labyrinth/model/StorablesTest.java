package labyrinth.model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


public class StorablesTest {
    Storable st;
    Labyrinth lab;
    Vector pos;

    @BeforeEach
    void setup() {
        lab = new RectLab(20, 20, 0.2, null);
        pos = new Vector(5, 5);
        st = new Storable(lab, pos);
    }

    @Test
    void gettersTest() {
        assertEquals(lab, st.getLab());
        assertEquals(pos, st.getInCell());
        Light li = new Light(st);
        st.setLight(li);
        assertEquals(li, st.getLight());
        assertEquals(ModelSprite.DEFAULT, st.getSprite());
    }

    @Test
    void validPosTest() {
        assertFalse(st.isValidPosition(-0.45, 0)); // outside padding
        assertTrue(st.isValidPosition(-0.35, 0)); // inside padding
        assertTrue(st.isValidPosition(0, 0.45)); // outside padding but neighbour
        assertFalse(st.isValidPosition(1, 0.45)); // outside padding not neighbour
    }

    @Test
    void positionTest() {
        st.setPosition(2.1, 1.8);
        assertEquals(new Vector(2, 2), st.getInCell());
        assertEquals(2.1, st.getXPos());
        assertEquals(1.8, st.getYPos());
        st.setCell(pos);
        assertEquals(pos, st.getInCell());
        assertEquals(5, st.getXPos());
        assertEquals(5, st.getYPos());
        st.setCell(new Vector(-10, -10));
        assertEquals(pos, st.getInCell());
        assertEquals(5, st.getXPos());
        assertEquals(5, st.getYPos());
    }

    @Test
    void brazierTest() {
        Storable br = Storable.brazier(lab, pos);
        assertEquals(ModelSprite.BRAZIER, br.getSprite());
        assertNotNull(br.getLight());
    }

    @Test
    void itemsTest() {
        Item it = new Item(lab, pos);
        assertEquals(ModelSprite.DEFAULT, it.getSprite());
        it.setCollected(true);
        assertTrue(it.getCollected());
        it = new Exit(lab, pos);
        assertEquals(ModelSprite.EXIT, it.getSprite());
        it = new Key(lab, pos);
        assertEquals(ModelSprite.KEY, it.getSprite());
        it = new MapPlan(lab, pos);
        assertEquals(ModelSprite.MAP, it.getSprite());
    }

    @Test
    void playerTest() {
        PlayerCharacter pl = new PlayerCharacter(lab, pos, 0.1);
        assertEquals(0.1, pl.getStepDist());
        pl.setDir(0.22);
        assertEquals(0.22, pl.getDir());
        assertNotNull(pl.getLight());
        assertEquals(ModelSprite.CHARACTER, pl.getSprite());
    }

    @Test
    void fireflyTest() {
        Firefly f = new Firefly(lab, pos, new Vector(8, 5), 0.1);
        assertEquals(0.1, f.getStepDist());
        assertEquals(List.of(
            new Vector(6, 5),
            new Vector(7, 5),
            new Vector(8, 5)
        ), f.getRoute());
        assertNotNull(f.getLight());
        assertEquals(ModelSprite.FIREFLY, f.getSprite());
    }
    

}
