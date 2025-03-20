package minicap.concordia.campusnav.helpers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import minicap.concordia.campusnav.components.ShuttleSchedule;

public class ShuttleScraper {

    public static List<ShuttleSchedule> fetchSchedule() {
        List<ShuttleSchedule> schedules = new ArrayList<>();

        try {
            // Fetch the HTML content of the page
            Document doc = Jsoup.connect("https://www.concordia.ca/maps/shuttle-bus.html#depart").get();

            // Select the tables for Monday-Thursday and Friday
            Elements tables = doc.select("table");

            // Extract data for Monday-Thursday
            Element mondayThursdayTable = tables.get(0);
            List<String> loyolaDeparturesMT = new ArrayList<>();
            List<String> sgwDeparturesMT = new ArrayList<>();
            extractTableData(mondayThursdayTable, loyolaDeparturesMT, sgwDeparturesMT);

            // Extract data for Friday
            Element fridayTable = tables.get(1);
            List<String> loyolaDeparturesFriday = new ArrayList<>();
            List<String> sgwDeparturesFriday = new ArrayList<>();
            extractTableData(fridayTable, loyolaDeparturesFriday, sgwDeparturesFriday);

            // Create ShuttleSchedule objects
            schedules.add(new ShuttleSchedule("Monday-Thursday", "Loyola", loyolaDeparturesMT));
            schedules.add(new ShuttleSchedule("Monday-Thursday", "SGW", sgwDeparturesMT));
            schedules.add(new ShuttleSchedule("Friday", "Loyola", loyolaDeparturesFriday));
            schedules.add(new ShuttleSchedule("Friday", "SGW", sgwDeparturesFriday));

        } catch (IOException e) {}
        return schedules;
    }

    private static void extractTableData(Element table, List<String> loyolaDepartures, List<String> sgwDepartures) {
        Elements rows = table.select("tr");
        for (Element row : rows) {
            Elements columns = row.select("td");
            if (columns.size() >= 2) {
                loyolaDepartures.add(columns.get(0).text());
                sgwDepartures.add(columns.get(1).text());
            }
        }
    }
}
