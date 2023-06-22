package com.example.blindmode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import java.util.Locale;
import android.content.Context;

public class MainActivity extends AppCompatActivity {
/*    EditText speechText;
    Button speechButton;
    ArrayList<String> speech;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speechText = findViewById(R.id.editText1);
        speechButton = findViewById(R.id.button1);

        try (SpeechClient speechClient = SpeechClient.create()) {

            speechButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent speechintent= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    RecognitionConfig config =
                            RecognitionConfig.newBuilder()
                                    .setEncoding(AudioEncoding.LINEAR16)
                                    .setSampleRateHertz(16000)
                                    .setLanguageCode("ar-XA")
                                    .build();

                }
            });



        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


    //Afnan: speech to text; detecting a speech using google and then write it into an editText.

    EditText speechText;
    Button speechButton;
    ArrayList<String> speech;
    ArrayList<String> changedSpeech;
    private TextToSpeech TTS;
    Button readSpeech;


    private static final int recognizer_result =1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speechText = findViewById(R.id.editText1);
        speechButton = findViewById(R.id.button1);

        speechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Afnan: creating an intent
                Intent speechintent= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                //Afnan: adding extra attributes to the intent
                speechintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechintent.putExtra(RecognizerIntent.EXTRA_PROMPT, "speech to text");
                //Afnan: defining Arabic as the language being detected.
                speechintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-XA");
                startActivityForResult(speechintent, recognizer_result);
            }
        });

        /*  speechText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changedSpeech= speechText.getText();
            }
        });*/

        //Afnan: text to speech intializing.
        TTS = textToSpeechInitialize(this,"ar");
        readSpeech = findViewById(R.id.button2);
        readSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak(TTS,speechText.getText().toString());
            }
        });
    }


    @Override
    //Afnan: during the activity of the speech to text
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //Afnan: during the receiving it has to match the same request and result codes.
        if(requestCode== recognizer_result && resultCode== RESULT_OK){
            speech = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            speechText.setText(speech.get(0).toString());

        }
        super.onActivityResult(requestCode, resultCode, data);

    }
    //Afnan: Text to Speech
    TextToSpeech textToSpeechInitialize(Context context, String language){
        return new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = TTS.setLanguage(new Locale(language));
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {Log.e("TTS", "Language Not Supported");}
                else { readSpeech.setEnabled(true); }
            } else { Log.e("TTS", "Initalization Failed"); }
        });
    }

    protected void onDestroy() {
        if (TTS != null) {
            TTS.stop();
            TTS.shutdown();
        }
        super.onDestroy();
    }

    //Afnan: speaking function.
    public static void speak(TextToSpeech mTTS,String textToBeSpeech){
        mTTS.speak(textToBeSpeech,TextToSpeech.QUEUE_FLUSH,null);
    }

}