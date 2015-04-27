package contactmanager;

import java.util.Calendar;
import java.util.Set;

/**
 * @author James Thornton
 */
public class MeetingImpl implements Meeting {

    private int id;
    private Set<Contact> contacts;
    private Calendar calendar;

    public MeetingImpl(int id, Calendar date, Set<Contact> contacts) {
        this.id = id;
        this.calendar = date;
        this.contacts = contacts;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Calendar getDate() {
        return calendar;
    }

    @Override
    public Set<Contact> getContacts() {
        return contacts;
    }
}
