package app.com.example.android.simplybudget;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import app.com.example.android.simplybudget.database.ExpenseEntry;
import app.com.example.android.simplybudget.database.ExpenseRepository;

public class ExpenseViewModel extends AndroidViewModel {
    private ExpenseRepository repository;
    private LiveData<List<ExpenseEntry>> allExpenses;
    private LiveData<List<String>> allBudgetCategories;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExpenseRepository(application);
        allExpenses = repository.getAllExpenses();
        allBudgetCategories = repository.getAllBudgetCategories();
    }

    public void insert(ExpenseEntry entry){
        repository.insert(entry);
    }
    public void update(ExpenseEntry entry){
        repository.update(entry);
    }
    public void delete(ExpenseEntry entry){
        repository.delete(entry);
    }
    public LiveData<List<ExpenseEntry>> getAllExpenses(){
        return allExpenses;
    }
    public LiveData<List<String>> getAllBudgetCategories(){
        return allBudgetCategories;
    }

}
