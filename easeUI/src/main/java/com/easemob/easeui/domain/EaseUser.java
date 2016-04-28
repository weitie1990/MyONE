/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.easeui.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.easemob.chat.EMContact;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NotNull;

import java.io.Serializable;

public class EaseUser extends EMContact implements Parcelable  {
    
    /**
     * 昵称首字母
     */
	@Id
	public int id;
	public String whosfriend;

	public String getWhosfriend() {
		return whosfriend;
	}

	public void setWhosfriend(String whosfriend) {
		this.whosfriend = whosfriend;
	}

	public String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String initialLetter;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String nickName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	/*//只是举例，所以剩下字段还请自行添加
	@Foreign(column = "company_id", foreign = "id")//前面id为存在本表里面外键的字段名(company_id)，后面id为对应父表主键字段名
	public ContantTongShi parent;*/
	@Column(column = "company_id")
	private int companyId;
	private String cellphone;

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

/*	public ContantTongShi getParent() {
		return parent;
	}

	public void setParent(ContantTongShi parent) {
		this.parent = parent;
	}*/

	/**
	 * 用户头像
	 */
	protected String avatar;
	
	public EaseUser(String username){
	    this.username = username;
	}
	public EaseUser(){
	}

	public String getInitialLetter() {
		return initialLetter;
	}

	public void setInitialLetter(String initialLetter) {
		this.initialLetter = initialLetter;
	}


	public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
	public int hashCode() {
		return 17 * getUsername().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof EaseUser)) {
			return false;
		}
		return getUsername().equals(((EaseUser) o).getUsername());
	}

	@Override
	public String toString() {
		return nick == null ? username : nick;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.whosfriend);
		dest.writeString(this.userId);
		dest.writeString(this.initialLetter);
		dest.writeString(this.nickName);
		dest.writeInt(this.companyId);
		dest.writeString(this.cellphone);
		dest.writeString(this.avatar);
	}

	protected EaseUser(Parcel in) {
		this.id = in.readInt();
		this.whosfriend = in.readString();
		this.userId = in.readString();
		this.initialLetter = in.readString();
		this.nickName = in.readString();
		this.companyId = in.readInt();
		this.cellphone = in.readString();
		this.avatar = in.readString();
	}

	public static final Parcelable.Creator<EaseUser> CREATOR = new Parcelable.Creator<EaseUser>() {
		@Override
		public EaseUser createFromParcel(Parcel source) {
			return new EaseUser(source);
		}

		@Override
		public EaseUser[] newArray(int size) {
			return new EaseUser[size];
		}
	};
}
