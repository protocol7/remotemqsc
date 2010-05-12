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

import java.util.Map;

import junit.framework.TestCase;

/**
 * @author Niklas Gustavsson (niklas@protocol7.com)
 *
 */
public class ArgsToMapConverterTest extends TestCase {
	public void testConvert(){
		String[] args = new String[]{
			"-aaa", "bbb",
			"-ccc", "ddd",
			"-eee", "fff"
		};
		
		Map map = ArgsToMapConverter.convert(args);
		
		assertEquals("bbb", map.get("aaa"));
		assertEquals("ddd", map.get("ccc"));
		assertEquals("fff", map.get("eee"));
		assertNull(map.get("foo"));
	}
}
