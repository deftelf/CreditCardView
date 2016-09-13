package com.cooltechworks.creditcarddesign.pager;

public interface IActionListener {
    void onActionComplete(CreditCardEntryView fragment);

    void onEdit(CreditCardEntryView fragment, String edit);
}