package com.example.latek.breakingnews;

public class NewsKeys {
    private String nAuthor;
    private String nTitle;
    private String nDate;
    private String nUrl;
    private String nType;
  //  private String nSum;
    public NewsKeys(String newsTitle, String typeOfNews, String date, String url, String contributor) {
        this.nTitle = newsTitle;
        this.nType = typeOfNews;
        this.nUrl= url;
        this.nDate =date;
        this.nAuthor = contributor;
   //     this.nSum = summary;
    }
    public String getnTitle() {
        return nTitle;
    }
    public String getnDate() {
        return nDate;
    }
    public String getnAuthor() {
        return nAuthor;
    }
    public String getnType() {
        return nType;
    }
    public String getnUrl() {
        return nUrl;
    }
  //  public String getnSum() {
  //      return nSum;
  //  }
    public void add(NewsKeys news) {
    }
}
