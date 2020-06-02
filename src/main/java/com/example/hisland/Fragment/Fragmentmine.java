package com.example.hisland.Fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.hisland.AppActivity.ChangeSignature;
import com.example.hisland.AppActivity.HelpWd;
import com.example.hisland.AppActivity.MySettings;
import com.example.hisland.AppActivity.My_Community;
import com.example.hisland.AppActivity.My_Shoucang;
import com.example.hisland.AppActivity.UpdateMessage;
import com.example.hisland.R;
import com.example.hisland.Service.UserinfoService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.SQLException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class Fragmentmine extends Fragment {
    UserinfoService userinfoService = UserinfoService.getUserinfoService();
    ImageView hHead, hBack, change;
    LinearLayout shezhi, ziliao;
    TextView signature, quanju_user_name ;
    LinearLayout my_community,my_shoucang;
    CircleImageView CR;
    Button help;
    public static final int CHOOSE_PHOTO = 2;
    String username,qianming1;
    int i=1,flag=-1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_mine, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hHead = getActivity().findViewById(R.id.h_head);
        hBack = getActivity().findViewById(R.id.h_back);
        change = getActivity().findViewById(R.id.change);
        shezhi = getActivity().findViewById(R.id.shezhi);
        ziliao = getActivity().findViewById(R.id.ziliao);
        CR = getActivity().findViewById(R.id.circleimg);
        help = getActivity().findViewById(R.id.help);
        signature = getActivity().findViewById(R.id.qianming);
        quanju_user_name = getActivity().findViewById(R.id.quanju_user_name);
        my_community = getActivity().findViewById(R.id.my_community);
        Intent getintent = getActivity().getIntent();
        username = getintent.getStringExtra("name");
        String signaturefirst = getintent.getStringExtra("signaturefirst");
        quanju_user_name.setText(username);
        signature.setText(signaturefirst);
        CR = (CircleImageView)getActivity().findViewById(R.id.circleimg);
        my_shoucang = getActivity().findViewById(R.id.my_shoucang);
        hHead = (ImageView)getActivity().findViewById(R.id.h_head);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Blob pic = null;
                pic = userinfoService.getBlob(username);
                if(pic!=null){
                    flag=1;
                InputStream in = null;
                try {
                    i++;
                    in = pic.getBinaryStream();
                    OutputStream out = null;
                    Log.d("iiiiiiii", "run: "+i+flag);
                    File f = new File("/sdcard/pic"+i+".png");
                    out = new FileOutputStream(f);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while((len = in.read(buffer)) != -1){
                        out.write(buffer, 0, len);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }}
        }).start();
            Log.d("5555555", "run: "+i+flag);
//        if(flag==1){
            File f = new File("/sdcard/pic"+i+".png");
            Bitmap bitmap = BitmapFactory.decodeFile(f.getPath());
            if(bitmap==null){
             Log.d("图片", "onCreate: "+"null"+flag);
            }else {
                CR.setImageBitmap(bitmap);
                hHead.setImageBitmap(bitmap);

        }
        my_shoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), My_Shoucang.class);
                intent.putExtra("name", quanju_user_name.getText().toString());
                startActivity(intent);
            }
        });

        //进行逻辑的处理
        hHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    Uri uri = intent.getData();
                    Bitmap bitmap = null;
                    if (uri != null||intent!=null) {
                        try {
                            //获取图片
                            ContentResolver cr = getContext().getContentResolver();
                            bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                            }catch (FileNotFoundException e) {
                            e.printStackTrace();
                            } catch (IOException e) {
                            e.printStackTrace();
                        }
                        startActivityForResult(intent, 1);

                         }
                    }
            }
        });
        ziliao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateMessage.class);
                intent.putExtra("name", quanju_user_name.getText().toString());
                startActivity(intent);
                //Toast.makeText(getActivity(), quanju_user_name.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        shezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MySettings.class);
                intent.putExtra("name", quanju_user_name.getText().toString());
                startActivity(intent);
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangeSignature.class);
                intent.putExtra("name", quanju_user_name.getText().toString());
                startActivityForResult(intent, 1);
            }
        });
        my_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), My_Community.class);
                intent.putExtra("name", quanju_user_name.getText().toString());
                startActivity(intent);
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent helpwd = new Intent(getActivity(), HelpWd.class);
                helpwd.putExtra("url","http://xxxxxx");
                startActivity(helpwd);
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    //String user_quanju = data.getStringExtra("user_quanju");
                    String qianming = data.getStringExtra("signature");
                    if(qianming!=null) {
                        quanju_user_name.setText(username);
                        signature.setText(qianming);
                    }else{
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                qianming1=userinfoService.select_signature(username);
                                Handler main=new Handler(Looper.getMainLooper());
                                main.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        quanju_user_name.setText(username);
                                        signature.setText(qianming1);
                                    }
                                });
                            }
                        }).start();
                    }
//                }
//                break;
//            case 2:
//                if (requestCode == RESULT_OK) {
//                    //判断手机系统版本号
//                    if (Build.VERSION.SDK_INT >= 19) {
//                        handleImageOnKitKat(data);
//                    } else {
//                        handleImageBeforeKitKat(data);
//                    }
                    Uri uri = data.getData();
                    if (uri != null) {
                        ContentResolver cr = getContext().getContentResolver();
                        try {
                            //获取图片
                            Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
//                            Log.d("shujuku", "onClick: "+uri);
                            String path = getPath(getContext(),uri);
//                            Log.d("path", "onActivityResult: "+path);

                            File f = new File(path);
                            InputStream in = new FileInputStream(f);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    userinfoService.updateHeadpic(username,in);}}).start();
                            hHead.setImageBitmap(bitmap);
                            CR.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            Log.e("Exception", e.getMessage(), e);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                }
                break;
            default:
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);//打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(getContext(), "你拒绝了权限申请！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

//    @TargetApi(19)
//    private void handleImageOnKitKat(Intent data) {
//        String imagePath = null;
//        Uri uri = data.getData();
//        if (DocumentsContract.isDocumentUri(getContext(), uri)) {
//            //如果是document类型的uri，则通过document id 处理
//            String docId = DocumentsContract.getDocumentId(uri);
//            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
//                String id = docId.split(":")[1];//解析出数字格式的id
//                String selection = MediaStore.Images.Media._ID + "=" + id;
//                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
//            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
//                imagePath = getImagePath(contentUri, null);
//            }
//        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            //如果是content类型的uri，则使用普通的方式处理
//            imagePath = getImagePath(uri, null);
//        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            //如果是file类型的uri,直接获取图片路径即可
//            imagePath = uri.getPath();
//        }
//        displayImage(imagePath);//根据图片路径显示图片
//    }

//    private void handleImageBeforeKitKat(Intent data) {
//        Uri uri = data.getData();
//        String imagePath = getImagePath(uri, null);
//        displayImage(imagePath);
//    }

//    private String getImagePath(Uri uri, String selection) {
//        String path = null;
//        //通过uri和selection来获取真实的图片路径
//        Cursor cursor = getContext().getContentResolver().query(uri, null, selection, null, null);
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            }
//            cursor.close();
//        }
//        return path;
//    }

//    private void displayImage(String imagePath) {
//        if (imagePath != null) {
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            hHead.setImageBitmap(bitmap);
//        } else {
//            Toast.makeText(getContext(), "获取图片失败", Toast.LENGTH_SHORT).show();
//        }
//    }
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
//                Log.i(TAG,"isExternalStorageDocument***"+uri.toString());
//                Log.i(TAG,"docId***"+docId);
//                以下是打印示例：
//                isExternalStorageDocument***content://com.android.externalstorage.documents/document/primary%3ATset%2FROC2018421103253.wav
//                docId***primary:Test/ROC2018421103253.wav
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
//                Log.i(TAG,"isDownloadsDocument***"+uri.toString());
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
//                Log.i(TAG,"isMediaDocument***"+uri.toString());
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            Log.i(TAG,"content***"+uri.toString());
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            Log.i(TAG,"file***"+uri.toString());
            return uri.getPath();
        }
        return null;
    }
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = getContext().getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }




}
