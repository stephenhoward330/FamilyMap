package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DataModel {
    private Event[] events;
    private Person[] persons;
    private String authToken;
    private String rootPersonID;

    // other settings (spouse, story, family line settings)
    private boolean showSpouseLines;
    private boolean showLifeStoryLines;
    private boolean showFamilyTreeLines;

    // filter settings (father/mother side, male, female)
    private boolean showFatherSide;
    private boolean showMotherSide;
    private boolean showMales;
    private boolean showFemales;

    public DataModel() {
        events = null;
        persons = null;
        authToken = null;
        rootPersonID = null;

        showSpouseLines = true;
        showLifeStoryLines = true;
        showFamilyTreeLines = true;

        showFatherSide = true;
        showMotherSide = true;
        showMales = true;
        showFemales = true;
    }

    public Event[] getFilteredEvents() {
        ArrayList<Event> newEvents = new ArrayList<>(Arrays.asList(events));

        Person rootPerson = findPerson(rootPersonID);

        if (!showFatherSide) {
            if (rootPerson != null && rootPerson.getFatherID() != null) {
                ArrayList<Event> fatherSideEvents = getSideEvents(rootPerson.getFatherID());
                newEvents.removeAll(fatherSideEvents);
            }
        }
        if (!showMotherSide) {
            if (rootPerson != null && rootPerson.getMotherID() != null) {
                ArrayList<Event> motherSideEvents = getSideEvents(rootPerson.getMotherID());
                newEvents.removeAll(motherSideEvents);
            }
        }

        ArrayList<Event> eventsToRemove = new ArrayList<>();
        if (!showMales) {
            for (Event event : newEvents) {
                Person person = findPerson(event.getPersonID());
                if (person != null && person.getGender().equals("m")) {
                    eventsToRemove.add(event);
                }
            }
            newEvents.removeAll(eventsToRemove);
        }
        eventsToRemove = new ArrayList<>();
        if (!showFemales) {
            for (Event event : newEvents) {
                Person person = findPerson(event.getPersonID());
                if (person != null && person.getGender().equals("f")) {
                    eventsToRemove.add(event);
                }
            }
            newEvents.removeAll(eventsToRemove);
        }

        Event[] finalEventArray = new Event[newEvents.size()];
        finalEventArray = newEvents.toArray(finalEventArray);
        return finalEventArray;
    }

    private ArrayList<Event> getSideEvents(String parentID) {
        ArrayList<Event> newEvents = new ArrayList<>();

        for (Event event : events) {
            if (event.getPersonID().equals(parentID)) {
                newEvents.add(event);
            }
        }

        Person rootPerson = findPerson(parentID);
        if (rootPerson != null && rootPerson.getFatherID() != null) {
            ArrayList<Event> fatherSideEvents = getSideEvents(rootPerson.getFatherID());
            newEvents.addAll(fatherSideEvents);
        }
        if (rootPerson != null && rootPerson.getMotherID() != null) {
            ArrayList<Event> motherSideEvents = getSideEvents(rootPerson.getMotherID());
            newEvents.addAll(motherSideEvents);
        }

        return newEvents;
    }

    public Event[] getPersonalEvents(Person person) {
        Event[] filteredEvents = getFilteredEvents();
        ArrayList<Event> personalEvents = new ArrayList<>();

        for (Event event : filteredEvents) {
            if (event.getPersonID().equals(person.getPersonID())) {
                personalEvents.add(event);
            }
        }

        ArrayList<Event> sortedEventArrayList = new ArrayList<>();
        ArrayList<String> eventTypeArray = new ArrayList<>();
        for (Event event : personalEvents) {
            if (!eventTypeArray.contains(event.getEventType().toLowerCase())) eventTypeArray.add(event.getEventType().toLowerCase());
        }
        Collections.sort(eventTypeArray);
        for (String eventType : eventTypeArray) {
            for (Event event : personalEvents) {
                if (event.getEventType().toLowerCase().equals(eventType)) {
                    sortedEventArrayList.add(event);
                }
            }
        }
        ArrayList<Integer> yearArrayList = new ArrayList<>();
        for (Event event : sortedEventArrayList) {
            if (!yearArrayList.contains(event.getYear())) yearArrayList.add(event.getYear());
        }
        Collections.sort(yearArrayList);
        ArrayList<Event> finalEventArrayList = new ArrayList<>();
        for (int year : yearArrayList) {
            for (Event event : sortedEventArrayList) {
                if (event.getYear() == year) {
                    finalEventArrayList.add(event);
                }
            }
        }
        Event birthEvent = null;
        Event deathEvent = null;
        for (Event event : finalEventArrayList) {
            if (event.getEventType().toLowerCase().equals("birth")) {
                birthEvent = event;
            }
            else if (event.getEventType().toLowerCase().equals("death")) {
                deathEvent = event;
            }
        }
        if (birthEvent != null) {
            finalEventArrayList.remove(birthEvent);
            finalEventArrayList.add(0, birthEvent);
        }
        if (deathEvent != null) {
            finalEventArrayList.remove(deathEvent);
            finalEventArrayList.add(deathEvent);
        }

        Event[] finalEvents = new Event[finalEventArrayList.size()];
        finalEvents = finalEventArrayList.toArray(finalEvents);
        return finalEvents;
    }

    private Person findPerson(String personID) {
        for (Person person : persons) {
            if (person.getPersonID().equals(personID)) {
                return person;
            }
        }

        // this should never happen
        return null;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getRootPersonID() {
        return rootPersonID;
    }

    public void setRootPersonID(String rootPersonID) {
        this.rootPersonID = rootPersonID;
    }

    public boolean isShowSpouseLines() {
        return showSpouseLines;
    }

    public void setShowSpouseLines(boolean showSpouseLines) {
        this.showSpouseLines = showSpouseLines;
    }

    public boolean isShowLifeStoryLines() {
        return showLifeStoryLines;
    }

    public void setShowLifeStoryLines(boolean showLifeStoryLines) {
        this.showLifeStoryLines = showLifeStoryLines;
    }

    public boolean isShowFamilyTreeLines() {
        return showFamilyTreeLines;
    }

    public void setShowFamilyTreeLines(boolean showFamilyTreeLines) {
        this.showFamilyTreeLines = showFamilyTreeLines;
    }

    public boolean isShowFatherSide() {
        return showFatherSide;
    }

    public void setShowFatherSide(boolean showFatherSide) {
        this.showFatherSide = showFatherSide;
    }

    public boolean isShowMotherSide() {
        return showMotherSide;
    }

    public void setShowMotherSide(boolean showMotherSide) {
        this.showMotherSide = showMotherSide;
    }

    public boolean isShowMales() {
        return showMales;
    }

    public void setShowMales(boolean showMales) {
        this.showMales = showMales;
    }

    public boolean isShowFemales() {
        return showFemales;
    }

    public void setShowFemales(boolean showFemales) {
        this.showFemales = showFemales;
    }
}
