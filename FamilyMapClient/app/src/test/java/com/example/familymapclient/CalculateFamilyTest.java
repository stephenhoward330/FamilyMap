package com.example.familymapclient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import Model.DataModel;
import Model.Person;
import Model.Singleton;

import static org.junit.jupiter.api.Assertions.*;

public class CalculateFamilyTest {

    private DataModel dataModel;
    private Person root;
    private PersonActivity personActivity;

    @BeforeEach
    public void setUp() {
        dataModel = Singleton.getInstance();

        root = new Person("rootPersonID", "username", "root", "jones", "m", "fatherID", "motherID", "spouseID");

        personActivity = new PersonActivity();
        personActivity.setMainPerson(root);
    }

    @Test
    public void calcFamilyPass() { // has all family members
        Person father = new Person("fatherID", "username", "father", "jones", "m", null, null, "motherID");
        Person mother = new Person("motherID", "username", "mother", "jones", "f", null, null, "fatherID");
        Person spouse = new Person("spouseID", "username", "spouse", "jones", "f", null, null, "rootPersonID");
        Person child = new Person("childID", "username", "child", "jones", "f", "rootPersonID", "childMotherID", null);

        Person[] family = {child, root, spouse, father, mother};
        dataModel.setPersons(family);

        ArrayList<Person> testPersons = personActivity.getFamilyMembers();

        assertEquals(4, testPersons.size());
        assertNotNull(testPersons.get(0));
        assertEquals(testPersons.get(0).getPersonID(), "fatherID");
        assertNotNull(testPersons.get(1));
        assertEquals(testPersons.get(1).getPersonID(), "motherID");
        assertNotNull(testPersons.get(2));
        assertEquals(testPersons.get(2).getPersonID(), "spouseID");
        assertNotNull(testPersons.get(3));
        assertEquals(testPersons.get(3).getPersonID(), "childID");
    }

    @Test
    public void calcFamilyFail() { // has no family members
        Person[] family = {root};
        dataModel.setPersons(family);

        ArrayList<Person> testPersons = personActivity.getFamilyMembers();

        assertEquals(4, testPersons.size());
        assertNull(testPersons.get(0));
        assertNull(testPersons.get(1));
        assertNull(testPersons.get(2));
        assertNull(testPersons.get(3));
    }
}