package com.emerchantpay.gateway.genesisandroid

import com.emerchantpay.gateway.genesisandroid.api.interfaces.AddressAttributes
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class AddressUnitTest {

    internal lateinit var addresses: AddressAttributes

    @Before
    fun createAddress() {
        addresses = mock(AddressAttributes::class.java)
    }

    @Test
    @Throws(IllegalAccessException::class)
    fun buildAddress() {
        // Billing address
        `when`(addresses.setBillingFirstname(anyString().toString())).thenReturn(addresses)
        `when`(addresses.setBillingLastname(anyString().toString())).thenReturn(addresses)
        `when`(addresses.setBillingPrimaryAddress(anyString().toString())).thenReturn(addresses)
        `when`(addresses.setBillingSecondaryAddress(anyString().toString())).thenReturn(addresses)
        `when`(addresses.setBillingCity(anyString().toString())).thenReturn(addresses)
        `when`(addresses.setBillingZipCode(anyString().toString())).thenReturn(addresses)
        `when`(addresses.setBillingCountry(anyString().toString())).thenReturn(addresses)

        assertEquals(addresses.setBillingFirstname("John"), addresses)
        assertEquals(addresses.setBillingLastname("Doe"), addresses)
        assertEquals(addresses.setBillingPrimaryAddress("Fifth avenue"), addresses)
        assertEquals(addresses.setBillingSecondaryAddress("Fourth avenue"), addresses)
        assertEquals(addresses.setBillingCity("New York"), addresses)
        assertEquals(addresses.setBillingZipCode("16000"), addresses)
        assertEquals(addresses.setBillingCountry("US"), addresses)
        assertEquals(addresses.buildBillingAddress(), addresses.getBillingAddress())

        verify(addresses).setBillingFirstname("John")
        verify(addresses).setBillingLastname("Doe")
        verify(addresses).setBillingPrimaryAddress("Fifth avenue")
        verify(addresses).setBillingSecondaryAddress("Fourth avenue")
        verify(addresses).setBillingCity("New York")
        verify(addresses).setBillingZipCode("16000")
        verify(addresses).setBillingCountry("US")
        verify(addresses).buildBillingAddress()
        verify(addresses).getBillingAddress()

        // Shipping address
        `when`(addresses.setShippingFirstname(anyString().toString())).thenReturn(addresses)
        `when`(addresses.setShippingLastname(anyString().toString())).thenReturn(addresses)
        `when`(addresses.setShippingPrimaryAddress(anyString().toString())).thenReturn(addresses)
        `when`(addresses.setShippingSecondaryAddress(anyString().toString())).thenReturn(addresses)
        `when`(addresses.setShippingCity(anyString().toString())).thenReturn(addresses)
        `when`(addresses.setShippingZipCode(anyString().toString())).thenReturn(addresses)
        `when`(addresses.setShippingCountry(anyString().toString())).thenReturn(addresses)

        assertEquals(addresses.setShippingFirstname("John"), addresses)
        assertEquals(addresses.setShippingLastname("Doe"), addresses)
        assertEquals(addresses.setShippingPrimaryAddress("Fifth avenue"), addresses)
        assertEquals(addresses.setShippingSecondaryAddress("Fourth avenue"), addresses)
        assertEquals(addresses.setShippingCity("New York"), addresses)
        assertEquals(addresses.setShippingZipCode("16000"), addresses)
        assertEquals(addresses.setShippingCountry("US"), addresses)
        assertEquals(addresses.buildShippingAddress(), addresses.getShippingAddress())

        verify(addresses).setShippingFirstname("John")
        verify(addresses).setShippingLastname("Doe")
        verify(addresses).setShippingPrimaryAddress("Fifth avenue")
        verify(addresses).setShippingSecondaryAddress("Fourth avenue")
        verify(addresses).setShippingCity("New York")
        verify(addresses).setShippingZipCode("16000")
        verify(addresses).setShippingCountry("US")
        verify(addresses).buildShippingAddress()
        verify(addresses).getShippingAddress()

        verifyNoMoreInteractions(addresses)
    }
}
