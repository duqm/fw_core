package net.sanmuyao.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 数学计算
 * @author 杜庆明
 * 2014年11月13日
 *
 */
public class MathUtils {

	/**
	 * 应行家算法，四舍六入五成双
	 * 1. 被修约的数字等于或小于4时，该数字舍去；
	 * 2. 被修约的数字等于或大于6时，则进位；
	 * 3. 被修约的数字等于5时，要看5前面的数字，若是奇数则进位，若是偶数则将5舍掉，即修约后末尾数字都成为偶数；
	 *     若5的后面还有不为“0”的任何数，则此时无论5的前面是奇数还是偶数，均应进位。
	 * @param value 要转换的值
	 * @param precision 保留的小数位数
	 * @return
	 */
	public static Double bankerRound(Double input) {
    	return bankerRound(input, 2);
    }
	
	/**
	 * 应行家算法，四舍六入五成双
	 * 1. 被修约的数字等于或小于4时，该数字舍去；
	 * 2. 被修约的数字等于或大于6时，则进位；
	 * 3. 被修约的数字等于5时，要看5前面的数字，若是奇数则进位，若是偶数则将5舍掉，即修约后末尾数字都成为偶数；
	 *     若5的后面还有不为“0”的任何数，则此时无论5的前面是奇数还是偶数，均应进位。
	 * @param value 要转换的值
	 * @param precision 保留的小数位数
	 * @return
	 */
    public static Double bankerRound(Double input, int precision) {
    	if( input == Double.NaN) {
    		return 0.00;
    	}
    	try {
    		BigDecimal d = new BigDecimal(input);
    		BigDecimal val = d.setScale(precision, RoundingMode.HALF_EVEN);
    		return val.doubleValue();
    	} catch (Exception e) {
    		return 0.00;
    	}
    }
    
    /**
     * 四舍去五入
     * @param input
     * @param precision
     * @return
     */
    public static Double round(Double input, int precision) {
    	if( input == Double.NaN) {
    		return 0.00;
    	}
    	try {
    		BigDecimal d = new BigDecimal(input);
    		BigDecimal val = d.setScale(precision, RoundingMode.HALF_UP);
    		return val.doubleValue();
    	} catch (Exception e) {
    		return 0.00;
    	}
    }
    
    /**
     * 转成字符串，不舍入，直接截取
     * @param input
     * @param precision 要保留的小数位数
     * @return
     */
    public static String toString(Double input, int precision) {
    	
    	int i = 0;
    	String size = "1";
    	while(i < precision) {
    		size += "0";
    		i++;
    	}
    	
    	Double value = (input * Integer.parseInt(size));
    	Long longValue = value.longValue();
    	String strValue = longValue.toString();
    	int diff = precision - strValue.length();
    	while( diff >= 0 ) {
    		strValue = "0" + strValue;
    		diff--;
    	}
    	String val = strValue.substring(0, strValue.length() - precision) + "." + strValue.substring(strValue.length() - precision, strValue.length());
    	
    	
    	return val;
    }
        
    
}
