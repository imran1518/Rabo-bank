package com.rabobank.transaction.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;

@Component
@XmlRootElement(name = "records")
@XmlAccessorType (XmlAccessType.FIELD)
public class CustomerTransactions implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "record")
	private List<CustomerTransaction> customerTransaction;

	public CustomerTransactions() {

	}

	public List<CustomerTransaction> getCustomerTransaction() {
		return customerTransaction;
	}

	public void setCustomerStatement(List<CustomerTransaction> customerTransaction) {
		this.customerTransaction = customerTransaction;
	}

	@Override
	public String toString() {
		return "CustomerTransactions [CustomerTransaction=" + customerTransaction + "]";
	}

}
