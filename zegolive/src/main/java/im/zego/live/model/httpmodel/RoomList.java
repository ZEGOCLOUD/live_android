package im.zego.live.model.httpmodel;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RoomList {

    @SerializedName("TotalCount")
    public int totalCount;

    @SerializedName("RoomList")
    public List<RoomBean> roomList;

    @Override
    public String toString() {
        return "RoomList{" +
            "roomList=" + roomList + ","+
            "totalCount=" + totalCount +
            '}';
    }
}
