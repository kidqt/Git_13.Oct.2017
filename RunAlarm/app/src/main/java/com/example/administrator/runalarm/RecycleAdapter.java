package com.example.administrator.runalarm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.SongViewHolder> {
    private List<listNotification> mNotifi;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private String[] listReadCalen,lisrReadRefer,listReadBu,listReadNot,listReadQa,listReadMas;
    File sdcard = Environment.getExternalStorageDirectory();
    final File file1 = new File(sdcard, "SynopexApp/readCalen.txt");
    final File file2 = new File(sdcard, "SynopexApp/readRefer.txt");
    final File file3 = new File(sdcard, "SynopexApp/readBu.txt");
    final File file4 = new File(sdcard, "SynopexApp/readNot.txt");
    final File file5 = new File(sdcard, "SynopexApp/readQa.txt");
    final File file6 = new File(sdcard, "SynopexApp/readMas.txt");
    final File filedeleCalen = new File(sdcard, "SynopexApp/deleCalen.txt");
    final File filedeleNot = new File(sdcard, "SynopexApp/deleNot.txt");
    final File filedeleBu = new File(sdcard, "SynopexApp/deleBu.txt");
    final File filedeleQa = new File(sdcard, "SynopexApp/deleQa.txt");
    final File filedeleMas = new File(sdcard, "SynopexApp/deleMas.txt");
    final File filedeleRefer = new File(sdcard, "SynopexApp/deleRefer.txt");

    public RecycleAdapter(Context context, List<listNotification> datas,String[] listReadCalen,String[] lisrReadRefer,String[] listReadBu,String[] listReadNot,String[] listReadQa,String[] listReadMas) {
        mContext = context;
        mNotifi = datas;
        mLayoutInflater = LayoutInflater.from(context);
        this.listReadCalen = listReadCalen;
        this.lisrReadRefer = lisrReadRefer;
        this.listReadBu = listReadBu;
        this.listReadNot = listReadNot;
        this.listReadQa = listReadQa;
        this.listReadMas = listReadMas;
    }
    public void removeItem(int position) {
        final int posi = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to delete this notification ? ");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                listNotification mNoti = mNotifi.get(posi);
                String column = mNoti.getTable();
                String id = mNoti.getID();

                if (column.equals("캘린더"))
                {
                    recordDelete(filedeleCalen,id);
                }
                else if (column.equals("공지사항"))
                {
                    recordDelete(filedeleNot,id);
                }
                else if (column.equals("자료실"))
                {
                    recordDelete(filedeleRefer,id);
                }
                else if (column.equals("업무기록"))
                {
                    recordDelete(filedeleBu,id);
                }
                else if (column.equals("업무요청"))
                {
                    recordDelete(filedeleQa,id);
                }
                else if (column.equals("마스터플랜"))
                {
                    recordDelete(filedeleMas,id);
                }

                mNotifi.remove(posi);
                notifyItemRemoved(posi);
                notifyItemRangeChanged(posi, mNotifi.size());

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void recordDelete(File mfile,String mID)
    {
        try {
            FileOutputStream fOut = new FileOutputStream(mfile,true);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append(mID + ",");
            myOutWriter.close();
            fOut.close();
        }
        catch (Exception ex)
        {
            Toast.makeText(mContext,"Lỗi 150 -- 1" + ex.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.row_item_noti, parent, false);
        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        //get song in mSong via position
        listNotification mNoti = mNotifi.get(position);
        //bind data to viewholder
        holder.tvDate.setText(mNoti.getDate());
        holder.tvTitle.setText(mNoti.getTitle());
        holder.tvContent.setText(mNoti.getContent());
        holder.tvTable.setText(mNoti.getTable());

        Boolean checkRead = false;
        String mTabletoSetback = mNoti.getTable();
        int check = Integer.parseInt(mNoti.getID());

        if (mTabletoSetback.equals("캘린더"))
        {
            if (listReadCalen!=null) {
                for (int k = 0; k < listReadCalen.length; k++) {
                    if (check == Integer.parseInt(listReadCalen[k])) {
                        checkRead = true;
                    }
                }
            }
        }
        else if (mTabletoSetback.equals("공지사항"))
        {
            if (listReadNot!=null) {
                for (int k = 0; k < listReadNot.length; k++) {
                    if (check == Integer.parseInt(listReadNot[k])) {
                        checkRead = true;
                    }
                }
            }
        }
        else if (mTabletoSetback.equals("자료실"))
        {
            if (lisrReadRefer!=null) {
                for (int k = 0; k < lisrReadRefer.length; k++) {
                    if (check == Integer.parseInt(lisrReadRefer[k])) {
                        checkRead = true;
                    }
                }
            }
        }
        else if (mTabletoSetback.equals("업무기록"))
        {
            if (listReadBu!=null) {
                for (int k = 0; k < listReadBu.length; k++) {
                    if (check == Integer.parseInt(listReadBu[k])) {
                        checkRead = true;
                    }
                }
            }
        }
        else if (mTabletoSetback.equals("업무요청"))
        {
            if (listReadQa!=null) {
                for (int k = 0; k < listReadQa.length; k++) {
                    if (check == Integer.parseInt(listReadQa[k])) {
                        checkRead = true;
                    }
                }
            }
        }
        else if (mTabletoSetback.equals("마스터플랜"))
        {
            if (listReadMas!=null) {
                for (int k = 0; k < listReadMas.length; k++) {
                    if (check == Integer.parseInt(listReadMas[k])) {
                        checkRead = true;
                    }
                }
            }
        }

        if (checkRead == false)
        {
            holder.itemView.getBackground().setColorFilter(Color.parseColor("#CCFFFF"), PorterDuff.Mode.MULTIPLY);
            holder.tvID.setText(mNoti.getID() + "_1");
        }
        else
        {
            holder.tvID.setText(mNoti.getID() + "_0");
        }
    }

    @Override
    public int getItemCount() {
        return mNotifi.size();
    }

    class SongViewHolder extends RecyclerView.ViewHolder {
        private TextView tvID;
        private TextView tvDate;
        private TextView tvTitle;
        private TextView tvContent;
        private TextView tvTable;
        private String mUrl;

        public SongViewHolder(final View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tv_id);
            tvDate =  itemView.findViewById(R.id.tv_date);
            tvTitle =  itemView.findViewById(R.id.tv_title);
            tvContent =  itemView.findViewById(R.id.tv_content);
            tvTable = itemView.findViewById(R.id.tv_table);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listNotification noti = mNotifi.get(getAdapterPosition());
                    final String mtable = noti.getTable();
                    final String mId = noti.getID();
                    final String checkColor = ((TextView) itemView.findViewById(R.id.tv_id)).getText().toString().split("_")[1];
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Confirm");
                    builder.setMessage("Do you want to open Vintrig menu : " + noti.getTable());
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (mtable == "캘린더") {
                                mUrl = "http://tft.synopex.com/calendar/c_01_01.asp";
                                if (checkColor.equals("1")) {
                                    listReadCalen = recordRead(mId, listReadCalen, file1);
                                }
                            } else if (mtable == "공지사항") {
                                mUrl = "http://tft.synopex.com/notice/notice_01.asp";
                                if (checkColor.equals("1")) {
                                    listReadNot = recordRead(mId, listReadNot, file4);
                                }
                            } else if (mtable == "자료실") {
                                mUrl = "http://tft.synopex.com/reference_room/ref_01.asp";
                                if (checkColor.equals("1")) {
                                    lisrReadRefer = recordRead(mId, lisrReadRefer, file2);
                                }
                            } else if (mtable == "업무기록") {
                                mUrl = "http://tft.synopex.com/business_log/log_01.asp";
                                if (checkColor.equals("1")) {
                                    listReadBu = recordRead(mId, listReadBu, file3);
                                }
                            } else if (mtable == "업무요청") {
                                mUrl = "http://tft.synopex.com/board/board_02.asp";
                                if (checkColor.equals("1")) {
                                    listReadQa = recordRead(mId, listReadQa, file5);
                                }
                            } else if (mtable == "마스터플랜") {
                                mUrl = "http://tft.synopex.com/master/m_01_01.asp";
                                if (checkColor.equals("1")) {
                                    listReadMas = recordRead(mId, listReadMas, file6);
                                }
                            }
                            itemView.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
                            mContext.startActivity(browserIntent);
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }

        public String[] recordRead(String id,String[] listRead,File mFile)
        {
            try {
                FileOutputStream fOut = null;
                if (listRead == null) {
                    fOut = new FileOutputStream(mFile, false);
                    OutputStreamWriter myOutWriter =
                            new OutputStreamWriter(fOut);
                    myOutWriter.append(String.valueOf(id));
                    myOutWriter.close();
                    fOut.close();
                    listRead = String.valueOf(id).split(",");
                } else {
                    fOut = new FileOutputStream(mFile, true);
                    OutputStreamWriter myOutWriter =
                            new OutputStreamWriter(fOut);
                    myOutWriter.append("," + String.valueOf(id));
                    myOutWriter.close();
                    fOut.close();
                    listRead[listRead.length - 1] = String.valueOf(id);
                }
            }
            catch (Exception ex)
            {
                Toast.makeText(mContext, "Lỗi Dòng 231", Toast.LENGTH_SHORT).show();
            }
            return listRead;
        }
    }
}