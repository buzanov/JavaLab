package homework1;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Downloader extends Thread {
    static Object MUTEX = new Object();
    String sourceURL;
    String targetDirectory;

    @Override
    public void run() {
        try {
            download(sourceURL, targetDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Downloader(String sourceURL, String targetDirectory) {
        this.sourceURL = sourceURL;
        this.targetDirectory = targetDirectory;
    }

    private static Path download(String sourceURL, String targetDirectory) throws IOException {
        URL url = new URL(sourceURL);
        synchronized (MUTEX) {
            String fileName = sourceURL.substring(sourceURL.lastIndexOf('/') + 1, sourceURL.length());
            File temp = new File(targetDirectory + "\\" + fileName);
            int i = 0;
            while (temp.exists()) {
                i++;
                temp = new File(targetDirectory + "\\" + fileName.substring(0, fileName.lastIndexOf(".")) + "(" + i + ")" + fileName.substring(fileName.lastIndexOf(".")));
            }
            if (i != 0) {
                fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "(" + i + ")" + fileName.substring(fileName.lastIndexOf("."));
            }
            Path targetPath = new File(targetDirectory + File.separator + fileName).toPath();
            Files.copy(url.openStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return targetPath;
        }
    }
}
