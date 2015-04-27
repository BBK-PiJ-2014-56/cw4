package contactmanager;

import java.util.Calendar;
import java.util.Set;

/**
 * Created by jimjohn_thornton on 14/03/15.
 */
public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting {
    public FutureMeetingImpl(int id, Calendar calendar, Set<Contact> contacts) {
        super(id, calendar, contacts);
    }
}
