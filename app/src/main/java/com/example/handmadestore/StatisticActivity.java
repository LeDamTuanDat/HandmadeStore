package com.example.handmadestore;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.handmadestore.Object.RevenueOverTime;
import com.example.handmadestore.Object.Order;
import com.example.handmadestore.databinding.ActivityStatisticBinding;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StatisticActivity extends AppCompatActivity {

    ActivityStatisticBinding binding;
    ArrayList<BarEntry> entries;
    ArrayList<String> lables;
    ArrayList<RevenueOverTime> revenueOverTimes;
    ArrayList<String> options;
    ArrayAdapter<String> adapter;
    Calendar calendar;
    int currentMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatisticBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        handleSelectMonth();
    }

    private void init(){
        entries = new ArrayList<>();
        lables = new ArrayList<>();
        revenueOverTimes = new ArrayList<>();
        options = new ArrayList<>();
        options.add("Cả năm");
        adapter = new ArrayAdapter<>(this,R.layout.spinner_item,options);
        binding.spinner.setAdapter(adapter);
        calendar = Calendar.getInstance();
        currentMonth = calendar.get(Calendar.MONTH);
        for (int i = 0; i <= currentMonth; i++) {
            options.add(getMonthName(i));
        }
    }

    private void handleSelectMonth(){
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                barChartForMonthlyRevenue(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void barChartForMonthlyRevenue(int month){
        if (month == 0){
            calculateMonthlyRevenue();
        }else {
            calculateDaylyRevenue(month);
        }
        entries.clear();
        lables.clear();
        for (int i = 0; i < revenueOverTimes.size(); i++) {
            int time = revenueOverTimes.get(i).getTime();
            long money = revenueOverTimes.get(i).getRevenue();
            entries.add(new BarEntry(i,money));
            if (month == 0){
                lables.add(getMonthName(time));
            }else {
                lables.add((i+1) + "");
            }
        }
        BarDataSet barDataSet = new BarDataSet(entries, "Doanh thu hàng tháng");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextSize(12f);

        Description description= new Description();
        description.setText("VNĐ");
        binding.chart.setDescription(description);

        BarData barData = new BarData(barDataSet);
        binding.chart.setFitBars(true);
        binding.chart.setData(barData);

        XAxis xAxis = binding.chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(lables));
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
//        xAxis.setLabelCount(lables.size());
        if (month == 0){
            xAxis.setLabelRotationAngle(270);
        }else {
            xAxis.setLabelRotationAngle(0);
        }
        xAxis.setAxisLineColor(Color.BLACK);

        YAxis leftAxis = binding.chart.getAxisLeft();
        YAxis rightAxis = binding.chart.getAxisRight();

        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);

        binding.chart.animateY(1000);
        binding.chart.getAxisLeft().setDrawLabels(false);
        binding.chart.setExtraOffsets(0, 30, 0, 0);
        binding.chart.invalidate();
    }

    private void calculateMonthlyRevenue(){
        revenueOverTimes.clear();
        for (int i = 0; i <= currentMonth; i++) {
            long money = 0;
            for (Order order: LoginActivity.orders) {
                if (order.getStatus().equals("Đã giao")) {
                    Date orderTime = order.getOrderTime();
                    calendar.setTime(orderTime);
                    int month = calendar.get(Calendar.MONTH);
                    if (i == month) {
                        money += order.calTotal();
                    }
                }
            }
            RevenueOverTime revenueOverTime = new RevenueOverTime(i,money);
            revenueOverTimes.add(revenueOverTime);
        }
    }

    private void calculateDaylyRevenue(int month){
        revenueOverTimes.clear();
        YearMonth yearMonth = YearMonth.of(2024, month);
        int daysInMonth = yearMonth.lengthOfMonth();
        for (int i = 1; i <= daysInMonth; i++) {
            long money = 0;
            for (Order order: LoginActivity.orders) {
                if (order.getStatus().equals("Đã giao")) {
                    Date orderTime = order.getOrderTime();
                    calendar.setTime(orderTime);
                    int dayOfOrderTime = calendar.get(Calendar.DAY_OF_MONTH);
                    int monthOfOrder = calendar.get(Calendar.MONTH) + 1;
                    if (i == dayOfOrderTime && monthOfOrder == month) {
                        money += order.calTotal();
                    }
                }
            }
            RevenueOverTime revenueOverTime = new RevenueOverTime(i,money);
            revenueOverTimes.add(revenueOverTime);
        }
    }

    private String getMonthName(int month) {
        String monthName;
        switch (month) {
            case 0:
                monthName = "Tháng 1";
                break;
            case 1:
                monthName = "Tháng 2";
                break;
            case 2:
                monthName = "Tháng 3";
                break;
            case 3:
                monthName = "Tháng 4";
                break;
            case 4:
                monthName = "Tháng 5";
                break;
            case 5:
                monthName = "Tháng 6";
                break;
            case 6:
                monthName = "Tháng 7";
                break;
            case 7:
                monthName = "Tháng 8";
                break;
            case 8:
                monthName = "Tháng 9";
                break;
            case 9:
                monthName = "Tháng 10";
                break;
            case 10:
                monthName = "Tháng 11";
                break;
            case 11:
                monthName = "Tháng 12";
                break;
            default:
                monthName = "Tháng không hợp lệ";
                break;
        }
        return monthName;
    }

}