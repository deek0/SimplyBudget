package app.com.example.android.simplybudget.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;



public class BudgetRepository {
//    used to access the budgetDao and expenseDao for the budgetview model
//    async tasks as required to run queries off the main thread

    private static final String LOG_TAG = BudgetRepository.class.getSimpleName();
    private BudgetDao budgetDao;
    private ExpenseDao expenseDao;
    private LiveData<List<BudgetEntry>> allBudgetEntries;
    private LiveData<List<ExpenseEntry>> expenseTotalsByCategory;

    public BudgetRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        budgetDao = database.budgetDao();
        expenseDao = database.expenseDao();
        allBudgetEntries = budgetDao.loadAllBudgetEntries();
        expenseTotalsByCategory = expenseDao.loadExpenseTotalsByCategory();
    }
    public void insert(BudgetEntry entry){
        new InsertBudgetEntryAsyncTask(budgetDao).execute(entry);
    }

    public void update(BudgetEntry entry){
        new UpdateBudgetEntryAsyncTask(budgetDao).execute(entry);
    }

    public void delete(BudgetEntry entry){
        new DeleteBudgetEntryAsyncTask(budgetDao).execute(entry);
    }

    public void changeCategoryName(String oldCat, String newCat){
        new ChangeCategoryNameAsyncTask(expenseDao).execute(oldCat,newCat);
    }

    public void updateSpentAmounts(String spent, String category) {
        new UpdateSpentAmountsAsyncTask(budgetDao).execute(spent,category);
    }

    public LiveData<List<BudgetEntry>> getAllBudgetEntries(){
        return allBudgetEntries;
    }

    public LiveData<List<ExpenseEntry>> loadExpenseTotalByCategory(){
        return expenseTotalsByCategory;
    }

    private static class InsertBudgetEntryAsyncTask extends AsyncTask<BudgetEntry, Void, Void> {
        private BudgetDao budgetDao;

        private InsertBudgetEntryAsyncTask(BudgetDao budgetDao) {
            this.budgetDao = budgetDao;
        }
        @Override
        protected Void doInBackground(BudgetEntry... budgetEntries) {
            budgetDao.insertBudgetEntry(budgetEntries[0]);
            return null;
        }
    }

    private static class UpdateBudgetEntryAsyncTask extends AsyncTask<BudgetEntry, Void, Void>{
        private BudgetDao budgetDao;

        private UpdateBudgetEntryAsyncTask(BudgetDao budgetDao) {
            this.budgetDao = budgetDao;
        }
        @Override
        protected Void doInBackground(BudgetEntry... budgetEntries) {
            budgetDao.updateBudgetEntry(budgetEntries[0]);
            return null;
        }
    }

    private static class DeleteBudgetEntryAsyncTask extends AsyncTask<BudgetEntry, Void, Void>{
        private BudgetDao budgetDao;

        private DeleteBudgetEntryAsyncTask(BudgetDao budgetDao) {
            this.budgetDao = budgetDao;
        }
        @Override
        protected Void doInBackground(BudgetEntry... budgetEntries) {
            budgetDao.deleteBudgetEntry(budgetEntries[0]);

            return null;
        }
    }
    private static class ChangeCategoryNameAsyncTask extends AsyncTask<String,String,Void>{
        private ExpenseDao expenseDao;

        @Override
        protected Void doInBackground(String... strings) {
            expenseDao.changeCategoryName(strings[0],strings[1]);
            return null;
        }

        private ChangeCategoryNameAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }
    }

    private static class UpdateSpentAmountsAsyncTask extends AsyncTask<String,String,Void>{
        private BudgetDao budgetDao;

        @Override
        protected Void doInBackground(String... strings) {
            budgetDao.updateSpentAmounts(strings[0],strings[1]);
            return null;
        }

        private  UpdateSpentAmountsAsyncTask(BudgetDao budgetDao) {
            this.budgetDao = budgetDao;
        }
    }
}
