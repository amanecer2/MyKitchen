package com.amanecer.themykitchen.networking;

/**
 * Created by amanecer on 19/11/2014.
 */
public class API_Constant {
    // the web site is : https://developer.yummly.com

    public static  final  String yumi_idre = "French-Onion-Soup-The-Pioneer-Woman-Cooks-_-Ree-Drummond-41364";
    public static final String getYummi_ingridients =  "http://api.yummly.com/v1/api/metadata/ingredient?_app_id=e16b46c1&_app_key=6aaf699217a04fbbd896e22f77aafbd7";
    //   http://api.yummly.com/v1/api/recipe/recipe-id?_app_id=e16b46c1&_app_key=6aaf699217a04fbbd896e22f77aafbd7
    public static final String yummi_applicationID = "e16b46c1";
    public static final String yummi_applicationKEYS = "6aaf699217a04fbbd896e22f77aafbd7";
    public static final String yummi_searchRecipeByID = "http://api.yummly.com/v1/api/recipe/recipe-id?_app_id="+yummi_applicationID+"&_app_key="+ yummi_applicationKEYS+"";
    public static final String yummi_searchRecipes = "http://api.yummly.com/v1/api/recipes?_app_id="+yummi_applicationID +"&_app_key="+yummi_applicationKEYS+"&q=";
    public static final String yummi_with_pic = "&requirePictures=true";
    public static final String yummi_specific_ingredient= "&allowedIngredient[]="; // with this ingrideient;
    public static final String yummi_excluded_ingredient= "&excludedIngredient[]=";// without a sfecific ingridient;
    public static final String yummi_allowed_allergy= "&allowedAllergy[]=";  //dairy free. glutian free;
    public static final String yummi_howManyRecipesToSearch= "&maxResult=100&start=0" ;
    public static final String yummi_allowed_diet= "&allowedDiet[]";

    public static final String basil = "http://api.yummly.com/v1/api/recipes?_app_id=e16b46c1&_app_key=6aaf699217a04fbbd896e22f77aafbd7&q=salad&allowedIngredient[]=basil";

    public static final String yahooStockInfo = "http://query.yahooapis.com/v1/public/yql?" + "q=select%20*%20from%20yahoo.finance.quote%20where%20symbol" + "%20in%20(%22YHOO%22)&format=json&env=store%3A%2F%2" + "Fdatatables.org%2Falltableswithkeys&callback=cbfunc";// - See more at: http://www.newthinktank.com/2013/07/android-development-15/#sthash.pdqT2Cac.dpuf";
    public static final String tryURl = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.places%20where%20text=%22sunnyvale,%20ca%22";
    public static final String space_in_the_url = "+";
    public static final String matches ="matches";
    public static final String criteria = "criteria";
    public static final String ingredients="ingredients";
    public static final String imageUrlsBySize = "imageUrlsBySize";
    public static final String id = "id";
    public static final String smallImageUrls = "smallImageUrls";
    public static final String recipeName= "recipeName";
    public static final String totalTimeInSeconds = "totalTimeInSeconds";
    public static final String rating = "rating";
    public static final String flavors = "flavors";
    public static final String attributes = "attributes";
    public static final String course = "course";// type of dish = Appetizers ...;
    public static final String cuisine = "cuisine"; // style of cooking = arabic, italian...;
    public static final String maxTotalTimeInSeconds="&maxTotalTimeInSeconds=";  // when you want to limit the cooking
    public static final String piquant = "piquant";
    public static final String meaty = "meaty";
    public static final String sour = "sour";
    public static final String bitter = "bitter";
    public static final String salty = "salty";
    public static final String sweet = "sweet";
    public static final String recipeId ="recipeId";
    public static final String myIngredients="myIngredients";
    public static final String productId="productId";
    public static final String productName="productName";
    public static final String PATH = "PATH" ;
    public static final String count= "count";
    public static final String searchRecipes ="searchRecipes";
    public static final String searchRecipeById = "searchRecipeById";
    public static final String nutritionEstimates ="nutritionEstimates";
    public static final String images ="images";
    public static final String source = "source";
    public static final String numberOfServings="numberOfServings";
    public static final String productWithCount = "productWithCount";
    public static final String producs ="producs";
    public static final int fail = -1;
    public static final String searchIngridients ="searchIngridients";
    public static final String MyFrigeId="MyFrigeId";
    public static final String searchValue ="searchValue";
    public static final String method_getAllIngridientAPI="method_getAllIngridientAPI";
    public static final String whatToSearch="whatToSearch";
    public static final String searchingForRecipe="Searching for recipes please wait";
    public static final String Searching="Searching";
    public static final String recipeNoMath = "RecipeNoMath";
    public static final String noConecction = "noConecction";


    //&requirePictures=true

}
///      your _search_parameters

//http://api.yummly.com/v1/api/recipes?_app_id=e16b46c1&_app_key=6aaf699217a04fbbd896e22f77aafbd7&q=

//"http://api.yummly.com/v1/api/recipes?_app_id=e16b46c1&_app_key="+applicationKEYS+"&";
/*
//              French-Onion-Soup-The-Pioneer-Woman-Cooks-_-Ree-Drummond-41364
        applicationID = "e16b46c1";
   applicationKEYS = "6aaf699217a04fbbd896e22f77aafbd7";
    http://api.yummly.com/v1/api/recipe/recipe-id?_app_id=e16b46c1&_app_key=6aaf699217a04fbbd896e22f77aafbd7=

// get recipe by id  =

*/