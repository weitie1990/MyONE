package com.lanxiao.doapp.entity;

public class ApplyItem {
		private int icon;
		private String name;
		private String info;
		private boolean add;
		public ApplyItem(int icon, String name, String info, boolean add) {
			super();
			this.icon = icon;
			this.name = name;
			this.info = info;
			this.add = add;
		}
	public ApplyItem(String name, boolean add) {
		super();
		this.name = name;
		this.add = add;
	}
	public ApplyItem() {
		super();
	}
		public int getIcon() {
			return icon;
		}
		public void setIcon(int icon) {
			this.icon = icon;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getInfo() {
			return info;
		}
		public void setInfo(String info) {
			this.info = info;
		}
		public boolean isAdd() {
			return add;
		}
		public void setAdd(boolean add) {
			this.add = add;
		}
		
}
