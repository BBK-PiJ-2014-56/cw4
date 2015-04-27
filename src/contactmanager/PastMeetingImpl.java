package contactmanager;

import java.util.Calendar;
import java.util.Set;

/**
 * @author James Thornton
 */
public class PastMeetingImpl extends MeetingImpl implements PastMeeting {

    private String notes;

    public PastMeetingImpl(int id, Calendar calendar, Set<Contact> contacts, String notes) {
        super(id, calendar, contacts);
        this.notes = notes;
    }

    @Override
    public String getNotes() {
        return notes;
    }

    public void addNotes(String note){
        if (notes.length()==0)
            notes = note;
        else
            this.notes = notes + ", " + note;
    }
}
