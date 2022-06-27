package im.zego.livedemo.feature.room.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.SizeUtils;

import java.util.ArrayList;
import java.util.List;

import im.zego.live.model.httpmodel.RoomBean;
import im.zego.livedemo.R;
import im.zego.livedemo.helper.AvatarHelper;

/**
 * Created by rocket_wang on 2021/12/22.
 */
public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.RoomListViewHolder> {

    private static final String TAG = "RoomListAdapter";
    private List<RoomBean> items = new ArrayList<>();
    private OnClickListener listener;

    public static final int[] coverList = new int[]{
        R.drawable.liveshow_room_1,
        R.drawable.liveshow_room_2,
        R.drawable.liveshow_room_3,
        R.drawable.liveshow_room_4,
        R.drawable.liveshow_room_5,
        R.drawable.liveshow_room_6
    };

    public void setList(List<RoomBean> list) {
        Log.d(TAG, "setList() called with: list = [" + list.size() + "]");
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

    public void addList(List<RoomBean> list) {
        items.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RoomListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_item_room_list, parent, false);
        return new RoomListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomListViewHolder holder, int position) {
        RoomBean item = items.get(position);

        int index = AvatarHelper.getIndex(item.getRoomID());
        Log.d(TAG, "onBindViewHolder: name=" + item.getRoomID() + ", index=" + index);
        Bitmap bitmap = BitmapFactory.decodeResource(holder.itemView.getResources(), coverList[index]);
        Bitmap roundBitmap = ImageUtils.toRoundCorner(bitmap, SizeUtils.dp2px(13f));
        holder.cover.setImageBitmap(roundBitmap);
        holder.roomUserNum.setText(String.valueOf(item.getUserNum()));
        holder.roomTitle.setText(item.getRoomID());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(v, position, item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        listener = onClickListener;
    }

    public interface OnClickListener {

        void onClick(View v, int position, RoomBean item);
    }

    static class RoomListViewHolder extends RecyclerView.ViewHolder {

        ImageView cover;
        TextView roomUserNum;
        TextView roomTitle;

        private RoomListViewHolder(View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.tv_room_cover);
            roomUserNum = itemView.findViewById(R.id.tv_room_user_num);
            roomTitle = itemView.findViewById(R.id.tv_room_title);
        }
    }
}