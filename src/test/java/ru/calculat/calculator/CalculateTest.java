package ru.calculat.calculator;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CalculateTest {

    @Test
    public void getAnsuer() throws Exception {
        Calculate calculate = new CalculateImpl();
        assertEquals(null, calculate.getAnsuer("(5.89*(6+56))-6/0"));
    }
    /*все тесты работают
    public void getAnsuer() throws Exception {
        Calculate calculate = new Calculate();
        assertEquals(null, calculate.getAnsuer("(5.89*(6+56))-r5"));
    }
    public void getAnsuer() throws Exception {
        Calculate calculate = new Calculate();
        assertEquals("363.98", calculate.getAnsuer("(5.89*(6+56))-6/5"));
    }
    public void getAnsuer() throws Exception {
        Calculate calculate = new Calculate();
        assertEquals("90.14", calculate.getAnsuer("(5.89*6+56)-6/5"));
    }*/
}