package com.fydo.Setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fydo.Adapter.ItemListAdapter;
import com.fydo.Config.CommonUtilities;
import com.fydo.Config.RecyclerItemClickListener;
import com.fydo.Config.ServerAccess;
import com.fydo.Config.SimpleDividerItemDecoration;
import com.fydo.Model.ItemDetails;
import com.fydo.Model.ItemList;
import com.fydo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sudhansu on 4/20/2017.
 */

public class ItemListActivity extends AppCompatActivity {

    Activity act;

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;

    @Bind(R.id.lin_dialog)
    LinearLayout lin_dialog;

    @Bind(R.id.edt_search)
    EditText edt_search;

    @Bind(R.id.ivSearch)
    ImageView ivSearch;

    @Bind(R.id.ivClear)
    ImageView ivClear;

    ItemListAdapter mAdapter;
    int page_index = 0, tc;
    LinearLayoutManager layoutManager;
    ArrayList<ItemList.lstItemList> array_practiceList;
    ItemList res;
    boolean isLoading = false;
    boolean isClear = false;
    boolean isRunning = false;
    ItemDetails response;
    boolean fromEdit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);

        ButterKnife.bind(this);

        act = ItemListActivity.this;
        CommonUtilities.setOrientation(act);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Items");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        array_practiceList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        lin_dialog.setVisibility(View.GONE);

        edt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_search.setCursorVisible(true);
            }
        });

        edt_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (s.toString().length() >= 1) {
                    isClear = true;
                    ivClear.setVisibility(View.VISIBLE);
                } else {
                    ivClear.setVisibility(View.GONE);
                    isClear = false;
                }
                if (s.toString().length() >= 3) {
                    page_index = 0;
                    CountDownTimer timer = new CountDownTimer(1000, 1000) {

                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            isRunning = false;
                            call_itemlist_api(true, s.toString());
                        }
                    };

                    if (!isRunning) {
                        timer.start();
                        isRunning = true;
                    }
                } else if (s.toString().toString().length() == 0) {
                    call_itemlist_api(true, s.toString());
                }
            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edt_search.getText().equals("")) {
                    page_index = 0;
                    call_itemlist_api(true, edt_search.getText().toString());
                }
            }
        });

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    //do something
                    if (!edt_search.getText().toString().equals("")) {
                        page_index = 0;
                        call_itemlist_api(true, edt_search.getText().toString());
                    }
                }
                return false;
            }
        });

        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClear) {
                    edt_search.setText("");
                }
            }
        });

        recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(act, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("ICC", array_practiceList.get(position).ICC);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ServerAccess.getResponse(act, "Items.svc/ItemDetails", "ItemDetails", jsonObject, true, new ServerAccess.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        response = new ItemDetails().response(result);
                        if (response.RS == 1) {
                            Intent intent = new Intent(act, ItemDetailsActivity.class);
                            CommonUtilities.setPreference(act, CommonUtilities.Pref_items_detail, result);
                            startActivity(intent);
                        } else {
                            CommonUtilities.ShowCustomToast(act, response.Msg, false);
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        }));

        recycler_view.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int total = layoutManager.getItemCount();
                int firstVisibleItemCount = layoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemCount = layoutManager.findLastVisibleItemPosition();

                if (!isLoading) {
                    if (total > 0)
                        if ((total - 1) == lastVisibleItemCount) {
                            if (array_practiceList.size() < tc) {
                                page_index++;
//                                lin_dialog.setVisibility(View.VISIBLE);
                                isLoading = true;
                                call_itemlist_api(true, "");
                            }
                        } else {
//                            lin_dialog.setVisibility(View.GONE);
                        }
                }
            }


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

        });
    }


    private void call_itemlist_api(boolean isDialogShow, String keyword) {
        try {
            JSONObject params = new JSONObject();

            params.put("SK", keyword);
            params.put("PI", page_index);

            ServerAccess.getResponse(act, "Items.svc/ItemsList", "ItemsList", params, isDialogShow, new ServerAccess.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    res = new ItemList().response(result);
                    isLoading = false;
                    if (res.RS == 1) {
                        tc = res.TR;
                        if (page_index == 0) {
                            array_practiceList.clear();
                            array_practiceList.addAll(res.lstItemList);
                            mAdapter = new ItemListAdapter(act, array_practiceList);
                            mAdapter.notifyDataSetChanged();
                            recycler_view.setAdapter(mAdapter);
                        } else {
                            array_practiceList.addAll(res.lstItemList);
                            mAdapter.notifyDataSetChanged();
//                            lin_dialog.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onError(String error) {

                }
            });

        } catch (Exception e) {
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        page_index = 0;
        call_itemlist_api(true, edt_search.getText().toString());
        edt_search.setCursorVisible(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        menu.findItem(R.id.action_Edit).setVisible(false);
        menu.findItem(R.id.action_Save).setVisible(false);
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_add).setVisible(true);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.action_add) {
            Intent i = new Intent(act, ItemAddEdit.class);
            i.putExtra("fromDetails", "no");
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    public void OnBack(View view) {
        finish();
    }
}
