package app.com.example.android.simplybudget.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "expenses")
public class ExpenseEntry {
//    table for tracking expenses with 5 columns including category, description, cost, and date
//    date is stored as a string mm/dd/yyyy
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String category;
    private String description;
    private String cost;

    @ColumnInfo(name="updated_at")
    private String updatedAt;

    @Ignore
    public ExpenseEntry(String category, String cost) {
        this.category = category;
        this.cost = cost;
        this.description="formatting";
        this.updatedAt="01/01/0001";
    }

    @Ignore
    public ExpenseEntry(String category, String description, String updatedAt, String cost) {
        this.description = description;
        this.category = category;
        this.updatedAt = updatedAt;
        this.cost = cost;
    }

    public ExpenseEntry(int id, String category, String description, String updatedAt, String cost) {
        this.id = id;
        this.description = description;
        this.category = category;
        this.cost = cost;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


}
