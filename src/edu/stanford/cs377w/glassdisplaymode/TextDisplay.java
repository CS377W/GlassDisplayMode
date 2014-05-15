package edu.stanford.cs377w.glassdisplaymode;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;



public class TextDisplay extends Activity {

   private static final int DISPLAY_MODE = 0;
   // A list of text
   private ArrayList<String> textList = new ArrayList<String>();
   private ArrayList<Integer> colorList = new ArrayList<Integer>(
         Arrays.asList(0xff37dd1c, 0xff37dd1c, 0xff37dd1c));
   private ArrayList<Integer> backgroundList = new ArrayList<Integer>(
         Arrays.asList(0xff000000, 0xffffffff));
   
   private int highlightColor;
   private int backgroundColor;
   // Current text index in list
   private int currentTextIndex = 0;

   // Highlight index in current line
   private int currentHighlightIndex = 0;
   
   private boolean playLeft = true;

   TextView tv1;
   TextView tv2;
   TextView tv3;
   private int runCount = 0;
   private Handler handler = new Handler();

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_text_display);

      
      switch (DISPLAY_MODE) {
         
      case 0: 
         backgroundColor = backgroundList.get(0);
         highlightColor = colorList.get(0);
         break;
      case 1:
         backgroundColor = backgroundList.get(0);
         highlightColor = colorList.get(1);
         break;
      case 2:
         backgroundColor = backgroundList.get(0);
         highlightColor = colorList.get(2);
         break;
      case 3:
         backgroundColor = backgroundList.get(1);
         highlightColor = colorList.get(0);
         break;
      case 4:
         backgroundColor = backgroundList.get(1);
         highlightColor = colorList.get(1);
         break;
      case 5:
         backgroundColor = backgroundList.get(1);
         highlightColor = colorList.get(2);
         break;
      }
      
      // Initialize views
      //findViewById(R.id.wrapper).getBackground().setAlpha(0);;
      tv1 = (TextView) findViewById(R.id.text1);
      tv2 = (TextView) findViewById(R.id.text2);
      tv1.setTypeface(null, Typeface.BOLD);
      tv2.setTypeface(null, Typeface.BOLD);
      // Initialize dummy text
      textList.add("This is a lyric 1");
      textList.add("This is a lyric 2 yeah yeah");
      textList.add("This is a lyric 3");

      currentTextIndex = runCount % 3;

      // Set text views with initial text
      tv1.setText(textList.get(0));
      tv2.setText(textList.get(0));
      //setTextViews();

      // Run a repeated task
      highlightRunnable.run();

   }

   private Runnable highlightRunnable = new Runnable() {
      
      private TextView tv;
      public void run() {

         TextView tv = (playLeft) ? tv1 : tv2;
         
         // When done highlighting this text
         if (currentHighlightIndex > textList.get(currentTextIndex).length()) {
            currentHighlightIndex = 0;
            runCount++;

            // Switch text views
            currentTextIndex = runCount % 3;
            
            final TextView nextTV = tv;
            handler.postDelayed(new Runnable(){
               @Override
               public void run() {
                  nextTV.setText(textList.get((currentTextIndex + 1) % 3));
               }
            }, 1000);
            
            
            playLeft = !playLeft;
            handler.postDelayed(this, 500); // pause a little when done
            return;

         }
         
 
         // Highlight the current text until the highlight index
         highlight(tv, currentHighlightIndex++);
         
         handler.postDelayed(this, 100); // repeat TODO: change time to variable
      }
   };

   // Highlight the middle textView
   private void highlight(TextView tv, int index) {
      Spannable text = new SpannableStringBuilder(
            textList.get(currentTextIndex));
      setSpan(text, index);
      tv.setText(text);
   }

   // Set the top and bottom textViews
   private void setTextViews() {
      tv1.setText(textList.get((currentTextIndex + 2) % 3));
      tv3.setText(textList.get((currentTextIndex + 1) % 3));
   }

   // Separate a line of text into two colors
   private void setSpan(Spannable text, int index) {
      int highlightColor = 0xff37dd1c;
      int remainingColor = 0xffffffff;
      ForegroundColorSpan highlight = new ForegroundColorSpan(highlightColor);
      ForegroundColorSpan remaining = new ForegroundColorSpan(remainingColor);
      if (index >= text.length()) {
         text.setSpan(highlight, 0, text.length(), 0);
      } else {
         text.setSpan(highlight, 0, index, 0);
         text.setSpan(remaining, index, text.length(), 0);
      }

   }

}