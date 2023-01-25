package com.example.familymapclient;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Model.DataModel;
import Model.Event;
import Model.Person;
import Model.Singleton;

public class PersonActivity extends AppCompatActivity {

    private TextView firstNameView;
    private TextView lastNameView;
    private TextView genderView;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private Person mainPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        firstNameView = findViewById(R.id.person_first_name);
        lastNameView = findViewById(R.id.person_last_name);
        genderView = findViewById(R.id.person_gender);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String mainPersonID = getIntent().getStringExtra("personID");

        mainPerson = null;
        if (mainPersonID != null) {
            mainPerson = findPerson(mainPersonID);
            if (mainPerson != null) {
                firstNameView.setText(mainPerson.getFirstName());
                lastNameView.setText(mainPerson.getLastName());
                if (mainPerson.getGender().equals("m")) {
                    genderView.setText(R.string.choose_male);
                } else {
                    genderView.setText(R.string.choose_female);
                }
            }
        }

        if (mainPerson != null) {
            Iconify.with(new FontAwesomeModule());
            initUI();
        }
    }

    public void setMainPerson(Person mainPerson) {
        this.mainPerson = mainPerson;
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

    public class ItemList implements Parent<ChildInfo> {

        private String title;
        private ChildInfo[] childInfos;

        ItemList(String title, ChildInfo[] childInfos) {
            this.title = title;
            this.childInfos = childInfos;
        }

        @Override
        public List<ChildInfo> getChildList() {
            return Arrays.asList(childInfos);
        }

        @Override
        public boolean isInitiallyExpanded() {
            return false;
        }

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

    void initUI() {

        // do events
        DataModel dataModel = Singleton.getInstance();
        Event[] events = dataModel.getPersonalEvents(mainPerson);

        ArrayList<ChildInfo> eventTempInfos = new ArrayList<>();
        for (Event event : events) {

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

            ChildInfo eventInfo = new ChildInfo(markerIcon, event.toString(), findPerson(event.getPersonID()).toString(), event.getEventID()); //FIXME change icons
            eventTempInfos.add(eventInfo);
        }

        ChildInfo[] eventInfos = new ChildInfo[eventTempInfos.size()];
        eventInfos = eventTempInfos.toArray(eventInfos);
        ItemList eventList = new ItemList("LIFE EVENTS", eventInfos);

        // do people
        ArrayList<Person> members = getFamilyMembers();

        ArrayList<ChildInfo> familyTempInfos = new ArrayList<>();
        if (members.get(0) != null) {
            Drawable icon = new IconDrawable(this, FontAwesomeIcons.fa_male).colorRes(R.color.baby_blue).sizeDp(48);
            Person father = members.get(0);
            ChildInfo fatherInfo = new ChildInfo(icon, father.toString(), "Father", father.getPersonID());
            familyTempInfos.add(fatherInfo);
        }
        if (members.get(1) != null) {
            Drawable icon = new IconDrawable(this, FontAwesomeIcons.fa_female).colorRes(R.color.pink).sizeDp(48);
            Person mother = members.get(1);
            ChildInfo motherInfo = new ChildInfo(icon, mother.toString(), "Mother", mother.getPersonID());
            familyTempInfos.add(motherInfo);
        }
        if (members.get(2) != null) {
            Drawable icon;
            Person spouse = members.get(2);
            if (spouse.getGender().equals("m")) {
                icon = new IconDrawable(this, FontAwesomeIcons.fa_male).colorRes(R.color.baby_blue).sizeDp(48);
            }
            else {
                icon = new IconDrawable(this, FontAwesomeIcons.fa_female).colorRes(R.color.pink).sizeDp(48);
            }
            ChildInfo spouseInfo = new ChildInfo(icon, spouse.toString(), "Spouse", spouse.getPersonID());
            familyTempInfos.add(spouseInfo);
        }
        if (members.get(3) != null) {
            Drawable icon;
            Person child = members.get(3);
            if (child.getGender().equals("m")) {
                icon = new IconDrawable(this, FontAwesomeIcons.fa_male).colorRes(R.color.baby_blue).sizeDp(48);
            }
            else {
                icon = new IconDrawable(this, FontAwesomeIcons.fa_female).colorRes(R.color.pink).sizeDp(48);
            }
            ChildInfo childInfo = new ChildInfo(icon, child.toString(), "Child", child.getPersonID());
            familyTempInfos.add(childInfo);
        }

        ChildInfo[] familyInfos = new ChildInfo[familyTempInfos.size()];
        familyInfos = familyTempInfos.toArray(familyInfos);
        ItemList personList = new ItemList("FAMILY", familyInfos);

        List<ItemList> itemLists = Arrays.asList(eventList, personList);

        adapter = new Adapter(this, itemLists);
        recyclerView.setAdapter(adapter);
    }

    public ArrayList<Person> getFamilyMembers() {
        ArrayList<Person> members = new ArrayList<>();

        DataModel dataModel = Singleton.getInstance();
        Person[] persons = dataModel.getPersons();

        Person father = null;
        Person mother = null;
        Person spouse = null;
        Person child = null;

        for (Person person : persons) {
            if (person.getPersonID() != null && mainPerson.getFatherID() != null) {
                if (person.getPersonID().equals(mainPerson.getFatherID())) {
                    father = person;
                }
            }
            if (person.getPersonID() != null && mainPerson.getMotherID() != null) {
                if (person.getPersonID().equals(mainPerson.getMotherID())) {
                    mother = person;
                }
            }
            if (person.getPersonID() != null && mainPerson.getSpouseID() != null) {
                if (person.getPersonID().equals(mainPerson.getSpouseID())) {
                    spouse = person;
                }
            }
            if (person.getFatherID() != null && person.getMotherID() != null && mainPerson.getPersonID() != null) {
                if (person.getFatherID().equals(mainPerson.getPersonID()) || person.getMotherID().equals(mainPerson.getPersonID())) {
                    child = person;
                }
            }
        }

        members.add(father);
        members.add(mother);
        members.add(spouse);
        members.add(child);

        return members;
    }

    class Adapter extends ExpandableRecyclerAdapter <ItemList, ChildInfo, ParentHolder, ChildHolder> {

        private LayoutInflater inflater;

        public Adapter(Context context, List<ItemList> itemLists) {
            super(itemLists);
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public ParentHolder onCreateParentViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.person_group, viewGroup, false);
            return new ParentHolder(view);
        }

        @NonNull
        @Override
        public ChildHolder onCreateChildViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.person_item, viewGroup, false);
            return new ChildHolder(view);
        }

        @Override
        public void onBindParentViewHolder(@NonNull ParentHolder holder, int i, ItemList itemList) {
            holder.bind(itemList.title);
        }

        @Override
        public void onBindChildViewHolder(@NonNull ChildHolder holder, int i, int j, ChildInfo info) {
            holder.bind(info);
        }

    }

    class ParentHolder extends ParentViewHolder {

        private ImageView imageView;
        private TextView textView;

        public ParentHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.drop_icon);
            textView = view.findViewById(R.id.person_group_title);
        }

        void bind(String title) {
            textView.setText(title);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            if (isExpanded()) {
                imageView.setImageDrawable(getDrawable(R.drawable.ic_dropup_foreground));
            }
            else {
                imageView.setImageDrawable(getDrawable(R.drawable.ic_dropdown_foreground));
            }
        }
    }

    class ChildHolder extends ChildViewHolder implements View.OnClickListener {

        private ImageView personIcon;
        private TextView textView1;
        private TextView textView2;
        private String ID;

        public ChildHolder(View view) {
            super(view);
            personIcon = view.findViewById(R.id.person_item_icon);
            textView1 = view.findViewById(R.id.person_item_text_1);
            textView2 = view.findViewById(R.id.person_item_text_2);
            view.setOnClickListener(this);
        }

        void bind(ChildInfo info) {
            personIcon.setImageDrawable(info.getPersonIcon());
            textView1.setText(info.getText1());
            textView2.setText(info.getText2());
            this.ID = info.getID();
        }

        @Override
        public void onClick(View view) {
            String text = (String) textView2.getText();
            if (text.equals("Father") || text.equals("Mother") || text.equals("Spouse") || text.equals("Child")) {
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
