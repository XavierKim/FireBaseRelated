package com.example.tacademy.fbtest189.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tacademy.fbtest189.R;
import com.example.tacademy.fbtest189.uti.U;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
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
    EditText uMail, uPassword;
    String email, password;
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
        //버튼 이벤트 : 익명 계정과 이메일 비번을 연결
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAnLinkEmail();

            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEmailSignUp();

            }
        });
        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
            }
        });
        uMail = (EditText)findViewById(R.id.email);
        uPassword = (EditText)findViewById(R.id.password);

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
                        U.getInstance().log(user.getEmail());
                    }
                    Toast.makeText(LoginActivity.this, "익명 계정 생성 성공", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(LoginActivity.this, "익명 계정 생성 실패", Toast.LENGTH_LONG).show();
                }
                stopPD();
            }
        });
    }
    // 익명 계정으로부터 이메일 전환 관련
    // 1. 이메일, 비번 입력
    // 2. 유효성 감사(TextUtil.isEmpty()) EditText.error()
    // 3. FB Auth 중 ?메소드를 통해 처리 -> 비동기 ->
    public void onAnLinkEmail(){
        if(!isValid()) return;
        // 익명 계정과 이메일 비번을 연결하는 구간
        logInClick();
        getUser().linkWithCredential(EmailAuthProvider.getCredential(email,password))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // 연결이 성공, 허가된 서비스로 이동
                            U.getInstance().log("연결완료");
                            Toast.makeText(LoginActivity.this, "이메일 연결 성공", Toast.LENGTH_LONG).show();
                        }else{
                            // 실패 사유 보이기
                            Toast.makeText(LoginActivity.this, "이메일 연결 실패", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // 이메일 비번으로부터 가입 및 로그인
    // 1. 이메일, 비번 입력
    // 2. 유효성 검사(TextUtil.isEmpty()) EditText.error()
    // 3. FB Auth 중 ?메소드를 통해 처리 -> 비동기 ->
    // 이메일 비번으로부터 가입 및 로그인
    public void onEmailSignUp(){
        if( !isValid() ) return;
        // 1. 로그아웃(로그인시에만 수행)
        // 2. 이메일, 비번 입력 후 로그인 버트 하나 추가하여
        // 3. 클릭시 아래 함수를 호출하여 로그인 되게 구현
        logInClick();
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if( task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "이메일 로그인 성공",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(LoginActivity.this, "이메일 로그인 실패",Toast.LENGTH_LONG).show();
                        }
                        stopPD();
                    }
                });
    }

    public boolean isValid(){
        logInClick();
        if(TextUtils.isEmpty(email)){
            this.uMail.setError("이메일을 입력하세요");
            return false;
        }
        if(TextUtils.isEmpty(password)){
            this.uPassword.setError("비밀번호를 입력하세요");
            return false;
        }
        return true;
    }

    public void logInClick(){                           //email, password 추출 코드 메소드
        email = this.uMail.getText().toString();
        password = this.uPassword.getText().toString();
    }

    // 참고
    // 이메일 비번으로 회원가입
    // firebaseAuth.createUserWithEmailAndPassword();
    // 이메일 검증
    // getUser().sendEmailVerification();
    // 비번 초기화
    // firebaseAuth.sendPasswordResetEmail();
}
