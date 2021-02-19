package com.cube.sewingmachine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CreateActivity extends AppCompatActivity {
    Button back_btn, finish_btn, start_btn, stop_btn;
    TextView textview;
    AppCompatImageView progress_image;

    private static final int MY_REQUEST_WRITE_STORAGE = 5;
    private int currentColor;
    private Button colorButtons[];
    private int colors[];
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    int pa_index = 0;
    int stream_x, stream_y;

    public static Bluetooth bluetooth = null;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    int [] times = {5000, 10000, 15000, 20000};
//    Runnable fill_progress;
    int [] progress_images = {R.drawable.progress_img0,R.drawable.progress_img1,R.drawable.progress_img2,R.drawable.progress_img3,R.drawable.progress_img4,R.drawable.progress_img5,
    R.drawable.progress_img6, R.drawable.progress_img7, R.drawable.progress_img8, R.drawable.progress_img9, R.drawable.progress_img10};

    int progress_num =0;
    ArrayList<String> pixel_array;
    ArrayList<Integer> color_array;

    int count =0;

    String [] number, number2;
    MyHandler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        if(bluetooth == null)bluetooth = LoginActivity.bluetooth;

        pixel_array = new ArrayList<>();
        color_array = new ArrayList<>();

        handler = new MyHandler();

        back_btn = findViewById(R.id.back_btn);
        finish_btn = findViewById(R.id.finish_btn);
        start_btn = findViewById(R.id.start_btn);
        stop_btn = findViewById(R.id.stop_btn);

        textview = findViewById(R.id.textview);
        progress_image = findViewById(R.id.progress_image);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = pixel_array.get(count).split("-");
                textview.setText((Integer.parseInt(number[0])+1) + "-" +(Integer.parseInt(number[1])+1) + " 진행중 ...");
                progressSequence();
                start_btn.setBackgroundResource(R.drawable.embroid_start_off_btn);
                start_btn.setEnabled(false);
                stop_btn.setBackgroundResource(R.drawable.embroid_stop_btn);
            }
        });

        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        start_btn.setBackgroundResource(R.drawable.embroid_start_btn);
                        start_btn.setEnabled(true);
                        stop_btn.setBackgroundResource(R.drawable.embroid_stop_off_btn);
                    }
                });
            }
        });


        initPixels();

        fillScreen(ContextCompat.getColor(CreateActivity.this, R.color.erase));

        pixelGrid(true);

        settings = getSharedPreferences("localData", 0);
        editor = settings.edit();
        Intent intent = getIntent();

        pa_index = intent.getIntExtra("pa_index", 0);
        openFile(pa_index + ".pixel_artist", false);

        //파일 블러오기 코드
        File imageFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), getString(R.string.app_name));

        File openFile = new File(imageFolder, pa_index + ".jpg");

        // 비트맵 데이터 전처리
        Bitmap tempBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
        tempBitmap.eraseColor(0xFFBDBDBD);

        Bitmap bMap;

        if(openFile.exists()) {
            Uri uri = Uri.fromFile(openFile);
            try {
                bMap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                bMap = tempBitmap;
                e.printStackTrace();
            }
        } else {
            bMap = tempBitmap;
        }

        ImageView mini_map = findViewById(R.id.mini_map);
        mini_map.setImageBitmap(bMap);

        readyStreamFile(pa_index + ".pixel_artist");
    }

    // TODO : 불러오기 함수
    public void openFile(String fileName, boolean showToast) {
        File imageFolder = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File openFile = new File(imageFolder, fileName);

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(openFile));
            int color;
            String value;

            int x, y;

            if ((value = bufferedReader.readLine()) != null) {
                x = Integer.valueOf(value);
            } else {
                throw new IOException();
            }

            if ((value = bufferedReader.readLine()) != null) {
                y = Integer.valueOf(value);
            } else {
                throw new IOException();
            }

            LinearLayout linearLayout = findViewById(R.id.paper_linear_layout);

            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {

                    if ((value = bufferedReader.readLine()) != null) {
                        color = Integer.valueOf(value);
                    } else {
                        throw new IOException();
                    }

                    View v = ((LinearLayout) linearLayout.getChildAt(i)).getChildAt(j);
                    v.setBackgroundColor(color);
                    if (color != getResources().getColor(R.color.erase)) {
                        pixel_array.add(i+"-"+j);
                        color_array.add(color);

                    }

                }
            }

            for (int i=0; i<pixel_array.size(); i++){
                Log.e("pixel",pixel_array.get(i));
                Log.e("color",color_array.get(i)+"");
            }



        } catch (FileNotFoundException e) {
            Log.e("MainActivity.openFile", "File not found");
        } catch (IOException e) {
            Log.e("MainActivity.openFile", "Could not open file");
        }
    }

    // TODO : 미니맵 갱신 함수
    private void updateDrawerHeader() {
        View view = findViewById(R.id.paper_linear_layout);
        Bitmap bitmap;

        if (view.getWidth() > 0 && view.getHeight() > 0) {
            bitmap = Bitmap.createBitmap(view.getWidth(),
                    view.getHeight(), Bitmap.Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(200,
                    200, Bitmap.Config.RGB_565);
            bitmap.eraseColor(Color.WHITE);
        }

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        ImageView mini_map = findViewById(R.id.mini_map);
        mini_map.setImageBitmap(bitmap);
    }

    //Initializes the "pixels" (basically sets OnLongClickListener on them)
    private void initPixels() {
        LinearLayout paper = findViewById(R.id.paper_linear_layout);

        for (int i = 0; i < paper.getChildCount(); i++) {
            LinearLayout l = (LinearLayout) paper.getChildAt(i);

            for (int j = 0; j < l.getChildCount(); j++) {
                View pixel = l.getChildAt(j);
            }
        }
    }

    //Shows or hides the pixels boundaries from the paper_linear_layout
    private void pixelGrid(boolean onOff) {
        LinearLayout paper = findViewById(R.id.paper_linear_layout);

        int x;
        int y;

        if (!onOff) {
            x = 0;
            y = 0;
        } else {
            x = 5;
            y = 5;
        }

        for (int i = 0; i < paper.getChildCount(); i++) {
            LinearLayout l = (LinearLayout) paper.getChildAt(i);

            for (int j = 0; j < l.getChildCount(); j++) {
                View pixel = l.getChildAt(j);

                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) pixel.getLayoutParams();

                layoutParams.setMargins(x, y, 0, 0);
                pixel.setLayoutParams(layoutParams);
            }
        }
    }

    // TODO : 캔버스 색상리셋(초기화) 함수
    //Fills paper_linear_layout with chosen color
    private void fillScreen(int color) {
        LinearLayout paper = findViewById(R.id.paper_linear_layout);

        for (int i = 0; i < paper.getChildCount(); i++) {
            LinearLayout l = (LinearLayout) paper.getChildAt(i);

            for (int j = 0; j < l.getChildCount(); j++) {
                View pixel = l.getChildAt(j);

                pixel.setBackgroundColor(color);
            }
        }
    }

    // TODO : 캔버스 디테일 색칠 함수
    private void fillScreen_detail(int color, int i, int j) {
        LinearLayout paper = findViewById(R.id.paper_linear_layout);
            LinearLayout l = (LinearLayout) paper.getChildAt(i);
            View pixel = l.getChildAt(j);
            pixel.setBackgroundColor(color);


    }

    //Onclick method that changes the color of a single "pixel"
    // TODO : 픽셀 선택 했을때 온-클릭
    public void changeColor(View v) {

    }

    // TODO : pa 파일 스트림 함수
    public void readyStreamFile(String fileName) {
        File imageFolder = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File openFile = new File(imageFolder, fileName);

        boolean flag = true;


        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(openFile));
            int color;
            String value;

            int x, y;

            if ((value = bufferedReader.readLine()) != null) {
                x = Integer.valueOf(value);
            } else {
                throw new IOException();
            }

            if ((value = bufferedReader.readLine()) != null) {
                y = Integer.valueOf(value);
            } else {
                throw new IOException();
            }



            LinearLayout linearLayout = findViewById(R.id.paper_linear_layout);

            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    if ((value = bufferedReader.readLine()) != null) {
                        color = Integer.valueOf(value);
                    } else {
                        throw new IOException();
                    }

                    if (color == getResources().getColor(R.color.erase)) {
                    } else {

                        if (flag) {
                            stream_x = x;
                            stream_y = y;



                            View v = ((LinearLayout) linearLayout.getChildAt(i)).getChildAt(j);
                            v.setBackgroundColor(getResources().getColor(R.color.target));
                            textview.setText( (i + 1) + "-" + (j + 1) + " 준비중 ..." );

                            flag = false;
                        }
                    }
                }
            }

        } catch (FileNotFoundException e) {
            Log.e("MainActivity.openFile", "File not found");
        } catch (IOException e) {
            Log.e("MainActivity.openFile", "Could not open file");
        }
    }




    // TODO : '자수 시작하기' 버튼 눌렀을때 준비하는 함수
    public void readySequence() {

    }

    // TODO : 준비 중 이후 10회 반복하는 함수
    public void progressSequence() {
            Log.e("data","A");
            send_data("a");
            handler.sendEmptyMessageDelayed(3,2000);
    }

    // TODO : 10회 반복이 완료되면 마무리하는 함수
    public void endSequence() {
        // TODO : 마무리 리스트
        // 1. 딜레이
        // 2. 타겟 컬러를 원래 컬러로 바꾸기
        // 3. i, j 좌표 증가시키고 타겟 컬러로 바꾸기
        // 4. 13, 13 좌표인지 체크하고, 루프 종료
    }

    public void send_data(String str){
        byte[] buffer = null;
        Log.e("str",str+"");
        buffer = str.getBytes();
        bluetooth.write(buffer);
        Log.e("getState",bluetooth.getState()+"");
    }


    class MyHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 0:
                    Log.e("?","gg");
                    progress_image.setBackgroundResource(progress_images[progress_num]);

                    if (progress_num == 10) {
                        Log.e("??","들어옴");
                        textview.setText((Integer.parseInt(number[0])+1) + "-" +(Integer.parseInt(number[1])+1) + " 완료!" );
                        sendEmptyMessageDelayed(1,2000);

                        return ;
                    }else{
                        sendEmptyMessageDelayed(2,7000);
                    }

                    break;

                case 1:

                    start_btn.setBackgroundResource(R.drawable.embroid_start_btn);
                    start_btn.setEnabled(true);
                    stop_btn.setBackgroundResource(R.drawable.embroid_stop_off_btn);

                    //progress image 초기화
                    progress_num = 0;
                    progress_image.setBackgroundResource(R.drawable.progress_img0);

                    //pixel 다음 단계로
                    fillScreen_detail(color_array.get(count),Integer.parseInt(number[0]),Integer.parseInt(number[1]));

                    count++;
                    number2 = pixel_array.get(count).split("-");
                    fillScreen_detail(getResources().getColor(R.color.target),Integer.parseInt(number2[0]),Integer.parseInt(number2[1]));
                    textview.setText((Integer.parseInt(number2[0])+1) + "-" +(Integer.parseInt(number2[1])+1) + " 준비중 ..." );

                    break;

                case 2:
                    progressSequence();
                    break;


                case 3:
                    Log.e("들어옴","ㅎㅎ");
                    send_data("f");
                    progress_num += 1;
                    sendEmptyMessageDelayed(0,0);
                    break;

                case 4:
                    removeMessages(0);
                    removeMessages(1);
                    removeMessages(2);
                    Log.e("msg","remove");
            }
        }
    }



}
