package com.example.handmadestore;

import android.os.Bundle;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;

public class IntroSliderActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.createInstance(
                "Sản phẩm chất lượng",
                "",
                R.drawable.intro,
                R.color.color1,
                R.color.title_intro
        ));
        addSlide(AppIntroFragment.createInstance(
                "Giá cả hấp dẫn",
                "",
                R.drawable.intro2,
                R.color.color2,
                R.color.title_intro
        ));
        addSlide(AppIntroFragment.createInstance(
                "Khuyến mãi ưu đãi",
                "",
                R.drawable.intro3,
                R.color.color3,
                R.color.title_intro
        ));
        addSlide(AppIntroFragment.createInstance(
                "Giao hàng nhanh chóng",
                "",
                R.drawable.intro4,
                R.color.color4,
                R.color.title_intro
        ));
        setWizardMode(true);
        setImmersiveMode();
        setColorTransitionsEnabled(true);
        setDoneText("Bắt đầu");
    }
}