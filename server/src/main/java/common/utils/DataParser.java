package common.utils;

import com.google.gson.Gson;
import common.messages.Account;

/**
 * Created by suparngupta on 4/4/14.
 */
public class DataParser {

    public Account parseAccount(String data) {
        return data == null ? null : (Account) parseString(data, Account.class);
    }

    public Object parseString(String data, Class type) {
        Gson gson = new Gson();
        return gson.fromJson(data, type);
    }
}
