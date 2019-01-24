package com.zwu.rent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {

    private SharedPreferences preferences;

    private SharedPreferences.Editor editor;

    private EditText accountEdit;

    private EditText passwordEdit;

    private Button login;

    private Button registered;

    private CheckBox rememberPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        accountEdit = findViewById(R.id.account);
        passwordEdit = findViewById(R.id.password);
        rememberPass = findViewById(R.id.remember_pass);
        registered = findViewById(R.id.registered);
        login = findViewById(R.id.login);

        boolean isRemember = preferences.getBoolean("remember_password", false);
        if (isRemember){
            //  将账号和密码都设置到文本框中
            String account = preferences.getString("account", "");
            String password = preferences.getString("password", "");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                //如果账号是2016010599，密码是123456，就认为登录成功
                if (account.equals("2016010599") && password.equals("123456")){
                    editor = preferences.edit();
                    if (rememberPass.isChecked()){
                        //检查复选框是否被选中
                        editor.putBoolean("remember_password", true);
                        editor.putString("account", account);
                        editor.putString("password", password);
                    }
                    else{
                        editor.clear();
                    }
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.
                            class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "account or password is " +
                                    "invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
        registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegisteredIntent = new Intent(LoginActivity.this,
                        Registered.class);
                startActivity(RegisteredIntent);
            }
        });
    }
}
