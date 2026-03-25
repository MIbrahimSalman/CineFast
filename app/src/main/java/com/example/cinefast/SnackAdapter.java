package com.example.cinefast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class SnackAdapter extends BaseAdapter {

    public interface OnQuantityChanged {
        void onChanged();
    }

    private final Context context;
    private final ArrayList<Snack> snacks;
    private final OnQuantityChanged listener;

    public SnackAdapter(Context context, ArrayList<Snack> snacks, OnQuantityChanged listener) {
        this.context = context;
        this.snacks = snacks;
        this.listener = listener;
    }

    @Override public int getCount() { return snacks.size(); }
    @Override public Object getItem(int pos) { return snacks.get(pos); }
    @Override public long getItemId(int pos) { return pos; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_snack_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Snack snack = snacks.get(position);
        holder.imgSnack.setImageResource(snack.getImageResId());
        holder.textName.setText(snack.getName());
        holder.textPrice.setText(String.format(Locale.getDefault(), "$%.2f", snack.getPrice()));
        holder.textQty.setText(String.valueOf(snack.getQuantity()));

        holder.btnPlus.setOnClickListener(v -> {
            snack.setQuantity(snack.getQuantity() + 1);
            holder.textQty.setText(String.valueOf(snack.getQuantity()));
            if (listener != null) listener.onChanged();
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (snack.getQuantity() > 0) {
                snack.setQuantity(snack.getQuantity() - 1);
                holder.textQty.setText(String.valueOf(snack.getQuantity()));
                if (listener != null) listener.onChanged();
            }
        });

        return convertView;
    }

    public double getTotalSnackCost() {
        double total = 0;
        for (Snack s : snacks) total += s.getPrice() * s.getQuantity();
        return total;
    }

    public int getQty(int index) {
        if (index >= 0 && index < snacks.size()) return snacks.get(index).getQuantity();
        return 0;
    }

    static class ViewHolder {
        ImageView imgSnack;
        TextView textName, textPrice, textQty;
        Button btnPlus, btnMinus;

        ViewHolder(View v) {
            imgSnack = v.findViewById(R.id.imgSnack);
            textName = v.findViewById(R.id.textSnackName);
            textPrice = v.findViewById(R.id.textSnackPrice);
            textQty = v.findViewById(R.id.textQty);
            btnPlus = v.findViewById(R.id.btnPlus);
            btnMinus = v.findViewById(R.id.btnMinus);
        }
    }
}
