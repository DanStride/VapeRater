package com.upskilled.vaperater;

import org.junit.Test;

import static org.junit.Assert.*;

public class ReUseComponentTesting {
    @Test
    public void CreateNewJuiceTest() {
        JuiceInfo juice = new JuiceInfo(7, "Red Berry", "Mr Jellyfish", 1, "A fictional flavour of epic proportion", (float) 7.5);

        assertEquals(7, juice.getJuiceID());
        assertEquals("Red Berry", juice.getJuiceName());
        assertEquals("Mr Jellyfish", juice.getJuiceBrand());
        assertEquals(1 - 1, juice.getJuiceCategory());
        assertEquals("A fictional flavour of epic proportion", juice.getJuiceDescription());
        assertEquals("7.5", juice.getJuiceRating());
    }

}
