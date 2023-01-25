package JsonData;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Random;

public class LocationNameProcessor {

    public Location getRandLocation() {
        Gson gson = new Gson();
        LocationData locationData;
        try {
            Reader reader = new FileReader("C:\\Users\\steph\\AndroidStudioProjects\\FamilyMapServer\\app"
                    + "\\src\\main\\java\\JsonData\\locations.json");
            locationData = gson.fromJson(reader, LocationData.class);
        } catch (FileNotFoundException e) {
            return null;
        }

        Location[] locations = locationData.getData();
        Random randNumGenerator = new Random();
        int randNum = randNumGenerator.nextInt(locations.length);

        return locations[randNum];
    }

    public String getRandSName() {
        Gson gson = new Gson();
        SName sName;
        try {
            Reader reader = new FileReader("C:\\Users\\steph\\AndroidStudioProjects\\FamilyMapServer\\app" +
                    "\\src\\main\\java\\JsonData\\snames.json");
            sName = gson.fromJson(reader, SName.class);
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            return null;
        }

        String[] sNames = sName.getData();
        Random randNumGenerator = new Random();
        int randNum = randNumGenerator.nextInt(sNames.length);

        return sNames[randNum];
    }

    public String getRandFName() {
        Gson gson = new Gson();
        FName fName;
        try {
            Reader reader = new FileReader("C:\\Users\\steph\\AndroidStudioProjects\\FamilyMapServer\\app"
                    + "\\src\\main\\java\\JsonData\\fnames.json");
            fName = gson.fromJson(reader, FName.class);
        } catch (FileNotFoundException e) {
            return null;
        }

        String[] fNames = fName.getData();
        Random randNumGenerator = new Random();
        int randNum = randNumGenerator.nextInt(fNames.length);

        return fNames[randNum];
    }

    public String getRandMName() {
        Gson gson = new Gson();
        MName mName;
        try {
            Reader reader = new FileReader("C:\\Users\\steph\\AndroidStudioProjects\\FamilyMapServer"
                    + "\\app\\src\\main\\java\\JsonData\\mnames.json");
            mName = gson.fromJson(reader, MName.class);
        } catch (FileNotFoundException e) {
            return null;
        }

        String[] mNames = mName.getData();
        Random randNumGenerator = new Random();
        int randNum = randNumGenerator.nextInt(mNames.length);

        return mNames[randNum];
    }
}
