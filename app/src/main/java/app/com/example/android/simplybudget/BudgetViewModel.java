package app.com.example.android.simplybudget;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import app.com.example.android.simplybudget.database.BudgetEntry;
import app.com.example.android.simplybudget.database.BudgetRepository;
import app.com.example.android.simplybudget.database.ExpenseEntry;
import app.com.example.android.simplybudget.database.ExpenseRepository;

public class BudgetViewModel extends AndroidViewModel {
    /*used to access data with the budgetRepository*/
    private BudgetRepository budgetRepository;
    private LiveData<List<BudgetEntry>> allBudgetEntries;
    private LiveData<List<ExpenseEntry>> expenseTotalsByCategory;

    public BudgetViewModel(@NonNull Application application) {
        /*initializes the repository and two live data lists*/
        super(application);
        budgetRepository = new BudgetRepository(application);
        allBudgetEntries = budgetRepository.getAllBudgetEntries();
        expenseTotalsByCategory = budgetRepository.loadExpenseTotalByCategory();
    }

    public void insert(BudgetEntry entry){
        budgetRepository.insert(entry);
    }
    public void update(BudgetEntry entry){
        budgetRepository.update(entry);
    }
    public void delete(BudgetEntry entry){
        /*when a budget category is deleted, change the expenses with the relevant category to a new string*/
        budgetRepository.delete(entry);
        budgetRepository.changeCategoryName(entry.getCategory(),"Misc");
    }

    public void updateSpentAmounts(String spent, String category){
        budgetRepository.updateSpentAmounts(spent,category);
    }

    public LiveData<List<BudgetEntry>> getAllBudgetEntries(){
        return allBudgetEntries;
    }
    public LiveData<List<ExpenseEntry>> getExpenseTotalsByCategory() {
        return expenseTotalsByCategory;
    }
}
