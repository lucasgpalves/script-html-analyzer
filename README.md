# HTML Analyzer

## Project Setup

### Clone the Repository

First, clone the repository to your local machine:

```sh
git clone https://github.com/lucasgpalves/script-html-analyzer.git
cd script-html-analyzer
```

## Create a Test HTML File
Create a directory named `test_html` and add a simple HTML file for testing:

```sh
mkdir test_html
cd test_html
```

Create a file named `test.html` with the following content:

```html
<!DOCTYPE html>
<html>
<head>
    <title>Test Title</title>
</head>
<body>
    <p>Test Body</p>
</body>
</html>
```

## Start a Local HTTP Server
Use Python's built-in HTTP server to serve the HTML file. Open a terminal and navigate to the `test_html` directory. Run the following command:

```sh
python3 -m http.server 8000
```

This will start a local server at `http://localhost:8000`.

## Compile the Java Program
Navigate back to the project root directory and compile the Java program:

```sh
cd ..
javac -d bin src/HtmlAnalyzer.java
```

## Run the Java Program
Run the Java program with the URL of the local server

```sh
java -cp bin HtmlAnalyzer http://localhost:8000/test.html
```
You should see the output `Test Title` as it is the deepest text in the HTML structure.

## Usage
To analyze an HTML file from a URL, use the following command:

```sh
java -cp bin HtmlAnalyzer <URL>
```

Replace `<URL>` with the URL of the HTML file you want to analyze.

## Error Handling
The program will output the following messages based on the situation:

- `malformed HTML`: If the HTML structure is not well-formed.
- `URL connection error`: If there is an issue connecting to the URL.

## Notes
- Ensure you have Java JDK 17 installed on your machine.
- The program does not handle HTML attributes or self-closing tags.
- The program assumes each line contains either a tag or text, not both.

Feel free to modify the HTML file and test the program with different HTML structures.