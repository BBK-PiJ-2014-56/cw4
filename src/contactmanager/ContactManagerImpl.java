package contactmanager;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author James Thornton
 */
public class ContactManagerImpl implements ContactManager {

    private Set<Contact> contacts;
    private List<Meeting> meetings;
    private int meetingNo;
    private ArrayList<Object> contactsAndTheirMeetings = new ArrayList();

    public ContactManagerImpl() throws IOException {
        DataIO IO = new DataIO();
        contactsAndTheirMeetings = IO.readFile();
        this.contacts = new HashSet<>();
        this.contacts = (Set<Contact>)contactsAndTheirMeetings.get(0);
        this.meetings = new LinkedList<>();
        this.meetings = (List<Meeting>)contactsAndTheirMeetings.get(1);
        this.meetingNo = meetings.size();

    }

    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        Meeting fm = new FutureMeetingImpl(meetingNo, date, contacts);
        meetings.add(fm);
        meetingNo++;
        return meetingNo;
    }

    @Override
    public PastMeeting getPastMeeting(int id) {
        Iterator meetingIterator = meetings.iterator();
        PastMeeting currentMeeting;
        while (meetingIterator.hasNext()) {
            currentMeeting = (PastMeetingImpl) meetingIterator.next();
            if (currentMeeting.getId() == id) {
                return currentMeeting;
            }
        }
        return null;
    }

    @Override
    public FutureMeeting getFutureMeeting(int id) {
        FutureMeeting m = new FutureMeetingImpl(id, getMeeting(id).getDate(), getMeeting(id).getContacts());
        if (Calendar.getInstance().compareTo(m.getDate())>0)
            try{
                return m;
            }catch (ClassCastException ex){
                ex.printStackTrace();
            }
        return null;

    }

    @Override
    public Meeting getMeeting(int id) {
        return meetings.get(id);
    }

    @Override
    public List<Meeting> getFutureMeetingList(Contact contact) {
        listSorter();
        List<Meeting> futureMeetings = new LinkedList<>();
        Iterator meetingIterator = meetings.iterator();
        Meeting currentMeeting;
        while(meetingIterator.hasNext()) {
            currentMeeting = (Meeting) meetingIterator.next();

            //date is today or in the future
            if ((currentMeeting.getDate().compareTo(Calendar.getInstance())>=0)) {
                Contact currentContact;
                Iterator contactIterator = currentMeeting.getContacts().iterator();
                while (contactIterator.hasNext()) {
                    currentContact = (Contact) contactIterator.next();
                    if (currentContact.getName().equals(contact.getName())) {
                        futureMeetings.add(currentMeeting);
                    }
                }
            }
        }
        return futureMeetings;
    }

    @Override
    public List<Meeting> getFutureMeetingList(Calendar date) {
        List<Meeting> futureMeetings = new LinkedList<>();
        Iterator meetingIterator = meetings.iterator();
        Meeting currentMeeting;
        listSorter();
        while(meetingIterator.hasNext()) {
            currentMeeting = (Meeting) meetingIterator.next();
            if (currentMeeting.getDate().equals(date)) {
                futureMeetings.add(currentMeeting);
            }
        }
        return futureMeetings;
    }

    @Override
    public List<PastMeeting> getPastMeetingList(Contact contact) {
        List<PastMeeting> pastMeetings = new LinkedList<>();
        Iterator meetingIterator = meetings.iterator();
        PastMeeting currentMeeting;
        while(meetingIterator.hasNext()) {
            currentMeeting = (PastMeeting) meetingIterator.next();
            Contact currentContact;
            Iterator contactIterator = currentMeeting.getContacts().iterator();
            while(contactIterator.hasNext()){
                currentContact = (Contact) contactIterator.next();
                if (currentContact.getName().equals(contact.getName())) {
                    pastMeetings.add(currentMeeting);
                }
            }
        }
        return pastMeetings;
    }

    @Override
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
        Meeting m = new PastMeetingImpl(meetingNo, date, contacts, text);
        meetings.add(m);
        meetingNo++;

    }

    @Override
    public void addMeetingNotes(int id, String text) {
        getPastMeeting(id).addNotes(text);
    }

    @Override
    public void addNewContact(String name, String notes) {
        Contact contact = new ContactImpl(contacts.size(), name, notes);
        contacts.add(contact);
    }

    @Override
    public Set<Contact> getContacts(int... ids) {
        Set<Contact> subsetOfContacts = new HashSet<>();
        Iterator i = contacts.iterator();
        Contact currentContact;
        while(i.hasNext()) {
            currentContact = (Contact) i.next();
            for (int id : ids) {
                if (currentContact.getId() == id) {
                    subsetOfContacts.add(currentContact);
                }
            }
        }
        return subsetOfContacts;
    }

    @Override
    public Set<Contact> getContacts(String name) {
        Set<Contact> subsetOfContacts = new HashSet<>();
        Iterator i = contacts.iterator();
        Contact currentContact;
        while(i.hasNext()) {
            currentContact = (Contact) i.next();
            if (currentContact.getName().equals(name)) {

                subsetOfContacts.add(currentContact);
            }
        }
        return subsetOfContacts;
    }

    @Override
    public void flush() throws IOException {
        //might need to delete contents of contacts.txt
        fileEraser();
        //listSorter();
        DataIO writer = new DataIO();
        writer.writeFile(contacts, meetings);
    }

    public void fileEraser() throws IOException {
        File contactsFile = new File("contacts.txt");
        contactsFile.delete();
        contactsFile.createNewFile();
    }

    /**
     * Sorts meetings into order by date
     * @return returns a list of meetings sorted by date
     */

    public List<Meeting> listSorter () {

        Collections.sort(meetings, (m1, m2) -> m1.getDate().compareTo(m2.getDate()));

        List<Meeting> sortedPastMeetings = new LinkedList<>();
        List<Meeting> sortedFutureMeetings = new LinkedList<>();
        for (Meeting meeting : meetings) {
            //if date is in the past
            if (meeting.getDate().compareTo(Calendar.getInstance()) < 0) {
                sortedPastMeetings.add(meetingNowInPast(meeting));
            } else {
                FutureMeeting fm = new FutureMeetingImpl(meeting.getId(), meeting.getDate(), meeting.getContacts());
                sortedFutureMeetings.add(fm);
            }
        }
        for (Meeting sortedFutureMeeting : sortedFutureMeetings) {
            sortedPastMeetings.add(sortedFutureMeeting);
        }
        return sortedPastMeetings;
    }
    public PastMeeting meetingNowInPast(Meeting m) {
        return new PastMeetingImpl(m.getId(), m.getDate(), m.getContacts(), "");
    }

    public List<Meeting> getMeetingList() {
        return meetings;
    }
}
