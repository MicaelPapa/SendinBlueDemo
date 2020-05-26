package com.sendemail.demo;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sendemail.demo.entities.Contact;
import com.squareup.okhttp.Response;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

@Route
@PWA(name = "Vaadin Application", shortName = "Vaadin App", description = "This is an example Vaadin application.", enableInstallPrompt = true)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {
	private static final long serialVersionUID = -3617572234996216193L;
	@Autowired
	protected ApplicationContext applicationContext;
	protected TextArea textArea;
	protected Label message;
	protected TextField textField;

	public MainView() {
		message = new Label();
		message.setText("");

		textArea = new TextArea();
		textArea.setValue("");
		textArea.setWidth("1400px");
		textArea.setHeight("300px");

		HorizontalLayout hl = new HorizontalLayout();

		textField = new TextField("Campaign ID");
		textField.setValue("5");

		hl.add(btnSendTestEmailCampaign(), btnGetAllTheCampaigns(), btnGetEmailCampaignById(), btnDeleteEmailCampaign(),
				btnSendEmailCampaign(), btnCreateEmailCampaign());

		VerticalLayout vl = new VerticalLayout();
		vl.add(message, hl, textField, textArea);

		HorizontalLayout hlContact = new HorizontalLayout();
		hlContact.add(btnRefreshContacts(), btnCreateContact(), btnGetAllTheContacts(), DeleteContact());

		vl.add(hlContact);

		this.add(vl);
	}

	private Button btnGetAllTheCampaigns() {
		Button btnGetCurrentCampaigns = new Button("Get all the campaigns");
		btnGetCurrentCampaigns.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
			private static final long serialVersionUID = 3855486305788443734L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				APIHelper demoHelper = applicationContext.getBean(APIHelper.class);
				Response response = demoHelper.returnAllEmailCampaigns();
				if (response == null) {
					textArea.setValue("");
					message.setText("Ha ocurrido un error");
					return;
				}

				if (!response.isSuccessful()) {
					textArea.setValue("");
					message.setText(response.message());
				} else {
					message.setText("");
					try {
						textArea.setValue(response.body().string());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		return btnGetCurrentCampaigns;
	}

	private Button btnSendTestEmailCampaign() {
		Button btnSendTestEmailCampaign = new Button("Send Test Email By ID");
		btnSendTestEmailCampaign.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				APIHelper demoHelper = applicationContext.getBean(APIHelper.class);
				Dialog d = new Dialog();
				d.setWidth("350px");
				d.setHeight("100px");

				TextField txtGmail = new TextField("Email");
				Button btnOk = new Button("Ok");
				btnOk.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
					private static final long serialVersionUID = 7890562319773899994L;

					@Override
					public void onComponentEvent(ClickEvent<Button> event) {
						if (textField.getValue() == null || textField.getValue().contentEquals("")) {
							Notification.show("Complete the campaign ID please");
							return;
						}
						if (txtGmail.getValue() == null || txtGmail.getValue().contentEquals("")) {
							Notification.show("Complete the gmail please");
							return;
						}

						Response response = demoHelper.sendTestEmailCampaign(Integer.parseInt(textField.getValue()),
								txtGmail.getValue());

						if (response == null) {
							textArea.setValue("");
							message.setText("Ha ocurrido un error");
							return;
						}

						if (!response.isSuccessful()) {
							textArea.setValue("");
							message.setText(response.message());
						} else {
							message.setText("");
							d.close();
							Notification.show("Check the email");
							try {
								textArea.setValue(response.body().string());
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				});

				Button btnClose = new Button("Cancel");
				btnClose.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
					private static final long serialVersionUID = 1L;

					@Override
					public void onComponentEvent(ClickEvent<Button> event) {
						d.close();
					}
				});

				d.add(txtGmail, btnOk, btnClose);
				d.open();
			}
		});
		return btnSendTestEmailCampaign;
	}

	private Button DeleteContact() {
		Button btnDeleteContact = new Button("Delete Contact");
		btnDeleteContact.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				APIHelper demoHelper = applicationContext.getBean(APIHelper.class);
				Dialog d = new Dialog();
				d.setWidth("350px");
				d.setHeight("100px");

				TextField txtGmail = new TextField("Email");
				Button btnOk = new Button("Ok");
				btnOk.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
					private static final long serialVersionUID = 7890562319773899994L;

					@Override
					public void onComponentEvent(ClickEvent<Button> event) {

						if (txtGmail.getValue() == null || txtGmail.getValue().contentEquals("")) {
							Notification.show("Complete the gmail please");
							return;
						}

						demoHelper.deleteContact(txtGmail.getValue());
						d.close();
					}
				});

				Button btnClose = new Button("Cancel");
				btnClose.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
					private static final long serialVersionUID = 1L;

					@Override
					public void onComponentEvent(ClickEvent<Button> event) {
						d.close();
					}
				});
				d.add(txtGmail, btnOk, btnClose);
				d.open();
			}
		});
		return btnDeleteContact;
	}

	private Button btnGetEmailCampaignById() {

		Button btnGetCampaignById = new Button("Get Email Campaign by ID");
		btnGetCampaignById.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			private static final long serialVersionUID = -4922629418642872950L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				APIHelper demoHelper = applicationContext.getBean(APIHelper.class);

				if (!validCampaignId())
					return;

				Response response = demoHelper.returnEmailCampaignById(Integer.parseInt(textField.getValue()));

				if (response == null) {
					textArea.setValue("");
					message.setText("Ha ocurrido un error");
					return;
				}

				if (!response.isSuccessful()) {
					textArea.setValue("");
				} else {
					message.setText("");
					try {
						textArea.setValue(response.body().string());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		return btnGetCampaignById;
	}

	private Button btnDeleteEmailCampaign() {
		Button btnDeleteEmailCampaign = new Button("Delete Email Campaign by ID");
		btnDeleteEmailCampaign.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
			private static final long serialVersionUID = -639935474002157762L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				APIHelper demoHelper = applicationContext.getBean(APIHelper.class);

				if (!validCampaignId())
					return;

				Response response = demoHelper.deleteEmailCampaignById(Integer.parseInt(textField.getValue()));

				if (response == null) {
					textArea.setValue("");
					message.setText("Ha ocurrido un error");
					return;
				}

				if (!response.isSuccessful()) {
					textArea.setValue("");
					if (response.message().contentEquals("Forbidden")) {
						message.setText(response.message()
								+ " (Esta prohibida la eliminacion de una campaña que ya se ha enviado)");
					} else {
						message.setText(response.message());
					}
				} else {
					message.setText("");
					try {
						textArea.setValue(response.body().string());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		return btnDeleteEmailCampaign;
	}

	private Boolean validCampaignId() {
		if (textField.getValue() == null || textField.getValue().contentEquals("")) {
			message.setText("Antes complete el id de la campaña en formato numerico");
			return false;
		}
		return true;
	}

	private Button btnSendEmailCampaign() {
		Button btnSendEmailCampaign = new Button("Send Email Campaign by ID");
		btnSendEmailCampaign.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			private static final long serialVersionUID = -639935474002157762L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				APIHelper demoHelper = applicationContext.getBean(APIHelper.class);

				if (!validCampaignId())
					return;

				Response response = demoHelper.sendEmailCampaign(Integer.parseInt(textField.getValue()));

				if (response == null) {
					textArea.setValue("");
					message.setText("Ha ocurrido un error");
					return;
				}

				if (!response.isSuccessful()) {
					textArea.setValue("");
					message.setText(response.message());
				} else {
					message.setText("");
					try {
						textArea.setValue(response.body().string());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		return btnSendEmailCampaign;
	}

	private Button btnCreateEmailCampaign() {
		Button btnCreate = new Button("Create Email Campaign");
		btnCreate.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
			private static final long serialVersionUID = -1892116707876827101L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				APIHelper demoHelper = applicationContext.getBean(APIHelper.class);
				Response response = demoHelper.createEmailCampaign();
				if (response == null) {
					message.setText("Ha ocurrido un error");
					return;
				}

				if (!response.isSuccessful()) {
					message.setText(response.message());
				} else {
					message.setText("");
				}
			}
		});
		return btnCreate;
	}

	private Button btnRefreshContacts() {
		Button btnRefresh = new Button("Refresh Contacts");
		btnRefresh.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				APIHelper demoHelper = applicationContext.getBean(APIHelper.class);
				ArrayList<Contact> contacts = getAllTheContacts();

				if (contacts == null) {
					textArea.setValue("");
					message.setText("Ha ocurrido un error");
					return;
				} else {
					// ESTA LOGICA SE HACE PORQUE EL PLAN FREE DE SENDINBLUE TIENE UNA CUOTA DE
					// PODER ENVIAR SOLO 3 EMAILS POR DIA A UN MISMO
					// CONTACTO SIEMPRE Y CUANDO CADA EMAIL SEA DE UNA campaña DIFERENTE, SI SE HACE
					// UN UPGRADE A UN PLAN DE PAGO SEGURAMENTE
					// LA CUOTA DE EMAILS A PODER ENVIAR A UN MISMO USUARIO SERA MAYOR
					demoHelper.deleteAllTheContacts(contacts);
					demoHelper.createContacts(contacts);
				}
			}
		});
		return btnRefresh;
	}

	private Button btnCreateContact() {
		Button btnCreateContact = new Button("Create Contact");
		btnCreateContact.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				APIHelper demoHelper = applicationContext.getBean(APIHelper.class);
				CreateContactWindow w = applicationContext.getBean(CreateContactWindow.class);
			}
		});
		return btnCreateContact;
	}

	private Button btnGetAllTheContacts() {
		Button btnGetAllTheContacts = new Button("Get All The Contacts");
		btnGetAllTheContacts.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			private static final long serialVersionUID = 5511096197061427412L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				textArea.setValue(getAllTheContacts().toString());
			}
		});
		return btnGetAllTheContacts;
	}

	private ArrayList<Contact> getAllTheContacts() {
		APIHelper demoHelper = applicationContext.getBean(APIHelper.class);
		Response response = demoHelper.getAllTheContacts();
		if (response == null)
			return null;
		else {
			if (!response.isSuccessful()) {
				return null;
			} else {
				JsonObject jsobject = new JsonObject();
				JsonArray jsarray = new JsonArray();

				try {
					jsobject = new JsonParser().parse(response.body().string()).getAsJsonObject();
					jsarray = jsobject.getAsJsonArray("contacts");
				} catch (Exception e1) {
					e1.printStackTrace();
					return null;
				}

				ArrayList<Contact> contacts = new ArrayList<Contact>();
				ObjectMapper objectMapper;
				JsonObject iteratorObject;

				for (int i = 0; i < jsarray.size(); i++) {
					try {
						iteratorObject = (JsonObject) jsarray.get(i);
						String a = iteratorObject.toString();

						objectMapper = new ObjectMapper();
						Contact c = objectMapper.readValue(a, Contact.class);
						contacts.add(c);

						// String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
						// Car car = objectMapper.readValue(json, Car.class);

					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}
				return contacts;
			}
		}
	}

}