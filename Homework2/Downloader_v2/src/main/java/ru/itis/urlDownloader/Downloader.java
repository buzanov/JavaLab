package main.java.ru.itis.urlDownloader;

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

    public void download() {
        this.start();
    }

    private Path download(String sourceURL, String targetDirectory) throws IOException {
        URL url = new URL(sourceURL);
        synchronized (MUTEX) {
            //https://sun7-7.userapi.com/c858328/v858328003/8ed50/fpsp5lpaRlI.jpg
            String fileName = sourceURL.substring(sourceURL.lastIndexOf('/') + 1);
            Path targetPath;

            int i = 0;
            if (targetDirectory.equals("")) {
                File temp = new File(fileName);
                while (temp.exists()) {
                    i++;
                    temp = new File(fileName.substring(0, fileName.lastIndexOf(".")) +
                            "(" + i + ")"
                            + fileName.substring(fileName.lastIndexOf(".")));
                }
                if (i != 0) {
                    fileName = fileName.substring(0, fileName.lastIndexOf("."))
                            + "(" + i + ")" +
                            fileName.substring(fileName.lastIndexOf("."));
                }
                targetPath = new File(fileName).toPath();
            } else {
                File temp = new File(targetDirectory + "\\" + fileName);
                while (temp.exists()) {
                    i++;
                    temp = new File(targetDirectory + "\\" + fileName.substring(0, fileName.lastIndexOf(".")) + "(" + i + ")." + fileName.substring(fileName.lastIndexOf(".") + 1));
                }
                if (i != 0) {
                    fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "(" + i + ")." + fileName.substring(fileName.lastIndexOf(".") + 1);
                }
                targetPath = new File(targetDirectory + File.separator + fileName).toPath();
            }
            Files.copy(url.openStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return targetPath;
        }
    }
}
