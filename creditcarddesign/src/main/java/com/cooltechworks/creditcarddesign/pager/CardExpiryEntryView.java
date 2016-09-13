package com.cooltechworks.creditcarddesign.pager;

import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.cooltechworks.creditcarddesign.R;

import java.util.Calendar;

/**
 * Created by sharish on 9/1/15.
 */
public class CardExpiryEntryView extends CreditCardEntryView {

    EditText cardExpiryView;

    private boolean mValidateCard = true;

    public CardExpiryEntryView(Context context, String expiry) {
        super(context);

        View v = LayoutInflater.from(getContext()).inflate(R.layout.lyt_card_expiry, this, true);
        cardExpiryView = (EditText) v.findViewById(R.id.card_expiry);

        if(expiry == null) {
            expiry = "";
        }

        cardExpiryView.setText(expiry);
        cardExpiryView.addTextChangedListener(this);
    }

    @Override
    public void afterTextChanged(Editable s) {

        String text = s.toString().replace(CreditCardUtils.SLASH_SEPERATOR, "");

        String month, year="";
        if(text.length() >= 2) {
            month = text.substring(0, 2);

            if(text.length() > 2) {
                year = text.substring(2);
            }

            if(mValidateCard) {
                int mm = Integer.parseInt(month);

                if (mm <= 0 || mm >= 13) {
                    cardExpiryView.setError("Invalid month");
                    return;
                }

                if (text.length() >= 4) {

                    int yy = Integer.parseInt(year);

                    final Calendar calendar = Calendar.getInstance();
                    int currentYear = calendar.get(Calendar.YEAR);
                    int currentMonth = calendar.get(Calendar.MONTH) + 1;

                    int millenium = (currentYear / 1000) * 1000;


                    if (yy + millenium < currentYear) {
                        cardExpiryView.setError("Card expired");
                        return;
                    } else if (yy + millenium == currentYear && mm < currentMonth) {
                        cardExpiryView.setError("Card expired");
                        return;
                    }
                }
            }

        }
        else {
            month = text;
        }

        int previousLength = cardExpiryView.getText().length();
        int cursorPosition = cardExpiryView.getSelectionEnd();

        text = CreditCardUtils.handleExpiration(month,year);

        cardExpiryView.removeTextChangedListener(this);
        cardExpiryView.setText(text);
        cardExpiryView.setSelection(text.length());
        cardExpiryView.addTextChangedListener(this);

        int modifiedLength = text.length();

        if(modifiedLength <= previousLength && cursorPosition < modifiedLength) {
            cardExpiryView.setSelection(cursorPosition);
        }

        onEdit(text);

        if(text.length() == 5) {
            onComplete();
        }

    }


    @Override
    public void focus() {

         cardExpiryView.selectAll();
    }


//    public void onSaveInstanceState(Bundle outState) {
//
//        outState.putBoolean(EXTRA_VALIDATE_EXPIRY_DATE, mValidateCard);
//        super.onSaveInstanceState(outState);
//    }
//
//    public void onActivityCreated(Bundle instate) {
//
//        if(instate != null) {
//            mValidateCard = instate.getBoolean(EXTRA_VALIDATE_EXPIRY_DATE, mValidateCard);
//        }
//
//        super.onActivityCreated(instate);
//    }
}
