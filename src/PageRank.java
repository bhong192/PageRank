

import java.util.ArrayList;
import java.util.Collections;


// class used to perform PageRank. It find the most important pages of the downloaded urls in the respective repos.

public class PageRank {
	private ArrayList<Webpage> webpages;
	// The total number of webpages
	private int n;
	// constant to used for PageRank (with teleporting) calculation
	private final double LAMDA = .15; 
	
	public PageRank(ArrayList<Webpage> webpages) {
		this.webpages = webpages;
		n = this.webpages.size();
	}
	
	
	public void calculate() {
//		int iteration = 0;
		ArrayList<Webpage> inlinks;
		double pr;
		boolean converged = false;
//		System.out.println("Initial");
//		printInfo();
		// Calculate PageRank of each webpage until convergence (no change in the PageRank value of each webpage) 
		while (!converged) {
//			System.out.println("Iteration: " + (iteration+1));
			for (Webpage wp : webpages) {
				pr = 0.0;
				// Only the webpages with 1 or more inlinks
				if (wp.getInLinksSize() > 0) {
					inlinks = wp.getInLinks();
					for (Webpage inlink : inlinks) {
						pr = pr + inlink.getCurPageRank()/inlink.getOutLinksSize();
					}
					pr = LAMDA/n + (1.0-LAMDA)*pr;
					wp.setNextPageRank(pr);
				// PageRank of webpages with 0 inlinks = Lamda/n
				} else if (wp.getInLinksSize() == 0) {
					pr = LAMDA/n;
					wp.setNextPageRank(pr);
				}
			}
//			printInfo();
			// If no covergence, set the next PageRank as the current PageRank
			if (!isConvergence()) {
				updateCurPageRank();
			} else {
				converged = true;
			}
//			iteration++;
		}
	}
	
	/**
	 * Sets the next PageRank as the current PageRank.
	 */
	public void updateCurPageRank() {
		for (Webpage wp : webpages) {
			wp.setCurPageRank(wp.getNextPageRank());
		}
	}
	
	/**
	 * Checks if it converged
	 * @return True if no change in PageRank for every webpage; otherwise, False
	 */
	public boolean isConvergence() {
		boolean converged = true;
		for (Webpage wp : webpages) {
			if (!wp.isConvergence()) {
				converged = false;
			}
		}
		return converged;
	}
	
	/**
	 * Sort the webpages in descending PageRank order 
	 */
	public void sort() {
		Collections.sort(webpages);
	}
	
	/**
	 * Prints the result
	 */
	public void printResult() {
		System.out.println("Final Result");
		System.out.println("----------------------------------------------------");
		for (Webpage wp : webpages ) {
			System.out.println("URL: " + wp.getUrl());
			System.out.println("Current PageRank: " +wp.getCurPageRank());
			System.out.println("Number of Outlinks: " + wp.getOutLinksSize());
			System.out.println("Number of Inlinks: " + wp.getInLinksSize());
			System.out.println("-----------------------------------------------------");
		}
	}
	
}
