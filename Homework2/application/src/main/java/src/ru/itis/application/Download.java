package src.ru.itis.application;

import main.java.ru.itis.urlDownloader.Downloader;

import java.util.Scanner;

public class Download {
    public static void main(String[] args) throws IllegalAccessException {
        String[] strs = args;
        Downloader[] downloader = new Downloader[strs.length];
        for (int i = 0; i < strs.length; i++) {
            downloader[i] = new Downloader(strs[i], "");
            downloader[i].download();
        }
        for (int i = 0; i < downloader.length; i++) {
            try {
                downloader[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("That just happened");
                throw new IllegalAccessException();
            }
        }
    }

}
