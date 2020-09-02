package me.lushan.screenshot;

import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookMain implements IXposedHookLoadPackage {
    String packageName = new String();

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        packageName = loadPackageParam.packageName;

        // Hook "window.setFlag(WindowManager.LayoutParams.FLAG_SECURE)"
        XposedHelpers.findAndHookMethod(Window.class, "setFlags", int.class, int.class,
                removeSecureFlagHook);

        // Hook "window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)"
        XposedHelpers.findAndHookMethod(Window.class, "addFlags", int.class,
                removeSecureFlagHook);

        // Hook "SurfaceView.setSecure"
        XposedHelpers.findAndHookMethod(SurfaceView.class, "setSecure", boolean.class,
                removeSetSecureHook);


    }


    private final XC_MethodHook removeSecureFlagHook = new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            if ((Integer) param.args[0] == WindowManager.LayoutParams.FLAG_SECURE){
                param.args[0] = 0;
                XposedBridge.log("Anti Screenshot : 已阻止" + packageName);
            }
        }
    };



    private final XC_MethodHook removeSetSecureHook = new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            param.args[0] = false;
            XposedBridge.log("Anti Screenshot : 已阻止" + packageName);
        }
    };

}