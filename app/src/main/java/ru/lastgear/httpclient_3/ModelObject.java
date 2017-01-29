package ru.lastgear.httpclient_3;

/**
 * Created by User on 18.01.2017.
 */

public enum ModelObject {

    RED (R.string.red, R.layout.balances_tab1),
    BLUE (R.string.blue, R.layout.view_blue),
    GREEN (R.string.green, R.layout.view_green);

    private int mTitleResId;
    private int mLayoutResId;

    ModelObject (int titleResId, int layoutResId){
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getmTitleResId(){
        return mTitleResId;
    }

    public int getmLayoutResId(){
        return mLayoutResId;
    }
}