package me.sam;

public class Debugger {

    public static void log(String string) {
        if (CloudyCurrencies.instance.getConfig().getBoolean("debug")) {
            CloudyCurrencies.instance.getLogger().info("[DEBUG] " + string);
        }
    }
}
