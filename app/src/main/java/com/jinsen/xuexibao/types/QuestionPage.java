/**
 *@author jinsen47
 *
 *
 *
 */
package com.jinsen.xuexibao.types;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Administrator
 *
 */
public class QuestionPage {
	private String originString;
	private List<Question> questionList;
    private static final String BASEURL = "http://ops.xuexibao.cn/report3/audio/getAudioById.do?audioId=";
	
	public QuestionPage (String originString) {
		this.originString = originString;
        questionList = new LinkedList<Question>();
		
		Document document = Jsoup.parse(this.originString);
		Element table = document.select("tbody").first();
		Elements trs = table.select("tr");
		for (Element tr : trs) {
			Question question = new Question();
			question.audioId = tr.child(1).text();
            question.audioLinkUrl = BASEURL + question.audioId;
			question.questionId = tr.child(2).text();
			question.time = new Integer(tr.child(9).text()).intValue();
			question.listenTime = setListenTime(question.time);
			questionList.add(question);
		}
	}
	
	public Question getQuestion() {
		return questionList.remove(questionList.size() - 1);
	}
	
	public List getQuestionList() {
		return questionList;
	}

    public int getQuestionSize() {
        return questionList.size();
    }

	
	
	private int setListenTime(int time) {
//		int tmp = (int) (time * 0.8);
//		if ((tmp / 10) % 10 > 8 || (tmp / 10) % 10 < 3 ) 
		return (int) ((time * 0.8) / 10 * 10);
	}
	
}
