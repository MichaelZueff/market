package market;

/**
 * Created by PerevalovaMA on 07.10.2016.
 */
public class Response {
    private int code;
    private String body;

    public Response(int code, String body) {
        this.code = code;
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public String getBody() {
        return body;
    }
}
