package org.sergeys.cookbook.logic;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


public class RecipeLibrary {
	
    private static Object instanceLock = new Object();
    private static RecipeLibrary instance;

    // singleton
    private RecipeLibrary(){        
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
			for(Recipe r: recipes){
				File f = new File(Settings.getRecipeLibraryPath() + File.separator + r.getHash() + ".html");
				if(!f.exists()){
					System.out.println("not unpacked " + f);
					
					File temp = File.createTempFile("cookbook", ".jar");
					temp.deleteOnExit();
										
					Database.getInstance().extractRecipeFile(r.getHash(), temp);
					
					Util.unpackJar(temp, Settings.getRecipeLibraryPath());
					
					temp.delete();
				}
			}
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
