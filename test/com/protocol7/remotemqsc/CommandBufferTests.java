/*
Copyright 2004 Niklas Gustavsson (niklas@protocol7.com)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.protocol7.remotemqsc;

import com.protocol7.remotemqsc.MqscCommandBuffer;

import junit.framework.TestCase;

/**
 * @author Niklas Gustavsson (niklas@protocol7.com)
 *
 */
public class CommandBufferTests extends TestCase {
	
	MqscCommandBuffer cb;
	
	protected void setUp() throws Exception {
		cb = new MqscCommandBuffer();
	}
	
	
	public void testSingleAppend(){
		assertTrue(cb.append("test"));
		
		assertEquals("test", cb.toString());
	}

	public void testMultiAppend(){
		assertFalse(cb.append("test1 + "));
		assertFalse(cb.append("test2 + "));
		assertTrue(cb.append("test3 "));
		
		assertEquals("test1 test2 test3", cb.toString());
	}

	public void testWhitespace(){
		assertFalse(cb.append("test1 + "));
		assertFalse(cb.append("    + "));
		assertTrue(cb.append("test3 "));
		
		assertEquals("test1 test3", cb.toString());
	}

	public void testComment(){
		assertFalse(cb.append("test1 + "));
		assertFalse(cb.append("*test2 + "));
		assertFalse(cb.append(" * test2 + "));
		assertTrue(cb.append("test3 "));
		
		assertEquals("test1 test3", cb.toString());
	}
}
