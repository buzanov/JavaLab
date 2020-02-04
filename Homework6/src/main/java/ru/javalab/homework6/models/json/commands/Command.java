package ru.javalab.homework6.models.json.commands;

import ru.javalab.homework6.models.json.Payload;

public class Command extends Payload {
    private String command;
    private Object information;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Object getInformation() {
        return information;
    }

    public void setInformation(Object information) {
        this.information = information;
    }
}
