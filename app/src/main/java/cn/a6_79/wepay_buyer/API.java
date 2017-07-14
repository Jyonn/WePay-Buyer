package cn.a6_79.wepay_buyer;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.a6_79.wepay_buyer.NetPack.HttpTaskRequest;
import cn.a6_79.wepay_buyer.NetPack.HttpTaskResponse;
import cn.a6_79.wepay_buyer.NetPack.OnAsyncHttpTaskListener;
import cn.a6_79.wepay_buyer.NetPack.HttpThreadTask;

public class API {
    public static int buttonID;
    public static int number;
    public static View.OnClickListener currentDeleteCard = null;
    public static String cookie = null;
    private static ArrayList<APITag> apis = new ArrayList<>();

    private static class CommonListener {
        OnAsyncHttpTaskListener httpListener;
        CommonListener(final ResponseListener listener) {
            httpListener = new OnAsyncHttpTaskListener() {
                @Override
                public void callback(HttpTaskResponse httpTaskResponse) throws JSONException {
                    if (httpTaskResponse.getCookie() != null)
                        cookie = httpTaskResponse.getCookie();
                    listener.callback(httpTaskResponse.getResponse());
                }
            };
        }
    }

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
        apis.add(new APITag(GET_CATEGORY, GET, "/category?type=unset"));
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

    static HttpThreadTask receive(int orderId, ResponseListener listener) {
        APITag apiTag = find(RECEIVE);
        if (apiTag == null)
            return null;
        String url = apiTag.url.replace("<0>", String.valueOf(orderId));
        CommonListener commonListener = new CommonListener(listener);
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + url, apiTag.method, "{}", cookie);
        return new HttpThreadTask(httpTaskRequest, commonListener.httpListener);
    }
    static HttpThreadTask getOrder(String status, int page, int count, ResponseListener listener) {
        APITag apiTag = find(GET_ORDER);
        if (apiTag == null)
            return null;
        String url = apiTag.url.
                replace("<0>", status).
                replace("<1>", String.valueOf(page)).
                replace("<2>", String.valueOf(count));
        CommonListener commonListener = new CommonListener(listener);
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + url, apiTag.method, "{}", cookie);
        return new HttpThreadTask(httpTaskRequest, commonListener.httpListener);
    }
    static HttpThreadTask getButton(ResponseListener listener) {
        return lazyGet(GET_BUTTON, listener);
    }
    static HttpThreadTask deleteButton(int buttonId, ResponseListener listener) {
        APITag apiTag = find(DELETE_BUTTON);
        if (apiTag == null)
            return null;
        String url = apiTag.url.replace("<0>", String.valueOf(buttonId));
        CommonListener commonListener = new CommonListener(listener);
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + url, apiTag.method, "{}", cookie);
        return new HttpThreadTask(httpTaskRequest, commonListener.httpListener);
    }
    static HttpThreadTask editButton(int buttonId, int goodId, int number, ResponseListener listener) {
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
        CommonListener commonListener = new CommonListener(listener);
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + url, apiTag.method, jsonObject.toString(), cookie);
        return new HttpThreadTask(httpTaskRequest, commonListener.httpListener);
    }
    static HttpThreadTask addButton(int goodId, int number, ResponseListener listener) {
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
        CommonListener commonListener = new CommonListener(listener);
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + apiTag.url, apiTag.method, jsonObject.toString(), cookie);
        return new HttpThreadTask(httpTaskRequest, commonListener.httpListener);
    }
    static HttpThreadTask getGoodInCategory(int categoryId, ResponseListener listener) {
        APITag apiTag = find(GET_GOOD_IN_CATEGORY);
        if (apiTag == null)
            return null;
        String url = apiTag.url.replace("<0>", String.valueOf(categoryId));
        CommonListener commonListener = new CommonListener(listener);
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + url, apiTag.method, "{}", cookie);
        return new HttpThreadTask(httpTaskRequest, commonListener.httpListener);
    }

    static HttpThreadTask getCategory(ResponseListener listener) {
        return lazyGet(GET_CATEGORY, listener);
    }

    static HttpThreadTask getCard(ResponseListener listener) {
        return lazyGet(GET_CARD, listener);
    }

    static HttpThreadTask deleteCard(String cardId, ResponseListener listener) {
        APITag apiTag = find(DELETE_CARD);
        if (apiTag == null)
            return null;
        String url = apiTag.url.replace("<0>", cardId);
        CommonListener commonListener = new CommonListener(listener);
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + url, apiTag.method, "{}", cookie);
        return new HttpThreadTask(httpTaskRequest, commonListener.httpListener);
    }

    static HttpThreadTask setDefaultCard(String cardId, ResponseListener listener) {
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
        CommonListener commonListener = new CommonListener(listener);
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + apiTag.url, apiTag.method, jsonObject.toString(), cookie);
        return new HttpThreadTask(httpTaskRequest, commonListener.httpListener);
    }

    static HttpThreadTask addCard(String card, int isDefault, ResponseListener listener) {
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
        CommonListener commonListener = new CommonListener(listener);
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + apiTag.url, apiTag.method, jsonObject.toString(), cookie);
        return new HttpThreadTask(httpTaskRequest, commonListener.httpListener);
    }

    static HttpThreadTask getAddress(ResponseListener listener) {
        return lazyGet(GET_ADDRESS, listener);
    }

    static HttpThreadTask editAddress(String realName, String address, ResponseListener listener) {
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
        CommonListener commonListener = new CommonListener(listener);
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + apiTag.url, apiTag.method, jsonObject.toString(), cookie);
        return new HttpThreadTask(httpTaskRequest, commonListener.httpListener);
    }

    static HttpThreadTask logout(ResponseListener listener) {
        APITag apiTag = find(LOGOUT);
        if (apiTag == null)
            return null;
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + apiTag.url, apiTag.method, "{}", cookie);
        CommonListener commonListener = new CommonListener(listener);
        return new HttpThreadTask(httpTaskRequest, commonListener.httpListener);
    }

    static HttpThreadTask login(String username, String password, ResponseListener listener) {
        APITag apiTag = find(LOGIN);
        if (apiTag == null)
            return null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username).put("password", password).put("is_seller", 0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        CommonListener commonListener = new CommonListener(listener);
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + apiTag.url, apiTag.method, jsonObject.toString(), cookie);
        return new HttpThreadTask(httpTaskRequest, commonListener.httpListener);
    }

    static HttpThreadTask register(String password, String captcha, ResponseListener listener) {
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
        CommonListener commonListener = new CommonListener(listener);
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + apiTag.url, apiTag.method, jsonObject.toString(), cookie);
        return new HttpThreadTask(httpTaskRequest, commonListener.httpListener);
    }

    static HttpThreadTask captcha(String phone, ResponseListener listener) {
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
        CommonListener commonListener = new CommonListener(listener);
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host + apiTag.url, apiTag.method, jsonObject.toString(), cookie);
        return new HttpThreadTask(httpTaskRequest, commonListener.httpListener);
    }

    private static HttpThreadTask lazyGet(int tag, ResponseListener listener) {
        APITag apiTag = find(tag);
        if (apiTag == null)
            return null;
        CommonListener commonListener = new CommonListener(listener);
        HttpTaskRequest httpTaskRequest = new HttpTaskRequest(host+apiTag.url, apiTag.method, "{}", cookie);
        return new HttpThreadTask(httpTaskRequest, commonListener.httpListener);
    }

    static JSONObject ResponseShow(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if ((int) jsonObject.get("code") != 0) {
                String msg = (String) jsonObject.get("msg");
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
            return jsonObject;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

interface ResponseListener {
    void callback(String response) throws JSONException;
}