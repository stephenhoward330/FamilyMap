package com.example.familymapclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;

import Model.DataModel;
import Model.Event;
import Model.Person;
import Model.Singleton;

public class SearchActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText editText;
    private RecyclerView recyclerView;
    private Adapter adapter;

    private Person[] persons;
    private Event[] events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        imageView = findViewById(R.id.search_icon);
        editText = findViewById(R.id.search_text);
        recyclerView = findViewById(R.id.search_recycler_view);

        setPersonsAndEvents();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Iconify.with(new FontAwesomeModule());
        imageView.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_search).colorRes(R.color.black).sizeDp(24));

        changeUI();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                changeUI();
            }
        };
        editText.addTextChangedListener(textWatcher);
    }

    public void setPersonsAndEvents() {
        DataModel dataModel = Singleton.getInstance();
        persons = dataModel.getPersons();
        events = dataModel.getFilteredEvents();
    }

    public Person[] searchPeople(String matchString) {
        ArrayList<Person> foundPersons = new ArrayList<>();

        if (matchString.length() > 0) {
            for (Person person : persons) {
                if (person.toString().toLowerCase().contains(matchString.toLowerCase())) {
                    foundPersons.add(person);
                }
            }
        }

        Person[] finalPersons = new Person[foundPersons.size()];
        finalPersons = foundPersons.toArray(finalPersons);
        return finalPersons;
    }

    public Event[] searchEvents(String matchString) {
        ArrayList<Event> foundEvents = new ArrayList<>();

        if (matchString.length() > 0) {
            for (Event event : events) {
                if (event.toString().toLowerCase().contains(matchString.toLowerCase())) {
                    foundEvents.add(event);
                }
            }
        }

        Event[] finalEvents = new Event[foundEvents.size()];
        finalEvents = foundEvents.toArray(finalEvents);
        return finalEvents;
    }

    void changeUI() {
        ArrayList<ChildInfo> childInfoArrayList = new ArrayList<>();

        Person[] foundPersons = searchPeople(editText.getText().toString());
        Event[] foundEvents = searchEvents(editText.getText().toString());

        if (editText.getText().toString().length() > 0) {
            for (Person person : foundPersons) {
                Drawable icon;
                ChildInfo childInfo;
                if (person.getGender().equals("m")) {
                    icon = new IconDrawable(this, FontAwesomeIcons.fa_male).colorRes(R.color.baby_blue).sizeDp(48);
                    childInfo = new ChildInfo(icon, person.toString(), "Male", person.getPersonID());
                } else {
                    icon = new IconDrawable(this, FontAwesomeIcons.fa_female).colorRes(R.color.pink).sizeDp(48);
                    childInfo = new ChildInfo(icon, person.toString(), "Female", person.getPersonID());
                }
                childInfoArrayList.add(childInfo);
            }
            for (Event event : foundEvents) {
                Drawable markerIcon;
                switch (event.getEventType()) {
                    case "Birth":
                        markerIcon = new IconDrawable(this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.green).sizeDp(48);
                        break;
                    case "Marriage":
                        markerIcon = new IconDrawable(this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.blue).sizeDp(48);
                        break;
                    case "Death":
                        markerIcon = new IconDrawable(this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.red).sizeDp(48);
                        break;
                    default:
                        markerIcon = new IconDrawable(this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.black).sizeDp(48);
                }

                ChildInfo eventInfo = new ChildInfo(markerIcon, event.toString(), findPerson(event.getPersonID()).toString(), event.getEventID());
                childInfoArrayList.add(eventInfo);
            }
        }

        ChildInfo[] infos = new ChildInfo[childInfoArrayList.size()];
        infos = childInfoArrayList.toArray(infos);
        adapter = new Adapter(this, infos);
        recyclerView.setAdapter(adapter);
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

    public class ChildInfo {
        private Drawable personIcon;
        private String text1;
        private String text2;
        private String ID;

        public ChildInfo(Drawable personIcon, String text1, String text2, String ID) {
            this.personIcon = personIcon;
            this.text1 = text1;
            this.text2 = text2;
            this.ID = ID;
        }

        public Drawable getPersonIcon() {
            return personIcon;
        }

        public String getText1() {
            return text1;
        }

        public String getText2() {
            return text2;
        }

        public String getID() {
            return ID;
        }
    }

    class Adapter extends RecyclerView.Adapter<Holder> {

        private ChildInfo[] infos;
        private LayoutInflater inflater;

        public Adapter(Context context, ChildInfo[] infos) {
            this.infos = infos;
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.person_item, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            ChildInfo info = infos[position];
            holder.bind(info);
        }

        @Override
        public int getItemCount() {
            return infos.length;
        }

    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView icon;
        private TextView textView1;
        private TextView textView2;
        private String ID;

        public Holder(View view) {
            super(view);
            icon = view.findViewById(R.id.person_item_icon);
            textView1 = view.findViewById(R.id.person_item_text_1);
            textView2 = view.findViewById(R.id.person_item_text_2);
            view.setOnClickListener(this);
        }

        void bind(ChildInfo info) {
            icon.setImageDrawable(info.getPersonIcon());
            textView1.setText(info.getText1());
            textView2.setText(info.getText2());
            this.ID = info.getID();
        }

        @Override
        public void onClick(View view) {
            String text = (String) textView2.getText();
            if (text.equals("Male") || text.equals("Female")) {
                startPersonActivity(ID);
            }
            else {
                startEventActivity(ID);
            }
        }

    }

    void startPersonActivity(String personID) {
        Intent intent = new Intent(this, PersonActivity.class);
        intent.putExtra("personID", personID);
        startActivity(intent);
    }

    void startEventActivity(String eventID) {
        Intent intent = new Intent(this, EventActivity.class);
        intent.putExtra("eventID", eventID);
        startActivity(intent);
    }
}
