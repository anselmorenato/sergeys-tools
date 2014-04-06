package org.sergeys.library.rss;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

/**
 * Based on http://www.vogella.de/articles/RSSFeed/article.html
 *
 * @author sergeys
 *
 */
public class RssFeedParser {
    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String CHANNEL = "channel";
    static final String LANGUAGE = "language";
    static final String COPYRIGHT = "copyright";
    static final String LINK = "link";
    static final String AUTHOR = "author";
    static final String ITEM = "item";
    static final String PUB_DATE = "pubDate";
    static final String GUID = "guid";

    final URL url;

    private InputStream is;
    private String simpleDateFormat;

    public RssFeedParser(String feedUrl) {
        try {
            this.url = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public RssFeedParser(InputStream is, String simpleDateFormat){
        url = null;
        this.is = is;
        this.simpleDateFormat = simpleDateFormat;
    }

    public Feed readFeed() throws XMLStreamException, ParseException {
        Feed feed = null;
        try {

            boolean isFeedHeader = true;
            // Set header values intial to the empty string
            String description = "";
            String title = "";
            String link = "";
            String language = "";
            String copyright = "";
            //String author = "";
            Date pubdate = new Date(0);
            String guid = "";

            // First create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = read();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // Read the XML document
            while (eventReader.hasNext()) {

                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    if (event.asStartElement().getName().getLocalPart().equals(ITEM)) {
                        if (isFeedHeader) {
                            isFeedHeader = false;
                            feed = new Feed(title, link, description, language,
                                    copyright, pubdate);
                        }
                        event = eventReader.nextEvent();
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals(TITLE)) {
                        event = eventReader.nextEvent();
                        title = event.asCharacters().getData();
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals(DESCRIPTION)) {
                        event = eventReader.nextEvent();
                        description = event.asCharacters().getData();
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals(LINK)) {
                        event = eventReader.nextEvent();
                        link = event.asCharacters().getData();
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals(GUID)) {
                        event = eventReader.nextEvent();
                        guid = event.asCharacters().getData();
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals(LANGUAGE)) {
                        event = eventReader.nextEvent();
                        language = event.asCharacters().getData();
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals(AUTHOR)) {
                        event = eventReader.nextEvent();
                        //author = event.asCharacters().getData();
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals(PUB_DATE)) {
                        event = eventReader.nextEvent();

                        String data = event.asCharacters().getData();

//                        try {
//                            //pubdate = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, new Locale("en")).parse(data);
//                            pubdate = new SimpleDateFormat(simpleDateFormat, new Locale("en")).parse(data);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }

                        pubdate = new SimpleDateFormat(simpleDateFormat, new Locale("en")).parse(data);
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals(COPYRIGHT)) {
                        event = eventReader.nextEvent();
                        copyright = event.asCharacters().getData();
                        continue;
                    }
                } else if (event.isEndElement()) {
                    if (event.asEndElement().getName().getLocalPart().equals(ITEM)) {
                        FeedMessage message = new FeedMessage();
                        //message.setAuthor(author);
                        message.setDescription(description);
                        message.setGuid(guid);
                        //message.setLink(link);
                        //message.setTitle(title);
                        message.setPubDate(pubdate);
                        feed.getMessages().add(message);
                        event = eventReader.nextEvent();
                        continue;
                    }
                }
            }
        }
//		catch (XMLStreamException e) {
//			throw new RuntimeException(e);
//		}
        finally{}

        return feed;
    }

    private InputStream read() {
        if(is == null && url != null){
            try {
                return url.openStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            return is;
        }
    }
}