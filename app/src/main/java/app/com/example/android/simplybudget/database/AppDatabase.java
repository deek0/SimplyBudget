package app.com.example.android.simplybudget.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Date;


@Database(entities = {ExpenseEntry.class,BudgetEntry.class}, version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    //creates a singleton instance of the database if it does not exist already

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "budget_db";
    private static AppDatabase sInstance;

    public abstract ExpenseDao expenseDao();
    public abstract BudgetDao budgetDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    private static AppDatabase.Callback roomCallback = new RoomDatabase.Callback(){

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(sInstance).execute();

        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {
//        populate the new database with 1 entry for each entity
        private ExpenseDao expenseDao;
        private BudgetDao budgetDao;

        private PopulateDbAsyncTask(AppDatabase db ){
            expenseDao = db.expenseDao();
            budgetDao = db.budgetDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            expenseDao.insertExpense(new ExpenseEntry("testCategory","testDescription", "testDate", "testCost"));
            budgetDao.insertBudgetEntry(new BudgetEntry("Misc","120", Boolean.FALSE));
            return null;
        }
    }
}
