package com.jorgereina.bellycodingchallenge.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jorgereina.bellycodingchallenge.R;
import com.squareup.picasso.Picasso;
import com.yelp.clientlib.entities.Business;

import java.util.List;

public class YelpAdapter extends RecyclerView.Adapter<YelpAdapter.MyViewHolder> {

    private Context context;
    private List<Business> businessList;

    public YelpAdapter(Context context, List<Business> businessList) {
        this.context = context;
        this.businessList = businessList;
    }

    @Override
    public YelpAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(YelpAdapter.MyViewHolder holder, int position) {


        Business business = businessList.get(position);
        holder.businessName.setText(business.name());
        holder.businessDistance.setText(business.distance()+"");
        holder.businessType.setText(business.categories().get(0).name());
        if (business.isClosed()){
            holder.businessStatus.setText("CLOSED");
        }
        else{
            holder.businessStatus.setText("OPEN");
        }
        Picasso.with(context).load(business.imageUrl()).into(holder.businessIv);
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView businessName;
        private TextView businessDistance;
        private TextView businessType;
        private TextView businessStatus;
        private ImageView businessIv;

        public MyViewHolder(View itemView) {
            super(itemView);
            businessName = (TextView) itemView.findViewById(R.id.business_name);
            businessDistance = (TextView) itemView.findViewById(R.id.business_distance);
            businessType = (TextView) itemView.findViewById(R.id.business_type);
            businessStatus = (TextView) itemView.findViewById(R.id.business_status);
            businessIv = (ImageView) itemView.findViewById(R.id.business_iv);
        }
    }
}
