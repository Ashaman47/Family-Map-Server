package Service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import DAO.PersonDAO;
import Handlers.JsonSerializer;
import Model.Event;
import Model.Person;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Random;

public class GenerateData {
    ArrayList<String> snames = (new snames()).getSnames();
    ArrayList<String> fnames = (new fnames()).getFnames();
    ArrayList<String> mnames = (new mnames()).getMnames();
    ArrayList<String> names = new ArrayList<>();

    public void Generate(String userName, String firstName, String lastName, String gender, int numGens) throws IOException, DataAccessException {
        File f = new File("C:\\Users\\ryank\\Family Map\\src\\Service\\json\\locations.json");
        InputStream i = new FileInputStream(f);
        InputStreamReader isr =  new InputStreamReader(i, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        int b;
        StringBuilder buf = new StringBuilder(512);
        while ((b = br.read()) != -1) {
            buf.append((char) b);
        }
        Data data = JsonSerializer.deserialize(buf.toString(),Data.class);
        Person newPerson = new Person(firstName + lastName,userName,firstName,lastName, gender);
        Random rand = new Random();
        ArrayList<location> loc = data.getData();
        location l = loc.get(rand.nextInt(loc.size()));
        int birthyear = 2020 - ((5 - numGens) * 30);
        Event birthEvent = new Event(firstName + lastName + userName + "birth",userName, newPerson.getPersonID(),
                Float.parseFloat(l.latitude), Float.parseFloat(l.longitude),l.country,l.city,"birth", birthyear);
        if (numGens >= 1) {
            String mfirstName = mnames.get(rand.nextInt(mnames.size()));
            while (names.contains(mfirstName)){
                mfirstName = mnames.get(rand.nextInt(mnames.size()));
            }
            names.add(mfirstName);
            newPerson.setFatherID(mfirstName + lastName + userName);
            String ffirstName = fnames.get(rand.nextInt(fnames.size()));
            String flastName = snames.get(rand.nextInt(snames.size()));
            while (names.contains(ffirstName)){
                ffirstName = mnames.get(rand.nextInt(fnames.size()));
            }
            names.add(ffirstName);
            Generate(userName, mfirstName, lastName, "m", numGens - 1, ffirstName + flastName + userName);
            newPerson.setMotherID(ffirstName + flastName + userName);
            Generate(userName, ffirstName, flastName, "f", numGens - 1, mfirstName + lastName + userName);
        }
        Database db = new Database();
        Connection conn = db.getConnection();
        PersonDAO pDAO = new PersonDAO(conn);
        pDAO.insert(newPerson);
        db.closeConnection(true);
        db = new Database();
        conn = db.getConnection();
        EventDAO eDAO = new EventDAO(conn);
        eDAO.insert(birthEvent);
        db.closeConnection(true);
    }
    public void Generate(String userName, String firstName, String lastName, String gender, int numGens, String spouseID) throws IOException, DataAccessException {
        File f = new File("C:\\Users\\ryank\\Family Map\\src\\Service\\json\\locations.json");
        InputStream i = new FileInputStream(f);
        InputStreamReader isr =  new InputStreamReader(i, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        int b;
        StringBuilder buf = new StringBuilder(512);
        while ((b = br.read()) != -1) {
            buf.append((char) b);
        }
        Data data = JsonSerializer.deserialize(buf.toString(),Data.class);
        Person newPerson = new Person(firstName + lastName + userName,userName,firstName,lastName, gender);
        newPerson.setSpouseID(spouseID);
        Random rand = new Random();
        ArrayList<location> loc = data.getData();
        location l = loc.get(rand.nextInt(loc.size()));
        int birthyear = 2020 - ((5 - numGens) * 30);
        Event birthEvent = new Event(firstName + lastName + userName + "birth",userName, firstName + lastName + userName,
                Float.parseFloat(l.latitude), Float.parseFloat(l.longitude),l.country,l.city,"birth", birthyear);
        l = loc.get(rand.nextInt(loc.size()));
        int deathyear = birthyear + 60;
        if (deathyear > 2020){
            deathyear = 2020;
        }
        Event deathEvent = new Event(firstName + lastName + userName + "death",userName, firstName + lastName + userName,
                Float.parseFloat(l.latitude), Float.parseFloat(l.longitude),l.country,l.city,"death", deathyear);
        if (numGens >= 1) {
            String mfirstName = mnames.get(rand.nextInt(mnames.size()));
            while (names.contains(mfirstName)){
                mfirstName = mnames.get(rand.nextInt(mnames.size()));
            }
            names.add(mfirstName);
            newPerson.setFatherID(mfirstName + lastName + userName);
            String ffirstName = fnames.get(rand.nextInt(fnames.size()));
            String flastName = snames.get(rand.nextInt(snames.size()));
            while (names.contains(ffirstName)){
                ffirstName = mnames.get(rand.nextInt(fnames.size()));
            }
            names.add(ffirstName);
            Generate(userName, mfirstName, lastName, "m", numGens - 1, ffirstName + flastName + userName);
            newPerson.setMotherID(ffirstName + flastName + userName);
            Generate(userName, ffirstName, flastName, "f", numGens - 1, mfirstName + lastName + userName);
        }
        Database db = new Database();
        Connection conn = db.getConnection();
        PersonDAO pDAO = new PersonDAO(conn);
        if (pDAO.find(newPerson.getPersonID()) != null){
            System.out.println("ERROR");
        }
        pDAO.insert(newPerson);
        db.closeConnection(true);
        db = new Database();
        conn = db.getConnection();
        EventDAO eDAO = new EventDAO(conn);
        l = loc.get(rand.nextInt(loc.size()));
        Event marriageEvent;
        if (eDAO.find(spouseID + "marriage") == null) {
            int marriageyear = birthyear + 25;
            if (marriageyear > 2020) {
                marriageyear = 2020;
            }
            marriageEvent = new Event(firstName + lastName + userName + "marriage", userName, firstName + lastName + userName,
                    Float.parseFloat(l.latitude), Float.parseFloat(l.longitude), l.country, l.city, "marriage", marriageyear);
        }
        else {
            marriageEvent = eDAO.find(spouseID + "marriage");
            marriageEvent.seteventID(firstName + lastName + userName + "marriage");
            marriageEvent.setPersonID(firstName + lastName + userName);
        }
        eDAO.insert(birthEvent);
        eDAO.insert(deathEvent);
        eDAO.insert(marriageEvent);
        db.closeConnection(true);
    }
}
