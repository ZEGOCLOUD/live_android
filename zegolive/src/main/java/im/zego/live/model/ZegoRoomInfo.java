package im.zego.live.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rocket_wang on 2021/12/14.
 */
public class ZegoRoomInfo {
    // room ID
    @SerializedName("RoomId")
    private String roomID;

    // room name
    @SerializedName("RoomName")
    private String roomName;

    // host user ID
    @SerializedName("HostId")
    private String hostID;

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getHostID() {
        return hostID;
    }

    public void setHostID(String hostID) {
        this.hostID = hostID;
    }

    @Override
    public String toString() {
        return "ZegoRoomInfo{" +
                "RoomId='" + roomID + '\'' +
                ", RoomName='" + roomName + '\'' +
                ", HostID='" + hostID + '\'' +
                '}';
    }
}