package demo.hjj.com.newsigndemo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.text.style.LineBackgroundSpan;
import android.util.DisplayMetrics;
import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener {
    private MaterialCalendarView mcv;
    private SameDayDecorator sameDayDecorator;
    private SignedDecorator signedDecorator;
    private UnSignedDecorator unSignedDecorator;
    private ArrayList<Integer> signList;
    private ArrayList<Integer> unSignList;
    private Date date;
    private String dateStr;
    private Date parse;
    private String selectDate;
    private ArrayList<Integer> normalList; //当月总共多少天的集合
    private int unSignNum; //没签到的数
    private int signNum;   //签到的数
    private Random rand;
    private int month;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        normalList = new ArrayList<>();
        signList = new ArrayList<>();
        unSignList = new ArrayList<>();
        rand = new Random();
        mcv = (MaterialCalendarView) findViewById(R.id.calendarView);
        //设置第一天是周日   最小日期 2017年1月1日， 形式 月   可切换成周
        mcv.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 1, 1))
                .setMaximumDate(new CalendarDay())
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        //设置年月的title
        mcv.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                StringBuffer buffer = new StringBuffer();
                int yearOne = day.getYear();
                int monthOne = day.getMonth() + 1;
                buffer.append(yearOne).append("年").append(monthOne).append("月");
                return buffer;
            }
        });
        //设置星期  默认 星期一 星期二.......
        mcv.setWeekDayLabels(new String[]{"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"});

        sameDayDecorator = new SameDayDecorator();
        //日期点击事件
        mcv.setOnDateChangedListener(this);
        mcv.setOnMonthChangedListener(this);
        date = new Date();
        month = date.getMonth();
        dateStr = TimeUtil.date2String(date, "yyyy-MM-dd");
        String[] split = dateStr.split("-");
        String s = split[2];
        day = Integer.parseInt(s);
        parse = TimeUtil.string2Date(dateStr, "yyyy-MM-dd"); //获取这个是为了和后面的date判断是否在同一天。直接用date不行 或者自行写一个方法去判断两个date是否在同一天
        getData(day);
        initData();
    }


    //获取一个集合 模拟数据
    private void getData(int day) {
        normalList.clear();
        for(int i=1;i<day+1;i++){
            normalList.add(i);
        }
        unSignNum = day/3;
        Log.e("hjjjjj","unSignNum::"+unSignNum);
        signNum = day - unSignNum;
        Log.e("hjjjjj","signNum::"+signNum);
    }

    private void initData() {
        getUnSignList();
        getSignList();
        sameDayDecorator = new SameDayDecorator();
        //签到
        signedDecorator = new SignedDecorator();
        //未签到
        unSignedDecorator = new UnSignedDecorator();
        mcv.addDecorators(sameDayDecorator, signedDecorator, unSignedDecorator);

    }
    private void getUnSignList() {
        unSignList.clear();
        for(int i =0;i<unSignNum;i++){
            int i1 = rand.nextInt(normalList.size());
            unSignList.add(normalList.get(i1));
            normalList.remove(i1);
        }
    }

    private void getSignList() {
        signList.clear();
        for(int i =0;i<normalList.size();i++){
            signList.add(normalList.get(i));
        }
    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        if(signList.contains(date.getDay())){
            ToastUtil.showMsg("无需重复签到！");
        }
        if(unSignList.contains(date.getDay())){
            ToastUtil.showMsg("补签成功");
            signList.add(date.getDay());
            unSignList.remove((Integer) date.getDay());
            mcv.addDecorator(signedDecorator);
            mcv.addDecorator(unSignedDecorator);
        }
        widget.setDateSelected(date, false);
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        if (date.getMonth() + 1 < 10) {
            selectDate = date.getYear() + "0" + (date.getMonth() + 1);
        } else {
            selectDate = date.getYear() + "" + (date.getMonth() + 1);
        }
        ToastUtil.showMsg(selectDate);
        mcv.removeDecorators();
        if(date.getMonth()==month){
            getData(day);
            initData();
        }else{
            getData(TimeUtil.getDaysOfMonth(date.getYear(),date.getMonth()+1));
            initData();
        }
    }



    //今天
    public class SameDayDecorator implements DayViewDecorator {
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if (day.getDate().equals(parse)) {
                return true;
            }
            return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new TodayBGSpan());
            view.addSpan(new TodaySpan());
            view.addSpan(new ForegroundColorSpan(Color.TRANSPARENT));
        }
    }




    //签到了的
    public class SignedDecorator implements DayViewDecorator {
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if (day.getDate().equals(parse)) {
                return false;
            }
            return signList.contains(day.getDay());
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new SignedSpan());
            view.addSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")));
        }
    }

    public class SignedSpan implements LineBackgroundSpan {
        @Override
        public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#fdc132"));
            c.drawCircle((right - left) / 2, (bottom - top) / 2, dip2px(18), paint);
        }
    }

    //补签
    public class UnSignedDecorator implements DayViewDecorator {
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if (day.getDate().equals(parse)) {
                return false;
            }
            return unSignList.contains(day.getDay());
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new unSginedSpan());
            view.addSpan(new unSignedTextSpan());
            view.addSpan(new ForegroundColorSpan(Color.TRANSPARENT));

        }
    }

    public class unSginedSpan implements LineBackgroundSpan {
        @Override
        public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
            Paint paint = new Paint();
            paint.setAntiAlias(true); //消除锯齿#ff841a
            paint.setColor(Color.parseColor("#ff841a"));
            c.drawCircle((right - left) / 2, (bottom - top) / 2, dip2px(18), paint);
        }
    }

    public class unSignedTextSpan implements LineBackgroundSpan {

        @Override
        public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
            Paint paint = new Paint();
            paint.setTextSize(dip2px(14));
            paint.setColor(Color.parseColor("#ffffff"));
            c.drawText("补", (right - left) / 2 - dip2px(7), (bottom - top) / 2 + dip2px(7), paint);
        }
    }


    public class TodaySpan implements LineBackgroundSpan {

        @Override
        public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
            Paint paint = new Paint();
            paint.setTextSize(dip2px(14));
            paint.setColor(Color.parseColor("#ff4141"));
            c.drawText("今", (right - left) / 2 - dip2px(7), (bottom - top) / 2 + dip2px(7), paint);
        }
    }

    public class TodayBGSpan implements LineBackgroundSpan {

        @Override
        public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
            Paint paint = new Paint();
            paint.setAntiAlias(true); //消除锯齿
            paint.setStyle(Paint.Style.STROKE);//绘制空心圆或 空心矩形
            int ringWidth = dip2px(1);//圆环宽度
            //绘制圆环
            paint.setColor(Color.parseColor("#ff4141"));
            paint.setStrokeWidth(ringWidth);
            c.drawCircle((right - left) / 2, (bottom - top) / 2, dip2px(18), paint);
        }

    }


    public int dip2px(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }
}
