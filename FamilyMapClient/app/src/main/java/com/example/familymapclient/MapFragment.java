package com.example.familymapclient;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;

import Model.DataModel;
import Model.Event;
import Model.Person;
import Model.Singleton;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_AZURE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_BLUE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_CYAN;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_MAGENTA;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ORANGE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ROSE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_VIOLET;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_YELLOW;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker;

public class MapFragment extends Fragment {

    private GoogleMap mMap;
    private TextView textView;
    private ImageView imageView;
    private String eventID;
    private SupportMapFragment mapFragment;
    private Event selectedEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);

        textView = view.findViewById(R.id.text);
        imageView = view.findViewById(R.id.map_icon);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        startMap();

        return view;
    }

    public void startMap() {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.clear();
                initMap();
                if (selectedEvent != null) drawLines(selectedEvent);
                if (getArguments() != null) {
                    eventID = getArguments().getString("eventID");
                    DataModel dataModel = Singleton.getInstance();
                    Event[] events = dataModel.getEvents();
                    for (Event event : events) {
                        if (event.getEventID().equals(eventID)) {
                            drawLines(event);
                        }
                    }
                    centerMap(eventID);
                }
                else eventID = null;
            }
        });
    }

    private void initMap() {
        DataModel dataModel = Singleton.getInstance();
        Event[] filteredEvents = dataModel.getFilteredEvents();

        addMarkers(filteredEvents);
        setMarkerListener();
    }

    private void centerMap(String eventID) {
        DataModel dataModel = Singleton.getInstance();
        Event[] events = dataModel.getEvents();

        Event centerEvent = null;
        for (Event event : events) {
            if (event.getEventID().equals(eventID)) {
                centerEvent = event;
            }
        }

        if (centerEvent == null) return;

        LatLng latLng = new LatLng(centerEvent.getLatitude(), centerEvent.getLongitude());
        CameraUpdate update = CameraUpdateFactory.newLatLng(latLng);
        mMap.moveCamera(update);

        String textToSet;
        Person person = findPerson(centerEvent.getPersonID());

        if (person != null) {
            Iconify.with(new FontAwesomeModule());
            if (person.getGender().equals("f")) {
                Drawable icon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).colorRes(R.color.pink).sizeDp(76);
                imageView.setImageDrawable(icon);
            }
            else {
                Drawable icon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.baby_blue).sizeDp(76);
                imageView.setImageDrawable(icon);
            }

            textToSet = person.toString() + "\n" + centerEvent.toString();
        }
        else {
            textToSet = centerEvent.toString();
        }
        textView.setText(textToSet);
        setTextListener(centerEvent.getPersonID());

    }

    private void addMarkers(Event[] events) {
        LatLng latLng;
        MarkerOptions options;
        float[] hueArray = {HUE_GREEN, HUE_BLUE, HUE_RED, HUE_YELLOW, HUE_AZURE, HUE_CYAN, HUE_MAGENTA, HUE_ORANGE, HUE_ROSE, HUE_VIOLET};
        ArrayList<String> claimedColors = new ArrayList<>();
        int hueIndex;

        for (Event event : events) {
            latLng = new LatLng(event.getLatitude(), event.getLongitude());

            if (claimedColors.contains(event.getEventType().toLowerCase())) {
                hueIndex = claimedColors.indexOf(event.getEventType().toLowerCase());
            }
            else {
                hueIndex = claimedColors.size();
                claimedColors.add(event.getEventType().toLowerCase());
            }

            hueIndex = hueIndex%10;
            options = new MarkerOptions().position(latLng).title(event.getCity()).icon(defaultMarker(hueArray[hueIndex]));

            Marker marker = mMap.addMarker(options);
            marker.setTag(event);
        }
    }

    private void setMarkerListener() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Event event = (Event) marker.getTag();

                String textToSet;
                if (event != null) {
                    selectedEvent = event;

                    Person person = findPerson(event.getPersonID());
                    if (person != null) {
                        Iconify.with(new FontAwesomeModule());
                        if (person.getGender().equals("f")) {
                            Drawable icon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).colorRes(R.color.pink).sizeDp(76);
                            imageView.setImageDrawable(icon);
                        }
                        else {
                            Drawable icon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.baby_blue).sizeDp(76);
                            imageView.setImageDrawable(icon);
                        }

                        textToSet = person.toString() + "\n" + event.toString();
                    }
                    else {
                        textToSet = event.toString();
                    }
                    textView.setText(textToSet);
                    setTextListener(event.getPersonID());

                    mMap.clear();
                    initMap();
                    drawLines(event);
                }

                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                selectedEvent = null;
                initMap();
            }
        });
    }

    private void setTextListener(final String personID) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                intent.putExtra("personID", personID);
                startActivity(intent);
            }
        });
    }

    private Person findPerson(String personID) {
        DataModel dataModel = Singleton.getInstance();
        Person[] persons = dataModel.getPersons();

        for (Person person : persons) {
            if (person.getPersonID().equals(personID)) {
                return person;
            }
        }

        // this should never happen
        return null;
    }

    private ArrayList<Event> findEvents(String personID, Event[] events) {
        ArrayList<Event> returnArray = new ArrayList<>();

        for (Event event : events) {
            if (event.getPersonID().equals(personID)) {
                returnArray.add(event);
            }
        }

        return returnArray;
    }

    public String getEventID() {
        return this.eventID;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                startActivity(SearchActivity.class);
                return true;    // return true when handled successfully
            case R.id.settings:
                startActivity(SettingsActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startActivity(Class aClass) {
        Intent intent = new Intent(getActivity(), aClass);
        startActivity(intent);
    }

    private void drawLines(Event event) {
        DataModel dataModel = Singleton.getInstance();
        Event[] filteredEvents = dataModel.getFilteredEvents();

        if (dataModel.isShowSpouseLines()) {
            drawSpouseLine(filteredEvents, event);
        }
        if (dataModel.isShowLifeStoryLines()) {
            drawLifeStoryLine(event);
        }
        if (dataModel.isShowFamilyTreeLines()) {
            drawFamilyTreeLine(filteredEvents, event);
        }
    }

    private void drawSpouseLine(Event[] events, Event mainEvent) {
        Person mainPerson = findPerson(mainEvent.getPersonID());
        if (mainPerson.getSpouseID() != null) {
            Person spouse = findPerson(mainPerson.getSpouseID());
            connectPersons(events, mainEvent, spouse, BLUE, 8);
        }
    }

    private void drawLifeStoryLine(Event mainEvent) {
        DataModel dataModel = Singleton.getInstance();
        Person mainPerson = findPerson(mainEvent.getPersonID());
        Event[] mainPersonEvents = dataModel.getPersonalEvents(mainPerson);

        for (int i = 0; i < mainPersonEvents.length-1; i++) {
            connectEvents(mainPersonEvents[i], mainPersonEvents[i+1], RED, 8);
        }
    }

    private void drawFamilyTreeLine(Event[] events, Event mainEvent) {
        connectToParents(mainEvent, events, 30);
    }

    private void connectToParents(Event childEvent, Event[] events, int width) {
        if (childEvent == null) return;
        Person child = findPerson(childEvent.getPersonID());
        if (child.getFatherID() != null) {
            Person father = findPerson(child.getFatherID());
            if (father != null) {
                Event parentEvent = connectPersons(events, childEvent, father, GREEN, width);
                connectToParents(parentEvent, events, width-7);
            }
        }
        if (child.getMotherID() != null) {
            Person mother = findPerson(child.getMotherID());
            if (mother != null) {
                Event parentEvent = connectPersons(events, childEvent, mother, GREEN, width);
                connectToParents(parentEvent, events, width-7);
            }
        }
    }

    private Event connectPersons(Event[] events, Event event1, Person person2, int color, int width) {
        Event event2 = null;

        // use birth event
        for (Event event : events) {
            if (event.getPersonID().equals(person2.getPersonID()) && event.getEventType().equals("Birth")) {
                event2 = event;
            }
        }

        // use earliest event
        int earliestYear;
        if (event2 == null) {
            earliestYear = 5000;
            for (Event event : events) {
                if (event.getPersonID().equals(person2.getPersonID()) && event.getYear() < earliestYear) {
                    event2 = event;
                    earliestYear = event.getYear();
                }
            }
        }

        if (event1 != null && event2 != null) {
            connectEvents(event1, event2, color, width);
        }

        return event2;
    }

    private void connectEvents(Event event1, Event event2, int color, int width) {
        LatLng latLng = new LatLng(event1.getLatitude(), event1.getLongitude());
        LatLng latLng2 = new LatLng(event2.getLatitude(), event2.getLongitude());
        PolylineOptions options = new PolylineOptions().add(latLng, latLng2).color(color).width(width);
        mMap.addPolyline(options);
    }
}
