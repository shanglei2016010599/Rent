package com.zwu.rent;

import java.io.InputStream;

public interface HttpCallbackListener {

    void onFinish(InputStream inputStream);

    void onError(Exception e);

}
