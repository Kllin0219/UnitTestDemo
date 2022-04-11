package com.jimmy.test.demo;

import static org.junit.Assert.*;

import org.junit.Test;


public class DoorCommandTest {

    public static class StubOpenDoor implements IDoor {
        @Override
        public String getStatus() {
            return "open";
        }
    }

    public static class StubClosedDoor implements IDoor {
        @Override
        public String getStatus() {
            return "not open";
        }
    }

    @Test
    public void testExecuteDoorCommandWhenDoorOpen() {
        // Test stub - 回傳固定值的實作
        DoorCommand doorCommand = new DoorCommand(new StubOpenDoor());
        Result result = doorCommand.execute();
        assertEquals(Result.CRITICAL, result.getStatus());
        assertTrue(result.getMessage().startsWith("Door is open"));
    }

    @Test
    public void testExecuteDoorCommandWhenDoorClosed() {
        DoorCommand doorCommand = new DoorCommand(new StubClosedDoor());
        Result result = doorCommand.execute();
        assertEquals(Result.OK, result.getStatus());
        assertTrue(result.getMessage().startsWith("OK"));
    }
}
