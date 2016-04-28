package com.lanxiao.doapp.myView;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.doapp.R;
import com.lidroid.xutils.util.LogUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;


/**
 * @author yangyu
 *	功能描述：标题按钮上的弹窗（继承自PopupWindow）
 */
public class DoPopup extends PopupWindow {
	private Context mContext;

	//列表弹窗的间隔
	protected final int LIST_PADDING = 20;

	//实例化一个矩形
	private Rect mRect = new Rect();

	//坐标的位置（x、y）
	private final int[] mLocation = new int[2];

	//屏幕的宽度和高度
	private int mScreenWidth,mScreenHeight;

	//判断是否需要添加或更新列表子类项
	private boolean mIsDirty;

	//位置不在中心
	private int popupGravity = Gravity.NO_GRAVITY;

	//弹窗子类项选中时的监听
	private OnItemOnClickListener mItemOnClickListener;

	//定义列表对象
	private ListView mListView;

	//定义弹窗子类项列表
	private ArrayList<String> mActionItems = new ArrayList<String>();

	public DoPopup(Context context){
		//设置布局的参数
		this(context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	public DoPopup(Context context, int width, int height){
		this.mContext = context;
		
		//设置可以获得焦点
		setFocusable(true);
		//设置弹窗内可点击
		setTouchable(true);
		//设置弹窗外可点击
		setOutsideTouchable(true);
		
		//获得屏幕的宽度和高度
		mScreenWidth = Util.getScreenWidth(mContext);
		mScreenHeight = Util.getScreenHeight(mContext);
		
		//设置弹窗的宽度和高度
		setWidth(width);
		setHeight(height);
		setTouchInterceptor(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Log.i("mengdd", "onTouch : ");

				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});
		setBackgroundDrawable(new BitmapDrawable(mContext.getResources()));
		
		//设置弹窗的布局界面
		setContentView(LayoutInflater.from(mContext).inflate(R.layout.title_popup, null));
		
		initUI();
	}
		
	/**
	 * 初始化弹窗列表
	 */
	private void initUI(){
		mListView = (ListView) getContentView().findViewById(R.id.title_list);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,long arg3) {
				//点击子类项后，弹窗消失
				dismiss();
				
				if(mItemOnClickListener != null)
					mItemOnClickListener.onItemClick(mActionItems.get(index), index);
			}
		}); 
	}
	
	/**
	 * 显示弹窗列表界面
	 */
	public void show(View view){
		//获得点击屏幕的位置坐标
		view.getLocationOnScreen(mLocation);
		
		//设置矩形的大小
		mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(),mLocation[1] + view.getHeight()+view.getPaddingBottom());
		
		//判断是否需要添加或更新列表子类项
		if(mIsDirty){
			populateActions();
		}
		
		//显示弹窗的位置
		LogUtils.i(mScreenWidth + "");
		LogUtils.i((mScreenWidth - LIST_PADDING - (getWidth() / 2)) + "");
		int maxWidth=view.getLeft();
		int lastWidth=(getWidth()-view.getWidth())/2;
		showAtLocation(view, popupGravity, maxWidth-lastWidth, mRect.bottom);
	}
	
	/**
	 * 设置弹窗列表子项
	 */
	private void populateActions(){
		mIsDirty = false;
		
		//设置列表的适配器
		mListView.setAdapter(new BaseAdapter() {			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView textView = null;
				
				if(convertView == null){
					textView = new TextView(mContext);
					textView.setTextColor(mContext.getResources().getColor(android.R.color.white));
					textView.setTextSize(14);
					//设置文本居中
					textView.setGravity(Gravity.CENTER);
					//设置文本域的范围
					textView.setPadding(10, 20, 0, 20);
					//设置文本在一行内显示（不换行）
					textView.setSingleLine(true);
				}else{
					textView = (TextView) convertView;
				}
				String item = mActionItems.get(position);

				
				//设置文本文字
				textView.setText(item);
                return textView;
			}
			
			@Override
			public long getItemId(int position) {
				return position;
			}
			
			@Override
			public Object getItem(int position) {
				return mActionItems.get(position);
			}
			
			@Override
			public int getCount() {
				return mActionItems.size();
			}
		}) ;
	}
	
	/**
	 * 添加子类项
	 */
	public void addAction(String action){
		if(action != null){
			mActionItems.add(action);
			mIsDirty = true;
		}
	}
	
	/**
	 * 清除子类项
	 */
	public void cleanAction(){
		if(mActionItems.isEmpty()){
			mActionItems.clear();
			mIsDirty = true;
		}
	}
	
	/**
	 * 根据位置得到子类项
	 */
	public String getAction(int position){
		if(position < 0 || position > mActionItems.size())
			return null;
		return mActionItems.get(position);
	}			
	
	/**
	 * 设置监听事件
	 */
	public void setItemOnClickListener(OnItemOnClickListener onItemOnClickListener){
		this.mItemOnClickListener = onItemOnClickListener;
	}
	
	/**
	 * @author yangyu
	 *	功能描述：弹窗子类项按钮监听事件
	 */
	public static interface OnItemOnClickListener{
		public void onItemClick(String item, int position);
	}
}
