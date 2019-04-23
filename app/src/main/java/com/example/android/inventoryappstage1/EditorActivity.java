package com.example.android.inventoryappstage1;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.inventoryappstage1.data.InventoryContract;
import com.example.android.inventoryappstage1.data.InventoryContract.InvEntry;

public class EditorActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_INVENTORY_LOADER = 0;
    private Uri mCurrentProductUri;

    private EditText pNameEditText;
    private EditText pPriceEditText;
    private EditText pQuantityEditText;
    private EditText pSupplierPhoneEditText;

    private Spinner pSupplierNameSpinner;

    private int pSupplierName = InventoryContract.InvEntry.SUPPLIER_UNKNOWN;


    private boolean pProductHasChanged = false;

    private View.OnTouchListener pTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            pProductHasChanged = true;
            Log.d("system", "onTouch");

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        Log.d("system", "onCreate");

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        if (mCurrentProductUri == null) {
            setTitle(getString(R.string.add_product));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.edit_product));
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }
        pNameEditText = findViewById(R.id.edit_product_name);
        pPriceEditText = findViewById(R.id.edit_product_price);
        pQuantityEditText = findViewById(R.id.edit_product_quantity);
        pSupplierNameSpinner = findViewById(R.id.spinner_product_supplier_name);
        pSupplierPhoneEditText = findViewById(R.id.edit_product_Supplier_phone);

        pNameEditText.setOnTouchListener(pTouchListener);
        pPriceEditText.setOnTouchListener(pTouchListener);
        pQuantityEditText.setOnTouchListener(pTouchListener);
        pSupplierNameSpinner.setOnTouchListener(pTouchListener);
        pSupplierPhoneEditText.setOnTouchListener(pTouchListener);
        setupSpinner();
    }


    private void setupSpinner() {
        ArrayAdapter productSupplieNameSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_product_options, android.R.layout.simple_spinner_item);

        productSupplieNameSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        pSupplierNameSpinner.setAdapter(productSupplieNameSpinnerAdapter);

        pSupplierNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.supplier_amazon))) {
                        pSupplierName = InvEntry.SUPPLIER_AMAZON;
                    } else if (selection.equals(getString(R.string.supplier_souq))) {
                        pSupplierName = InvEntry.SUPPLIER_SOUQ;
                    } else if (selection.equals(getString(R.string.supplier_ebay))) {
                        pSupplierName = InvEntry.SUPPLIER_EBAY;
                    } else {
                        pSupplierName = InvEntry.SUPPLIER_UNKNOWN;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pSupplierName = InvEntry.SUPPLIER_UNKNOWN;
            }
        });
    }

    private void saveProduct() {
        String productNameString = pNameEditText.getText().toString().trim();
        String productPriceString = pPriceEditText.getText().toString().trim();
        String productQuantityString = pQuantityEditText.getText().toString().trim();
        String productSupplierPhoneNumberString = pSupplierPhoneEditText.getText().toString().trim();
        if (mCurrentProductUri == null) {
            if (TextUtils.isEmpty(productNameString)) {
                Toast.makeText(this, getString(R.string.product_name_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productPriceString)) {
                Toast.makeText(this, getString(R.string.price_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productQuantityString)) {
                Toast.makeText(this, getString(R.string.quantity_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (pSupplierName == InvEntry.SUPPLIER_UNKNOWN) {
                Toast.makeText(this, getString(R.string.supplier_name_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productSupplierPhoneNumberString)) {
                Toast.makeText(this, getString(R.string.supplier_phone_requires), Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();

            values.put(InvEntry.COLUMN_PRODUCT_NAME, productNameString);
            values.put(InvEntry.COLUMN_PRODUCT_PRICE, productPriceString);
            values.put(InvEntry.COLUMN_PRODUCT_QUANTITY, productQuantityString);
            values.put(InvEntry.COLUMN_PRODUCT_SUPPLIER_NAME, pSupplierName);
            values.put(InvEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, productSupplierPhoneNumberString);

            Uri newUri = getContentResolver().insert(InvEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.insert_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.insert_successful),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }else{

            if (TextUtils.isEmpty(productNameString)) {
                Toast.makeText(this, getString(R.string.product_name_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productPriceString)) {
                Toast.makeText(this, getString(R.string.price_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productQuantityString)) {
                Toast.makeText(this, getString(R.string.quantity_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (pSupplierName == InvEntry.SUPPLIER_UNKNOWN) {
                Toast.makeText(this, getString(R.string.supplier_name_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productSupplierPhoneNumberString)) {
                Toast.makeText(this, getString(R.string.supplier_phone_requires), Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();

            values.put(InvEntry.COLUMN_PRODUCT_NAME, productNameString);
            values.put(InvEntry.COLUMN_PRODUCT_PRICE, productPriceString);
            values.put(InvEntry.COLUMN_PRODUCT_QUANTITY, productQuantityString);
            values.put(InvEntry.COLUMN_PRODUCT_SUPPLIER_NAME, pSupplierName);
            values.put(InvEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, productSupplierPhoneNumberString);


            int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.update_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.update_successful),
                        Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        Log.d("message", "open Editor Activity");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveProduct();
                return true;
            case android.R.id.home:
                if (!pProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!pProductHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InvEntry._ID,
                InvEntry.COLUMN_PRODUCT_NAME,
                InvEntry.COLUMN_PRODUCT_PRICE,
                InvEntry.COLUMN_PRODUCT_QUANTITY,
                InvEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                InvEntry.COLUMN_PRODUCT_SUPPLIER_PHONE
        };
        return new CursorLoader(this,
                mCurrentProductUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(InvEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InvEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InvEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(InvEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(InvEntry.COLUMN_PRODUCT_SUPPLIER_PHONE);

            String currentName = cursor.getString(nameColumnIndex);
            int currentPrice = cursor.getInt(priceColumnIndex);
            int currentQuantity = cursor.getInt(quantityColumnIndex);
            int currentSupplierName = cursor.getInt(supplierNameColumnIndex);
            int currentSupplierPhone = cursor.getInt(supplierPhoneColumnIndex);

            pNameEditText.setText(currentName);
            pPriceEditText.setText(Integer.toString(currentPrice));
            pQuantityEditText.setText(Integer.toString(currentQuantity));
            pSupplierPhoneEditText.setText(Integer.toString(currentSupplierPhone));

            switch (currentSupplierName) {
                case InvEntry.SUPPLIER_AMAZON:
                    pSupplierNameSpinner.setSelection(1);
                    break;
                case InvEntry.SUPPLIER_SOUQ:
                    pSupplierNameSpinner.setSelection(2);
                    break;
                case InvEntry.SUPPLIER_EBAY:
                    pSupplierNameSpinner.setSelection(3);
                    break;
                default:
                    pSupplierNameSpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        pNameEditText.setText("");
        pPriceEditText.setText("");
        pQuantityEditText.setText("");
        pSupplierPhoneEditText.setText("");
        pSupplierNameSpinner.setSelection(0);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
