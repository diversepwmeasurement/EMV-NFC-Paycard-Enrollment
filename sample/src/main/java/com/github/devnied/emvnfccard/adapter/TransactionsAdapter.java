package com.github.devnied.emvnfccard.adapter;

import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.devnied.emvnfccard.R;
import com.github.devnied.emvnfccard.model.EmvTransactionRecord;
import com.github.devnied.emvnfccard.utils.ViewHolder;
import com.github.devnied.emvnfccard.utils.ViewUtils;

public class TransactionsAdapter extends BaseExpandableListAdapter {

	/**
	 * Transaction records
	 */
	private List<EmvTransactionRecord> mTransactionRecord;

	/**
	 * Constructor using fields
	 * 
	 * @param pTransactions
	 */
	public TransactionsAdapter(final List<EmvTransactionRecord> pTransactions) {
		mTransactionRecord = pTransactions;
	}

	@Override
	public int getGroupCount() {
		return mTransactionRecord.size();
	}

	@Override
	public int getChildrenCount(final int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(final int groupPosition) {
		return mTransactionRecord.get(groupPosition);
	}

	@Override
	public Object getChild(final int groupPosition, final int childPosition) {
		return mTransactionRecord.get(groupPosition);
	}

	@Override
	public long getGroupId(final int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(final int groupPosition, final int childPosition) {
		return groupPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(final int groupPosition, final boolean isExpanded, final View convertView, final ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			v = View.inflate(parent.getContext(), R.layout.transaction_item, null);
		}
		LinearLayout layout = ViewHolder.get(v, R.id.transaction_group_card);
		// Divider
		View divider = ViewHolder.get(v, R.id.transaction_group_divider);
		ImageView indicator = ViewHolder.get(v, R.id.transaction_indicator);
		if (isExpanded) {
			layout.setBackgroundResource(R.drawable.cardui_no_bottom_shadow);
			indicator.setImageResource(R.drawable.expander_close_holo_light);
			divider.setVisibility(View.GONE);
		} else {
			layout.setBackgroundResource(R.drawable.cardui);
			indicator.setImageResource(R.drawable.expander_open_holo_light);
			divider.setVisibility(View.VISIBLE);
		}

		int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10L, parent.getResources().getDisplayMetrics());
		layout.setPadding(value, value, value, value);

		// Get views
		TextView date = ViewHolder.get(v, R.id.transaction_date);
		TextView price = ViewHolder.get(v, R.id.transaction_price);
		TextView symbol = ViewHolder.get(v, R.id.transaction_symbol);

		// Get Transaction
		EmvTransactionRecord record = (EmvTransactionRecord) getGroup(groupPosition);

		// Set date
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
		date.setText(format.format(record.getTransactionDate()));

		// set price amount
		String amount = null;
		if (record.getCurrency() != null) {
			amount = record.getCurrency().format(record.getAmount().longValue());
		} else {
			amount = String.valueOf(record.getAmount());
		}
		price.setText(amount);

		// Set Symbol
		if (record.getCurrency() != null) {
			Currency currency = Currency.getInstance(record.getCurrency().getISOCodeAlpha());
			if (currency != null) {
				symbol.setText(currency.getSymbol(Locale.getDefault()));
			}
		}

		return v;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, final View convertView,
			final ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			v = View.inflate(parent.getContext(), R.layout.transaction_detail, null);
		}

		// Get views
		TextView country = ViewHolder.get(v, R.id.transaction_county);
		TextView cryptogram = ViewHolder.get(v, R.id.transaction_cryptogram);
		TextView type = ViewHolder.get(v, R.id.transaction_type);

		// Get data
		EmvTransactionRecord transaction = (EmvTransactionRecord) getChild(groupPosition, childPosition);

		// Update content
		if (transaction.getTerminalCountry() != null) {
			country.setText(transaction.getTerminalCountry().getName());
		} else {
			country.setText(parent.getContext().getString(R.string.transaction_unknown));
		}
		cryptogram.setText(transaction.getCyptogramData());
		type.setText(ViewUtils.getStringRessourceByName(parent.getContext(), "transaction_type_"
				+ transaction.getTransactionType().getKey()));

		return v;
	}

	@Override
	public boolean isChildSelectable(final int groupPosition, final int childPosition) {
		return true;
	}

}