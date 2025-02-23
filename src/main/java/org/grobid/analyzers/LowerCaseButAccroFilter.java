package org.grobid.analyzers;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

	/**
	 * Normalizes token text to lower case.
	 *
	 * @version $Id: LowerCaseButAccroFilter.java 56 2013-10-29 10:39:02Z uschindler $
	 */
	public final class LowerCaseButAccroFilter extends TokenFilter {
		private CharTermAttribute termAttr;
		private TypeAttribute typeAttr;

		
		public LowerCaseButAccroFilter(TokenStream in) {
	    super(in);
	    termAttr =  addAttribute(CharTermAttribute.class);
	    typeAttr=(TypeAttribute) addAttribute(TypeAttribute.class);
	  }
	  
	  public boolean incrementToken() throws IOException {

			if (!input.incrementToken()) //#B
				return false; //#C

			if (typeAttr.type().equals("<SYMBOL>")) return true;
			// Here we convert any word to lowercase, except when the word contains an uppercase char (but not in position 0)
			// e.g Invention=>invention, SO2=>SO2, dSiR3=>sSiR3 etc.
			char[] buffer = termAttr.buffer(); 
			final int bufferLength = termAttr.length();


			boolean containUppercase = false;
			for (int i=1;i<bufferLength;i++) {
				if (Character.isUpperCase(buffer[i])) {
					containUppercase=true; break;
				}
			}
			if (! containUppercase) {
				for(int i=0;i<bufferLength;i++)
					buffer[i] = Character.toLowerCase(buffer[i]);
			}
			return true;
	}
}
