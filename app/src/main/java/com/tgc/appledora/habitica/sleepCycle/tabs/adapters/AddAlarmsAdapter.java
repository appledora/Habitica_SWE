package com.tgc.appledora.habitica.sleepCycle.tabs.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.maseno.franklinesable.lifereminder.R;
import com.tgc.appledora.habitica.sleepCycle.data.pojo.Item;
import com.tgc.appledora.habitica.sleepCycle.events.AddAlarmEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAlarmsAdapter extends RecyclerView.Adapter<AddAlarmsAdapter.ListAdapterHolder> {

    private List<Item> listItems;

    public AddAlarmsAdapter(List<Item> listItems) {
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public ListAdapterHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup,
                                                final int index) {
        return new ListAdapterHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_alarm, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterHolder listAdapterHolder, int position) {
        final Item item = listItems.get(position);
        if (item != null) {
            listAdapterHolder.bind(item);
        }
    }

    public class ListAdapterHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cl_item_alarm_root)
        ConstraintLayout clRoot;

        @BindView(R.id.iv_item_alarm_icon)
        ImageView ivIcon;

        @BindView(R.id.tv_item_alarm_title)
        TextView tvTitle;

        @BindView(R.id.tv_item_alarm_subtitle)
        TextView tvSubtitle;

        ListAdapterHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(final Item item) {
            ivIcon.setImageResource(R.drawable.ic_list_alarm_add_full_shape);
            tvTitle.setText(item.getTitle());
            tvSubtitle.setText(item.getSummary());

            clRoot.setOnClickListener(view -> EventBus.getDefault().post(new AddAlarmEvent(item)));
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
