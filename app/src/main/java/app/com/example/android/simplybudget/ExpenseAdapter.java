package app.com.example.android.simplybudget;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.com.example.android.simplybudget.database.ExpenseEntry;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseHolder> {
    /*adapter for expense tracker recycler view, card laout from expense_entry_item.xml*/
    private List<ExpenseEntry> expenses = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ExpenseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.expense_entry_item, viewGroup, false);
        return new ExpenseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseHolder holder, int position) {
        ExpenseEntry currentExpense = expenses.get(position);
        holder.textViewDate.setText(currentExpense.getUpdatedAt());
        holder.textViewCategory.setText(currentExpense.getCategory());
        holder.textViewDescription.setText(currentExpense.getDescription());
        holder.textViewCost.setText("$" + currentExpense.getCost());
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public void setExpenses(List<ExpenseEntry> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();
    }

    public ExpenseEntry getExpenseAt(int position) {
        return expenses.get(position);
    }

    class ExpenseHolder extends RecyclerView.ViewHolder {
        private TextView textViewDate;
        private TextView textViewCategory;
        private TextView textViewDescription;
        private TextView textViewCost;

        public ExpenseHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.text_view_date);
            textViewCategory = itemView.findViewById(R.id.text_view_category);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewCost = itemView.findViewById(R.id.text_view_cost);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(expenses.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ExpenseEntry entry);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        /*used to edit items in the recycler view*/
        this.listener = listener;
    }
}
