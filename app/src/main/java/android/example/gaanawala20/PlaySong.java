package android.example.gaanawala20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateseek.interrupt();
    }

    TextView textView;
    ImageView previous,pause,next;
    ArrayList<File>Songs;
    MediaPlayer mediaPlayer;
    String textContent;
    Integer position;
    SeekBar seekBar;
    Thread updateseek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        textView = findViewById(R.id.textView);
        previous = findViewById(R.id.previous);
        pause = findViewById(R.id.pause);
        next = findViewById(R.id.next);
        seekBar = findViewById(R.id.seekBar);

     Intent intent = getIntent();
     Bundle bundle = intent.getExtras();
     Songs = (ArrayList)bundle.getParcelableArrayList("songlist");
     textContent =intent.getStringExtra("currentsong");
     textView.setText(textContent);

     position = intent.getIntExtra("position",0);
     Uri uri = Uri.parse(Songs.get(position).toString());
     mediaPlayer = MediaPlayer.create(this,uri);
     mediaPlayer.start();
     seekBar.setMax(mediaPlayer.getDuration());


      seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
          @Override
          public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

          }

          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {

          }

          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {
mediaPlayer.seekTo(seekBar.getProgress());
          }
      });

      updateseek = new Thread(){
          public void run(){
int currentposition = 0;
try {
while (currentposition<mediaPlayer.getDuration()){
    currentposition=mediaPlayer.getCurrentPosition();
    seekBar.setProgress(currentposition);
    sleep(100);


}
}
catch (Exception e){
    e.printStackTrace();
}


          }
      };

updateseek.start();

pause.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(mediaPlayer.isPlaying()){
            pause.setImageResource(R.drawable.play);
            mediaPlayer.pause();
        }
        else {
            pause.setImageResource(R.drawable.pause);
            mediaPlayer.start();
        }

            }
});



previous.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        mediaPlayer.stop();
        mediaPlayer.release();
       if(position!=0)
       {
            position=position-1;
       }
       else {
           position=Songs.size()-1;
       }

        Uri uri = Uri.parse(Songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        pause.setImageResource(R.drawable.pause);
        seekBar.setMax(mediaPlayer.getDuration());
        textContent = Songs.get(position).getName().toString();
        textView.setText(textContent);
        textView.setSelected(true);

    }
});




next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=Songs.size()-1)
                {
                    position=position+1;
                }
                else {
                    position=0;
                }

                Uri uri = Uri.parse(Songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                pause.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());

                textContent = Songs.get(position).getName().toString();
                textView.setText(textContent);

            }
        });








    }
}