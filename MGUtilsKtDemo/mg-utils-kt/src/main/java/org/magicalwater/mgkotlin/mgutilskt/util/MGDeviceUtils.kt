package org.magicalwater.mgkotlin.mgutilskt.util

import android.net.wifi.WifiManager
import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import java.io.File
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException

/**
 * 裝置相關資訊, 參考
 * https://github.com/Blankj/AndroidUtilCode/blob/master/utilcode/src/main/java/com/blankj/utilcode/util/DeviceUtils.java
 * */
class MGDeviceUtils(context: Context) {

    private val mContext: Context = context

    /**
     * Return whether device is rooted.
     *
     * @return `true`: yes<br></br>`false`: no
     */
    fun isDeviceRooted(): Boolean {
        val su = "su"
        val locations = arrayOf("/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/")
        for (location in locations) {
            if (File(location + su).exists()) {
                return true
            }
        }
        return false
    }

    /**
     * Return the version name of device's system.
     *
     * @return the version name of device's system
     */
    fun getSDKVersionName(): String {
        return android.os.Build.VERSION.RELEASE
    }

    /**
     * Return version code of device's system.
     *
     * @return version code of device's system
     */
    fun getSDKVersionCode(): Int {
        return android.os.Build.VERSION.SDK_INT
    }

    /**
     * Return the android id of device.
     *
     * @return the android id of device
     */
    @SuppressLint("HardwareIds")
    fun getAndroidID(context: Context): String {
        val id = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
        )
        return id ?: ""
    }

    /**
     * Return the MAC address.
     *
     * Must hold
     * `<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />`,
     * `<uses-permission android:name="android.permission.INTERNET" />`
     *
     * @return the MAC address
     */
//    @RequiresPermission(allOf = arrayOf(ACCESS_WIFI_STATE, INTERNET))
//    fun getMacAddress(): String {
//        return getMacAddress(*null as Array<String>?)
//    }

    /**
     * Return the MAC address.
     *
     * Must hold
     * `<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />`,
     * `<uses-permission android:name="android.permission.INTERNET" />`
     *
     * @return the MAC address
     */
//    @RequiresPermission(allOf = [ACCESS_WIFI_STATE, INTERNET])
//    fun getMacAddress(vararg excepts: String): String {
//        var macAddress = getMacAddressByWifiInfo()
//        if (isAddressNotInExcepts(macAddress, *excepts)) {
//            return macAddress
//        }
//        macAddress = getMacAddressByNetworkInterface()
//        if (isAddressNotInExcepts(macAddress, *excepts)) {
//            return macAddress
//        }
//        macAddress = getMacAddressByInetAddress()
//        if (isAddressNotInExcepts(macAddress, *excepts)) {
//            return macAddress
//        }
//        macAddress = getMacAddressByFile()
//        return if (isAddressNotInExcepts(macAddress, *excepts)) {
//            macAddress
//        } else ""
//    }

    private fun isAddressNotInExcepts(address: String, vararg excepts: String): Boolean {
        if (excepts == null || excepts.isEmpty()) {
            return "02:00:00:00:00:00" != address
        }
        for (filter in excepts) {
            if (address == filter) {
                return false
            }
        }
        return true
    }

    @SuppressLint("HardwareIds", "MissingPermission")
    private fun getMacAddressByWifiInfo(): String {
        try {
            val context = mContext.applicationContext
            val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            if (wifi != null) {
                val info = wifi.connectionInfo
                if (info != null) return info.macAddress
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "02:00:00:00:00:00"
    }

    private fun getMacAddressByNetworkInterface(): String {
        try {
            val nis = NetworkInterface.getNetworkInterfaces()
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement()
                if (ni == null || !ni!!.name.equals("wlan0", true)) continue
                val macBytes = ni!!.hardwareAddress
                if (macBytes != null && macBytes!!.isNotEmpty()) {
                    val sb = StringBuilder()
                    for (b in macBytes!!) {
                        sb.append(String.format("%02x:", b))
                    }
                    return sb.substring(0, sb.length - 1)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "02:00:00:00:00:00"
    }

    private fun getMacAddressByInetAddress(): String {
        try {
            val inetAddress = getInetAddress()
            if (inetAddress != null) {
                val ni = NetworkInterface.getByInetAddress(inetAddress)
                if (ni != null) {
                    val macBytes = ni!!.hardwareAddress
                    if (macBytes != null && macBytes!!.isNotEmpty()) {
                        val sb = StringBuilder()
                        for (b in macBytes!!) {
                            sb.append(String.format("%02x:", b))
                        }
                        return sb.substring(0, sb.length - 1)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "02:00:00:00:00:00"
    }

    private fun getInetAddress(): InetAddress? {
        try {
            val nis = NetworkInterface.getNetworkInterfaces()
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement()
                // To prevent phone of xiaomi return "10.0.2.15"
                if (!ni.isUp) continue
                val addresses = ni.inetAddresses
                while (addresses.hasMoreElements()) {
                    val inetAddress = addresses.nextElement()
                    if (!inetAddress.isLoopbackAddress) {
                        val hostAddress = inetAddress.hostAddress
                        if (hostAddress.indexOf(':') < 0) return inetAddress
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

        return null
    }

//    private fun getMacAddressByFile(): String {
//        var result = ShellUtils.execCmd("getprop wifi.interface", false)
//        if (result.result === 0) {
//            val name = result.successMsg
//            if (name != null) {
//                result = ShellUtils.execCmd("cat /sys/class/net/$name/address", false)
//                if (result.result === 0) {
//                    val address = result.successMsg
//                    if (address != null && address!!.length > 0) {
//                        return address
//                    }
//                }
//            }
//        }
//        return "02:00:00:00:00:00"
//    }

//    /**
//     * Return the manufacturer of the product/hardware.
//     *
//     * e.g. Xiaomi
//     *
//     * @return the manufacturer of the product/hardware
//     */
//    fun getManufacturer(): String {
//        return Build.MANUFACTURER
//    }
//
//    /**
//     * Return the model of device.
//     *
//     * e.g. MI2SC
//     *
//     * @return the model of device
//     */
//    fun getModel(): String {
//        var model: String? = Build.MODEL
//        if (model != null) {
//            model = model.trim { it <= ' ' }.replace("\\s*".toRegex(), "")
//        } else {
//            model = ""
//        }
//        return model
//    }
//
//    /**
//     * Return an ordered list of ABIs supported by this device. The most preferred ABI is the first
//     * element in the list.
//     *
//     * @return an ordered list of ABIs supported by this device
//     */
//    fun getABIs(): Array<String> {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Build.SUPPORTED_ABIS
//        } else {
//            if (!TextUtils.isEmpty(Build.CPU_ABI2)) {
//                arrayOf(Build.CPU_ABI, Build.CPU_ABI2)
//            } else arrayOf(Build.CPU_ABI)
//        }
//    }
//
//    /**
//     * Shutdown the device
//     *
//     * Requires root permission
//     * or hold `android:sharedUserId="android.uid.system"`,
//     * `<uses-permission android:name="android.permission.SHUTDOWN/>`
//     * in manifest.
//     */
//    fun shutdown() {
//        ShellUtils.execCmd("reboot -p", true)
//        val intent = Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN")
//        intent.putExtra("android.intent.extra.KEY_CONFIRM", false)
//        Utils.getApp().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
//    }
//
//    /**
//     * Reboot the device.
//     *
//     * Requires root permission
//     * or hold `android:sharedUserId="android.uid.system"` in manifest.
//     */
//    fun reboot() {
//        ShellUtils.execCmd("reboot", true)
//        val intent = Intent(Intent.ACTION_REBOOT)
//        intent.putExtra("nowait", 1)
//        intent.putExtra("interval", 1)
//        intent.putExtra("window", 0)
//        Utils.getApp().sendBroadcast(intent)
//    }
//
//    /**
//     * Reboot the device.
//     *
//     * Requires root permission
//     * or hold `android:sharedUserId="android.uid.system"`,
//     * `<uses-permission android:name="android.permission.REBOOT" />`
//     *
//     * @param reason code to pass to the kernel (e.g., "recovery") to
//     * request special boot modes, or null.
//     */
//    fun reboot(reason: String) {
//        val mPowerManager = Utils.getApp().getSystemService(Context.POWER_SERVICE) as PowerManager
//        try {
//            if (mPowerManager == null) return
//            mPowerManager.reboot(reason)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//    }
//
//    /**
//     * Reboot the device to recovery.
//     *
//     * Requires root permission.
//     */
//    fun reboot2Recovery() {
//        ShellUtils.execCmd("reboot recovery", true)
//    }
//
//    /**
//     * Reboot the device to bootloader.
//     *
//     * Requires root permission.
//     */
//    fun reboot2Bootloader() {
//        ShellUtils.execCmd("reboot bootloader", true)
//    }
}