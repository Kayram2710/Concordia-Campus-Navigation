package minicap.concordia.campusnav.helpers;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import minicap.concordia.campusnav.components.ShuttleSchedule;

// Scrapes shuttle schedule from Concordia website
public class ShuttleScraper {

    private static final String SHUTTLE_SCRAPER_TAG = "ShuttleScraper";

    private ShuttleScraper() {}

    /**
     * Fetches and parses the shuttle bus schedule
     * @return The schedule of the shuttle bus as a list of schedules
     */
    public static List<ShuttleSchedule> fetchSchedule() {
        List<ShuttleSchedule> schedules = new ArrayList<>();
        // Uses Jsoup for web scraping
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

        } catch (IOException e) {
            Log.e(SHUTTLE_SCRAPER_TAG, "Exception while fetching shuttle schedule: ", e);
        }

        return schedules;
    }

    /**
     * Helper function that extracts data from given table
     * @param table The table with the data to extract
     * @param loyolaDepartures The list of loyola departures to populate
     * @param sgwDepartures The list of SGW departures to populate
     */
    private static void extractTableData(Element table, List<String> loyolaDepartures, List<String> sgwDepartures) {
        // Parses HTML table rows/columns
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