
import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.io.PrintWriter;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.net.HttpURLConnection;
import java.lang.String;
import java.net.URL;
import java.io.InputStream;
import java.util.Scanner;
import java.net.URLEncoder;
import java.io.File;

public class Crawler {

	// creates hash map for all the links starting from seed and so on
		// and records the url and its outlinks
		private HashMap<String, Integer> links;
		
		// sets max depth to crawl through
		private static final int MAX_DEPTH = 2;
		
		// constructor
		public Crawler() {
			links = new HashMap<String, Integer>();
		}
		
		// returns the full hash map of all the links the crawler crawled through
		public HashMap<String, Integer> getPageLinks() {
			return links;
		}
		
		public void getLinks(String URL, int depth) {
			// Checks if the URL has already been crawled
			// if the link is not in the hash table, add the link in the table
			if ((!links.containsKey(URL) && (depth < MAX_DEPTH))) {
				try {
					// Fetch HTML code and parse it to find all the outlinks
					// on the current URL
					Document document = Jsoup.connect(URL).get();
					Elements pageLinks = document.select("a[href]");
					
					// increments depth each time after finding all the
					// outlinks from the current url
					depth++;
					// prints out current URL name
					System.out.println(">> URL: " + URL);
					// puts current URL and its outlinks into hash map
					Integer i = Integer.valueOf(pageLinks.size());
					links.put(URL, i);
					
					
					// Connects to current URL and parses it to read through
					// the contents of the page
					Connection.Response response = Jsoup.connect(URL).execute();
					Document responseDoc = response.parse();
					
					// Writes a html file of the current URL into repository folder
					String language = getLang(document.body().text().substring(0, Math.min(75, document.body().text().length())).replace(".", " "));
					if (!language.equals("English")) {
						System.out.println("it's not english");
						System.out.println(language);
						int x = 5;
						x = 6;
					}
					System.out.println(language);
					
					String current_repo_path = "./repository/"+ language;
					File directory_check = new File (current_repo_path);
					if(!directory_check.isDirectory()){
					  directory_check.mkdir();
					}
					FileWriter myWriter = new FileWriter("./repository/" + language + "/" 
														+ document.title().replace(" ", "").replace(":", "").replace("|", "").replace(";", "")+ ".html", true);
					myWriter.write(responseDoc.outerHtml());
					myWriter.close();
					System.out.println("Successfully downloaded page");
					
					
					// for loop loops through each outlink found from the url and
					// recursively finds more outlinks 
					// filter links with "cpp" in the url
					for (Element page : pageLinks) {
						String url = page.attr("abs:href");
                                        	CharSequence seq = "cpp";
                                        	if (url.contains(seq))
							getLinks(page.attr("abs:href"), depth);
					}
				} catch (IOException e) {
					// prints out error if the url CANNOT be crawled
					// because of politeness policy or error 
					System.err.println("For '" + URL + "': " + e.getMessage());
				}
			}
		}
		
		// function that detects language
		public String getLang(String inputText){
	        String languagelayerendpoint = "http://api.languagelayer.com/detect?access_key=%s&query=%s";
	        String APIAccessKey = "e070762b63656bc7f826bc86e9be9b5e";
	        String newInputText = "";
	        try{
	           newInputText = URLEncoder.encode(inputText,"UTF-8");
	        }
	        catch(Exception e){
	           e.getMessage();
	        }
	        
	        String Request = String.format(languagelayerendpoint,APIAccessKey,newInputText);
	        String language = "";
	        try{
	            URL url = new URL(Request);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            InputStream is = connection.getInputStream();
	            
	            String text = null;
	            try (Scanner scanner = new Scanner(is)) {
	                text = scanner.useDelimiter("\\A").next();
	            }
	            is.close();
	            connection.disconnect();
	            // Finally we have the response
	            System.out.println(text);
	            String regex = "\"language_name\":\\s*\"\\w+\"";
	            Pattern pattern = Pattern.compile(regex);
	            Matcher matcher = pattern.matcher(text);
	            while (matcher.find()) {
	              System.out.print("Start index: " + matcher.start());
	              System.out.print(" End index: " + matcher.end());
	              System.out.println(" Found: " + matcher.group());
	              String lang1 = text.substring(matcher.start(),matcher.end());
	              language = (lang1.replaceAll("(\"|\\s)","").split(":")[1]);
	           }
	        } catch (Exception e){
	            e.printStackTrace();
	            System.out.print(e.getMessage());
	            language = "Unknown";
	        } 
	        
	        return language;
	     }

		
		public static void main(String[] args) {
			Crawler wc = new Crawler();
			// set seed URL
			wc.getLinks("https://www.cpp.edu/", 0);
			
			// writes URL and outlinks to csv file
			File csvFile = new File("report.csv");
			try (PrintWriter cFile = new PrintWriter(csvFile)) {
				Map<String, Integer> links = wc.getPageLinks();
				for (Map.Entry<String, Integer> page : links.entrySet()) {
					cFile.append(page.getKey() + ", " + page.getValue() + "\n");
				}
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}

}
