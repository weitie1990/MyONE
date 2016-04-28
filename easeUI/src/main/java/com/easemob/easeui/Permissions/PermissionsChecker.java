package com.easemob.easeui.Permissions;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by Thinkpad on 2016/4/15.
 */
public class PermissionsChecker {
    private Context context;

    public PermissionsChecker(Context context) {
        this.context = context;
    }

    public boolean lacksPermissions(String... permissions){
        for(String permission : permissions){
            if (lacksPermission(permission)){
                return true;
            }
        }
        return false;
    }

    private boolean lacksPermission(String permission){
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED;
    }
}

