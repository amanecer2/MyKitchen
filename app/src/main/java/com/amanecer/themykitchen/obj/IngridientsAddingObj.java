package com.amanecer.themykitchen.obj;


import com.amanecer.themykitchen.networking.API_Constant;

/**
 * Created by amanecer on 14/12/2014.
 */
public class IngridientsAddingObj {

    private String ingridientName;
    private boolean obligatory;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public IngridientsAddingObj() {
    }

    public IngridientsAddingObj(String ingridientName, boolean obligatory) {
        this.ingridientName = ingridientName;
        this.obligatory = obligatory;
        value = returnTheValue();
    }

    public String returnTheValue(){
        if (isObligatory()){
            value = API_Constant.yummi_specific_ingredient+ingridientName;
        }else {
            value = ingridientName;
        }
        return value;
    }

    public String getIngridientName() {
        return ingridientName;
    }

    public void setIngridientName(String ingridientName) {
        this.ingridientName = ingridientName;
    }

    public boolean isObligatory() {
        return obligatory;
    }

    public void setObligatory(boolean obligatory) {
        this.obligatory = obligatory;
    }
}
