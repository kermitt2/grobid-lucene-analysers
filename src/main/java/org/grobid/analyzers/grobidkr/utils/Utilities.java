package org.grobid.analyzers.grobidkr.utils;

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

import org.grobid.analyzers.grobidkr.morph.AnalysisOutput;
import org.grobid.analyzers.grobidkr.morph.MorphException;
import org.grobid.analyzers.grobidkr.morph.PatternConstants;

public class Utilities {

	public static String arrayToString(String[] strs) {
		StringBuffer sb = new StringBuffer();
		for(String str:strs) {
			sb.append(str);
		}
		return sb.toString();
	}
	
	public static AnalysisOutput cloneOutput(AnalysisOutput o) throws MorphException {
		try {
			return o.clone();
		} catch (CloneNotSupportedException e) {
			throw new MorphException(e.getMessage(),e);
		}
	}
	
	public static String buildOutputString(AnalysisOutput o) {
		

		StringBuffer buff = new StringBuffer();
	
		buff.append(MorphUtil.buildTypeString(o.getStem(),o.getPos()));
		if(o.getNsfx()!=null)
			buff.append(",").append(MorphUtil.buildTypeString(o.getNsfx(),PatternConstants.POS_SFX_N));
		
		if(o.getPatn()==PatternConstants.PTN_NJ || o.getPatn()==PatternConstants.PTN_ADVJ) {
			buff.append(",").append(MorphUtil.buildTypeString(o.getJosa(),PatternConstants.POS_JOSA));
		}else if(o.getPatn()==PatternConstants.PTN_NSM) {
			buff.append(",").append(MorphUtil.buildTypeString(o.getVsfx(),PatternConstants.POS_SFX_V));
			if(o.getPomi()!=null) 
				buff.append(",").append(MorphUtil.buildTypeString(o.getPomi(),PatternConstants.POS_PEOMI));
			buff.append(",").append(MorphUtil.buildTypeString(o.getEomi(),PatternConstants.POS_EOMI));			
		}else if(o.getPatn()==PatternConstants.PTN_NSMJ) {
			buff.append(",").append(MorphUtil.buildTypeString(o.getVsfx(),PatternConstants.POS_SFX_V));
			buff.append(",").append(MorphUtil.buildTypeString(o.getElist().get(0),PatternConstants.POS_NEOMI));
			buff.append(",").append(MorphUtil.buildTypeString(o.getJosa(),PatternConstants.POS_JOSA));
		}else if(o.getPatn()==PatternConstants.PTN_NSMXM) {
			buff.append(",").append(MorphUtil.buildTypeString(o.getVsfx(),PatternConstants.POS_SFX_V));
			buff.append(",").append(MorphUtil.buildTypeString(o.getElist().get(0),PatternConstants.POS_COPULA));
			buff.append(",").append(MorphUtil.buildTypeString(o.getXverb(),PatternConstants.POS_XVERB));		
			if(o.getPomi()!=null) 
				buff.append(",").append(MorphUtil.buildTypeString(o.getPomi(),PatternConstants.POS_PEOMI));
			buff.append(",").append(MorphUtil.buildTypeString(o.getEomi(),PatternConstants.POS_EOMI));
		}else if(o.getPatn()==PatternConstants.PTN_NJCM) {
			buff.append(",").append(MorphUtil.buildTypeString(o.getJosa(),PatternConstants.POS_JOSA));
			buff.append(",").append(MorphUtil.buildTypeString(o.getElist().get(0),PatternConstants.POS_SFX_V));
			if(o.getPomi()!=null) 
				buff.append(",").append(MorphUtil.buildTypeString(o.getPomi(),PatternConstants.POS_PEOMI));			
			buff.append(",").append(MorphUtil.buildTypeString(o.getEomi(),PatternConstants.POS_EOMI));	
		}else if(o.getPatn()==PatternConstants.PTN_NSMXMJ) {
			buff.append(",").append(MorphUtil.buildTypeString(o.getVsfx(),PatternConstants.POS_SFX_V));			
			buff.append(",").append(MorphUtil.buildTypeString(o.getElist().get(1),PatternConstants.POS_COPULA));			
			buff.append(",").append(MorphUtil.buildTypeString(o.getXverb(),PatternConstants.POS_XVERB));	
			if(o.getPomi()!=null) 
				buff.append(",").append(MorphUtil.buildTypeString(o.getPomi(),PatternConstants.POS_PEOMI));	
			buff.append(",").append(MorphUtil.buildTypeString(o.getElist().get(0),PatternConstants.POS_NEOMI));			
			buff.append(",").append(MorphUtil.buildTypeString(o.getJosa(),PatternConstants.POS_JOSA));				
		}else if(o.getPatn()==PatternConstants.PTN_VM) {
			if(o.getPomi()!=null) 
				buff.append(",").append(MorphUtil.buildTypeString(o.getPomi(),PatternConstants.POS_PEOMI));			
			buff.append(",").append(MorphUtil.buildTypeString(o.getEomi(),PatternConstants.POS_EOMI));				
		}else if(o.getPatn()==PatternConstants.PTN_VMJ) {
			buff.append(",").append(MorphUtil.buildTypeString(o.getElist().get(0),PatternConstants.POS_NEOMI));			
			buff.append(",").append(MorphUtil.buildTypeString(o.getJosa(),PatternConstants.POS_JOSA));				
		}else if(o.getPatn()==PatternConstants.PTN_VMCM) {
			buff.append(",").append(MorphUtil.buildTypeString(o.getElist().get(0),PatternConstants.POS_NEOMI));			
			buff.append(",").append(MorphUtil.buildTypeString(o.getElist().get(1),PatternConstants.POS_SFX_N));			
			if(o.getPomi()!=null) 
				buff.append(",").append(MorphUtil.buildTypeString(o.getPomi(),PatternConstants.POS_PEOMI));			
			buff.append(",").append(MorphUtil.buildTypeString(o.getEomi(),PatternConstants.POS_EOMI));				
		}else if(o.getPatn()==PatternConstants.PTN_VMXM) {
			buff.append(",").append(MorphUtil.buildTypeString(o.getElist().get(0),PatternConstants.POS_COPULA));			
			buff.append(",").append(MorphUtil.buildTypeString(o.getXverb(),PatternConstants.POS_XVERB));
			if(o.getPomi()!=null) 
				buff.append(",").append(MorphUtil.buildTypeString(o.getPomi(),PatternConstants.POS_PEOMI));			
			buff.append(",").append(MorphUtil.buildTypeString(o.getEomi(),PatternConstants.POS_EOMI));				
		}else if(o.getPatn()==PatternConstants.PTN_VMXMJ) {
			buff.append(",").append(MorphUtil.buildTypeString(o.getElist().get(1),PatternConstants.POS_COPULA));			
			buff.append(",").append(MorphUtil.buildTypeString(o.getXverb(),PatternConstants.POS_XVERB));	
			if(o.getPomi()!=null) 
				buff.append(",").append(MorphUtil.buildTypeString(o.getPomi(),PatternConstants.POS_PEOMI));	
			buff.append(",").append(MorphUtil.buildTypeString(o.getElist().get(0),PatternConstants.POS_NEOMI));			
			buff.append(",").append(MorphUtil.buildTypeString(o.getJosa(),PatternConstants.POS_JOSA));								
		}
		return buff.toString();
		
	}
}
