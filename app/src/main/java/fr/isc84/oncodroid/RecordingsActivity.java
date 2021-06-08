package fr.isc84.oncodroid;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecordingsActivity extends AppCompatActivity {

    ArrayList<ModelRecordings> audioArrayList;
    RecyclerView recyclerView;
    MediaPlayer mediaPlayer;
    double current_pos, total_duration;
    TextView current, total;
    ImageView prev, next, pause;
    SeekBar seekBar;
    int audio_index = 0;
    //  public static final int PERMISSION_READ = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void getRecordings() {
        //   recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        audioArrayList = new ArrayList<>();
        mediaPlayer = new MediaPlayer();
        getAudioRecordings();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                current_pos = seekBar.getProgress();
                mediaPlayer.seekTo((int) current_pos);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                audio_index++;
                if (audio_index < (audioArrayList.size())) {
                    playRecording(audio_index);
                } else {
                    audio_index = 0;
                    playRecording(audio_index);
                }

            }
        });

        if (!audioArrayList.isEmpty()) {
            playRecording(audio_index);
            prevRecording();
            nextRecording();
            setPause();
        }
    }

    public void playRecording(int pos) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, audioArrayList.get(pos).getUri());
            mediaPlayer.prepare();
            mediaPlayer.start();
            pause.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
            audio_index = pos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        setAudioProgress();
    }

    public void setAudioProgress() {
        current_pos = mediaPlayer.getCurrentPosition();
        total_duration = mediaPlayer.getDuration();

        total.setText(timeConversion((long) total_duration));
        current.setText(timeConversion((long) current_pos));
        seekBar.setMax((int) total_duration);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    current_pos = mediaPlayer.getCurrentPosition();
                    current.setText(timeConversion((long) current_pos));
                    seekBar.setProgress((int) current_pos);
                    handler.postDelayed(this, 1000);
                } catch (IllegalStateException ed) {
                    ed.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    public void prevRecording() {
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audio_index > 0) {
                    audio_index--;
                    playRecording(audio_index);
                } else {
                    audio_index = audioArrayList.size() - 1;
                    playRecording(audio_index);
                }
            }
        });
    }

    public void nextRecording() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audio_index < (audioArrayList.size() - 1)) {
                    audio_index++;
                    playRecording(audio_index);
                } else {
                    audio_index = 0;
                    playRecording(audio_index);
                }
            }
        });
    }

    public void setPause() {
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    pause.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                } else {
                    mediaPlayer.start();
                    pause.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                }
            }
        });
    }

    // Conversion du temps
    public String timeConversion(long value) {
        String audioTime;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            audioTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
            audioTime = String.format("%02d:%02d", mns, scs);
        }
        return audioTime;
    }

    public void getAudioRecordings() {
        ContentResolver contentResolver = getContentResolver();
        //création d'un résolveur de contenu et récupération de fichiers audio depuis le stockage
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, null, MediaStore.Audio.Media.DATA + " like ?", new String[]{"%11zon%"}, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {

                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                ModelRecordings modelRecordings = new ModelRecordings();
                modelRecordings.setTitle(title);
                File file = new File(data);
                Date date = new Date(file.lastModified());
                SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy");

                modelRecordings.setDate(format.format(date));
                modelRecordings.setUri(Uri.parse(data));

                //Récupérer la durée de l'audio en utilisant la classe  MediaMetadataRetriever
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(data);

                modelRecordings.setDuration(timeConversion(Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))));
                audioArrayList.add(modelRecordings);

            } while (cursor.moveToNext());
        }


   /*  @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    } */
    }
}