package org.sergeys.cookbook.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javafx.concurrent.Task;


public final class RecipeLibrary {

    private static Object instanceLock = new Object();
    private static RecipeLibrary instance;

    private Hashtable<String, String> fullwords = new Hashtable<String, String>();
    private Hashtable<String, String> prefixes = new Hashtable<String, String>();

    // singleton
    private RecipeLibrary(){
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        getClass().getResourceAsStream("/resources/tagsuggestions_ru.txt"), StandardCharsets.UTF_8));
        String s;
        try {
            while((s = br.readLine()) != null){
                if(!s.isEmpty()){
                    String[] arr = s.split("=");
                    String[] words = arr[1].split(",");
                    for(String word: words){
                        word = word.trim().toLowerCase();
                        if(word.endsWith("-")){
                            prefixes.put(word.substring(0, word.length() - 1), arr[0].trim().toLowerCase());
                        }
                        else{
                            fullwords.put(word, arr[0].trim().toLowerCase());
                        }

                    }
                }
            }
        } catch (IOException e) {
            Settings.getLogger().error("", e);
        }
    }

    public static RecipeLibrary getInstance() {
        synchronized (instanceLock) {
            if(instance == null){
                instance = new RecipeLibrary();
            }
        }

        return instance;
    }

    public void validate(){
        try {
            ArrayList<Recipe> recipes = Database.getInstance().getAllRecipes();
            for(final Recipe r: recipes){
                final File f = new File(Settings.getRecipeLibraryPath() + File.separator + r.getHash() + ".html");
                if(!f.exists()){

                    Task<Void> task = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            Settings.getLogger().debug("unpacking " + f.getAbsolutePath());
                            File temp = File.createTempFile("cookbook", ".jar");
                            temp.deleteOnExit();

                            Database.getInstance().extractRecipeFile(r.getHash(), temp);

                            Util.unpackJar(temp, Settings.getRecipeLibraryPath());

                            temp.delete();
                            return null;
                        }
                    };

                    Settings.getExecutor().execute(task);
                }
            }
        } catch (Exception e) {
            Settings.getLogger().error("", e);
        }
    }

    public List<String> suggestTags(String phrase){

        Hashtable<String, String> tags = new Hashtable<>();

        String[] words = phrase.split("[\\n\\r\\t\\p{Space}\\p{Punct}]");
        for(String word: words){
            word = word.trim().toLowerCase();
            if(fullwords.containsKey(word)){
                tags.put(fullwords.get(word), "");
            }

            for(Enumeration<String> en = prefixes.keys(); en.hasMoreElements();){
                String prefix = en.nextElement();
                if(word.startsWith(prefix)){
                    tags.put(prefixes.get(prefix), "");
                }
            }
        }

        return Collections.list(tags.keys());
    }
}
