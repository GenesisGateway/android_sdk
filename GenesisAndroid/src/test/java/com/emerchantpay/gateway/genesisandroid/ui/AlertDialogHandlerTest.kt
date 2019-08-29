package com.emerchantpay.gateway.genesisandroid.ui

import com.emerchantpay.gateway.genesisandroid.api.ui.AlertDialogHandler
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class AlertDialogHandlerTest {

    private var alertDialogHandler: AlertDialogHandler? = null

    @Before
    fun setup() {
        alertDialogHandler = mock(AlertDialogHandler::class.java)
    }

    @Test
    fun verifyAlertHandlerShow() {
        alertDialogHandler?.show()
        verify(alertDialogHandler, times(1))?.show()
    }

    @Test
    fun verifyAlertHandlerDismiss() {
        alertDialogHandler?.dismiss()
        verify(alertDialogHandler, times(1))?.dismiss()
    }
}
