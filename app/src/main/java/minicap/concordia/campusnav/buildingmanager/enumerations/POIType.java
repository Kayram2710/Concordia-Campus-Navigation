package minicap.concordia.campusnav.buildingmanager.enumerations;

public enum POIType {
    WASHROOM("washroom"),
    STAIRS("stairs"),
    WATER_FOUNTAIN("water_fountain"),
    ELEVATOR("elevator"),
    RESTAURANT("restaurant"),
    COFFEE_SHOP("cafe"),
    CLASS_ROOM("classroom");

    private final String value;
    POIType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
