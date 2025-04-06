package minicap.concordia.campusnav.buildingmanager.entities;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;

public class BuildingNameTest {

    @Test
    public void testGetResourceName() {
        Assert.assertEquals("BuildingHall", BuildingName.HALL.getResourceName());
        assertEquals("BuildingJMSB", BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName());
        assertEquals("BuildingVL", BuildingName.VANIER_LIBRARY.getResourceName());
        assertEquals("BuildingVE", BuildingName.VANIER_EXTENSION.getResourceName());
        assertEquals("BuildingCC", BuildingName.LOYOLA_CENTRAL_BUILDING.getResourceName());
    }
}
