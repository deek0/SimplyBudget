package app.com.example.android.simplybudget;

import android.app.Application;
import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import app.com.example.android.simplybudget.database.BudgetEntry;

import app.com.example.android.simplybudget.database.ExpenseEntry;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetHolder> {
//    adapter for the budget overview recycler view
//    card view layout budget_overview_item.xml

    private List<BudgetEntry> budgetEntries = new ArrayList<>();
    private OnItemClickListener listener;
    private static final String LOG_TAG = BudgetAdapter.class.getSimpleName();



    @NonNull
    @Override
    public BudgetHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.budget_overview_item,viewGroup,false);
        return new BudgetHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetHolder holder, int position) {

        BudgetEntry currentBudgetEntry = budgetEntries.get(position);
        String budgetedAmount = currentBudgetEntry.getAmountBudgetedMonthly();
        String spentAmount = currentBudgetEntry.getAmountSpent();
        holder.textViewCategory.setText(currentBudgetEntry.getCategory());
        holder.textViewBudgetedAmount.setText("$"+budgetedAmount);
        holder.progressBarSpent.setMax(800);
        holder.progressBarBudgeted.setProgress(getProgressBarInt(budgetedAmount));
        holder.textViewSpentAmount.setText("$"+spentAmount);
        holder.progressBarSpent.setProgress(getProgressBarInt(spentAmount));
        setProgressBarColor(holder,spentAmount,budgetedAmount);

    }

    private void setProgressBarColor(@NonNull BudgetHolder holder,String spentAmount, String budgetedAmount){
//        used to set the color of the progress bars, the spent amount bar will change if it is more than the budgeted amount
        holder.progressBarBudgeted.getProgressDrawable().setColorFilter(holder.itemView.getResources().getColor(R.color.budgetAmountBar), PorterDuff.Mode.SRC_IN);
        if(Double.parseDouble(spentAmount)<Double.parseDouble(budgetedAmount)){
            holder.progressBarSpent.getProgressDrawable().setColorFilter(holder.itemView.getResources().getColor(R.color.spentBarOverBudget),PorterDuff.Mode.SRC_IN);
        }
        else {
            holder.progressBarSpent.getProgressDrawable().setColorFilter(holder.itemView.getResources().getColor(R.color.spentBarUnderBudget),PorterDuff.Mode.SRC_IN);
        }
    }

    private int getProgressBarInt(String amount){
//        trying to make a suitable scale for filling the progress bars

        Double dAmount = Double.parseDouble(amount);
        if (dAmount>22000){
            return R.integer.budget_overview_progress_bar_max;
        }
        else if (dAmount>8){
            return (int)(100*Math.log(dAmount)-200);
        }
        else if (dAmount>0){
            return (int) (1*dAmount);
        }
        else return 0;

        }


    @Override
    public int getItemCount() {
        return budgetEntries.size();
    }



    public void setBudgetEntries(List<BudgetEntry> budgetEntries) {
        this.budgetEntries = budgetEntries;
        notifyDataSetChanged();
    }


    public BudgetEntry getBudgetEntryAt(int position) {
        return  budgetEntries.get(position);
    }

//    public ExpenseEntry getExpenseEntryAt(int position) {
//        return expenseTotalsByCategory.get(position);
//    }

    class BudgetHolder extends RecyclerView.ViewHolder {


        private TextView textViewCategory;
        private ProgressBar progressBarBudgeted;
        private ProgressBar progressBarSpent;
        private TextView textViewBudgetedAmount;
        private TextView textViewSpentAmount;

        public BudgetHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategory = itemView.findViewById(R.id.text_view_category_budget);
            progressBarBudgeted = itemView.findViewById(R.id.progress_bar_budget);
            progressBarSpent = itemView.findViewById(R.id.progress_bar_spent);
            textViewBudgetedAmount = itemView.findViewById(R.id.text_view_budgeted_amount);
            textViewSpentAmount = itemView.findViewById(R.id.text_view_spent_amount);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(budgetEntries.get(position));
                    }
                }
            });
        }

    }
    public interface OnItemClickListener {
        void onItemClick(BudgetEntry entry);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
