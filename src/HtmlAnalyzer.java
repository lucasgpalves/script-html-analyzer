import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HtmlAnalyzer {

    private static final Pattern TAG_PATTERN = Pattern.compile("<(/?\\w+)[^>]*>");
    private static final Pattern TEXT_PATTERN = Pattern.compile("^\\s*([^<]+)\\s*$");
    private static final String MALFORMED_HTML = "malformed HTML";
    private static final String URL_CONNECTION_ERROR = "URL connection error";
    
    private static List<String> fetchHtmlContent(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
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

    private static String analyzeHtml(List<String> lines){
        int deepestLevel = -1;
        String deepestText = "";
        Stack<String> stack = new Stack<>();
        
        for (String line : lines) {
            if (line.isEmpty()) continue;
        
            Matcher tags = TAG_PATTERN.matcher(line);
            Matcher text = TEXT_PATTERN.matcher(line);

            while (tags.find()) {
                String tag = tags.group(1);
                if (tag.startsWith("/")) {
                    if (stack.isEmpty() || !stack.peek().equals(tag.substring(1))) {
                        return MALFORMED_HTML;
                    }
                    stack.pop();
                } else {
                    stack.push(tag);
                }
            }

            while (text.find()) {
                String textHTML = text.group(1);
                if (!textHTML.isEmpty()) {
                    int level = stack.size();
                    if (level > deepestLevel) {
                        deepestLevel = level;
                        deepestText = textHTML;
                    }
                }
            } 
        }

        if (!stack.isEmpty()) return MALFORMED_HTML;  
        return deepestText;
    }

    public static void main(String[] args) throws Exception {
        String url = args[0];

        try {
            List<String> html = fetchHtmlContent(url);
            System.out.println( analyzeHtml(html));
        } catch (IOException e) {
            System.out.println(URL_CONNECTION_ERROR);
        }
    }
}
