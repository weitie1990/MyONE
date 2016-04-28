package com.lanxiao.doapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.doapp.R;
import com.lanxiao.doapp.adapter.FileAdapter;
import com.lanxiao.doapp.untils.util.FileUtils;
import com.lanxiao.doapp.untils.util.Utils;
import com.lanxiao.doapp.entity.MyFile;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FileBrowActivity extends BaseActivity {
    private ListView fileFlistView;
    private FileAdapter fileAdapter;
    private ArrayList<MyFile> items=new ArrayList<MyFile>();
    private final String ROOT_PATH= Environment.getExternalStorageDirectory().getPath();
    String fileName;
    File file;
    LinearLayout ll_file_backToUp;
    ImageView ib_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_brow);
        ib_back= (ImageView) findViewById(R.id.ib_file_back);
        fileFlistView= (ListView) findViewById(R.id.lv_file);
        ll_file_backToUp= (LinearLayout) findViewById(R.id.ll_file_backToUp);
        items= FileUtils.browserTo(new File(ROOT_PATH),this);
        fileAdapter=new FileAdapter(items,this);
        fileFlistView.setAdapter(fileAdapter);
        fileFlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyFile item = (MyFile) fileAdapter.getItem(i);
                fileName = item.getFile_Name();
                file = new File(FileUtils.currentDir, fileName);
                if (file.isFile()) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("file", item);
                    intent.putExtras(bundle);
                    setResult(RESULT_FILE_BACK, intent);
                    finish();
                } else {
                    if (file.listFiles().length != 0) {
                        items = FileUtils.browserTo(file, getApplicationContext());
                        fileAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        //返回上一级
        ll_file_backToUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FileUtils.currentDir.getPath().equals("/storage/emulated")){
                    Utils.showToast("已经到顶层根目录了！",FileBrowActivity.this);
                    return;
                }
                LogUtils.i(FileUtils.currentDir+"");
                FileUtils.currentDir = new File(FileUtils.currentDir.getParent());
                FileUtils.browserTo(FileUtils.currentDir, getApplicationContext());
                fileAdapter.notifyDataSetChanged();
            }
        });
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
