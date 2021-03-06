package com.softsquared.wadiz.src.main.mypage.mypage_like.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.softsquared.wadiz.R;
import com.softsquared.wadiz.src.main.mypage.mypage_like.models.LikeItemlist;

import java.util.ArrayList;

public class LikeItemRvAdapter extends RecyclerView.Adapter<LikeItemRvAdapter.ViewHolder> {

    ArrayList<LikeItemlist> mData = null;
    Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivItem;
        TextView tvName;
        TextView tvCompany;
        TextView tvPercent;
        TextView tvMoney;
        TextView tvDay;
        TextView tvCategory;
        ProgressBar pb;

        ViewHolder(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조. (hold strong reference)
            ivItem = itemView.findViewById(R.id.mypage_funding_item_iv);
            tvName = itemView.findViewById(R.id.mypage_funding_item_tv_name);
            tvCompany = itemView.findViewById(R.id.mypage_funding_item_tv_company);
            tvPercent = itemView.findViewById(R.id.mypage_funding_item_tv_percent);
            tvMoney = itemView.findViewById(R.id.mypage_funding_item_tv_money);
            tvDay = itemView.findViewById(R.id.mypage_funding_item_tv_day);
            tvCategory = itemView.findViewById(R.id.mypage_funding_item_tv_category);
            pb = itemView.findViewById(R.id.mypage_funding_item_progress);
            ivItem.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);

        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public LikeItemRvAdapter(ArrayList<LikeItemlist> list, Context context) {
        mData = list;
        mContext = context;
    }

    @NonNull
    @Override
    public LikeItemRvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.mypage_fundinglist_item, parent, false);
        LikeItemRvAdapter.ViewHolder vh = new LikeItemRvAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull LikeItemRvAdapter.ViewHolder holder, int position) {

        Glide.with(mContext).load(mData.get(position).getImage()).into(holder.ivItem);
        holder.tvName.setText(mData.get(position).getName());
        holder.tvCompany.setText(mData.get(position).getCompany());
        holder.tvCategory.setText(mData.get(position).getCategory());
        holder.tvDay.setText(mData.get(position).getDay());
        if (mData.get(position).getPercent() == null) {
            holder.tvPercent.setText("0%");
            holder.pb.setProgress(0);
        } else {
            holder.tvPercent.setText(mData.get(position).getPercent());
            int idx = (mData.get(position).getPercent()).indexOf("%");
            holder.pb.setProgress(Integer.parseInt(mData.get(position).getPercent().substring(0, idx)));
        }
        if (mData.get(position).getMoney() == null) {
            holder.tvMoney.setText("0원");
        } else {
            holder.tvMoney.setText(mData.get(position).getMoney());

        }

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }
}