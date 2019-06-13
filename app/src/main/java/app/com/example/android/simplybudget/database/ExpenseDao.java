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
public interface ExpenseDao {

    @Insert
    void insertExpense(ExpenseEntry expenseEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateExpense(ExpenseEntry expenseEntry);

    @Delete
    void deleteExpense(ExpenseEntry expenseEntry);

    @Query("SELECT * FROM expenses ORDER BY updated_at DESC, id DESC")
//    used to populate the expense tracker recycler view
    LiveData<List<ExpenseEntry>> loadAllExpenses();

    @Query("SELECT id, category, SUM(cost) as cost FROM expenses Group By category Order By category")
//    used to populate the spent amounts in the budget entity with data from the expense entity
    LiveData<List<ExpenseEntry>> loadExpenseTotalsByCategory();

    @Query("UPDATE expenses SET category= :newCat WHERE category = :oldCat")
//    used to change categories in expenses when relevant category is deleted from the budget entity
    void changeCategoryName(String oldCat, String newCat);


}
