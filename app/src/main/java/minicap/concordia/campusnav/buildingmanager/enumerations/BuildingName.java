package minicap.concordia.campusnav.buildingmanager.enumerations;

public enum BuildingName {
    HALL,
    MOLSON_SCHOOL_OF_BUSINESS,
    VANIER_LIBRARY,
    VANIER_EXTENSION,
    LOYOLA_CENTRAL_BUILDING;

    /**
     * Gets the resource name for the current value of enum
     * @return A string used for reading resource bundles
     */
    public String getResourceName() {
        switch(this) {
            case HALL:
                return "BuildingHall";
            case MOLSON_SCHOOL_OF_BUSINESS:
                return "BuildingJMSB";
            case VANIER_LIBRARY:
                return "BuildingVL";
            case LOYOLA_CENTRAL_BUILDING:
                return "BuildingCC";
            case VANIER_EXTENSION:
                return "BuildingVE";
            default:
                return "Unknown";
        }
    }
}
