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
public class CardCVVEntryView extends CreditCardEntryView {

    private EditText mCardCVVView;

    public CardCVVEntryView(Context context, String cvv) {
        super(context);

        View v = LayoutInflater.from(getContext()).inflate(R.layout.lyt_card_cvv, this, true);
        mCardCVVView = (EditText) v.findViewById(R.id.card_cvv);
        mCardCVVView.addTextChangedListener(this);

        if(cvv == null) {
            cvv = "";
        }

        mCardCVVView.setText(cvv);
    }

    @Override
    public void afterTextChanged(Editable s) {

        onEdit(s.toString());
        if(s.length() == 3) {
            onComplete();
        }
    }

    @Override
    public void focus() {
        mCardCVVView.selectAll();
    }
}
