package rvo.com.book.android.notification.retrofit;

import java.util.List;

public class Response {
    public long multicastId;
    public int success;
    public int failure;
    public int canonicalIds;
    public List<Result> results;

    public Response() {

    }

    public Response(long multicastId, int success, int failure, int canonicalIds, List<Result> results) {
        this.multicastId = multicastId;
        this.success = success;
        this.failure = failure;
        this.canonicalIds = canonicalIds;
        this.results = results;
    }

    public int getCanonicalIds() {
        return canonicalIds;
    }

    public int getSuccess() {
        return success;
    }

    public List<Result> getResults() {
        return results;
    }

    public int getFailure() {
        return failure;
    }

    public long getMulticastId() {
        return multicastId;
    }
}
