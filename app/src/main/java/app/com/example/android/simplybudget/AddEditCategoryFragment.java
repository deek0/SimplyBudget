package app.com.example.android.simplybudget;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import app.com.example.android.simplybudget.database.BudgetEntry;

public class AddEditCategoryFragment extends Fragment {
//    fragment used to add and edit budget categories. accessed from the budget overview by add category
//    or clicking on one of the cards in the recycler view
    private BudgetViewModel budgetViewModel;
    private EditText editTextCategory;
    private EditText editTextAmount;
    private CheckBox checkBoxOneTime;
    private Button buttonSaveCategory;
    private Boolean isEditEntry = false;
    private int id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        bundle is passed if editing an entry
        final Bundle bundle = getArguments();
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.fragment_add_category_title);
        budgetViewModel = ViewModelProviders.of(getActivity()).get(BudgetViewModel.class);
        editTextCategory = (EditText)getView().findViewById(R.id.input_budget_category);
        editTextAmount = (EditText)getView().findViewById(R.id.input_budget_amount);
        checkBoxOneTime = (CheckBox) getView().findViewById(R.id.checkbox_one_time_payment);
        buttonSaveCategory = (Button) getView().findViewById(R.id.button_save_budget_category);
        buttonSaveCategory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                saveCategory();
            }
        });
        setHasOptionsMenu(true);
        if (bundle != null) {
//            if there was a bundle passed, we grab the data and set the text fields in the fragment,
//            we store the id for when the entry is updated and set isEditEntry to true for the save method.
//            set the title to edit entry if there was a bundle and add entry if there was not
            if (bundle.containsKey(getContext().getResources().getString(R.string.id_column_title))) {
                isEditEntry = true;
                id = bundle.getInt(getContext().getResources().getString(R.string.id_column_title));
                getActivity().setTitle(R.string.fragment_edit_category_title);
                editTextAmount.setText(bundle.getString(getContext().getResources()
                        .getString(R.string.budgeted_amount_column_title)));
                editTextCategory.setText(bundle.getString(getContext().getResources()
                        .getString(R.string.category_column_title)));
                if (bundle.getBoolean(getContext().getResources()
                        .getString(R.string.scheduled_payment_column_title))){
                    checkBoxOneTime.toggle();
                }
            } else {
                getActivity().setTitle(R.string.fragment_add_category_title);
            }
        } else {
            getActivity().setTitle(R.string.fragment_add_category_title);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_category, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_back:
                Fragment destinationFragment = new BudgetOverviewFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, destinationFragment).commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saveCategory(){
//        grabs the values from the editText boxes in the fragment and stores them in the database
//        calls viewModel.update if its and edit and viewModel.insert if its a new category
//        returns to the expense tracker fragment after saving the category

        String category = editTextCategory.getText().toString();
        String amount = editTextAmount.getText().toString();
        Boolean oneTimePayment = checkBoxOneTime.isChecked();
        if (category.trim().isEmpty()||amount.trim().isEmpty()) {
            Toast.makeText(getView().getContext(), "Cannot enter blank fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isEditEntry){
            budgetViewModel.update(new BudgetEntry(id,category,amount,oneTimePayment,0));
            Toast.makeText(getView().getContext(), "Entry updated", Toast.LENGTH_SHORT).show();
        }
        else {
            budgetViewModel.insert(new BudgetEntry(category, amount, oneTimePayment));
            Toast.makeText(getView().getContext(), "Category added", Toast.LENGTH_SHORT).show();
        }
//        editTextAmount.setText("");
//        editTextCategory.setText("");
//        if (checkBoxOneTime.isChecked()){
//            checkBoxOneTime.toggle();
//        }
        Fragment destinationFragment = new BudgetOverviewFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, destinationFragment).commit();
    }
}
