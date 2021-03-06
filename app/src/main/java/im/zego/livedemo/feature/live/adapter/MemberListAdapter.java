package im.zego.livedemo.feature.live.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ImageUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import im.zego.live.helper.UserInfoHelper;
import im.zego.live.model.ZegoRoomUserRole;
import im.zego.live.model.ZegoUserInfo;
import im.zego.livedemo.R;
import im.zego.livedemo.helper.AvatarHelper;


public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.UserListHolder> {

    private IItemOnClickListener<ZegoUserInfo> itemOnClickListener = null;

    private List<ZegoUserInfo> userListInRoom;

    public MemberListAdapter(List<ZegoUserInfo> userListInRoom) {
        this.userListInRoom = new ArrayList<>();
        this.userListInRoom.addAll(userListInRoom);
        sort();
    }

    @NonNull
    @Override
    public UserListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_user, parent, false);
        return new UserListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListHolder holder, int position) {
        final ZegoUserInfo userInfo = userListInRoom.get(position);

        int avatarId = AvatarHelper.getAvatarIdByUserName(userInfo.getUserName());
        holder.ivUserAvatar.setImageBitmap(ImageUtils.toRound(ImageUtils.getBitmap(avatarId), true));
        holder.tvUserName.setText(userInfo.getUserName());
        holder.ivInvite.setVisibility(View.GONE);
        holder.tvUserInfo.setVisibility(View.GONE);
        holder.tvUserInfo.setTextColor(ColorUtils.getColor(R.color.light_gray));

        switch (getRoleType(userInfo)) {
            case Host:
                holder.tvUserInfo.setVisibility(View.VISIBLE);
                holder.tvUserInfo.setText(R.string.user_list_page_host);
                break;
            case CoHost:
            case MeCoHost:
                holder.tvUserInfo.setVisibility(View.VISIBLE);
                holder.tvUserInfo.setText(R.string.user_list_page_conneted);
                holder.tvUserInfo.setTextColor(ColorUtils.getColor(R.color.light_gray2));
                break;
            case InvitedParticipant:
                holder.tvUserInfo.setVisibility(View.VISIBLE);
                holder.tvUserInfo.setText(R.string.user_list_page_invited);
                holder.tvUserInfo.setTextColor(ColorUtils.getColor(R.color.light_gray2));
                break;
            case Me:
                holder.tvUserInfo.setVisibility(View.VISIBLE);
                holder.tvUserInfo.setText(R.string.user_list_page_me);
                break;
            case Participant:
                if (UserInfoHelper.isSelfHost()) {
                    holder.ivInvite.setVisibility(View.VISIBLE);
                }
                break;
        }

        holder.ivInvite.setOnClickListener(v -> {
            if (itemOnClickListener != null) {
                itemOnClickListener.onClick(userInfo);
            }
        });
    }

    public void updateUserList(List<ZegoUserInfo> userList) {
        userListInRoom.clear();
        userListInRoom.addAll(userList);
        sort();
        notifyDataSetChanged();
    }

    private void sort() {
        Collections.sort(userListInRoom, (o1, o2) -> getRoleWeight(o1) - getRoleWeight(o2));
    }

    @Override
    public int getItemCount() {
        return userListInRoom.size();
    }

    public void setItemOnClick(IItemOnClickListener<ZegoUserInfo> itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    static class UserListHolder extends RecyclerView.ViewHolder {

        public ImageView ivUserAvatar;
        public TextView tvUserName;
        public TextView tvUserInfo;
        public ImageView ivInvite;

        public UserListHolder(@NonNull View itemView) {
            super(itemView);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvUserInfo = itemView.findViewById(R.id.tv_user_info);
            ivInvite = itemView.findViewById(R.id.iv_invite);
        }
    }

    private RoleType getRoleType(ZegoUserInfo userInfo) {
        if (UserInfoHelper.isUserIDHost(userInfo.getUserID())) {
            return RoleType.Host;
        } else if (UserInfoHelper.isUserIDSelf(userInfo.getUserID())) {
            if (UserInfoHelper.isUserIDCoHost(userInfo.getUserID())) {
                return RoleType.MeCoHost;
            } else {
                return RoleType.Me;
            }
        } else if (UserInfoHelper.isUserIDCoHost(userInfo.getUserID())) {
            return RoleType.CoHost;
        } else if (userInfo.getRole() == ZegoRoomUserRole.Participant && userInfo.isHasInvited()) {
            return RoleType.InvitedParticipant;
        } else {
            return RoleType.Participant;
        }
    }

    private int getRoleWeight(ZegoUserInfo userInfo) {
        if (UserInfoHelper.isSelfHost()) {
            switch (getRoleType(userInfo)) {
                case Host:
                    return 1;
                case CoHost:
                    return 2;
                case InvitedParticipant:
                case Participant:
                    return 3;
            }
        } else {
            switch (getRoleType(userInfo)) {
                case Host:
                    return 1;
                case Me:
                case MeCoHost:
                    return 2;
                case CoHost:
                    return 3;
                case InvitedParticipant:
                case Participant:
                    return 4;
            }
        }
        return 5;
    }

    enum RoleType {
        Host,
        Participant,
        CoHost,
        InvitedParticipant,
        MeCoHost,
        Me
    }
}
