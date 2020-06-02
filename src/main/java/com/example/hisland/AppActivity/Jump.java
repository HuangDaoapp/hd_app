package com.example.hisland.AppActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.hisland.R;

public class Jump extends Activity { //过渡到unity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump);
        Intent intent = new Intent(Jump.this, UnityPlayerActivity.class);
        startActivity(intent);
    }

}
