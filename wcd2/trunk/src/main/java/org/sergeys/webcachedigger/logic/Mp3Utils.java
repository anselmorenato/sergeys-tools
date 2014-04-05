package org.sergeys.webcachedigger.logic;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.Set;

import javax.imageio.ImageIO;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

public class Mp3Utils {

    // http://download.oracle.com/javase/6/docs/technotes/guides/intl/encoding.doc.html
    private Charset charset8859_1 = null;
    private Charset charsetFixed = null;
    private boolean decodeStrings = false;

    private Properties charsetByLang;

    private static Mp3Utils instance;

    // singletion
    private Mp3Utils() {
//		try{
            charset8859_1 = Charset.forName("ISO-8859-1");
            charsetFixed = charset8859_1;

            InputStream is = null;
            charsetByLang = new Properties();
            try {
                //is = getClass().getResourceAsStream("/resources/charsetByLanguage.properties");
                is = Mp3Utils.class.getResourceAsStream("/resources/charsetByLanguage.properties");
                charsetByLang.load(is);
            } catch (IOException e1) {
                Settings.getLogger().error("", e1);
            }
            finally{
                if(is != null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        Settings.getLogger().error("", e);
                    }
                }
            }

//		}
//		catch(Exception ex){
//			SimpleLogger.logMessage("Cannot instantiate Charset for ISO-8859-1");
//		}

    }

    public static Mp3Utils getInstance(){
        return instance;
    }

    static{
        instance = new Mp3Utils();
    }

    /**
     * See http://download.oracle.com/javase/6/docs/technotes/guides/intl/encoding.doc.html
     * for encoding names
     *
     * @param encoding
     */
    protected void setDecodeCharset(String encoding){
        //charsetFixed = Charset.forName("windows-1251");
        charsetFixed = Charset.forName(encoding);
    }

    /**
     *
     * @param lang Language key as defined in charsetByLanguage.properties (two-letter name for Locale class)
     */
    public void setDecodeLanguage(String lang){
        if(charsetByLang.containsKey(lang)){
            setDecodeCharset(charsetByLang.getProperty(lang));
        }
    }

    public Set<Object> getDecodingLanguages(){
        return charsetByLang.keySet();
    }

    public String decode(String src){

        if(decodeStrings){
            ByteBuffer bb = charset8859_1.encode(src);
            String res = charsetFixed.decode(bb).toString();	// TODO: see EncodedText, seems trailing zero can occur
            return res;
        }
        else{
            return src;
        }

    }

    public Image getArtwork(File file){
        Mp3File mp3;
        try {
            mp3 = new Mp3File(file.getAbsolutePath());
            if(mp3.hasId3v2Tag()){
                byte[] bytes = mp3.getId3v2Tag().getAlbumImage();

                return ImageIO.read(new ByteArrayInputStream(bytes));
            }
        } catch (UnsupportedTagException e) {
            Settings.getLogger().error("", e);
        } catch (InvalidDataException e) {
            Settings.getLogger().error("", e);
        } catch (IOException e) {
            Settings.getLogger().error("", e);
        }

        return null;
    }

    public String proposeName(File file){
        String newName = "";

        try {
            Mp3File mp3 = new Mp3File(file.getAbsolutePath());
            if(mp3.hasId3v2Tag()){
                ID3v2 id3v2 = mp3.getId3v2Tag();

                if(id3v2.getTitle() != null && !id3v2.getTitle().trim().isEmpty()){
                    newName = id3v2.getTitle().trim();
                }

                if(id3v2.getArtist() != null && !id3v2.getArtist().trim().isEmpty() && !newName.isEmpty()){
                    newName = id3v2.getArtist().trim() + " - " + newName;
                }

//				if(id3v2.getTrack() != null && !id3v2.getTrack().trim().isEmpty() && !newName.isEmpty()){
//					newName = id3v2.getTrack().trim() + " - " + newName;
//				}

                if(newName.isEmpty()){
                    newName = file.getName();
                }

                if(newName.contains(" ")){
                    newName = newName.replace("\"", "");
                }

                newName = newName.replace(File.separatorChar, '.');
                newName = newName.replace('/', '.');	// on windows still path separator
                newName = decode(newName);
            }
            else if(mp3.hasId3v1Tag()){
                ID3v1 id3v1 = mp3.getId3v1Tag();

//				if(id3v1.getTitle() != null){
//					SimpleLogger.logMessage("id3v1 title: " + id3v1.getTitle());
//				}

                if(id3v1.getTitle() != null && !id3v1.getTitle().trim().isEmpty()){
                    newName = id3v1.getTitle().trim();
                }

                if(id3v1.getArtist() != null && !id3v1.getArtist().trim().isEmpty() && !newName.isEmpty()){
                    newName = id3v1.getArtist().trim() + " - " + newName;
                }

//				if(id3v1.getTrack() != null && !id3v1.getTrack().trim().isEmpty() && !newName.isEmpty()){
//					newName = id3v1.getTrack().trim() + " - " + newName;
//				}

                if(newName.isEmpty()){
                    newName = file.getName();
                }

                if(newName.contains(" ")){
                    newName = newName.replace("\"", "");
                }

                newName = newName.replace(File.separatorChar, '.');
                newName = newName.replace('/', '.');
                newName = decode(newName);
            }
            else{
                newName = file.getName();
            }
        } catch (UnsupportedTagException e) {
            Settings.getLogger().error("", e);
        } catch (InvalidDataException e) {
            Settings.getLogger().error("", e);
        } catch (IOException e) {
            Settings.getLogger().error("", e);
        }



        return newName;
    }

    public boolean isDecodeStrings() {
        return decodeStrings;
    }

    public void setDecodeStrings(boolean decodeStrings) {
        this.decodeStrings = decodeStrings;
    }
}
