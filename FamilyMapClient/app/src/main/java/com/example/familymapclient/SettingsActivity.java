package com.example.familymapclient;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import Model.DataModel;
import Model.Singleton;

public class SettingsActivity extends AppCompatActivity {

    Switch ls_switch;
    Switch ft_switch;
    Switch spouse_switch;
    Switch father_switch;
    Switch mother_switch;
    Switch male_switch;
    Switch female_switch;
    LinearLayout logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ls_switch = findViewById(R.id.life_story_switch);
        ft_switch = findViewById(R.id.family_tree_switch);
        spouse_switch = findViewById(R.id.spouse_switch);
        father_switch = findViewById(R.id.father_side_switch);
        mother_switch = findViewById(R.id.mother_side_switch);
        male_switch = findViewById(R.id.male_event_switch);
        female_switch = findViewById(R.id.female_event_switch);
        logout = findViewById(R.id.logout_button);

        final DataModel dataModel = Singleton.getInstance();
        ls_switch.setChecked(dataModel.isShowLifeStoryLines());
        ft_switch.setChecked(dataModel.isShowFamilyTreeLines());
        spouse_switch.setChecked(dataModel.isShowSpouseLines());
        father_switch.setChecked(dataModel.isShowFatherSide());
        mother_switch.setChecked(dataModel.isShowMotherSide());
        male_switch.setChecked(dataModel.isShowMales());
        female_switch.setChecked(dataModel.isShowFemales());

        ls_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataModel.setShowLifeStoryLines(isChecked);
            }
        });
        ft_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataModel.setShowFamilyTreeLines(isChecked);
            }
        });
        spouse_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataModel.setShowSpouseLines(isChecked);
            }
        });
        father_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataModel.setShowFatherSide(isChecked);
            }
        });
        mother_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataModel.setShowMotherSide(isChecked);
            }
        });
        male_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataModel.setShowMales(isChecked);
            }
        });
        female_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataModel.setShowFemales(isChecked);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.destroyInstance();
                up();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        up();
    }

    private void up() {
        NavUtils.navigateUpFromSameTask(this);
    }
}
