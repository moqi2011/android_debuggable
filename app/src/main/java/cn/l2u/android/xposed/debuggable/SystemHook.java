package cn.l2u.android.xposed.debuggable;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;


public class SystemHook implements IXposedHookZygoteInit {
    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        XposedBridge.hookAllMethods(getClass().getClassLoader().loadClass("com.android.internal.os.ZygoteConnection"),
                "applyDebuggerSystemProperty", new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("applyDebuggerSystemProperty:" + param.args[0]);
                        Object insArguments = param.args[0];
                        Field f = insArguments.getClass().getDeclaredField("debugFlags");
                        f.setAccessible(true);
                        //args.debugFlags |= Zygote.DEBUG_ENABLE_DEBUGGER;
                        int debugFlags = (int) f.get(insArguments);
                        debugFlags |= 1;
                        f.set(insArguments, debugFlags);
                        return null;
                    }
        });
    }
}
