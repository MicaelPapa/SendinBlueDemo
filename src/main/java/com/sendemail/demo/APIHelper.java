package com.sendemail.demo;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.context.annotation.Scope;

import com.sendemail.demo.entities.Contact;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
@Scope("singleton")
public class APIHelper {
	public static final String APPID_V3 = "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";
	public static final String APPID_V2 = "X5JNbgxcFa2RZWQs";

	public Response createEmailCampaign() {
		Response response = null;
		OkHttpClient client = new OkHttpClient();

		try {
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType,
					"{\"sender\":{\"name\":\"micael\",\"email\":\"micaelpapa@advenio.com.ar\"},\"inlineImageActivation\":false,\"sendAtBestTime\":false,\"abTesting\":false,\"ipWarmupEnable\":false,\"name\":\"x campaign\",\"htmlContent\":\"content test\",\"subject\":\"Soy el subject del testing\",\"replyTo\":\"micaelpapa@advenio.com.ar\",\"toField\":\"micaelpapa@advenio.com.ar\",\"header\":\"Soy el header del campaign\"}");
			Request request = new Request.Builder().url("https://api.sendinblue.com/v3/emailCampaigns").post(body)
					.addHeader("accept", "application/json").addHeader("content-type", "application/json")
					.addHeader("api-key",
							"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")
					.build();

			response = client.newCall(request).execute();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return response;
	}

	public Response sendEmailCampaign(int campaignId) {
		OkHttpClient client = new OkHttpClient();

		try {
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, "");
			Request request = new Request.Builder()
					.url("https://api.sendinblue.com/v3/emailCampaigns/" + campaignId + "/sendNow").post(body)
					.addHeader("accept", "application/json").addHeader("api-key", APPID_V3).build();

			return client.newCall(request).execute();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Response sendTestEmailCampaign(int campaignId, String emailReceipt) {
		OkHttpClient client = new OkHttpClient();
		Response response;

		try {
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, "{\"emailTo\":[\"" + emailReceipt + "\"]}");
			Request request = new Request.Builder().url("https://api.sendinblue.com/v3/emailCampaigns/3/sendTest")
					.post(body).addHeader("accept", "application/json").addHeader("content-type", "application/json")
					.addHeader("api-key", APPID_V3).build();

			response = client.newCall(request).execute();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return response;
	}

	public Response returnAllEmailCampaigns() {
		OkHttpClient client = new OkHttpClient();
		Request request;

		try {
			request = new Request.Builder().url("https://api.sendinblue.com/v3/emailCampaigns?limit=500&offset=0").get()
					.addHeader("accept", "application/json").addHeader("api-key", APPID_V3).build();

			return client.newCall(request).execute();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Response returnEmailCampaignById(Integer campaignId) {
		OkHttpClient client = new OkHttpClient();
		Request request;

		try {
			request = new Request.Builder().url("https://api.sendinblue.com/v3/emailCampaigns/" + campaignId).get()
					.addHeader("accept", "application/json").addHeader("api-key", APPID_V3).build();

			return client.newCall(request).execute();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Response deleteEmailCampaignById(Integer campaignId) {
		OkHttpClient client = new OkHttpClient();
		Request request;

		try {

			request = new Request.Builder().url("https://api.sendinblue.com/v3/emailCampaigns/" + campaignId)
					.delete(null).addHeader("accept", "application/json").addHeader("api-key", APPID_V3).build();

			return client.newCall(request).execute();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Response getAllTheContacts() {
		OkHttpClient client = new OkHttpClient();
		Request request;

		try {
			request = new Request.Builder().url("https://api.sendinblue.com/v3/contacts?limit=50&offset=0").get()
					.addHeader("accept", "application/json").addHeader("api-key", APPID_V3).build();

			return client.newCall(request).execute();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void deleteAllTheContacts(ArrayList<Contact> contacts) {
		for (Contact contact : contacts) {
			deleteContact(contact.getEmail());
		}
	}

	public void deleteContact(String email) {
		OkHttpClient client = new OkHttpClient();
		try {
			if (email.contains("@")) {
				email = email.replace("@", "%40");
			}

			Request request = new Request.Builder().url("https://api.sendinblue.com/v3/contacts/" + email).delete(null)
					.addHeader("accept", "application/json").addHeader("api-key", APPID_V3).build();
			client.newCall(request).execute();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	public void createContacts(ArrayList<Contact> contacts) {
		for (Contact contact : contacts) {
			createContact(contact);
		}
	}

	public void createContact(Contact contact) {
		OkHttpClient client = new OkHttpClient();
		Boolean flag = true;
		StringBuilder concat = new StringBuilder("{");
		try {
			ArrayList<Long> ids = contact.getListIds();
			if (ids != null && !ids.isEmpty()) {
				concat.append("\"listIds\":[");
				for (int i = 0; i < ids.size(); i++) {
					if (flag) {
						concat.append(ids.get(i));
						flag = false;
					} else {
						concat.append(",");
						concat.append(ids.get(i));
					}
				}
				concat.append("],\"updateEnabled\":true,\"email\":\"");
				concat.append(contact.getEmail());
				concat.append("\",\"emailBlacklisted\":false,\"smsBlacklisted\":false}");
			}
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, concat.toString());
			Request request = new Request.Builder().url("https://api.sendinblue.com/v3/contacts").post(body)
					.addHeader("accept", "application/json").addHeader("content-type", "application/json")
					.addHeader("api-key", APPID_V3).build();

			client.newCall(request).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}