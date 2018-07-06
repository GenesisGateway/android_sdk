package com.emerchantpay.gateway.genesisandroid.ui;

import com.emerchantpay.gateway.genesisandroid.api.ui.AlertDialogHandler;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AlertDialogHandlerTest {

    private AlertDialogHandler alertDialogHandler;

    @Before
    public void setup() {
        alertDialogHandler = mock(AlertDialogHandler.class);
    }

    @Test
    public void verifyAlertHandlerShow() {
        alertDialogHandler.show();
        verify(alertDialogHandler, times(1)).show();
    }

    @Test
    public void verifyAlertHandlerDismiss() {
        alertDialogHandler.dismiss();
        verify(alertDialogHandler, times(1)).dismiss();
    }
}
