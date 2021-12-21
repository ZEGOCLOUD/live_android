package im.zego.live.service;

import java.util.ArrayList;
import java.util.List;

import im.zego.live.ZegoRoomManager;
import im.zego.live.ZegoZIMManager;
import im.zego.live.callback.ZegoRoomCallback;
import im.zego.live.listener.ZegoGiftServiceListener;
import im.zego.live.model.ZegoCustomCommand;
import im.zego.live.model.ZegoUserInfo;
import im.zego.zim.ZIM;
import im.zego.zim.entity.ZIMCustomMessage;
import im.zego.zim.entity.ZIMMessage;
import im.zego.zim.enums.ZIMMessageType;

/**
 * room gift send and receive.
 */
public class ZegoGiftService {

    private ZegoGiftServiceListener giftServiceListener;

    /**
     * send gift to room users.
     *
     * @param giftID     giftID
     * @param toUserList send gift target
     * @param callback   operation result callback
     */
    public void sendGift(String giftID, List<String> toUserList, ZegoRoomCallback callback) {
        ZegoUserInfo localUserInfo = ZegoRoomManager.getInstance().userService.localUserInfo;
        ZegoCustomCommand command = new ZegoCustomCommand();
        command.actionType = ZegoCustomCommand.Gift;
        command.target = toUserList;
        command.userID = localUserInfo.getUserID();
        command.content.put("giftID", giftID);
        command.toJson();
        String roomID = ZegoRoomManager.getInstance().roomService.roomInfo.getRoomID();
        ZegoZIMManager.getInstance().zim.sendRoomMessage(command, roomID, (message, errorInfo) -> {
            if (callback != null) {
                callback.roomCallback(errorInfo.code.value());
            }
        });
    }

    public void setListener(ZegoGiftServiceListener listener) {
        this.giftServiceListener = listener;
    }

    public void onReceiveRoomMessage(ZIM zim, ArrayList<ZIMMessage> messageList, String fromRoomID) {
        for (ZIMMessage zimMessage : messageList) {
            if (zimMessage.type == ZIMMessageType.CUSTOM) {
                ZIMCustomMessage zimCustomMessage = (ZIMCustomMessage) zimMessage;
                ZegoCustomCommand command = new ZegoCustomCommand();
                command.type = zimCustomMessage.type;
                command.userID = zimCustomMessage.userID;
                command.fromJson(zimCustomMessage.message);
                if (command.actionType == ZegoCustomCommand.Gift) {
                    String giftID = command.content.get("giftID");
                    List<String> toUserList = command.target;
                    if (giftServiceListener != null) {
                        giftServiceListener.onReceiveGift(giftID, command.userID, toUserList);
                    }
                }
            }
        }
    }
}