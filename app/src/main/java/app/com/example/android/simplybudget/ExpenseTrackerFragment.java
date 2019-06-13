package app.com.example.android.simplybudget;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import app.com.example.android.simplybudget.database.ExpenseEntry;

public class ExpenseTrackerFragment extends Fragment {
    /* shows a recycler view with data from the expense entity
    floating action button to add new eppenses
    click on a recycler view item to edit */

    private ExpenseViewModel expenseViewModel;
    private BudgetViewModel budgetViewModel;
    private FloatingActionButton buttonAddExpense;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expense_tracker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.bottom_nav_expense_tracker_title);
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view_expense_tracker);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        final ExpenseAdapter adapter = new ExpenseAdapter();
        recyclerView.setAdapter(adapter);
        expenseViewModel = ViewModelProviders.of(getActivity()).get(ExpenseViewModel.class);
        budgetViewModel = ViewModelProviders.of(getActivity()).get(BudgetViewModel.class);
        expenseViewModel.getAllExpenses().observe(getActivity(), new Observer<List<ExpenseEntry>>() {
            @Override
            public void onChanged(@Nullable List<ExpenseEntry> expenseEntries) {
                adapter.setExpenses(expenseEntries);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                ExpenseEntry deletedEntry = adapter.getExpenseAt(viewHolder.getAdapterPosition());
                budgetViewModel.updateSpentAmounts("0",deletedEntry.getCategory());
                expenseViewModel.delete(deletedEntry);
                Toast.makeText(getView().getContext(), "Entry deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new ExpenseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ExpenseEntry entry) {
                Bundle bundle = new Bundle();
                bundle.putString(getContext().getResources().getString(R.string.date_column_title),entry.getUpdatedAt());
                bundle.putString(getContext().getResources().getString(R.string.cost_column_title), entry.getCost());
                bundle.putString(getContext().getResources().getString(R.string.description_column_title),entry.getDescription());
                bundle.putString(getContext().getResources().getString(R.string.category_column_title),entry.getCategory());
                bundle.putInt(getContext().getResources().getString(R.string.id_column_title),entry.getId());
                Fragment destinationFragment = new AddEditExpenseFragment();
                destinationFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,destinationFragment).commit();
            }
        });

        buttonAddExpense=getView().findViewById(R.id.fab_add_expense);
        buttonAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment destinationFragment = new AddEditExpenseFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,destinationFragment).commit();
            }
        });
    }
}
