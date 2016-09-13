package com.cooltechworks.creditcarddesign.pager;

import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.cooltechworks.creditcarddesign.R;

/**
 * Created by sharish on 9/1/15.
 */
public class CardNameEntryView extends CreditCardEntryView {


    private EditText mCardNameView;

    public CardNameEntryView(Context context, String name) {
        super(context);

        View v = LayoutInflater.from(context).inflate(R.layout.lyt_card_holder_name, this, true);
        mCardNameView = (EditText) v.findViewById(R.id.card_name);
        mCardNameView.addTextChangedListener(this);

        if(name == null) {
            name = "";
        }

        mCardNameView.setText(name);
    }

    @Override
    public void afterTextChanged(Editable s) {

        onEdit(s.toString());
        if(s.length() == 16) {
            onComplete();
        }
    }

    @Override
    public void focus() {
        mCardNameView.selectAll();
    }
}
