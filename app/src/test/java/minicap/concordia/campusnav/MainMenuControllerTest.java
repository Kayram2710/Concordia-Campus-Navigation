package minicap.concordia.campusnav;

import static org.mockito.Mockito.*;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import minicap.concordia.campusnav.screens.MainMenuController;
import android.view.ViewPropertyAnimator;


public class MainMenuControllerTest {

    @Mock Context mockContext;
    @Mock View mockSlidingMenu;
    @Mock ImageButton mockOpenButton, mockCloseButton, mockClassScheduleRedirect, mockDirectionsRedirect, mockCampusMapRedirect;
    @Mock ViewPropertyAnimator mockAnimator;

    private MainMenuController mainMenuController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // We need to be sure that animate() will not return null
        when(mockSlidingMenu.animate()).thenReturn(mockAnimator);
        when(mockAnimator.translationX(anyFloat())).thenReturn(mockAnimator);
        when(mockAnimator.setDuration(anyLong())).thenReturn(mockAnimator);

        mainMenuController = new MainMenuController(mockContext, mockSlidingMenu,
                mockOpenButton, mockCloseButton, mockClassScheduleRedirect,
                mockDirectionsRedirect, mockCampusMapRedirect);
    }

    @Test
    public void testOpenMenu() {
        mainMenuController.open();
        verify(mockSlidingMenu.animate()).translationX(0);
        verify(mockAnimator).setDuration(300);
        verify(mockAnimator).start();
    }

    @Test
    public void testCloseMenu() {
        mainMenuController.close();
        verify(mockSlidingMenu.animate()).translationX(anyFloat());
        verify(mockAnimator).setDuration(300);
        verify(mockAnimator).start();
    }

    @Test
    public void testOpenClassSchedule() {
        mainMenuController.openClassSchedule();
        verify(mockContext).startActivity(any(Intent.class));
    }
}

