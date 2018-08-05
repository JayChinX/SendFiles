package com.example.administrator.sendfile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.button;
import static android.R.attr.visibility;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button sendFile, deleteFile;
    TextView nameFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        nameFile = (TextView) findViewById(R.id.name_File);
        sendFile = (Button) findViewById(R.id.send_File);
        deleteFile = (Button) findViewById(R.id.delete_Fale);
        sendFile.setOnClickListener(this);
        deleteFile.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_File:
                Intent intent = new Intent(MainActivity.this, FileChooseStartActivity.class);
                startActivityForResult(intent,0);
                break;
            case R.id.delete_Fale:
                dialog();
                break;
        }
    }
    public void dialog(){
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setMessage("你将删除已上传的文件");
        dialog.setTitle("");
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE,"取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "取消删除", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,"确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFile.setVisibility(View.GONE);
                sendFile.setVisibility(View.VISIBLE);
                nameFile.setText("添加附件-小于5M");
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "确认删除", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
        Button btnPositive = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);
        btnNegative.setTextColor(Color.BLACK);
        btnNegative.setTextSize(18);
        btnPositive.setTextColor(Color.BLACK);
        btnPositive.setTextSize(18);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0){
            String fileName = data.getStringExtra("name");
            String type = data.getStringExtra("type");
            Log.i("aaaaaa", "onActivityResult: " + "  "+  TextLongEnd.TextLongSL( fileName, nameFile) );
            int a =TextLongEnd.TextLongSL( fileName, nameFile);
            nameFile.setText(fileName.length()> a ? fileName.substring(0, a / 2 + 1) + "..." + fileName.substring(fileName.length() - a / 2,fileName.length()) : fileName);
            sendFile.setVisibility(View.GONE);
            deleteFile.setVisibility(View.VISIBLE);

        }else if (resultCode == 1){

        }
    }
}
 