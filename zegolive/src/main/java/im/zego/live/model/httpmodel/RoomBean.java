package im.zego.live.model.httpmodel;

import com.google.gson.annotations.SerializedName;

public class RoomBean {

    @SerializedName("RoomId")
    private String roomID;

    @SerializedName("UserCount")
    private Integer userNum;

    public String getRoomID() {
        return roomID;
    }

    public Integer getUserNum() {
        return userNum;
    }

    @Override
    public String toString() {
        return "RoomListBean{" +
            "id='" + roomID + '\'' +
            ", userNum=" + userNum +
            '}';
    }
}
