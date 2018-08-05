package com.example.administrator.sendfile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static java.lang.String.valueOf;

public class FileChooseStartActivity extends AppCompatActivity implements View.OnClickListener, OnUploadListener {

    List<NameFile> nameFileListDOC;
    List<NameFile> nameFileList;
    ListView fileChoose;
    FileChooseAdapter adapter;
    boolean isFileC = true;
    Button getF, startGetFile, noGF;


    private String urlString = "";//服务器地址
    private int fileSizeMax = 5;//限制传输文件大小5m
    String filePath;//路径
    String fileName;//文件名
    String fileSize;//文件大小

    int oldProcess;
    int filePosition;
    Handler handler;
    Thread start;


    //测试
    int a = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_choose_start);
        initView();
        initData();

    }

    private void initView() {
        fileChoose = (ListView) findViewById(R.id.fileChoose);
        getF = (Button) findViewById(R.id.getFile);
        startGetFile = (Button) findViewById(R.id.startGetFile);
        noGF = (Button) findViewById(R.id.noGetFile);
        startGetFile.setOnClickListener(this);
        noGF.setOnClickListener(this);
    }

    private void initData() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 200){
                    nameFileList = invertOrderList(nameFileListDOC);
                    // 获得SD卡根目录路径 "/sdcard"
                    Log.i("aaaaaaaaa", "initData:  nameFileList.size()" +  nameFileList.size());
                    adapter = new FileChooseAdapter(nameFileList, FileChooseStartActivity.this);
                    View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.file_choose_footer, null);
                    TextView fileNum = (TextView) view.findViewById(R.id.fileNum);
                    fileNum.setText(nameFileList.size() + "个文件");
                    fileChoose.addFooterView(view, null, false);
                    fileChoose.setAdapter(adapter);
                }
                if (msg.arg1 >=100){
                    Intent intent = new Intent();
                    intent.putExtra("name", fileName);
                    intent.putExtra("type",nameFileList.get(filePosition).getType()+ "");
                    setResult(0, intent);
                    Toast.makeText(FileChooseStartActivity.this, "上传完成", Toast.LENGTH_SHORT).show();
                    finish();
                }
                if (msg.arg1 >0){
                    nameFileList.get(filePosition).setProcess(msg.arg1);
                    adapter.notifyDataSetChanged();
                }

                return false;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                File sdDir = Environment.getExternalStorageDirectory();
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().
                        equals(Environment.MEDIA_MOUNTED)) {
                    // 读取文件夹下文件
                    //保存读取的文件
                    nameFileListDOC = new ArrayList<>();
                    //按时间排序
                    ToSearchFiles(sdDir);
                    handler.sendEmptyMessage(200);
                }
            }
        }).start();
        fileChoose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isFileC){
                    a = 0;
                    filePosition = position;
                    for (NameFile nameFile : nameFileList){
                        nameFile.setFileChoose(false);
                        nameFile.setProcess(0);
                    }
                    nameFileList.get(position).setFileChoose(true);
                    adapter.notifyDataSetChanged();
                    startGetFile.setVisibility(View.VISIBLE);
                    getF.setVisibility(View.GONE);

                }
            }
        });


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startGetFile:

                upLoad();
                noGF.setVisibility(View.VISIBLE);
                startGetFile.setVisibility(View.GONE);
                isFileC = false;

                //测试。。。。。。。。。。。。。。。。。。。。。。。。。。。
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (a < 100){
                            Random random = new Random();
                            int b  = random.nextInt(30);
                            Message msg = handler.obtainMessage();

                            msg.arg1 = (int) a + b;
                            a = a + b;
                            handler.sendMessage(msg);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                }).start();


                break;
            case R.id.noGetFile:
                HttpUtilLoad.endFile();
                isFileC = true;
                noGF.setVisibility(View.GONE);
                startGetFile.setVisibility(View.VISIBLE);
                break;
        }

    }
    public List<NameFile> ToSearchFiles(File file) {
    /* 定义一个File文件数组，用来存放/sdcard目录下的文件或文件夹 */
        File[] the_Files = file.listFiles();
    /* 通过遍历所有文件和文件夹 */
        for (File tempF : the_Files) {
            if (tempF.isDirectory()) {
                ToSearchFiles(tempF);
            /* 如果是文件夹的话继续遍历搜索 */
            } else {
//                try {
                /* 是文件，进行比较，如果文件名称中包含文件类型关键字，则保存到list中 */
                     /* 在这里做文件类型的判断 */
                String name = tempF.getName();
                if (name.length()>3){
                    String substr=name.substring(name.length()-3,name.length());
                    NameFile nameFile;
                    switch (substr){
                        case "ocx":
                            nameFile = new NameFile();
                            nameFile.setName(name);
                            //getFileOrFilesSize();指定文件和指定单位
                            nameFile.setFileBig(FileSize.getAutoFileOrFilesSize(tempF));
                            nameFile.setFileTime(FileTime.getStandardTime(tempF));
                            nameFile.setType(1);
                            nameFile.setFileUri(tempF.getPath());
                            nameFile.setFileChoose(false);

                            nameFile.setProcess(0);
                            nameFileListDOC.add(nameFile);
                            break;
                        case "lsx":
                            nameFile = new NameFile();
                            nameFile.setName(name);
                            nameFile.setFileBig(FileSize.getAutoFileOrFilesSize(tempF));
                            nameFile.setFileTime(FileTime.getStandardTime(tempF));
                            nameFile.setType(2);
                            nameFile.setFileUri(tempF.getPath());
                            nameFile.setFileChoose(false);
                            nameFile.setProcess(0);
                            nameFileListDOC.add(nameFile);
                            break;
                        case "ptx":
                            nameFile = new NameFile();
                            nameFile.setName(name);
                            nameFile.setFileBig(FileSize.getAutoFileOrFilesSize(tempF));
                            nameFile.setFileTime(FileTime.getStandardTime(tempF));
                            nameFile.setType(3);
                            nameFile.setFileUri(tempF.getPath());
                            nameFile.setFileChoose(false);
                            nameFile.setProcess(0);
                            nameFileListDOC.add(nameFile);
                            break;
                        case "pdf":
                            nameFile = new NameFile();
                            nameFile.setName(name);
                            nameFile.setFileBig(FileSize.getAutoFileOrFilesSize(tempF));
                            nameFile.setFileTime(FileTime.getStandardTime(tempF));
                            nameFile.setType(4);
                            nameFile.setFileUri(tempF.getPath());
                            nameFile.setFileChoose(false);
                            nameFile.setProcess(0);
                            nameFileListDOC.add(nameFile);
                            break;
                        case "rar":
                            nameFile = new NameFile();
                            nameFile.setName(name);
                            nameFile.setFileBig(FileSize.getAutoFileOrFilesSize(tempF));
                            nameFile.setFileTime(FileTime.getStandardTime(tempF));
                            nameFile.setType(5);
                            nameFile.setFileUri(tempF.getPath());
                            nameFile.setFileChoose(false);
                            nameFile.setProcess(0);
                            nameFileListDOC.add(nameFile);
                            break;
                        case ".7z":
                            nameFile = new NameFile();
                            nameFile.setName(name);
                            nameFile.setFileBig(FileSize.getAutoFileOrFilesSize(tempF));
                            nameFile.setFileTime(FileTime.getStandardTime(tempF));
                            nameFile.setType(6);
                            nameFile.setFileUri(tempF.getPath());
                            nameFile.setFileChoose(false);
                            nameFile.setProcess(0);
                            nameFileListDOC.add(nameFile);
                            break;
                    }
                }

//                } catch (Exception e) {
//                /* 如果路径找不到，提示出错 */
//                    Toast.makeText(this, "搜索失败", Toast.LENGTH_SHORT).show();
//                }
            }
        }
        return nameFileListDOC;
    }

    public void upLoad(){
        NameFile nameFile = nameFileList.get(filePosition);
        filePath = nameFile.getFileUri();//路径
        fileName = nameFile.getName();//名字
        fileSize = nameFile.getFileBig();
        Log.i("aaaaaa", "run: " + "路径：" + filePath +  "名字："+ fileName + "文件：" + fileSize);
        long fs = FileSize.getFileOrFilesSize(filePath);
        Log.i("aaaaaa", "upLoad: " + fs);
        if (fs > 1024 * 1024 * fileSizeMax){
            Toast.makeText(this, "文件过大", Toast.LENGTH_SHORT).show();
        }else {
            start =  new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String response = HttpUtilLoad.sendFile(urlString, filePath,
                                fileName, fileSize, FileChooseStartActivity.this);
                        Log.i("response", "response" + response );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            start.start();
        }



    }
    @Override
    public void onUpload(double process) {
        process = process * 100;
        int currentProcess = (int) process;
        if (currentProcess > oldProcess){
            Message msg = handler.obtainMessage();
            msg.arg1 = (int) process;
            handler.sendMessage(msg);
        }
        oldProcess = currentProcess;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            setResult(1);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpUtilLoad.endFile();
    }

    static class DateUtil {
        public static Date stringToDate(String s) {
            ParsePosition parsePosition = new ParsePosition(0);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse(s, parsePosition);
            return date;
        }
    }

    private List<NameFile> invertOrderList(List<NameFile> nameFile) {
        Collections.sort(nameFile, new Comparator<NameFile>() {
            @Override
            public int compare(NameFile lhs, NameFile rhs) {
                Date date1 = DateUtil.stringToDate(lhs.getFileTime());
                Date date2 = DateUtil.stringToDate(rhs.getFileTime());
                if (date1.before(date2)) {
                    return 1;
                }
                return -1;
            }
        });
        return nameFileListDOC;
    }

}
