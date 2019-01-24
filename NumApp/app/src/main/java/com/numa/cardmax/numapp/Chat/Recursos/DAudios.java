package com.numa.cardmax.numapp.Chat.Recursos;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.numa.cardmax.numapp.Chat.MensajesChats.MensajesAdapter;
import com.numa.cardmax.numapp.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DAudios {
    private ProgressDialog prgDialog;
    private MediaPlayer mediaPlayer;
    private int mediaFileLenght;
    private int realtimeLength;
    final Handler handler = new Handler();
    private Context mContexto;

    public DAudios(Context context) {
        mContexto = context;
    }

    public void DescargarAudio(String urlAudio, String idguardada, String nombreAudio,
                               FloatingActionButton btnreproducir, SeekBar seekBar, FloatingActionButton btnpause,String tipo) {

        File file_path = Environment.getExternalStorageDirectory().getAbsoluteFile();
      //  File dir = new File(file_path + "/NumAPP/" + "Audio/" + "Send/" + idguardada);
        File dir = new File(file_path + "/NumAPP/" + "Audio/" + tipo + idguardada);
        File audio = new File(dir + "/" + nombreAudio + ".3gp");
        Uri myUri1 = Uri.parse(String.valueOf(audio));

        if (!dir.exists()) {
            dir.mkdirs();
            if (audio.exists()) {
                //  playMusic(idguardada,nombre);
                reporducirAudio(myUri1, btnreproducir, seekBar, btnpause);
            } else {
                new DescargarAudioFire().execute(urlAudio, idguardada, nombreAudio);
                reporducirAudio(myUri1, btnreproducir, seekBar, btnpause);

            }

        } else {
            if (audio.exists()) {
                //   playMusic(idguardada,nombre);
                //  Toast.makeText(context, "existe el audio", Toast.LENGTH_SHORT).show();
                reporducirAudio(myUri1, btnreproducir, seekBar, btnpause);
            } else {
                new DescargarAudioFire().execute(urlAudio, idguardada, nombreAudio,tipo);
                reporducirAudio(myUri1, btnreproducir, seekBar, btnpause);
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    class DescargarAudioFire extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new ProgressDialog(mContexto);
            prgDialog.setIcon(R.mipmap.ic_d_audio);
            prgDialog.setTitle("Descargando Audio");
            prgDialog.setMessage("Por favor espere...");
            prgDialog.setIndeterminate(false);
            prgDialog.setMax(100);
            prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            prgDialog.setCancelable(false);
            prgDialog.show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 10 * 1024);
                File file_path = Environment.getExternalStorageDirectory().getAbsoluteFile();
                String carpeta = f_url[1];
                String nombre = "/" + f_url[2];
                String tipo = f_url[3];
                //File dir = new File(file_path + "/NumAPP/" + "Audio/" + "Send/" + carpeta + nombre + ".3gp");
                File dir = new File(file_path + "/NumAPP/" + "Audio/" + tipo + carpeta + nombre + ".3gp");
                OutputStream output = new FileOutputStream(dir);


                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            prgDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            prgDialog.dismiss();

        }
    }

    public void reporducirAudio(Uri url, final FloatingActionButton reproducir_sender,
                                final SeekBar seekBar,
                                final FloatingActionButton pause_reproducir) {
        mediaPlayer = new MediaPlayer();

        @SuppressLint("StaticFieldLeak")
        AsyncTask<String, String, String> mp3Play = new AsyncTask<String, String, String>() {

            @Override
            protected String doInBackground(String... strings) {

                try {
                    mediaPlayer.setDataSource(strings[0]);
                    mediaPlayer.prepare();
                } catch (Exception e) {

                }
                return "";
            }

            @Override
            protected void onPostExecute(String s) {

                mediaFileLenght = mediaPlayer.getDuration();
                realtimeLength = mediaFileLenght;
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    reproducir_sender.setVisibility(View.GONE);
                    pause_reproducir.setVisibility(View.VISIBLE);
                    pause_reproducir.setImageResource(R.drawable.ic_pause_29);
                } else {
                    mediaPlayer.pause();
                    pause_reproducir.setImageResource(R.drawable.ic_play_29);
                }
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        try {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            seekBar.setProgress(0);
                            reproducir_sender.setVisibility(View.VISIBLE);
                            pause_reproducir.setVisibility(View.GONE);
                        } catch (IllegalStateException e) {
                            Log.e("Error #32: ", e.getMessage());
                        }
                        reproducir_sender.setImageResource(R.drawable.ic_play_29);
                    }

                });
                updateseekbar(seekBar);
            }
        };

        mp3Play.execute(String.valueOf(url));
    }

    public void updateseekbar(final SeekBar seekBar) {

        try {
            seekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLenght) * 100));

            if (mediaPlayer.isPlaying()) {
                Runnable updater = new Runnable() {
                    @Override
                    public void run() {
                        updateseekbar(seekBar);
                    }
                };
                handler.postDelayed(updater, 1000); // 1 second
            }


        } catch (IllegalStateException e) {
            seekBar.setProgress(0);
        }

    }

    public void seekbarEstado(View v) {
        try {
            if (mediaPlayer.isPlaying()) {
                SeekBar seekBar = (SeekBar) v;
                int playPosition = (mediaFileLenght / 100) * seekBar.getProgress();
                mediaPlayer.seekTo(playPosition);
            } else {
                mediaPlayer.seekTo(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Iniciar(Context context, String urlAudio, String idguardada, String nombreAudio,
                        FloatingActionButton btnreproducir, SeekBar seekBar, FloatingActionButton btnpause,String tipo) {
        try {
            if (mediaPlayer.isPlaying()) {
                Toast.makeText(context, "hay una en curso", Toast.LENGTH_SHORT).show();
            } else if (!mediaPlayer.isPlaying() && mediaPlayer.getCurrentPosition() > 1) {
                Toast.makeText(context, "hay una en curso pausada", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            DescargarAudio(urlAudio, idguardada,
                    nombreAudio, btnreproducir,
                    seekBar, btnpause,tipo);
            e.printStackTrace();
        }
    }

    public void PausarBoton(FloatingActionButton boton, SeekBar seekBar) {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                boton.setImageResource(R.drawable.ic_play_29);
            } else {
                mediaPlayer.start();
                boton.setImageResource(R.drawable.ic_pause_29);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        updateseekbar(seekBar);
    }

    public void EstaSonando() {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void PararAudio() {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


