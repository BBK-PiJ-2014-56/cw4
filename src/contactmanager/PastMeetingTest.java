package contactmanager;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class PastMeetingTest {

    private Meeting m;
    private ContactManager cm;
    private Contact James;
    private Set<Contact> contacts = new HashSet<Contact>();
    private Calendar meetingDate;

    @Before
    public void setUp() throws Exception {
        James = new ContactImpl(1, "James", "Is getting old");
        contacts.add(James);
        meetingDate = new GregorianCalendar(2015, 2, 25);
        //m = new PastMeetingImpl(1, meetingDate, contacts, "");
        cm = new ContactManagerImpl();
        cm.addNewPastMeeting(contacts, meetingDate, "");
        cm.addMeetingNotes(0, "Birthday");
    }

    @Test
    public void testGetNotes() throws Exception {
        assertEquals("Birthday", cm.getPastMeeting(0).getNotes());
    }
}