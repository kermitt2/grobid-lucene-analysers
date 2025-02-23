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
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
//import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/** Normalizes tokens extracted with {@link MyTokenizer}. */

public final class MyJapanesePunctuationFilter extends TokenFilter {

private Token bufferedToken=null;
private CharTermAttribute termAttr;
private TypeAttribute typeAttr;
private PositionIncrementAttribute posAttr;
//private OffsetAttribute offsetAttr;

  /** Construct filtering <i>in</i>. */
  public MyJapanesePunctuationFilter(TokenStream in) {
    super(in);
    termAttr = (CharTermAttribute) addAttribute(CharTermAttribute.class);
    typeAttr=(TypeAttribute) addAttribute(TypeAttribute.class);
  }
  
  public boolean incrementToken() throws IOException {
	  
	  if (bufferedToken != null) { 
		  termAttr.setEmpty(); termAttr.append(bufferedToken.toString());
		  typeAttr.setType(bufferedToken.type()); 
		  posAttr.setPositionIncrement(1);
      	  bufferedToken=null;
      	  return true;
	    }
	  if (!input.incrementToken()) //#B
		  return false; //#C

	    char[] buffer = termAttr.buffer(); 
	    final int bufferLength = termAttr.length();
//	    String type = typeAttr.type();
	    
	     // Here we convert any Japanese punctuation to its usual Latin counterpart
	    // see for example: http://en.wikipedia.org/wiki/Japanese_typographic_symbols
	    
	     if (bufferLength==1) {
	    	 char c= buffer[0];
	    	 switch (c) {
	    	 case '％': termAttr.setEmpty(); termAttr.append('%'); break;
	    	 case '，':
	    	 case '、': termAttr.setEmpty(); termAttr.append(','); break;
			 case '。': // Japanese dot
				  termAttr.setEmpty(); termAttr.append("."); break;	    	 
		     case '>': // this is not the usual bracket symbol
	    	 case '〉':
	    		 termAttr.setEmpty(); termAttr.append(">"); break;
	    	 case '〈': 
	    	 case '<': // this is not the usual bracket symbol
	    		 termAttr.setEmpty(); termAttr.append("<"); break;

			  case  '【': // Japanese square or curly brackets
			  case '〔':
			  case '［':
			  case '｛':
			  case '〘':
			  case '〚':
				  termAttr.setEmpty(); termAttr.append("["); break;
			  case  '】':
			  case  '〕':
			  case '］':
			  case '｝':
			  case '〙':
			  case '〛':
				  termAttr.setEmpty(); termAttr.append("]"); break;
			  case '（':termAttr.setEmpty(); termAttr.append("("); break;
			  case '）':termAttr.setEmpty(); termAttr.append(")"); break;
			  
			  case  '「': // all Japanese chars for quotes
			  case  '」':
			  case '“':
			  case '”':
			  case '″': // this is not a usual double-quote!
			  case '《':
			  case '》':
			  case '『': // double quotation marks
			  case '』':
				  termAttr.setEmpty(); termAttr.append("\""); break;
			  case '−': // these are not usual hyphens
			  case '‐':
			  case '-':
			  case '─':
			  case '〜':
			  case '～':
				  termAttr.setEmpty(); termAttr.append("-"); break;
			  case '：':
				  termAttr.setEmpty(); termAttr.append(":"); break;
			  case '；':
				  termAttr.setEmpty(); termAttr.append(";"); break;
			  case '/':
				  termAttr.setEmpty(); termAttr.append("/"); break;
			  case '！':termAttr.setEmpty(); termAttr.append("!"); break;
			  case '？':termAttr.setEmpty(); termAttr.append("?"); break;
			  case '…':
			  case '‥':
				  termAttr.setEmpty(); termAttr.append("..."); break;
			  case '・': // non breakable space in names e.g. "M・K Sale CO., LTD."
			  case '　': // <= this is not a usual space
				  termAttr.setEmpty(); termAttr.append(" "); break;		  
			  }
	    	 buffer = termAttr.buffer(); 
		  }
	        
	    return true;
	  }
  
}

