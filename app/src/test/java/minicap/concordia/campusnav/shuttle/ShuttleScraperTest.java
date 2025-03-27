package minicap.concordia.campusnav.shuttle;

import org.junit.Test;
import java.util.List;

import static org.junit.Assert.*;

import minicap.concordia.campusnav.components.ShuttleSchedule;
import minicap.concordia.campusnav.helpers.ShuttleScraper;

public class ShuttleScraperTest {

    @Test
    public void testFetchScheduleReturnsNonEmptyList() {
        List<ShuttleSchedule> schedules = ShuttleScraper.fetchSchedule();

        // Basic checks (won't fail if website is down)
        assertNotNull(schedules);
        assertFalse(schedules.isEmpty());

        // Verify expected schedule types exist
        boolean hasLoyolaSchedule = schedules.stream()
            .anyMatch(s -> s.getCampus().equals("Loyola"));
        boolean hasSGWSchedule = schedules.stream()
            .anyMatch(s -> s.getCampus().equals("SGW"));

        assertTrue(hasLoyolaSchedule);
        assertTrue(hasSGWSchedule);
    }

    @Test
    public void testScheduleHasValidDayFields() {
        List<ShuttleSchedule> schedules = ShuttleScraper.fetchSchedule();
        // Check at least one schedule has a valid day label
        assertTrue(schedules.stream()
                .anyMatch(s -> s.getDay().equals("Monday-Thursday") || s.getDay().equals("Friday")));
    }
}