package com.ekspeace.kimopax.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ekspeace.kimopax.Model.EventBus.DeleteServiceEvent;
import com.ekspeace.kimopax.Model.Service;
import com.ekspeace.kimopax.R;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class RecyclerViewSelectedServiceAdapter extends RecyclerView.Adapter<RecyclerViewSelectedServiceAdapter.MyViewHolder> {
    private Context context;
    private List<Service> services;
    private RecyclerViewSelectedServiceAdapter.OnItemClickListener onItemClickListener;

    public RecyclerViewSelectedServiceAdapter(Context context, List<Service> services) {
        this.context = context;
        this.services = services;
    }

    @NonNull
    @Override
    public RecyclerViewSelectedServiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_selected_service_recycler_view, parent, false);
        return new RecyclerViewSelectedServiceAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewSelectedServiceAdapter.MyViewHolder holder, int position) {
        Service service = services.get(position);
        holder.tvServiceName.setText(service.getName());
        Picasso.get().load(service.getImage()).into(holder.ivServiceImage);
        holder.ivServiceDelete.setOnClickListener(view -> {
            EventBus.getDefault().postSticky(new DeleteServiceEvent(service, services, true));
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView ivServiceImage, ivServiceDelete;
        private TextView tvServiceName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivServiceImage = itemView.findViewById(R.id.selected_service_image);
            ivServiceDelete = itemView.findViewById(R.id.selected_service_delete);
            tvServiceName = itemView.findViewById(R.id.selected_service_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                int position = RecyclerViewSelectedServiceAdapter.MyViewHolder.this.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(itemView, position);
                }
            }
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    public void setOnItemClickListener(RecyclerViewSelectedServiceAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}