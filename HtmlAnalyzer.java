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
    private static final Pattern TEXT_PATTERN = Pattern.compile(">([^<]+)<");
    private static final String MALFORMED_HTML = "malformed HTML";
    private static final String URL_CONNECTION_ERROR = "URL connection error";
    private static final String NO_TEXT_FOUND = "No text found in the HTML.";

    /**
     * The main entry point for the HtmlAnalyzer program.
     *
     * @param args the command-line arguments; expects a single URL as input
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a URL as an argument.");
            return;
        }

        String url = args[0];

        try {
            List<String> htmlContent = fetchHtmlContent(url);
            String result = analyzeHtml(htmlContent);
            System.out.println(result);
        } catch (IOException e) {
            System.out.println(URL_CONNECTION_ERROR);
        }
    }

    /**
     * Fetches the HTML content from the given URL.
     *
     * @param url the URL to fetch HTML content from
     * @return a list of strings, each representing a line of HTML content
     * @throws IOException if an I/O error occurs
     */
    private static List<String> fetchHtmlContent(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("GET request did not work.");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            List<String> lines = new ArrayList<>();
            String inputLine;

            while ((inputLine = reader.readLine()) != null) {
                lines.add(inputLine.trim());
            }

            return lines;
        }
    }

    /**
     * Analyzes the HTML content to find the deepest nested text.
     *
     * @param lines the list of HTML content lines
     * @return the deepest nested text or an error message
     */
    private static String analyzeHtml(List<String> lines) {
        String deepestText = "";
        Stack<String> tagStack = new Stack<>();
        int deepestLevel = -1;

        for (String line : lines) {
            if (line.isEmpty()) continue;

            tagStack = updateTagStack(tagStack, line);
            String text = extractText(line);

            if (!text.isEmpty()) {
                int currentLevel = tagStack.size();
                if (currentLevel > deepestLevel) {
                    deepestLevel = currentLevel;
                    deepestText = text;
                }
            }
        }

        if (!tagStack.isEmpty()) {
            return MALFORMED_HTML;
        }

        return verifyDeepestText(deepestText);
    }

    /**
     * Updates the stack of HTML tags based on the given line.
     *
     * @param tagStack the current stack of HTML tags
     * @param line the current line of HTML content
     * @return the updated stack of HTML tags
     */
    private static Stack<String> updateTagStack(Stack<String> tagStack, String line) {
        Matcher matcher = TAG_PATTERN.matcher(line);

        while (matcher.find()) {
            String tag = matcher.group(1);
            if (tag.startsWith("/")) {
                if (!tagStack.isEmpty() && tagStack.peek().equals(tag.substring(1))) {
                    tagStack.pop();
                }
            } else {
                tagStack.push(tag);
            }
        }
        return tagStack;
    }

    /**
     * Extracts the text content from the given line.
     *
     * @param line the current line of HTML content
     * @return the extracted text content
     */
    private static String extractText(String line) {
        Matcher matcher = TEXT_PATTERN.matcher(line);
        return matcher.find() ? matcher.group(1).trim() : "";
    }

    /**
     * Verifies the deepest nested text.
     *
     * @param deepestText the deepest nested text found
     * @return the deepest nested text or a message indicating no text was found
     */
    private static String verifyDeepestText(String deepestText) {
        return deepestText.isEmpty() ? NO_TEXT_FOUND : deepestText;
    }
}