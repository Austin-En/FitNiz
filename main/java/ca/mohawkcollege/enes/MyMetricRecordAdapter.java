package ca.mohawkcollege.enes;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class MyMetricRecordAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final String[] dates;

    /**
     * This adapter is used to display the listview inside the results page containing all of the user metrics to be tracked
     * @param context
     * @param dates This contains strings of all of the metrics for the textfields
     * @param values This contains the values to be displayed such as time spent on the workout/cardio
     */
    public MyMetricRecordAdapter(Context context, String[] dates, String[] values) {
        super(context, R.layout.list_item, values);
        this.context = context;
        this.dates = dates;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        final EditText editText = (EditText) rowView.findViewById(R.id.edit_text);

        editText.setText(values[position]);

        editText.setEnabled(false);

        final TextView textView = (TextView) rowView.findViewById(R.id.text_view);

        textView.setText(dates[position]);


            editText.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    values[position] = editText.getText().toString();
                    ResultsActivity.finalValues = values;
                }
            });


        return rowView;
    }
}
