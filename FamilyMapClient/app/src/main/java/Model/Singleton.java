package Model;

public class Singleton {
    private static DataModel instance = null;

    public static DataModel getInstance() {
        if (instance == null)
            instance = new DataModel();
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }
}
