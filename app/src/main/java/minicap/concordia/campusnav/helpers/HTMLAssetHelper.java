package minicap.concordia.campusnav.helpers;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HTMLAssetHelper {
    private static final String TAG = "MappedInHTMLHelper";

    private Context context;

    public HTMLAssetHelper(Context context) {
        this.context = context;
    }

    /**
     * Gets the html content of an asset file as a string
     * @return Un-encoded string of html
     */
    public String getHtmlContent(String fileName) {
        BufferedReader in = null;
        try {
            StringBuilder htmlBuilder = new StringBuilder();
            InputStream is = context.getAssets().open(fileName);

            in = new BufferedReader(new InputStreamReader(is));
            String cur;
            boolean isFirstLine = true;
            while ((cur = in.readLine()) != null) {
                if(isFirstLine) {
                    isFirstLine = false;
                }
                else {
                    htmlBuilder.append("\n");
                }

                htmlBuilder.append(cur);
            }

            return htmlBuilder.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error while opening asset: " + fileName);
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error while closing asset: " + fileName);
                }
            }
        }

        return null;
    }
}
