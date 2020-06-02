package com.example.hisland.AppActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.hisland.Adapter.SectionsPageAdapter;
import com.example.hisland.Fragment.Fragmentcontacts;
import com.example.hisland.Fragment.Fragmentisland;
import com.example.hisland.Fragment.Fragmentmessage;
import com.example.hisland.Fragment.Fragmentmine;
import com.example.hisland.R;
import com.example.hisland.Service.UserinfoService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//import java.sql.SQLException;

public class Main extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener, ViewPager.OnPageChangeListener, PopupMenu.OnMenuItemClickListener, View.OnClickListener {
    private ViewPager viewPager;
    private BottomNavigationBar bottomNavigationBar;
    private List<Fragment> fragments;
    private ImageButton imagebutton;
    private TextView title;
    CircleImageView CR;
    ImageView hHead;
    String name="";
    UserinfoService  userinfoService = UserinfoService.getUserinfoService();
    private static boolean isExit = false;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        viewPager = findViewById(R.id.viewpager);
        bottomNavigationBar = findViewById(R.id.bottom_main);
        imagebutton = findViewById(R.id.more);
        title = findViewById(R.id.title_text);
        Intent getintent = getIntent();
        name = getintent.getStringExtra("name");
        title.setText(name);
        imagebutton.setOnClickListener(this);
        initView();

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    public void initView() {
        initViewpage();
        initBottomMain();
    }

    //内容显示界面
    public void initViewpage() {
        viewPager.setOffscreenPageLimit(4);
        fragments = new ArrayList<Fragment>();
        fragments.add(new Fragmentmessage());
        fragments.add(new Fragmentcontacts());
        fragments.add(new Fragmentisland());
        fragments.add(new Fragmentmine());

        viewPager.setAdapter(new SectionsPageAdapter(getSupportFragmentManager(), fragments));
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(0);


    }

    //配置底部导航栏
    public void initBottomMain() {
        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar.clearAll();
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);//自适应大小
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT);
        bottomNavigationBar.setBarBackgroundColor(R.color.huibai).setActiveColor(R.color.colorbase).setInActiveColor(R.color.black);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.message, "消息").setInactiveIconResource(R.drawable.message))
                .addItem(new BottomNavigationItem(R.drawable.contact, "好友").setInactiveIconResource(R.drawable.contact))
                .addItem(new BottomNavigationItem(R.drawable.space, "动态").setInactiveIconResource(R.drawable.space))
                .addItem(new BottomNavigationItem(R.drawable.mine, "我的").setInactiveIconResource(R.drawable.mine))
                .setFirstSelectedPosition(0).initialise();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override//让图片和文字一起显示的方法
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override//逻辑处理
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.goto_island:
//                Toast.makeText(this, "goto_island", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.add_friend:
//                Toast.makeText(this, "add_friend", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.match_friend:
//                Toast.makeText(this, "match_friend", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.creat_room:
//                Toast.makeText(this, "creat_room", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.somethingelse:
//                Toast.makeText(this, "somethingelse", Toast.LENGTH_SHORT).show();
//                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onTabSelected(int position) {
        viewPager.setCurrentItem(position);

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bottomNavigationBar.selectTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override//弹出菜单的事件监听处理
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.goto_island:
                Toast.makeText(this, "goto_island", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add_friend:
                Intent intent = new Intent(this, AddFriend.class);
                intent.putExtra("name", title.getText().toString());
                startActivity(intent);
                break;
            case R.id.match_friend:
                Toast.makeText(this, "match_friend", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(this, Match.class);
                intent1.putExtra("name", title.getText().toString());
                startActivity(intent1);
                break;
            case R.id.somethingelse:
                Intent intent2=new Intent(this,List_addfriend.class);
                intent2.putExtra("username",title.getText().toString());
                startActivity(intent2);
                break;
            default:
                break;
        }
        return false;
    }

    @Override//更多：  的事件监听处理
    public void onClick(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }
}
