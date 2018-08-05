package com.example.administrator.sendfile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 */

public class FileChooseAdapter extends BaseAdapter {
    List<NameFile> nameFileList;
    Context context;
    public FileChooseAdapter( List<NameFile> nameFileList,
            Context context){
        this.context = context;
        this.nameFileList = nameFileList;
    }
    @Override
    public int getCount() {
        return nameFileList.size();
    }

    @Override
    public Object getItem(int position) {
        return nameFileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.file_choose_item , parent, false);
            holder = new ViewHolder();
            holder.file_pb = (ProgressBar) convertView.findViewById(R.id.fileProgressBar);
            holder.file_Image = (ImageView) convertView.findViewById(R.id.fileImage);
            holder.file_name = (TextView) convertView.findViewById(R.id.fileName);
            holder.file_det = (TextView) convertView.findViewById(R.id.fileDet);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        NameFile files = new NameFile();
        files =  nameFileList.get(position);
        holder.file_name.setText(files.getName());
        holder.file_det.setText(files.getFileBig() + ", " + files.getFileTime());
        switch (files.getType()){
            case 1:
                holder.file_Image.setImageResource(R.mipmap.file_word);
                break;
            case 2:
                holder.file_Image.setImageResource(R.mipmap.file_xl);
                break;
            case 3:
                holder.file_Image.setImageResource(R.mipmap.file_ppt);
                break;
            case 4:
                holder.file_Image.setImageResource(R.mipmap.file_pdf);
                break;
            case 5:
                holder.file_Image.setImageResource(R.mipmap.file_rar);
                break;
            case 6:
                holder.file_Image.setImageResource(R.mipmap.file_rar);
                break;
        }
        if (files.isFileChoose()){
            holder.file_pb.setVisibility(View.VISIBLE);
            holder.file_pb.setProgress(files.getProcess());
        }else {
            holder.file_pb.setVisibility(View.GONE);
        }





        return convertView;
    }

    public class ViewHolder{

        ImageView file_Image;
        TextView file_name, file_det;
        ProgressBar file_pb;

    }
}
