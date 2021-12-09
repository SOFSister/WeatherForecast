package com.example.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private SPUtils sp;
    private String account;
    private String password;
    private Button loginBtn;
    private EditText accountET;
    private EditText passwordET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp=new SPUtils(this,"weather");
        loginBtn=findViewById(R.id.login_btn);
        accountET=findViewById(R.id.account);
        passwordET=findViewById(R.id.password);
        //隐藏actionbar
        this.getSupportActionBar().hide();
        if(sp.getBoolean("isLogin")){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            return;
        }
        initAccount();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(accountET.getText().toString().equals(account)&&passwordET.getText().toString().equals(password)){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LoginActivity.this,"登陆失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void initAccount(){
        account=sp.getString("account");
        if(account==null){
            sp.putString("account","2019212212192");
            account="2019212212192";
            sp.putBoolean("isLogin",false);
            sp.putInt("maxItem",8);
        }
        password=sp.getString("password");
        if(password==null){
            sp.putString("password","123456");
            password="123456";
        }
    }
}