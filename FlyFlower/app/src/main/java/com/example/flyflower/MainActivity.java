package com.example.flyflower;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //读取诗词库
        Workbook book = null;
        try {
            book = Workbook.getWorkbook(getAssets().open("PoetryLibrary.xls"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

        final Sheet sheet = book.getSheet(0);



        //为搜索按钮设置事件监听器
        Button searchbutton=findViewById(R.id.searchbutton);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //获取用户输入的关键字
                EditText inputbox=findViewById(R.id.inputbox);
                String key=inputbox.getText().toString();
                if(key.equals("")){
                    Toast.makeText(getApplicationContext(), "输入不能为空!", Toast.LENGTH_SHORT).show();
                }else {

                        //首先清空原布局只保留搜索框和搜索按钮
                        LinearLayout layout=findViewById(R.id.layout);
                        LinearLayout sublayout=findViewById(R.id.sublayout);
                        layout.removeAllViews();
                        layout.addView(sublayout);

                        //搜索含有关键字的诗词
                        int amount = sheet.getRows();
                        for (int i = 0; i < amount; i++) {
                            Cell cell = sheet.getCell(2,i);
                            String poetry = cell.getContents();

                            if(poetry.contains(key)){
                                String title=sheet.getCell(0,i).getContents();
                                String author=sheet.getCell(1,i).getContents();

                                SpannableStringBuilder titlestyle = new SpannableStringBuilder(title);
                                SpannableStringBuilder authorstyle = new SpannableStringBuilder(author);
                                SpannableStringBuilder poetrystyle = new SpannableStringBuilder(poetry);

                                SpannableStringBuilder kg = new SpannableStringBuilder("\t\t");

                               ///设置字体颜色
                                titlestyle.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")),
                                        0,title.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                authorstyle.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")),
                                        0,author.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                poetrystyle.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")),
                                        0,poetry.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);


                                //改变关键字的颜色
                                poetrystyle.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")),
                                poetry.indexOf(key), poetry.indexOf(key) + key.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                                SpannableStringBuilder str=kg.append(poetrystyle).append("\t").append("<<").append(titlestyle).append(">>").append(authorstyle);
                                //设置字体大小
                                str.setSpan(new AbsoluteSizeSpan(17,true),0,str.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                                TextView view=new TextView(getApplicationContext());
                                view.setText(str);
                                view.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));



                                layout.addView(view);


                                TextView line=new TextView(getApplicationContext());
                                line.setWidth(1);
                                layout.addView(line);




                            }


                        }



                }

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
