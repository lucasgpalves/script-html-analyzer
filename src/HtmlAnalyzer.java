import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class HtmlAnalyzer {
    
    public static List<String> fetch(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);
        if (responseCode != HttpURLConnection.HTTP_OK) {
			throw new IOException("GET request did not work.");
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        
        String inputLine;
        List<String> lines = new ArrayList<>();

        while ((inputLine = in.readLine()) != null) {
            lines.add(inputLine.trim());
        }
        in.close();

        return lines;
    }

    public static String analyzeHtml(List<String> lines) {
        for (String line : lines) {
            if (line.isEmpty()) continue;
        }


        return "Malformed Html";
    }

    public static void main(String[] args) throws Exception {
        String url = args[0];

        HtmlAnalyzer.fetch(url);
    }
}
