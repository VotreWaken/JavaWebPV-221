package itstep.learning.rest;

public class RestResponseStatus {
    private boolean isSuccessful;
    private int code;
    private String phrase;

    public RestResponseStatus() {
    }

    public RestResponseStatus( int code ) {
        this.setCode( code );
        switch (code) {
            case 200: this.setSuccessful(true);  this.setPhrase("OK"); break;
            case 201: this.setSuccessful(true);  this.setPhrase("Created"); break;
            case 202: this.setSuccessful(true);  this.setPhrase("Accepted"); break;
            case 401: this.setSuccessful(false); this.setPhrase("Unauthorized"); break;
            case 403: this.setSuccessful(false); this.setPhrase("Forbidden"); break;
            case 404: this.setSuccessful(false); this.setPhrase("Not Found"); break;
            case 409: this.setSuccessful(false); this.setPhrase("Conflict"); break;
            case 415: this.setSuccessful(false); this.setPhrase("Unsupported Media Type"); break;
            case 422: this.setSuccessful(false); this.setPhrase("Unprocessable Entity"); break;
            case 500: this.setSuccessful(false); this.setPhrase("Internal Server Error"); break;
            case 501: this.setSuccessful(false); this.setPhrase("Not Acceptable"); break;
            default:  this.setSuccessful(false); this.setPhrase("Bad Request"); break;
        }
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public RestResponseStatus setSuccessful(boolean successful) {
        isSuccessful = successful;
        return this;
    }

    public int getCode() {
        return code;
    }

    public RestResponseStatus setCode(int code) {
        this.code = code;
        return this;
    }

    public String getPhrase() {
        return phrase;
    }

    public RestResponseStatus setPhrase(String phrase) {
        this.phrase = phrase;
        return this;
    }
}
