package org.sergeys.library.rss;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Based on http://www.vogella.de/articles/RSSFeed/article.html
 *
 * @author sergeys
 *
 */
public class Feed {
    final String title;
    final String link;
    final String description;
    final String language;
    final String copyright;
    final Date pubDate;
    final List<FeedMessage> entries = new ArrayList<FeedMessage>();

    public Feed(String title, String link, String description, String language,
            String copyright, Date pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.language = language;
        this.copyright = copyright;
        this.pubDate = pubDate;
    }

    public List<FeedMessage> getMessages() {
        return entries;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public String getCopyright() {
        return copyright;
    }

    public Date getPubDate() {
        return pubDate;
    }

    @Override
    public String toString() {
        return "Feed [copyright=" + copyright + ", description=" + description
                + ", language=" + language + ", link=" + link + ", pubDate="
                + pubDate + ", title=" + title + "]";
    }
}
