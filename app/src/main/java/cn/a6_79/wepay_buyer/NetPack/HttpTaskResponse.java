package cn.a6_79.wepay_buyer.NetPack;

public class HttpTaskResponse {
    private String cookie;
    private String response;
    HttpTaskResponse(String cookie, String response) {
        this.cookie = cookie;
        this.response = response;
    }
    String getCookie() { return cookie; }
    String getResponse() { return response; }
}
