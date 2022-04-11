package com.jimmy.test.demo;

public class DoorCommand implements ICommand {
    private IDoor _door;

    public DoorCommand(IDoor door) {
        _door = door;
    }

    @Override
    public Result execute() {
        Result result;
        if ("open".equals(_door.getStatus())) {
            result = new Result(Result.CRITICAL, "Door is open: ");
        } else {
            result = new Result();
        }
        return result;
    }
}
