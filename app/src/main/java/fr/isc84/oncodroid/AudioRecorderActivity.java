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
import android.view.MenuItem;
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
    public static final int permission = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);
        if (checkPermission()) {
            setAudioRecorder();
        }
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
                    fichierAudio = "Dict??e_du_" + date;
                    cheminDuFichier = myDirectory.getAbsolutePath() + File.separator + fichierAudio +".mp3";
                    startAudioRecorder();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                showTimer();
            }
        });
    }

    //fonction permettant de demarrer l'enregistrement audio et choix du format de sortie
    public void startAudioRecorder() {
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
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
                // Arret du compte ?? rebours
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

                //cr??er un r??solveur de contenu et mettre les valeurs
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

    // Donne les droits d'execution
    public boolean checkPermission() {
        int RECORD_AUDIO_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int WRITE_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        ArrayList<String> PERMISSION_LIST = new ArrayList<>();
        if ((RECORD_AUDIO_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            PERMISSION_LIST.add(Manifest.permission.RECORD_AUDIO);
        }
        if ((WRITE_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            PERMISSION_LIST.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!PERMISSION_LIST.isEmpty()) {
            ActivityCompat.requestPermissions(this, PERMISSION_LIST.toArray(new String[PERMISSION_LIST.size()]), permission);
            return false;
        }
        return true;
    }
    // Autorisation des droits d'acces au micro et au stockage de l'appareil
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean record = false, storage = false;
        switch (requestCode) {
            case permission: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(Manifest.permission.RECORD_AUDIO)) {
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                record = true;
                            } else {
                                Toast.makeText(getApplicationContext(), "j'autorise l'acces a mon micro", Toast.LENGTH_LONG).show();
                            }
                        } else if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                storage = true;
                            } else {
                                Toast.makeText(getApplicationContext(), "j'autorise l'acces a mon stockage de fichiers", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
                if (record && storage) {
                    setAudioRecorder();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaRecorder != null) {
            mediaRecorder.release();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.delItem) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}