package com.applemango.presentation.util.manager

import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission

object PermissionManager {

    /**
     * @param permissions 권한을 요청할 String 타입의 권한 배열
     * @param grantCallback 권한 허용 콜백
     * @param deniedCallback 권한 거부 콜백
     */
    fun requestPermission(
        vararg permissions: String,
        grantCallback: (() -> Unit)?,
        deniedCallback: ((MutableList<String>?) -> Unit)?
    ) {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    grantCallback?.invoke()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    deniedCallback?.invoke(deniedPermissions)
                }
            })
            .setPermissions(*permissions)
            .check()
    }
}