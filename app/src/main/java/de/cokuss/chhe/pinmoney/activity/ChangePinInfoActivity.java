package de.cokuss.chhe.pinmoney.activity;import android.app.DatePickerDialog;import android.content.Intent;import android.support.v7.app.ActionBar;import android.support.v7.app.AppCompatActivity;import android.support.v7.widget.Toolbar;import android.text.Editable;import android.text.TextWatcher;import android.util.Log;import android.view.Menu;import android.view.MenuItem;import android.view.View;import android.widget.Button;import android.widget.DatePicker;import android.widget.EditText;import android.widget.RadioButton;import android.widget.RadioGroup;import android.widget.TextView;import org.androidannotations.annotations.AfterTextChange;import org.androidannotations.annotations.AfterViews;import org.androidannotations.annotations.Click;import org.androidannotations.annotations.EActivity;import org.androidannotations.annotations.OptionsMenu;import org.androidannotations.annotations.ViewById;import java.util.Calendar;import de.cokuss.chhe.pinmoney.fundamentals.Cycle;import de.cokuss.chhe.pinmoney.DAOImplSQLight;import de.cokuss.chhe.pinmoney.DateHelper;import de.cokuss.chhe.pinmoney.fundamentals.Payments;import de.cokuss.chhe.pinmoney.PinMoneyEntry;import de.cokuss.chhe.pinmoney.R;import de.cokuss.chhe.pinmoney.help.HelpBookingActivity_;import de.cokuss.chhe.pinmoney.help.HelpChangePinActivity_;//@OptionsMenu(R.menu.main_menu_change_pin)@EActivity(R.layout.activity_change_pin_info)public class ChangePinInfoActivity extends AppCompatActivity {    private static final String LOG_TAG = ChangePinInfoActivity.class.getSimpleName();    private final Calendar c = Calendar.getInstance();    DateHelper dateHelper = new DateHelper();    private String inhaber;    private PinMoneyEntry pinMoneyEntry;    private DAOImplSQLight daoImplSQLight = DAOImplSQLight.getInstance(ChangePinInfoActivity.this);    // i don't use annotation on changeButton cause i need to use it in showActiveButton    private Button changeButton;    private Cycle cycle;    private float value;    private int mYear = c.get(Calendar.YEAR);    private int mMonth = c.get(Calendar.MONTH);    private int mDay = c.get(Calendar.DAY_OF_MONTH);    private boolean clickbar = false;    boolean firstChangeValue = false;    boolean firstChangeDate = false;    //private DecimalFormat df = new DecimalFormat( "###,##0.00" );    @ViewById(R.id.cycle)    RadioGroup itemtypeGroup;    @ViewById    EditText valueField, dateField;    @ViewById(R.id.rbDay)    RadioButton radioDay;    @ViewById(R.id.rbWeek)    RadioButton radioWeek;    @ViewById(R.id.rbMonth)    RadioButton radioMonth;    @ViewById(R.id.inhaber)    TextView tvInhaber;    private void log(String string) {        if (string == null) {            Log.d(LOG_TAG, "Parameter ist Null");        } else {            Log.d(LOG_TAG, string);        }    }    @AfterViews    protected void init() {        // i don't use annotation on changeButton cause i need to use it in showActiveButton        changeButton = (Button) findViewById(R.id.variButton);        inhaber = getIntent().getStringExtra("Inhaber");        tvInhaber.setText(inhaber);        showCurrentState();        setListeners();        initToolbar();    }    @AfterTextChange    void valueFieldAfterTextChanged(TextView valueField) {        /* the first time this is called while setting the values via showCurrentState        this should not call showActiveButton only the secound invocation does the call */        if (!firstChangeValue) {            firstChangeValue = true;        } else showActiveButton();    }    @AfterTextChange    void dateFieldAfterTextChanged(TextView dateField) {        /* the first time this is called while setting the values via showCurrentState        this should not call showActiveButton only the secound invocation does the call */        if (!firstChangeDate) {            firstChangeDate = true;        } else showActiveButton();    }    void setListeners() {        //listen to the changes in cycle        itemtypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {            @Override            public void onCheckedChanged(RadioGroup group, int checkedId) {                showActiveButton();            }        });        changeButton.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                if (clickbar) {                    //get cycle                    switch (itemtypeGroup.getCheckedRadioButtonId()) {                        case R.id.rbDay:                            cycle = Cycle.TAEGLICH;                            break;                        case R.id.rbWeek:                            cycle = Cycle.WOECHENTLICH;                            break;                        case R.id.rbMonth:                            cycle = Cycle.MONATLICH;                            break;                    }                    //get value                    //Todo Immer das alte Thema , oder .                    value = Float.valueOf(valueField.getText().toString());                    //create Payment                    //NullPointer                    log("Date " + dateField.getText().toString() + " Value " + value);                    Payments newPayments = new Payments(dateHelper.string2Date(dateField.getText().toString()), cycle, value);                    //Todo abschliessende Buchung ??? Letzte Chance ??? Wie funktioniert so eine Änderung ?                    //add the Entry                    daoImplSQLight.addEntryToPinMoney(inhaber, pinMoneyEntry.getBirthDate(), newPayments, "changed");                    finish();                }            }        });    }    private void initToolbar() {        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);        setSupportActionBar(toolbar);        if (getSupportActionBar() != null) {            getSupportActionBar().setDisplayHomeAsUpEnabled(true);        }        ActionBar actionBar = getSupportActionBar();        actionBar.setDisplayShowHomeEnabled(true);        actionBar.setIcon(R.mipmap.ic_launcher_change);    }    @Click(R.id.changeStartButton)    void datePickerButton(View v) {        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),                new DatePickerDialog.OnDateSetListener() {                    @Override                    public void onDateSet(DatePicker view, int year,                                          int monthOfYear, int dayOfMonth) {                        dateField.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);                    }                }, mYear, mMonth, mDay);        datePickerDialog.show();    }    private void showCurrentState() {        pinMoneyEntry = daoImplSQLight.getEntryFromPinMoney(getApplicationContext(), inhaber);        Payments payments = pinMoneyEntry.getPayments();        valueField.setText(String.valueOf(payments.getBetrag()));        dateField.setText(dateHelper.sdfShort.format(payments.getDate()));        switch (payments.getTurnusStrShort()) {            case "t":                radioDay.setChecked(true);                break;            case "w":                radioWeek.setChecked(true);                break;            case "m":                radioMonth.setChecked(true);                break;        }    }    private void showActiveButton() {        changeButton.setBackgroundResource(R.drawable.button_change);        changeButton.isClickable();        clickbar = true;    }    @Override    public boolean onCreateOptionsMenu(Menu menu) {        // Inflate the menu; this adds items to the action bar if it is present.        getMenuInflater().inflate(R.menu.main_menu_change_pin, menu);        return true;    }    @Override    public boolean onOptionsItemSelected(MenuItem item) {        switch (item.getItemId()) {            case R.id.action_help_change_pin:                Intent intent1 = new Intent(this, HelpChangePinActivity_.class);                startActivity(intent1);                return true;        }        return super.onOptionsItemSelected(item);    }}