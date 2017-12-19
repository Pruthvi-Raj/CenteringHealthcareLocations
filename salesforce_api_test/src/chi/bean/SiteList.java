package chi.bean;

import java.util.ArrayList;

public class SiteList {
	
	ArrayList<Site> sitesList;
	
	
	
public SiteList(){
	this.sitesList=new ArrayList<Site>();
}
	public ArrayList<Site> getSitesList() {
		return sitesList;
	}

	public void setSitesList(ArrayList<Site> sitesList) {
		this.sitesList = sitesList;
	}
	
	public Site addSite(){
		Site site = new Site();
		sitesList.add(site);
		return site;
	}
	
}
