package com.cube.sewingmachine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
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
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.cube.sewingmachine.PixelAdapter.*;

public class EmbroideryActivity extends AppCompatActivity {
    private static final int MY_REQUEST_WRITE_STORAGE = 5;
    Context context;

    RecyclerView Pixel_Recycler_View;
    PixelAdapter pixelAdapter;

    Bitmap[] dataSet;

    Button create_btn, modify_btn, start_btn;

    AppCompatButton back_btn;

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

        back_btn = findViewById(R.id.back_btn);

        settings = getSharedPreferences("localData", 0);
        editor = settings.edit();

        if (!settings.getBoolean("firstInit", false)) {
            firstInit();
        }

        // 비트맵 데이터 전처리
        Bitmap tempBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
        tempBitmap.eraseColor(0xFFF4F4F4);

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

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //파일 블러오기 코드
                //File imageFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getString(R.string.app_name));
                File imageFolder = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                //File openFile = new File(imageFolder, pixelAdapter.getCurrentCheckedPos() + ".jpg");
                File openFile = new File(imageFolder, pixelAdapter.getCurrentCheckedPos() + ".pixel_artist");

                if(openFile.exists()) {
                    Intent intent2 = new Intent(EmbroideryActivity.this, CreateActivity.class);
                    intent2.putExtra("pa_index", pixelAdapter.getCurrentCheckedPos());
                    startActivity(intent2);
                    finish();
                } else {
                    Toast.makeText(EmbroideryActivity.this, "빈 자수는 시작 할 수 없습니다", Toast.LENGTH_SHORT).show();
                }
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

    public void firstInit() {
        copyAssets();

        BitmapDrawable drawable0 = (BitmapDrawable) getResources().getDrawable(R.drawable.pixel_0);
        screenShot2(drawable0.getBitmap().copy(Bitmap.Config.RGB_565, true), "0" + ".jpg");
        drawable0.getBitmap().recycle();

        BitmapDrawable drawable1 = (BitmapDrawable) getResources().getDrawable(R.drawable.pixel_1);
        screenShot2(drawable1.getBitmap().copy(Bitmap.Config.RGB_565, true), "1" + ".jpg");
        drawable1.getBitmap().recycle();

        BitmapDrawable drawable2 = (BitmapDrawable) getResources().getDrawable(R.drawable.pixel_2);
        screenShot2(drawable2.getBitmap().copy(Bitmap.Config.RGB_565, true), "2" + ".jpg");
        drawable2.getBitmap().recycle();

        BitmapDrawable drawable3 = (BitmapDrawable) getResources().getDrawable(R.drawable.pixel_3);
        screenShot2(drawable3.getBitmap().copy(Bitmap.Config.RGB_565, true), "3" + ".jpg");
        drawable3.getBitmap().recycle();

        BitmapDrawable drawable4 = (BitmapDrawable) getResources().getDrawable(R.drawable.pixel_bl);
        screenShot2(drawable4.getBitmap().copy(Bitmap.Config.RGB_565, true), "bl" + ".jpg");
        drawable4.getBitmap().recycle();

        editor.putBoolean("firstInit", true);
        editor.apply();
    }

    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        for(String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                // images, sounds, webkit
                if (filename.equals("images") ||
                        filename.equals("sounds") ||
                        filename.equals("webkit")) {
                } else {
                    in = assetManager.open(filename);

                    File outDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                    File outFile = new File(outDir, filename);

                    out = new FileOutputStream(outFile);
                    copyFile(in, out);
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                }
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    // TODO : JPG 저장 함수2
    public void screenShot2(Bitmap bm, String filename) {
        if (isExternalStorageWritable()) {
            Log.e(MainActivity.class.getName(), "External storage is not writable");
        }

        File imageFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), getString(R.string.app_name));
        Log.e("프리셋 스샷", "screenShot: " + imageFolder.getPath());

        boolean success = true;

        if (!imageFolder.exists()) {
            success = imageFolder.mkdirs();
        }

        if (success) {
            File imageFile = new File(imageFolder, filename);

            FileOutputStream outputStream;

            try {

                if (!imageFile.exists()) {
                    imageFile.createNewFile();
                }

                outputStream = new FileOutputStream(imageFile);
                int quality = 30;
                bm.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (FileNotFoundException e) {
                Log.e(MainActivity.class.getName(), "File not found");
            } catch (IOException e) {
                Log.e(MainActivity.class.getName(), "IOException related to generating bitmap file");
            }
        } else {
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return !Environment.MEDIA_MOUNTED.equals(state);
    }
}