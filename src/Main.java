/**
 * 
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String dir = "uciEduRepo";
		ParseWebpages pwp = new ParseWebpages(dir);
		PageRank pr = new PageRank(pwp.getWebpages());
		pr.calculate();
		pr.sort();
		pr.printResult();
	}

}
