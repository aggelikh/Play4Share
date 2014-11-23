
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
        String start, end, videoUrl = null;
        
        // 1st attempt
        start = "data-videoid=\"";
        if (pageSource.contains(start)) {
            videoUrl = pageSource.substring(pageSource.indexOf(start));
            end = "\" ";
            if(videoUrl != null)
            videoUrl = videoUrl.substring(videoUrl.indexOf(start) + 14,
                    videoUrl.indexOf(end));
        }
        
        // 2nd attempt
        if (videoUrl == null) {
            start = ".youtube.com/embed/";
            if (pageSource.contains(start)) {
                videoUrl = pageSource.substring(pageSource.indexOf(start));
                end = "\" ";
                if(videoUrl != null)
                videoUrl = videoUrl.substring(videoUrl.indexOf(start) + 19,
                        videoUrl.indexOf(end));
            }
        }

        // 3rd attempt
        if (videoUrl == null) {
            start = ".youtube.com/watch?v=";
            if (pageSource.contains(start)) {
                videoUrl = pageSource.substring(pageSource.indexOf(start));
                end = "\" ";
                if(videoUrl != null)
                videoUrl = videoUrl.substring(videoUrl.indexOf(start) + 21,
                        videoUrl.indexOf(end));
            }
        }
        return videoUrl;
    }
}
