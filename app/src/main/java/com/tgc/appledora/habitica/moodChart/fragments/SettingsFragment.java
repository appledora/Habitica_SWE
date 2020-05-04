package com.tgc.appledora.habitica.moodChart.fragments;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.maseno.franklinesable.lifereminder.R;
import com.tgc.appledora.habitica.moodChart.utils.Preferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import yuku.ambilwarna.AmbilWarnaDialog;

public class SettingsFragment extends Fragment {

    @BindView(R.id.settings_mood_1_label)
    TextView mMood1LabelTextView;
    @BindView(R.id.settings_mood_2_label)
    TextView mMood2LabelTextView;
    @BindView(R.id.settings_mood_3_label)
    TextView mMood3LabelTextView;
    @BindView(R.id.settings_mood_4_label)
    TextView mMood4LabelTextView;
    @BindView(R.id.settings_mood_5_label)
    TextView mMood5LabelTextView;
    @BindView(R.id.settings_mood_6_label)
    TextView mMood6LabelTextView;
    @BindView(R.id.settings_mood_7_label)
    TextView mMood7LabelTextView;
    @BindView(R.id.settings_mood_8_label)
    TextView mMood8LabelTextView;
    @BindView(R.id.settings_mood_9_label)
    TextView mMood9LabelTextView;
    @BindView(R.id.settings_mood_10_label)
    TextView mMood10LabelTextView;
    @BindView(R.id.settings_mood_11_label)
    TextView mMood11LabelTextView;
    @BindView(R.id.settings_mood_12_label)
    TextView mMood12LabelTextView;

    public SettingsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        // Bind the views
        ButterKnife.bind(this, rootView);

        setTypefaceFont(mMood1LabelTextView);
        setTypefaceFont(mMood2LabelTextView);
        setTypefaceFont(mMood3LabelTextView);
        setTypefaceFont(mMood4LabelTextView);
        setTypefaceFont(mMood5LabelTextView);
        setTypefaceFont(mMood6LabelTextView);
        setTypefaceFont(mMood7LabelTextView);
        setTypefaceFont(mMood8LabelTextView);
        setTypefaceFont(mMood9LabelTextView);
        setTypefaceFont(mMood10LabelTextView);
        setTypefaceFont(mMood11LabelTextView);
        setTypefaceFont(mMood12LabelTextView);

        setMoodOption(
                rootView.findViewById(R.id.settings_mood_1_color_view),
                rootView.findViewById(R.id.settings_mood_1_color),
                getString(R.string.pref_mood_1_color_key),
                Preferences.getMoodColor(
                        getContext(),
                        getString(R.string.pref_mood_1_color_key),
                        ContextCompat.getColor(getContext(), R.color.mood_color_1)),
                (LinearLayout) rootView.findViewById(R.id.settings_mood_1_label_layout),
                mMood1LabelTextView,
                getString(R.string.pref_mood_1_label_key),
                Preferences.getMoodLabel(
                        getContext(),
                        getString(R.string.pref_mood_1_label_key),
                        getString(R.string.label_mood_1)));
        setMoodOption(
                rootView.findViewById(R.id.settings_mood_2_color_view),
                rootView.findViewById(R.id.settings_mood_2_color),
                getString(R.string.pref_mood_2_color_key),
                Preferences.getMoodColor(
                        getContext(),
                        getString(R.string.pref_mood_2_color_key),
                        ContextCompat.getColor(getContext(), R.color.mood_color_2)),
                (LinearLayout) rootView.findViewById(R.id.settings_mood_2_label_layout),
                mMood2LabelTextView,
                getString(R.string.pref_mood_2_label_key),
                Preferences.getMoodLabel(
                        getContext(),
                        getString(R.string.pref_mood_2_label_key),
                        getString(R.string.label_mood_2)));
        setMoodOption(
                rootView.findViewById(R.id.settings_mood_3_color_view),
                rootView.findViewById(R.id.settings_mood_3_color),
                getString(R.string.pref_mood_3_color_key),
                Preferences.getMoodColor(
                        getContext(),
                        getString(R.string.pref_mood_3_color_key),
                        ContextCompat.getColor(getContext(), R.color.mood_color_3)),
                (LinearLayout) rootView.findViewById(R.id.settings_mood_3_label_layout),
                mMood3LabelTextView,
                getString(R.string.pref_mood_3_label_key),
                Preferences.getMoodLabel(
                        getContext(),
                        getString(R.string.pref_mood_3_label_key),
                        getString(R.string.label_mood_3)));
        setMoodOption(
                rootView.findViewById(R.id.settings_mood_4_color_view),
                rootView.findViewById(R.id.settings_mood_4_color),
                getString(R.string.pref_mood_4_color_key),
                Preferences.getMoodColor(
                        getContext(),
                        getString(R.string.pref_mood_4_color_key),
                        ContextCompat.getColor(getContext(), R.color.mood_color_4)),
                (LinearLayout) rootView.findViewById(R.id.settings_mood_4_label_layout),
                mMood4LabelTextView,
                getString(R.string.pref_mood_4_label_key),
                Preferences.getMoodLabel(
                        getContext(),
                        getString(R.string.pref_mood_4_label_key),
                        getString(R.string.label_mood_4)));
        setMoodOption(
                rootView.findViewById(R.id.settings_mood_5_color_view),
                rootView.findViewById(R.id.settings_mood_5_color),
                getString(R.string.pref_mood_5_color_key),
                Preferences.getMoodColor(
                        getContext(),
                        getString(R.string.pref_mood_5_color_key),
                        ContextCompat.getColor(getContext(), R.color.mood_color_5)),
                (LinearLayout) rootView.findViewById(R.id.settings_mood_5_label_layout),
                mMood5LabelTextView,
                getString(R.string.pref_mood_5_label_key),
                Preferences.getMoodLabel(
                        getContext(),
                        getString(R.string.pref_mood_5_label_key),
                        getString(R.string.label_mood_5)));
        setMoodOption(
                rootView.findViewById(R.id.settings_mood_6_color_view),
                rootView.findViewById(R.id.settings_mood_6_color),
                getString(R.string.pref_mood_6_color_key),
                Preferences.getMoodColor(
                        getContext(),
                        getString(R.string.pref_mood_6_color_key),
                        ContextCompat.getColor(getContext(), R.color.mood_color_6)),
                (LinearLayout) rootView.findViewById(R.id.settings_mood_6_label_layout),
                mMood6LabelTextView,
                getString(R.string.pref_mood_6_label_key),
                Preferences.getMoodLabel(
                        getContext(),
                        getString(R.string.pref_mood_6_label_key),
                        getString(R.string.label_mood_6)));
        setMoodOption(
                rootView.findViewById(R.id.settings_mood_7_color_view),
                rootView.findViewById(R.id.settings_mood_7_color),
                getString(R.string.pref_mood_7_color_key),
                Preferences.getMoodColor(
                        getContext(),
                        getString(R.string.pref_mood_7_color_key),
                        ContextCompat.getColor(getContext(), R.color.mood_color_7)),
                (LinearLayout) rootView.findViewById(R.id.settings_mood_7_label_layout),
                mMood7LabelTextView,
                getString(R.string.pref_mood_7_label_key),
                Preferences.getMoodLabel(
                        getContext(),
                        getString(R.string.pref_mood_7_label_key),
                        getString(R.string.label_mood_7)));
        setMoodOption(
                rootView.findViewById(R.id.settings_mood_8_color_view),
                rootView.findViewById(R.id.settings_mood_8_color),
                getString(R.string.pref_mood_8_color_key),
                Preferences.getMoodColor(
                        getContext(),
                        getString(R.string.pref_mood_8_color_key),
                        ContextCompat.getColor(getContext(), R.color.mood_color_8)),
                (LinearLayout) rootView.findViewById(R.id.settings_mood_8_label_layout),
                mMood8LabelTextView,
                getString(R.string.pref_mood_8_label_key),
                Preferences.getMoodLabel(
                        getContext(),
                        getString(R.string.pref_mood_8_label_key),
                        getString(R.string.label_mood_8)));
        setMoodOption(
                rootView.findViewById(R.id.settings_mood_9_color_view),
                rootView.findViewById(R.id.settings_mood_9_color),
                getString(R.string.pref_mood_9_color_key),
                Preferences.getMoodColor(
                        getContext(),
                        getString(R.string.pref_mood_9_color_key),
                        ContextCompat.getColor(getContext(), R.color.mood_color_9)),
                (LinearLayout) rootView.findViewById(R.id.settings_mood_9_label_layout),
                mMood9LabelTextView,
                getString(R.string.pref_mood_9_label_key),
                Preferences.getMoodLabel(
                        getContext(),
                        getString(R.string.pref_mood_9_label_key),
                        getString(R.string.label_mood_9)));
        setMoodOption(
                rootView.findViewById(R.id.settings_mood_10_color_view),
                rootView.findViewById(R.id.settings_mood_10_color),
                getString(R.string.pref_mood_10_color_key),
                Preferences.getMoodColor(
                        getContext(),
                        getString(R.string.pref_mood_10_color_key),
                        ContextCompat.getColor(getContext(), R.color.mood_color_10)),
                (LinearLayout) rootView.findViewById(R.id.settings_mood_10_label_layout),
                mMood10LabelTextView,
                getString(R.string.pref_mood_10_label_key),
                Preferences.getMoodLabel(
                        getContext(),
                        getString(R.string.pref_mood_10_label_key),
                        getString(R.string.label_mood_10)));
        setMoodOption(
                rootView.findViewById(R.id.settings_mood_11_color_view),
                rootView.findViewById(R.id.settings_mood_11_color),
                getString(R.string.pref_mood_11_color_key),
                Preferences.getMoodColor(
                        getContext(),
                        getString(R.string.pref_mood_11_color_key),
                        ContextCompat.getColor(getContext(), R.color.mood_color_11)),
                (LinearLayout) rootView.findViewById(R.id.settings_mood_11_label_layout),
                mMood11LabelTextView,
                getString(R.string.pref_mood_11_label_key),
                Preferences.getMoodLabel(
                        getContext(),
                        getString(R.string.pref_mood_11_label_key),
                        getString(R.string.label_mood_11)));
        setMoodOption(
                rootView.findViewById(R.id.settings_mood_12_color_view),
                rootView.findViewById(R.id.settings_mood_12_color),
                getString(R.string.pref_mood_12_color_key),
                Preferences.getMoodColor(
                        getContext(),
                        getString(R.string.pref_mood_12_color_key),
                        ContextCompat.getColor(getContext(), R.color.mood_color_12)),
                (LinearLayout) rootView.findViewById(R.id.settings_mood_12_label_layout),
                mMood12LabelTextView,
                getString(R.string.pref_mood_12_label_key),
                Preferences.getMoodLabel(
                        getContext(),
                        getString(R.string.pref_mood_12_label_key),
                        getString(R.string.label_mood_12)));

        return rootView;
    }

    private void setTypefaceFont(TextView view) {
//        view.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "Norican-Regular.ttf"));
    }

    private void setMoodOption(View containerView, View colorView, String colorKey, int defaultColor, LinearLayout labelLayout, TextView labelView, String labelKey, String label) {
        setMoodColorListener(containerView, colorView, colorKey, defaultColor);
        setMoodColor(colorView, defaultColor);

        setMoodLabelListener(labelLayout, labelView, labelKey);
        setMoodLabel(labelView, label);
    }

    private void setMoodColorListener(View containerView, final View colorView, final String key, final int color) {
        containerView.setOnClickListener(view -> openColorPicker(colorView, key, color));
    }

    private void setMoodColor(View view, int color) {
        GradientDrawable one = (GradientDrawable) view.getBackground().mutate();
        one.setColor(color);
    }

    private void setMoodLabel(TextView textView, String label) {
        textView.setText(label);
    }

    private void setMoodLabelListener(LinearLayout layout, final TextView textView, final String key) {
        layout.setOnClickListener(view -> showUpdateMoodNameDialog(textView, key));
    }

    // Show a dialog for a section insertion or section update.
    // If title is null, we insert a new section, otherwise we update a section title
    public void showUpdateMoodNameDialog(final TextView textView, final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        final View changeMoodNameView = inflater.inflate(R.layout.dialog_change_mood_name, null);
        // Set current name of selected mood as the text for the EditText
        EditText editText = changeMoodNameView.findViewById(R.id.new_mood_name_edit_text);
        editText.setText(textView.getText());
        editText.setSelection(textView.getText().length());

        builder.setIcon(R.drawable.ic_edit);
        builder.setTitle(R.string.change_mood_name);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(changeMoodNameView)
                // Add action buttons
                .setPositiveButton(R.string.action_ok, (dialog, id) -> {
                    // Find the EditText view so we can read data from it
                    EditText newTitleEditText = changeMoodNameView.findViewById(R.id.new_mood_name_edit_text);
                    // Store the data entered by the user in newTitle
                    String newMoodName = newTitleEditText.getText().toString().toLowerCase();

                    // If the new title is NOT null or an empty string
                    if (!TextUtils.isEmpty(newMoodName)) {
                        // If there is an old title passed to this method, update it with the
                        // new section title
                        Preferences.setMoodLabel(getContext(), key, newMoodName);
                        textView.setText(newMoodName);
                    } else {
                        // Otherwise, we display a toast to let the user know about this error.
                        Toast.makeText(getContext(),
                                getString(R.string.invalid_name),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.action_cancel, (dialog, id) -> {
                    // User clicked the "Cancel" button, so dismiss the deletion dialog.
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void openColorPicker(final View view, final String key, int defaultColor) {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(getContext(), defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                //
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                Preferences.setMoodColor(getContext(), key, color);
                setMoodColor(view, color);
            }
        });
        colorPicker.show();
    }
}
