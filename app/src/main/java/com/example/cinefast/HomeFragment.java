package com.example.cinefast;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        HomePagerAdapter adapter = new HomePagerAdapter(requireActivity());
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                tab.setText(position == 0 ? "Now Showing" : "Coming Soon")
        ).attach();

        TextView btnToday = view.findViewById(R.id.btnDateToday);
        TextView btnTomorrow = view.findViewById(R.id.btnDateTomorrow);
        btnToday.setOnClickListener(v -> updateDateSelection(true, btnToday, btnTomorrow));
        btnTomorrow.setOnClickListener(v -> updateDateSelection(false, btnToday, btnTomorrow));

        view.findViewById(R.id.btnOverflowMenu).setOnClickListener(this::showOverflowMenu);
    }

    private void showOverflowMenu(View anchor) {
        PopupMenu popup = new PopupMenu(requireContext(), anchor);
        popup.getMenu().add(0, 1, 0, "View Last Booking");
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == 1) {
                showLastBooking();
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void showLastBooking() {
        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("CineFastPrefs", android.content.Context.MODE_PRIVATE);
        String movie = prefs.getString("last_movie", null);
        int seats = prefs.getInt("last_seats", -1);
        float price = prefs.getFloat("last_price", -1f);

        String message;
        if (movie == null || seats == -1 || price == -1f) {
            message = "No previous booking found.";
        } else {
            message = "Movie: " + movie
                    + "\nSeats Booked: " + seats
                    + "\nTotal Paid: " + String.format(java.util.Locale.getDefault(), "$%.2f", price);
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Last Booking")
                .setMessage(message)
                .setPositiveButton("Close", null)
                .show();
    }

    private void updateDateSelection(boolean isToday, TextView btnToday, TextView btnTomorrow) {
        if (isToday) {
            btnToday.setText("◉ Today");
            btnToday.setTextColor(requireContext().getResources().getColor(R.color.white));
            btnToday.setBackgroundResource(R.drawable.bg_radio_selected);
            btnToday.setTypeface(null, Typeface.BOLD);

            btnTomorrow.setText("○ Tomorrow");
            btnTomorrow.setTextColor(requireContext().getResources().getColor(R.color.text_gray));
            btnTomorrow.setBackgroundResource(R.drawable.bg_radio_unselected);
            btnTomorrow.setTypeface(null, Typeface.NORMAL);
        } else {
            btnToday.setText("○ Today");
            btnToday.setTextColor(requireContext().getResources().getColor(R.color.text_gray));
            btnToday.setBackgroundResource(R.drawable.bg_radio_unselected);
            btnToday.setTypeface(null, Typeface.NORMAL);

            btnTomorrow.setText("◉ Tomorrow");
            btnTomorrow.setTextColor(requireContext().getResources().getColor(R.color.white));
            btnTomorrow.setBackgroundResource(R.drawable.bg_radio_selected);
            btnTomorrow.setTypeface(null, Typeface.BOLD);
        }
    }
}
