package contactmanager;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * This Test class unit tests...
 *
 * @author James Thornton
 */

public class CMTest {
    private Meeting pub;
    private Meeting home;
    private Meeting skiing;
    private Contact James;
    private Contact Julie;
    private Contact John;
    private Contact Jess;

    private Set<Contact> contacts = new HashSet<>();
    private Calendar futureMeetingDate = new GregorianCalendar(2015, 6, 1);
    private Calendar pastMeetingDate = new GregorianCalendar(2015, 2, 1);
    private Set<Contact> pubContacts = new HashSet<>();
    private Set<Contact> homeContacts = new HashSet<>();
    private Set<Contact> skiingContacts = new HashSet<>();
    private ContactManagerImpl cm;

    @Before
    public void setUp() throws Exception {

        cm = new ContactManagerImpl();
        cm.fileEraser();

        James = new ContactImpl(0, "James", "is the first contact");
        Julie = new ContactImpl(1, "Julie", "is the second contact");
        John = new ContactImpl(2, "John", "is the third contact");
        Jess = new ContactImpl(3, "Jess", "is a cat");


        pubContacts.add(James);
        pubContacts.add(Julie);
        pubContacts.add(John);

        //homeContacts.addAll(contacts);
        homeContacts.add(James);
        homeContacts.add(Julie);
        homeContacts.add(John);
        homeContacts.add(Jess);

        skiingContacts.add(James);
        skiingContacts.add(Julie);

        pub = new PastMeetingImpl(0, pastMeetingDate, pubContacts, "");
        skiing = new PastMeetingImpl(1, pastMeetingDate, skiingContacts, "Awesome holiday!");
        home = new FutureMeetingImpl(2, futureMeetingDate, homeContacts);

        //cm.flush();

    }
    @Test
    public void testAddFutureMeeting() throws IOException {
        //cm.fileEraser();
        //assertEquals(null, cm.getFutureMeetingList(James));
        cm.addFutureMeeting(homeContacts, futureMeetingDate);
        assertEquals(homeContacts, cm.getMeeting(0).getContacts());
    }

    @Test
    public void testGetPastMeeting() {
        cm.addNewPastMeeting(pubContacts, pastMeetingDate, "food");
        cm.addNewPastMeeting(homeContacts, pastMeetingDate, "hung out");
        cm.addNewPastMeeting(skiingContacts, pastMeetingDate, "Morzine");
        //cm.addMeetingNotes(1, "hung out");
        assertEquals("food", cm.getPastMeeting(0).getNotes());
        assertEquals("hung out", cm.getPastMeeting(1).getNotes());
        assertEquals("Morzine", cm.getPastMeeting(2).getNotes());
    }

    @Test
    public void testGetFutureMeeting() {
        cm.addFutureMeeting(pubContacts, futureMeetingDate); // , "");
        cm.addFutureMeeting(homeContacts, futureMeetingDate); //, "hung out");
        cm.addFutureMeeting(skiingContacts, futureMeetingDate); //, "went boarding");
        assertEquals(2, cm.getFutureMeeting(2).getId());
    }

    @Test
    public void testGetMeeting() {
        cm.addNewPastMeeting(pubContacts, pastMeetingDate, "");
        cm.addFutureMeeting(homeContacts, futureMeetingDate); //, "hung out");
        cm.addFutureMeeting(skiingContacts, futureMeetingDate); //, "went boarding");
        assertEquals(pubContacts, cm.getMeeting(0).getContacts());
        assertEquals(homeContacts, cm.getMeeting(1).getContacts());
        assertEquals(skiingContacts, cm.getMeeting(2).getContacts());
    }

    @Test
    public void testGetFutureMeetingList() {
        cm.addNewPastMeeting(pubContacts, pastMeetingDate, "happened in the past");
        cm.addFutureMeeting(pubContacts, futureMeetingDate);
        cm.addFutureMeeting(homeContacts, futureMeetingDate);
        cm.addFutureMeeting(skiingContacts, futureMeetingDate);
        assertEquals(3, cm.getFutureMeetingList(James).size());
        assertEquals(3, cm.getFutureMeetingList(futureMeetingDate).size());

        Calendar futureMeetingDate2 = new GregorianCalendar(2015, 6, 2);
        Calendar pastMeetingDate2 = new GregorianCalendar(2015, 2, 2);
        cm.addFutureMeeting(pubContacts, futureMeetingDate2);
        cm.addFutureMeeting(skiingContacts, futureMeetingDate);
        cm.addNewPastMeeting(homeContacts, pastMeetingDate, "meetingdate1");
        cm.addNewPastMeeting(pubContacts, pastMeetingDate2, "meetingdate2");
        cm.addNewPastMeeting(skiingContacts, pastMeetingDate, "meetingdate1");

        assertEquals(futureMeetingDate, cm.getFutureMeetingList(James).get(0).getDate());
        assertEquals(futureMeetingDate, cm.getFutureMeetingList(James).get(3).getDate());
        assertEquals(futureMeetingDate2, cm.getFutureMeetingList(James).get(4).getDate());
        //assertEquals(3, cm.getFutureMeetingList(futureMeetingDate).size());

    }

    @Test
    public void testGetPastMeetingList() {

        cm.addNewPastMeeting(pubContacts, pastMeetingDate, "");
        cm.addNewPastMeeting(homeContacts, pastMeetingDate, "hung out");
        cm.addNewPastMeeting(skiingContacts, pastMeetingDate, "");
        assertEquals(3, cm.getPastMeetingList(James).size());
        assertEquals(1, cm.getPastMeetingList(Jess).size());
    }

    @Test
    public void testAddNewPastMeeting() {
        cm.addNewPastMeeting(skiingContacts, pastMeetingDate, "a previous holiday");
        assertEquals("a previous holiday", cm.getPastMeeting(0).getNotes());
    }

    @Test
    public void testAddMeetingNotes() throws IOException {
        cm.fileEraser();
        cm.addNewPastMeeting(pubContacts, pastMeetingDate, "");
        cm.addMeetingNotes(0, "a good beer");
        assertEquals("a good beer", cm.getPastMeeting(0).getNotes());

    }

    @Test
    public void testAddNewContact() {
        assertEquals(0, cm.getContacts("Pauline").size());
        cm.addNewContact("Pauline", "");
        assertEquals(1, cm.getContacts("Pauline").size());
        //Contact Pauline = new ContactImpl(1, "Pauline", "");
        //assertEquals(Pauline.getName() , cm.getContacts("Pauline"));
    }

    @Test
    public void testGetContacts() throws IOException {
        cm.fileEraser();
        ContactManagerImpl cm2 = new ContactManagerImpl();
        cm2.addNewContact("James", "");
        cm2.addNewContact("Julie", "");
        cm2.addNewContact("John", "");
        cm2.addNewContact("Jess", "");
        cm2.addNewContact("James", "");
        cm2.addNewContact("James2", "");
        assertEquals(2, cm2.getContacts("James").size());
        assertEquals(3, cm2.getContacts(0, 2, 3).size());
    }

    @Test
    public void testListSorter() {
        Calendar futureMeetingDate2 = new GregorianCalendar(2015, 6, 2);
        Calendar pastMeetingDate2 = new GregorianCalendar(2015, 2, 2);
        cm.addFutureMeeting(skiingContacts, futureMeetingDate);
        cm.addNewPastMeeting(homeContacts, pastMeetingDate, "meetingdate1");
        cm.addNewPastMeeting(pubContacts, pastMeetingDate2, "meetingdate2");
        cm.addFutureMeeting(pubContacts, futureMeetingDate2);
        cm.addNewPastMeeting(skiingContacts, pastMeetingDate, "meetingdate1");

        cm.listSorter();

//        assertEquals(pastMeetingDate, cm.getMeetingList().get(0).getDate());
//        assertEquals(pastMeetingDate, cm.getMeetingList().get(1).getDate());
//        assertEquals(pastMeetingDate2, cm.getMeetingList().get(2).getDate());
//        assertEquals(futureMeetingDate, cm.getMeetingList().get(3).getDate());
//        assertEquals(futureMeetingDate2, cm.getMeetingList().get(4).getDate());
//
//        SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.yyyy");
//
//        System.out.println(simpleDate.format(cm.getMeetingList().get(0).getDate().getTime()));
//
//
//
//        assertEquals(pastMeetingDate, cm.getPastMeeting(0).getDate());
//        assertEquals(pastMeetingDate, cm.getPastMeeting(1).getDate());
//        assertEquals(pastMeetingDate2, cm.getPastMeeting(2).getDate());
//        assertEquals(futureMeetingDate, cm.getFutureMeeting(3).getDate());
//        assertEquals(futureMeetingDate2, cm.getFutureMeeting(4).getDate());

    }

    @Test
    public void testFlush() throws IOException {

        DataIO testdata = new DataIO();

        Set<Contact> contacts = new HashSet<>();
        contacts.add(James);
        contacts.add(Jess);
        contacts.add(John);
        contacts.add(Julie);

        List<Meeting> meetings = new LinkedList<>();
        meetings.add(pub);
        meetings.add(home);
        meetings.add(skiing);

        testdata.writeFile(contacts, meetings);
        String firstline;
        Path path = Paths.get("contacts.txt");
        Charset ENCODING = StandardCharsets.UTF_8;

        try (Scanner fileReader = new Scanner(path, String.valueOf(ENCODING))){
            firstline = fileReader.nextLine();
        }

        assertEquals("Contact", firstline.substring(0,7));

    }
    @Test
    public void testEraser() throws IOException {
        cm.fileEraser();
        String firstLine = null;
        Path path = Paths.get("contacts.txt");
        Charset ENCODING = StandardCharsets.UTF_8;
        try (Scanner fileReader = new Scanner(path, String.valueOf(ENCODING))){
            firstLine = fileReader.nextLine();
        } catch (NoSuchElementException e) {
            //e.printStackTrace();
        }
        assertEquals(null, firstLine);
    }
    @Test
    public void meetingReadAndWriteTest() throws IOException {
        //erases file
        cm.fileEraser();
        //sets up new Contact manager with 3 contacts and 1 meeting
        ContactManagerImpl cm3 = new ContactManagerImpl();
        cm3.addNewContact("James", "Number 1");
        cm3.addNewContact("Suzy", "Number 2");
        cm3.addNewContact("Annie", "Number 3");
        cm3.addFutureMeeting(cm3.getContacts(0, 1, 2), futureMeetingDate);
        cm3.addNewPastMeeting(cm3.getContacts(1, 2), pastMeetingDate, "already happened");
        //writes file
        cm3.flush();
        //new contact manager that reads the file just written
        ContactManagerImpl cm4 = new ContactManagerImpl();
        Set<Contact> contactSet = cm4.getContacts(0, 1, 2);

        List<Meeting> meetingList = cm4.getMeetingList();
        System.out.println(meetingList.size());

        // FileReader reads text files in the default encoding.
        FileReader fileReader = new FileReader("contacts.txt");

        // Wrap FileReader in BufferedReader.
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String firstLine = null;
        //File contactsFile = new File("contacts.txt");
        Path path = Paths.get("contacts.txt");
        //contactsFile.createNewFile();
        Charset ENCODING = StandardCharsets.UTF_8;
        String line;
        while((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }
        bufferedReader.close();
    }

}
