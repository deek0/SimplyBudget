package app.com.example.android.simplybudget.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface BudgetDao {
    @Insert
    void insertBudgetEntry(BudgetEntry budgetEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateBudgetEntry(BudgetEntry budgetEntry);

    @Delete
    void deleteBudgetEntry(BudgetEntry budgetEntry);

    @Query("SELECT * FROM budget ORDER BY category")
//    used to populate the budget overview recycler view
    LiveData<List<BudgetEntry>> loadAllBudgetEntries();

    @Query("SELECT category FROM budget")
//    used to populate the category spinner in the add expense fragment
    LiveData<List<String>> getAllBudgetCategories();

    @Query("UPDATE budget SET amount_spent = :spent WHERE category = :matchingCat")
//    updating the spent amount in the budget entity
    void updateSpentAmounts(String spent, String matchingCat);




}
