package com.utro.bmw.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import com.utro.bmw.R;
import com.utro.bmw.SQLDatabaseManagers.AccountDBManager;
import com.utro.bmw.model.Service;

import java.util.ArrayList;

/**
 * Created by 123 on 21.06.2015.
 */
public class ServicesOrderActivity extends BaseActivity implements View.OnClickListener {

    Button btnRegister;
    EditText editSurname;
    EditText editName;
    EditText editLastname;
    EditText editEmail;
    EditText editPhone;

    AccountDBManager accountDB;

    @Override
    protected int getLayoutResourceIdentifier() {
        return R.layout.activity_register_service;
    }

    @Override
    protected String getTitleToolBar() {
        return getString(R.string.title_activity_register_service);
    }

    @Override
    protected boolean getDisplayHomeAsUp() {
        return false;
    }

    @Override
    protected boolean getHomeButtonEnabled() {
        return false;
    }

    @Override
    protected boolean getDisplayShowTitleEnabled() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView toolbarTitle = (TextView)mToolBar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTextColor(getResources().getColor(R.color.toolbar_text_gray));

        /*btnClear = (Button)mToolBar.findViewById(R.id.btn_right);
        btnClear.setBackground(getResources().getDrawable(R.drawable.ic_clear_grey600_24dp));
        btnClear.setVisibility(View.VISIBLE);*/

        accountDB = new AccountDBManager(mContext);
        btnRegister = (Button) findViewById(R.id.register_btn);
        btnRegister.setOnClickListener(this);

        editSurname = (EditText) findViewById(R.id.edit_text_surname);
        editName  = (EditText) findViewById(R.id.edit_text_name);
        editLastname = (EditText) findViewById(R.id.edit_text_lastname);
        editEmail = (EditText) findViewById(R.id. edit_text_email);
        editPhone = (EditText) findViewById(R.id.edit_text_phone);

        ArrayList<Object> row;
        if (accountDB.fact())
        {
            row = accountDB.getRowAsArray();
            editName.setText(row.get(1).toString());
            editSurname.setText(row.get(2).toString());
            editLastname.setText(row.get(3).toString());
            editEmail.setText(row.get(4).toString());
            editPhone.setText(row.get(5).toString());
        }
    }

    @Override
    public void onClick(View view){
        switch(view.getId()) {
            case R.id.register_btn:
                //TODO check empty's of edittexts
                Service.postOrder(editName.getText().toString(), editSurname.getText().toString(), editLastname.getText().toString(), editPhone.getText().toString(), editEmail.getText().toString(), "26.06.2015 19:23");
                if(accountDB.fact()){
                    accountDB.updateRow(editName.getText().toString(), editSurname.getText().toString(), editLastname.getText().toString(), editEmail.getText().toString(), editPhone.getText().toString());
                }else
                    accountDB.addRow(editName.getText().toString(), editSurname.getText().toString(), editLastname.getText().toString(), editEmail.getText().toString(), editPhone.getText().toString());
                finish();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_services, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_btn_close:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
