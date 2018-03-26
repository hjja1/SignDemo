#签到Demo 

   compile 'com.prolificinteractive:material-calendarview:1.4.3'

支持自定义日期的各个背景
![demoImg](https://github.com/hjja1/SignDemo/blob/master/image/device-2018-03-26-152829.png)


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
   //日期点击监听
   
      mcv.setOnDateChangedListener(this);
   //月份切换监听，在方法里请求接口 目前demo中用的随机数模拟数据。所以数据不固定
   
      mcv.setOnMonthChangedListener(this)；
  
  //当前时间的Decorator，通过不同的span来修改其背景。具体步骤如下 
  
  
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
    
    
//字体span
         
         public class TodaySpan implements LineBackgroundSpan {

    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
            Paint paint = new Paint();
            paint.setTextSize(dip2px(14));
            paint.setColor(Color.parseColor("#ff4141"));
            c.drawText("今", (right - left) / 2 - dip2px(7), (bottom - top) / 2 + dip2px(7), paint);
        }
    }
    
    
//背景Span

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
    
    
 //最后将所有的Decorator放进去。
 
     mcv.addDecorators(sameDayDecorator.....);
 
详细内容请看demo

