package com.lanxiao.doapp.entity;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by Thinkpad on 2015/12/3.
 */
public class MyFile implements Serializable {
    private static final long serialVersionUID = -7060210544600464482L;
        private int file_Id;
        private String file_Name;
        private String file_Path;
        private String file_lenth;
        private String file_CreateTime;
        private String file_Url;
        private int iconId;

    public int getIcon() {
        return iconId;
    }

    public void setIcon(int icon) {
        this.iconId = icon;
    }

    public MyFile() {
        }

        public MyFile(int file_Id, String file_Name, String file_Path, String file_lenth, String file_CreateTime, String file_Url) {
            this.file_Id = file_Id;
            this.file_Name = file_Name;
            this.file_Path = file_Path;
            this.file_lenth = file_lenth;
            this.file_CreateTime = file_CreateTime;
            this.file_Url = file_Url;
        }

        public int getFile_Id() {
            return file_Id;
        }

        public void setFile_Id(int file_Id) {
            this.file_Id = file_Id;
        }

        public String getFile_Name() {
            return file_Name;
        }

        public void setFile_Name(String file_Name) {
            this.file_Name = file_Name;
        }

        public String getFile_Path() {
            return file_Path;
        }

        public void setFile_Path(String file_Path) {
            this.file_Path = file_Path;
        }

        public String getFile_lenth() {
            return file_lenth;
        }

        public void setFile_lenth(String file_lenth) {
            this.file_lenth = file_lenth;
        }

        public String getFile_CreateTime() {
            return file_CreateTime;
        }

        public void setFile_CreateTime(String file_CreateTime) {
            this.file_CreateTime = file_CreateTime;
        }

        public String getFile_Url() {
            return file_Url;
        }

        public void setFile_Url(String file_Url) {
            this.file_Url = file_Url;
        }

}
