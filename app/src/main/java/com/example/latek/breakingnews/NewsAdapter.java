package com.example.latek.breakingnews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//GOT THE MAIN FRAME FROM THE PREVIOUS WORKS
public class NewsAdapter extends ArrayAdapter<NewsKeys> {
    private List<NewsKeys> currentNews;
    public NewsAdapter(Context context, List<NewsKeys> news) {
        super(context, 0, news);
        currentNews = news;
    }
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list, parent, false);
        }
        if (currentNews != null) {
            NewsKeys news = currentNews.get(position);
            TextView newsTitle = (TextView) listItemView.findViewById(R.id.news_title);
            newsTitle.setText(news.getnTitle());
            TextView contributor = (TextView) listItemView.findViewById(R.id.author_text);
            contributor.setText(news.getnAuthor());
            TextView newsDate = (TextView) listItemView.findViewById(R.id.news_date);
            newsDate.setText(news.getnDate());
            TextView newsType = (TextView) listItemView.findViewById(R.id.news_type);
            newsType.setText(news.getnType());
      //      TextView newsSum = (TextView) listItemView.findViewById(R.id.news_summary);
      //      newsSum.setText(news.getnSum());
            //I wanted to put summary of the text. wanted to get first few lines of news
            //Could not achieve it. I need help with that too.
        }
        return listItemView;
    }
}
