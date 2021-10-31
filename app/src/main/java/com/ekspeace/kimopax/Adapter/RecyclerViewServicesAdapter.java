package com.ekspeace.kimopax.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ekspeace.kimopax.Model.EventBus.AddServiceEvent;
import com.ekspeace.kimopax.Model.EventBus.DeleteServiceEvent;
import com.ekspeace.kimopax.Model.Service;
import com.ekspeace.kimopax.R;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class RecyclerViewServicesAdapter extends RecyclerView.Adapter<RecyclerViewServicesAdapter.MyViewHolder> {
    private Context context;
    private List<Service> services;
    private OnItemClickListener onItemClickListener;

    public RecyclerViewServicesAdapter(Context context, List<Service> services) {
        this.context = context;
        this.services = services;
    }

    @NonNull
    @Override
    public RecyclerViewServicesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_service_recycler_view, parent, false);
        return new RecyclerViewServicesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewServicesAdapter.MyViewHolder holder, int position) {
        Service service = services.get(position);
        holder.tvServiceName.setText(service.getName());
        Picasso.get().load(service.getImage()).into(holder.ivServiceImage);
        holder.ivServiceAdd.setOnClickListener(view -> {
            EventBus.getDefault().postSticky(new AddServiceEvent(service, true));
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView ivServiceImage, ivServiceAdd;
        private TextView tvServiceName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivServiceImage = itemView.findViewById(R.id.service_image);
            ivServiceAdd = itemView.findViewById(R.id.service_add);
            tvServiceName = itemView.findViewById(R.id.service_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                int position = MyViewHolder.this.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(itemView, position);
                }
            }
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}