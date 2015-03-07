/**
 *@author jinsen47
 *
 *
 *
 */
package com.jinsen.xuexibao.types;


public class Question {
	public String questionId;
	public String audioId;
	public String audioFileUrl;
	public String audioLinkUrl;
	public int time;
	public int listenTime;
	
	
	public Question() {
		questionId = "";
		audioId = "";
		audioFileUrl = "";
		audioLinkUrl = "";
		
	}
	
	/* （非 Javadoc）
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO 自动生成的方法存根
		return "questionId=" + questionId + "," + 
			   "audioId=" + audioId + "," + 
		       "time=" + time + "," + 
			   "listenTime" + listenTime;
	}
	

	
	
}
