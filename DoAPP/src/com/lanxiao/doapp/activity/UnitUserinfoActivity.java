package com.lanxiao.doapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.easeui.Conversion;
import com.example.doapp.R;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.PersonInfo;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.myView.CircularImage;
import com.lanxiao.doapp.myView.MainSetItemView;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class UnitUserinfoActivity extends BaseActivity {


    String companyid = null;
    @InjectView(R.id.ll_unit_shortname)
    MainSetItemView llUnitShortname;
    @InjectView(R.id.ll_unit_fullname)
    MainSetItemView llUnitFullname;
    @InjectView(R.id.ll_unit_business)
    MainSetItemView llUnitBusiness;
    @InjectView(R.id.ll_unit_tag)
    MainSetItemView llUnitTag;
    @InjectView(R.id.ll_unit_country)
    MainSetItemView llUnitCountry;
    @InjectView(R.id.ll_unit_province)
    MainSetItemView llUnitProvince;
    @InjectView(R.id.ll_unit_city)
    MainSetItemView llUnitCity;
    @InjectView(R.id.ll_unit_address)
    MainSetItemView llUnitAddress;
    @InjectView(R.id.ll_unit_email)
    MainSetItemView llUnitEmail;
    @InjectView(R.id.ll_unit_phone)
    MainSetItemView llUnitPhone;
    @InjectView(R.id.ll_unit_fax)
    MainSetItemView llUnitFax;
    @InjectView(R.id.ll_unit_url)
    MainSetItemView llUnitUrl;
    @InjectView(R.id.ll_unit_logout)
    MainSetItemView llUnitLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_userinfo);
        ButterKnife.inject(this);
        init();
        getUserInfofromServer();
    }

    private void init() {

    }

    /**
     * 从服务器上面获取公司信息
     */
    private void getUserInfofromServer() {
        try {
            final PersonInfo Parent = DemoApplication.getInstance().getDb().findFirst(Selector.from(PersonInfo.class).where("userid", "=", DemoHelper.getInstance().getCurrentUsernName()));
            RequestParams params = new RequestParams("UTF-8");
            companyid = Parent.getCompanyID();
            LogUtils.i(companyid);
            params.addBodyParameter("companyid", companyid);
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.send(HttpRequest.HttpMethod.POST, Api.COMPANY_INFO, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    LogUtils.i(responseInfo.result);
                    try {
                        JSONObject jb = new JSONObject(responseInfo.result);
                        llUnitShortname.setMiddleText(jb.optString("ShortName"));
                        llUnitFullname.setMiddleText(jb.optString("CompanyName"));
                        llUnitBusiness.setMiddleText(jb.optString("Industry"));
                        llUnitTag.setMiddleText(jb.optString("IndustryTag1"));
                        llUnitCountry.setMiddleText(jb.optString("Country"));
                        llUnitProvince.setMiddleText(jb.optString("City"));
                        llUnitCity.setMiddleText(jb.optString("City"));
                        llUnitAddress.setMiddleText(jb.optString("StreetAddress"));
                        llUnitEmail.setMiddleText(jb.optString("Zip"));
                        llUnitPhone.setMiddleText(jb.optString("PhoneNumber"));
                        llUnitFax.setMiddleText(jb.optString("FAXNumber"));
                        llUnitUrl.setMiddleText(jb.optString("WebSite"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Utils.showToast("无网络，请检查网络设置", getApplicationContext());
                }
            });
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    public void back(View v) {
        setResult(0x17);
        finish();
    }

    @OnClick({R.id.ll_unit_shortname, R.id.ll_unit_fullname, R.id.ll_unit_business, R.id.ll_unit_tag, R.id.ll_unit_country, R.id.ll_unit_province, R.id.ll_unit_city, R.id.ll_unit_address, R.id.ll_unit_email, R.id.ll_unit_phone, R.id.ll_unit_fax, R.id.ll_unit_url,R.id.ll_unit_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_unit_shortname:
                //UnitDiogAlret("设置简称", llUnitShortname, companyid);
                break;
            case R.id.ll_unit_fullname:
                //UnitDiogAlret("设置全称", llUnitFullname, companyid);

                break;
            case R.id.ll_unit_business:
                UnitDiogAlret("设置行业", llUnitBusiness, companyid);

                break;
            case R.id.ll_unit_tag:
                UnitDiogAlret("设置行业标签", llUnitTag, companyid);

                break;
            case R.id.ll_unit_country:
                UnitDiogAlret("设置国家", llUnitCountry, companyid);

                break;
            case R.id.ll_unit_province:
                UnitDiogAlret("设置省份", llUnitProvince, companyid);

                break;
            case R.id.ll_unit_city:
                UnitDiogAlret("设置城市", llUnitCity, companyid);

                break;
            case R.id.ll_unit_address:
                UnitDiogAlret("设置公司地址", llUnitAddress, companyid);

                break;
            case R.id.ll_unit_email:
                UnitDiogAlret("设置公司邮编", llUnitEmail, companyid);

                break;
            case R.id.ll_unit_phone:
                UnitDiogAlret("设置公司电话", llUnitPhone, companyid);

                break;
            case R.id.ll_unit_fax:
                UnitDiogAlret("设置公司传真", llUnitFax, companyid);

                break;
            case R.id.ll_unit_url:
                UnitDiogAlret("设置网址", llUnitUrl, companyid);
                break;
            case R.id.ll_unit_logout:
                updataUnitinfo();
                break;
        }
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
    /**
     * 退出公司
     */
    public void updataUnitinfo(){
        RequestParams params = new RequestParams("UTF-8");
        LogUtils.i(DemoHelper.getInstance().getCurrentUsernName());
        params.addBodyParameter("userid", DemoHelper.getInstance().getCurrentUsernName());
        params.addBodyParameter("fieldname", "CompanyID"+","+"CompanyName"+","+"Department"+","+"DepartmentID");
        params.addBodyParameter("fieldvalue", "-1"+","+""+","+""+","+"");
        HttpUtils httpUtils=new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.UPDATAUSER, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                try {
                    JSONObject jb = new JSONObject(responseInfo.result);
                    if (jb.optString("result").equals("1")) {
                        Utils.showToast("成功退出", getApplicationContext());
                        PersonInfo pf = DemoApplication.getInstance().getDb().findFirst(Selector.from(PersonInfo.class).where("userid", "=", DemoHelper.getInstance().getCurrentUsernName()));
                        pf.setCompanyID("");
                        pf.setCompanyName("");
                        pf.setDepartment("");
                        pf.setDepartmentID("");
                        DemoApplication.getInstance().getDb().update(pf);
                        setResult(0x17);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Utils.showToast("退出失败,请重试", UnitUserinfoActivity.this);
            }
        });
    }
}
