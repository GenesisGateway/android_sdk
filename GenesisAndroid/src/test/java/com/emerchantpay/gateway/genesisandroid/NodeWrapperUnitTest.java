package com.emerchantpay.gateway.genesisandroid;

import com.emerchantpay.gateway.genesisandroid.api.util.NodeWrapper;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NodeWrapperUnitTest {

    private NodeWrapper wrapper;
    private List<String> nodeList;

    @Before
    public void setup() {
        wrapper = mock(NodeWrapper.class);
        nodeList = new ArrayList<String>();
        nodeList.add("wpf_payment");
    }

    @Test
    public void testFindAllStrings() {
        when(wrapper.findAllStrings("wpf_payment")).thenReturn(nodeList);
        assertTrue(wrapper.findAllStrings("wpf_payment").contains("wpf_payment"));
    }

    @Test
    public void testFindString() {
        when(wrapper.findString("wpf_payment")).thenReturn(anyString());
        assertNotNull(wrapper.findString("wpf_payment"));
    }
}
