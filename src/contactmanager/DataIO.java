package contactmanager;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by James Thornton
 */
public class DataIO {

    private final File contactsFile = new File("contacts.txt");
    final static Charset ENCODING = StandardCharsets.UTF_8;
    private final SimpleDateFormat simpleDate = new SimpleDateFormat("dd-MM-yyyy");
    private Set<Contact> contacts = new HashSet<>();
    private List<Meeting> meetings = new LinkedList<>();
    private ArrayList<Object> contactsAndTheirMeetings = new ArrayList();

    public DataIO() throws IOException {
        if (!contactsFile.isFile())
            contactsFile.createNewFile();
    }

    public ArrayList<Object>  readFile() {

        // The name of the file to open.
        String fileName = "contacts.txt";

        // This will reference one line at a time
        String line;
        String line2;

        try {
            //reads file twice the first time adding contacts the second adding meetings
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("Contact"))
                    contactadder(line);
            }
            bufferedReader.close();

            FileReader fileReader2 = new FileReader(fileName);
            BufferedReader bufferedReader2 = new BufferedReader(fileReader2);

            while((line2 = bufferedReader2.readLine()) != null) {
                if (line2 != null) {
                    if (line2.startsWith("Meeting")) {
                        meetingadder(line2);
                    }
                }
            }

            contactsAndTheirMeetings.add(contacts);
            contactsAndTheirMeetings.add(meetings);
            bufferedReader2.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
        return contactsAndTheirMeetings;


    }

    private void meetingadder(String line) {

        //splits the Meeting line into an array
        String[] stringArray = line.split(",");
        //Sets the Id of the meeting
        int ID = Integer.parseInt(stringArray[1]);
        //splits a list of contact ID's
        String[] contactArray = stringArray[2].split(";");
        Set<Contact> meetingContacts = new HashSet<>();
        //iterates through contacts adding them as they are found to another list
        for (int i = 1; i < contactArray.length; i++) {
            int contactID = Integer.parseInt(contactArray[i]);
            meetingContacts.addAll(contacts
                    .stream()
                    .filter(inContactList -> inContactList.getId() == contactID)
                    .collect(Collectors.toList()));
        }
        //Adds date
        String[] splitDate = stringArray[3].split("-");
        int day = Integer.parseInt(splitDate[0]);
        int month = Integer.parseInt(splitDate[1]);
        int year = Integer.parseInt(splitDate[2]);
        Calendar date = new GregorianCalendar(day, month, year);
        Meeting newMeeting = new MeetingImpl(ID, date, meetingContacts);
        meetings.add(newMeeting);
    }

    private void contactadder(String line) {
        //split line into parts
        String[] stringArray = line.split(",");
        //each part of the array gets added to a new contact
        int ID = Integer.parseInt(stringArray[1]);
        String name = stringArray[2];
        String notes = stringArray[3];
        Contact newContact = new ContactImpl(ID, name, notes);
        contacts.add(newContact);
    }

    public void writeFile(Set<Contact> contactData, List<Meeting> meetingData) {
        Path path = Paths.get("contacts.txt");
        try (Scanner scanner = new Scanner(path, String.valueOf(ENCODING))){
            try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)){
                for(Contact c : contactData){
                    writer.write("Contact," + c.getId() + "," + c.getName() + "," + c.getNotes());
                    writer.newLine();
                }

                for(Meeting m : meetingData){
                    String meetingContactsByID = "";
                    for(Contact c : m.getContacts()){
                        meetingContactsByID = meetingContactsByID + ";" + c.getId();
                    }
                    if (m instanceof FutureMeeting) {
                        writer.write("Meeting," + m.getId() + ",meetingContactIDs" + meetingContactsByID  + "," + simpleDate.format(m.getDate().getTime()) + System.getProperty("line.separator"));
                    }
                    // If meeting is a Past Meeting write with notes
                    else if (m instanceof PastMeeting) {
                        writer.write("Meeting," + m.getId() + ",meetingContactIDs" + meetingContactsByID  + "," + simpleDate.format(m.getDate().getTime()) + ", " + ((PastMeeting) m).getNotes() + System.getProperty("line.separator"));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

