package com.example.xq.action;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.jar.Manifest;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class First_Activity extends AppCompatActivity {
    private String[] ele = new String[100];
    private Uri imageUri;
    private ImageView picture;
    private ArrayList<String> mm = new ArrayList<>();
    private Workbook book;
    private int i = 0;
    private int numOfSheet;
    private String filePath=null;
    private int numOfCamera = 0;
    private String id = null;
    private String page = null;
    private File outpuImage = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_);
        Button button = (Button) findViewById(R.id.b01);
        Button button1 = (Button) findViewById(R.id.b02);
        button1.setText("切换表格");
        button.setText("选择文件");

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mm.removeAll(mm);
                if(i<numOfSheet)
                    i+=1;
                else
                    i = 0;
                if(filePath==null)
                  Toast.makeText(First_Activity.this,"请先选择文件",Toast.LENGTH_SHORT).show();
                else
                {
                 getExcel(i);
                 setAdap();
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(First_Activity.this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(First_Activity.this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA},1);
                }
                else
                {
                    readFile();
                }

            }
        });

    }
    private void readFile()
    {
        Intent intent = new Intent();
        intent.setType("*/*"); //选择任意文件
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,1);
    }
    private void readExcel()
    {   numOfCamera += 1;
        if(numOfCamera<=2)
        {   if(numOfCamera==1)
              page = "A";
            else
              page = "B";
            outpuImage = new File(getExternalCacheDir(),page+id+".jpg");
            try
            {
                if(outpuImage.exists())
                    outpuImage.delete();
                outpuImage.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        imageUri = Uri.fromFile(outpuImage);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);  //存储到指定的位置
       // this.sendBroadcast(intent);
        startActivityForResult(intent,2);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 1:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    readFile();
                }
                else
                {
                 Toast.makeText(this,"you refuse",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void getExcel(int num)
    {
        int startRow = 0;
        int colOfSch = 0;
        int colOfName = 0;
        int colOfId = 0;
       // System.out.print(filePath);
        try
        {
            InputStream is = new FileInputStream(filePath);
            book = Workbook.getWorkbook(is);
            Sheet sheet = book.getSheet(num);
            numOfSheet = book.getSheets().length;
            int row = sheet.getRows();
            int col = sheet.getColumns();
            for(int i=0;i<Integer.MAX_VALUE;i++)
            {
                Cell cell = sheet.getCell(0,i);  //第一个是列，第二个是行
                String result = cell.getContents();
                if(result.equals("序号"))
                {
                    startRow = i;
                    break;
                }
            }
            for(int j=0;j<col;j++)
            {
                Cell cell = sheet.getCell(j,startRow);  //第一个是列，第二个是行
                String result = cell.getContents();
                if(result.equals("学校"))
                {
                    colOfSch = j;
                }
                if(result.equals("姓名"))
                {
                    colOfName = j;
                }
                if(result.toLowerCase().equalsIgnoreCase("教育id"))
                {
                    colOfId = j;
                }
            }
            for(int i=startRow+1;i<row;i++)
            {
                //ele[i-startRow-1] = sheet.getCell(colOfSch,i).getContents()+"  "+sheet.getCell(colOfName,i).getContents()+"  "+sheet.getCell(colOfId,i).getContents();
                mm.add(sheet.getCell(colOfSch,i).getContents()+"  "+sheet.getCell(colOfName,i).getContents()+"  "+sheet.getCell(colOfId,i).getContents());
            }
        }
        catch (Exception e)
        {
            Log.e("erros",e.getMessage());
        }
       finally {
            book.close();
        }


    }

    private void setAdap()
    {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list2); //get recycleview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recycleview adap = new recycleview(mm); //适配器
        adap.setOnRecycleViewListener(new recycleview.OnRecycleViewListener()
                                      {
                                          @Override
                                          public void onItemClick(View view, int position) {
                                              String res = (String) ((TextView) view.findViewById(R.id.ele)).getText();
                                             // if(view)
//                                              lastRes = res;
//
//                                              if(((String) ((TextView) view.findViewById(R.id.ele)).getText()).equals(lastRes))
//                                              {
//                                                  ((TextView) view.findViewById(R.id.ele)).setBackgroundColor(Color.parseColor("#90EE90"));
//                                              }
//                                              else
//                                              {
//                                                  ((TextView) view.findViewById(R.id.ele)).setBackgroundColor(Color.parseColor("#FFFFFF"));
//                                                 // view.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                                              }
//                                              if(!res.equals(lastRes))
//                                              {
//                                                  if(!lastRes.equals(res))
//                                                      view.setBackgroundColor(Color.parseColor("#FFFFFF"));
//
//                                                     view.setBackgroundColor(Color.parseColor("#4EEE94"));
//                                                     lastRes = res;
//                                              }
//                                              else
//                                                  view.setBackgroundColor(Color.parseColor("#FFFFFF"));

                                              id = res.split("  ")[2];
                                              Toast.makeText(First_Activity.this,res,Toast.LENGTH_SHORT).show();
//                                              File outpuImage = new File(getExternalCacheDir(),id+".jpg");
//                                              try
//                                              {
//                                                  if(outpuImage.exists())
//                                                      outpuImage.delete();
//                                                  outpuImage.createNewFile();
//                                              }
//                                              catch (IOException e)
//                                              {
//                                                  e.printStackTrace();
//                                              }
//                                              imageUri = Uri.fromFile(outpuImage);
                                              numOfCamera = 0;
                                              readExcel();
                                          }
                                      }
        );
        recyclerView.setAdapter(adap);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        int startRow = 0;
//        int colOfSch = 0;
//        int colOfName = 0;
//        int colOfId = 0;
        if(requestCode==1)
        {
        if(resultCode==RESULT_OK)
        {
            Uri uri = data.getData(); //得到文件路径
            Log.d("uripath", uri.getPath());
            filePath = Tst.getPath(this,uri);
            try
            {
               // InputStream is = new FileInputStream(filePath);
              //  book = Workbook.getWorkbook(is);
//                Sheet sheet = book.getSheet(0);
//                int row = sheet.getRows();
//                int col = sheet.getColumns();
//                for(int i=0;i<Integer.MAX_VALUE;i++)
//                {
//                    Cell cell = sheet.getCell(0,i);  //第一个是列，第二个是行
//                    String result = cell.getContents();
//                    if(result.equals("序号"))
//                    {
//                        startRow = i;
//                        break;
//                    }
//                }
//                for(int j=0;j<col;j++)
//                {
//                    Cell cell = sheet.getCell(j,startRow);  //第一个是列，第二个是行
//                    String result = cell.getContents();
//                    if(result.equals("学校"))
//                    {
//                        colOfSch = j;
//                    }
//                    if(result.equals("姓名"))
//                    {
//                        colOfName = j;
//                    }
//                    if(result.toLowerCase().equalsIgnoreCase("教育id"))
//                    {
//                        colOfId = j;
//                    }
//                }
//                for(int i=startRow+1;i<row;i++)
//                {
//                    //ele[i-startRow-1] = sheet.getCell(colOfSch,i).getContents()+"  "+sheet.getCell(colOfName,i).getContents()+"  "+sheet.getCell(colOfId,i).getContents();
//                    mm.add(sheet.getCell(colOfSch,i).getContents()+"  "+sheet.getCell(colOfName,i).getContents()+"  "+sheet.getCell(colOfId,i).getContents());
//
//                }
                getExcel(0);
                setAdap();
//                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list2); //get recycleview
//                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//                recyclerView.setLayoutManager(layoutManager);
//                recycleview adap = new recycleview(mm); //适配器
//                adap.setOnRecycleViewListener(new recycleview.OnRecycleViewListener()
//                                              {
//                                                  @Override
//                                                  public void onItemClick(View view, int position) {
//                                                   String res = (String) ((TextView) view.findViewById(R.id.ele)).getText();
//                                                   String id = res.split("  ")[2];
//                                                   Toast.makeText(First_Activity.this,res,Toast.LENGTH_SHORT).show();
//                                                   File outpuImage = new File(getExternalCacheDir(),id+".jpg");
//                        try
//                        {
//                            if(outpuImage.exists())
//                                outpuImage.delete();
//                            outpuImage.createNewFile();
//                        }
//                        catch (IOException e)
//                        {
//                            e.printStackTrace();
//                        }
//                        imageUri = Uri.fromFile(outpuImage);
//                        readExcel();
//                                                  }
//                                              }
//                );
//                recyclerView.setAdapter(adap);

//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(First_Activity.this,android.R.layout.simple_list_item_1,ele);
//                ListView listView = (ListView) findViewById(R.id.list1);
//                listView.setAdapter(adapter);
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        String getEle = ele[position];    //list view中的监听事件
//                        File outpuImage = new File(getExternalCacheDir(),"1.jpg");
//                        try
//                        {
//                            if(outpuImage.exists())
//                                outpuImage.delete();
//                            outpuImage.createNewFile();
//                        }
//                        catch (IOException e)
//                        {
//                            e.printStackTrace();
//                        }
//                        imageUri = Uri.fromFile(outpuImage);
//                        readExcel();
////                        if(ContextCompat.checkSelfPermission(First_Activity.this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
////                        {
////                            ActivityCompat.requestPermissions(First_Activity.this,new String[]{android.Manifest.permission.CAMERA},2);
////                        }
////                        else
////                        {
////                            readExcel();
////                        }
//                    }
//                });


            }
            catch (Exception e)
            {
                Log.e("uri", e.toString());
            }
        }
        }
        if(requestCode==2)   //相机活动销毁之前调用的函数
        {


           //  System.out.print("");
             Uri uri1 = Uri.fromFile(outpuImage);
             Intent intent1 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
             intent1.setData(uri1);
             sendBroadcast(intent1);
             readExcel();
             Toast.makeText(First_Activity.this,"拍照完成",Toast.LENGTH_SHORT).show();  //在这判断拍照次数
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
