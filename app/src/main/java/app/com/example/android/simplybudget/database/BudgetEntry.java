package app.com.example.android.simplybudget.database;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "budget")
public class BudgetEntry {
//    Table for budget data with 6 columns including category and budgeted amount
//    spent amount is filled from the expenses entity
//    one time payment and day of month are for future updates
    @PrimaryKey (autoGenerate = true)
    private int id;
    private String category;
    @ColumnInfo(name="amount_budgeted_monthly")
    private String amountBudgetedMonthly;
    @ColumnInfo(name = "amount_spent")
    private String amountSpent;
    @ColumnInfo(name="one_time_payment")
    private Boolean oneTimePayment = false;
    @ColumnInfo(name="day_of_month")
    private int dayOfMonth;

    @Ignore
    public BudgetEntry( String category, String amountBudgetedMonthly, Boolean oneTimePayment
            , int dayOfMonth) {
        this.id = id;
        this.category = category;
        this.amountBudgetedMonthly = amountBudgetedMonthly;
        this.oneTimePayment = oneTimePayment;
        this.dayOfMonth = dayOfMonth;
        this.amountSpent = "0";
    }

    @Ignore
    public BudgetEntry( String category, String amountBudgetedMonthly, Boolean oneTimePayment
            ) {
        this.id = id;
        this.category = category;
        this.amountBudgetedMonthly = amountBudgetedMonthly;
        this.oneTimePayment = oneTimePayment;
        this.dayOfMonth = 0;
        this.amountSpent = "0";
    }

    public BudgetEntry(int id, String category, String amountBudgetedMonthly, Boolean oneTimePayment
            , int dayOfMonth ) {
        this.id = id;
        this.category = category;
        this.amountBudgetedMonthly = amountBudgetedMonthly;
        this.oneTimePayment = oneTimePayment;
        this.dayOfMonth = dayOfMonth;
        this.amountSpent = "0";
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAmountBudgetedMonthly(String amountBudgetedMonthly) {
        this.amountBudgetedMonthly = amountBudgetedMonthly;
    }

    public void setOneTimePayment(Boolean oneTimePayment) {
        this.oneTimePayment = oneTimePayment;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getAmountBudgetedMonthly() {
        return amountBudgetedMonthly;
    }

    public Boolean getOneTimePayment() {
        return oneTimePayment;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public String getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(String amountSpent) {
        this.amountSpent = amountSpent;
    }
}

