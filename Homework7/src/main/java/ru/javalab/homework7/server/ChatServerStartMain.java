package ru.javalab.homework7.server;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import ru.javalab.context.ApplicationContext;
import ru.javalab.context.ApplicationContextReflectionBased;

import java.util.ArrayList;
import java.util.List;

public class ChatServerStartMain {
    public static void main(String[] argv) {
        Args args = new Args();
        JCommander.newBuilder()
                .addObject(args)
                .build()
                .parse(argv);
        ApplicationContext applicationContext = ApplicationContextReflectionBased.getInstance();
        MultiClientServer multiClientServer = applicationContext.getComponent(MultiClientServer.class);
        multiClientServer.start(args.port);
    }

    @Parameters(separators = "=")
    public static class Args {
        @Parameter
        public List<String> parameters = new ArrayList<>();

        @Parameter(names = {"--port"})
        private Integer port = 6000;

        @Parameter(names = "--db-properties-path")
        private String dbPropFilePath = "db.properties";

    }
}
