package com.example.handmadestore;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.handmadestore.Object.MonthlyRevenue;
import com.example.handmadestore.Object.Order;
import com.example.handmadestore.databinding.ActivityStatisticBinding;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StatisticActivity extends AppCompatActivity {

    ActivityStatisticBinding binding;
    ArrayList<BarEntry> entries;
    ArrayList<String> lables;
    ArrayList<MonthlyRevenue> monthlyRevenues = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatisticBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        calculateMonthlyRevenue();

        entries = new ArrayList<>();
        lables = new ArrayList<>();

        for (int i = 0; i < monthlyRevenues.size(); i++) {
            int month = monthlyRevenues.get(i).getMonth();
            long money = monthlyRevenues.get(i).getRevenue();
            entries.add(new BarEntry(i,money));
            lables.add(getMonthName(month + 1));
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Doanh thu hàng tháng");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//        barDataSet.setDrawValues(false);
        Description description= new Description();
        description.setText("VNĐ");
        binding.chart.setDescription(description);
        BarData barData = new BarData(barDataSet);
        binding.chart.setData(barData);

        XAxis xAxis = binding.chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(lables));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(lables.size());
        xAxis.setLabelRotationAngle(0);//
        xAxis.setAxisLineColor(Color.BLACK);//
        xAxis.setSpaceMin(0.5f); // Thêm khoảng trống trước nhãn đầu tiên
        xAxis.setSpaceMax(0.5f);

        binding.chart.animateY(1000);
        binding.chart.getAxisLeft().setDrawLabels(false);//
        binding.chart.invalidate();

    }

    private void calculateMonthlyRevenue(){
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        for (int i = 0; i < currentMonth; i++) {
            long money = 0;
            for (Order order: LoginActivity.orders) {
                Date orderTime = order.getOrderTime();
                calendar.setTime(orderTime);
                int month = calendar.get(Calendar.MONTH) + 1;
                if ((i+1) == month){
                    money += order.calTotal();
                }
            }
            MonthlyRevenue monthlyRevenue = new MonthlyRevenue(i,money);
            monthlyRevenues.add(monthlyRevenue);
        }
    }

    public String getMonthName(int month) {
        String monthName;
        switch (month) {
            case 1:
                monthName = "Tháng 1";
                break;
            case 2:
                monthName = "Tháng 2";
                break;
            case 3:
                monthName = "Tháng 3";
                break;
            case 4:
                monthName = "Tháng 4";
                break;
            case 5:
                monthName = "Tháng 5";
                break;
            case 6:
                monthName = "Tháng 6";
                break;
            case 7:
                monthName = "Tháng 7";
                break;
            case 8:
                monthName = "Tháng 8";
                break;
            case 9:
                monthName = "Tháng 9";
                break;
            case 10:
                monthName = "Tháng 10";
                break;
            case 11:
                monthName = "Tháng 11";
                break;
            case 12:
                monthName = "Tháng 12";
                break;
            default:
                monthName = "Tháng không hợp lệ";
                break;
        }
        return monthName;
    }
}