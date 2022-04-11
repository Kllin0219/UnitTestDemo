package com.jimmy.test.demo;

import android.os.Build;

import java.util.LinkedList;
import java.util.List;

public class Server {
    private static final boolean DEBUG = Build.TYPE.equalsIgnoreCase("userdebug");

    private List<ICommand> _commandsList;
    private IAlert _alert;

    public Server(IAlert alert) {
        _commandsList = new LinkedList<>();
        _alert = alert;
    }

    public void addCommand(ICommand command) {
        if (command != null) {
            _commandsList.add(command);
        }
    }

    public void removeCommand(ICommand command) {

    }

    public int getCommandSize() {
        return _commandsList.size();
    }

    public void monitor() {
        for (ICommand command : _commandsList) {
            Result result = command.execute();
            if(Result.OK != result.getStatus()) {
                _alert.sentAlert(result.getMessage());
            }
        }
    }

    public String getServerInfo() {
        String info;
        if(Utils.isCNSku()) {
            info = "cn server";
        } else  {
            info = "ww server";
        }
        return info;
    }

    private boolean isCommandEmpty() {
        return _commandsList.isEmpty();
    }
}
