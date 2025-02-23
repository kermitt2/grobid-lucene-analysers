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
//import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
//import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

//import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/** Normalizes tokens extracted with {@link MyTokenizer}. */

public final class GrobidFilterTwoBytesLatinChars extends TokenFilter {

private CharTermAttribute termAttr;
//private TypeAttribute typeAttr;
//private PositionIncrementAttribute posAttr;
//private OffsetAttribute offsetAttr;


  /** Construct filtering <i>in</i>. */
  public GrobidFilterTwoBytesLatinChars(TokenStream in) {
    super(in);
    termAttr = (CharTermAttribute) addAttribute(CharTermAttribute.class);
//    typeAttr=(TypeAttribute) addAttribute(TypeAttribute.class);
  }
  
  public boolean incrementToken() throws IOException {
	  
	  if (!input.incrementToken()) 
		  return false; 

	    char[] buffer = termAttr.buffer(); 
	    final int bufferLength = termAttr.length();
	    boolean charModified=false;
	     // Here we convert any Japanese punctuation to its usual Latin counterpart
	    // see for example: http://en.wikipedia.org/wiki/Japanese_typographic_symbols	    
	    for (int i=0; i<bufferLength; i++) {
	    	char c= buffer[i]; char newChar=c;
	    	if (c>=0xFF01 && c<=0xFF5E && c!= 0xFF0F) { 
	    		// This is a Kanji-latin char, let's convert all the string
	    		newChar=(char)(c-0xFF01+0x0021);
	    	} else {
	    		switch (c) {
/*	    		case '，':
	    		case '、': newChar=','; break;
	    		case '。': // Japanese dot
	    		case '･': // Mainly used in Japanese for three dots... "･･･"
	    			newChar='.'; break;	    	 
	    		case '>': // this is not the usual bracket symbol
	    		case '〉':
	    			newChar='>'; break;
	    		case '〈': 
	    		case '<': // this is not the usual bracket symbol
	    			newChar='<'; break;

	    		case  '【': // Japanese square or curly brackets
	    		case '〔':
	    		case '［':
	    		case '｛':
	    		case '〘':
	    		case '〚':
	    			newChar='['; break;
	    		case  '】':
	    		case  '〕':
	    		case '］':
	    		case '｝':
	    		case '〙':
	    		case '〛':
	    			newChar=']'; break;
	    		case '（':newChar='(';  break;
	    		case '）':newChar=')';  break;

	    		case  '「': // all Japanese chars for quotes
	    		case  '」':
	    		case '“':
	    		case '”':
	    		case '″': // this is not a usual double-quote!
	    		case '《':
	    		case '》':
	    		case '『': // double quotation marks
	    		case '』':
	    			newChar='"'; break;
	    		case '−': // these are not usual hyphens
	    		case '‐':
	    		case '-':
	    		case '─':
	    		case '〜':
	    		case '～':
	    			newChar='-'; break;
	    		case '：':
	    			newChar=':'; break;
	    		case '／':
	    			newChar='/'; break;
	    		case '！':newChar='!'; break;
	    		case '？':newChar='?'; break;
	    		case '…':
	    		case '‥':
	    		case '・': // non breakable space in names e.g. "M・K Sale CO., LTD."
*/	    		case '　': // <= this is not a usual space
	    			newChar=' '; break;				  
	    		}
	    	}
	    	if (newChar != c) {buffer[i]=newChar; charModified=true;}
	    }

	    if (charModified) { // The character was changed, change termAttr 
	    	termAttr.setEmpty(); 
	    	for (int i=0; i < bufferLength; i++) {termAttr.append(buffer[i]);}
	    }
	    return true;
  }

}

