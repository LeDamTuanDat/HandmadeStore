package com.example.handmadestore.HelperClass;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.handmadestore.User.LoginActivity;
import com.example.handmadestore.R;
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

    @Override
    protected void onDonePressed(@Nullable Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}