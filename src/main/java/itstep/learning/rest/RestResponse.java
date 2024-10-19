package itstep.learning.rest;

public class RestResponse {
    private RestResponseStatus status;
    private RestMetaData meta;
    private Object data;

    public RestResponseStatus getStatus() {
        return status;
    }


    public RestResponse setStatus( RestResponseStatus status ) {
        this.status = status;
        return this;
    }
    public RestResponse setStatus( int code ) {
        return this.setStatus( new RestResponseStatus( code ) ) ;
    }

    public Object getData() {
        return data;
    }

    public RestResponse setData(Object data) {
        this.data = data;
        return this;
    }

    public RestMetaData getMeta() {
        return meta;
    }

    public RestResponse setMeta(RestMetaData meta) {
        this.meta = meta;
        return this;
    }
}

