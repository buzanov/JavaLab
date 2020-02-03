package homework1;


public class Main {
    public static void main(String[] args) {
        try {
            Structure.createFolder("downloaded");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("Folder error.");
        }
        if (args.length == 0) {
            System.exit(0);
        }
        Downloader[] downloaders = new Downloader[args.length];
        for (int i = 0; i < args.length; i++) {
            downloaders[i] = new Downloader(args[i], "downloaded");
            downloaders[i].start();
        }
        for (int j = 0; j < args.length; j++) {
            try {
                downloaders[j].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}
