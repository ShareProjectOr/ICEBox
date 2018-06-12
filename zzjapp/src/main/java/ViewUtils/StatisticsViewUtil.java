package ViewUtils;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhazhijiguanlixitong.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WH on 2017/7/25.
 */

public class StatisticsViewUtil {
    private Context mContext;
    private DrawOrder[] mDrawOrders = {DrawOrder.BAR, DrawOrder.BUBBLE,
            DrawOrder.CANDLE, DrawOrder.LINE, DrawOrder.SCATTER};
    private List<ScatterDataSet> mScatterSets;
    private List<LineDataSet> mLineSets;

    public StatisticsViewUtil(Context context) {
        mContext = context;
    }

    public void draw(CombinedChart mChart, List<LineDataSet> sets, List<String> xDatas) {
        mChart = getCombinedChart(mChart);
        setAxisX(mChart);
        setAxisY(mChart);
        CombinedData combinedData = new CombinedData(xDatas);
        if (mScatterSets != null) {
            ScatterData data = new ScatterData();
            for (int i = 0; i < mScatterSets.size(); i++) {
                data.addDataSet(mScatterSets.get(i));
            }
            combinedData.setData(data);
        }
        if (mLineSets != null) {
            LineData data = new LineData();
            for (int i = 0; i < mLineSets.size(); i++) {
                data.addDataSet(mLineSets.get(i));
            }
            combinedData.setData(data);
        }
        mChart.setData(combinedData);
        mChart.animateX(2000);
        mChart.invalidate();
    }

    public void setmScatterSets(List<ScatterDataSet> scatterSets) {
        mScatterSets = scatterSets;
    }

    public void setmLineSets(List<LineDataSet> lineSets) {
        mLineSets = lineSets;
    }

    public CombinedChart getCombinedChart(final CombinedChart mChart) {
        mChart.setDescription(mContext.getString(R.string.riqi));
        mChart.setNoDataText(mContext.getResources().getString(R.string.avgTime));
        mChart.setNoDataTextDescription(mContext.getResources().getString(R.string.avgTime));
        mChart.setPinchZoom(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(true);
        mChart.setDrawBarShadow(true);
        mChart.setDrawHighlightArrow(true);
        mChart.setHighlightIndicatorEnabled(true);
        mChart.setVerticalScrollBarEnabled(false);
        mChart.setScaleEnabled(true);
        mChart.setTouchEnabled(true);
        mChart.getAxisRight().setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mChart.setNoDataText(mContext.getString(R.string.nin_zanwushuju));
//                    mChart.setDescription(null);
                mChart.invalidate();
//                    mChart.refreshDrawableState();
            }
        }, 0);
        return mChart;
    }

    private void setAxisY(CombinedChart mChart) {
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
    }

    private void setAxisX(CombinedChart mChart) {
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setTextColor(mContext.getResources().getColor(R.color.black_overlay));
        xAxis.setAxisLineColor(mContext.getResources().getColor(R.color.black_overlay));
        xAxis.setDrawGridLines(true);
        xAxis.setGridColor(mContext.getResources().getColor(R.color.black_overlay));
    }

    public LineDataSet initLineDataSet(List<Integer> datas, int color, boolean mode, String title) {
        ArrayList<Entry> entries = getEntries(datas);
        LineDataSet lineDataSet = new LineDataSet(entries, title);
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleSize(4f);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(9f);
        lineDataSet.setDrawValues(true);
        lineDataSet.setValueTextColor(color);
        //设置折线图填充
        lineDataSet.setDrawFilled(mode);
        //线模式为圆滑曲线（默认折线）
        lineDataSet.setDrawCubic(true);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        //设置平滑度
        lineDataSet.setCubicIntensity(0.2f);
        return lineDataSet;
    }

    public ScatterDataSet initScatterSets(List<Integer> datas, int color, boolean mode, String title) {
        ArrayList<Entry> entries = getEntries(datas);
        ScatterDataSet scatterDataSet = new ScatterDataSet(entries, title);
        scatterDataSet.setColor(color);
        scatterDataSet.setValueTextSize(9f);
        scatterDataSet.setDrawValues(true);
        scatterDataSet.setValueTextColor(color);
        scatterDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        return scatterDataSet;
    }

    private ArrayList<Entry> getEntries(List<Integer> datas) {
        ArrayList<Entry> entries = new ArrayList<Entry>();
        for (int i = 0; i < datas.size(); i++) {
            entries.add(new Entry(datas.get(i), i));
        }
        return entries;
    }


    /*
        绘制饼状图
    */
    public PieChart initPieChart(PieChart mChart, String description, PieData pieData) {
        mChart.setDescription(description);
//        mChart.setDescriptionTextSize(20);
//        mChart.setDescriptionColor(Color.BLACK);
//        mChart.setDescriptionPosition(mChart.getX() + 450f, mChart.getY() + 50f);
        mChart.setHoleColorTransparent(true);
        mChart.setTransparentCircleRadius(0f);
        mChart.setHighlightEnabled(true);
        mChart.setDragDecelerationFrictionCoef(0.95f);
//        mChart.setHoleRadius(60f);
        mChart.setDrawMarkerViews(true);
        mChart.setHoleRadius(0);//实心圆
        mChart.setDrawCenterText(true);
        mChart.setDrawHoleEnabled(false);
        mChart.setRotationEnabled(true);// 可以手动旋转  
        mChart.setRotationAngle(90);
        mChart.setNoDataText(mContext.getString(R.string.meiyoushujuxianshi));
        mChart.setUsePercentValues(true);//设置显示成比例
        mChart.setTouchEnabled(true);


        mChart.animateXY(2000, 2000);
        mChart.animateY(2000, Easing.EasingOption.EaseInOutQuad);
        mChart.setData(pieData);
        initLegeng(mChart);
        return mChart;
    }

    public void setOnPieChatClickListener(final PieChart pieChart, final Context context, final List<String> names) {
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Toast.makeText(context, names.get(e.getXIndex()) + ": " + (int) e.getVal() + "杯", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {
            }
        });
    }

    //初始化比列图
    private void initLegeng(PieChart mChar) {
        Legend mLegend = mChar.getLegend();
        mLegend.setEnabled(false);
//        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
//        mLegend.setForm(Legend.LegendForm.SQUARE);//设置比例块形状，默认为方块 
//        mLegend.setFormSize(12f);//比例块字体大小 
//        mLegend.setXEntrySpace(2f);
//        mLegend.setTextSize(15f);
//        mLegend.setYEntrySpace(5f);
    }

    //将饼状图分成几部分
    public PieData getPieData(List<String> names, List<Integer> datasOfPecense, List<Integer> colors, boolean isShowText) {

        PieDataSet pieDataSet = new PieDataSet(getEntries(datasOfPecense), "");
//        pieDataSet.setDrawValues(false);
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(15);
        pieDataSet.setSliceSpace(0f);
        //选中时多出来的部分
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px / 2);
        PieData pieData = new PieData(names, pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
//        pieData.setDrawValues(false);
        return pieData;
    }

    //将数据转换为百分比
    private List<Float> getPesence(List<Float> datas) {
        float total = 0.0f;
        List<Float> results = new ArrayList<>();
        for (float data : datas) {
            total += data;
        }
        for (int i = 0; i < datas.size(); i++) {
            results.add(i, (datas.get(i) * 100) / total);
        }
        return results;
    }

    //无数据时，绘制饼图
    public PieChart drawNPieChat(PieChart pieChart, String description, int color) {
        List<Integer> defaultValue = new ArrayList<>();
        List<Integer> defaultColor = new ArrayList<>();
        List<String> defaultName = new ArrayList<>();
        defaultValue.add(100);
        defaultColor.add(color);
//        defaultName.add("暂无数据");
        defaultName.add(description);
        PieDataSet pieDataSet = new PieDataSet(getEntries(defaultValue), "");
        pieDataSet.setColors(defaultColor);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(20);
        pieDataSet.setSliceSpace(0f);
        pieDataSet.setDrawValues(false);
//        pieDataSet.setLabel("支付宝0杯\n微信0杯\n现金0杯");
        PieData pieData = new PieData(defaultName, pieDataSet);
        pieData.setDrawValues(false);
        pieChart = initPieChart(pieChart, "", pieData);
//        pieChart.setTransparentCircleRadius(100f);
//        pieChart.setDrawHoleEnabled(true);
//        pieChart.setDrawCenterText(true);
//        pieChart.setCenterText("暂无数据");
//        pieChart.setCenterTextColor(Color.WHITE);
        pieChart.invalidate();
        return pieChart;
    }

    public void customizeLegend(List<String> names, List<Integer> datas, List<Integer> colors, Context context, LinearLayout legendLayout) {
        Log.d("debug", "names.size==" + names.size());
        for (int i = 0; i < names.size(); i++) {
            LinearLayout.LayoutParams lp = new LinearLayout.
                    LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            lp.weight = 1;//设置比重为1
            LinearLayout layout = new LinearLayout(context);//单个图例的布局
            layout.setOrientation(LinearLayout.HORIZONTAL);//水平排列
            layout.setGravity(Gravity.CENTER_VERTICAL);//垂直居中
            layout.setLayoutParams(lp);

            //添加color
            LinearLayout.LayoutParams colorLP = new LinearLayout.
                    LayoutParams(20, 20);
            colorLP.setMargins(0, 0, 0, 0);
            LinearLayout colorLayout = new LinearLayout(context);
            colorLayout.setLayoutParams(colorLP);
            colorLayout.setBackgroundColor(colors.get(i));
            layout.addView(colorLayout);

            //添加label
            TextView labelTV = new TextView(context);
            labelTV.setText(names.get(i) + " ");
            layout.addView(labelTV);
            if (datas != null) {
                //添加data
                TextView dataTV = new TextView(context);
                dataTV.setText((datas.get(i) + mContext.getResources().getString(R.string.bei)));
                dataTV.setTextColor(colors.get(i));
                layout.addView(dataTV);

            }
            legendLayout.addView(layout);//legendLayout为外层布局即整个图例布局，是在xml文件中定义
        }
    }

    /*
    * 绘制直线图
    * */
    public void drawLineChart(LineChart mChart, List<LineDataSet> sets, List<String> xDatas) {
        mChart.setDescription(mContext.getResources().getString(R.string.riqi));
        mChart.setNoDataText(mContext.getString(R.string.haimeiyoushuju));
        mChart.setNoDataTextDescription(mContext.getString(R.string.jiazaizhong));
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(true);
        mChart.setHighlightIndicatorEnabled(true);
        mChart.setVerticalScrollBarEnabled(false);
        mChart.fitScreen();
        mChart.setScaleEnabled(true);
        mChart.setTouchEnabled(true);
        mChart.getAxisRight().setEnabled(false);
        mChart.invalidate();
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setTextColor(mContext.getResources().getColor(R.color.black_overlay));
        xAxis.setAxisLineColor(mContext.getResources().getColor(R.color.black_overlay));
        xAxis.setDrawGridLines(true);
        xAxis.setGridColor(mContext.getResources().getColor(R.color.black_overlay));
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        LineData data = new LineData(xDatas);
        for (int i = 0; i < sets.size(); i++) {
            data.addDataSet(sets.get(i));
        }
        mChart.setData(data);
        mChart.animateX(2000);
        mChart.invalidate();
    }

    /*
     * 绘制散点图
     * */
    public void drawScatterChart(ScatterChart mChart, List<ScatterDataSet> sets, List<String> xDatas) {
        mChart.setDescription(mContext.getResources().getString(R.string.riqi));
        mChart.setNoDataText(mContext.getString(R.string.haimeiyoushuju));
        mChart.setNoDataTextDescription(mContext.getString(R.string.jiazaizhong));
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(true);
        mChart.setHighlightIndicatorEnabled(true);
        mChart.setVerticalScrollBarEnabled(false);
        mChart.fitScreen();
        mChart.setScaleEnabled(true);
        mChart.setTouchEnabled(true);
        mChart.getAxisRight().setEnabled(false);
        mChart.invalidate();
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setTextColor(mContext.getResources().getColor(R.color.black_overlay));
        xAxis.setAxisLineColor(mContext.getResources().getColor(R.color.black_overlay));
        xAxis.setDrawGridLines(true);
        xAxis.setGridColor(mContext.getResources().getColor(R.color.black_overlay));
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        Legend legend = mChart.getLegend();
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        legend.setForm(Legend.LegendForm.SQUARE);
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        ScatterData data = new ScatterData(xDatas);
        for (int i = 0; i < sets.size(); i++) {
            data.addDataSet(sets.get(i));
        }
        mChart.setData(data);
        mChart.animateXY(1000, 1000);
        mChart.invalidate();
    }
}
