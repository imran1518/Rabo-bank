package com.rabobank.transaction.model;

import java.util.List;

public class RbFailedTransactionResponse {

	private List<FailedTransaction> failedTransactions;

	public List<FailedTransaction> getFailedTransactions() {
		return failedTransactions;
	}

	public void setFailedTransactions(List<FailedTransaction> failedTransactions) {
		this.failedTransactions = failedTransactions;
	}
	
	@Override
	public String toString() {
		return "RbFailedTransactionResponse [FailedTransaction=" + failedTransactions + "]";
	}
}
