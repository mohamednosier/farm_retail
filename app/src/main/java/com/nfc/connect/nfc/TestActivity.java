package com.nfc.connect.nfc;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.connect.nfc.R;
import com.jesusm.holocircleseekbar.lib.HoloCircleSeekBar;
import com.innovattic.rangeseekbar.RangeSeekBar;
//import com.example.peng.
//import com.jesusm.holocircleseekbar.lib.HoloCircleSeekBar;
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final HoloCircleSeekBar picker = (HoloCircleSeekBar) findViewById(R.id.pp);
//        HoloCircleSeekBar pp=(HoloCircleSeekBar)findViewById(R.id.pic)
//        HoloSeekBar picker = (HoloSeekBar) findViewById(R.id.picker);
        picker.getValue();
        final RangeSeekBar rangeSeekBar=(RangeSeekBar)findViewById(R.id.rangeSeekBar);
        rangeSeekBar.setMax(300);
//        rangeSeekBar.setMaxThumbValue(200);

        Button click=(Button)findViewById(R.id.click);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(TestActivity.this," her getMinRange"+rangeSeekBar.getMinRange(),Toast.LENGTH_LONG).show();
                Toast.makeText(TestActivity.this," her4 getMaxThumbValue"+rangeSeekBar.getMaxThumbValue(),Toast.LENGTH_LONG).show();
                Toast.makeText(TestActivity.this," her5 getMinThumbValue"+rangeSeekBar.getMinThumbValue(),Toast.LENGTH_LONG).show();
                Toast.makeText(TestActivity.this," her5 picker.getValue()"+picker.getValue(),Toast.LENGTH_LONG).show();


            }
        });
        rangeSeekBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestActivity.this," her0 getMaxThumbValue"+rangeSeekBar.getMaxThumbValue(),Toast.LENGTH_LONG).show();
                Toast.makeText(TestActivity.this," her0 getMinThumbValue"+rangeSeekBar.getMinThumbValue(),Toast.LENGTH_LONG).show();

            }
        });
        rangeSeekBar.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                Toast.makeText(TestActivity.this," her1 getMaxThumbValue"+rangeSeekBar.getMaxThumbValue(),Toast.LENGTH_LONG).show();
                Toast.makeText(TestActivity.this," her1 getMinThumbValue"+rangeSeekBar.getMinThumbValue(),Toast.LENGTH_LONG).show();

                return false;
            }
        });
        rangeSeekBar.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Toast.makeText(TestActivity.this," her2 getMaxThumbValue"+rangeSeekBar.getMaxThumbValue(),Toast.LENGTH_LONG).show();
                Toast.makeText(TestActivity.this," her2 getMinThumbValue"+rangeSeekBar.getMinThumbValue(),Toast.LENGTH_LONG).show();

            }
        });
        rangeSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Toast.makeText(TestActivity.this," her3 getMaxThumbValue"+rangeSeekBar.getMaxThumbValue(),Toast.LENGTH_LONG).show();
//                Toast.makeText(TestActivity.this," her4 getMinThumbValue"+rangeSeekBar.getMinThumbValue(),Toast.LENGTH_LONG).show();

                return false;
            }
        });
//        rangeSeekBar.setOnRangeSeekBarChangeListener(new O)
       }
}
