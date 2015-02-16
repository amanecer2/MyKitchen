package com.amanecer.themykitchen.obj;

/**
 * Created by amanecer on 30/11/2014.
 */
public class Recipes {
    private String recipeName;
    private String describe;
    private String PATH;
    private boolean gotAllIngridients;
    private int rating;
    private String recipeId;

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public Recipes(String recipeName, String describe, String PATH, boolean gotAllIngridients, int rating, String recipeId) {
        this.recipeName = recipeName;
        this.describe = describe;
        this.PATH = PATH;
        this.gotAllIngridients = gotAllIngridients;
        this.rating = rating;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPATH() {
        return PATH;
    }

    public void setPATH(String PATH) {
        this.PATH = PATH;
    }

    public boolean isGotAllIngridients() {
        return gotAllIngridients;
    }

    public void setGotAllIngridients(boolean gotAllIngridients) {
        this.gotAllIngridients = gotAllIngridients;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
