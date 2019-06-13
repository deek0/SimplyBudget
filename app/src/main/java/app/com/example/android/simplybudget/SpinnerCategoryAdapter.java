package app.com.example.android.simplybudget;

import java.util.ArrayList;
import java.util.List;

public class SpinnerCategoryAdapter {
    /*used to populate the category spinner in the add expense fragment with the categories in the budget*/
    private List<String> mCategoryList = new ArrayList<String>();

    void setCategory(List<String> category){
        mCategoryList = category;
    }

    public int getItemCount() {
        if (mCategoryList != null)
            return mCategoryList.size();
        else return 0;
    }


}




