package im.zego.live.service;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.gson.Gson;
import im.zego.live.http.APIBase;
import im.zego.live.http.IAsyncGetCallback;
import im.zego.live.model.httpmodel.GetRoomListParam;
import im.zego.live.model.httpmodel.RoomBean;
import im.zego.live.model.httpmodel.RoomList;
import im.zego.live.model.httpmodel.RoomRequestCommonParam;

/**
 * Created by rocket_wang on 2021/12/21.
 */
public class ZegoRoomListService {

    private static final String TAG = "RoomApi";
    private static final String roomListUrl = 
    // such as "http://xxxxxxxxxxxxx.herokuapp.com/describe_room_list";
    // see https://github.com/ZEGOCLOUD/room_list_server_nodejs
    private static Gson gson = new Gson();

    public static void getRoomList(int pageNumber, String fromIndex, IAsyncGetCallback<RoomList> reqCallback) {
        Uri.Builder builder = Uri
                .parse(roomListUrl)
                .buildUpon()
                .appendQueryParameter("PageIndex",fromIndex==null?"1":fromIndex)
                .appendQueryParameter("PageSize",String.valueOf(pageNumber));

        String url = builder.build().toString();

        Log.d(TAG, "getRoomList: url:" + url );

        APIBase.asyncGet(url, RoomList.class, new IAsyncGetCallback<RoomList>() {
            @Override
            public void onResponse(int errorCode, @NonNull String message, RoomList responseJsonBean) {
                Log.d(TAG,
                        "getRoomList onResponse() called with: errorCode = [" + errorCode + "], message = [" + message
                                + "], responseJsonBean = [" + responseJsonBean + "]");
                if (reqCallback != null) {
                    reqCallback.onResponse(errorCode, message, responseJsonBean);
                }
            }
        });
    }
}