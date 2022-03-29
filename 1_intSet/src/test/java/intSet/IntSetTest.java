package intSet;

/* If your IDE does not recognise annotations or assertions manually import the following */
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This is a dummy class. Feel free to delete it and generate your own using JUnit.
 * Important: make sure to fix your package directory yourself if you delete this!
 *
 * You can run the tests by clicking the green arrows next to "public class IntSetTest"
 * (once you have added at least one test)
 */
public class IntSetTest {

    /**
     * Test the constructor.
     *
     * Test 1: Capacity less than 0 should fail
     * Test 2: Check initial capacity and count
     */
    @Test
    public void testIntSet () throws Exception {

        try{
            IntSet i = new IntSet ( -10 ) ;
            fail("capacity should be >= 0");
        }
        catch(Exception e){  }

        IntSet i = new IntSet ( 10 ) ;
        assertEquals (10 , i.getCapacity (), "IntSet initial capacity") ;
        assertEquals (0 , i.getCount (), "IntSet initial count") ;
    }

    /**
     * Test function isEmpty().
     *
     * Test 1: Empty set
     * Test 2: Not empty set
     */
    @Test
    public void testIsEmpty () throws Exception {

        IntSet i = new IntSet ( 10 ) ;
        assertEquals (true , i.isEmpty (), "IntSet is empty") ;

        i.add(2);
        assertEquals (false , i.isEmpty (), "IntSet is not empty") ;
    }

    /**
     * Test function add().
     *
     * Test 1: Set count incremented
     * Test 2: Set contains added number
     * Test 3: Set contains added number
     * Test 4: Count not incremented when adding a number that is already on the set
     * Test 5: Not able to add when count == capacity
     */
    @Test
    public void testAdd () throws Exception {

        IntSet i = new IntSet ( 3 ) ;

        assertEquals (0 , i.getCount (), "IntSet correct count") ;
        i.add(2);
        assertEquals (1 , i.getCount (), "IntSet correct count") ;

        assertEquals (true , i.has (2), "IntSet contains expected value") ;

        i.add(1);
        assertEquals (true , i.has (1), "IntSet contains expected value") ;

        assertEquals (2 , i.getCount (), "IntSet has correct count") ;
        try{
            i.add(1);
            fail("Not able to add when set already contains element");
        }
        catch(Exception e){
            assertEquals (2 , i.getCount (), "IntSet has correct count") ;
        }

        i.add(3);
        try{
            i.add(4);
            fail("Not able to add when count == capacity");
        }
        catch(Exception e){  }
    }

    /**
     * Test function has().
     *
     * Test 1: Set does not have a number when empty
     * Test 2: Set has a number after adding it
     * Test 3: Has returns false when set not empty but does not have the number
     */
    @Test
    public void testHas () throws Exception {

        IntSet i = new IntSet ( 10 ) ;
        assertEquals (false , i.has (2), "IntSet is empty") ;

        i.add(2);
        assertEquals (true , i.has (2), "IntSet contains expected value") ;

        assertEquals (false , i.has (3), "IntSet does not contain a number") ;
    }

    /**
     * Test function remove().
     *
     * Test 1: Remove a value from an empty set (Nothing should happen)
     * Test 2: !has(value)
     * Test 3: count not decreased when removing a value that was not on the set
     * Test 4: Count decreased when removing an element of the set
     */
    @Test
    public void testRemove () throws Exception {

        IntSet i = new IntSet ( 3 ) ;
        try{
            i.remove(4);
        }
        catch(Exception e){
            fail("Remove implementation is wrong");
        }

        i.add(1);
        i.add(2);
        i.add(3);
        i.remove(2);
        assertEquals (false , i.has (2), "number removed") ;

        int count = i.getCount();
        i.remove(2);
        assertEquals (count , i.getCount (), "IntSet correct count") ;

        i.remove(1);
        assertEquals (count-1 , i.getCount (), "IntSet count decreased") ;
    }

    /**
     * Test function intersect().
     *
     * Test 1: Throws an exception when other == null
     * Test 2: Returns empty set when intersection is empty
     * Test 3: Returns correct intersection
     * Test 4: Count is correct
     * Test 5: Capacity is correct
     * Test 6: Intersection of an empty set should return an empty set
     */
    @Test
    public void testIntersect () throws Exception {

        IntSet i = new IntSet ( 3 ) ;
        try{
            i.intersect(null);
            fail("other != null");
        }
        catch(Exception e) { }

        i.add(1);
        i.add(2);

        IntSet other = new IntSet ( 4 ) ;
        other.add(3);
        other.add(4);

        IntSet intersection = i.intersect(other);
        assertEquals (true , intersection.isEmpty(), "Empty intersection") ;

        i.add(3);
        intersection = i.intersect(other);
        assertEquals (true , intersection.has(3), "Correct intersection") ;

        other.add(2);
        intersection = i.intersect(other);
        assertEquals (2 , intersection.getCount(), "Correct count") ;

        assertEquals (3 , intersection.getCapacity(), "Correct capacity") ;

        IntSet empty = new IntSet(0);
        intersection = empty.intersect(other);
        assertEquals (true , intersection.isEmpty(), "Empty intersection") ;
    }

    /**
     * Test function union().
     *
     * Test 1: Throws an exception when other == null
     * Test 2: Returns empty set when both sets are empty
     * Test 3: Returns correct union
     * Test 4: Returns correct union
     * Test 5: Count is correct
     * Test 6: Capacity is correct
     */
    @Test
    public void testUnion () throws Exception {

        IntSet i = new IntSet ( 3 ) ;
        try{
            i.union(null);
            fail("other != null");
        }
        catch(Exception e) { }

        IntSet other = new IntSet ( 4 ) ;

        IntSet union = i.union(other);
        assertEquals (true , union.isEmpty(), "Empty union") ;

        i.add(1);
        union = i.union(other);
        assertEquals (true , union.has(1), "Correct union") ;
        other.add(2);
        union = i.union(other);
        assertEquals (true , union.has(2), "Correct union") ;

        assertEquals (2 , union.getCount(), "Correct count") ;

        assertEquals (7 , union.getCapacity(), "Correct capacity") ;
    }

    /**
     * Test function difference().
     *
     * Test 1: Throws an exception when other == null
     * Test 2: Returns empty set when first set is empty
     * Test 3: Returns same set when no elements in common
     * Test 4: Returns correct difference
     * Test 5: Count is correct
     * Test 6: Capacity is correct
     */
    @Test
    public void testDifference () throws Exception {

        IntSet i = new IntSet ( 3 ) ;
        try{
            i.difference(null);
            fail("other != null");
        }
        catch(Exception e) { }

        IntSet other = new IntSet ( 4 ) ;
        other.add(1);
        other.add(2);

        IntSet difference = i.difference(other);
        assertEquals (true , difference.isEmpty(), "Empty difference") ;

        i.add(3);
        difference = i.difference(other);
        assertEquals (true , difference.has(3), "Correct difference") ;
        assertEquals (1 , difference.getCount(), "Correct difference") ;

        i.add(2);
        difference = i.difference(other);
        assertEquals (false , difference.has(2), "Element deleted") ;
        assertEquals (1 , difference.getCount(), "Correct difference") ;

        i.add(5);
        difference = i.difference(other);
        assertEquals (2 , difference.getCount(), "Correct count") ;

        assertEquals (3 , difference.getCapacity(), "Correct capacity") ;
    }

    /**
     * Test function symmetricDifference().
     *
     * Test 1: Throws an exception when other == null
     * Test 2: Returns empty set when both sets are empty
     * Test 3: Returns same set when second set is empty
     * Test 4: Returns correct symmetric difference
     * Test 5: Count is correct
     * Test 6: Capacity is correct
     */
    @Test
    public void testSymmetricDifference () throws Exception {

        IntSet i = new IntSet ( 3 ) ;
        try{
            i.symmetricDifference(null);
            fail("other != null");
        }
        catch(Exception e) { }

        IntSet other = new IntSet ( 4 ) ;

        IntSet symmetricDifference = i.symmetricDifference(other);
        assertEquals (true , symmetricDifference.isEmpty(), "Empty sets") ;

        i.add(1);
        i.add(2);
        symmetricDifference = i.symmetricDifference(other);
        assertEquals (2 , symmetricDifference.getCount(), "Empty second set") ;

        other.add(2);
        other.add(3);
        symmetricDifference = i.symmetricDifference(other);
        assertEquals (false , symmetricDifference.has(2), "Element deleted") ;
        assertEquals (2 , symmetricDifference.getCount(), "Correct symmetricDifference") ;

        i.add(5);
        symmetricDifference = i.symmetricDifference(other);
        assertEquals (3 , symmetricDifference.getCount(), "Correct count") ;

        assertEquals (7 , symmetricDifference.getCapacity(), "Correct capacity") ;
    }

    /**
     * Test function toString().
     *
     * Test 1: Empty set
     * Test 2: Singleton set
     * Test 3: Longer set
     */
    @Test
    public void testToString () throws Exception {

        IntSet i = new IntSet ( 10 ) ;
        assertEquals ("{}" , i.toString (), "IntSet is empty") ;

        i.add(1);
        assertEquals ("{1}" , i.toString (), "IntSet is singleton") ;

        i.add(2);
        i.add(3);
        i.add(4);
        i.add(5);
        assertEquals ("{1,2,3,4,5}" , i.toString (), "IntSet is singleton") ;

    }
}
