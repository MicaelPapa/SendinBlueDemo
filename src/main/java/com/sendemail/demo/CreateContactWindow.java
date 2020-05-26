package com.sendemail.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import com.sendemail.demo.entities.Contact;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
@Scope("prototype")
public class CreateContactWindow extends Dialog {
	private static final long serialVersionUID = 2653839633893487534L;

	@Autowired
	protected ApplicationContext applicationContext;
	protected TextField txtGmail;
	
	public CreateContactWindow() {
		setWidth("300px");
		setHeight("150px");
		open();
		init();
	}
	
	private void init() {
		txtGmail = new TextField("Gmail: ");
		
		Button btnSave = new Button("Save");
		btnSave.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
			private static final long serialVersionUID = 1L;
			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				if (txtGmail.getValue() == null || txtGmail.getValue().contentEquals("")) {
					Notification.show("Complete the gmail please");
					return;
				}
				
				APIHelper demoHelper = applicationContext.getBean(APIHelper.class);
				demoHelper.createContact(new Contact(txtGmail.getValue(),new Long(3)));
				close();
			}
		});
		
		Button btnClose = new Button("Close");
		btnClose.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
			private static final long serialVersionUID = -1473118915714292795L;
			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				close();
			}
		});
		
		VerticalLayout vl = new VerticalLayout();
		HorizontalLayout hl = new HorizontalLayout();
		hl.add(btnSave,btnClose);
		vl.add(txtGmail,hl);
		add(vl);
	}
}
