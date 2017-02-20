package com.example.makke.tic_202541;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.makke.tic_202541.R.id.board;
import static com.example.makke.tic_202541.R.id.musicbtn;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    //music player

    MediaPlayer bgMusic;

    TableLayout table;
    boolean turn = true;

    // shared prefs
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Nums = "numKey";
    public static final String Colors = "colorsKey";
    public static final String Ai = "aiKey";

    // pelintilanne



    SharedPreferences sharedPreferences;


    boolean nums = false;    //laitetaanko numero ruutuihin
    boolean colors = false;  //laitetaanko värit ruutuihin
    boolean ai = false;      // ai päälle / pois


    int moves = 0;
    List<Button> buttons = new ArrayList<Button>();
    int[] lauta = new int[9]; //yksiulotteinen simppeli lauta. jälkihuomautuksena sanottakoon että ei ollut paras ratkaisu

    boolean victory1 = false;
    boolean victory2 = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        // musiikkisoitin
        bgMusic = MediaPlayer.create(this, R.raw.music);
        bgMusic.setLooping(true);


        create_board();

        // switchit
        // musiikki
        Switch musicSwitch = (Switch) findViewById(R.id.musicbtn);

        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    //Toast.makeText(MainActivity.this, "Music ON", Toast.LENGTH_SHORT).show();
                    bgMusic.start();

                } else {
                    //Toast.makeText(MainActivity.this, "Music OFF", Toast.LENGTH_SHORT).show();
                    bgMusic.pause();

                }

            }
        });



        // Värit
        Switch colorSwitch = (Switch) findViewById(R.id.colorbtn);

        boolean clrs = sharedPreferences.getBoolean(Colors, false);

        if (clrs) {
            colorSwitch.setChecked(true);
        }

        colorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    //Toast.makeText(MainActivity.this, "Colors ON", Toast.LENGTH_SHORT).show();
                    colors = true;
                    editor.putBoolean(Colors, true);
                } else {
                    //Toast.makeText(MainActivity.this, "Colors OFF", Toast.LENGTH_SHORT).show();
                    colors = false;
                    editor.putBoolean(Colors, false);
                }
                editor.commit();

            }
        });

        // laudan numerot
        Switch numSwitch = (Switch) findViewById(R.id.numbtn);

        boolean nmbrs = sharedPreferences.getBoolean(Nums, false);
        if (nmbrs) {
            numSwitch.setChecked(true);
            //cleartbl(table);
        }

        numSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    //Toast.makeText(MainActivity.this, "Numbers ON", Toast.LENGTH_SHORT).show();
                    nums = true;
                    editor.putBoolean(Nums, true);
                    cleartbl(table);
                } else {
                    //Toast.makeText(MainActivity.this, "Numbers OFF", Toast.LENGTH_SHORT).show();
                    nums = false;
                    editor.putBoolean(Nums, false);
                    cleartbl(table);
                }
                editor.commit();
            }
        });


        // AI
        Switch aiSwitch = (Switch) findViewById(R.id.aibtn);

        boolean aib = sharedPreferences.getBoolean(Ai, false);
        if (aib) {
            aiSwitch.setChecked(true);
        }

        aiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Toast.makeText(MainActivity.this, "AI ON", Toast.LENGTH_SHORT).show();
                    ai = true;
                    editor.putBoolean(Ai, true);
                    cleartbl(table);
                } else {
                    Toast.makeText(MainActivity.this, "AI OFF", Toast.LENGTH_SHORT).show();
                    ai = false;
                    editor.putBoolean(Ai, false);
                    cleartbl(table);
                }
                editor.commit();
            }
        });



    }





    @Override
    public void onClick(View v) {



        moves++;
        Button b = buttons.get(v.getId());




        if (turn == true) {
            v.setClickable(false);
            if (colors == true) {
                v.setBackgroundColor(0xFFFF0000);
            }
            v.setTag("X");
            b.setText("X");
            lauta[b.getId()] = 1;


            turn = false;
            chkwin();

            if (ai == true && moves < 5 && !victory1) {

                boolean move = false;

                while (!move) {

                    Random rng = new Random();
                    int r = rng.nextInt(9);



                    if (lauta[r] != 1 && lauta[r] != 2) {

                        lauta[r] = 2;

                        Button b2 = buttons.get(r);
                        b2.setText("O");
                        if (colors == true) {
                            b2.setBackgroundColor(0xFF4853a7);
                        }
                        b2.setClickable(false);
                        //Toast.makeText(MainActivity.this, "O "+r, Toast.LENGTH_SHORT).show();
                        move = true;
                    }

                }

                chkwin();
                turn = true;

            }


        } else if (ai == false) {
            v.setClickable(false);
            if (colors == true) {
                v.setBackgroundColor(0xFF4853a7);
            }
            v.setTag("O");
            b.setText("O");
            lauta[b.getId()] = 2;
            //Toast.makeText(MainActivity.this, "O", Toast.LENGTH_SHORT).show();
            turn = true;

            chkwin();
        }






    }



    public void chkwin() {



        // ei kovin kaunis tarkistusmenetelmä

        // testataan vaaka rivit


        if (lauta[0] == 1 && lauta[1] == 1 && lauta[2] == 1) {

            //Toast.makeText(MainActivity.this, "voitto X", Toast.LENGTH_SHORT).show();
            victory1 = true;

        }

        if (lauta[0] == 2 && lauta[1] == 2 && lauta[2] == 2) {

            //Toast.makeText(MainActivity.this, "voitto O", Toast.LENGTH_SHORT).show();
            victory2 = true;

        }

        if (lauta[3] == 1 && lauta[4] == 1 && lauta[5] == 1) {

            //Toast.makeText(MainActivity.this, "voitto X", Toast.LENGTH_SHORT).show();
            victory1 = true;

        }

        if (lauta[3] == 2 && lauta[4] == 2 && lauta[5] == 2) {

            //Toast.makeText(MainActivity.this, "voitto O", Toast.LENGTH_SHORT).show();
            victory2 = true;

        }

        if (lauta[6] == 1 && lauta[7] == 1 && lauta[8] == 1) {

            //Toast.makeText(MainActivity.this, "voitto X", Toast.LENGTH_SHORT).show();
            victory1 = true;

        }

        if (lauta[6] == 2 && lauta[7] == 2 && lauta[8] == 2) {

            //Toast.makeText(MainActivity.this, "voitto O", Toast.LENGTH_SHORT).show();
            victory2 = true;

        }

        // testataan pystyrivit

        if (lauta[0] == 1 && lauta[3] == 1 && lauta[6] == 1) {

            //Toast.makeText(MainActivity.this, "voitto X", Toast.LENGTH_SHORT).show();
            victory1 = true;

        }

        if (lauta[0] == 2 && lauta[3] == 2 && lauta[6] == 2) {

            //Toast.makeText(MainActivity.this, "voitto O", Toast.LENGTH_SHORT).show();
            victory2 = true;

        }

        if (lauta[1] == 1 && lauta[4] == 1 && lauta[7] == 1) {

            //Toast.makeText(MainActivity.this, "voitto X", Toast.LENGTH_SHORT).show();
            victory1 = true;

        }

        if (lauta[1] == 2 && lauta[4] == 2 && lauta[7] == 2) {

            //Toast.makeText(MainActivity.this, "voitto O", Toast.LENGTH_SHORT).show();
            victory2 = true;

        }

        if (lauta[2] == 1 && lauta[5] == 1 && lauta[8] == 1) {

            //Toast.makeText(MainActivity.this, "voitto X", Toast.LENGTH_SHORT).show();
            victory1 = true;

        }

        if (lauta[2] == 2 && lauta[5] == 2 && lauta[8] == 2) {

            //Toast.makeText(MainActivity.this, "voitto O", Toast.LENGTH_SHORT).show();
            victory2 = true;

        }

        // testataan vinot

        if (lauta[0] == 1 && lauta[4] == 1 && lauta[8] == 1) {

            //Toast.makeText(MainActivity.this, "voitto X", Toast.LENGTH_SHORT).show();
            victory1 = true;

        }

        if (lauta[0] == 2 && lauta[4] == 2 && lauta[8] == 2) {

            //Toast.makeText(MainActivity.this, "voitto O", Toast.LENGTH_SHORT).show();
            victory2 = true;

        }

        if (lauta[2] == 1 && lauta[4] == 1 && lauta[6] == 1) {

            //Toast.makeText(MainActivity.this, "voitto X", Toast.LENGTH_SHORT).show();
            victory1 = true;

        }

        if (lauta[2] == 2 && lauta[4] == 2 && lauta[6] == 2) {

            //Toast.makeText(MainActivity.this, "voitto O", Toast.LENGTH_SHORT).show();
            victory2 = true;

        }


        // loppusilaus
        if (victory1 == true || victory2 == true) {
            if (victory1 == true) {
                Toast.makeText(MainActivity.this, "X WINS!", Toast.LENGTH_SHORT).show();
            } else if (victory2 == true) {
                Toast.makeText(MainActivity.this, "O WINS!", Toast.LENGTH_SHORT).show();
            }
            disableButtons();

        } else if (moves == 9 || moves == 5 && ai == true) {

            Toast.makeText(MainActivity.this, "DRAW", Toast.LENGTH_SHORT).show();
            disableButtons();
        }


    }

    // disabloidaan napit kun loppu on saapunut
    public void disableButtons() {

        for (Button b: buttons) {
            b.setClickable(false);
        }

    }

    // clear table and start new game
    public void cleartbl(View view) {

        table.removeAllViews();
        buttons.clear();
        lauta = new int[9];
        moves = 0;
        turn = true;
        create_board();
        victory1 = false;
        victory2 = false;
    }

    private void create_board() {


        int count = 0;
        Button button;
        String n = "n"+count;

        table = (TableLayout) findViewById(R.id.board);

        for (int i = 0; i < 3; i++) {

            TableRow row = new TableRow(this);

            TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT);


            for (int j = 0; j < 3; j++) {

                row.setLayoutParams(rowParams);



                button = new Button(this);

                button.setLayoutParams(new TableRow.LayoutParams(200, 300));
                button.setOnClickListener(this);
                button.setTag(count);
                button.setId(count);

                if (nums == true) {
                    button.setText("" + count);
                }

                // laitetaan vanha peli laudalle
                if (lauta[count] == 1) {
                    button.setText("X");
                    button.setClickable(false);
                } else if (lauta[count] == 2) {
                    button.setText("O");
                    button.setClickable(false);
                }

                buttons.add(button);


                row.addView(button);
                count++;
            }

            table.addView(row);

        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        table.removeAllViews();
        buttons.clear();



        // luetaan shared prefsejä
        if(sharedPreferences.getBoolean(Colors, false)) {
            colors = true;
        }
        if(sharedPreferences.getBoolean(Nums, false)) {
            nums = true;
            //cleartbl(table);
        }
        if(sharedPreferences.getBoolean(Ai, false)) {
            ai = true;

        }

        turn = sharedPreferences.getBoolean("turn", true);
        moves = sharedPreferences.getInt("moves", 0);

        // luetaan yksitellen laudan tilanne
        lauta[0] = sharedPreferences.getInt("n0", 0);
        lauta[1] = sharedPreferences.getInt("n1", 0);
        lauta[2] = sharedPreferences.getInt("n2", 0);
        lauta[3] = sharedPreferences.getInt("n3", 0);
        lauta[4] = sharedPreferences.getInt("n4", 0);
        lauta[5] = sharedPreferences.getInt("n5", 0);
        lauta[6] = sharedPreferences.getInt("n6", 0);
        lauta[7] = sharedPreferences.getInt("n7", 0);
        lauta[8] = sharedPreferences.getInt("n8", 0);

        Log.e("lauta0", ""+lauta[0]);
        Log.e("lauta1", ""+lauta[1]);
        Log.e("lauta2", ""+lauta[2]);

        create_board();

    }

    @Override
    protected void onPause() {
        super.onPause();
        bgMusic.pause();

        SharedPreferences lautaPrefs = this.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor2 = lautaPrefs.edit();

        // laitetaan yksitellen lauta jemmaan
        editor2.putInt("n0", lauta[0]);
        editor2.putInt("n1", lauta[1]);
        editor2.putInt("n2", lauta[2]);
        editor2.putInt("n3", lauta[3]);
        editor2.putInt("n4", lauta[4]);
        editor2.putInt("n5", lauta[5]);
        editor2.putInt("n6", lauta[6]);
        editor2.putInt("n7", lauta[7]);
        editor2.putInt("n8", lauta[8]);

        editor2.putBoolean("turn", turn);
        editor2.putInt("moves", moves);

        editor2.commit();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bgMusic.release();
    }
}