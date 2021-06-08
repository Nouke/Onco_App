package fr.isc84.oncodroid;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AudioRecorderActivity extends AppCompatActivity {

    TextView textView;
    ImageView start, stop, rec;
    MediaRecorder mediaRecorder;
    CountDownTimer countDownTimer;
    int seconde = -1, minute, heure;
    String cheminDuFichier;
    String fichierAudio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);

    }

    public void setAudioRecorder() {
        textView = (TextView) findViewById(R.id.text);
        start = (ImageView) findViewById(R.id.start);
        stop = (ImageView) findViewById(R.id.stop);
        rec = (ImageView) findViewById(R.id.recordings);
        stop.setEnabled(false);
        stop.setBackgroundResource(R.drawable.normal_background);
        stop.setImageResource(R.drawable.noraml_stop);
        Recording();
        stopRecording();
        getRecordings();
    }

    public void Recording() {
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setEnabled(false);
                rec.setEnabled(false);
                stop.setEnabled(true);
                stop.setBackgroundResource(R.drawable.round_shape);
                stop.setImageResource(R.drawable.ic_stop_black_35dp);
                rec.setBackgroundResource(R.drawable.normal_background);
                rec.setImageResource(R.drawable.normal_menu);

                try {
                    // Creation d'un repertoire pour stocker les fichiers audio
                    File myDirectory = new File(Environment.getExternalStorageDirectory(), "Oncodroid");
                    if (!myDirectory.exists()) {
                        myDirectory.mkdirs();
                    }
                    // Declaration objet date
                    SimpleDateFormat dateFormat = new SimpleDateFormat("mmddyyyyhhmmss");
                    String date = dateFormat.format(new Date());

                    //Concatenation nom file et date
                    fichierAudio = "REC" + date;
                    cheminDuFichier = myDirectory.getAbsolutePath() + File.separator + fichierAudio;
                    startAudioRecorder();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                showTimer();
            }
        });
    }

    //fonction permettant de demarrer l'enregistrement audio
    public void startAudioRecorder() {
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(cheminDuFichier);
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //fonction permettant de stopper l'enregistrement audio
    public void stopRecording() {
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Arret du compte à rebours
                countDownTimer.cancel();
                start.setEnabled(true);
                rec.setEnabled(true);
                stop.setEnabled(false);
                stop.setBackgroundResource(R.drawable.normal_background);
                stop.setImageResource(R.drawable.noraml_stop);
                rec.setBackgroundResource(R.drawable.round_shape);
                rec.setImageResource(R.drawable.ic_menu_black_35dp);
                seconde = -1;
                minute = 0;
                heure = 0;
                textView.setText("00:00:00");

                if (mediaRecorder != null) {
                    try {
                        //Arreter l'enregistrement
                        mediaRecorder.stop();
                        mediaRecorder.reset();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }

                //créer un résolveur de contenu et mettre les valeurs
                ContentValues values = new ContentValues();
                values.put(MediaStore.Audio.Media.DATA, cheminDuFichier);
                values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
                values.put(MediaStore.Audio.Media.TITLE, fichierAudio);
                // stocker le fichier audio dans l'uri du contenu externe
                getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
            }
        });
    }

    //lancer RecordingsActivity
    public void getRecordings() {
        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setEnabled(true);
                rec.setEnabled(true);
                stop.setEnabled(false);
                stop.setBackgroundResource(R.drawable.normal_background);
                stop.setImageResource(R.drawable.noraml_stop);
                rec.setBackgroundResource(R.drawable.round_shape);
                rec.setImageResource(R.drawable.ic_menu_black_35dp);
                startActivity(new Intent(getApplicationContext(), RecordingsActivity.class));
            }
        });
    }

    // afficher le miniteur ou timer
    public void showTimer() {
        countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                seconde++;
                textView.setText(recorderTime());
            }

            public void onFinish() {

            }
        };
        countDownTimer.start();
    }

    // le temps du recorder
    public String recorderTime() {
        if (seconde == 60) {
            minute++;
            seconde = 0;
        }
        if (minute == 60) {
            heure++;
            minute = 0;
        }
        return String.format("%02d:%02d:%02d", heure, minute, seconde);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaRecorder != null) {
            mediaRecorder.release();
        }
    }
}