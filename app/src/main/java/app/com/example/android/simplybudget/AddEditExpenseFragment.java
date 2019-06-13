package app.com.example.android.simplybudget;

import android.app.DatePickerDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import app.com.example.android.simplybudget.database.ExpenseEntry;

public class AddEditExpenseFragment extends Fragment {
    /*fragment used to add and edit expenses. accessed from the expense tracker fragment by add expense
    or clicking on one of the cards in the recycler view.
    The date text box is clickable and opens a date spinner*/
    private ExpenseViewModel expenseViewModel;
    private Spinner spinnerCategory;
    private EditText editTextDescription;
    private EditText editTextCost;
    final Calendar myCalendar = Calendar.getInstance();
    private EditText editTextDate;
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };
    private Button buttonAdd;
    private LiveData<List<String>> allBudgetCategories;
    private Boolean isEditEntry = false;
    private int id;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_expense, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final Bundle bundle = getArguments();

        expenseViewModel = ViewModelProviders.of(getActivity()).get(ExpenseViewModel.class);
        spinnerCategory = (Spinner) getView().findViewById(R.id.input_expense_category);
        editTextDescription = (EditText) getView().findViewById(R.id.input_expense_description);
        editTextCost = (EditText) getView().findViewById(R.id.input_expense_cost);
        editTextDate = (EditText) getView().findViewById(R.id.input_expense_date);
        updateLabel();
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        buttonAdd = (Button) getView().findViewById(R.id.button_add_expense);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpense();
                Fragment destinationFragment = new ExpenseTrackerFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, destinationFragment).commit();

            }
        });
        if (bundle != null) {
            if (bundle.containsKey(getContext().getResources().getString(R.string.id_column_title))) {
                isEditEntry = true;
                id = bundle.getInt(getContext().getResources().getString(R.string.id_column_title));
                getActivity().setTitle(R.string.fragment_edit_expense_title);
                editTextDate.setText(bundle.getString(getContext().getResources().getString(R.string.date_column_title)));
                editTextCost.setText(bundle.getString(getContext().getResources().getString(R.string.cost_column_title)));
                editTextDescription.setText(bundle.getString(getContext().getResources().getString(R.string.description_column_title)));
            } else {
                getActivity().setTitle(R.string.fragment_add_expense_title);
            }
        }else {
            getActivity().setTitle(R.string.fragment_add_expense_title);
        }
        setHasOptionsMenu(true);

        final SpinnerCategoryAdapter adapter = new SpinnerCategoryAdapter();
        expenseViewModel.getAllBudgetCategories().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> categories) {
                ArrayList<String> categoryNames = new ArrayList<>();
                adapter.setCategory(categories);
                for (int i = 0; i < adapter.getItemCount(); i++) {
                    categoryNames.add(String.valueOf(categories.get(i)));
                }
                Collections.sort(categoryNames);
                createSpinner(categoryNames);
                if (isEditEntry) {
                    spinnerCategory.setSelection(getIndex(spinnerCategory
                            , bundle.getString(getContext().getResources().getString(R.string.category_column_title))));
                }
            }
        });
           }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_expense, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_back:
                Fragment destinationFragment = new ExpenseTrackerFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, destinationFragment).commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void createSpinner(ArrayList<String> categories) {
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, categories);
        spinnerCategory.setAdapter(adp1);

    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    private void updateLabel() {
        String myFormat = "yy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editTextDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void saveExpense() {
        //saves the expense entry using the expenseViewModel, then clears the lines and resets the calendar

        String date = editTextDate.getText().toString();
        String category = spinnerCategory.getSelectedItem().toString();
        String description = editTextDescription.getText().toString();
        String cost = editTextCost.getText().toString();

        if (date.trim().isEmpty() || description.trim().isEmpty() || category.trim().isEmpty()
                || cost.trim().isEmpty()) {
            Toast.makeText(getView().getContext(), "Cannot enter blank fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isEditEntry) {
            expenseViewModel.update(new ExpenseEntry(id,category,description,date,cost));
            Toast.makeText(getView().getContext(), "Expense updated", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            expenseViewModel.insert(new ExpenseEntry(category, description, date, cost));
            Toast.makeText(getView().getContext(), "Expense added", Toast.LENGTH_SHORT).show();
            return;
        }
//        editTextDescription.setText("");
//        editTextCost.setText("");
//        Calendar today = Calendar.getInstance();
//        myCalendar.set(today.get(Calendar.YEAR) - 1900, today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
//        updateLabel();


    }

}
