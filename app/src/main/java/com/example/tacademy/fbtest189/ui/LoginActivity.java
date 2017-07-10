package com.example.tacademy.fbtest189.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.tacademy.fbtest189.R;
import com.example.tacademy.fbtest189.uti.U;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/*
 *      FireBase를 이용한 인증 처리
 *      - 익명 인증
 *      - 이메일, 비번 인증
 *      - 익명인증의 이메일, 비번 계정으로 전환 처리
 *      - 추후
 *      - 전번 인증, sms 인증
 *      - 쇼셜 인증
 *      - 기타 국내 인증
 */
public class LoginActivity extends RootActivity {


    // 인증 관련 라이브러리 객체 획득
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 생성
        firebaseAuth = FirebaseAuth.getInstance();

        initUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 익명 로그인한 유저 객체 획득 => 로그아웃을 하지 않았다면 객체(세션) 획득 가능
        // FirebaseUser user  = getUser();
        if( getUser() != null){
            Toast.makeText(this, "Login is completed !!", Toast.LENGTH_LONG).show();
        }
    }

    public void initUI(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 익명 로그인을 수행해라
                if( getUser() != null){     //로그인 중인데 눌렀다.
                    firebaseAuth.signOut(); //로그아웃
                }else{                      //로그아웃 중인데 눌렀다.
                    anonymouslySignUp();    //계속해서 익명 생성
                }

                anonymouslySignUp();
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
    }
    public void anonymouslySignUp(){
        showPD();
        firebaseAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if( task.isSuccessful()){
                    //성공 정보 획득
                    FirebaseUser user = getUser();
                    if(user != null){
                        U.getInstance().log(user.getUid());
                        U.getInstance().log(user.getEmail();
                    }
                }else{

                }
                stopPD();
            }
        })
    }
}
