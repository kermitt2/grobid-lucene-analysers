package org.grobid.analyzers.grobidkr;

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

//import org.apache.lucene.analysis.Token;
//import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
%%

%class KoreanTokenizerImpl
%unicode
%integer
%function getNextToken
%pack
%char

%{

public static final int ALPHANUM          = KoreanTokenizer.ALPHANUM;
public static final int APOSTROPHE        = KoreanTokenizer.APOSTROPHE;
public static final int ACRONYM           = KoreanTokenizer.ACRONYM;
public static final int COMPANY           = KoreanTokenizer.COMPANY;
public static final int EMAIL             = KoreanTokenizer.EMAIL;
public static final int HOST              = KoreanTokenizer.HOST;
public static final int NUM               = KoreanTokenizer.NUM;
public static final int CJ                = KoreanTokenizer.CJ;
public static final int PUNCT                = KoreanTokenizer.PUNCT;

/**
 * @deprecated this solves a bug where HOSTs that end with '.' are identified
 *             as ACRONYMs. It is deprecated and will be removed in the next
 *             release.
 */
public static final int ACRONYM_DEP       = KoreanTokenizer.ACRONYM_DEP;
public static final int KOREAN            = KoreanTokenizer.KOREAN;

public static final String [] TOKEN_TYPES = KoreanTokenizer.TOKEN_TYPES;

public final int yychar()
{
    return yychar;
}

/**
 * Fills Lucene token with the current token text.
 */
public final void getText(CharTermAttribute t) {
    t.copyBuffer(zzBuffer, zzStartRead, zzMarkedPos-zzStartRead);
  }



%}

// korean word: a sequence of digits & letters & 
//BP stick to HANLETTERS now
//KOREAN   = ({LETTER}|{NUM}|{DIGIT})* {HANLETTER}+ ({LETTER}|{DIGIT})*
KOREAN   = {HANLETTER}+

// basic word: a sequence of digits & letters
ALPHANUM   = ({LETTER}|{DIGIT})+

// internal apostrophes: O'Reilly, you're, O'Reilly's
// use a post-filter to remove possesives
APOSTROPHE =  {ALPHA} ("'" {ALPHA})+

// acronyms: U.S.A., I.B.M., etc.
// use a post-filter to remove dots
ACRONYM    =  {LETTER} "." ({LETTER} ".")+

ACRONYM_DEP	= {ALPHANUM} "." ({ALPHANUM} ".")+

// company names like AT&T and Excite@Home.
COMPANY    =  {ALPHA} ("&"|"@") {ALPHA}

// email addresses
EMAIL      =  {ALPHANUM} (("."|"-"|"_") {ALPHANUM})* "@" {ALPHANUM} (("."|"-") {ALPHANUM})+

// hostname
HOST       =  {ALPHANUM} ((".") {ALPHANUM})+

// floating point, serial, model numbers, ip addresses, etc.
// every other segment must have at least one digit
NUM        = ({ALPHANUM} {P} {HAS_DIGIT}
 		   | "." {DIGIT}
 		   | {DIGIT} "." 		   
           | {HAS_DIGIT} {P} {ALPHANUM}
           | {ALPHANUM} ({P} {HAS_DIGIT} {P} {ALPHANUM})+
           | {HAS_DIGIT} ({P} {ALPHANUM} {P} {HAS_DIGIT})+
           | {ALPHANUM} {P} {HAS_DIGIT} ({P} {ALPHANUM} {P} {HAS_DIGIT})+
           | {HAS_DIGIT} {P} {ALPHANUM} ({P} {HAS_DIGIT} {P} {ALPHANUM})+)

// punctuation
P	         = ("_"|"-"|"/"|"."|",")

// at least one digit
HAS_DIGIT  =
    ({LETTER}|{DIGIT})*
    {DIGIT}
    ({LETTER}|{DIGIT})*

ALPHA      = ({LETTER})+

//BP added the range for Kanji-latin letters (0xFF01 - 0xFF5E)
// deleted the U+FF0F  	FULLWIDTH SOLIDUS
LETTER     = [\u0041-\u005a\u0061-\u007a\u00c0-\u00d6\u00d8-\u00f6\u00f8-\u00ff\u0100-\u1fff\uff01-\uff0e\uff10-\uff5e\uffa0-\uffdc]

DIGIT      = [\u0030-\u0039\u0660-\u0669\u06f0-\u06f9\u0966-\u096f\u09e6-\u09ef\u0a66-\u0a6f\u0ae6-\u0aef\u0b66-\u0b6f\u0be7-\u0bef\u0c66-\u0c6f\u0ce6-\u0cef\u0d66-\u0d6f\u0e50-\u0e59\u0ed0-\u0ed9\u1040-\u1049]

HANLETTER     = [\uac00-\ud7af\u1100-\u11ff]

// Chinese, Japanese
CJ         = ([\u3040-\u318f\u3100-\u312f\u3040-\u309F\u30A0-\u30FF\u31F0-\u31FF\u3300-\u337f\u3400-\u4dbf\u4e00-\u9fff\uf900-\ufaff\uff65-\uff9f])+

WHITESPACE = \r\n | [ \r\n\t\f]

%%

{KOREAN}                                                       { System.err.println("c="+yychar+"KOREAN."); return KOREAN; }
{ALPHANUM}                                                     { return ALPHANUM; }
{APOSTROPHE}                                                   { return APOSTROPHE; }
{ACRONYM}                                                      { return ACRONYM; }
{COMPANY}                                                      { return COMPANY; }
{EMAIL}                                                        { return EMAIL; }
{HOST}                                                         { return HOST; }
{NUM}                                                          { return NUM; }
{CJ}                                                           { return CJ; }
{ACRONYM_DEP}                                                  { return ACRONYM_DEP; }


{WHITESPACE}  													{ /* ignore */ }
/** BP do not ignore the rest !*/
/*[\uff0f]											{return EMAIL;}*/
.                                                { System.err.println("c="+yychar+"PUNCT.");return PUNCT; }
