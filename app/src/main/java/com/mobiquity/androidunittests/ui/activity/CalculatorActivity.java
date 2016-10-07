package com.mobiquity.androidunittests.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mobiquity.androidunittests.CalculatorApplication;
import com.mobiquity.androidunittests.R;
import com.mobiquity.androidunittests.di.components.CalculatorComponent;
import com.mobiquity.androidunittests.di.components.DaggerCalculatorComponent;
import com.mobiquity.androidunittests.ui.mvpview.CalculatorView;
import com.mobiquity.androidunittests.ui.presenter.CalculatorPresenter;
import com.mobiquity.androidunittests.ui.view.CalculatorEditText;
import com.mobiquity.androidunittests.ui.view.NumericPad;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class CalculatorActivity extends BaseActivity<CalculatorComponent>
        implements CalculatorView {

    private Animator resultAnimator;

    @Inject
    CalculatorPresenter presenter;

    @BindView(R.id.numeric_pad)
    NumericPad numericPad;
    @BindView(R.id.display_input)
    CalculatorEditText displayInput;
    @BindView(R.id.display_result)
    TextView resultText;
    @BindView(R.id.sliding_panel)
    SlidingUpPanelLayout slidingPanel;

    @BindViews(value = {
            R.id.add_op,
            R.id.subtract_op,
            R.id.multiply_op,
            R.id.left_paren,
            R.id.right_paren,
            R.id.decimal,
            R.id.exponent_op
    }) List<Button> calculatorButtons;

    @BindViews(value = {
            R.id.divide_op,
            R.id.function_arg_separator
    }) List<View> notImplementedButtons;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator, true);
        ButterKnife.bind(this);

        numericPad.addOnNumberClickedListener(number -> presenter.handleCalculatorButtonPress(Integer.toString(number)));
        ButterKnife.apply(calculatorButtons, (ButterKnife.Action<? super TextView>) (button, index) -> {
            String operator = button.getText().toString();
            button.setOnClickListener(v -> presenter.handleCalculatorButtonPress(operator));
        });

        ButterKnife.apply(notImplementedButtons, (ButterKnife.Action<? super View>) (button, index) -> {
            button.setOnClickListener(v -> Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    protected CalculatorComponent buildComponent() {
        return DaggerCalculatorComponent.builder()
                .appComponent(CalculatorApplication.getAppComponent(this))
                .build();
    }

    @Override
    protected void injectDependencies() {
        component.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.bind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unbind();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        if (resultAnimator != null) {
            resultAnimator.end();
        }
    }

    @OnClick(R.id.delete_op)
    void onClickDeleteButton() {
        presenter.handleDelete();
    }

    @OnLongClick(R.id.delete_op)
    boolean onLongClickDeleteButton() {
        return presenter.handleClear();
    }

    @OnClick(R.id.eq)
    void onClickEqualsButton() {
        presenter.handleEvaluate();
    }

    @OnClick(R.id.extra_button_wolfram)
    void onClickWolframButton() {
        slidingPanel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    slidingPanel.removePanelSlideListener(this);
                    Intent intent = new Intent(CalculatorActivity.this, WolframActivity.class);
                    startActivity(intent);
                }
            }
        });
        slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    @Override
    public void updateDisplayText(String displayText) {
        displayInput.setText(displayText);
    }

    @Override
    public void showPassiveCalculation(String result) {
        resultText.setText(result);
    }

    @Override
    public void showResult(final String result) {
        final float scale = displayInput.getVariableTextSize(result) / resultText.getTextSize();

        // Slide result text to match position of display text
        final float resultTranslationX = (1.0f - scale) *
                (resultText.getWidth() / 2.0f - resultText.getPaddingRight());
        final float resultTranslationY = (1.0f - scale) *
                (resultText.getHeight() / 2.0f - resultText.getPaddingBottom()) +
                (displayInput.getBottom() - resultText.getBottom()) +
                (resultText.getPaddingBottom() - displayInput.getPaddingBottom());

        // Slide display text upwards offscreen
        final float displayTranslationY = -displayInput.getBottom();

        // Animate the result text color to match the display text color
        final int resultTextColor = resultText.getCurrentTextColor();
        final int displayTextColor = displayInput.getCurrentTextColor();
        ValueAnimator textColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), resultTextColor, displayTextColor);
        textColorAnimator.addUpdateListener(animation -> resultText.setTextColor((int) animation.getAnimatedValue()));

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                textColorAnimator,
                ObjectAnimator.ofFloat(resultText, View.SCALE_X, scale),
                ObjectAnimator.ofFloat(resultText, View.SCALE_Y, scale),
                ObjectAnimator.ofFloat(resultText, View.TRANSLATION_X, resultTranslationX),
                ObjectAnimator.ofFloat(resultText, View.TRANSLATION_Y, resultTranslationY),
                ObjectAnimator.ofFloat(displayInput, View.TRANSLATION_Y, displayTranslationY)
        );
        animatorSet.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                resultText.setText(result);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                // Reset result text
                resultText.setTextColor(resultTextColor);
                resultText.setScaleX(1);
                resultText.setScaleY(1);
                resultText.setTranslationX(0);
                resultText.setTranslationY(0);

                // Reset display text
                displayInput.setTranslationY(0);

                displayInput.setText(result);
                resultText.setText("");
                resultAnimator = null;
            }
        });
        resultAnimator = animatorSet;
        animatorSet.start();
    }

    @Override
    public void showResultError() {
        Toast.makeText(this, R.string.invalid_expression, Toast.LENGTH_SHORT).show();
    }
}
