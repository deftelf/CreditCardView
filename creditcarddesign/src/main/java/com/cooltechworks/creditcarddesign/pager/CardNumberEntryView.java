package com.cooltechworks.creditcarddesign.pager;

import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.cooltechworks.creditcarddesign.R;

import static com.cooltechworks.creditcarddesign.CreditCardUtils.MAX_LENGTH_CARD_NUMBER;
import static com.cooltechworks.creditcarddesign.CreditCardUtils.MAX_LENGTH_CARD_NUMBER_WITH_SPACES;

/**
 * Created by sharish on 9/1/15.
 */
public class CardNumberEntryView extends CreditCardEntryView {

    EditText mCardNumberView;

    public CardNumberEntryView(Context context, String number) {
        super(context);

        View v = LayoutInflater.from(getContext()).inflate(R.layout.lyt_card_number, this, true);
        mCardNumberView = (EditText) v.findViewById(R.id.card_number_field);
        mCardNumberView.addTextChangedListener(this);

        if (number == null) {
            number = "";
        }

        mCardNumberView.setText(number);
    }


    @Override
    public void afterTextChanged(Editable s) {

        int cursorPosition = mCardNumberView.getSelectionEnd();
        int previousLength = mCardNumberView.getText().length();

        String cardNumber = CreditCardUtils.handleCardNumber(s.toString());
        int modifiedLength = cardNumber.length();

        mCardNumberView.removeTextChangedListener(this);
        mCardNumberView.setText(cardNumber);
        mCardNumberView.setSelection(cardNumber.length() > MAX_LENGTH_CARD_NUMBER_WITH_SPACES ? MAX_LENGTH_CARD_NUMBER_WITH_SPACES : cardNumber.length());
        mCardNumberView.addTextChangedListener(this);

        if(modifiedLength <= previousLength && cursorPosition < modifiedLength) {
            mCardNumberView.setSelection(cursorPosition);
        }

        onEdit(cardNumber);


        if(cardNumber.replace(CreditCardUtils.SPACE_SEPERATOR,"").length() == MAX_LENGTH_CARD_NUMBER) {
            onComplete();
        }
    }

    @Override
    public void focus() {
        mCardNumberView.selectAll();
    }
}
