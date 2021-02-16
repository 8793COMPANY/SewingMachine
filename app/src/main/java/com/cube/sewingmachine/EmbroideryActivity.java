package com.cube.sewingmachine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static com.cube.sewingmachine.PixelAdapter.*;

public class EmbroideryActivity extends AppCompatActivity {
    Context context;

    RecyclerView Pixel_Recycler_View;
    PixelAdapter pixelAdapter;

    Bitmap[] dataSet;

    Button create_btn, modify_btn, start_btn;

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_embroidery);

        create_btn = findViewById(R.id.create_btn);
        modify_btn = findViewById(R.id.modify_btn);
        start_btn = findViewById(R.id.start_btn);

        settings = getSharedPreferences("localData", 0);
        editor = settings.edit();

        // 비트맵 데이터 전처리
        Bitmap tempBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
        tempBitmap.eraseColor(0xFFBDBDBD);

        dataSet = new Bitmap[]{
                tempBitmap,
                tempBitmap,
                tempBitmap,

                tempBitmap,
                tempBitmap,
                tempBitmap,

                tempBitmap,
                tempBitmap,
                tempBitmap,

                tempBitmap,
                tempBitmap,
                tempBitmap,

                tempBitmap,
                tempBitmap,
                tempBitmap
        };


        //saveFile(si + ".pixel_artist", false);
        //screenShot(linearLayout, si + ".jpg");
        int si = settings.getInt("save_index", 3);
        Log.e("자수 시작하기", "세이브 인덱스: " + si);

        if (si < 4) {
            // 처음 사용자
            for (int i = 0; i < 4; i++) {
                //파일 블러오기 코드
                File imageFolder = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), getString(R.string.app_name));

                File openFile = new File(imageFolder, i + ".jpg");

                Bitmap bMap;

                if(openFile.exists()) {
                    Uri uri = Uri.fromFile(openFile);
                    try {
                        bMap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    } catch (IOException e) {
                        bMap = tempBitmap;
                        e.printStackTrace();
                    }
                    //bMap = BitmapFactory.decodeFile(openFile.getPath());
                } else {
                    bMap = tempBitmap;
                }

                dataSet[i] = bMap;
            }
        } else {
            // 고인물 전용 : 비트맵 로드
            for (int i = 0; i < dataSet.length; i++) {
                //파일 블러오기 코드
                File imageFolder = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), getString(R.string.app_name));

                File openFile = new File(imageFolder, i + ".jpg");

                Bitmap bMap;

                if(openFile.exists()) {
                    Uri uri = Uri.fromFile(openFile);
                    try {
                        bMap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    } catch (IOException e) {
                        bMap = tempBitmap;
                        e.printStackTrace();
                    }
                    //bMap = BitmapFactory.decodeFile(openFile.getPath());
                } else {
                    bMap = tempBitmap;
                }

                dataSet[i] = bMap;
            }
        }



        Pixel_Recycler_View = findViewById(R.id.Pixel_Recycler_View);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 3);
        Pixel_Recycler_View.setLayoutManager(layoutManager);
        pixelAdapter = new PixelAdapter(dataSet);

        Pixel_Recycler_View.setAdapter(pixelAdapter);

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmbroideryActivity.this, StartEmbroidActivity.class);
                intent.putExtra("mode", "start");
                startActivity(intent);
                finish();
            }
        });

        modify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //파일 블러오기 코드
                //File imageFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getString(R.string.app_name));
                File imageFolder = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                //File openFile = new File(imageFolder, pixelAdapter.getCurrentCheckedPos() + ".jpg");
                File openFile = new File(imageFolder, pixelAdapter.getCurrentCheckedPos() + ".pixel_artist");

                if(openFile.exists()) {
                    Intent intent = new Intent(EmbroideryActivity.this, StartEmbroidActivity.class);
                    intent.putExtra("mode", "edit");
                    intent.putExtra("pa_index", pixelAdapter.getCurrentCheckedPos());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EmbroideryActivity.this, "빈 자수는 수정 할 수 없습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : 구현 끝판왕... ㅠㅠ
            }
        });

    }

    // TODO : 불러오기 함수
    public LinearLayout openFile(String fileName, boolean showToast) {
        File imageFolder = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File openFile = new File(imageFolder, fileName);

        LayoutInflater inflater = getLayoutInflater();

        View ase = inflater.inflate(R.layout.activity_start_embroid, null);

        LinearLayout linearLayout = ase.findViewById(R.id.paper_linear_layout);

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

            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {

                    if ((value = bufferedReader.readLine()) != null) {
                        color = Integer.valueOf(value);
                    } else {
                        throw new IOException();
                    }

                    View v = ((LinearLayout) linearLayout.getChildAt(i)).getChildAt(j);
                    v.setBackgroundColor(color);
                }
            }

        } catch (FileNotFoundException e) {
            Log.e("MainActivity.openFile", "File not found");
        } catch (IOException e) {
            Log.e("MainActivity.openFile", "Could not open file");
        }

        return linearLayout;
    }
}