package me.sam;

import java.util.List;

public class Locale {

    public static Locale instance;

    public Locale() {
        instance = this;
    }

    public String get(String configKey) {
        return CloudyCurrencies.instance.messages.getString("prefix") + CloudyCurrencies.instance.messages.getString(configKey);
    }
    public List<String> getList(String configKey) {
        return CloudyCurrencies.instance.messages.getStringList(configKey);
    }

    public String getNoPrefix(String configKey) {
        return CloudyCurrencies.instance.messages.getString(configKey);
    }
}
