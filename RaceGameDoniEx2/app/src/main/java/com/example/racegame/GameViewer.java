package com.example.racegame;

import static com.example.racegame.MainActivity.lati;
import static com.example.racegame.MainActivity.longi;
import static com.example.racegame.MainActivity.sharedPreferences;
import com.google.gson.Gson;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;

public class GameViewer extends AppCompatActivity {
    Records records;
    private ImageView car;
    private int lastIndexCrach;
    private MediaPlayer mediaPlayer;
    private ImageView heart1;
    private ImageView heart2;
    private ImageView heart3;
    private int indexLastStep;
    private ImageView[] obstacleImageViews;
    private int NUM_OBSTACLES;
    private Button moveLeftButton;
    private Button moveRightButton;
    private Runnable gameRunnable;
    long speed_time;
    private int score;
    private boolean isMovingLeft;
    private boolean isMovingRight;
    private RelativeLayout mainLayout;
    private Handler handler = new Handler();
    private int obstacleSpeed;
    private Point point;
    private int numLife;
    private Handler mHandler;
    private AlertDialog gameOverDialog;
    private Intent intent;
    private TextView scoreTytle;
    private boolean AlowBackPress;
    private Bundle bundle;
    SensorManager sensorManager;
    Sensor accelerometer;
    boolean speed;
    int mode;
    private boolean isPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        speed = sharedPreferences.getBoolean("speed", false);
        mode = sharedPreferences.getInt("mode", 1);
        if (speed) speed_time = 25;
        else speed_time = 90;
        records= new Records();
        create();

    }


    private void moveCarRight() {
        float carX = car.getX() + ((point.x / (2.27f)) - car.getWidth());
        if (carX + car.getWidth() > findViewById(R.id.main_layout).getWidth()) {
            return;
        }
        car.setX(carX);
        checkCollision();
    }

    private void checkCollision() {
        Rect carRect = new Rect();
        car.getHitRect(carRect);
        for (int i = 0; i < NUM_OBSTACLES; i++) {
            Rect obstacleRect = new Rect();
            obstacleImageViews[i].getHitRect(obstacleRect);

            if (Rect.intersects(carRect, obstacleRect)) {
                endGame(i);

            }
        }
    }

    private void moveCarLeft() {
        float carX = car.getX() - ((point.x / 2.27f) - car.getWidth());
        Log.e("", "" + point.x);
        if (carX < 0) {
            return;
        }
        car.setX(carX);
        checkCollision();
    }


    private void startGameLoop() {
        gameRunnable = new Runnable() {
            @Override
            public void run() {
                if (isPlaying) {
                    moveObstacle();
                    checkCollision();

                    handler.postDelayed(this, speed_time);
                }
            }
        };
        handler.postDelayed(gameRunnable, speed_time);
    }


    private void moveObstacle() {

        for (int i = 0; i < NUM_OBSTACLES; i++) {
            float obstacleY = obstacleImageViews[i].getY() + 10;
            if (obstacleY > findViewById(R.id.main_layout).getHeight()) {
                resetObstacle(obstacleImageViews[i]);

                updateScore();
            } else {
                obstacleImageViews[i].setY(obstacleY);
            }

        }


    }

    private void resetObstacle(ImageView obstacle) {
        Random random;

        random = new Random();
        int maxX = (int) (findViewById(R.id.main_layout).getWidth() - obstacle.getWidth());
        int randomX = random.nextInt(maxX);
        obstacle.setX(setXobstcale());
        obstacle.setY(-obstacle.getHeight());


    }

    private void updateScore() {
        score++;
        scoreTytle.setText("Score: " + score);
    }

    private void endGame(int index) {
        if (lastIndexCrach != index) {
            if (numLife == 3) {
                mediaPlayer.start();
                numLife -= 1;
                lastIndexCrach = index;
                mainLayout.removeView(heart3);
            } else if (numLife == 2) {
                mediaPlayer.start();
                numLife -= 1;
                lastIndexCrach = index;
                mainLayout.removeView(heart2);

            } else {
                mediaPlayer.start();
                numLife -= 1;
                lastIndexCrach = index;
                mainLayout.removeView(heart1);
                handler.removeCallbacks(gameRunnable);
                isPlaying = false;
                Toast.makeText(this, "Game over! Your score was " + score, Toast.LENGTH_SHORT).show();
                saveGameScore(score);
                viewGameOverAlert();

            }
        }
    }

    Records     RecordsList;     Gson gson;
    private void saveGameScore(int score) {
        RecordManager recordManager = new RecordManager(this);

        List<Record> records = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (recordManager.hasRecord(i)) {
                int scored = recordManager.getScore(i);
                float longi = recordManager.getlongi(i);
                float lati = recordManager.getlati(i);
                Record record = new Record(scored, lati, longi);
                records.add(record);
            }
        }
        Collections.sort(records, new Comparator<Record>() {
            @Override
            public int compare(Record r1, Record r2) {
                return Integer.compare(r1.getScore(), r2.getScore());
            }
        });
        int insertIndex = 0;
        for (int i = 0; i < records.size(); i++) {
            Record record = records.get(i);
            if (score < record.getScore()) {
                insertIndex = i;
                break;
            } else {
                insertIndex = i + 1;
            }
        }
// Insert the new record at the index found above
        Record newRecord = new Record(score, (float) lati, (float) longi);
        records.add(insertIndex, newRecord);


        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < records.size(); i++) {
            Record record = records.get(i);
            editor.putInt("score" + i, record.getScore());
            editor.putFloat("lati" + i, record.getlati());
            editor.putFloat("longi" + i, record.getlongi());
        }
        editor.apply();


    private int setXobstcale() {
        Random random = new Random();
        int randomX = random.nextInt(3);
        switch (randomX) {
            case 0:
                return 100;
            case 1:
                return 500;
            case 2:
                return 870;

        }
        return 0;
    }

    private void viewGameOverAlert() {


        // create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.game_over_dialog, null);
        builder.setCancelable(false);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();

        // find buttons in the dialog
        Button newGameButton = dialogView.findViewById(R.id.new_game_button);
        Button exitButton = dialogView.findViewById(R.id.exit_button);

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
                AlowBackPress = false;
                onBackPressed();



            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // exit game
                finish();
                alertDialog.dismiss();
                onBackPressed();
            }
        });

        // show the dialog
        alertDialog.show();
    }

    private void startNewGame() {

        GameViewer.super.onBackPressed();

        startActivity(intent);


    }

    @Override
    public void onBackPressed() {
        if (AlowBackPress) {
            super.onBackPressed();
        } else {

            isPlaying = true;
            create();
        }
    }

    boolean isCarOnLeft = false, AlreadyCentered = true;

    public void create() {
        AlowBackPress = true;


        setContentView(R.layout.game_viewer);
        moveLeftButton = findViewById(R.id.left_button);
        moveRightButton = findViewById(R.id.right_button);

        if (mode == 2) {
            sensorManager.registerListener(new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    float x = event.values[0];
                    if (x < -3) {
                        AlreadyCentered = false;
                        moveCarRight();
                        isCarOnLeft = true;
                    } else if (x > 3) {
                        AlreadyCentered = false;
                        isCarOnLeft = false;
                        moveCarLeft();
                    } else {
                        if (!AlreadyCentered) {
                            if (isCarOnLeft)
                                moveCarLeft();
                            else moveCarRight();
                        }
                        AlreadyCentered = true;
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
            }, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            moveLeftButton.setVisibility(View.INVISIBLE);
            moveRightButton.setVisibility(View.INVISIBLE);
        }

        numLife = 3;
        isPlaying = true;
        car = findViewById(R.id.car);
        heart1 = findViewById(R.id.heart1);
        heart2 = findViewById(R.id.heart2);
        heart3 = findViewById(R.id.heart3);
        scoreTytle = findViewById(R.id.score_textview);
        scoreTytle.setTextColor(getResources().getColor(R.color.red));
        mediaPlayer = MediaPlayer.create(this, R.raw.car_crash);
        lastIndexCrach = -1;
        score = 0;
        obstacleSpeed = 5;
        point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        indexLastStep = 4;
        NUM_OBSTACLES = 5;
        obstacleImageViews = new ImageView[NUM_OBSTACLES];
        mainLayout = (RelativeLayout) findViewById(R.id.main_layout);

        for (int i = 0; i < NUM_OBSTACLES; i++) {
            ImageView obstacleImageView = new ImageView(this);
            obstacleImageView.setImageResource(R.drawable.pngegg);
            obstacleImageViews[i] = obstacleImageView;
            obstacleImageViews[i].setLayoutParams(new RelativeLayout.LayoutParams(90, 90));
            obstacleImageViews[i].setY(-point.y + (i * 500) - car.getWidth());


            obstacleImageViews[i].setX(setXobstcale());
            mainLayout.addView(obstacleImageView);
        }


        moveLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveCarLeft();
            }
        });

        moveRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveCarRight();
            }
        });


        startGameLoop();


    }
}



