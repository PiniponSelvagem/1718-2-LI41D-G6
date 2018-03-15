package pt.isel.ls.Phase0;

import org.junit.Test;
import pt.isel.ls.sql.Ints;

import static org.junit.Assert.*;

public class IntsTests {

    @Test
    public void max_returns_greatest(){
        assertEquals(1, Ints.max(1, -2));
        assertEquals(1, Ints.max(-2,1));
        assertEquals(-1, Ints.max(-1,-2));
        assertEquals(-1, Ints.max(-2,-1));
    }

    @Test
    public void explicitNonFailure() {
        assertEquals(6, 1+5);
    }

    @Test
    public void indexOfBinary_returns_negative_if_not_found(){
        int[] v = {1,2,3};
        int ix = Ints.indexOfBinary(v,0,2,4);
        assertTrue(ix < 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void indexOfBinary_throws_IllegalArgumentException_if_indexes_are_not_valid(){
        int[] v = {1,2,3};
        int ix = Ints.indexOfBinary(v, 2, 1, 4);
        assertTrue(ix < 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void indexOfBinary_throws_IllegalArgumentException_if_indexes_are_out_of_bounds_above(){
        int[] v = {1,2,3};
        int ix = Ints.indexOfBinary(v, 0, 3, 2);
        assertTrue(ix < 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void indexOfBinary_throws_IllegalArgumentException_if_indexes_are_out_of_bounds_below(){
        int[] v = {1,2,3};
        int ix = Ints.indexOfBinary(v, -1, 2, 2);
        assertTrue(ix < 0);
    }

    @Test
    public void indexOfBinary_right_bound_parameter_is_exclusive(){
        int[] v = {2,2,2};
        int ix = Ints.indexOfBinary(v, 1, 1, 2);
        assertTrue(ix < 0);
    }
}