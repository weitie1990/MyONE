package com.lanxiao.doapp.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AlphabetIndexer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.easeui.widget.EaseSidebar;
import com.example.doapp.R;
import com.lanxiao.doapp.adapter.LocatContactAdapter;
import com.lanxiao.doapp.untils.ChineseSpelling;
import com.lanxiao.doapp.entity.Contact;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class LocationContantActivity extends BaseActivity {
    /**
     * 存储所有手机中的联系人
     */
    private List<Contact> contacts = new ArrayList<Contact>();
    /**
     * 用于进行字母表分组
     */
    private AlphabetIndexer indexer;
    /**
     * 定义字母表的排序规则
     */
    private String alphabet = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /**
     * 联系人ListView
     */
    private ListView contactsListView;
    /**
     * 上次第一个可见元素，用于滚动时记录标识。
     */
    private int lastFirstVisibleItem = -1;
    private LinearLayout titleLayout;
    private TextView title;
    private TextView floating_header;
    private EaseSidebar sidebar;
    private ImageView iv_back;
    private LocatContactAdapter adapter;
    private TextView query;
    private ImageButton clearSearch;
    private List<Contact> contactList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_contant);
        init();
        getContact();

    }

    private void init() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        title = (TextView) findViewById(R.id.title);
        floating_header = (TextView) findViewById(R.id.floating_header);
        sidebar = (EaseSidebar) findViewById(R.id.sidebar);
        contactsListView = (ListView) findViewById(R.id.list);
        sidebar.setListView(contactsListView);
        //搜索框
        query = (TextView) findViewById(R.id.query);
        query.setHint(R.string.search);
        clearSearch = (ImageButton) findViewById(R.id.search_clear);
        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s.toString());
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.setText("");
                hideKeyboard();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 获取本地联系人的数据
     */
    private void getContact() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, PHONES_PROJECTION,
                null, null, ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY);
        if (cursor.getCount() != 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                //得到手机号码
                String phoneNumber = cursor.getString(PHONES_NUMBER_INDEX);
                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                //得到联系人名称
                String contactName = cursor.getString(PHONES_DISPLAY_NAME_INDEX);
                //得到联系人ID
                String sort_key = getSortKey(cursor.getString(PHONES_SORT_KEY_PRIMARY));
                LogUtils.i(sort_key);
                Contact contact = new Contact();
                contact.setName(contactName);
                contact.setNumber(phoneNumber);
                contact.setSortKey(sort_key);
                contacts.add(contact);
            }
        }
        //startManagingCursor(cursor);
        indexer = new AlphabetIndexer(cursor, 2, alphabet);

        adapter = new LocatContactAdapter(this, R.layout.listitemforcontant, contacts);
        Log.i("weitie", "contacts:" + contacts.size());
        adapter.setIndexer(indexer);
        if (contacts.size() != 0) {
            contactsListView.setAdapter(adapter);
            Log.i("weitie", "weitie");
        }
    }


    /**
     * 获取sort key的首个字符，如果是英文字母就直接返回，否则返回#。
     *
     * @param sortKeyString 数据库中读取出的sort key
     * @return 英文字母或者#
     */
    private String getSortKey(String sortKeyString) {
        String key = sortKeyString.substring(0, 1).toUpperCase();
        if (key.matches("[A-Z]")) {
            return key;
        }
        return "#";
    }

    /**
     * 按号码-拼音搜索联系人
     *
     * @param str
     */
    public void search(String str) {
        LogUtils.i("search");
        contactList.clear();
        // 如果搜索条件以0 1 +开头则按号码搜索
        if (str.startsWith("0") || str.startsWith("1") || str.startsWith("+")) {
            for (Contact contact : contacts) {
                if (contact.getNumber().contains(str)) {
                    contactList.add(contact);
                }
            }
            refreshList();
            return;
        }

        boolean isChinese = false;
        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FA5]");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) { // 如果是中文
            isChinese = true;
        }

        for (Contact contact : contacts) {
            if (contains(contact, str, isChinese)) {
                contactList.add(contact);
            } else if (contact.getNumber().contains(str)) {
                contactList.add(contact);
            }

        }
        refreshList();
    }

    private void refreshList() {
        adapter.setData(contactList);
    }

    /**
     * 根据拼音搜索
     * <p/>
     * 正则表达式
     * 拼音
     * 搜索条件是否大于6个字符
     *
     * @return
     */
    public boolean contains(Contact contact, String search, boolean isChinese) {
        if (TextUtils.isEmpty(contact.getName())) {
            return false;
        }

        boolean flag = false;
        if (isChinese) {
            // 根据全拼中文查询
            Pattern pattern = Pattern.compile(search.replace("-", ""),
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(contact.getName());
            return matcher.find();
        }

        // 简拼匹配
        ChineseSpelling finder = ChineseSpelling.getInstance();
        finder.setResource(contact.getName());
        Pattern pattern2 = Pattern.compile(search.toUpperCase(),
                Pattern.CASE_INSENSITIVE);
        Matcher matcher2 = pattern2.matcher(finder.getSpelling());
        flag = matcher2.find();

        return flag;
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
