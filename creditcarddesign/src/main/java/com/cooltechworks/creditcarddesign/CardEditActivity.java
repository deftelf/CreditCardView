package com.cooltechworks.creditcarddesign;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cooltechworks.creditcarddesign.pager.CardCVVEntryView;
import com.cooltechworks.creditcarddesign.pager.CardExpiryEntryView;
import com.cooltechworks.creditcarddesign.pager.CardNameEntryView;
import com.cooltechworks.creditcarddesign.pager.CardNumberEntryView;
import com.cooltechworks.creditcarddesign.pager.CreditCardEntryView;
import com.cooltechworks.creditcarddesign.pager.IActionListener;

import static com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_CVV;
import static com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_EXPIRY;
import static com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_HOLDER_NAME;
import static com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_NUMBER;


public class CardEditActivity extends AppCompatActivity {


    int mLastPageSelected = 0;
    private CreditCardView mCreditCardView;

    private String mCardNumber;
    private String mCVV;
    private String mCardHolderName;
    private String mExpiry;
    private Adapter mCardAdapter;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.cooltechworks.creditcarddesign.R.layout.activity_card_edit);

        findViewById(com.cooltechworks.creditcarddesign.R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewPager pager = (ViewPager) findViewById(com.cooltechworks.creditcarddesign.R.id.card_field_container_pager);

                int max = pager.getAdapter().getCount();

                if(pager.getCurrentItem() == max -1) {
                    // if last card.
                    onDoneTapped();
                }
                else {
                    showNext();
                }
            }
        });
        findViewById(com.cooltechworks.creditcarddesign.R.id.previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrevious();
            }
        });

        setKeyboardVisibility(true);
        mCreditCardView = (CreditCardView) findViewById(com.cooltechworks.creditcarddesign.R.id.credit_card_view);

        Bundle state;
        if(savedInstanceState != null) {
            state = savedInstanceState;
        }
        else {
            state = getIntent().getExtras();
        }


        checkParams(state);
        loadPager();


    }

    private void checkParams(Bundle bundle) {


        if(bundle == null) {
            return;
        }
        mCardHolderName = bundle.getString(EXTRA_CARD_HOLDER_NAME);
        mCVV = bundle.getString(EXTRA_CARD_CVV);
        mExpiry = bundle.getString(EXTRA_CARD_EXPIRY);
        mCardNumber = bundle.getString(EXTRA_CARD_NUMBER);


        mCreditCardView.setCVV(mCVV);
        mCreditCardView.setCardHolderName(mCardHolderName);
        mCreditCardView.setCardExpiry(mExpiry);
        mCreditCardView.setCardNumber(mCardNumber);



        if(mCardAdapter != null) {
            mCardAdapter.notifyDataSetChanged();
        }
    }

    public void refreshNextButton() {

        int max = pager.getAdapter().getCount();

        int text = com.cooltechworks.creditcarddesign.R.string.next;

        if(pager.getCurrentItem() == max -1) {
            text = com.cooltechworks.creditcarddesign.R.string.done;
        }

        ((TextView)findViewById(com.cooltechworks.creditcarddesign.R.id.next)).setText(text);
    }

    public void loadPager() {
        pager = (ViewPager) findViewById(R.id.card_field_container_pager);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                mCardAdapter.focus(position);

                if (position == 2) {
                    mCreditCardView.showBack();
                } else if ((position == 1 && mLastPageSelected == 2) || position == 3) {
                    mCreditCardView.showFront();
                }

                mLastPageSelected = position;

                refreshNextButton();

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        pager.setOffscreenPageLimit(4);

        mCardAdapter = new Adapter();
        mCardAdapter.setOnCardEntryCompleteListener(new ICardEntryCompleteListener() {
            @Override
            public void onCardEntryComplete(CreditCardEntryView view) {
                showNext();
            }

            @Override
            public void onCardEntryEdit(CreditCardEntryView view, String entryValue) {
                if (view instanceof CardNumberEntryView) {
                    mCardNumber = entryValue.replace(CreditCardUtils.SPACE_SEPERATOR, "");
                    mCreditCardView.setCardNumber(mCardNumber);
                } else if (view instanceof CardExpiryEntryView) {
                    mExpiry = entryValue;
                    mCreditCardView.setCardExpiry(entryValue);
                } else if (view instanceof CardCVVEntryView) {
                    mCVV = entryValue;
                    mCreditCardView.setCVV(entryValue);
                } else {
                    mCardHolderName = entryValue;
                    mCreditCardView.setCardHolderName(entryValue);
                }
            }
        });

        pager.setAdapter(mCardAdapter);
    }

    public void onSaveInstanceState(Bundle outState) {

        outState.putString(EXTRA_CARD_CVV,mCVV);
        outState.putString(EXTRA_CARD_HOLDER_NAME,mCardHolderName);
        outState.putString(EXTRA_CARD_EXPIRY,mExpiry);
        outState.putString(EXTRA_CARD_NUMBER,mCardNumber);

        super.onSaveInstanceState(outState);
    }


    public void showPrevious() {

        final ViewPager pager = (ViewPager) findViewById(com.cooltechworks.creditcarddesign.R.id.card_field_container_pager);
        int currentIndex = pager.getCurrentItem();

        if (currentIndex - 1 >= 0) {
            pager.setCurrentItem(currentIndex - 1);
        }

        refreshNextButton();
    }

    public void showNext() {

        final ViewPager pager = (ViewPager) findViewById(com.cooltechworks.creditcarddesign.R.id.card_field_container_pager);
        Adapter adapter = (Adapter) pager.getAdapter();

        int max = adapter.getCount();
        int currentIndex = pager.getCurrentItem();

        if (currentIndex + 1 < max) {

            pager.setCurrentItem(currentIndex + 1);
        } else {
            // completed the card entry.
            setKeyboardVisibility(false);
        }

        refreshNextButton();
    }

    private void onDoneTapped() {

        Intent intent = new Intent();

        intent.putExtra(EXTRA_CARD_CVV, mCVV);
        intent.putExtra(EXTRA_CARD_HOLDER_NAME, mCardHolderName);
        intent.putExtra(EXTRA_CARD_EXPIRY, mExpiry);
        intent.putExtra(EXTRA_CARD_NUMBER, mCardNumber);


        setResult(RESULT_OK,intent);
        finish();


    }

    // from the link above
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {

            LinearLayout parent = (LinearLayout) findViewById(com.cooltechworks.creditcarddesign.R.id.parent);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) parent.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
            parent.setLayoutParams(layoutParams);

        }
    }

    private void setKeyboardVisibility(boolean visible) {

        final EditText editText = (EditText) findViewById(com.cooltechworks.creditcarddesign.R.id.card_number_field);


        if (!visible) {

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }


    private class Adapter extends PagerAdapter implements IActionListener {


        private ICardEntryCompleteListener onCardEntryCompleteListener;

        public Adapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            CreditCardEntryView v;
            switch (position) {
                case 0:
                    v = new CardNumberEntryView(CardEditActivity.this, mCardNumber);
                    container.addView(v);
                    break;
                case 1:
                    v = new CardExpiryEntryView(CardEditActivity.this, mExpiry);
                    container.addView(v);
                    break;
                case 2:
                    v = new CardCVVEntryView(CardEditActivity.this, mCVV);
                    container.addView(v);
                    break;
                default:
                    v = new CardNameEntryView(CardEditActivity.this, mCardHolderName);
                    container.addView(v);
                    break;
            }
            v.setActionListener(this);
            return v;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public void setOnCardEntryCompleteListener(ICardEntryCompleteListener onCardEntryCompleteListener) {
            this.onCardEntryCompleteListener = onCardEntryCompleteListener;
        }

        public void focus(int position) {

        }

        @Override
        public void onActionComplete(CreditCardEntryView fragment) {
            showNext();
        }

        @Override
        public void onEdit(CreditCardEntryView fragment, String edit) {
            onCardEntryCompleteListener.onCardEntryEdit(fragment, edit);
        }
    }

    public interface ICardEntryCompleteListener {
        void onCardEntryComplete(CreditCardEntryView view);

        void onCardEntryEdit(CreditCardEntryView view, String entryValue);
    }
}
