package com.jimmy.test.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Build.class, Utils.class, Server.class})
public class ServerTest {

    public class DummyCommand implements ICommand {
        @Override
        public Result execute() {
            throw new RuntimeException("UnImplement method");
        }
    }

    public class DummyAlert implements IAlert {
        @Override
        public void sentAlert(String message) {
            throw new RuntimeException("UnImplement method");
        }
    }

    public class SpyAlert implements IAlert {
        private boolean _sendAlert = false;


        @Override
        public void sentAlert(String message) {
            _sendAlert = true;
        }

        public boolean wasAlertSend() {
            return _sendAlert;
        }
    }

    public class FakeLogAlert implements IAlert {
        private PrintWriter _writer;
        private String _fileName;

        public FakeLogAlert(String fileName) {
            _fileName = fileName;
        }

        @Override
        public void sentAlert(String message) {
            try {
                _writer = new PrintWriter(_fileName);
                _writer.println(message);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close(_writer);
            }
        }

        private void close(Closeable c) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Before
    public void setUp() {
        // Mock static field
        Whitebox.setInternalState(Build.class, "TYPE", "userdebug");
    }

    @Test
    public void testCommandSize() {
        // Test Double - Dummy
        // ???????????????
        Server server = new Server(new DummyAlert());
        assertEquals(0, server.getCommandSize());

        server.addCommand(new DummyCommand());
        server.addCommand(new DummyCommand());

        assertEquals(2, server.getCommandSize());
    }

    @Test
    public void testMonitorSendAlertWhenDoorOpen() {
        // Test Double - Spy
        // ??????Stub,???????????????????????????????????????,?????????SUT???????????????????????????
        SpyAlert spyAlert = new SpyAlert();
        Server server = new Server(spyAlert);
        server.addCommand(new DoorCommand(new DoorCommandTest.StubOpenDoor()));
        server.monitor();
        assertTrue(spyAlert.wasAlertSend());
    }

    @Test
    public void testMonitorSendAlertWhenDoorClosed() {
        FakeLogAlert spyAlert = new FakeLogAlert("./fake_dooropen.txt");
        Server server = new Server(spyAlert);
        server.addCommand(new DoorCommand(new DoorCommandTest.StubClosedDoor()));
        server.monitor();
        // assert that the fake_dooropen.txt contains a message
    }

    @Test
    public void testMonitorSendAlertToFakeAlertWhenDoorOpen() {
        // Test Double - Fake
        // ??????????????????????????????????????????
        FakeLogAlert spyAlert = new FakeLogAlert("fake_doorclosed.txt");
        Server server = new Server(spyAlert);
        server.addCommand(new DoorCommand(new DoorCommandTest.StubOpenDoor()));
        server.monitor();
        // assert that the fake_doorclosed is empty
    }

    @Test
    public void testMonitorSendAlertToFakeAlertWhenDoorClosed() {
        SpyAlert spyAlert = new SpyAlert();
        Server server = new Server(spyAlert);
        server.addCommand(new DoorCommand(new DoorCommandTest.StubClosedDoor()));
        server.monitor();
        assertFalse(spyAlert.wasAlertSend());
    }

    @Test
    public void testMockWhenWindowBroken() {
        // Test Double - Mock
        // ???Mock?????????????????????,????????????Dummy???Stub???Spy?????????
        // ?????????Mock??????????????????????????????????????????????????????
        ICommand mockCmd = mock(ICommand.class);
        when(mockCmd.execute()).thenReturn(new Result(Result.CRITICAL, "Window is broken"));

        IAlert alert = mock(IAlert.class);

        Server server = new Server(alert);
        server.addCommand(mockCmd);
        server.monitor();

        verify(alert, times(1)).sentAlert("Window is broken");
    }

    @Test
    public void testMockWhenWindowClose() {
        ICommand mockCmd = mock(ICommand.class);
        when(mockCmd.execute()).thenReturn(new Result(Result.OK, "Window is close"));

        IAlert alert = mock(IAlert.class);

        Server server = new Server(alert);
        server.addCommand(mockCmd);
        server.monitor();

        verify(alert, times(0)).sentAlert("Window is close");
    }

    @Test
    public void testServerInfo() {
        // Mock static function
        PowerMockito.mockStatic(Utils.class);
        Mockito.when(Utils.isCNSku()).thenReturn(true);

        Server server = new Server(new DummyAlert());
        assertTrue(server.getServerInfo().endsWith("cn server"));

        Mockito.when(Utils.isCNSku()).thenReturn(false);
        assertTrue(server.getServerInfo().endsWith("ww server"));
    }

    @Test
    public void testIsCommandEmpty() throws Exception {
        // ?????? Whitebox.setInternalState(..) ??????????????????????????????????????????
        // ?????? Whitebox.getInternalState(..) ??????????????????????????????????????????
        // ?????? Whitebox.invokeMethod(..) ??????????????????????????????????????????
        // ?????? Whitebox.invokeConstructor(..) ??????????????????????????????????????????

        Server server = new Server(new DummyAlert());
        // Test private function
        assertTrue(Whitebox.invokeMethod(server, "isCommandEmpty"));

        server.addCommand(new DummyCommand());
        assertFalse(Whitebox.invokeMethod(server, "isCommandEmpty"));
    }
}