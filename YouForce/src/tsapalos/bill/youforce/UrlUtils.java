
package tsapalos.bill.youforce;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class UrlUtils {
    public static String getRawPageUrl(String facebookUrl) {
        String start = ".php?u=";
        String end = "&h=";
        if (!(facebookUrl.contains(start) && facebookUrl.contains(end))) {
            // clean the stripped url from page pointers
            if (facebookUrl.lastIndexOf("/#") != -1)
                facebookUrl = facebookUrl.replaceAll("/#", "/");
            return facebookUrl;
        }
        String rawURL = facebookUrl.substring(facebookUrl.indexOf(start) + 7,
                facebookUrl.indexOf(end));
        rawURL = rawURL.replaceAll("%3A", ":");
        rawURL = rawURL.replaceAll("%2F", "/");
        return rawURL;
    }

    public static String getHtmlSource(String pageUrl) throws IllegalStateException, IOException {
        HttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
        HttpGet httpget = new HttpGet(pageUrl); // Set the action you want to do
        HttpResponse response = httpclient.execute(httpget); // Executeit
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent(); // Create an InputStream with the
                                              // response
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null)
            // Read line by line
            sb.append(line + "\n");

        String resString = sb.toString(); // Result is here

        is.close();
        return resString;
    }

    public static String exportVideoUrl(String pageSource) {
        String[] start = new String[] {
                "data-videoid=\"", ".youtube.com/embed/", ".youtube.com/watch?v="
        };
        int[] start_advance = new int[] {
                14, 19, 21
        };
        String end = "\" ", videoUrl = null;

        int i = 0;
        while (i < start.length) {
            if (pageSource.contains(start[i])) {
                videoUrl = pageSource.substring(pageSource.indexOf(start[i]));
                if (videoUrl != null)
                    videoUrl = videoUrl.substring(videoUrl.indexOf(start[i]) + start_advance[i],
                            videoUrl.indexOf(end));
                if (videoUrl != null)
                    break;
            }
            i++;
        }
        return videoUrl;
    }
}
