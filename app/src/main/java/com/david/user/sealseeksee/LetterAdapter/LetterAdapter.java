package com.david.user.sealseeksee.LetterAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.david.user.sealseeksee.R;

import java.util.List;

/**
 * Created by yarolegovich on 17.04.2016.
 */
public class LetterAdapter extends ArrayAdapter<LetterOption> {

    public LetterAdapter(Context context, List<LetterOption> letterOptions) {
        super(context, 0, letterOptions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.letter_option, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else vh = (ViewHolder) convertView.getTag();

        LetterOption option = getItem(position);

        vh.description.setText(option.description);
        vh.amount.setText(option.amount);

        return convertView;
    }

    private static final class ViewHolder {
        TextView description;
        TextView amount;

        public ViewHolder(View v) {
            description = (TextView) v.findViewById(R.id.description);
            amount = (TextView) v.findViewById(R.id.amount);
        }
    }
}