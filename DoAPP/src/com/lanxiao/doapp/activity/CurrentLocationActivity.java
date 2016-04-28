package com.lanxiao.doapp.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.example.doapp.R;
import com.lanxiao.doapp.adapter.CurrentLocationAdapter;
import com.lanxiao.doapp.adapter.SreachLocationAdapter;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.untils.PoiOverlay;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

public class CurrentLocationActivity extends BaseActivity implements BaiduMap.OnMapStatusChangeListener {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private DemoApplication mApplication;
    private ImageView mBack;
    private EditText mSearch;
    private ImageButton clearSearch;
    private TextView tv_location_putdow,sign_search;
    private ListView mDisplay;
    private CurrentLocationAdapter mAdapter;
    private SreachLocationAdapter mSreachLocationAdapter;
    private LocationClient mClient;
    private LocationClientOption mOption;
    private Double latitude=0.00;
    private Double longitude=0.00;
    private boolean isFail=false;
    /**
     * 定位模式
     */
    private MyLocationConfiguration.LocationMode mCurrentMode;
    /**
     * 是否是第一次定位
     */
    private boolean isFirstLoc = true;
    /**
     * 定位城市
     */
    private String city;
    /**
     * 反地理编码
     */
    private GeoCoder geoCoder;
    /**
     * 搜索输入框对应的ListView
     */
    private ListView searchPois;
    String type=null;
    String name=null;
    private PoiInfo MPoiInfo;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_current_location);
        mApplication = (DemoApplication) getApplication();
        initLBS();
        findViewById();
        setListener();
        mClient.start();




    }

    private void findViewById() {
        MPoiInfo=getIntent().getParcelableExtra("poiInfo");
        sign_search= (TextView) findViewById(R.id.sign_search);
        type=getIntent().getStringExtra("type");

        mBack = (ImageView) findViewById(R.id.currentlocation_back);
        mSearch = (EditText) findViewById(R.id.query);
        clearSearch= (ImageButton) findViewById(R.id.search_clear);
        mDisplay = (ListView) findViewById(R.id.currentlocation_display);
        searchPois = (ListView) findViewById(R.id.main_search_pois);
        tv_location_putdow= (TextView) findViewById(R.id.tv_location_putdow);
        mMapView = (MapView) findViewById(R.id.main_bdmap);
        mBaiduMap = mMapView.getMap();
        if(type!=null){
            //mBaiduMap.setMapStatusLimits(new LatLngBounds.Builder().include(northeast).include(southwest).build());
            sign_search.setVisibility(View.VISIBLE);
            mSearch.setHint("查找附近公司写字楼");
        }
    }

    private void setListener() {
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder().zoom(18).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        //地图状态改变相关监听
        mBaiduMap.setOnMapStatusChangeListener(this);

        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        //定位图层显示方式
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

        /**
         * 设置定位图层配置信息，只有先允许定位图层后设置定位图层配置信息才会生效
         * customMarker用户自定义定位图标
         * enableDirection是否允许显示方向信息
         * locationMode定位图层显示方式
         */
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, null));
        mBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
                // overridePendingTransition(0, R.anim.roll_down);
            }
        });
        /**
         * 取消按钮点击事件
         */
        tv_location_putdow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDisplay.setVisibility(View.VISIBLE);
                searchPois.setVisibility(View.GONE);
                tv_location_putdow.setVisibility(View.GONE);
            }
        });
        /**
         *定位的listitem点击事件
         */
        mDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PoiInfo poiInfo = (PoiInfo) mAdapter.getItem(i);
                if (type == null) {
                    Intent intent = new Intent();
                    Bundle bundle=new Bundle();
                    bundle.putParcelable("poiInfo",poiInfo);
                    intent.putExtras(bundle);
                    setResult(RESULT_LOCATION_CURRENT, intent);
                } else {
                    Intent intent = new Intent(CurrentLocationActivity.this,SignInActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putParcelable("poiInfo", poiInfo);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                finish();
            }
        });
        /**
         *附近搜索的listitem点击事件
         */
        searchPois.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PoiInfo poiInfo = (PoiInfo) mSreachLocationAdapter.getItem(i);
                if (type == null) {
                    Intent intent = new Intent();
                    Bundle bundle=new Bundle();
                    bundle.putParcelable("poiInfo", poiInfo);
                    intent.putExtras(bundle);
                    setResult(RESULT_LOCATION_CURRENT, intent);
                }else {
                    Intent intent = new Intent(CurrentLocationActivity.this,SignInActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putParcelable("poiInfo", poiInfo);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                    finish();
                }
        });
        /**
         * 输入框改变事件
         */
        mSearch.addTextChangedListener(new

                                               TextWatcher() {

                                                   public void onTextChanged(CharSequence s, int start, int before,
                                                                             int count) {
                                                       if (s.length() > 0) {
                                                           clearSearch.setVisibility(View.VISIBLE);
                                                       } else {
                                                           clearSearch.setVisibility(View.INVISIBLE);
                                                       }
                                                   }

                                                   public void beforeTextChanged(CharSequence s, int start, int count,
                                                                                 int after) {

                                                   }

                                                   public void afterTextChanged(Editable s) {
                                                       LogUtils.i("isfali:"+isFail+"");
                                                       if(isFail){
                                                           return;
                                                       }
                                                       if (s.length() == 0 || "".equals(s.toString())) {
                                                           searchPois.setVisibility(View.GONE);
                                                           tv_location_putdow.setVisibility(View.GONE);
                                                       } else {
                                                           //创建PoiSearch实例
                                                           PoiSearch poiSearch = PoiSearch.newInstance();
                                                           if(type!=null&&type.equals("jiandao")) {
                                                              nearbySearch(1,s.toString(),poiSearch);
                                                           }else {
                                                               //城市内检索
                                                               PoiCitySearchOption poiCitySearchOption = new PoiCitySearchOption();
                                                               //关键字
                                                               poiCitySearchOption.keyword(s.toString());
                                                               //城市
                                                               poiCitySearchOption.city(city);
                                                               //设置每页容量，默认为每页10条
                                                               poiCitySearchOption.pageCapacity(20);
                                                               //分页编号
                                                               poiCitySearchOption.pageNum(1);
                                                               poiSearch.searchInCity(poiCitySearchOption);
                                                           }
                                                           //设置poi检索监听者
                                                           poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
                                                               //poi 查询结果回调
                                                               @Override
                                                               public void onGetPoiResult(PoiResult poiResult) {
                                                                   if (poiResult == null
                                                                           || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
                                                                       Toast.makeText(CurrentLocationActivity.this, "未找到结果",
                                                                               Toast.LENGTH_LONG).show();
                                                                       return;
                                                                   }
                                                                   List<PoiInfo> poiInfos = poiResult.getAllPoi();
                                                                   if (poiInfos != null) {
                                                                       mBaiduMap.clear();
                                                                       MyPoiOverlay poiOverlay = new MyPoiOverlay(mBaiduMap);
                                                                       poiOverlay.setData(poiResult);// 设置POI数据
                                                                       mBaiduMap.setOnMarkerClickListener(poiOverlay);
                                                                       poiOverlay.addToMap();// 将所有的overlay添加到地图上
                                                                       poiOverlay.zoomToSpan();
                                                                       mSreachLocationAdapter = new SreachLocationAdapter(CurrentLocationActivity.this, poiInfos);
                                                                       searchPois.setVisibility(View.VISIBLE);
                                                                       tv_location_putdow.setVisibility(View.VISIBLE);
                                                                       mDisplay.setVisibility(View.GONE);
                                                                       searchPois.setAdapter(mSreachLocationAdapter);
                                                                   }
                                                               }

                                                               //poi 详情查询结果回调
                                                               @Override
                                                               public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                                                               }
                                                           });
                                                       }
                                                   }
                                               });
       mClient.registerLocationListener(new BDLocationListener() {
           @Override
           public void onReceiveLocation(BDLocation arg0) {
               //如果bdLocation为空或mapView销毁后不再处理新数据接收的位置
               if (arg0 == null || mBaiduMap == null) {
                   isFail = true;
                   return;
               }
               isFail = false;
               //定位数据
               MyLocationData data = new MyLocationData.Builder()
                       //定位精度bdLocation.getRadius()
                       .accuracy(arg0.getRadius())
                               //此处设置开发者获取到的方向信息，顺时针0-360
                       .direction(arg0.getDerect())
                               //经度
                       .latitude(arg0.getLatitude())
                               //纬度
                       .longitude(arg0.getLongitude())
                               //构建
                       .build();

               //设置定位数据
               mBaiduMap.setMyLocationData(data);
               //是否是第一次定位
               if (isFirstLoc) {
                   isFirstLoc = false;
                   latitude = arg0.getLatitude();
                   longitude = arg0.getLongitude();
                   LatLng ll = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                   MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(ll, 18);
                   mBaiduMap.animateMapStatus(msu);
               }
               //获取城市，待会用于POISearch
               city = arg0.getCity();
               //创建GeoCoder实例对象
               geoCoder = GeoCoder.newInstance();
               //发起反地理编码请求(经纬度->地址信息)
               ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption();
               //设置反地理编码位置坐标
               reverseGeoCodeOption.location(new LatLng(arg0.getLatitude(), arg0.getLongitude()));
               geoCoder.reverseGeoCode(reverseGeoCodeOption);

               //设置查询结果监听者
               geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                   //地理编码查询结果回调函数
                   @Override
                   public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

                   }

                   //反地理编码查询结果回调函数
                   @Override
                   public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                       List<PoiInfo> poiInfos = reverseGeoCodeResult.getPoiList();
                       if (poiInfos != null) {
                           Log.i("weitie", "poinfos:" + poiInfos.size());
                           if (MPoiInfo != null) {
                               poiInfos.add(0, MPoiInfo);
                           }
                           mAdapter = new CurrentLocationAdapter(getApplicationContext(), poiInfos);
                           mDisplay.setAdapter(mAdapter);
                       }
                   }
               });
               //new Async().execute();
           }


       });
        sign_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CurrentLocationActivity.this, MeetingWebActivity.class);
                intent1.putExtra("result", Api.SIGNIN_SEARCH + DemoHelper.getInstance().getCurrentUsernName());
                intent1.putExtra("type", "2");
                startActivity(intent1);
            }
        });
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearch.getText().clear();
                hideKeyboard();
            }
        });
    }
    class MyPoiOverlay extends PoiOverlay {
        public MyPoiOverlay(BaiduMap arg0) {
            super(arg0);
        }
        @Override
        public boolean onPoiClick(int arg0) {
            super.onPoiClick(arg0);
            return true;
        }
    }
    private void initLBS() {
        mOption = new LocationClientOption();
        mOption.setOpenGps(true);
        mOption.setCoorType("bd09ll");
        mOption.setAddrType("all");
        mOption.disableCache(true);
        mOption.setPriority(40);
        mClient = new LocationClient(this, mOption);


    }
    /**
     * 附近检索
     */
    private void nearbySearch(int page,String s,PoiSearch poiSearch) {
        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
        nearbySearchOption.location(new LatLng(latitude, longitude));
        nearbySearchOption.keyword(s);
        nearbySearchOption.radius(1000);// 检索半径，单位是米
        nearbySearchOption.pageNum(page);
        nearbySearchOption.pageCapacity(20);
        poiSearch.searchNearby(nearbySearchOption);// 发起附近检索请求
    }
    /**
     * 范围检索
     */
    private void boundSearch(int page,String s,PoiSearch poiSearch) {
        PoiBoundSearchOption boundSearchOption = new PoiBoundSearchOption();
        LatLng southwest = new LatLng(latitude - 0.01, longitude - 0.012);// 西南
        LatLng northeast = new LatLng(latitude + 0.01, longitude + 0.012);// 东北
        LatLngBounds bounds = new LatLngBounds.Builder().include(southwest)
                .include(northeast).build();// 得到一个地理范围对象
        boundSearchOption.bound(bounds);// 设置poi检索范围
        boundSearchOption.keyword(s);// 检索关键字
        boundSearchOption.pageNum(page);
        poiSearch.searchInBound(boundSearchOption);// 发起poi范围检索请求
    }
    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        mMapView.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        mMapView.onPause();
        MobclickAgent.onPause(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出时停止定位
        mClient.stop();
        //退出时关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        // activity 销毁时同时销毁地图控件
        mMapView.onDestroy();
        //释放资源
        if (geoCoder != null) {
            geoCoder.destroy();
        }

    }
    /**
     * 手势操作地图，设置地图状态等操作导致地图状态开始改变
     *
     * @param mapStatus 地图状态改变开始时的地图状态
     */
    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }
    /**
     * 地图状态变化中
     *
     * @param mapStatus 当前地图状态
     */
    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }
    /**
     * 地图状态改变结束
     *
     * @param mapStatus 地图状态改变结束后的地图状态
     */
    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
            if(type==null||isFirstLoc) {
                mDisplay.setVisibility(View.VISIBLE);
                searchPois.setVisibility(View.GONE);
                tv_location_putdow.setVisibility(View.GONE);
                //地图操作的中心点
                LatLng cenpt = mapStatus.target;
                if(geoCoder==null){
                    geoCoder=GeoCoder.newInstance();
                }
                geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(cenpt));
            }else {
                mDisplay.setVisibility(View.VISIBLE);
                searchPois.setVisibility(View.GONE);
                tv_location_putdow.setVisibility(View.GONE);
                //地图操作的中心点
                LatLng cenpt = mapStatus.target;
                float[] results = new float[1];
                Location.distanceBetween(latitude, longitude, cenpt.latitude, cenpt.longitude, results);
                LogUtils.i(results[0] + "");
                if(results[0]>2000){
                    Utils.showToast("超出检索范围两千米",CurrentLocationActivity.this);
                    return;
                }
                geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(cenpt));

            }
    }
}
