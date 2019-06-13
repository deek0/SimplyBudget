package app.com.example.android.simplybudget.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class ExpenseRepository {
//    used to access the budgetDao and expenseDao for the expenseViewModel
//    async tasks as required to run queries off the main thread
    private ExpenseDao expenseDao;
    private BudgetDao budgetDao;
    private LiveData<List<ExpenseEntry>> allExpenses;
    private LiveData<List<String>> allBudgetCategories;

    public ExpenseRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        expenseDao = database.expenseDao();
        budgetDao = database.budgetDao();
        allExpenses = expenseDao.loadAllExpenses();
        allBudgetCategories = budgetDao.getAllBudgetCategories();
    }

    public void insert(ExpenseEntry entry){
        new InsertExpenseAsyncTask(expenseDao).execute(entry);
    }

    public void update(ExpenseEntry entry){
        new UpdateExpenseAsyncTask(expenseDao).execute(entry);
    }

    public void delete(ExpenseEntry entry){
        new DeleteExpenseAsyncTask(expenseDao).execute(entry);
    }



    public LiveData<List<ExpenseEntry>> getAllExpenses(){
        return allExpenses;
    }

    public LiveData<List<String>> getAllBudgetCategories(){
        return allBudgetCategories;
    }



    private static class InsertExpenseAsyncTask extends AsyncTask<ExpenseEntry, Void, Void>{
        private ExpenseDao expenseDao;

        private InsertExpenseAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }
        @Override
        protected Void doInBackground(ExpenseEntry... expenseEntries) {
            expenseDao.insertExpense(expenseEntries[0]);
            return null;
        }
    }

    private static class UpdateExpenseAsyncTask extends AsyncTask<ExpenseEntry, Void, Void>{
        private ExpenseDao expenseDao;

        private UpdateExpenseAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }
        @Override
        protected Void doInBackground(ExpenseEntry... expenseEntries) {
            expenseDao.updateExpense(expenseEntries[0]);
            return null;
        }
    }



    private static class DeleteExpenseAsyncTask extends AsyncTask<ExpenseEntry, Void, Void>{
        private ExpenseDao expenseDao;

        private DeleteExpenseAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }
        @Override
        protected Void doInBackground(ExpenseEntry... expenseEntries) {
            expenseDao.deleteExpense(expenseEntries[0]);
            return null;
        }
    }



}
