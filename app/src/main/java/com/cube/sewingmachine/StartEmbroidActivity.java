package com.cube.sewingmachine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class StartEmbroidActivity extends AppCompatActivity {

    private static final String SETTINGS_GRID = "grid";
    private static final String URL_ABOUT = "https://github.com/RodrigoDavy/PixelArtist/blob/master/README.md";
    private static final int MY_REQUEST_WRITE_STORAGE = 5;
    private int currentColor;
    private Button colorButtons[];
    private int colors[];
    private ActionBarDrawerToggle drawerToggle;
    private SharedPreferences settings;
    private boolean grid;

    /**
     * Converts a file to a content uri, by inserting it into the media store.
     * Requires this permission: <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     */
    protected static Uri convertFileToContentUri(Context context, File file) throws Exception {

        //Uri localImageUri = Uri.fromFile(localImageFile); // Not suitable as it's not a content Uri

        ContentResolver cr = context.getContentResolver();
        String imagePath = file.getAbsolutePath();
        String uriString = Media.insertImage(cr, imagePath, null, null);
        return Uri.parse(uriString);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_embroid);
        
        initPalette();
        initPixels();
        fillScreen(ContextCompat.getColor(StartEmbroidActivity.this, R.color.white));

        settings = getPreferences(0);
        pixelGrid(true);

        // 프리셋 테스트
        fillScreen(ContextCompat.getColor(StartEmbroidActivity.this, R.color.white));
        drawPreset_friedEgg();
        saveFile("0" + ".pixel_artist", false);

        fillScreen(ContextCompat.getColor(StartEmbroidActivity.this, R.color.white));
        drawPreset_heart();
        saveFile("1" + ".pixel_artist", false);

        fillScreen(ContextCompat.getColor(StartEmbroidActivity.this, R.color.white));
        drawPreset_cactus();
        saveFile("2" + ".pixel_artist", false);

        fillScreen(ContextCompat.getColor(StartEmbroidActivity.this, R.color.white));
        drawPreset_smile();
        saveFile("3" + ".pixel_artist", false);

        fillScreen(ContextCompat.getColor(StartEmbroidActivity.this, R.color.white));
        //

        openFile(".tmp", false);
    }

    @Override
    protected void onStop() {
        super.onStop();

        saveFile(".tmp", false);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(SETTINGS_GRID, grid);
        editor.apply();
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

    // TODO : 저장 함수
    public void saveFile(String fileName, boolean showToast) {
        if (isExternalStorageWritable()) {
            Log.e(MainActivity.class.getName(), "External Storage is not writable");
        }

        File imageFolder = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File saveFile = new File(imageFolder, fileName);

        try {
            if (!saveFile.exists()) {
                saveFile.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(saveFile);
            fileWriter.append("8\n8\n");

            LinearLayout linearLayout = findViewById(R.id.paper_linear_layout);
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    View v = ((LinearLayout) linearLayout.getChildAt(i)).getChildAt(j);
                    int color = ((ColorDrawable) v.getBackground()).getColor();
                    fileWriter.append(String.valueOf(color));
                    fileWriter.append("\n");
                }
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            Log.e("MainActivity.saveFile", "File not found");
        }
    }

    // TODO : 권한 요청
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    // can run additional stuff here
                    //Toast.makeText(this, R.string.write_permission_granted, Toast.LENGTH_LONG).show();
                } else {
                    // permission denied
                    //Toast.makeText(this, R.string.write_permission_unavailable, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return !Environment.MEDIA_MOUNTED.equals(state);
    }

    // TODO : 미니맵 갱신 함수
    private void updateDrawerHeader() {
        View view = findViewById(R.id.paper_linear_layout);
        Bitmap bitmap;
        pixelGrid(false);

        if (view.getWidth() > 0 && view.getHeight() > 0) {
            bitmap = Bitmap.createBitmap(view.getWidth(),
                    view.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(200,
                    200, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.WHITE);
        }

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        ImageView mini_map = findViewById(R.id.mini_map);
        mini_map.setImageBitmap(bitmap);
        pixelGrid(true);
    }

    private void initPalette() {
        colorButtons = new Button[]{
                findViewById(R.id.color_button_black),
                findViewById(R.id.color_button_eclipse),
                findViewById(R.id.color_button_grey),
                findViewById(R.id.color_button_silver),
                findViewById(R.id.color_button_white),

                findViewById(R.id.color_button_red),
                findViewById(R.id.color_button_vermilion),
                findViewById(R.id.color_button_orange),
                findViewById(R.id.color_button_amber),
                findViewById(R.id.color_button_yellow),
                findViewById(R.id.color_button_lime),
                findViewById(R.id.color_button_chartreuse),
                findViewById(R.id.color_button_harlequin),
                findViewById(R.id.color_button_green),
                findViewById(R.id.color_button_malachite),
                findViewById(R.id.color_button_mint),
                findViewById(R.id.color_button_turquoise),
                findViewById(R.id.color_button_cyan),
                findViewById(R.id.color_button_sky_blue),
                findViewById(R.id.color_button_azure),
                findViewById(R.id.color_button_sapphire),
                findViewById(R.id.color_button_blue),
                findViewById(R.id.color_button_indigo),
                findViewById(R.id.color_button_purple),
                findViewById(R.id.color_button_lt_purple),
                findViewById(R.id.color_button_magenta),
                findViewById(R.id.color_button_fuchsia),
                findViewById(R.id.color_button_rose),
                findViewById(R.id.color_button_carmine)
        };

        colors = new int[]{
                ContextCompat.getColor(this, R.color.black),
                ContextCompat.getColor(this, R.color.eclipse),
                ContextCompat.getColor(this, R.color.grey),
                ContextCompat.getColor(this, R.color.silver),
                ContextCompat.getColor(this, R.color.white),

                ContextCompat.getColor(this, R.color.red),
                ContextCompat.getColor(this, R.color.vermilion),
                ContextCompat.getColor(this, R.color.orange),
                ContextCompat.getColor(this, R.color.amber),
                ContextCompat.getColor(this, R.color.yellow),
                ContextCompat.getColor(this, R.color.lime),
                ContextCompat.getColor(this, R.color.chartreuse),
                ContextCompat.getColor(this, R.color.harlequin),
                ContextCompat.getColor(this, R.color.green),
                ContextCompat.getColor(this, R.color.malachite),
                ContextCompat.getColor(this, R.color.mint),
                ContextCompat.getColor(this, R.color.turquoise),
                ContextCompat.getColor(this, R.color.cyan),
                ContextCompat.getColor(this, R.color.sky_blue),
                ContextCompat.getColor(this, R.color.azure),
                ContextCompat.getColor(this, R.color.sapphire),
                ContextCompat.getColor(this, R.color.blue),
                ContextCompat.getColor(this, R.color.indigo),
                ContextCompat.getColor(this, R.color.purple),
                ContextCompat.getColor(this, R.color.lt_purple),
                ContextCompat.getColor(this, R.color.magenta),
                ContextCompat.getColor(this, R.color.fuchsia),
                ContextCompat.getColor(this, R.color.rose),
                ContextCompat.getColor(this, R.color.carmine)
        };

        for (int i = 0; i < colorButtons.length; i++) {

            GradientDrawable cd = (GradientDrawable) colorButtons[i].getBackground();
            cd.setColor(colors[i]);
        }

        selectColor(colorButtons[0]);
    }

    //Initializes the "pixels" (basically sets OnLongClickListener on them)
    private void initPixels() {
        LinearLayout paper = findViewById(R.id.paper_linear_layout);

        for (int i = 0; i < paper.getChildCount(); i++) {
            LinearLayout l = (LinearLayout) paper.getChildAt(i);

            for (int j = 0; j < l.getChildCount(); j++) {
                View pixel = l.getChildAt(j);

                //Sets OnLongCLickListener to be able to select current color based on that pixel's color
                pixel.setOnLongClickListener(view -> {
                    selectColor(((ColorDrawable) view.getBackground()).getColor());
                    return false;
                });
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
            x = 1;
            y = 1;
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
    private void fillScreen_detail(int color, int start_row, int end_row, int start_column, int end_column) {
        LinearLayout paper = findViewById(R.id.paper_linear_layout);

        for (int i = start_row; i < end_row; i++) {
            LinearLayout l = (LinearLayout) paper.getChildAt(i);

            for (int j = start_column; j < end_column; j++) {
                View pixel = l.getChildAt(j);

                pixel.setBackgroundColor(color);
            }
        }
    }

    // TODO : 1번 프리셋 - 계란후라이
    private void drawPreset_friedEgg() {
        // STEP 1 : 원 그리기
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 0, 1, 2, 6);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 1, 2, 1, 2);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 1, 2, 6, 7);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 2, 3, 0, 1);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 2, 3, 7, 8);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 3, 4, 0, 1);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 3, 4, 7, 8);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 4, 5, 0, 1);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 4, 5, 7, 8);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 5, 6, 0, 1);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 5, 6, 7, 8);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 6, 7, 1, 2);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 6, 7, 6, 7);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 7, 8, 2, 6);

        // STEP 2 : 노른자
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.amber), 2, 3, 3, 5);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.amber), 3, 4, 2, 6);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.white), 3, 4, 3, 4);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.amber), 4, 5, 2, 6);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.amber), 5, 6, 3, 5);
    }

    // TODO : 2번 프리셋 - 하트
    private void drawPreset_heart() {
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 0, 1, 1, 3);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 0, 1, 5, 7);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 1, 2, 0, 1);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 1, 2, 3, 5);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 1, 2, 7, 8);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.red), 1, 2, 1, 3);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.red), 1, 2, 5, 7);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 2, 3, 0, 1);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 2, 3, 7, 8);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.red), 2, 3, 1, 7);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 3, 4, 0, 1);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 3, 4, 7, 8);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.red), 3, 4, 1, 7);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 4, 5, 0, 1);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 4, 5, 7, 8);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.red), 4, 5, 1, 7);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 5, 6, 1, 2);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 5, 6, 6, 7);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.red), 5, 6, 2, 6);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 6, 7, 2, 3);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 6, 7, 5, 6);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.red), 6, 7, 3, 5);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 7, 8, 3, 5);
    }

    // TODO : 3번 프리셋 - 선인장
    private void drawPreset_cactus() {
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 0, 1, 3, 5);

        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 1, 2, 1, 3);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 1, 2, 5, 6);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.malachite), 1, 2, 3, 5);

        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 2, 3, 0, 1);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 2, 3, 2, 3);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 2, 3, 5, 7);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.malachite), 2, 3, 1, 2);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.malachite), 2, 3, 3, 5);

        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 3, 4, 1, 2);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 3, 4, 5, 6);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 3, 4, 7, 8);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.malachite), 3, 4, 2, 5);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.malachite), 3, 4, 6, 7);

        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 4, 5, 2, 3);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 4, 5, 6, 7);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.malachite), 4, 5, 3, 6);

        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 5, 6, 2, 3);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 5, 6, 5, 6);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.malachite), 5, 6, 3, 5);

        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 6, 7, 2, 3);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 6, 7, 5, 6);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.malachite), 6, 7, 3, 5);

        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 7, 8, 3, 5);
    }

    // TODO : 4번 프리셋 - 스마일
    private void drawPreset_smile() {
        // STEP 1 : 머리 그리기
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 0, 1, 2, 6);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 1, 2, 1, 2);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.amber), 1, 2, 2, 6);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 1, 2, 6, 7);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 2, 3, 0, 1);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.amber), 2, 3, 1, 7);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 2, 3, 7, 8);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 3, 4, 0, 1);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.amber), 3, 4, 1, 7);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 3, 4, 7, 8);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 4, 5, 0, 1);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.amber), 4, 5, 1, 7);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 4, 5, 7, 8);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 5, 6, 0, 1);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.amber), 5, 6, 1, 7);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 5, 6, 7, 8);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 6, 7, 1, 2);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.amber), 6, 7, 2, 7);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 6, 7, 6, 7);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 7, 8, 2, 6);

        // STEP 2 : 표정 그리기
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 2, 3, 2, 3);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 2, 3, 5, 6);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 4, 5, 2, 3);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 4, 5, 5, 6);
        fillScreen_detail(ContextCompat.getColor(StartEmbroidActivity.this, R.color.black), 5, 6, 3, 5);
    }

    //On click method that selects the current color based on the palette button pressed
    public void selectColor(View v) {
        int i = 0;

        for (Button b : colorButtons) {
            if (v.getId() == b.getId()) {
                break;
            }

            i += 1;
        }

        selectColor(colors[i]);
    }

    //Sets the current color based on the "color" argument
    public void selectColor(int color) {
        currentColor = color;

        findViewById(R.id.palette_linear_layout).setBackgroundColor(currentColor);
    }

    //Onclick method that changes the color of a single "pixel"
    // TODO : 픽셀 선택 했을때 온-클릭
    public void changeColor(View v) {
        v.setBackgroundColor(currentColor);

        updateDrawerHeader();
    }
}