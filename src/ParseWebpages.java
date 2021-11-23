
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;

/**
 * This class is used to parse downloaded webpages, creating and initializing webpage objects. 
 *
 */
public class ParseWebpages {
	private String dir;
	private ArrayList<Webpage> webpages = new ArrayList<Webpage>();

	public ParseWebpages(String dir) {
		this.dir = dir;
		initWebPages();
		initCurPageRanks();
		initOutLinks();
		initInLinks();
	}
	
	/**
	 * Parses and initializes webpage objects. Only sets temporary outlinks of each webpage.
	 */
	public void initWebPages() {
		File folder = new File(dir);
		File[] listOfFiles = folder.listFiles();
		String filePath = "";
		String url = "";
		int num = 1;
		
		for (File file : listOfFiles) {
			filePath = folder + "/" + file.getName();
			File webPageFile = new File(filePath);
			// Get the url of the current webpage
			url = getURL(webPageFile);
			// Create a webpage object with the url
			Webpage wp = new Webpage(url);
			wp.setNum(num);
			num++;
			ArrayList<String> tempOutLinks = new ArrayList<String>();
			try {
				Document doc = Jsoup.parse(webPageFile, "UTF-8", "");
				Elements outLinks = doc.select("a[href]");
				for (Element page : outLinks) {
					String temp = page.attr("abs:href");
					// Remove white spaces
					temp = temp.replaceAll("\\s+", "");
					// Only add non-empty links
					if (!temp.equals("")) {
						tempOutLinks.add(page.attr("abs:href"));
					}
				}
				wp.setTempOutLinks(tempOutLinks);
			} catch (IOException e) {
				System.out.println(e);
			}
			webpages.add(wp);
		}
	}
	
	public ArrayList<Webpage> getWebpages() {
		return webpages;
	}
	
	/**
	 * Sets actual and valid outlinks of a webpage based on downloaded webpages.
	 * @return
	 */
	public void initOutLinks() {
		for (int i = 0; i < webpages.size(); ++i) {
			webpages.get(i).setOutLinks(webpages);
		}
	}
	
	/**
	 * Finds inlinks of a webpage and sets them as the inlinks of the webpage.
	 */
	public void initInLinks() {
		for (int i = 0; i< webpages.size(); ++i) {
			ArrayList<Webpage> inLinks = new ArrayList<>();
			for (int j = 0; j < webpages.size(); ++j) {
				if (webpages.get(j).isOutLink(webpages.get(i))) {
					inLinks.add(webpages.get(j));
				}
			}
			webpages.get(i).setInLinks(inLinks);
		}
	}
	
	/**
	 * Initializes current PageRank for every webpage. Initial vaule = 1/(total number of webpages)
	 */
	public void initCurPageRanks() {
		int total = webpages.size();
		for (int i = 0; i < total; ++i) {
			webpages.get(i).setCurPageRank(1.0/total);
		}
	}
	
	/**
	 * Gets the url of the webpage, which is in the first line of the file within comment tag.
	 * @param file
	 * @return url of the webpage
	 */
	public String getURL(File file) {
		String url = "";
		try {
			Scanner myReader = new Scanner(file);
			url = myReader.nextLine();
			// Remove comment tag
			url = url.replace("<!--", "");
			url = url.replace("-->", "");
			// Remove white spaces
			url = url.replaceAll("\\s+", "");
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		return url;
	}
	
	/**
	 * Prints information of all the webpages.
	 */
	public void printInfo() {
		System.out.println("----------------------------------------------------------------");
		for (int i = 0; i < webpages.size(); ++i) {
			System.out.println("URL: " + webpages.get(i).getUrl());
			System.out.println("Num: " + webpages.get(i).getNum());
			System.out.println("Current PageRank: " + webpages.get(i).getCurPageRank());
			System.out.println("Next PageRank: " + webpages.get(i).getNextPageRank());
			System.out.println("Number of outlinks: " + webpages.get(i).getOutLinksSize());
			System.out.println("Number of inlinks: " + webpages.get(i).getInLinksSize());
			System.out.println("----------------------------------------------------------------");
		}
	}
	
	/**
	 * Prints information of all the webpages with their inlinks and outlinks.
	 */
	public void printInOutLinks() {
		System.out.println("Number of Webpages: " + webpages.size());
		System.out.println("----------------------------------------------------------------");
		for (int i = 0; i < webpages.size(); ++i) {
			System.out.println("URL: " + webpages.get(i).getUrl());
			System.out.println("Num: " + webpages.get(i).getNum());
			System.out.println("Current PageRank: " + webpages.get(i).getCurPageRank());
			System.out.println();
			webpages.get(i).printOutLinks();
			System.out.println();
			webpages.get(i).printInLinks();
			System.out.println("----------------------------------------------------------------");
		}
	}
	
}
