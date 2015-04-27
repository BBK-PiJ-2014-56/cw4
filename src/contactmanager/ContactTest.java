package contactmanager;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContactTest {

    private  Contact c;
    @Before
    public void setUp() throws Exception {
        c = new ContactImpl(1, "James", "");

    }

    @org.junit.Test
    public void testGetId() throws Exception {
        assertEquals(1, 1);
        assertEquals(1, c.getId());

    }

    @Test
    public void testGetName() throws Exception {
        assertEquals("James", c.getName());
    }

    @Test
    public void testGetNotes() throws Exception {
        assertEquals("", c.getNotes());
    }

    @Test
    public void testAddNotes() throws Exception {
        assertEquals("", c.getNotes());
        c.addNotes("loves surfing");
        assertEquals("loves surfing", c.getNotes());
    }
}