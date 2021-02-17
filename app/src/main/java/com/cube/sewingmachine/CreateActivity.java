package com.cube.sewingmachine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CreateActivity extends AppCompatActivity {
    Button back_btn, finish_btn, start_btn, stop_btn;

    private static final int MY_REQUEST_WRITE_STORAGE = 5;
    private int currentColor;
    private Button colorButtons[];
    private int colors[];
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    int pa_index = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        back_btn = findViewById(R.id.back_btn);
        finish_btn = findViewById(R.id.finish_btn);
        start_btn = findViewById(R.id.start_btn);
        stop_btn = findViewById(R.id.stop_btn);

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
                finish();
            }
        });

        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                }
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
    private void fillScreen_detail(int color, int start_row, int start_column, int end_column) {
        LinearLayout paper = findViewById(R.id.paper_linear_layout);

        start_row -= 1;
        start_column -= 1;

        for (int i = start_row, end_row = start_row + 1; i < end_row; i++) {
            LinearLayout l = (LinearLayout) paper.getChildAt(i);

            for (int j = start_column; j < end_column; j++) {
                View pixel = l.getChildAt(j);

                pixel.setBackgroundColor(color);
            }
        }
    }

    //Onclick method that changes the color of a single "pixel"
    // TODO : 픽셀 선택 했을때 온-클릭
    public void changeColor(View v) {

    }
}
