
import java.util.*;

// helper class represents a webpage. Implements Comparable to sort webpages in decreasing current PageRank order.

public class Webpage implements Comparable<Webpage> {
	//To easily identify a webpage
	private int num;
	// Current PageRank of a webpage; initial value = 1/(total number of webpages)
	private double curPageRank;
	private double nextPageRank = 0.0;
	private String url;
	// Initially have all outlinks of a webpage; later use hashmap to check if downloaded webpage is actually one of the outlinks of a webpage
	private HashMap<String, Integer> tempOutLinks = new HashMap<>();
	private ArrayList<Webpage> outLinks = new ArrayList<Webpage>();
	private ArrayList<Webpage> inLinks = new ArrayList<Webpage>();
	
	public Webpage(String url) {
		this.url = url;
	}
	
	public int getNum() {
		return num;
	}
	
	public void setNum(int num) {
		this.num = num;
	}

	public double getCurPageRank() {
		return curPageRank;
	}

	public void setCurPageRank(double curPageRank) {
		this.curPageRank = curPageRank;
	}

	public double getNextPageRank() {
		return nextPageRank;
	}

	public void setNextPageRank(double nextPageRank) {
		this.nextPageRank = nextPageRank;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ArrayList<Webpage> getOutLinks() {
		return outLinks;
	}

	public void setOutLinks(ArrayList<Webpage> outLinks) {
		for (int i = 0; i < outLinks.size(); ++i) {
			// If a url is one of temp outlinks, add to the actual outlink list.
			if (tempOutLinks.containsKey(outLinks.get(i).getUrl())) {
				this.outLinks.add(outLinks.get(i));
			}
		}
	}
	
	public void printOutLinks() {
		System.out.println("Number of Outlinks: " + outLinks.size());
		for (int i = 0; i < outLinks.size(); ++i) {
			System.out.println(outLinks.get(i).getUrl());
		}
	}
	
	public void printInLinks() {
		System.out.println("Number of Inlinks: " + inLinks.size());
		for (int i = 0; i < inLinks.size(); ++i) {
			System.out.println(inLinks.get(i).getUrl());
		}
	} 

	public ArrayList<Webpage> getInLinks() {
		return inLinks;
	}

	public void setInLinks(ArrayList<Webpage> inLinks) {
		this.inLinks = inLinks;
	}
	
	public HashMap<String, Integer> getTempOutLinks() {
		return tempOutLinks;
	}
	
	public void setTempOutLinks(ArrayList<String> tempOutLinks) {
		for (int i = 0; i < tempOutLinks.size(); ++i) {
			this.tempOutLinks.put(tempOutLinks.get(i), i);
		}
	}
	
	public boolean isOutLink(Webpage wp) {
		return outLinks.contains(wp);
	}
	
	public boolean isInLink(Webpage wp) {
		return inLinks.contains(wp);
	}
	
	public boolean isConvergence() {
		return curPageRank == nextPageRank;
	}
	
	public int getInLinksSize() {
		return inLinks.size();
	}
	
	public int getOutLinksSize() {
		return outLinks.size();
	}

	@Override
	public int compareTo(Webpage wp) {
		double compareCurPr = wp.getCurPageRank();
		if (this.curPageRank == compareCurPr) {
			return 0;
		} 
		return this.curPageRank > compareCurPr ? -1 : 1;
	}


}
