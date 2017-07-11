package cn.a6_79.wepay_buyer;

import org.json.JSONObject;

import java.util.ArrayList;

import cn.a6_79.wepay_buyer.NetPack.HttpTaskRequest;
import cn.a6_79.wepay_buyer.NetPack.OnAsyncTaskListener;
import cn.a6_79.wepay_buyer.NetPack.ThreadTask;

public class API {
    private static String cookie = null;
    private static ArrayList<APITag> apis = new ArrayList<>();

    static void init() {
        String GET = "GET", POST = "POST", PUT = "PUT", DELETE = "DELETE";
        apis.clear();
        apis.add(new APITag(CAPTCHA, PUT, "/session"));
        apis.add(new APITag(REGISTER, POST, "/user"));
        apis.add(new APITag(LOGIN, POST, "/session"));
        apis.add(new APITag(LOGOUT, DELETE, "/session"));
        apis.add(new APITag(EDIT_ADDRESS, PUT, "/user/address"));
        apis.add(new APITag(GET_ADDRESS, GET, "/user/address"));
        apis.add(new APITag(ADD_CARD, POST, "/card"));
        apis.add(new APITag(SET_DEFAULT_CARD, PUT, "/user/default-card"));
        apis.add(new APITag(DELETE_CARD, DELETE, "/card/<0>"));
        apis.add(new APITag(GET_CARD, GET, "/card"));
        apis.add(new APITag(GET_CATEGORY, GET, "/category"));
        apis.add(new APITag(GET_GOOD_IN_CATEGORY, GET, "/category/<0>/good"));
        apis.add(new APITag(ADD_BUTTON, POST, "/button"));
        apis.add(new APITag(EDIT_BUTTON, PUT, "/button/<0>"));
        apis.add(new APITag(DELETE_BUTTON, DELETE, "/button/<0>"));
        apis.add(new APITag(GET_BUTTON, GET, "/button"));
        apis.add(new APITag(GET_ORDER, GET, "/order?status=<0>&page=<1>&count=<2>"));
        apis.add(new APITag(RECEIVE, PUT, "/order/<0>/status"));
    }

    private static String host = "https://aks.6-79.cn";

    private static int CAPTCHA = 1;  // 发送验证码
    private static int REGISTER = 2;  // 注册
    private static int LOGIN = 3;  // 登录
    private static int LOGOUT = 4;  // 登出
    private static int EDIT_ADDRESS = 5;  // 编辑收货信息
    private static int GET_ADDRESS = 6;  // 获取收货信息
    private static int ADD_CARD = 7;  // 增加银行卡
    private static int SET_DEFAULT_CARD = 8;  // 设置默认银行卡
    private static int DELETE_CARD = 9;  // 删除银行卡
    private static int GET_CARD = 10;  // 获取银行卡列表
    private static int GET_CATEGORY = 11;  // 获取类别
    private static int GET_GOOD_IN_CATEGORY = 12;  // 获取类别所有商品
    private static int ADD_BUTTON = 13;  // 增加按钮
    private static int EDIT_BUTTON = 14;  // 编辑按钮
    private static int DELETE_BUTTON = 15;  // 删除按钮
    private static int GET_BUTTON = 16;  // 获取按钮列表
    private static int GET_ORDER = 17;  // 获取订单列表
    private static int RECEIVE = 18;  // 确认收货

    private static APITag find(int tag) {
        for (APITag item : apis) {
            if (item.tag == tag)
                return item;
        }
        return null;
    }
    static ThreadTask receive(int orderId, OnAsyncTaskListener listener) {
        APITag apiTag = find(RECEIVE);
        if (apiTag == null)
            return null;
        String url = apiTag.url.replace("<0>", String.valueOf(orderId));
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + url, apiTag.method, "{}", cookie);
        return new ThreadTask(httpTaskRequest, listener);
    }
    static ThreadTask getOrder(String status, int page, int count, OnAsyncTaskListener listener) {
        APITag apiTag = find(GET_ORDER);
        if (apiTag == null)
            return null;
        String url = apiTag.url.
                replace("<0>", status).
                replace("<1>", String.valueOf(page)).
                replace("<2>", String.valueOf(count));
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + url, apiTag.method, "{}", cookie);
        return new ThreadTask(httpTaskRequest, listener);
    }
    static ThreadTask getButton(OnAsyncTaskListener listener) {
        return lazyGet(GET_BUTTON, listener);
    }
    static ThreadTask deleteButton(int buttonId, OnAsyncTaskListener listener) {
        APITag apiTag = find(DELETE_BUTTON);
        if (apiTag == null)
            return null;
        String url = apiTag.url.replace("<0>", String.valueOf(buttonId));
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + url, apiTag.method, "{}", cookie);
        return new ThreadTask(httpTaskRequest, listener);
    }
    static ThreadTask editButton(int buttonId, int goodId, int number, OnAsyncTaskListener listener) {
        APITag apiTag = find(EDIT_BUTTON);
        if (apiTag == null)
            return null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("good_id", goodId).put("number", number);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        String url = apiTag.url.replace("<0>", String.valueOf(buttonId));
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + url, apiTag.method, jsonObject.toString(), cookie);
        return new ThreadTask(httpTaskRequest, listener);
    }
    static ThreadTask addButton(int goodId, int number, OnAsyncTaskListener listener) {
        APITag apiTag = find(ADD_BUTTON);
        if (apiTag == null)
            return null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("good_id", goodId).put("number", number);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + apiTag.url, apiTag.method, jsonObject.toString(), cookie);
        return new ThreadTask(httpTaskRequest, listener);
    }
    static ThreadTask getGoodInCategory(int categoryId, OnAsyncTaskListener listener) {
        APITag apiTag = find(GET_GOOD_IN_CATEGORY);
        if (apiTag == null)
            return null;
        String url = apiTag.url.replace("<0>", String.valueOf(categoryId));
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + url, apiTag.method, "{}", cookie);
        return new ThreadTask(httpTaskRequest, listener);
    }

    static ThreadTask getCategory(OnAsyncTaskListener listener) {
        return lazyGet(GET_CATEGORY, listener);
    }

    static ThreadTask getCard(OnAsyncTaskListener listener) {
        return lazyGet(GET_CARD, listener);
    }

    static ThreadTask deleteCard(String cardId, OnAsyncTaskListener listener) {
        APITag apiTag = find(DELETE_CARD);
        if (apiTag == null)
            return null;
        String url = apiTag.url.replace("<0>", cardId);
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + url, apiTag.method, "{}", cookie);
        return new ThreadTask(httpTaskRequest, listener);
    }

    static ThreadTask setDefaultCard(String cardId, OnAsyncTaskListener listener) {
        APITag apiTag = find(SET_DEFAULT_CARD);
        if (apiTag == null)
            return null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("card_id", cardId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + apiTag.url, apiTag.method, jsonObject.toString(), cookie);
        return new ThreadTask(httpTaskRequest, listener);
    }

    static ThreadTask addCard(String card, int isDefault, OnAsyncTaskListener listener) {
        APITag apiTag = find(ADD_CARD);
        if (apiTag == null)
            return null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("card", card).put("is_default", isDefault);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + apiTag.url, apiTag.method, jsonObject.toString(), cookie);
        return new ThreadTask(httpTaskRequest, listener);
    }

    static ThreadTask getAddress(OnAsyncTaskListener listener) {
        return lazyGet(GET_ADDRESS, listener);
    }

    static ThreadTask editAddress(String realName, String address, OnAsyncTaskListener listener) {
        APITag apiTag = find(EDIT_ADDRESS);
        if (apiTag == null)
            return null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("real_name", realName).put("address", address);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + apiTag.url, apiTag.method, jsonObject.toString(), cookie);
        return new ThreadTask(httpTaskRequest, listener);
    }

    static ThreadTask logout(OnAsyncTaskListener listener) {
        APITag apiTag = find(LOGOUT);
        if (apiTag == null)
            return null;
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + apiTag.url, apiTag.method, "{}", cookie);
        return new ThreadTask(httpTaskRequest, listener);
    }

    static ThreadTask login(String username, String password, OnAsyncTaskListener listener) {
        APITag apiTag = find(LOGIN);
        if (apiTag == null)
            return null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username).put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + apiTag.url, apiTag.method, jsonObject.toString(), cookie);
        return new ThreadTask(httpTaskRequest, listener);
    }

    static ThreadTask register(String password, String captcha, OnAsyncTaskListener listener) {
        APITag apiTag = find(REGISTER);
        if (apiTag == null)
            return null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.
                    put("password", password).
                    put("is_seller", 0).
                    put("brand", "").
                    put("username", "").
                    put("captcha", captcha);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + apiTag.url, apiTag.method, jsonObject.toString(), cookie);
        return new ThreadTask(httpTaskRequest, listener);
    }

    static ThreadTask captcha(String phone, OnAsyncTaskListener listener) {
        APITag apiTag = find(CAPTCHA);
        if (apiTag == null)
            return null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", phone);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + apiTag.url, apiTag.method, jsonObject.toString(), cookie);
        return new ThreadTask(httpTaskRequest, listener);
    }

    private static ThreadTask lazyGet(int tag, OnAsyncTaskListener listener) {
        APITag apiTag = find(tag);
        if (apiTag == null)
            return null;
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host+apiTag.url, apiTag.method, "{}", cookie);
        return new ThreadTask(httpTaskRequest, listener);
    }
}

class APITag {
    int tag;
    String url;
    String method;
    APITag(int tag, String method, String url) {
        this.tag = tag;
        this.method = method;
        this.url = url;
    }
}
