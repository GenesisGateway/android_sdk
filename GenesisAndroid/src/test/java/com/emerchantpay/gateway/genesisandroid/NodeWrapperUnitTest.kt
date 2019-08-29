package com.emerchantpay.gateway.genesisandroid

import com.emerchantpay.gateway.genesisandroid.api.util.NodeWrapper

import org.junit.Before
import org.junit.Test

import java.util.ArrayList

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class NodeWrapperUnitTest {

    private var wrapper: NodeWrapper? = null
    private var nodeList: MutableList<String>? = null

    @Before
    fun setup() {
        wrapper = mock(NodeWrapper::class.java)
        nodeList = ArrayList()
        nodeList!!.add("wpf_payment")
    }

    @Test
    fun testFindAllStrings() {
        `when`(wrapper!!.findAllStrings("wpf_payment")).thenReturn(nodeList)
        assertTrue(wrapper!!.findAllStrings("wpf_payment").contains("wpf_payment"))
    }

    @Test
    fun testFindString() {
        `when`(wrapper!!.findString("wpf_payment")).thenReturn(anyString())
        assertNotNull(wrapper!!.findString("wpf_payment"))
    }
}
