package minicap.concordia.campusnav.shuttle;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

import minicap.concordia.campusnav.components.ShuttleSchedule;

public class ShuttleScheduleTest {

    @Test
    public void testConstructorAndGetters() {
        List<String> times = Arrays.asList("9:15", "9:30");
        ShuttleSchedule schedule = new ShuttleSchedule("Monday-Thursday", "Loyola", times);

        assertEquals("Monday-Thursday", schedule.getDay());
        assertEquals("Loyola", schedule.getCampus());
        assertEquals(times, schedule.getDepartureTimes());
    }
}
