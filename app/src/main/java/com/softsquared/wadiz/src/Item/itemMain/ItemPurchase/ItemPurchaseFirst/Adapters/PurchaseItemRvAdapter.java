package com.softsquared.wadiz.src.Item.itemMain.ItemPurchase.ItemPurchaseFirst.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softsquared.wadiz.R;
import com.softsquared.wadiz.src.Item.itemMain.ItemPurchase.ItemPurchaseFirst.PurchaseFirstActivity;
import com.softsquared.wadiz.src.Item.itemMain.ItemPurchase.ItemPurchaseFirst.interfaces.PurchaseFirstActivityView;
import com.softsquared.wadiz.src.Item.itemMain.ItemPurchase.ItemPurchaseFirst.models.PurchaseItemlist;
import com.softsquared.wadiz.src.Item.itemMain.ItemPurchase.ItemPurchaseFirst.models.RewardList;

import java.util.ArrayList;

public class PurchaseItemRvAdapter extends RecyclerView.Adapter<PurchaseItemRvAdapter.ViewHolder> {

    ArrayList<PurchaseItemlist> mData = null;
    Context mContext;
    PurchaseFirstActivity mPurchaseFirstActivity;
    PurchaseFirstActivityView mPurchaseFirstActivityView;
    int mMoney = 0;
    RewardList mRewardList;
    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox cb;
        TextView tvMoney, tvRewardName, tvRewardInfo, tvDeliveryMoney, tvDeliveryDay;
        LinearLayout llChecked, llMain;
        ImageButton ibMinus, ibPlus;
        EditText etNumber;


        ViewHolder(View view) {
            super(view);
            llMain = view.findViewById(R.id.reward_select_ll_main);
            cb = view.findViewById(R.id.reward_select_cb);
            tvMoney = view.findViewById(R.id.reward_select_tv_money);
            tvRewardName = view.findViewById(R.id.reward_select_tv_reawrd_name);
            tvRewardInfo = view.findViewById(R.id.reward_select_tv_info);
            tvDeliveryMoney = view.findViewById(R.id.reward_select_tv_delivery_money);
            tvDeliveryDay = view.findViewById(R.id.reward_select_tv_delivery_day);
            llChecked = view.findViewById(R.id.reward_select_ll_checked);
            ibMinus = view.findViewById(R.id.reward_select_ib_minus);
            ibPlus = view.findViewById(R.id.reward_select_ib_plus);
            etNumber = view.findViewById(R.id.reward_select_et_number);

            ibPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            ibMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


            // 뷰 객체에 대한 참조. (hold strong reference)
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public PurchaseItemRvAdapter(ArrayList<PurchaseItemlist> list, PurchaseFirstActivityView purchaseFirstActivityView) {
        mData = list;
        mPurchaseFirstActivityView = purchaseFirstActivityView;
    }

    @NonNull
    @Override
    public PurchaseItemRvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.reward_select, parent, false);
        PurchaseItemRvAdapter.ViewHolder vh = new PurchaseItemRvAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseItemRvAdapter.ViewHolder holder, int position) {
        mRewardList = new RewardList(999, null, 0, null, null);

        holder.tvMoney.setText(mData.get(position).Money);
        holder.tvRewardName.setText(mData.get(position).RewardName);
        holder.tvRewardInfo.setText(mData.get(position).Info);
        holder.tvDeliveryMoney.setText(mData.get(position).DeliveryMoney);

        //원 이전 숫자만 뽑아서 int형식으로 바꾸기
        int idx = (mData.get(position).getMoney()).indexOf("원");
        int money = Integer.parseInt(mData.get(position).getMoney().substring(0,idx).replace(",",""));

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.cb.isChecked()) { //체크 되어있을때 선택 --> 체크 해제
                    holder.cb.setChecked(false);
                    holder.llMain.setBackgroundResource(R.drawable.customborder_rounded_nonfocus);
                    holder.llChecked.setVisibility(View.GONE);
                    int quantity = Integer.parseInt(holder.etNumber.getText().toString());
                    mMoney -= (money*quantity);
                    holder.etNumber.setText("1");

                    mRewardList.setRewardIdx(999);
                    mPurchaseFirstActivityView.addItemList(mRewardList, position);
                    mPurchaseFirstActivityView.addTotalMoney(mMoney);
                    mPurchaseFirstActivityView.addDeliveryMoney(-Integer.parseInt(mData.get(position).getDeliveryMoney()));
                    System.out.println(-Integer.parseInt(mData.get(position).getDeliveryMoney()));
                } else {  //체크 안되어 있을때 선택 --> 체크
                    holder.cb.setChecked(true);
                    holder.llMain.setBackgroundResource(R.drawable.customborder_rounded_reward_click);
                    holder.llChecked.setVisibility(View.VISIBLE);
                    holder.ibMinus.setEnabled(true);
                    int num = Integer.parseInt(holder.etNumber.getText().toString());
                    if (num == 1) {
                        holder.ibMinus.setEnabled(false);
                    }
                    mMoney += money;
                    int quantity = Integer.parseInt(holder.etNumber.getText().toString());

                    mRewardList.setRewardIdx(position);
                    mRewardList.setName(mData.get(position).getRewardName());
                    mRewardList.setRewardNum(quantity);
                    mRewardList.setInfo(mData.get(position).getInfo());
                    mRewardList.setMoney(String.format("%,d", (quantity*money)));

                    System.out.println("클릭 위치 : " + position);
                    System.out.println("클릭 리워드 아이템 리스트 : " + mRewardList.getRewardIdx() +mRewardList.getName());
                    mPurchaseFirstActivityView.addItemList(mRewardList, position);
                    mPurchaseFirstActivityView.addTotalMoney(mMoney);
                    mPurchaseFirstActivityView.addDeliveryMoney(Integer.parseInt(mData.get(position).getDeliveryMoney()));
                    System.out.println(Integer.parseInt(mData.get(position).getDeliveryMoney()));
                }
            }

        });

        holder.ibPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(holder.etNumber.getText().toString());
                num ++;
                holder.ibMinus.setEnabled(true);
                if (num == 1) {
                    holder.ibMinus.setEnabled(false);
                }
                holder.etNumber.setText(Integer.toString(num));
                mMoney += money;

                mRewardList.setRewardNum(num);
                mRewardList.setMoney(String.format("%,d", (money * num)));

                mPurchaseFirstActivityView.addItemList(mRewardList, position);
                mPurchaseFirstActivityView.addTotalMoney(mMoney);
            }
        });

        holder.ibMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(holder.etNumber.getText().toString());
                num --;
                if (num == 1) {
                    holder.ibMinus.setEnabled(false);
                }
                holder.etNumber.setText(Integer.toString(num));
                mMoney -= money;

                mRewardList.setRewardNum(num);
                mRewardList.setMoney(String.format("%,d", (money * num)));

                mPurchaseFirstActivityView.addItemList(mRewardList, position);
                mPurchaseFirstActivityView.addTotalMoney(mMoney);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

}