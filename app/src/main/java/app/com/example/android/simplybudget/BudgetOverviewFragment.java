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

import app.com.example.android.simplybudget.database.BudgetEntry;
import app.com.example.android.simplybudget.database.ExpenseEntry;

public class BudgetOverviewFragment extends Fragment {
//    shows a recycler view with data from the budget entity
//    floating action button to add new categories to the budget
//    click on a recycler view item to edit

    private BudgetViewModel budgetViewModel;
    private FloatingActionButton buttonAddCategory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budget_overview, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.bottom_nav_budget_title);
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view_budget_overview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        final BudgetAdapter adapter = new BudgetAdapter();
        recyclerView.setAdapter(adapter);
        budgetViewModel = ViewModelProviders.of(getActivity()).get(BudgetViewModel.class);
        budgetViewModel.getAllBudgetEntries().observe(getActivity(), new Observer<List<BudgetEntry>>() {
            @Override
            public void onChanged(@Nullable List<BudgetEntry> budgetEntries) {
                adapter.setBudgetEntries(budgetEntries);
            }
        });

        budgetViewModel.getExpenseTotalsByCategory().observe(getActivity(), new Observer<List<ExpenseEntry>>() {
            @Override
            public void onChanged(@Nullable List<ExpenseEntry> expenseEntries) {
                for (ExpenseEntry entry : expenseEntries) {
                    budgetViewModel.updateSpentAmounts(entry.getCost(), entry.getCategory());
                }
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder
                    , @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                budgetViewModel.delete(adapter.getBudgetEntryAt(viewHolder.getAdapterPosition()));

                Toast.makeText(getView().getContext(), "Entry deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new BudgetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BudgetEntry entry) {
                Bundle bundle = new Bundle();
                bundle.putString(getContext().getResources().getString(R.string.category_column_title)
                        , entry.getCategory());
                bundle.putString(getContext().getResources().getString(R.string.budgeted_amount_column_title)
                        , entry.getAmountBudgetedMonthly());
                bundle.putBoolean(getContext().getResources().getString(R.string.scheduled_payment_column_title)
                        ,entry.getOneTimePayment());
                bundle.putInt(getContext().getResources().getString(R.string.id_column_title)
                        , entry.getId());
                Fragment destinationFragment = new AddEditCategoryFragment();
                destinationFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, destinationFragment).commit();
            }
        });

        buttonAddCategory = getView().findViewById(R.id.fab_add_budget_category);
        buttonAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment destinationFragment = new AddEditCategoryFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, destinationFragment).commit();
            }
        });

    }
}
