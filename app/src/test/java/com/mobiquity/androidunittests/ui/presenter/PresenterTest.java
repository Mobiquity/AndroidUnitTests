package com.mobiquity.androidunittests.ui.presenter;

import com.mobiquity.androidunittests.ui.mvpview.MvpView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.google.common.truth.Truth.assertThat;

public class PresenterTest {


    private Presenter<MvpView> presenter;
    @Mock MvpView view;

    @Before
    public void setUp() {
        presenter = new Presenter<MvpView>() {};
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBindView_ShouldBindViewToPresenter() {
        presenter.bind(view);
        assertThat(presenter.view()).isEqualTo(view);
    }

    @Test
    public void testView_IsNullByDefault() {
        assertThat(presenter.view()).isNull();
    }

    @Test
    public void testIsViewAttached_IsTrueAfterBind() {
        assertThat(presenter.isViewAttached()).isFalse();
        presenter.bind(view);
        assertThat(presenter.isViewAttached()).isTrue();
    }

    @Test
    public void testUnbindView_ShouldBeNullAfterUnbind() {
        presenter.bind(view);
        presenter.unbind();
        assertThat(presenter.view()).isNull();
    }

    @Test
    public void testIsViewAttached_IsFalseAfterUnbind() {
        assertThat(presenter.isViewAttached()).isFalse();
        presenter.bind(view);
        presenter.unbind();
        assertThat(presenter.isViewAttached()).isFalse();
    }

}