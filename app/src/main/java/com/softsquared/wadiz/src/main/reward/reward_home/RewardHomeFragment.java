package com.softsquared.wadiz.src.main.reward.reward_home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.softsquared.wadiz.R;
import com.softsquared.wadiz.src.BaseFragment;
import com.softsquared.wadiz.src.category.CategoryActivity;
import com.softsquared.wadiz.src.main.reward.reward_home.Adapters.BigItemRvAdapter;
import com.softsquared.wadiz.src.main.reward.reward_home.Adapters.CategoryRvAdapter;
import com.softsquared.wadiz.src.main.reward.reward_home.Adapters.SmallItemRvAdapter;
import com.softsquared.wadiz.src.main.reward.reward_home.Adapters.ViewpagerAdapter;
import com.softsquared.wadiz.src.main.reward.reward_home.interfaces.RewardHomeView;
import com.softsquared.wadiz.src.main.reward.reward_home.models.BannerItemlist;
import com.softsquared.wadiz.src.main.reward.reward_home.models.BannerResponse;
import com.softsquared.wadiz.src.main.reward.reward_home.models.CategoryItemList;
import com.softsquared.wadiz.src.main.reward.reward_home.models.CategoryResponse;
import com.softsquared.wadiz.src.main.reward.reward_home.models.ItemResponse;
import com.softsquared.wadiz.src.main.reward.reward_home.models.Itemlist;

import java.util.ArrayList;
import java.util.List;

public class RewardHomeFragment extends BaseFragment implements RewardHomeView {
    View view;
    ViewPager viewPager;
    ViewpagerAdapter pagerAdapter;
    RecyclerView rvCategory, rvItem;
    EditText etSearch;
    Button btnControl, mBtnOrder;
    ImageButton ibShowlist;
    boolean showitemflag;
    ArrayList<BannerItemlist> mBannerItemlist;
    BannerResponse mBannerResponse;
    ArrayList<CategoryItemList> mCategoryItemlist;
    CategoryResponse mCategoryResponse;
    ArrayList<Itemlist> mItemlist;
    ItemResponse mItemResponse;
    ProgressBar mPb;
    RewardHomeService rewardHomeService;
    String mOrder;
    SmallItemRvAdapter smallItemRvAdapter;
    BigItemRvAdapter bigItemRvAdapter;
    String mSearchWord;
    FloatingActionButton mBtnFloating;
    NestedScrollView mSv;
    boolean isFabOpen = false;
    private Animation fab_open, fab_close;

    public RewardHomeFragment() {

    }

    public static RewardHomeFragment newInstance() {
        RewardHomeFragment mainActivity = new RewardHomeFragment();
        return mainActivity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reward_home, container, false);
        viewPager = view.findViewById(R.id.reward_home_vp);
        rvCategory = view.findViewById(R.id.reward_home_rv_category);
        etSearch = view.findViewById(R.id.reward_home_et);
        btnControl = view.findViewById(R.id.reward_home_control);
        mBtnOrder = view.findViewById(R.id.reward_home_order);
        ibShowlist = view.findViewById(R.id.reward_home_showlist);
        mPb = view.findViewById(R.id.reward_home_pb);
        mBannerResponse = new BannerResponse();
        mCategoryResponse = new CategoryResponse();
        mBannerItemlist = new ArrayList<>();
        mCategoryItemlist = new ArrayList<>();
        mItemlist = new ArrayList<>();
        mItemResponse = new ItemResponse();
        mOrder = "recommend";
        rvItem = view.findViewById(R.id.reward_home_lv);
        rvCategory = view.findViewById(R.id.reward_home_rv_category);
        mBtnFloating = view.findViewById(R.id.reward_home_btn_floating);
        mSv = view.findViewById(R.id.reward_home_sv);
        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.btn_open);
        fab_close = AnimationUtils.loadAnimation(getActivity(), R.anim.btn_close);

        tryGetTest();

        rvItem.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0) {
                    mBtnFloating.show();
                }
                else if (dy > 0) {
                    mBtnFloating.hide();
                }
            }
        });

        //카테고리 클릭 이벤트 구현
        rvCategory.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rvCategory, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                intent.putExtra("categoryIdx", position);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        mBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //순서 다이얼로그 생성
                final List<String> ListItems = new ArrayList<>();
                ListItems.add("추천순");
                ListItems.add("인기순");
                ListItems.add("펀딩액순");
                ListItems.add("마감임박순");
                ListItems.add("최신순");
                ListItems.add("응원참여자순");
                final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

                final List SelectedItems = new ArrayList();
                int defaultItem = 0;
                SelectedItems.add(defaultItem);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setSingleChoiceItems(items, defaultItem,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SelectedItems.clear();
                                SelectedItems.add(which);
                            }
                        });
                builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String msg = "";

                                if (!SelectedItems.isEmpty()) {
                                    int index = (int) SelectedItems.get(0);
                                    msg = ListItems.get(index);
                                }
                                mBtnOrder.setText(msg);

                                switch (msg) {
                                    case "추천순":
                                        mOrder = "recommend";
                                        break;
                                    case "응원순":
                                        mOrder = "famous";
                                        break;
                                    case "펀딩액순":
                                        mOrder = "funding";
                                        break;
                                    case "마감임박순":
                                        mOrder = "deadline";
                                        break;
                                    case "최신순":
                                        mOrder = "newp";
                                        break;
                                    case "응원참여자순":
                                        mOrder = "supporter";
                                        break;
                                }
                                tryGetTest();
                            }
                        });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
            }
        });

        mBtnFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSv.smoothScrollTo(0, 0);
            }
        });

        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);    //hide keyboard

                    mSearchWord = etSearch.getText().toString();
                    trySearch();
                    return true;
                }
                return false;
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }


    private void toggleFab() {

        if (isFabOpen) {
            mBtnFloating.startAnimation(fab_close);
            isFabOpen = false;

        } else {
            mBtnFloating.startAnimation(fab_open);
            isFabOpen = true;
        }

    }

    //리사이클러뷰 클릭 리스너 추가
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private RewardHomeFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final RewardHomeFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }


    private void tryGetTest() {
        showProgressDialog();

        rewardHomeService = new RewardHomeService(this);
        rewardHomeService.getBanner();
        rewardHomeService.getCategory();
        System.out.println("순서 : " + mOrder);
        rewardHomeService.getItem(mOrder);
    }

    private void trySearch() {
        showProgressDialog();

        rewardHomeService = new RewardHomeService(this);
        rewardHomeService.getSearch(mSearchWord);
    }

    @Override
    public void validateBannerSuccess(ArrayList<BannerItemlist> item) {
        hideProgressDialog();

        mBannerItemlist = item;
        pagerAdapter = new ViewpagerAdapter(getActivity(), mBannerItemlist);
        pagerAdapter.view_count = mBannerItemlist.size();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(pagerAdapter.view_count);
        mPb.setMax(pagerAdapter.view_count);
        mPb.setProgress(1);
//       자동스크롤 잘안됨;
//        mCurrentPage = 0;
//        final Handler handler = new Handler();
//        final Runnable Update = new Runnable() {
//            public void run() {
//                if (mCurrentPage == pagerAdapter.view_count-1) {
//                    mCurrentPage = 0;
//                }
//                viewPager.setCurrentItem(mCurrentPage++, true);
//            }
//        };
//
//        Timer timer = new Timer(); // This will create a new Thread
//
//        timer.schedule(new TimerTask() { // task to be scheduled
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, 500, 3000);

        //배너 무한스크롤 구현 (마지막에서 다시 처음으로)
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mPb.setProgress(position % pagerAdapter.view_count + 1);
                System.out.println(position);
                System.out.println(position % pagerAdapter.view_count);
                if (position < pagerAdapter.view_count) {       //1번째 아이템에서 마지막 아이템으로 이동하면
                    viewPager.setCurrentItem(position + pagerAdapter.view_count, false); //이동 애니메이션을 제거 해야 한다
                    mPb.setProgress(pagerAdapter.view_count+1);
                } else if (position >= pagerAdapter.view_count * 2) {   //마지막 아이템에서 1번째 아이템으로 이동하면
                    viewPager.setCurrentItem(position - pagerAdapter.view_count, false);
                    mPb.setProgress(1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    public void validateBannerFailure(@Nullable String message) {
        hideProgressDialog();
        showCustomToast(message == null || message.isEmpty() ? getString(R.string.network_error) : message);
    }

    @Override
    public void validateCategorySuccess(ArrayList<CategoryItemList> item) {
        hideProgressDialog();
        mCategoryItemlist = item;
        //카테고리 리사이클러뷰 생성

        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        CategoryRvAdapter categoryRvAdapter = new CategoryRvAdapter(getContext(), mCategoryItemlist);
        rvCategory.setAdapter(categoryRvAdapter);
        System.out.println(categoryRvAdapter.getItemCount());
    }

    @Override
    public void validateCategoryFailure(String message) {
        hideProgressDialog();

    }

    @Override
    public void validateItemSuccess(ArrayList<Itemlist> item) {
        hideProgressDialog();

        //아이템 리사이클러뷰 생성
        rvItem.setLayoutManager(new LinearLayoutManager(getActivity()));

        mItemlist = item;
        smallItemRvAdapter = new SmallItemRvAdapter(mItemlist, getActivity());
        bigItemRvAdapter = new BigItemRvAdapter(mItemlist, getActivity());
        rvItem.setAdapter(smallItemRvAdapter);
        smallItemRvAdapter.notifyDataSetChanged();

        showitemflag = true; //아이템 리스트 보여주는 방식 변경을 위한 플래그 (small리스트 사용시 true, big리스트 사용시 false)

        //버튼 클릭시 보여주기 방식 변경 (크게/작게)
        ibShowlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showitemflag) {
                    rvItem.setAdapter(bigItemRvAdapter);
                    ibShowlist.setImageResource(R.drawable.biglist);
                    showitemflag = false;
                } else {
                    rvItem.setAdapter(smallItemRvAdapter);
                    ibShowlist.setImageResource(R.drawable.smalllist);
                    showitemflag = true;
                }

            }
        });

    }

    @Override
    public void validateItemFailure(String message) {
        hideProgressDialog();

    }

    @Override
    public void validateSearchSuccess(ArrayList<Itemlist> item) {
        hideProgressDialog();
        //아이템 리사이클러뷰 생성
        rvItem.setLayoutManager(new LinearLayoutManager(getActivity()));

        mItemlist = item;
        smallItemRvAdapter = new SmallItemRvAdapter(mItemlist, getActivity());
        bigItemRvAdapter = new BigItemRvAdapter(mItemlist, getActivity());
        rvItem.setAdapter(smallItemRvAdapter);
        smallItemRvAdapter.notifyDataSetChanged();

        showitemflag = true; //아이템 리스트 보여주는 방식 변경을 위한 플래그 (small리스트 사용시 true, big리스트 사용시 false)

        //버튼 클릭시 보여주기 방식 변경 (크게/작게)
        ibShowlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showitemflag) {
                    rvItem.setAdapter(bigItemRvAdapter);
                    ibShowlist.setImageResource(R.drawable.biglist);
                    showitemflag = false;
                } else {
                    rvItem.setAdapter(smallItemRvAdapter);
                    ibShowlist.setImageResource(R.drawable.smalllist);
                    showitemflag = true;
                }

            }
        });
    }

    @Override
    public void validateSearchFailure(String message) {
        hideProgressDialog();

    }

    public void customOnClick(View view) {
        switch (view.getId()) {
//            case R.id.main_btn_hello_world:
//                tryGetTest();
//                break;
            default:
                break;
        }
    }

}
