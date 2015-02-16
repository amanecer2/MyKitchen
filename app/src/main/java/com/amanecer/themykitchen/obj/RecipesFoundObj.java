package com.amanecer.themykitchen.obj;

/**
 * Created by amanecer on 03/12/2014.
 */
public class RecipesFoundObj {
    String recipeName;
    String PATH;
    String details;

    public RecipesFoundObj(String recipeName, String PATH, String details) {
        this.recipeName = recipeName;
        this.PATH = PATH;
        this.details = details;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getPATH() {
        return PATH;
    }

    public void setPATH(String PATH) {
        this.PATH = PATH;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
