package com.blogspot.programer27android.playsound;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<PlayInfo> arrayPlayinfo = new ArrayList<>();
    RecyclerView recyclerView;
    AdapterPlay playAdapter;
    SeekBar seekBar;
    MediaPlayer mediaPlayer;

    Handler handler=new Handler();
    String ofc35 = "https://guruinovatif-my.sharepoint.com/personal/geri_guruinovatif_net/Documents/kajian_dan_murotal/murotal_persurat/Hafs/syeaikh_nasher_al-qatami_hafs/055.mp3";
    String linkluar = "http://archive.org/download/Khaled_AlKahtani/055.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycleiew);
        seekBar = findViewById(R.id.seekbar);
        //online data
//        PlayInfo plp = new PlayInfo("al-qatamy", "arrahman", ofc35);
//        arrayPlayinfo.add(plp);

        playAdapter = new AdapterPlay(this, arrayPlayinfo);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(playAdapter);

        playAdapter.setClickItem(new AdapterPlay.OnitemClickListener() {
            @Override
            public void onItemClick(final Button b, View v, final PlayInfo obj, int posisi) {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (b.getText().toString().equals("stop")) {
                                b.setText("play");
                                mediaPlayer.stop();
                                mediaPlayer.reset();
                                mediaPlayer.release();
                                mediaPlayer = null;
                            } else {
                                mediaPlayer = new MediaPlayer();
                                mediaPlayer.setDataSource(obj.getUrlInfo());
                                mediaPlayer.prepareAsync();
                                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        mp.start();
                                        seekBar.setProgress(0);
                                        seekBar.setMax(mp.getDuration());
                                        b.setText("stop");
                                    }
                                });
                            }
                        } catch (IOException e) {
                            Toast.makeText(MainActivity.this, "File Missing", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                handler.postDelayed(r,100);

            }
        });
        cekPermisi();
        Thread t= new MyThread();
        t.start();
    }


    public class MyThread extends Thread{
        @Override
        public void run() {
            try{
                Thread.sleep(1000);
                if(mediaPlayer !=null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition()
                    );
                }
            }catch (InterruptedException e){
                e.printStackTrace();

            }

        }
    }

    private void cekPermisi() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                return;
            }
        } else {
            loadSound();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 123:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    loadSound();
                }else {
                    Toast.makeText(this, "Permission Danied", Toast.LENGTH_SHORT).show();
                    cekPermisi();
                }
                break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void loadSound() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String pilihan= MediaStore.Audio.Media.IS_MUSIC+"!=0";
        Cursor cursor=getContentResolver().query(uri,null,pilihan,null,null);
        if (cursor !=null){
            if (cursor.moveToFirst()){
                do{
                    String surat=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String qori=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String url=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    PlayInfo pdatastorage=new PlayInfo(surat,qori,url);
                    arrayPlayinfo.add(pdatastorage);
                }while (cursor.moveToNext());
            }
            cursor.close();
            playAdapter=new AdapterPlay(this,arrayPlayinfo);
        }


    }
}
