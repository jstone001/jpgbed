package com.sw.util;

/**
 * 字符串工具类
 * @author
 *
 */
public class StringUtil {

	/**
	 * 判断是否是空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		if(str==null||"".equals(str.trim())){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 判断是否不是空
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str){
		if((str!=null)&&!"".equals(str.trim())){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 去除两边空格
	 * @param Str
	 * @param Flag
	 * @return
	 */
	public static String  trims(String Str,String Flag) {
		if (Str == null || Str.equals("")) {
			return Str;
		} else {
			Str =   ""+Str;
			if(   Flag   ==   "l"   ||   Flag   ==   "L"   )/*trim   left   side   only*/
			{
				String RegularExp =  "^[　 ]+";
				return   Str.replaceAll(RegularExp,"");
			}
			else   if(   Flag   ==   "r"   ||   Flag   ==   "R"   )/*trim   right   side   only*/
			{
				String RegularExp =   "[　 ]+$";
				return   Str.replaceAll(RegularExp,"");
			}
			else/*defautly,   trim   both   left   and   right   side*/
			{
				String RegularExp =   "^[　 ]+|[　 ]+$";
				return   Str.replaceAll(RegularExp,"");
			}
		}
	}

	/**
	 * 去除左边空格
	 */
	public static String leftTrim(String str){
		if (str == null || str.equals("")) {
			return str;
		}else{
			str =   ""+str;
			String RegularExp =  "^[　 ]+";
			return   str.replaceAll(RegularExp,"");
		}

	}

	/**
	 * 去除右边空格
	 */
	public static String rightTrim(String str){
		if (str == null || str.equals("")) {
			return str;
		}else{
			str =   ""+str;
			String RegularExp =   "[　 ]+$";
			return   str.replaceAll(RegularExp,"");
		}

	}
}