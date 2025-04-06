package minicap.concordia.campusnav.buildingmanager.enumerations;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CampusNameTest {

    @Test
    public void testGetResourceName() {
        assertEquals("CampusSGW", CampusName.SGW.getResourceName());
        assertEquals("CampusLoyola", CampusName.LOYOLA.getResourceName());
    }
}
