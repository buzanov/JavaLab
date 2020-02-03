package homework1;

import java.io.File;

public class Structure {
    public static final void createFolder(String name) throws IllegalArgumentException {
        try {
            File folder = new File(name);
            if (!folder.exists()) {
                boolean b = folder.mkdir();
                if (!b) {
                    System.out.println("Folder creation error");
                    throw new IllegalArgumentException();
                }
            }
        } catch (SecurityException e) {
            System.out.println("Security Exception!");
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

}
