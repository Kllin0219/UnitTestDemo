package com.jimmy.test.demo;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UtilsTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void dpToPx() {
        int result = Utils.dpToPx(120);
        int expected = 12;

        assertEquals(expected, result);
    }

    @After
    public void tearDown() throws Exception {
    }
}