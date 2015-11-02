package com.xyz.kjy.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class HanYuUtil {
	/**
	 * 将单个字符（包括单个汉字或者单个英文字母）转换为小写字母
	 * @param c
	 * @return
	 */
	public static String getCharacterPinYin(char c)
	{
		String[] pinyin=null;
		HanyuPinyinOutputFormat format =new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//汉字没有声调
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		try{
			pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
		}catch(BadHanyuPinyinOutputFormatCombination e){
			e.printStackTrace();
		}
		// 如果c不是汉字，toHanyuPinyinStringArray会返回null
		if (pinyin == null)
			return ((Character)c).toString().toLowerCase();
		// 只取一个发音，如果是多音字，仅取第一个发音
		return pinyin[0];
	}
	/**
	 * 将包含中英文的字符串以小写英文字母的形式返回
	 * @param str
	 * @return
	 */
	public static String getStringPinYin(String str)
	{
		StringBuilder sb = new StringBuilder();
		String tempPinyin = null;
		for (int i = 0; i < str.length(); ++i)
		{
			tempPinyin = getCharacterPinYin(str.charAt(i));
			sb.append(tempPinyin);
		}
		return sb.toString();
	}
}

