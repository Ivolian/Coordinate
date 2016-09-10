package com.unicorn.coordinate.base;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;
import com.unicorn.coordinate.volley.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

;


public abstract class RefreshFragment extends LazyLoadFragment {


    // ================================== abstract methods ==================================

    abstract public RefreshAdapter getAdapter();

    abstract public Object parseDataList(String jsonArrayString);

    abstract public String getLatterPartUrl();

    abstract public Map<String, String> getParamsMap();

    abstract public String getKey();


    // ================================== views ==================================

//    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

//    @BindView(R.id.recycleView)
    RecyclerView recyclerView;


    // ================================== adapter ==================================

    RefreshAdapter adapter;


    // ================================== paging fields ==================================

    public final Integer PAGE_SIZE = 10;

    public Integer pageNo = 1;

    boolean lastPage = false;

    boolean loadingMore;




    // ================================== onFirstUserVisible ==================================

    @Override
    public void onFirstUserVisible() {
        initSwipeRefreshLayout();
        initRecyclerView();
        firstLoad();
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
            }
        });
    }

    private void initRecyclerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = getAdapter();
        recyclerView.setAdapter((RecyclerView.Adapter) adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                final int itemCount = linearLayoutManager.getItemCount();
                if (!loadingMore && !lastPage && newState == RecyclerView.SCROLL_STATE_IDLE && itemCount != 0
                        && linearLayoutManager.findLastVisibleItemPosition() == itemCount - 1) {
                    loadMore();
                }
            }
        });
//        recyclerView.addItemDecoration(getItemDecoration());
    }

//    private RecyclerView.ItemDecoration getItemDecoration() {
//        final Context context = getActivity();
//        int dividerColor = ContextCompat.getColor(context, R.color.md_grey_300);
//        int marginPx = DensityUtils.dip2px(context, 20);
//        return new HorizontalDividerItemDecoration.Builder(context)
//                .color(dividerColor)
//                .size(1)
//                .margin(marginPx, marginPx)
//                .build();
//    }

    private void firstLoad() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                startRefreshing();
            }
        });
        reload();
    }


    // ================================== reload ==================================

    public void reload() {
        clearPageFields();
        final Request request = new StringRequest(
                Request.Method.POST,
                getCompleteUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       stopRefreshing();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(getKey());
                            adapter.reload(parseDataList(jsonArray.toString()));
                            checkLastPage(new JSONObject(response));
                            pageNo++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        stopRefreshing();
                        ToastUtils.show(VolleyErrorHelper.getErrorMessage(volleyError));
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = getParamsMap();
                return params;
            }
        };
        SimpleVolley.addRequest(request);
    }


    private void loadMore() {
        loadingMore = true;
        Request request = new StringRequest(
                Request.Method.POST,
                getCompleteUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            loadingMore = false;
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(getKey());
                            adapter.loadMore(parseDataList(jsonArray.toString()));
                            checkLastPage(new JSONObject(response));
                            pageNo++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loadingMore = false;
                        ToastUtils.show(VolleyErrorHelper.getErrorMessage(volleyError));
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = getParamsMap();
                return params;
            }
        };
        SimpleVolley.addRequest(request);
    }

    private void clearPageFields() {
        pageNo = 1;
        lastPage = false;
    }

    // ========================== 基础方法 ==========================

    public String getCompleteUrl() {
        return ConfigUtils.getBaseUrl() + getLatterPartUrl();
    }

    private void checkLastPage(JSONObject response) throws Exception {
        if (lastPage = isLastPage(response)) {
            ToastUtils.show(noData(response) ? "暂无数据" : "已加载全部数据");
        }
    }

    //
    private boolean isLastPage(JSONObject response) throws Exception {
        return pageNo * PAGE_SIZE >= totals(response) ;
    }

    private boolean noData(JSONObject response) throws Exception {
        return totals(response) == 0;
    }

    private int totals(JSONObject response) throws Exception {
        return response.getInt("total");
    }


    // ========================== 安全方法 ==========================

    private void stopRefreshing() {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void startRefreshing() {
        if (swipeRefreshLayout != null && !swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

}
