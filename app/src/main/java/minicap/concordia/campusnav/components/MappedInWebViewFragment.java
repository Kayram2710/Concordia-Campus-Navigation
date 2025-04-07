package minicap.concordia.campusnav.components;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import minicap.concordia.campusnav.BuildConfig;
import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.helpers.HTMLAssetHelper;
import minicap.concordia.campusnav.helpers.UserLocationService;
import minicap.concordia.campusnav.map.MapCoordinates;

public class MappedInWebViewFragment extends Fragment implements UserLocationService.UserLocationUpdatedListener {

    private static final String MAPPED_IN_ASSET_NAME = "mappedIn.html";
    private static final String MAPPED_IN_KEY_STRING = "<MAPPED_IN_KEY>";
    private static final String MAPPED_IN_SECRET_STRING = "<MAPPED_IN_SECRET>";
    private static final String TAG = "MappedInWebViewFragment";

    private UserLocationService locationService;

    private MappedInMapEventListener curListener;
    private WebView map;

    public MappedInWebViewFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new instance of the MappedInWebViewFragment
     * @param listener Listener for map events
     * @return New MappedInWebViewFragment
     */
    public static MappedInWebViewFragment newInstance(MappedInMapEventListener listener) {
        MappedInWebViewFragment fragment = new MappedInWebViewFragment();
        fragment.curListener = listener;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mapped_in_web_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        map = view.findViewById(R.id.mapped_in_map);

        map.getSettings().setJavaScriptEnabled(true);
        map.getSettings().setGeolocationEnabled(true);
        map.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                curListener.mapPageLoaded();
            }
        });

        map.addJavascriptInterface(new AndroidJSListener(curListener), "Android");

        HTMLAssetHelper helper = new HTMLAssetHelper(view.getContext());

        String unencodedHtml = helper.getHtmlContent(MAPPED_IN_ASSET_NAME);

        unencodedHtml = unencodedHtml.replace(MAPPED_IN_KEY_STRING, BuildConfig.MAPPED_IN_KEY);
        unencodedHtml = unencodedHtml.replace(MAPPED_IN_SECRET_STRING, BuildConfig.MAPPED_IN_SECRET);

        String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(), Base64.NO_PADDING);

        map.loadData(encodedHtml, "text/html", "base64");
    }

    /**
     * Toggles location tracking on the map
     * @param isEnabled Whether to enable or disable the tracking
     */
    public void toggleLocationTracking(boolean isEnabled) {
        String[] args = new String[1];
        args[0] = String.valueOf(isEnabled);
        runJavascriptCommand("toggleBlueDot", args);

        startManualLocationTracking(this.getContext());
    }

    /**
     * Draws a path for navigation on the map
     * @param origin The starting point of the journey
     * @param end The destination of the journey
     * @param isAccessibility Flag for if this is an accessibility route
     */
    public void drawPath(MapCoordinates origin, MapCoordinates end, boolean isAccessibility) {
        String[] args = new String[7];
        args[0] = String.valueOf(origin.getLat());
        args[1] = String.valueOf(origin.getLng());
        args[2] = String.valueOf(origin.getName());
        args[3] = String.valueOf(end.getLat());
        args[4] = String.valueOf(end.getLng());
        args[5] = String.valueOf(end.getName());
        args[6] = String.valueOf(isAccessibility);

        runJavascriptCommand("drawPathWithCoordinates", args);
    }

    /**
     * Adds a marker on the webview map
     * @param coordinates The coordinates of the marker
     * @param title The title of the marker
     */
    public void addMarker(MapCoordinates coordinates, String title) {
        String[] args = new String[3];
        args[0] = String.valueOf(coordinates.getLat());
        args[1] = String.valueOf(coordinates.getLng());
        args[2] = title;

        runJavascriptCommand("addMarkerFromAndroid", args);
    }

    /**
     * Removes all markers from the map
     */
    public void removeAllMarkers() {
        runJavascriptCommand("removeAllMarkers");
    }

    /**
     * Starts the service to manually track location updates
     * This is necessary because the WebView is not able to use geolocation services (since it considers our page "insecure")
     * @param context The context for the fragment's view
     */
    private void startManualLocationTracking(Context context) {
        locationService = new UserLocationService(context, this);
        locationService.start(1000);
    }

    /**
     * Loads a map in the WebView
     * @param mapId The id of the map to load (from MappedIn)
     * @param floorId The id of the initial floor to load (from MappedIn)
     */
    public void loadMap(String mapId, String floorId) {
        String[] args = new String[2];
        args[0] = mapId;
        args[1] = floorId;
        runJavascriptCommand("loadMap", args);
    }

    /**
     * Switches the map to the specified floor
     * @param floorId The id of the floor to switch to (from MappedIn)
     */
    public void switchFloor(String floorId) {
        String[] args = new String[1];
        args[0] = floorId;

        runJavascriptCommand("switchFloor", args);
    }

    /**
     * Clears the navigation path from the map
     */
    public void clearPath() {
        runJavascriptCommand("clearPath");
    }

    /**
     * Manually updates the user's position on the map
     * @param coords The coordinates of the user
     */
    private void updateUserPosition(MapCoordinates coords) {
        String[] args = new String[2];
        args[0] = String.valueOf(coords.getLat());
        args[1] = String.valueOf(coords.getLng());

        runJavascriptCommand("updateBlueDotPosition", args);
    }

    /**
     * Runs a javascript method in the WebView without any parameters
     * @param methodName The method to invoke
     */
    private void runJavascriptCommand(String methodName) {
        Log.d(TAG, "Running command: " + methodName);
        map.post(new Runnable() {
            @Override
            public void run() {
                map.evaluateJavascript("javascript:" + methodName + "()", null);
            }
        });
    }

    /**
     * Runs a javascript method in the WebView with parameters
     * NOTE: All parameters will be converted to a string, so they have to be converted back in the HTML file
     * @param methodName The name of the method to invoke
     * @param args The arguments for the method
     */
    private void runJavascriptCommand(String methodName, String[] args) {
        StringBuilder argList = new StringBuilder();
        for(int i = 0; i < args.length - 1; i++) {
            argList.append("'");
            argList.append(args[i]);
            argList.append("', ");
        }
        argList.append("'");
        argList.append(args[args.length - 1]);
        argList.append("'");

        Log.d(TAG, "Running command: " + methodName + ", with params: " + argList);

        map.post(new Runnable() {
            @Override
            public void run() {
                map.evaluateJavascript("javascript:" + methodName + "(" + argList + ")", null);
            }
        });
    }

    @Override
    public void onUserLocationUpdated(MapCoordinates newPosition) {
        updateUserPosition(newPosition);
    }

    private class AndroidJSListener {

        private MappedInMapEventListener listener;

        public AndroidJSListener(MappedInMapEventListener listener) {
            this.listener = listener;
        }

        /**
         * Javascript interface that's called when the map is clicked
         * @param name The name of the clicked location
         * @param lat The latitude of the clicked location
         * @param lng The longitude of the clicked location
         */
        @JavascriptInterface
        public void mapClicked(String name, double lat, double lng) {
            MapCoordinates clicked = new MapCoordinates(lat, lng, name);

            listener.mapClicked(clicked);
        }

        /**
         * Javascript interface that is invoked when the map is loaded and ready to use
         */
        @JavascriptInterface
        public void mapReady() {
            listener.mapLoaded();
        }
    }

    public interface MappedInMapEventListener {

        /**
         * Method that is invoked when the web page has finished loading
         */
        void mapPageLoaded();

        /**
         * Method invoked when the map is ready for use
         */
        void mapLoaded();

        /**
         * Method invoked when the map was clicked
         * @param coords The coordinates of the place that was clicked
         */
        void mapClicked(MapCoordinates coords);
    }
}