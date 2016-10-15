package com.whyq.util;

import java.util.List;

import org.apache.log4j.Logger;

import com.whyq.model.Receipt;
import com.whyq.model.Token;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class TokenUtils {
	static Logger log = Logger.getLogger(TokenUtils.class.getName());

	public void sendMessage(String contextObject) {
		log.info("in sendMessage " + contextObject);

		String URL = "https://api.gupshup.io/sm/api/bot/WhyQDraft/msg";
		String body = "context=" + contextObject + "&message=Payment complete. Sit Back and Relax Order is on the way.";

		System.out.println(body);
		try {
			okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
			okhttp3.MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
			RequestBody body1 = RequestBody.create(mediaType, body);
			okhttp3.Request request1 = new okhttp3.Request.Builder().url(URL)
					.header("apikey", "4f331fc14fb34579c52dd66a805ae1c8").post(body1).build();
			okhttp3.Response response = client.newCall(request1).execute();
			System.out.println(response.code());
			;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("End sendMessage " + contextObject);
	}

	public void sendTokenCarolMessage(String contextObject, String serverPath, List<Token> tokenList) {

		log.info("in sendTokenCarolMessage " + contextObject);
		String URL = "https://api.gupshup.io/sm/api/bot/WhyQDraft/msg";
		String body = "context=" + contextObject + "&message="
				+ BotUtils.tokenCoralView(tokenList, serverPath).toString();
		try {
			okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
			okhttp3.MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
			RequestBody body1 = RequestBody.create(mediaType, body);
			okhttp3.Request request1 = new okhttp3.Request.Builder().url(URL)
					.header("apikey", "4f331fc14fb34579c52dd66a805ae1c8").post(body1).build();
			okhttp3.Response response = client.newCall(request1).execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("End sendTokenCarolMessage " + contextObject);
	}

	public void sendReceipt(String contextObject, String serverPath, Receipt receipt) {
		log.info("in sendReceipt " + contextObject + " Order Num " + receipt.getOrderNumber());

		String URL = "https://api.gupshup.io/sm/api/bot/WhyQDraft/msg";
		String body = "context=" + contextObject + "&message=" + BotUtils.receiptTemplate(serverPath, receipt)
				+ "&bypass=true";

		System.out.println(body);
		try {
			okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
			okhttp3.MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
			RequestBody body1 = RequestBody.create(mediaType, body);
			okhttp3.Request request1 = new okhttp3.Request.Builder().url(URL)
					.header("apikey", "4f331fc14fb34579c52dd66a805ae1c8").post(body1).build();
			okhttp3.Response response = client.newCall(request1).execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("End sendReceipt " + contextObject + " Order Num " + receipt.getOrderNumber());
	}

}
