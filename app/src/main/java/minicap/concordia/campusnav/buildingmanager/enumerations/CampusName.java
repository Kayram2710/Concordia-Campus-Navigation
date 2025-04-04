package minicap.concordia.campusnav.buildingmanager.enumerations;

public enum CampusName {
    SGW,
    LOYOLA;

    /**
     * Gets a string representing the resource name of this campus
     * @return String used in resource bundle to identify the campus
     */
    public String getResourceName() {
        switch(this) {
            case SGW:
                return "CampusSGW";
            case LOYOLA:
                return "CampusLoyola";
            default:
                return "Unknown";
        }
    }
}
