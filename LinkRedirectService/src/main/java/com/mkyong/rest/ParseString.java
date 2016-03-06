package com.mkyong.rest;



public class ParseString {
	/*public static void main(String[] args) throws IOException {
		String str = "[Mozilla/5.0 (Windows abc; CPU iPhone OS 8_3 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Mobile/12F70 [FBAN/MessengerForiOS;FBAV/49.0.0.22.53;FBBV/17680434;FBDV/iPhone6,1;FBMD/iPhone;FBSN/iPhone OS;FBSV/8.3;FBSS/2; FBCR/Verizon;FBID/phone;FBLC/en_US;FBOP/5]]";
		System.out.println(machinetype(str));
		str = "[24.130.92.115, 172.31.27.114]";
		// str="[24.130.92.115,26.2.0]";
		System.out.println(ip(str));
	}*/
	public static String shorturl(String str)
	{
		String result="";
		try{
		result= str.substring(str.lastIndexOf("/") + 1);
		}catch(Exception e){
			return " ";
		}
		return result;
	}

	
	public static String machinetype(String str) {
		try {
			// String str
			// ="URL RESPONSE: upgrade-insecure-requests : [1] URL RESPONSE: user-agent : [Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.73 Safari/537.36] URL RESPONSE: accept-encoding : [gzip, deflate, sdch] URL RESPONSE: accept-language : [en-US,en;q=0.8] URL RESPONSE:";
			// String str
			// ="[Mozilla/5.0 (iPhone; CPU iPhone OS 8_3 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Mobile/12F70 [FBAN/MessengerForiOS;FBAV/49.0.0.22.53;FBBV/17680434;FBDV/iPhone6,1;FBMD/iPhone;FBSN/iPhone OS;FBSV/8.3;FBSS/2; FBCR/Verizon;FBID/phone;FBLC/en_US;FBOP/5]]";
			String result = str.substring(str.indexOf("(") + 1,
					str.indexOf(";"));
			if (result.matches(".*\\s+.*")) {
				String res = result.substring(0, result.indexOf(" "));
				result = res;
			} else {
				result = result;
			}
			return result;
		} catch (Exception e) {
		}
		return null;
	}

	public static String ip(String str) {
		try {
			String result;
			if (str.contains(",")) {
				result = str.substring(str.indexOf("[") + 1, str.indexOf(","));
			} else {
				result = str.substring(str.indexOf("[") + 1, str.indexOf("]"));
			}
			return result;
		} catch (Exception e) {
		}
		return null;
	}
}
